package es.daniylorena.juegodecartas.state;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoleAssignerTest {

    @AfterEach
    void cleanRoleList(){
        RoleAssigner.deleteRoleList();
    }

    @Test
    void initializeRoles(){
        int numPlayers = 5;

        RoleAssigner.initializeRoles(numPlayers);

        List<Role> roleList = RoleAssigner.getRolesList();
        assertEquals(numPlayers, roleList.size());

        assertTrue(roleList.remove(Role.PRESI) && roleList.remove(Role.CULO));
        assertFalse(roleList.contains(Role.PRESI) && roleList.contains(Role.CULO));

        int i = numPlayers - 4; // PRESI CULO VICE x2
        while(i > 0){
            assertTrue(roleList.remove(Role.NEUTRO));
            i--;
        }
        assertFalse(roleList.contains(Role.NEUTRO));
    }

    @Test
    void assignRoles(){
        Role roleToAssign = Role.PRESI;
        List<Role> roleList = new ArrayList<>(List.of(roleToAssign));
        RoleAssigner.setRoles(roleList);
        int roles = RoleAssigner.getRolesList().size();
        Player player = new Player("Player 1");

        RoleAssigner.assignRole(player);

        assertEquals(roleToAssign, player.getRole());
        assertEquals(roles-1, RoleAssigner.getRolesList().size());
    }

}
