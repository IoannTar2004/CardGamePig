package org.ioanntar.webproject.mbeans;

import org.ioanntar.webproject.database.entities.Player;
import org.ioanntar.webproject.database.utils.Database;

import java.util.List;

public class LastHourPlayed implements LastHourPlayedMBean {

    private float percents;

    public void setPercents(Database database) {
        List<Player> players = database.getAll(Player.class);
        long count = players.stream().filter(p -> (System.currentTimeMillis() - p.getLastGame().getTime()) / 1000 < 3600).count();
        this.percents = (float) count / players.size() * 100;
    }

    @Override
    public float getPercents() {
        return percents;
    }

    @Override
    public void refresh() {
        Database database = new Database();
        setPercents(database);
        database.commit();
        System.out.println(percents);
    }
}
