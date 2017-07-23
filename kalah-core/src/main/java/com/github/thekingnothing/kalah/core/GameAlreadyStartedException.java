package com.github.thekingnothing.kalah.core;


public class GameAlreadyStartedException extends RuntimeException {
    public GameAlreadyStartedException(final String message) {
        super(message);
    }
}
