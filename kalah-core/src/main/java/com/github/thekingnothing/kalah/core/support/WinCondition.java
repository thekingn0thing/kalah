package com.github.thekingnothing.kalah.core.support;

import com.github.thekingnothing.kalah.core.KalahGameDesk;
import com.github.thekingnothing.kalah.core.PlayerHouse;
import com.github.thekingnothing.kalah.core.PlayerStore;
import com.github.thekingnothing.kalah.core.Player;

import java.util.List;

class WinCondition {
    
    private final Player player;
    private final KalahGameDesk gameDesk;
    private final int halfOfStones;
    
    WinCondition(final KalahGameDesk gameDesk, final Player player, final int halfOfStones) {
        this.gameDesk = gameDesk;
        this.player = player;
        this.halfOfStones = halfOfStones;
    }
    
    boolean check() {
        return  playerCollectedMoreThanHalfOfStones();
    }
    
    private boolean playerCollectedMoreThanHalfOfStones() {
        final PlayerStore turnPlayerStore = gameDesk.getPlayerStore(player);
        return turnPlayerStore.getStones() > halfOfStones;
    }

}
