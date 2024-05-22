package org.ioanntar.webproject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ioanntar.webproject.database.utils.HibernateUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = MainController.class)
public class HttpControllerTest {

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
    void mainControllerEnterNotFound() throws Exception {
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
                .put("password", "121212");
        mockMvc.perform(post("/registration")
                        .param("data", jsonObject.toString()))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"status\":\"ok\"}"));
    }
}
