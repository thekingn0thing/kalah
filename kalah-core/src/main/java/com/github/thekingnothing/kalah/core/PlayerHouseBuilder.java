package com.github.thekingnothing.kalah.core;

public interface PlayerHouseBuilder {
    PlayerHouse andIndex(int houseIndex);
    
    PlayerHouseBuilder withStones(int stones);
}
