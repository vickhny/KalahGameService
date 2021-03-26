package io.assignment.kalah.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlayerTest {

    @Test
    public void testHouseIndex() {
        final Player player1 = Player.PLAYER_A;
        final Player player2 = Player.PLAYER_B;

        Assert.assertEquals(7, player1.getHouseIndex());
        Assert.assertEquals(14, player2.getHouseIndex());
    }
}
