package org.ioanntar.webproject;

import org.ioanntar.webproject.database.utils.HibernateUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebApplication {
    public static void main(String[] args) {
        HibernateUtils.init();
        SpringApplication.run(WebApplication.class, args);
    }
}
