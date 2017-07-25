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

import com.github.thekingnothing.kalah.core.KalahGameDesk;
import com.github.thekingnothing.kalah.core.PlayerHouse;
import com.github.thekingnothing.kalah.core.support.Players.PlayerWrapper;
import com.github.thekingnothing.kalah.core.DeskLocation;
import com.github.thekingnothing.kalah.core.Player;
import com.github.thekingnothing.kalah.core.PlayerStore;

import java.util.Arrays;
import java.util.List;

class ArrayKalahGameDesk implements KalahGameDesk {
    
    private final int initStonesCount;
    private final Players players;
    
    private PlayerHouse[][] playerHouses;
    private PlayerStore[] playerStores;
    private int stonesOutOfDesk;
    private DeskLocation lastLocation;
    
    ArrayKalahGameDesk(final Players players, final int initStonesCount) {
        this.players = players;
        this.initStonesCount = initStonesCount;
        this.stonesOutOfDesk = 0;
        init();
    }
    
    private ArrayKalahGameDesk(final Players players, final int initStonesCount, final PlayerHouse[][] playerHouses,
                               final PlayerStore[] playerStores, final int stonesOutOfDesk, final DeskLocation lastLocation) {
        this.players = players;
        this.initStonesCount = initStonesCount;
        this.playerHouses = playerHouses;
        this.playerStores = playerStores;
        this.stonesOutOfDesk = stonesOutOfDesk;
        this.lastLocation = lastLocation;
    }
    
    @Override
    public DeskLocation getLastLocation() {
        return lastLocation;
    }
    
    @Override
    public List<PlayerHouse> getPlayerHouses(final Player player) {
        return Arrays.asList(getPlayerHouses0(players.findPlayer(player)));
    }
    
    @Override
    public PlayerStore getPlayerStore(final Player player) {
        return getPlayerStore0(players.findPlayer(player));
    }
    
    @Override
    public KalahGameDesk pickUpAllStones(final Player player, final int houseIndex) {
        final PlayerHouse[] playerHouses = getPlayerHouses0(players.findPlayer(player));
        
        final PlayerHouse startHouse = playerHouses[houseIndex];
        
        final int stones = startHouse.getStones();
        playerHouses[houseIndex] = startHouse.pickUpAllStones();
        
        return ArrayKalahGameDeskBuilder.newBuilder(this)
                                        .withStonesOutOfDesk(stones + stonesOutOfDesk)
                                        .withLastLocation(playerHouses[houseIndex])
                                        .build();
    }
    
    @Override
    public KalahGameDesk pickUpAllStones() {
        int stones = 0;
        for (int i = 0; i < playerHouses.length; i++) {
            final PlayerHouse[] houses = playerHouses[i];
            for (int j = 0; j < houses.length; j++) {
                final PlayerHouse house = playerHouses[i][j];
                stones += house.getStones();
                playerHouses[i][j] = house.pickUpAllStones();
            }
        }
        return ArrayKalahGameDeskBuilder.newBuilder(this)
                                        .withStonesOutOfDesk(stones)
                                        .build();
    }
    
    @Override
    public KalahGameDesk putStonesToPlayerHouses(final Player player, final int houseIndex) {
        final PlayerHouse[] playerHouses = getPlayerHouses0(players.findPlayer(player));
        return putStonesStartFrom(houseIndex, playerHouses);
    }
    
    int getStonesOutOfDesk() {
        return stonesOutOfDesk;
    }
    
    private KalahGameDesk putStonesStartFrom(final int startHouseIndex, final PlayerHouse[] playerHouses) {
        int index = startHouseIndex;
        DeskLocation lastPlayerHouse = lastLocation;
        while (index < playerHouses.length && stonesOutOfDesk > 0) {
            playerHouses[index] = playerHouses[index].putStone();
            lastPlayerHouse = playerHouses[index];
            index++;
            stonesOutOfDesk--;
        }
        return ArrayKalahGameDeskBuilder.newBuilder(this)
                                        .withLastLocation(lastPlayerHouse)
                                        .build();
    }
    
    @Override
    public KalahGameDesk putStoneToPlayerStore(final Player player) {
        if (stonesOutOfDesk > 0) {
            return putStonesToPLayerStore(player, 1);
        } else {
            return this;
        }
    }
    
    @Override
    public KalahGameDesk putAllStoneToPlayerStore(final Player player) {
        return putStonesToPLayerStore(player, stonesOutOfDesk);
    }
    
    @Override
    public boolean hasStonesOutOfDesk() {
        return stonesOutOfDesk > 0;
    }
    
    private KalahGameDesk putStonesToPLayerStore(final Player player, final int stones) {
        final PlayerWrapper playerWrapper = players.findPlayer(player);
        final PlayerStore playerStore = getPlayerStore0(playerWrapper);
        setPlayerStore(playerWrapper, playerStore.putStone(stones));
        return ArrayKalahGameDeskBuilder.newBuilder(this)
                                        .withStonesOutOfDesk(stonesOutOfDesk - stones)
                                        .withLastLocation(playerStore)
                                        .build();
    }
    
    private void init() {
        playerHouses = createDefaultPlayerHouses();
        playerStores = createDefaultPlayerStores();
    }
    
    private PlayerStore[] createDefaultPlayerStores() {
        PlayerStore[] playerHouses = new PlayerStore[players.count()];
        for (PlayerWrapper player : players) {
            playerHouses[player.getIndex()] = new PlayerStoreImpl();
        }
        return playerHouses;
    }
    
    private PlayerHouse[][] createDefaultPlayerHouses() {
        PlayerHouse[][] playerHouses = new PlayerHouse[players.count()][DefaultKalahGame.HOUSES_PER_PLAYER];
        for (PlayerWrapper player : players) {
            for (int i = 0; i < DefaultKalahGame.HOUSES_PER_PLAYER; i++) {
                playerHouses[player.getIndex()][i] = PlayerHouseBuilderImpl.forPlayer(player)
                                                                           .withStones(initStonesCount)
                                                                           .andIndex(i);
            }
        }
        return playerHouses;
    }
    
    private void setPlayerStore(final PlayerWrapper player, final PlayerStore newValue) {
        playerStores[player.getIndex()] = newValue;
    }
    
    private PlayerHouse[] getPlayerHouses0(final PlayerWrapper player) {
        return playerHouses[player.getIndex()];
    }
    
    private PlayerStore getPlayerStore0(final PlayerWrapper player) {
        return playerStores[player.getIndex()];
    }
    
    static class PlayerStoreImpl implements PlayerStore {
        private int stones;
        
        private PlayerStoreImpl() {
            this.stones = 0;
        }
        
        PlayerStoreImpl(final int stones) {
            this.stones = stones;
        }
        
        @Override
        public int getStones() {
            return stones;
        }
        
        @Override
        public PlayerStore putStone(final int stones) {
            return new PlayerStoreImpl(this.stones + stones);
        }
    }
    
    static class ArrayKalahGameDeskBuilder {
        
        static ArrayKalahGameDeskBuilder newBuilder() {
            return new ArrayKalahGameDeskBuilder();
        }
        
        private static ArrayKalahGameDeskBuilder newBuilder(final ArrayKalahGameDesk gameDesk) {
            return new ArrayKalahGameDeskBuilder()
                       .withPlayers(gameDesk.players)
                       .withInitStonesCount(gameDesk.initStonesCount)
                       .withPlayerHouses(gameDesk.playerHouses)
                       .withPlayerStores(gameDesk.playerStores)
                       .withStonesOutOfDesk(gameDesk.stonesOutOfDesk)
                       .withLastLocation(gameDesk.lastLocation);
        }
        
        private Players players;
        private int initStonesCount;
        private PlayerHouse[][] playerHouses;
        private PlayerStore[] playerStores;
        private int stonesOutOfDesk;
        
        private DeskLocation lastLocation;
        
        private ArrayKalahGameDeskBuilder() {
            initStonesCount = 6;
            stonesOutOfDesk = 0;
            lastLocation = null;
        }
        
        ArrayKalahGameDeskBuilder withPlayers(final Players players) {
            this.players = players;
            return this;
        }
        
        ArrayKalahGameDeskBuilder withInitStonesCount(final int initStonesCount) {
            this.initStonesCount = initStonesCount;
            return this;
        }
        
        ArrayKalahGameDeskBuilder withPlayerHouses(final PlayerHouse[][] playerHouses) {
            this.playerHouses = playerHouses;
            return this;
        }
        
        ArrayKalahGameDeskBuilder withPlayerStores(final PlayerStore[] playerStores) {
            this.playerStores = playerStores;
            return this;
        }
        
        ArrayKalahGameDeskBuilder withStonesOutOfDesk(final int stonesOutOfDesk) {
            this.stonesOutOfDesk = stonesOutOfDesk;
            return this;
        }
        
        ArrayKalahGameDeskBuilder withLastLocation(final DeskLocation lastLocation) {
            this.lastLocation = lastLocation;
            return this;
        }
        
        ArrayKalahGameDesk build() {
            return new ArrayKalahGameDesk(players, initStonesCount, playerHouses, playerStores, stonesOutOfDesk, lastLocation);
        }
    }
}
