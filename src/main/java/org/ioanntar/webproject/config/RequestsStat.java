package org.ioanntar.webproject.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ioanntar.webproject.database.entities.Player;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RequestsStat {

    @Getter
    private static final BlockingQueue<String> requests = new LinkedBlockingQueue<>();

    public void addRequest(Player player, RequestMessages msg) {
        JSONObject request = new JSONObject();
        request
                .put("Message", msg.getMessage())
                .put("id", player.getPlayerId())
                .put("name", player.getName())
                .put("email", player.getEmail())
                .put("time", new SimpleDateFormat("dd MM yyyy - hh:mm:ss").format(new Date().getTime()));

        requests.add(request.toString());
//        clearOldRequests();
    }

    public void clearOldRequests() {
        while (requests.size() > 50)
            requests.poll();
    }

    @AllArgsConstructor
    public enum RequestMessages {
        CHECK_IN("Зарегистрировался"),
        ENTER("Вошёл"),
        CREATE_GAME("Создал игру"),
        JOIN_GAME("Присоединился к игре"),
        EXIT_GAME("Вышел из игры"),
        LOG_OUT("Вышел из аккаунта");

        @Getter
        private final String message;
    }
}
