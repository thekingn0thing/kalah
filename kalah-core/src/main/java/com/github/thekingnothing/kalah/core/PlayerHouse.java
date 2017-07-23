package com.github.thekingnothing.kalah.core;

public interface PlayerHouse extends DeskLocation {
    
    Player getPlayer();
    
    int getHouseIndex();
    
    PlayerHouse pickUpAllStones();
    
    PlayerHouse putStone();
}
