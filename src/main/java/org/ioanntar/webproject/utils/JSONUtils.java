package org.ioanntar.webproject.utils;

import org.ioanntar.webproject.database.entities.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JSONUtils {

    public static JSONObject getClient(Player player, String... keys) {
        Set<String> availableKeys = Set.of("playerId", "email", "name", "rating", "weight");
        Class<?> playerClass = player.getClass();

        Set<String> keySet = keys.length == 0 ? availableKeys : Arrays.stream(keys).collect(Collectors.toSet());
        JSONObject jsonObject = new JSONObject();
        for (String key : keySet) {
            try {
                Field field = playerClass.getDeclaredField(key);
                field.setAccessible(true);
                jsonObject.put(key, field.get(player));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return jsonObject;
    }

    public static JSONArray getClientsArray(List<Player> players, String... keys) {
        JSONArray array = new JSONArray();
        for (Player player: players) {
            JSONObject jsonObject = getClient(player, keys);
            array.put(jsonObject);
        }
        return array;
    }
}
