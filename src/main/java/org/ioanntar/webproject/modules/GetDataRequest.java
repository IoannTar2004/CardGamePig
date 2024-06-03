package org.ioanntar.webproject.modules;

import jakarta.servlet.http.HttpSession;
import lombok.NoArgsConstructor;
import org.ioanntar.webproject.config.RequestsStat;
import org.ioanntar.webproject.database.entities.Player;
import org.ioanntar.webproject.database.utils.Database;
import org.ioanntar.webproject.database.utils.DatabaseProvider;
import org.ioanntar.webproject.utils.JSONUtils;
import org.ioanntar.webproject.utils.PasswordHash;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@NoArgsConstructor
public class GetDataRequest {

    private JSONObject object;
    private final RequestsStat requestsStat = new RequestsStat();

    public GetDataRequest(JSONObject object) {
        this.object = object;
    }

    public JSONObject getClientData(long id) {
        Database database = new Database();

        Player player = database.get(Player.class, id);
        JSONObject jsonObject = JSONUtils.getClient(player);
        jsonObject.put("gameId", player.getPlayerProps() != null ? player.getPlayerProps().getGame().getId() : null);
        requestsStat.addRequest(player, RequestsStat.RequestMessages.ENTER);

        database.commit();
        return jsonObject;
    }

    public JSONObject clientEnter(HttpSession session) {
        Database database = new Database();
        String sha = new PasswordHash(object.getString("password")).hash("SHA-224");
        List<Player> players = database.getAll(Player.class);
        database.commit();

        String email = object.getString("email");
        Player player = players.stream().filter(p -> p.getEmail().equals(email)).findFirst().orElse(null);
        boolean pass = player != null & players.stream().anyMatch(p -> p.getPassword().equals(sha));

        JSONObject jsonObject = new JSONObject();
        if(player == null || !pass) {
            jsonObject.put("status", "not found");
            jsonObject.put("text", "Неверный логин или пароль!");
            return jsonObject;
        }

        session.setAttribute("playerId", player.getPlayerId());
        jsonObject.put("status", "ok");
        return jsonObject;
    }

    public JSONObject regClient(HttpSession session) {
        String sha = new PasswordHash(object.getString("password")).hash("SHA-224");
        String email = object.getString("email");
        Database database = new Database();

        List<Player> players = database.getAll(Player.class);
        JSONObject jsonObject = new JSONObject();
        if(players.stream().anyMatch(p -> p.getEmail().equals(email))) {
            jsonObject.put("status", "account exists");
            jsonObject.put("text", "Аккаунт с такой почтой уже есть!");
            return jsonObject;
        }
        Player player = new Player(object.getString("name"), email, sha, (int) object.getNumber("weight"));
        player = database.merge(player);
        requestsStat.addRequest(player, RequestsStat.RequestMessages.CHECK_IN);

        session.setAttribute("playerId", player.getPlayerId());
        jsonObject.put("status", "ok");

        database.commit();
        return jsonObject;
    }

    public JSONObject getClients(Comparator<Player> comparable, long playerId) {
        Database database = new Database();

        List<Player> players = database.getAll(Player.class).stream().sorted(comparable).toList();
        List<Player> resultList = players.stream().skip(players.size() - 13).limit(13).toList();
        List<Player> listToPlayer = getClientsWithPlayer(players, playerId);

        JSONObject response = new JSONObject()
                .put("top", JSONUtils.getClientsArray(resultList, "name", "weight", "rating", "lastGame"))
                .put("my", JSONUtils.getClientsArray(listToPlayer, "playerId", "name", "weight", "rating", "lastGame"))
                .put("index", resultList.indexOf(listToPlayer.get(listToPlayer.size() - 1)) + 1);
        database.commit();
        return response;
    }

    private List<Player> getClientsWithPlayer(List<Player> players, long playerId) {
        Player player = players.stream().filter(p -> p.getPlayerId() == playerId).findFirst().get();
        int index = players.indexOf(player);

        List<Player> resultList;
        if (players.size() - index >= 6)
            resultList = index >= 6 ? players.stream().skip(index - 6)
                    .limit(13).toList() : players.stream().limit(13).toList();
        else
            resultList = players.size() >= 13 ? players.stream().skip(players.size() - 13)
                    .limit(13).toList() : players.stream().limit(13).toList();

        return resultList;
    }
}