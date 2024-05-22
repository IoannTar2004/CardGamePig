package org.ioanntar.webproject.logic;

import jakarta.servlet.http.HttpSession;
import lombok.NoArgsConstructor;
import org.ioanntar.webproject.database.entities.Game;
import org.ioanntar.webproject.database.entities.Player;
import org.ioanntar.webproject.database.entities.PlayerProps;
import org.ioanntar.webproject.database.utils.Database;
import org.ioanntar.webproject.database.utils.DatabaseProvider;
import org.ioanntar.webproject.modules.Response;
import org.ioanntar.webproject.utils.JSONUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
public class GameConnector {

    public void create(HttpSession session, int count) {
        Database database = new Database();

        Game game = database.merge(new Game(count));
        Player player = database.get(Player.class, (long) session.getAttribute("playerId"));
        game.getPlayerProps().add(new PlayerProps(player.getPlayerId(), game, 0));

        database.commit();
        session.setAttribute("gameId", game.getId());
    }

    public Game exit(SimpMessageHeaderAccessor sha) {
        long playerId = (long) sha.getSessionAttributes().get("playerId");
        Database database = new Database();
        Game game = database.get(Game.class, (long) sha.getSessionAttributes().get("gameId"));

        Player player = database.get(Player.class, playerId);
        game.getPlayerProps().remove(player.getPlayerProps());

        if (game.getPlayerProps().size() == 0) {
            database.delete(game);
        }

        Game gameObject = game.fetchObjectFromProxy();
        database.commit();
        return gameObject;
    }

    public JSONObject bind(Game game) {
        List<JSONObject> playersList = new LinkedList<>();
        List<PlayerProps> playerPropsList = game.getPlayerProps();
        playerPropsList.sort(Comparator.comparing(PlayerProps::getPosition));

        for (PlayerProps playerProps: playerPropsList) {
            JSONObject jsonPlayers = JSONUtils.getClient(playerProps.getPlayer(), "name", "playerId");
            playersList.add(jsonPlayers);
        }
        for(int i = 0; i < game.getCount() - playerPropsList.size(); i++)
            playersList.add(null);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("players", new JSONArray(playersList)).put("gameId", game.getId());

        return jsonObject;
    }

    public String join(HttpSession session, long gameId) {
        Database database = new Database();

        Player player = database.get(Player.class, (long) session.getAttribute("playerId"));
        Game game = database.get(Game.class, gameId);

        JSONObject jsonObject = new JSONObject();
        if (game == null) {
            jsonObject.put("status", "not found");
            return jsonObject.toString();
        } else if (game.getPlayerProps().size() == game.getCount()) {
            jsonObject.put("status", "full");
            return jsonObject.toString();
        }

        game.getPlayerProps().add(new PlayerProps(player.getPlayerId(), game, game.getPlayerProps().size()));
        session.setAttribute("gameId", gameId);
        jsonObject.put("status", "ok");

        database.commit();
        return jsonObject.toString();
    }

    public Game connectToGame(String data, SimpMessageHeaderAccessor sha, SimpMessagingTemplate template) {
        JSONObject jsonObject = new JSONObject(data);
        Database database = new Database();
        PlayerProps player = database.get(PlayerProps.class, jsonObject.getLong("playerId"));

        sha.getSessionAttributes().put("playerId", player.getPlayerId());
        sha.getSessionAttributes().put("gameId", jsonObject.getLong("gameId"));

        Game game = player.getGame().fetchObjectFromProxy();
        database.commit();
        return game;
    }
}