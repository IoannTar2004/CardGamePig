package org.ioanntar.webproject.controllers;

import org.ioanntar.webproject.database.utils.HibernateUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = MainController.class)
public class GameTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void init() {
        HibernateUtils.init();
    }

    @Test
    void createGame() throws Exception {
        JSONObject player = new JSONObject()
                .put("email", "ivan@ya.ru")
                .put("password", "121212");
        mockMvc.perform(post("/enter").param("data", player.toString()));
        mockMvc.perform(post("/createGame").param("data", new JSONObject().put("count", 2).toString()));
    }
}
