package es.daniylorena.juegodecartas.state;

import es.daniylorena.juegodecartas.display.GameDisplay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoleAssigner {

    private static List<Role> roles;

    private RoleAssigner() {
        throw new UnsupportedOperationException("Utility Class");
    }

    public static void initializeRoles(int players) {
        if (players == GameDisplay.MIN_PLAYERS) {
            roles = Arrays.asList(Role.PRESI, Role.NEUTRO, Role.CULO);
        } else {
            roles = new ArrayList<>(players);
            roles.add(Role.PRESI);
            roles.add(Role.VICEPRESI);
            int i = 2;
            while (i < players - 2) {
                roles.add(Role.NEUTRO);
                i++;
            }
            roles.add(Role.VICECULO);
            roles.add(Role.CULO);
        }
    }

    public static void assignRole(Player player) {
        if(roles != null && !roles.isEmpty()){
            Role role = roles.removeFirst();
            player.setRole(role);
        } else throw new IllegalStateException("Roles no inicializados");
    }

    public static List<Role> getRolesList(){
        return roles;
    }

    public static void deleteRoleList(){
        roles = null;
    }
}
