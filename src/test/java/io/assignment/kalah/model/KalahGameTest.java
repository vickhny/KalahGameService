package io.assignment.kalah.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KalahGameTest {

    @Value("${kalah.pit.stones:6}")
    private int pitStoneCount;

    @Test
    public void testInitialization() {
        final KalahGame givenGame = new KalahGame(pitStoneCount);

        Assert.assertNotNull(givenGame.getBoard());
        Assert.assertNull(givenGame.getTurn());
        Assert.assertNull(givenGame.getWinner());
    }

    @Test
    public void testWinner() {
        final KalahGame game = new KalahGame(pitStoneCount);
        game.setWinner(Player.PLAYER_A);
        Assert.assertEquals(Player.PLAYER_A, game.getWinner());

        game.setWinner(Player.PLAYER_B);
        Assert.assertEquals(Player.PLAYER_B, game.getWinner());
    }

    @Test
    public void testTurn() {
        final KalahGame game = new KalahGame(pitStoneCount);
        game.setTurn(Player.PLAYER_A);
        Assert.assertEquals(Player.PLAYER_A, game.getTurn());

        game.setTurn(Player.PLAYER_B);
        Assert.assertEquals(Player.PLAYER_B, game.getTurn());
    }
}
