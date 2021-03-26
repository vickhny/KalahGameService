package io.assignment.kalah.model;

import lombok.Getter;

@Getter
public class Pit {

    private int id;
    private int stoneCount;

    public Pit(final int id, final int pitStones) {
        this.id = id;
        if (!this.isHouse()) {
            this.stoneCount = pitStones;
        }
    }

    public Player getPlayerTurn() {
        if (this.getId() <= Player.PLAYER_A.getHouseIndex()) {
            return Player.PLAYER_A;
        } else {
            return Player.PLAYER_B;
        }
    }

    public void setStoneCount(final int stoneCount) {
        this.stoneCount = stoneCount;
    }

    public boolean isDistributable(final Player turn) {
        return (!turn.equals(Player.PLAYER_A)
                || (this.getId() != Player.PLAYER_B.getHouseIndex()))
                && (!turn.equals(Player.PLAYER_B)
                || (this.getId() != Player.PLAYER_A.getHouseIndex()));
    }

    public boolean isHouse() {
        return (this.getId() == Player.PLAYER_A.getHouseIndex())
                || (this.getId() == Player.PLAYER_B.getHouseIndex());
    }
}
