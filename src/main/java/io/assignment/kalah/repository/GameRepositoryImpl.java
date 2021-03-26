package io.assignment.kalah.repository;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import io.assignment.kalah.exception.GameNotFoundException;
import io.assignment.kalah.model.KalahGame;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GameRepositoryImpl implements GameRepository {

    private final Map<String, KalahGame> repository = new HashMap<>();

    @Override
    public KalahGame findById(final String id) {
        final KalahGame kalahGame = this.repository.get(id);
        if (kalahGame == null) {
            log.error("Game not found with id - %d", id);
            throw new GameNotFoundException(id);
        }
        return kalahGame;
    }

    @Override
    public KalahGame save(final KalahGame kalahGame) {
        this.repository.put(kalahGame.getId(), kalahGame);
        return this.findById(kalahGame.getId());
    }

}
