package org.ioanntar.webproject.modules;

import lombok.ToString;
import org.ioanntar.webproject.database.entities.Game;
import org.ioanntar.webproject.database.entities.PlayerProps;
import org.ioanntar.webproject.logic.GameConnector;
import org.ioanntar.webproject.utils.JSONUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@ToString
public class Response {

    private final SimpMessagingTemplate template;
    private final Game game;

    public Response(Game game, SimpMessagingTemplate template) {
        this.game = game;
        this.template = template;
    }

    public void sendToPlayers(String destination, JSONObject response) {
        for (PlayerProps player: game.getPlayerProps())
            template.convertAndSendToUser(String.valueOf(player.getPlayerId()), "/game/" + destination, response.toString());
    }

    private void sendWithPosition(String destination, JSONObject response, List<PlayerProps> playerPropsList) {
        for (PlayerProps player: playerPropsList) {
            response.put("position", player.getPosition());
            template.convertAndSendToUser(String.valueOf(player.getPlayerId()), "/game/" + destination, response.toString());
        }
    }

    public void sendStart(String card) {
        JSONArray players = new GameConnector().bind(game).getJSONArray("players");
        JSONObject response = new JSONObject().put("common", card).put("current", game.getCurrent()).put("players", players);
        sendWithPosition("start", response, game.getPlayerProps());
    }

    public void sendToFinish(JSONArray ratings) {
        List<JSONObject> playersList = new LinkedList<>();
        List<PlayerProps> playerPropsList = game.getPlayerProps().stream().sorted(Comparator.comparing(e -> e.getPlayersDeck().size())).toList();

        for (PlayerProps player: playerPropsList) {
            JSONObject jsonObject = JSONUtils.getClient(player.getPlayer(), "name", "rating", "weight");

            JSONObject rating = ratings.getJSONObject(playerPropsList.indexOf(player));
            for (String key: rating.keySet())
                jsonObject.put(key, rating.get(key));

            playersList.add(jsonObject);
        }
        JSONObject response = new JSONObject().put("players", new JSONArray(playersList));
        sendWithPosition("finish", response, playerPropsList);
    }
}
