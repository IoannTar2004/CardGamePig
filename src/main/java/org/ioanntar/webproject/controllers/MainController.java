package org.ioanntar.webproject.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.ioanntar.webproject.config.RequestsStat;
import org.ioanntar.webproject.database.entities.Player;
import org.ioanntar.webproject.database.utils.Database;
import org.ioanntar.webproject.logic.GameConnector;
import org.ioanntar.webproject.mbeans.CreatedGames;
import org.ioanntar.webproject.mbeans.MBeanManager;
import org.ioanntar.webproject.modules.GetDataRequest;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
public class MainController {

    private static final CreatedGames createdGames = MBeanManager.getCreatedGames();

    @PostMapping("/registration")
    public String registration(@RequestParam(name = "data") String data, HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession();
        GetDataRequest request = new GetDataRequest(new JSONObject(data));

        return request.regClient(session).toString();
    }

    @PostMapping("/enter")
    public String enter(@RequestParam(name = "data") String data, HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession();
        GetDataRequest request = new GetDataRequest(new JSONObject(data));
        JSONObject response = request.clientEnter(session);

        return response.toString();
    }

    @GetMapping("/get_client")
    public String getClient(HttpServletRequest request) {
        HttpSession session = request.getSession();
        GetDataRequest getDataRequest = new GetDataRequest();
        JSONObject response = getDataRequest.getClientData((long) session.getAttribute("playerId"));

        return response.toString();
    }

    @GetMapping("/exit_home")
    public void exitHome(HttpServletRequest request) {
        HttpSession session = request.getSession();
        long id = (long) session.getAttribute("playerId");
        Database database = new Database();
        new RequestsStat().addRequest(database.get(Player.class, id), RequestsStat.RequestMessages.LOG_OUT);
        database.commit();

        session.removeAttribute("playerId");
    }

    @PostMapping("/createGame")
    public void createGame(@RequestParam(name = "data") String data, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject(data);
        GameConnector gameConnector = new GameConnector();
        createdGames.gameCreateNotify();
        gameConnector.create(request.getSession(), jsonObject.getInt("count"));
    }

    @GetMapping("/exit_game")
    public void exitGame(HttpServletRequest request) {
        HttpSession session = request.getSession();
        long id = (long) session.getAttribute("playerId");
        Database database = new Database();
        new RequestsStat().addRequest(database.get(Player.class, id), RequestsStat.RequestMessages.EXIT_GAME);
        database.commit();

        session.removeAttribute("gameId");
    }

    @PostMapping("/joinGame")
    public String joinGame(@RequestParam(name = "data") String data, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject(data);
        GameConnector gameConnector = new GameConnector();
        return gameConnector.join(request.getSession(), jsonObject.getLong("gameId"));
    }
}
