package org.ioanntar.webproject.database.utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.Statistics;
import org.ioanntar.webproject.database.entities.*;

import java.util.LinkedList;
import java.util.List;

public final class HibernateUtils {

    private static SessionFactory sessionFactory;

    public static void init() {
        LinkedList<Class<?>> classList = new LinkedList<>(List.of(Game.class, Player.class, GameCard.class, PlayerCard.class, PlayerProps.class));
        try {
                Configuration configuration = new Configuration().configure();
                for (Class<?> eClass: classList)
                    configuration.addAnnotatedClass(eClass);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());

            Statistics stats = sessionFactory.getStatistics();
            stats.setStatisticsEnabled(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private HibernateUtils() {}
}
