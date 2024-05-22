package org.ioanntar.webproject.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/*")
    public String start(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("gameId") != null)
            return "redirect:/templates/game.html";
        if (session.getAttribute("playerId") != null)
            return "redirect:/templates/home.html";
        return "redirect:/templates/index.html";
    }

    //TODO впоследствии уберу

    @GetMapping("/home")
    public String home() {
        return "redirect:/templates/home.html";
    }

    @GetMapping("/game")
    public String game() {
        return "redirect:/templates/game.html";
    }
}
