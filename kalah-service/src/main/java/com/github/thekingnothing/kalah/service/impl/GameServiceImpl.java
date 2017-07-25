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

package com.github.thekingnothing.kalah.service.impl;


import com.github.thekingnothing.kalah.core.GameLoader;
import com.github.thekingnothing.kalah.core.KalahGame;
import com.github.thekingnothing.kalah.core.Player;
import com.github.thekingnothing.kalah.core.model.GameData;
import com.github.thekingnothing.kalah.core.support.PlayerHouseBuilderImpl;
import com.github.thekingnothing.kalah.repository.GameRepository;
import com.github.thekingnothing.kalah.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {
    
    @Autowired
    private GameLoader gameLoader;
    
    @Autowired
    private GameRepository gameRepository;
    
    @Override
    public GameData startGame(final String playerOneId, final String playerTwoId) {
    
        Player playerOne = gameRepository.getPlayer(playerOneId);
        Player playerTwo = gameRepository.getPlayer(playerTwoId);
        
        KalahGame kalahGame = gameLoader.initGame(playerOne, playerTwo);
        final GameData gameData = kalahGame.toGameData();
    
    
        String gameId = UUID.randomUUID().toString();
        gameData.setGameId(gameId);
        
        gameRepository.save(gameData);
        
        return gameData;
    }
    
    @Override
    public GameData makeTurn(String gameId, final String playerId, int houseIndex) {
    
        GameData gameData =  gameRepository.load(gameId);
        
        if (gameData == null){
            throw new IllegalArgumentException("Cannot find a game with id" + gameId);
        }
        
        Player player = gameRepository.getPlayer(playerId);
        
        KalahGame kalahGame = gameLoader.load(gameData);
        
        kalahGame.makeTurn(
            PlayerHouseBuilderImpl.forPlayer(player)
                           .andIndex(houseIndex)
        );
        
        return kalahGame.toGameData();
    }
}
