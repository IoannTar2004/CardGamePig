package org.ioanntar.webproject.database.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "game_decks")
@ToString(exclude = "game")
public class GameCard {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    @Id
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private DeckType type;

    @Id
    @Column(name = "card")
    private String card;

    @Column(name = "position")
    private int position;

    public GameCard() {}

    public GameCard(Game game, DeckType type, String card, int position) {
        this.game = game;
        this.type = type;
        this.card = card;
        this.position = position;
    }
}
