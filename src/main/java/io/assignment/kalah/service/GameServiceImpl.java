package io.assignment.kalah.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.Map;
import java.util.stream.Collectors;

import io.assignment.kalah.dto.KalahGameDTO;
import io.assignment.kalah.exception.IllegalMoveException;
import io.assignment.kalah.model.KalahBoard;
import io.assignment.kalah.model.KalahGame;
import io.assignment.kalah.model.Pit;
import io.assignment.kalah.model.Player;
import io.assignment.kalah.repository.GameRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GameServiceImpl implements GameService {

    private final GameRepository repository;

    private final Environment environment;

    @Value("${kalah.pit.stones:6}")
    private Integer pitStoneCount;

    @Autowired
    public GameServiceImpl(final GameRepository repository, final Environment environment) {
        this.repository = repository;
        this.environment = environment;
    }

    @Override
    public KalahGameDTO createGame() {
        final KalahGame kalahGame = this.repository.save(new KalahGame(pitStoneCount));
        return new KalahGameDTO(kalahGame.getId(), getUrl(kalahGame.getId()));
    }

    @Override
    public KalahGameDTO play(final String gameId, final Integer pitId) {
        final KalahGame kalahGame = this.repository.findById(gameId);
        distributeStones(kalahGame, pitId);
        checkGameOver(kalahGame);

        final Map<Integer, String> status = kalahGame.getBoard().getPits().stream()
                .collect(Collectors.toMap(Pit::getId, value -> Integer.toString(value.getStoneCount())));
        return new KalahGameDTO(kalahGame.getId(), getUrl(kalahGame.getId()), status);
    }

    private String getUrl(final String gameId) {
        final int port = environment.getProperty("server.port", Integer.class, 8090);
        return String.format("http://%s:%s/games/%s", InetAddress.getLoopbackAddress().getHostName(),
                port, gameId);
    }

    private void resetBoard(final KalahGame kalahGame) {
        kalahGame.getBoard().getPits().parallelStream()
                .filter(pit -> (Player.PLAYER_A.getHouseIndex() != pit.getId())
                        && (Player.PLAYER_B.getHouseIndex() != pit.getId()))
                .forEach(pit -> pit.setStoneCount(0));
    }

    private void distributeStones(final KalahGame kalahGame, int pitId) {
        final Pit startPit = kalahGame.getBoard().getPit(pitId);
        validateMove(kalahGame, pitId);
        int stoneToDistribute = startPit.getStoneCount();
        startPit.setStoneCount(0);
        while (stoneToDistribute > 0) {
            final Pit currentPit = kalahGame.getBoard().getPit(++pitId);
            if (currentPit.isDistributable(kalahGame.getTurn())) {
                currentPit.setStoneCount(currentPit.getStoneCount() + 1);
                stoneToDistribute--;
            }
        }
        lastEmptyPit(kalahGame, pitId);
        decidePlayerTurn(kalahGame, pitId);
    }

    private void checkGameOver(final KalahGame kalahGame) {
        final int playerAPitStoneCount = kalahGame.getBoard().getStoneCount(Player.PLAYER_A, false);
        final int playerBPitStoneCount = kalahGame.getBoard().getStoneCount(Player.PLAYER_B, false);
        if ((playerAPitStoneCount == 0) || (playerBPitStoneCount == 0)) {
            final Pit houseA = kalahGame.getBoard().getPit(Player.PLAYER_A.getHouseIndex());
            final Pit houseB = kalahGame.getBoard().getPit(Player.PLAYER_B.getHouseIndex());
            houseA.setStoneCount(houseA.getStoneCount() + playerAPitStoneCount);
            houseB.setStoneCount(houseB.getStoneCount() + playerBPitStoneCount);
            determineWinner(kalahGame);
            resetBoard(kalahGame);
        }
    }

    private void determineWinner(final KalahGame kalahGame) {
        final int houseAStoneCount = kalahGame.getBoard().getStoneCount(Player.PLAYER_A, true);
        final int houseBStoneCount = kalahGame.getBoard().getStoneCount(Player.PLAYER_B, true);
        if (houseAStoneCount > houseBStoneCount) {
            kalahGame.setWinner(Player.PLAYER_A);
        } else if (houseAStoneCount < houseBStoneCount) {
            kalahGame.setWinner(Player.PLAYER_B);
        }
    }

    private void lastEmptyPit(final KalahGame kalahGame, final int endPitId) {
        final Pit endPit = kalahGame.getBoard().getPit(endPitId);
        if (!endPit.isHouse() && endPit.getPlayerTurn().equals(kalahGame.getTurn())
                && (endPit.getStoneCount() == 1)) {
            final Pit oppositePit = kalahGame.getBoard().getPit(KalahBoard.PIT_END_INDEX - endPit.getId());
            if (oppositePit.getStoneCount() > 0) {
                final Pit house = kalahGame.getBoard().getPit(endPit.getPlayerTurn().getHouseIndex());
                house.setStoneCount(
                        (house.getStoneCount() + oppositePit.getStoneCount()) + endPit.getStoneCount());
                oppositePit.setStoneCount(0);
                endPit.setStoneCount(0);
            }
        }
    }

    private void decidePlayerTurn(final KalahGame kalahGame, final int pitId) {
        final Pit pit = kalahGame.getBoard().getPit(pitId);
        if (pit.isHouse() && Player.PLAYER_A.equals(pit.getPlayerTurn())
                && Player.PLAYER_A.equals(kalahGame.getTurn())) {
            kalahGame.setTurn(Player.PLAYER_A);
        } else if (pit.isHouse() && Player.PLAYER_B.equals(pit.getPlayerTurn())
                && Player.PLAYER_B.equals(kalahGame.getTurn())) {
            kalahGame.setTurn(Player.PLAYER_B);
        } else {
            if (Player.PLAYER_A.equals(kalahGame.getTurn())) {
                kalahGame.setTurn(Player.PLAYER_B);
            } else {
                kalahGame.setTurn(Player.PLAYER_A);
            }
        }
    }

    private void validateMove(final KalahGame kalahGame, final int startPitId) {
        final Pit startPit = kalahGame.getBoard().getPit(startPitId);
        if (startPit.isHouse()) {
            log.error("Can not start from house");
            throw new IllegalMoveException("Can not start from house");
        }
        if (Player.PLAYER_A.equals(kalahGame.getTurn())
                && !Player.PLAYER_A.equals(startPit.getPlayerTurn())) {
            log.error("It's Player A turn");
            throw new IllegalMoveException("It's Player A turn");
        }
        if (Player.PLAYER_B.equals(kalahGame.getTurn())
                && !Player.PLAYER_B.equals(startPit.getPlayerTurn())) {
            log.error("It's Player B turn");
            throw new IllegalMoveException("It's Player B turn");
        }
        if (startPit.getStoneCount() == 0) {
            log.error("Can not start from empty pit");
            throw new IllegalMoveException("Can not start from empty pit");
        }
        if (kalahGame.getTurn() == null) {
            if (Player.PLAYER_A.equals(startPit.getPlayerTurn())) {
                kalahGame.setTurn(Player.PLAYER_A);
            } else {
                kalahGame.setTurn(Player.PLAYER_B);
            }
        }
    }
}
