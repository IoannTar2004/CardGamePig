package org.ioanntar.webproject.controllers;

import org.ioanntar.webproject.database.entities.Player;
import org.ioanntar.webproject.modules.GetDataRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Comparator;

@Controller
public class TableController {

    @Autowired
    SimpMessagingTemplate template;

    @MessageMapping("/getClients")
    public void getTable(SimpMessageHeaderAccessor sha) {
        JSONObject players = new GetDataRequest().getClients(Comparator.comparing(Player::getRating), Long.parseLong(sha.getUser().getName()));
        template.convertAndSendToUser(sha.getUser().getName(), "/home/getClients", players.toString());
    }

    @MessageMapping("/ratingIsChangedInform")
    @SendTo("/home/ratings")
    public String ratingIsChangedInform() {
        JSONArray topPlayers = new JSONArray();
        JSONObject jsonObject = new JSONObject().put("top", topPlayers);

        return jsonObject.toString();
    }
}
