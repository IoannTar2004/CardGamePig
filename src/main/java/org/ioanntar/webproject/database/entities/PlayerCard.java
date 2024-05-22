package org.ioanntar.webproject.database.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "player_decks")
@ToString(exclude = "playerProps")
public class PlayerCard {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private PlayerProps playerProps;

    @Id
    @Column(name = "card")
    private String card;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private DeckType type;

    @Column(name = "position")
    private int position;

    public PlayerCard() {
    }

    public PlayerCard(PlayerProps playerProps, String card, DeckType type, int position) {
        this.playerProps = playerProps;
        this.card = card;
        this.type = type;
        this.position = position;
    }
}
