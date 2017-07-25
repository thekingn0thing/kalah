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

import com.github.thekingnothing.kalah.core.DeskLocation;
import com.github.thekingnothing.kalah.core.KalahGameDesk;
import com.github.thekingnothing.kalah.core.Player;
import com.github.thekingnothing.kalah.core.PlayerHouse;
import com.github.thekingnothing.kalah.core.PlayerHouseBuilder;
import com.github.thekingnothing.kalah.core.model.GameData;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import io.github.benas.randombeans.api.Randomizer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;

public class DefaultGameLoaderTest {
    
    private DefaultGameLoader defaultGameLoader;
    private GameData gameData;
    
    @Before
    public void setUp() throws Exception {
        defaultGameLoader = new DefaultGameLoader();
        gameData = createTestGameData();
    }
    
    @Test
    public void should_load_game_from_game_data() {
        DefaultKalahGame game = (DefaultKalahGame) defaultGameLoader.load(gameData);
        
        assertGameIsLoaded(game);
        assertThatGameFieldsAreLoadedCorectly(gameData, game);
    }
    
    @Test
    public void should_load_game_from_game_data_without_winner() {
        gameData.setWinnerId(null);
        
        DefaultKalahGame game = (DefaultKalahGame) defaultGameLoader.load(gameData);
        
        assertGameIsLoaded(game);
        assertThatGameFieldsAreLoadedCorectly(gameData, game);
    }
    
    @Test
    public void should_load_game_desk_from_game_data() {
        DefaultKalahGame game = (DefaultKalahGame) defaultGameLoader.load(gameData);
        
        final KalahGameDesk gameDesk = game.getGameDesk();
        
        final Player playerOne = player(gameData.getPlayerOneId());
        final Player playerTwo = player(gameData.getPlayerTwoId());
        
        
        assertThatStoresAreLoaded(gameDesk, playerOne, playerTwo);
        
        assertThat(gameDesk.getPlayerHouses(playerOne))
            .as("Houses are loaded correctly.")
            .containsExactlyElementsOf(toHousesList(playerOne, gameData.getPlayerOneHouses()));
        
    }
    
    private Iterable<PlayerHouse> toHousesList(final Player player, final List<Integer> houses) {
        List<PlayerHouse> playerHouses = new ArrayList<>(houses.size());
    
    
        for (int i = 0; i < houses.size(); i++) {
            final PlayerHouse playerHouse = PlayerHouseBuilderImpl.forPlayer(player)
                                                                  .withStones(houses.get(i))
                                                                  .andIndex(i);
            playerHouses.add(playerHouse);
        }
    
        return playerHouses;
    }
    
    private void assertThatStoresAreLoaded(final KalahGameDesk gameDesk, final Player playerOne,
                                           final Player playerTwo) {
        assertThat(gameDesk.getPlayerStore(playerOne))
            .as("Player one store is loaded")
            .extracting(DeskLocation::getStones)
            .containsExactly(gameData.getPlayerOneStore());
        
        assertThat(gameDesk.getPlayerStore(playerTwo))
            .as("Player two store is loaded")
            .extracting(DeskLocation::getStones)
            .containsExactly(gameData.getPlayerTwoStore());
    }
    
    private void assertThatGameFieldsAreLoadedCorectly(final GameData gameData, final DefaultKalahGame game) {
        assertThat(game)
            .as("Game fields are loaded correctly")
            .extracting(
                DefaultKalahGame::getStatus,
                DefaultKalahGame::getWinner,
                DefaultKalahGame::getPlayerOne,
                DefaultKalahGame::getPlayerTwo,
                DefaultKalahGame::getNextPlayer
            )
            .containsExactly(
                gameData.getStatus(),
                player(gameData.getWinnerId()),
                player(gameData.getPlayerOneId()),
                player(gameData.getPlayerTwoId()),
                player(gameData.getNextPlayerId())
            );
    }
    
    private void assertGameIsLoaded(final DefaultKalahGame game) {
        assertThat(game)
            .as("Game is loaded")
            .isNotNull();
    }
    
    private Player player(final String id) {
        if (id == null) {
            return null;
        }
        return new PlayerImpl(id);
    }
    
    private GameData createTestGameData() {
        
        EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
                                                     .stringLengthRange(5, 8)
                                                     .collectionSizeRange(6, 6)
                                                     .randomize(Integer.class, (Randomizer<Integer>) () -> nextInt(3, 6))
                                                     .build();
        
        final GameDataImpl gameData = random.nextObject(GameDataImpl.class);
        
        gameData.setWinnerId(gameData.getPlayerOneId());
        gameData.setNextPlayerId(gameData.getPlayerTwoId());
        
        return gameData;
    }
    
}
