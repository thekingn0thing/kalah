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
import com.github.thekingnothing.kalah.core.PlayerHouse;
import com.github.thekingnothing.kalah.core.PlayerHouseBuilder;

import java.util.Objects;

public class PlayerHouseBuilderImpl implements PlayerHouseBuilder {
    
    static BuilderFactory builderFactory = PlayerHouseBuilderImpl::new;
    
    public static PlayerHouseBuilder forPlayer(Player player) {
        return builderFactory.create(player);
    }
    
    private final Player player;
    private int stones;
    
    private PlayerHouseBuilderImpl(final Player player) {
        this.player = player;
    }
    
    @Override
    public PlayerHouse andIndex(final int houseIndex) {
        return new PlayerHouseImpl(player, houseIndex, stones);
    }
    
    @Override
    public PlayerHouseBuilder withStones(final int stones) {
        this.stones = stones;
        return this;
    }
    
    private final static class PlayerHouseImpl implements PlayerHouse {
        
        private final int stones;
        private final Player player;
        private final int houseIndex;
        
        private PlayerHouseImpl(final Player player, final int houseIndex, final int stones) {
            this.player = player;
            this.houseIndex = houseIndex;
            this.stones = stones;
        }
        
        @Override
        public int getStones() {
            return stones;
        }
        
        @Override
        public Player getPlayer() {
            return player;
        }
        
        @Override
        public int getHouseIndex() {
            return houseIndex;
        }
        
        @Override
        public PlayerHouse pickUpAllStones() {
            return new PlayerHouseImpl(player, houseIndex, 0);
        }
        
        @Override
        public PlayerHouse putStone() {
            return new PlayerHouseImpl(player, houseIndex, stones + 1);
        }
        
        @Override
        public String toString() {
            
            return "PlayerHouseImpl{" + "stones=" + stones +
                                  ", player=" + player.getId() +
                                  ", houseIndex=" + houseIndex +
                                  '}';
        }
    
        @Override
        public boolean equals(final Object o) {
            if (this == o) { return true; }
            if (!(o instanceof PlayerHouseImpl)) { return false; }
            final PlayerHouseImpl that = (PlayerHouseImpl) o;
            return getStones() == that.getStones() &&
                       getHouseIndex() == that.getHouseIndex() &&
                       Objects.equals(getPlayer(), that.getPlayer());
        }
    
        @Override
        public int hashCode() {
            return Objects.hash(getStones(), getPlayer(), getHouseIndex());
        }
    }
    
    interface BuilderFactory {
        PlayerHouseBuilder create(Player player);
    }
    
}
