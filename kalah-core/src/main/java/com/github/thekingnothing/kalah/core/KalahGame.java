package com.github.thekingnothing.kalah.core;

public interface KalahGame {
    void start(Player playerOne, Player playerTwo);
    
    GameStatus getStatus();
    
    void makeTurn(PlayerHouse startHouse);
    
    KalahGameDesk getGameDesk();
    
    Player getWinner();
}
