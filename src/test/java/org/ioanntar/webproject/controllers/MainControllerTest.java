package org.ioanntar.webproject.controllers;

import org.ioanntar.webproject.database.entities.Player;
import org.ioanntar.webproject.database.utils.Database;
import org.ioanntar.webproject.database.utils.HibernateUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = MainController.class)
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void init() {
        HibernateUtils.init();
    }

    @Test
    void mainControllerEnterOk() throws Exception {
        JSONObject jsonObject = new JSONObject()
                .put("email", "ivan@ya.ru")
                .put("password", "121212");
        mockMvc.perform(post("/enter").param("data", jsonObject.toString()))
                .andExpect(MockMvcResultMatchers.content().string("{\"status\":\"ok\"}"));
    }

    @Test
    void mainControllerEnterFail() throws Exception {
        JSONObject jsonObject = new JSONObject()
                .put("email", "ivan@ya.ru")
                .put("password", "12121");
        mockMvc.perform(post("/enter").param("data", jsonObject.toString()))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"text\":\"Неверный логин или пароль!\",\"status\":\"not found\"}"));
    }

    @Test
    void mainControllerRegistrOk() throws Exception {
        JSONObject jsonObject = new JSONObject()
                .put("email", "new@ya.ru")
                .put("name", "Kolya")
                .put("weight", 78)
                .put("password", "121212");
        mockMvc.perform(post("/registration").param("data", jsonObject.toString()))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"status\":\"ok\"}"));

        Database database = new Database();
        List<Player> players = database.getAll(Player.class);
        Player player = players.stream().filter(p -> p.getEmail().equals("new@ya.ru")).findFirst().orElse(null);
        if (player != null)
            database.delete(player);
        database.commit();
    }

    @Test
    void mainControllerRegistrFail() throws Exception {
        String expect = "{\"text\":\"Аккаунт с такой почтой уже есть!\",\"status\":\"account exists\"}";
        JSONObject jsonObject = new JSONObject()
                .put("email", "ivan@ya.ru")
                .put("name", "ivan")
                .put("weight", 78)
                .put("password", "121212");
        mockMvc.perform(post("/registration").param("data", jsonObject.toString()))
                .andExpect(MockMvcResultMatchers.content().string(expect));
    }
}
