package org.ioanntar.webproject.controllers;

import org.ioanntar.webproject.database.entities.*;
import org.ioanntar.webproject.logic.GameConnector;
import org.ioanntar.webproject.logic.GameManager;
import org.ioanntar.webproject.modules.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/le")
    public void le(SimpMessageHeaderAccessor sha) {
        System.out.println(sha);
    }

    @MessageMapping("/connect")
    public void bind(@Payload String data, SimpMessageHeaderAccessor sha) {
        GameConnector gameConnector = new GameConnector();
        Game game = gameConnector.connectToGame(data, sha, template);
        JSONObject players = gameConnector.bind(game);

        new Response(game, template).sendToPlayers("connectedPlayers", players);
    }

    @MessageMapping("/disconnect")
    public void disconnect(SimpMessageHeaderAccessor sha) {
        GameConnector gameConnector = new GameConnector();
        Game game = gameConnector.exit(sha);
        JSONObject players = gameConnector.bind(game);

        new Response(game, template).sendToPlayers("connectedPlayers", players);
    }

    @MessageMapping("/start")
    public void start(SimpMessageHeaderAccessor sha) {
        GameManager gameManager = new GameManager(sha, template);
        String card = gameManager.start();

        new Response(gameManager.getGame(), template).sendStart(card);
        gameManager.commit();
    }

    @MessageMapping("/click")
    public void click(@Payload String data, SimpMessageHeaderAccessor sha) {
        JSONObject jsonObject = new JSONObject(data);
        GameManager gameManager = new GameManager(sha, template);
        Game game = gameManager.getGame();
        JSONObject response = gameManager.click(jsonObject.getInt("cardPos"));

        new Response(game, template).sendToPlayers("click", response);
        gameManager.commit();
    }

    @MessageMapping("/put")
    public void put(@Payload String data, SimpMessageHeaderAccessor sha) {
        JSONObject jsonObject = new JSONObject(data);
        GameManager gameManager = new GameManager(sha, template);
        JSONObject gamePos = gameManager.put(jsonObject.getInt("player"), jsonObject.getString("card"));

        Response response = new Response(gameManager.getGame(), template);
        response.sendToPlayers("put", gamePos);

        if (jsonObject.getInt("player") != 0 && gameManager.isPlayerWins())
            response.sendToFinish(gameManager.getPlayersStat());

        gameManager.commit();
    }

    @MessageMapping("/take")
    public void take(SimpMessageHeaderAccessor sha) {
        GameManager gameManager = new GameManager(sha, template);
        JSONObject response = gameManager.take();
        new Response(gameManager.getGame(), template).sendToPlayers("take", response);
        gameManager.commit();
    }

    @MessageMapping("/turn")
    public void turn(SimpMessageHeaderAccessor sha) {
        GameManager gameManager = new GameManager(sha, template);
        gameManager.turn();
        new Response(gameManager.getGame(), template).sendToPlayers("turn", new JSONObject());
        gameManager.commit();
    }

    @MessageMapping("/clickOnPlayerDeck")
    public void clickOnPlayerDeck(SimpMessageHeaderAccessor sha) {
        GameManager gameManager = new GameManager(sha, template);
        JSONObject response = gameManager.clickOnPlayerDeck();
        new Response(gameManager.getGame(), template).sendToPlayers("clickOnPlayerDeck", response);
        gameManager.commit();
    }

    //Конец игры

    @MessageMapping("/clickOnPig")
    public void clickOnPig(SimpMessageHeaderAccessor sha) {
        GameManager gameManager = new GameManager(sha, template);
        new Response(gameManager.getGame(), template).sendToPlayers("pigSound",
                new JSONObject().put("position", gameManager.getPlayer().getPosition()));
        gameManager.commit();
    }

    @MessageMapping("/checkReadyPlayers")
    public void punishEnough(SimpMessageHeaderAccessor sha) {
        GameManager gameManager = new GameManager(sha, template);
        if (gameManager.setPlayerReady())
            new Response(gameManager.getGame(), template).sendToPlayers("punishEnough", new JSONObject());
        gameManager.commit();
    }

    @MessageMapping("/continue")
    public void reload(SimpMessageHeaderAccessor sha) {
        GameManager gameManager = new GameManager(sha, template);
        if (gameManager.playersReadyAll())
            new Response(gameManager.getGame(), template).sendToPlayers("reload", new JSONObject());
        gameManager.commit();
    }
}
