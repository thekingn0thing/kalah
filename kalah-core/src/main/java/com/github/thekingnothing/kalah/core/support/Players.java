package com.github.thekingnothing.kalah.core.support;

import com.github.thekingnothing.kalah.core.Player;
import com.github.thekingnothing.kalah.core.support.Players.PlayerWrapper;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

class Players implements Iterable<PlayerWrapper> {
    private final Map<Player, PlayerWrapper> players;
    
    Players(final Player playerOne, final Player playerTwo) {
        players = new LinkedHashMap<>();
        
        final PlayerWrapper playerOneWrapper = new PlayerWrapper(0, playerOne);
        players.put(playerOne, playerOneWrapper);
        
        final PlayerWrapper playerTwoWrapper = new PlayerWrapper(1, playerTwo);
        players.put(playerTwo, playerTwoWrapper);
    }
    
    @Override
    public Iterator<PlayerWrapper> iterator() {
        return players.values().iterator();
    }
    
    Player getOpponent(final Player player) {
        for (PlayerWrapper playerWrapper : players.values()) {
            if (!playerWrapper.equals(player)) {
                return playerWrapper;
            }
        }
        return null;
    }
    
    boolean contains(final Player player) {
        return players.containsKey(player);
    }
    
    int count() {
        return players.size();
    }
    
    PlayerWrapper findPlayer(final Player player) {
        if (player instanceof PlayerWrapper) {
            return (PlayerWrapper) player;
        }
        return players.get(player);
    }
    
    static class PlayerWrapper implements Player {
        private final int index;
        private final Player player;
        
        private PlayerWrapper(final int index, final Player player) {
            this.index = index;
            this.player = player;
        }
        
        int getIndex() {
            return index;
        }
        
        @Override
        public String getId() {return player.getId();}
    
        @Override
        public boolean equals(final Object o) {
            if (this == o) { return true; }
            if (!(o instanceof Player)) { return false; }
            final Player player = (Player) o;
            return Objects.equals(getId(), player.getId());
        }
    
        @Override
        public int hashCode() {
            return Objects.hash(getId());
        }
    }
}
