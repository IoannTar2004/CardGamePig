package org.ioanntar.webproject.database.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "player_props")
@Data
@NoArgsConstructor
@ToString(exclude = {"game", "player"})
public class PlayerProps {

    @Id
    @Column(name = "player_id")
    private long playerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(name = "position")
    private int position;

    @Column(name = "ready")
    private boolean ready = true;

    @OneToMany(mappedBy = "playerProps", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<PlayerCard> playersDeck = new LinkedList<>();

    @OneToOne()
    @JoinColumn(name = "player_id")
    private Player player;

    public PlayerProps(long playerId, Game game, int position) {
        this.playerId = playerId;
        this.game = game;
        this.position = position;
    }
}
