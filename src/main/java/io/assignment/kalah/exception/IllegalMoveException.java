package io.assignment.kalah.exception;

public class IllegalMoveException extends RuntimeException {

    private static final long serialVersionUID = -3495331101146435929L;

    public IllegalMoveException(final String message) {
        super("Illegal move: " + message);
    }
}
