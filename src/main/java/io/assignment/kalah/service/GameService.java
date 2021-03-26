package io.assignment.kalah.service;

import io.assignment.kalah.dto.KalahGameDTO;

public interface GameService {

    KalahGameDTO createGame();

    KalahGameDTO play(String gameId, Integer pitId);

}
