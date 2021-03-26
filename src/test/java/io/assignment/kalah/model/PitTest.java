package io.assignment.kalah.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PitTest {

    @Value("${kalah.pit.stones:6}")
    private int pitStoneCount;

    @Test
    public void testDistributable() {
        final Pit givenPit1 = new Pit(1, pitStoneCount);
        final Pit givenPit2 = new Pit(14, pitStoneCount);
        final Pit givenPit3 = new Pit(7, pitStoneCount);

        Assert.assertTrue(givenPit1.isDistributable(Player.PLAYER_A));
        Assert.assertTrue(givenPit2.isDistributable(Player.PLAYER_B));
        Assert.assertFalse(givenPit2.isDistributable(Player.PLAYER_A));
        Assert.assertFalse(givenPit3.isDistributable(Player.PLAYER_B));
    }

    @Test
    public void testHouse() {
        final Pit givenPit1 = new Pit(7, pitStoneCount);
        final Pit givenPit2 = new Pit(14, pitStoneCount);
        final Pit givenPit3 = new Pit(3, pitStoneCount);
        final Pit givenPit4 = new Pit(9, pitStoneCount);

        Assert.assertTrue(givenPit1.isHouse());
        Assert.assertTrue(givenPit2.isHouse());
        Assert.assertFalse(givenPit3.isHouse());
        Assert.assertFalse(givenPit4.isHouse());
    }

    @Test
    public void testInitialization() {
        final Pit givenPit1 = new Pit(1, pitStoneCount);
        final Pit givenPit2 = new Pit(14, pitStoneCount);
        final Pit givenPit3 = new Pit(7, pitStoneCount);

        Assert.assertEquals(1, givenPit1.getId());
        Assert.assertEquals(14, givenPit2.getId());
        Assert.assertEquals(7, givenPit3.getId());
    }

    @Test
    public void testInitialStoneCount() {
        final Pit givenPit1 = new Pit(1, pitStoneCount);
        final Pit givenPit2 = new Pit(7, pitStoneCount);

        Assert.assertEquals(6, givenPit1.getStoneCount());
        Assert.assertEquals(0, givenPit2.getStoneCount());
    }

    @Test
    public void testOwner() {
        final Pit givenPit1 = new Pit(4, pitStoneCount);
        final Pit givenPit2 = new Pit(7, pitStoneCount);
        final Pit givenPit3 = new Pit(10, pitStoneCount);
        final Pit givenPit4 = new Pit(14, pitStoneCount);

        Assert.assertEquals(Player.PLAYER_A, givenPit1.getPlayerTurn());
        Assert.assertEquals(Player.PLAYER_A, givenPit2.getPlayerTurn());
        Assert.assertEquals(Player.PLAYER_B, givenPit3.getPlayerTurn());
        Assert.assertEquals(Player.PLAYER_B, givenPit4.getPlayerTurn());
    }

    @Test
    public void testStoneCountSet() {
        final Pit givenPit = new Pit(1, pitStoneCount);
        givenPit.setStoneCount(7);

        Assert.assertEquals(7, givenPit.getStoneCount());
    }
}
