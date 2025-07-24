package es.daniylorena.juegodecartas.state;

import es.daniylorena.juegodecartas.display.GameDisplay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoleAssigner {

    private List<Role> roles;


    public void initializeRoles(int players) {
        if (players == GameDisplay.MIN_PLAYERS) {
            this.roles = Arrays.asList(Role.PRESI, Role.NEUTRO, Role.CULO);
        } else {
            this.roles = new ArrayList<>(players);
            this.roles.add(Role.PRESI);
            this.roles.add(Role.VICEPRESI);
            for (int i = 2; i < players - 2; i++) this.roles.add(Role.NEUTRO);
            this.roles.add(Role.VICECULO);
            this.roles.add(Role.CULO);
        }
    }

    public void assignRole(Player player) {
        Role role = this.roles.removeFirst();
        player.setRole(role);
    }
}
