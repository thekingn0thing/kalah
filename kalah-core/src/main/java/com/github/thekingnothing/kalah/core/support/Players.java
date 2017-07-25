/*
 *   Copyright 2017 Arthur Zagretdinov
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.github.thekingnothing.kalah.core.support;

import com.github.thekingnothing.kalah.core.Player;
import com.github.thekingnothing.kalah.core.support.Players.PlayerWrapper;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

class Players implements Iterable<PlayerWrapper> {
    private final Map<Player, PlayerWrapper> players;
    private final Player[] playerOrder;
    
    Players(final Player playerOne, final Player playerTwo) {
        players = new LinkedHashMap<>();
        playerOrder = new Player[2];
        
        playerOrder[0] = playerOne;
        playerOrder[1] = playerTwo;
        
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
    
    Player getPlayerOne() {
        return playerOrder[0];
    }
    
    Player getPlayerTwo() {
        return playerOrder[1];
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
