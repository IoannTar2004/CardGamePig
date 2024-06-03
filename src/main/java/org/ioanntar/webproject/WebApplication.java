package org.ioanntar.webproject;

import org.ioanntar.webproject.config.RequestsStat;
import org.ioanntar.webproject.database.entities.Player;
import org.ioanntar.webproject.database.utils.Database;
import org.ioanntar.webproject.database.utils.HibernateUtils;
import org.ioanntar.webproject.mbeans.MBeanManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

@SpringBootApplication
public class WebApplication {
    public static void main(String[] args) {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = new ObjectName("org.ioanntar.webproject.mbeans:type=CreatedGames");
            ObjectName name1 = new ObjectName("org.ioanntar.webproject.mbeans:type=LastHourPlayed");
            mbs.registerMBean(MBeanManager.getCreatedGames(), name);
            mbs.registerMBean(MBeanManager.getLastHourPlayed(), name1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HibernateUtils.init();
        new Thread(new FakeRequests()).start();
        SpringApplication.run(WebApplication.class, args);
    }

    static class FakeRequests implements Runnable {
        private final RequestsStat requestsStat = new RequestsStat();

        @Override
        public void run() {
            Database database = new Database();
            Player player = database.get(Player.class, 1);
            while (true) {
                for (int i = 0; i < 500; i++)
                    requestsStat.addRequest(player, RequestsStat.RequestMessages.values()[(int) (Math.random() * 6)]);
                try {
                    System.out.println(RequestsStat.getRequests().size());
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
