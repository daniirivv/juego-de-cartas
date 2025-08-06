package es.daniylorena.juegodecartas.state;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

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



}
