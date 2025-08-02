package es.daniylorena.juegodecartas.state;

import es.daniylorena.juegodecartas.display.GameDisplay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoleAssigner {

    private static List<Role> roles;


    public static void initializeRoles(int players) {
        if (players == GameDisplay.MIN_PLAYERS) {
            roles = Arrays.asList(Role.PRESI, Role.NEUTRO, Role.CULO);
        } else {
            roles = new ArrayList<>(players);
            roles.add(Role.PRESI);
            roles.add(Role.VICEPRESI);
            for (int i = 2; i < players - 2; i++){
                roles.add(Role.NEUTRO);
            }
            roles.add(Role.VICECULO);
            roles.add(Role.CULO);
        }
    }

    public static void assignRole(Player player) {
        Role role = roles.removeFirst();
        player.setRole(role);
    }
}
