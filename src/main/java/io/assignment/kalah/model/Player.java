package io.assignment.kalah.model;

public enum Player {
    PLAYER_A(KalahBoard.PIT_END_INDEX / 2),
    PLAYER_B(KalahBoard.PIT_END_INDEX);

    private int houseIndex;

    Player(final int houseIndex) {
        this.houseIndex = houseIndex;
    }

    public int getHouseIndex() {
        return this.houseIndex;
    }
}
