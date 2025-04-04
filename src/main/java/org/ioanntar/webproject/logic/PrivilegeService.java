package org.ioanntar.webproject.logic;

import org.ioanntar.webproject.database.entities.Player;
import org.ioanntar.webproject.database.utils.Database;
import org.json.JSONObject;

import java.util.List;

public class PrivilegeService {

    public PrivilegeService(String password) {
        if (!password.equals("admin"))              // я знаю, что так делать плохо!
            throw new RuntimeException();
    }

    public void removeTestPlayer(String email) {
        Database database = new Database();
        Player player = database.getAll(Player.class).stream().filter(e -> e.getEmail().equals(email)).findFirst().orElseThrow();
        database.delete(player);
        database.commit();
    }
}
