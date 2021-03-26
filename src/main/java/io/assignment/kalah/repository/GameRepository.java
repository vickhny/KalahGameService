package io.assignment.kalah.repository;

import io.assignment.kalah.model.KalahGame;

public interface GameRepository {

    KalahGame findById(final String id);

    KalahGame save(final KalahGame kalahGame);

}
