package io.assignment.kalah.exception;

public class GameNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 8234747595950930961L;

    public GameNotFoundException(final String id) {
        super("Could not find game " + id);
    }
}
