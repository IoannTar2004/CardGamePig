package org.ioanntar.webproject.logic;

import org.ioanntar.webproject.database.entities.DeckType;
import org.ioanntar.webproject.database.entities.Game;
import org.ioanntar.webproject.database.entities.GameCard;

import java.util.LinkedList;
import java.util.List;

public class GenerateDeck {
    public static List<GameCard> generate(Game game) {
        String[] suits = {"spades", "clubs", "hearts", "diamonds"};
        String[] dignities = {"6", "7", "8", "9", "10", "j", "q", "k", "a"};
        List<String> cards = new LinkedList<>();
        for (String suit: suits) {
            for (String dignity: dignities) {
                cards.add(suit + "/" + dignity);
            }
        }
        List<GameCard> gameDecks = new LinkedList<>();
        for (int i = 0; i < 36; i++) {
            int j = (int) Math.round(Math.random() * (35 - i));
            GameCard card = new GameCard(game, DeckType.DISTRIBUTION, cards.get(j), i);
            gameDecks.add(card);
            cards.remove(j);
        }
        return gameDecks;
    }
}
