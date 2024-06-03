package org.ioanntar.webproject.controllers;

import org.ioanntar.webproject.database.entities.Game;
import org.ioanntar.webproject.database.entities.GameCard;
import org.ioanntar.webproject.database.utils.HibernateUtils;
import org.ioanntar.webproject.logic.GenerateDeck;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

public class GameTest {

    @BeforeAll
    static void init() {
        HibernateUtils.init();
    }

    @Test
    void gameDeck() {
        Game game = new Game(2);
        game.setId(1);
        List<GameCard> deck = GenerateDeck.generate(game);
        assert deck.size() == 36;
    }
}
