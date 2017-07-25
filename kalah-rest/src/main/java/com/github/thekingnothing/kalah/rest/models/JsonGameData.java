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

package com.github.thekingnothing.kalah.rest.models;

import com.github.thekingnothing.kalah.core.model.GameStatus;

import java.util.List;

public class JsonGameData {
    
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
    
    
    public String getGameId() {
        return gameId;
    }
    
    
    public void setGameId(final String gameId) {
        this.gameId = gameId;
    }
    
    
    public String getNextPlayerId() {
        return nextPlayerId;
    }
    
    
    public void setNextPlayerId(final String nextPlayerId) {
        this.nextPlayerId = nextPlayerId;
    }
    
    
    public int getInitStonesCount() {
        return initStonesCount;
    }
    
    
    public void setInitStonesCount(final int initStonesCount) {
        this.initStonesCount = initStonesCount;
    }
    
    
    public GameStatus getStatus() {
        return status;
    }
    
    
    public void setStatus(final GameStatus status) {
        this.status = status;
    }
    
    
    public String getPlayerOneId() {
        return playerOneId;
    }
    
    
    public void setPlayerOneId(final String playerOneId) {
        this.playerOneId = playerOneId;
    }
    
    
    public String getPlayerTwoId() {
        return playerTwoId;
    }
    
    
    public void setPlayerTwoId(final String playerTwoId) {
        this.playerTwoId = playerTwoId;
    }
    
    
    public String getWinnerId() {
        return winnerId;
    }
    
    
    public void setWinnerId(final String winnerId) {
        this.winnerId = winnerId;
    }
    
    
    public int getPlayerOneStore() {
        return playerOneStore;
    }
    
    
    public void setPlayerOneStore(final int playerOneStore) {
        this.playerOneStore = playerOneStore;
    }
    
    
    public int getPlayerTwoStore() {
        return playerTwoStore;
    }
    
    
    public void setPlayerTwoStore(final int playerTwoStore) {
        this.playerTwoStore = playerTwoStore;
    }
    
    
    public List<Integer> getPlayerOneHouses() {
        return playerOneHouses;
    }
    
    
    public void setPlayerOneHouses(final List<Integer> playerOneHouses) {
        this.playerOneHouses = playerOneHouses;
    }
    
    
    public List<Integer> getPlayerTwoHouses() {
        return playerTwoHouses;
    }
    
    
    public void setPlayerTwoHouses(final List<Integer> playerTwoHouses) {
        this.playerTwoHouses = playerTwoHouses;
    }
}
