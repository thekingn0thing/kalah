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

package com.github.thekingnothing.kalah.core.model;

import java.util.List;

public interface GameData {
    String getGameId();
    
    void setGameId(String gameId);
    
    String getNextPlayerId();
    
    void setNextPlayerId(String nextPlayerId);
    
    int getInitStonesCount();
    
    void setInitStonesCount(int initStonesCount);
    
    GameStatus getStatus();
    
    void setStatus(GameStatus status);
    
    String getPlayerOneId();
    
    void setPlayerOneId(String playerOneId);
    
    String getPlayerTwoId();
    
    void setPlayerTwoId(String playerTwoId);
    
    String getWinnerId();
    
    void setWinnerId(String winnerId);
    
    int getPlayerOneStore();
    
    void setPlayerOneStore(int playerOneStore);
    
    int getPlayerTwoStore();
    
    void setPlayerTwoStore(int playerTwoStore);
    
    List<Integer> getPlayerOneHouses();
    
    void setPlayerOneHouses(List<Integer> playerOneHouses);
    
    List<Integer> getPlayerTwoHouses();
    
    void setPlayerTwoHouses(List<Integer> playerTwoHouses);
}
