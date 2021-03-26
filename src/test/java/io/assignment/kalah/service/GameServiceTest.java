package io.assignment.kalah.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.assignment.kalah.dto.KalahGameDTO;
import io.assignment.kalah.exception.IllegalMoveException;
import io.assignment.kalah.model.KalahGame;
import io.assignment.kalah.model.Player;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameServiceTest {

    @Autowired
    private GameService service;

    @Value("${kalah.pit.stones:6}")
    private int pitStoneCount;

    private KalahGame gameInitial;
    private KalahGame gameFinishedWinnerB;
    private KalahGame gameFinishedWinnerA;
    private KalahGame gameAMovedFirst;
    private KalahGame gameBMovedFirst;
    private KalahGame gameTurnA;
    private KalahGame gameTurnB;

    @Before
    public void init()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        this.gameInitial = new KalahGame(pitStoneCount);
        this.gameFinishedWinnerB = new KalahGame(pitStoneCount);
        this.gameFinishedWinnerA = new KalahGame(pitStoneCount);
        this.gameAMovedFirst = new KalahGame(pitStoneCount);
        this.gameBMovedFirst = new KalahGame(pitStoneCount);
        this.gameTurnA = new KalahGame(pitStoneCount);
        this.gameTurnB = new KalahGame(pitStoneCount);

        final Method resetBoard =
                openMethodForTest(service.getClass().getDeclaredMethod("resetBoard", KalahGame.class));
        final Method distributeStones = openMethodForTest(
                service.getClass().getDeclaredMethod("distributeStones", KalahGame.class, int.class));

        resetBoard.invoke(this.service, this.gameFinishedWinnerB);
        this.gameFinishedWinnerB.getBoard().getPit(Player.PLAYER_A.getHouseIndex())
                .setStoneCount(10);
        this.gameFinishedWinnerB.getBoard().getPit(Player.PLAYER_B.getHouseIndex())
                .setStoneCount(62);
        resetBoard.invoke(this.service, this.gameFinishedWinnerA);
        this.gameFinishedWinnerA.getBoard().getPit(1).setStoneCount(1);
        this.gameFinishedWinnerA.getBoard().getPit(Player.PLAYER_A.getHouseIndex())
                .setStoneCount(39);
        this.gameFinishedWinnerA.getBoard().getPit(Player.PLAYER_B.getHouseIndex())
                .setStoneCount(32);

        distributeStones.invoke(this.service, this.gameAMovedFirst, 1);
        distributeStones.invoke(this.service, this.gameBMovedFirst, 10);
        distributeStones.invoke(this.service, this.gameTurnA, 1);
        distributeStones.invoke(this.service, this.gameTurnB, 8);
    }

    @Test
    public void testCreateKalahGame() {
        final KalahGameDTO game = this.service.createGame();

        Assert.assertNotNull(game);
    }

    @Test
    public void testPlay() {
        final KalahGameDTO game = this.service.createGame();
        this.service.play(game.getId(), 6);

        Assert.assertNull(game.getStatus());
    }

    @Test
    public void testDecidePlayerTurn() {
        Assert.assertNull(this.gameInitial.getTurn());
        Assert.assertEquals(Player.PLAYER_A, this.gameAMovedFirst.getTurn());
        Assert.assertEquals(Player.PLAYER_A, this.gameBMovedFirst.getTurn());
    }

    @Test
    public void testDetermineWinner()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Method determineWinner =
                openMethodForTest(service.getClass().getDeclaredMethod("determineWinner", KalahGame.class));
        determineWinner.invoke(this.service, this.gameInitial);
        determineWinner.invoke(this.service, this.gameFinishedWinnerB);
        determineWinner.invoke(this.service, this.gameFinishedWinnerA);

        Assert.assertNull(this.gameInitial.getWinner());
        Assert.assertEquals(Player.PLAYER_B, this.gameFinishedWinnerB.getWinner());
        Assert.assertEquals(Player.PLAYER_A, this.gameFinishedWinnerA.getWinner());
    }

    @Test
    public void testCheckKalahGameOver()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Method checkKalahGameOver =
                openMethodForTest(service.getClass().getDeclaredMethod("checkGameOver", KalahGame.class));
        checkKalahGameOver.invoke(this.service, this.gameInitial);
        checkKalahGameOver.invoke(this.service, this.gameFinishedWinnerB);
        checkKalahGameOver.invoke(this.service, this.gameFinishedWinnerA);

        Assert.assertEquals(36, this.gameInitial.getBoard().getStoneCount(Player.PLAYER_A, true));
        Assert.assertEquals(36, this.gameInitial.getBoard().getStoneCount(Player.PLAYER_B, true));
        Assert.assertEquals(10,
                this.gameFinishedWinnerB.getBoard().getStoneCount(Player.PLAYER_A, true));
        Assert.assertEquals(62,

                this.gameFinishedWinnerB.getBoard().getStoneCount(Player.PLAYER_B, true));
        Assert.assertEquals(40,
                this.gameFinishedWinnerA.getBoard().getStoneCount(Player.PLAYER_A, true));
        Assert.assertEquals(32,
                this.gameFinishedWinnerA.getBoard().getStoneCount(Player.PLAYER_B, true));
    }

    @Test(expected = IllegalMoveException.class)
    public void testInvalidMovePlayerATurn() throws Throwable {
        try {
            final Method validateMove = openMethodForTest(
                    service.getClass().getDeclaredMethod("validateMove", KalahGame.class, int.class));
            validateMove.invoke(this.service, this.gameTurnA, 12);
        } catch (final Exception e) {
            if (e instanceof InvocationTargetException) {
                throw ((InvocationTargetException) e).getTargetException();
            } else {
                throw e;
            }
        }
    }

    @Test(expected = IllegalMoveException.class)
    public void testInvalidMovePlayerBTurn() throws Throwable {
        try {
            final Method validateMove = openMethodForTest(
                    service.getClass().getDeclaredMethod("validateMove", KalahGame.class, int.class));
            validateMove.invoke(this.service, this.gameTurnB, 1);
        } catch (final Exception e) {
            if (e instanceof InvocationTargetException) {
                throw ((InvocationTargetException) e).getTargetException();
            } else {
                throw e;
            }
        }
    }

    @Test(expected = IllegalMoveException.class)
    public void testInvalidMoveStartFromEmptyPit() throws Throwable {
        try {
            final Method validateMove = openMethodForTest(
                    service.getClass().getDeclaredMethod("validateMove", KalahGame.class, int.class));
            validateMove.invoke(this.service, this.gameTurnA, 1);
        } catch (final Exception e) {
            if (e instanceof InvocationTargetException) {
                throw ((InvocationTargetException) e).getTargetException();
            } else {
                throw e;
            }
        }
    }

    @Test(expected = IllegalMoveException.class)
    public void testInvalidMoveStartFromHouse() throws Throwable {
        try {
            final Method validateMove = openMethodForTest(
                    service.getClass().getDeclaredMethod("validateMove", KalahGame.class, int.class));
            validateMove.invoke(this.service, this.gameInitial, Player.PLAYER_A.getHouseIndex());
        } catch (final Exception e) {
            if (e instanceof InvocationTargetException) {
                throw ((InvocationTargetException) e).getTargetException();
            } else {
                throw e;
            }
        }
    }

    @Test
    public void testLastEmptyPit()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Method resetBoard =
                openMethodForTest(service.getClass().getDeclaredMethod("resetBoard", KalahGame.class));
        final Method lastEmptyPit = openMethodForTest(
                service.getClass().getDeclaredMethod("lastEmptyPit", KalahGame.class, int.class));

        resetBoard.invoke(this.service, this.gameTurnA);
        this.gameTurnA.getBoard().getPit(Player.PLAYER_A.getHouseIndex()).setStoneCount(0);
        this.gameTurnA.getBoard().getPit(Player.PLAYER_B.getHouseIndex()).setStoneCount(0);
        this.gameTurnA.getBoard().getPit(4).setStoneCount(1);
        this.gameTurnA.getBoard().getPit(10).setStoneCount(6);

        lastEmptyPit.invoke(this.service, this.gameTurnA, 4);

        Assert.assertEquals(7, this.gameTurnA.getBoard().getStoneCount(Player.PLAYER_A, true));
        Assert.assertEquals(0, this.gameTurnA.getBoard().getStoneCount(Player.PLAYER_B, true));
    }

    @Test
    public void testValidMovePlayerA()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Method validateMove = openMethodForTest(
                service.getClass().getDeclaredMethod("validateMove", KalahGame.class, int.class));
        validateMove.invoke(this.service, this.gameInitial, 1);
        Assert.assertEquals(Player.PLAYER_A, this.gameInitial.getTurn());
    }

    @Test
    public void testValidMovePlayerB()
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        final Method validateMove = openMethodForTest(
                service.getClass().getDeclaredMethod("validateMove", KalahGame.class, int.class));
        validateMove.invoke(this.service, this.gameInitial, 13);
        Assert.assertEquals(Player.PLAYER_B, this.gameInitial.getTurn());
    }

    private Method openMethodForTest(final Method method) {
        ReflectionUtils.makeAccessible(method);
        return method;
    }
}
