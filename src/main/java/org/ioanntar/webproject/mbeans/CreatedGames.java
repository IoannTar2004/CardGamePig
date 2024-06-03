package org.ioanntar.webproject.mbeans;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

public class CreatedGames extends NotificationBroadcasterSupport implements CreatedGamesMBean {

    private int gameCreated = 0;

    public void gameCreateNotify() {
        gameCreated++;
        if (gameCreated % 5 == 0 && gameCreated != 0) {
            Notification notification = new Notification("Created games", this, 0,
                    "Created games: " + gameCreated);
            sendNotification(notification);
        }
    }

    @Override
    public int getCreatedGames() {
        return gameCreated;
    }
}
