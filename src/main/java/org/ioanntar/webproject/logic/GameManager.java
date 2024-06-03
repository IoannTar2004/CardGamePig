package org.ioanntar.webproject.logic;

import lombok.Getter;
import org.ioanntar.webproject.database.entities.*;
import org.ioanntar.webproject.database.utils.Database;
import org.ioanntar.webproject.mbeans.LastHourPlayed;
import org.ioanntar.webproject.mbeans.MBeanManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.sql.Timestamp;
import java.util.*;

@Getter
public class GameManager {

    private final SimpMessageHeaderAccessor sha;
    private final SimpMessagingTemplate template;
    private final Game game;
    private final PlayerProps player;
    private final Database database = new Database();

    private static final LastHourPlayed lastHourPlayed = MBeanManager.getLastHourPlayed();

    public GameManager(SimpMessageHeaderAccessor sha, SimpMessagingTemplate template) {
        this.sha = sha;
        this.template = template;
        this.game = database.get(Game.class, (long) sha.getSessionAttributes().get("gameId"));
        this.player = database.get(PlayerProps.class, (long) sha.getSessionAttributes().get("playerId"));
    }

    public String start() {
        List<GameCard> gameDeck = GenerateDeck.generate(game);
        GameCard card = gameDeck.get(35);
        gameDeck.remove(35);
        card.setType(DeckType.COMMON);
        card.setPosition(0);
        game.getGameDecks().addAll(gameDeck);
        game.getGameDecks().add(card);

        return card.getCard();
    }

    public JSONObject click(int cardPosition) {
        GameCard card = game.getGameDecks().stream().filter(c -> c.getType() == DeckType.DISTRIBUTION && c.getPosition() == cardPosition)
                .findFirst().get();

        JSONObject jsonObject = new JSONObject().put("card", card.getCard()).put("cardPos", "card" + cardPosition);
        game.getGameDecks().remove(card);
        return jsonObject;
    }

    public JSONObject put(int playerPut, String card) {
        int put;

        if (playerPut == 3) {
            put = playerPut;
            int commonSize = game.getGameDecks().stream().filter(g -> g.getType() == DeckType.COMMON).toList().size();
            game.getGameDecks().add(new GameCard(game, DeckType.COMMON, card, commonSize));
        } else {
            put = (game.getCurrent() + playerPut) % game.getCount();
            PlayerProps player = game.getPlayerProps().stream().filter(p -> p.getPosition() == put).findFirst().get();

            long openedPosition = player.getPlayersDeck().stream().filter(p -> p.getType() == DeckType.OPENED).count();
            player.getPlayersDeck().add(new PlayerCard(player, card, DeckType.OPENED, (int) openedPosition));
        }

        if (playerPut == 0)
            game.setCurrent((game.getCurrent() + 1) % (game.getCount()));

        return new JSONObject().put("gamePos", put);
    }

    public JSONObject take() {
        List<PlayerCard> deck = new ArrayList<>(player.getPlayersDeck().stream().filter(c -> c.getType() == DeckType.OPENED).toList());
        deck.sort(Comparator.comparing(PlayerCard::getPosition));
        PlayerCard card = deck.get(deck.size() - 1);
        player.getPlayersDeck().remove(card);
        String subCard = deck.size() != 1 ? deck.get(deck.size() - 2).getCard() : "none";

        return new JSONObject().put("card", card.getCard()).put("subCard", subCard);
    }

    public void turn() {
        player.getPlayersDeck().forEach(p -> p.setType(DeckType.CLOSED));
    }

    public JSONObject clickOnPlayerDeck() {
        List<PlayerCard> closeCards = new ArrayList<>(player.getPlayersDeck().stream().filter(p -> p.getType() == DeckType.CLOSED).toList());
        PlayerCard card = closeCards.stream().min(Comparator.comparing(PlayerCard::getPosition)).get();
        player.getPlayersDeck().remove(card);

        return new JSONObject().put("card", card.getCard()).put("last", closeCards.size() - 1 == 0);
    }

    private JSONArray changeRating(Game game) {
        List<PlayerProps> playerPropsList = game.getPlayerProps().stream()
                .sorted(Comparator.comparing(e -> e.getPlayersDeck().size())).toList();

        List<JSONObject> objects = new LinkedList<>();
        for (PlayerProps player: playerPropsList) {
            JSONObject jsonObject = new JSONObject().put("position", player.getPosition()).put("lastRating", player.getPlayer().getRating());

            if (playerPropsList.indexOf(player) == playerPropsList.size() - 1)
                player.getPlayer().setRating(player.getPlayer().getRating() > 10 ? player.getPlayer().getRating() - 10 : 0);
            else
                player.getPlayer().setRating(player.getPlayer().getRating() + (16 - 16 * playerPropsList.indexOf(player)));

            objects.add(jsonObject);
        }
        return new JSONArray(objects);
    }

    public boolean isPlayerWins() {
        return player.getPlayersDeck().size() == 0 & game.getGameDecks().stream().noneMatch(c -> c.getType() == DeckType.DISTRIBUTION);
    }

    public JSONArray getPlayersStat() {
        JSONArray playerStat = changeRating(game);

        game.getGameDecks().clear();
        game.getPlayerProps().forEach(e -> {
            e.setReady(false);
            e.getPlayersDeck().clear();
            e.getPlayer().setLastGame(new Timestamp(System.currentTimeMillis()));
        });
        game.getPlayerProps().forEach(e -> e.getPlayersDeck().clear());
        lastHourPlayed.setPercents(database);

        return playerStat;
    }

    public boolean playersReadyAll() {
        return game.getPlayerProps().stream().allMatch(PlayerProps::isReady);
    }

    public boolean setPlayerReady() {
        player.setReady(true);
        return playersReadyAll();
    }

    public void commit() {
        database.commit();
    }
}