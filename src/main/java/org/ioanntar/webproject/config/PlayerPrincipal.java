package org.ioanntar.webproject.config;

import lombok.ToString;

import java.security.Principal;

@ToString
public class PlayerPrincipal implements Principal {
    private String name;

    public PlayerPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
