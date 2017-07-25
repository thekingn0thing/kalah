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

import com.github.thekingnothing.kalah.core.model.GameData;
import com.github.thekingnothing.kalah.core.model.GameStatus;

import java.util.List;

public class GameDataImpl implements GameData {
    
    private String gameId;
    
    private int initStonesCount;
    private GameStatus status;
    private String playerOneId;
    private String playerTwoId;
    private int playerOneStore;
    private int playerTwoStore;
    private List<Integer> playerOneHouses;
    private List<Integer> playerTwoHouses;
    private String winnerId;
    private String nextPlayerId;
    
    @Override
    public String getGameId() {
        return gameId;
    }
    
    @Override
    public void setGameId(final String gameId) {
        this.gameId = gameId;
    }
    
    @Override
    public String getNextPlayerId() {
        return nextPlayerId;
    }
    
    @Override
    public void setNextPlayerId(final String nextPlayerId) {
        this.nextPlayerId = nextPlayerId;
    }
    
    @Override
    public int getInitStonesCount() {
        return initStonesCount;
    }
    
    @Override
    public void setInitStonesCount(final int initStonesCount) {
        this.initStonesCount = initStonesCount;
    }
    
    @Override
    public GameStatus getStatus() {
        return status;
    }
    
    @Override
    public void setStatus(final GameStatus status) {
        this.status = status;
    }
    
    @Override
    public String getPlayerOneId() {
        return playerOneId;
    }
    
    @Override
    public void setPlayerOneId(final String playerOneId) {
        this.playerOneId = playerOneId;
    }
    
    @Override
    public String getPlayerTwoId() {
        return playerTwoId;
    }
    
    @Override
    public void setPlayerTwoId(final String playerTwoId) {
        this.playerTwoId = playerTwoId;
    }
    
    @Override
    public String getWinnerId() {
        return winnerId;
    }
    
    @Override
    public void setWinnerId(final String winnerId) {
        this.winnerId = winnerId;
    }
    
    @Override
    public int getPlayerOneStore() {
        return playerOneStore;
    }
    
    @Override
    public void setPlayerOneStore(final int playerOneStore) {
        this.playerOneStore = playerOneStore;
    }
    
    @Override
    public int getPlayerTwoStore() {
        return playerTwoStore;
    }
    
    @Override
    public void setPlayerTwoStore(final int playerTwoStore) {
        this.playerTwoStore = playerTwoStore;
    }
    
    @Override
    public List<Integer> getPlayerOneHouses() {
        return playerOneHouses;
    }
    
    @Override
    public void setPlayerOneHouses(final List<Integer> playerOneHouses) {
        this.playerOneHouses = playerOneHouses;
    }
    
    @Override
    public List<Integer> getPlayerTwoHouses() {
        return playerTwoHouses;
    }
    
    @Override
    public void setPlayerTwoHouses(final List<Integer> playerTwoHouses) {
        this.playerTwoHouses = playerTwoHouses;
    }
}
