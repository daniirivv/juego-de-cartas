package es.daniylorena.juegodecartas.logic;

import es.daniylorena.juegodecartas.display.GameDisplay;
import es.daniylorena.juegodecartas.state.Game;
import es.daniylorena.juegodecartas.state.Player;
import es.daniylorena.juegodecartas.state.Role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RoleAssigner {

    private Game game;
    private List<Role> roles;

    public RoleAssigner(int players) {
        if(players == GameDisplay.MIN_PLAYERS){
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

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Role assignRole(Player player) {
        Role role = this.roles.removeFirst();
        player.setRole(role);
        return role;
    }
}
