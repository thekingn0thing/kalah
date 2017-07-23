package com.github.thekingnothing.kalah.core;


import java.util.List;

public interface KalahGameDesk {
    DeskLocation getLastLocation();
    
    List<PlayerHouse> getPlayerHouses(final Player player);
    
    PlayerStore getPlayerStore(Player player);
    
    KalahGameDesk pickUpAllStones(Player player, int houseIndex);
    
    KalahGameDesk putStonesToPlayerHouses(Player player, int houseIndex);
    
    KalahGameDesk putStoneToPlayerStore(Player player);
    
    KalahGameDesk putAllStoneToPlayerStore(Player player);
    
    boolean hasStonesOutOfDesk();
    
    KalahGameDesk pickUpAllStones();
}
