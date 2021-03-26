package io.assignment.kalah.model;

import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class KalahGame {

    @EqualsAndHashCode.Include
    private final String id;

    private final KalahBoard board;
    private Player winner;
    private Player turn;

    public KalahGame(int pitStoneCount) {
        this.id = UUID.randomUUID().toString();
        this.board = new KalahBoard(pitStoneCount);
    }
}
