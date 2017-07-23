package com.github.thekingnothing.kalah.core.support;

import com.github.thekingnothing.kalah.core.Player;
import com.github.thekingnothing.kalah.core.PlayerHouse;
import com.github.thekingnothing.kalah.core.PlayerHouseBuilder;

class PlayerHouseBuilderImpl implements PlayerHouseBuilder {
    
    static BuilderFactory builderFactory = PlayerHouseBuilderImpl::new;
    
    static PlayerHouseBuilder forPlayer(Player player) {
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
    }

    interface BuilderFactory {
        PlayerHouseBuilder create(Player player);
    }
    
}
