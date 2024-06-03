package org.ioanntar.webproject.mbeans;

import lombok.Getter;

public class MBeanManager {
    private static final CreatedGames createdGames = new CreatedGames();
    private static final LastHourPlayed lastHourPlayed = new LastHourPlayed();

    public static CreatedGames getCreatedGames() {
        return createdGames;
    }

    public static LastHourPlayed getLastHourPlayed() {
        return lastHourPlayed;
    }
}
