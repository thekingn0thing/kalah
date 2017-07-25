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

import com.github.thekingnothing.kalah.core.GameLoader;
import com.github.thekingnothing.kalah.core.KalahGame;
import com.github.thekingnothing.kalah.core.KalahGameDesk;
import com.github.thekingnothing.kalah.core.Player;
import com.github.thekingnothing.kalah.core.PlayerHouse;
import com.github.thekingnothing.kalah.core.PlayerStore;
import com.github.thekingnothing.kalah.core.model.GameData;
import com.github.thekingnothing.kalah.core.support.ArrayKalahGameDesk.ArrayKalahGameDeskBuilder;
import com.github.thekingnothing.kalah.core.support.ArrayKalahGameDesk.PlayerStoreImpl;

import java.util.List;

public class DefaultGameLoader implements GameLoader {
    
    @Override
    public KalahGame load(final GameData gameData) {
        final int initStonesCount = gameData.getInitStonesCount();
        
        final String playerOneId = gameData.getPlayerOneId();
        final String playerTwoId = gameData.getPlayerTwoId();
        
        final Player playerOne = new PlayerImpl(playerOneId);
        final Player playerTwo = new PlayerImpl(playerTwoId);
        
        final Player nextPlayer = createNextPlayer(gameData, playerOneId, playerOne, playerTwo);
        final Player winnerPlayer = createWinnerPlayer(gameData, playerOneId, playerTwoId, playerOne, playerTwo);
        
        final Players players = new Players(playerOne, playerTwo);
        
        final KalahGameDesk gameDesk = ArrayKalahGameDeskBuilder.newBuilder()
                                                                .withInitStonesCount(initStonesCount)
                                                                .withPlayers(players)
                                                                .withPlayerStores(createPlayerStores(gameData.getPlayerOneStore(), gameData.getPlayerTwoStore()))
                                                                .withPlayerHouses(createPlayersHouses(playerOne, playerTwo, gameData))
                                                                .build();
        
        return new DefaultKalahGame(initStonesCount, gameData.getStatus(), players, gameDesk, nextPlayer, winnerPlayer);
    }
    
    @Override
    public KalahGame initGame(final Player playerOne, final Player playerTwo) {
        final DefaultKalahGame defaultKalahGame = new DefaultKalahGame(6);
        defaultKalahGame.start(playerOne, playerTwo);
        return defaultKalahGame;
    }
    
    private PlayerHouse[][] createPlayersHouses(final Player playerOne, final Player playerTwo, final GameData gameData) {
        final PlayerHouse[][] playerHouses = new PlayerHouse[2][DefaultKalahGame.HOUSES_PER_PLAYER];
        
        playerHouses[0] = createPlayerHouses(playerOne, gameData.getPlayerOneHouses());
        playerHouses[1] = createPlayerHouses(playerTwo, gameData.getPlayerTwoHouses());
        
        return playerHouses;
    }
    
    private PlayerHouse[] createPlayerHouses(final Player player, final List<Integer> playerOneHouses) {
        final PlayerHouse[] playerHouses = new PlayerHouse[DefaultKalahGame.HOUSES_PER_PLAYER];
        for (int i = 0; i < playerOneHouses.size(); i++) {
            final PlayerHouse playerHouse = PlayerHouseBuilderImpl.forPlayer(player)
                                                                  .withStones(playerOneHouses.get(i))
                                                                  .andIndex(i);
            playerHouses[i] = playerHouse;
        }
        return playerHouses;
    }
    
    private Player createNextPlayer(final GameData gameData, final String playerOneId, final Player playerOne, final Player playerTwo) {
        return gameData.getNextPlayerId().equals(playerOneId) ? playerOne : playerTwo;
    }
    
    private Player createWinnerPlayer(final GameData gameData, final String playerOneId, final String playerTwoId, final Player playerOne,
                                      final Player playerTwo) {
        final String winnerId = gameData.getWinnerId();
        return playerOneId.equals(winnerId) ? playerOne : playerTwoId.equals(winnerId) ? playerTwo : null;
    }
    
    private PlayerStore[] createPlayerStores(final int playerOneStore, final int playerTwoStore) {
        return new PlayerStore[]{new PlayerStoreImpl(playerOneStore), new PlayerStoreImpl(playerTwoStore)};
    }
}
