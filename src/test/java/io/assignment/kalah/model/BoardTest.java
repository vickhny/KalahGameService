package io.assignment.kalah.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BoardTest {

    @Value("${kalah.pit.stones:6}")
    private int pitStoneCount;

    @Test
    public void testGetPit() {
        final KalahBoard board = new KalahBoard(pitStoneCount);
        final Pit pit = board.getPit(4);

        Assert.assertNotNull(pit);
        Assert.assertEquals(4, pit.getId());
    }

    @Test
    public void testInitialization() {
        final KalahBoard board = new KalahBoard(pitStoneCount);

        Assert.assertNotNull(board.getPits());
        Assert.assertEquals(KalahBoard.PIT_END_INDEX, board.getPits().size());
    }

    @Test
    public void testStoneCount() {
        final KalahBoard board1 = new KalahBoard(pitStoneCount);
        final KalahBoard board2 = new KalahBoard(pitStoneCount);
        board2.getPit(5).setStoneCount(0);
        board2.getPit(11).setStoneCount(9);

        Assert.assertEquals(36, board1.getStoneCount(Player.PLAYER_A, true));
        Assert.assertEquals(30, board2.getStoneCount(Player.PLAYER_A, true));
        Assert.assertEquals(39, board2.getStoneCount(Player.PLAYER_B, true));
    }
}
