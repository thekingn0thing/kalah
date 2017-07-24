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
import com.github.thekingnothing.kalah.core.GameAlreadyStartedException;
import com.github.thekingnothing.kalah.core.GameStatus;
import com.github.thekingnothing.kalah.core.IllegalPlayerException;
import com.github.thekingnothing.kalah.core.IllegalTurnException;
import com.github.thekingnothing.kalah.core.KalahGameDesk;
import com.github.thekingnothing.kalah.core.PlayerHouse;
import com.github.thekingnothing.kalah.core.PlayerHouseBuilder;
import com.github.thekingnothing.kalah.core.PlayerStore;
import com.github.thekingnothing.kalah.core.support.ArrayKalahGameDesk.ArrayKalahGameDeskBuilder;
import com.github.thekingnothing.kalah.core.support.PlayerHouseBuilderImpl.BuilderFactory;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import com.github.thekingnothing.kalah.core.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(HierarchicalContextRunner.class)
public class KalahGameTest {
    
    private static final int INIT_STONES_COUNT = 6;
    
    private Player playerOne;
    private Player playerTwo;
    private DefaultKalahGame objectUnderTest;
    
    @Before
    public void setUp() throws Exception {
        playerOne = new PlayerImpl("A");
        playerTwo = new PlayerImpl("B");
        objectUnderTest = new DefaultKalahGame(INIT_STONES_COUNT);
    }
    
    public class General {
        
        @Test
        public void should_start_a_game_with_two_players() {
            objectUnderTest.start(playerOne, playerTwo);
            
            assertThat(objectUnderTest.getStatus())
                .as("Game is started.")
                .isEqualTo(GameStatus.Started);
        }
        
        @Test
        public void should_return_null_as_game_desk_if_game_not_started() {
            KalahGameDesk gameDesk = objectUnderTest.getGameDesk();
            
            assertThat(gameDesk)
                .as("Game desk is null.")
                .isNull();
        }
    }
    
    public class GameStarted {
        
        @Before
        public void setUp() throws Exception {
            objectUnderTest.start(playerOne, playerTwo);
        }
        
        @Test
        public void should_throw_exception_if_try_to_already_started_game() {
            assertThatThrownBy(() -> objectUnderTest.start(playerOne, playerTwo))
                .as("Exception is thrown")
                .isInstanceOf(GameAlreadyStartedException.class)
                .hasMessageContaining("Game is already started.");
        }
        
        @Test
        public void should_throw_exception_if_a_player_not_started_a_game_tries_to_make_turn() {
            assertThatThrownBy(() -> objectUnderTest.makeTurn(new PlayerHouseMock(new PlayerImpl("C"))))
                .as("Not a game player cannot make turn")
                .isInstanceOf(IllegalPlayerException.class);
        }
        
        @Test
        public void should_return_game_desk_if_game_started() {
            KalahGameDesk gameDesk = objectUnderTest.getGameDesk();
            
            assertThat(gameDesk)
                .as("Game desk is returned")
                .isNotNull();
        }
    }
    
    public class MockedPlayerHouseBuilder {
        
        private PlayerHouseBuilderMock playerHouseBuilderMock;
        private BuilderFactory oldBuilderFactory;
        
        @Before
        public void setUp() throws Exception {
            playerHouseBuilderMock = new PlayerHouseBuilderMock();
            oldBuilderFactory = PlayerHouseBuilderImpl.builderFactory;
            PlayerHouseBuilderImpl.builderFactory = player -> {
                playerHouseBuilderMock.setActivePlayer(player);
                return playerHouseBuilderMock;
            };
            objectUnderTest.start(playerOne, playerTwo);
        }
        
        @After
        public void tearDown() {
            PlayerHouseBuilderImpl.builderFactory = oldBuilderFactory;
        }
        
        @Test
        public void stones_should_be_moved_counter_clockwise_by_one_after_player_turn() {
            objectUnderTest.makeTurn(new PlayerHouseMock(playerOne, 0));
            assertPlayerHousesHasStones(playerOne, new Integer[]{0, 7, 7, 7, 7, 7});
            
            objectUnderTest.makeTurn(new PlayerHouseMock(playerOne, 1));
            objectUnderTest.makeTurn(new PlayerHouseMock(playerTwo, 0));
            assertPlayerHousesHasStones(playerTwo, new Integer[]{0, 8, 7, 7, 7, 7});
            assertPlayerHousesHasStones(playerOne, new Integer[]{1, 0, 8, 8, 8, 8});
        }
        
        @Test
        public void stones_should_be_moved_to_player_store_if_stones_count_more_than_clockwise_houses() {
            objectUnderTest.makeTurn(new PlayerHouseMock(playerOne, 0));
            assertStonesMoveToPlayerStore(playerOne);
            
            objectUnderTest.makeTurn(new PlayerHouseMock(playerOne, 1));
            objectUnderTest.makeTurn(new PlayerHouseMock(playerTwo, 0));
            assertStonesMoveToPlayerStore(playerTwo);
        }
        
        @Test
        public void stones_should_be_moved_to_opposite_side_if_count_more_than_than_clockwise_houses_and_store() {
            objectUnderTest.makeTurn(new PlayerHouseMock(playerOne, 3));
            assertPlayerHousesHasStones(playerTwo, new Integer[]{7, 7, 7, 6, 6, 6});
            assertPlayerHousesHasStones(playerOne, new Integer[]{6, 6, 6, 0, 7, 7});
            
            objectUnderTest.makeTurn(new PlayerHouseMock(playerTwo, 5));
            
            assertPlayerHousesHasStones(playerTwo, new Integer[]{7, 7, 7, 6, 6, 0});
            assertPlayerHousesHasStones(playerOne, new Integer[]{7, 7, 7, 1, 8, 7});
        }
        
        @Test
        public void should_throw_exception_if_make_turn_with_house_out_of_range() {
            assertThatThrownBy(() -> objectUnderTest.makeTurn(new PlayerHouseMock(playerTwo, 10)))
                .as("IllegalTurnException is thrown")
                .isInstanceOf(IllegalTurnException.class)
                .hasMessageContaining("out of range");
        }
        
        @Test
        public void should_throw_exception_if_make_turn_for_empty_house() {
            objectUnderTest.makeTurn(new PlayerHouseMock(playerOne, 0));
            assertThatThrownBy(() -> objectUnderTest.makeTurn(new PlayerHouseMock(playerOne, 0)))
                .as("IllegalTurnException is thrown")
                .isInstanceOf(IllegalTurnException.class)
                .hasMessageContaining("empty");
        }
        
        @Test
        public void should_throw_exception_if_make_turn_out_of_order() {
            objectUnderTest.makeTurn(new PlayerHouseMock(playerOne, 3));
            assertThatThrownBy(() -> objectUnderTest.makeTurn(new PlayerHouseMock(playerOne, 0)))
                .as("IllegalTurnException is thrown")
                .isInstanceOf(IllegalTurnException.class)
                .hasMessageContaining("out of order");
        }
        
        private void assertStonesMoveToPlayerStore(final Player player) {
            PlayerStore playerStore = objectUnderTest.getGameDesk().getPlayerStore(player);
            assertThat(playerStore.getStones())
                .as("Stone is moved to player %s store", player)
                .isEqualTo(1);
        }
        
        private void assertPlayerHousesHasStones(final Player player, final Integer[] houseStones) {
            for (int index = 0; index < houseStones.length; index++) {
                PlayerHouseMock playerHouse = playerHouseBuilderMock.getLastPlayerHouseMocks(player, index);
                
                assertThat(playerHouse)
                    .as("Player %s house with index %s exist", player, index)
                    .isNotNull();
                
                assertThat(playerHouse.getStones())
                    .as("Player %s house %s stones count is equal to %s", player, index, houseStones[index])
                    .isEqualTo(houseStones[index]);
            }
        }
    }
    
    public class PreDefinedDesk {
        
        private PlayerHouse[][] playerHouses;
        private PlayerStore[] playerStores;
        
        @Before
        public void setUp() throws Exception {
            objectUnderTest.start(playerOne, playerTwo);
            
            initPlayerStores();
            initPlayerHouses();
            
            final ArrayKalahGameDesk gameDesk = ArrayKalahGameDeskBuilder.newBuilder()
                                                                         .withPlayers(new Players(playerOne, playerTwo))
                                                                         .withPlayerHouses(playerHouses)
                                                                         .withPlayerStores(playerStores)
                                                                         .build();
            
            objectUnderTest.setGameDesk(gameDesk);
            
        }
        
        private void initPlayerStores() {
            playerStores = new PlayerStore[2];
            
            playerStores[0] = new PlayerStoreMock();
            playerStores[1] = new PlayerStoreMock();
        }
        
        private void initPlayerHouses() {
            playerHouses = new PlayerHouse[2][6];
            
            initPlayerHouses(0, playerOne);
            initPlayerHouses(1, playerTwo);
        }
        
        private void initPlayerHouses(final int playerIndex, final Player player) {
            for (int j = 0; j < 6; j++) {
                playerHouses[playerIndex][j] = new PlayerHouseMock(player, j);
            }
        }
        
        @Test
        public void turn_player_should_capture_opposite_stones_when_finished_on_empty_house() {
            final Player turnPlayer = playerOne;
            final int[][] init = {
                {0, 0, 3, 5, 0, 0},
                {0, 2, 0, 0, 0, 0}
            };
            
            fillPlayerHouses(init);
            
            objectUnderTest.makeTurn(new PlayerHouseMock(turnPlayer, 1));
            
            assertThatStonesIsCaptured();
            
            assertThatDeskHasExpectedStateAfterCapturing(objectUnderTest.getGameDesk());
        }
        
        @Test
        public void turn_player_should_win_if_capture_more_than_half_of_all_stones() {
            final Player turnPlayer = playerOne;
            
            final int[][] init = {
                {0, 0, 2, 6, 0, 0},
                {0, 2, 0, 0, 0, 0}
            };
            
            fillPlayerHouses(init);
            
            playerStores[0] = new PlayerStoreMock(30);
            playerStores[1] = new PlayerStoreMock(32);
            
            objectUnderTest.makeTurn(new PlayerHouseMock(turnPlayer, 1));
            
            assertGameIsFinishedAndTurnPlayerHasWon(turnPlayer);
        }
        
        @Test
        public void turn_player_should_win_if_opponent_does_not_have_stones_after_turn_and_capture_more_than_half_of_all_stones() {
            final Player turnPlayer = playerOne;
            
            final int[][] init = {
                {0, 0, 0, 5, 0, 0},
                {0, 2, 0, 0, 0, 0}
            };
    
            playerStores[0] = new PlayerStoreMock(30);
            playerStores[1] = new PlayerStoreMock(35);
            
            fillPlayerHouses(init);
            
            objectUnderTest.makeTurn(new PlayerHouseMock(turnPlayer, 1));
            
            assertGameIsFinishedAndTurnPlayerHasWon(turnPlayer);
        }
    
    
        @Test
        public void the_should_end_in_draw_if_there_is_not_way_to_make_turn_but_player_collect_same_count_of_stones() {
            final Player turnPlayer = playerOne;
        
            final int[][] init = {
                {0, 0, 0, 4, 0, 0},
                {0, 2, 0, 0, 0, 0}
            };
        
            playerStores[0] = new PlayerStoreMock(30);
            playerStores[1] = new PlayerStoreMock(36);
        
            fillPlayerHouses(init);
        
            objectUnderTest.makeTurn(new PlayerHouseMock(turnPlayer, 1));
    
            assertThat(objectUnderTest.getStatus())
                .as("Game is finished.")
                .isEqualTo(GameStatus.Finished);
    
            assertThat(objectUnderTest.getWinner())
                .as("The game is ended in draw.")
                .isNull();
        }
        
        @Test
        public void should_continue_seeding_stones_after_finish_opposite_houses_with_skipping_opponent_store() {
            final Player turnPlayer = playerOne;
            
            final int[][] init = {
                {0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 10}
            };
            
            fillPlayerHouses(init);
            
            objectUnderTest.makeTurn(new PlayerHouseMock(turnPlayer, 5));
            
            final KalahGameDesk gameDesk = objectUnderTest.getGameDesk();
    
            assertThatHousesHasCorrentNumberOfStones(gameDesk);
    
            assertThatStonesArePutOnlyToTurnPlayerStore();
        }
    
        private void assertThatHousesHasCorrentNumberOfStones(final KalahGameDesk gameDesk) {
            assertThat(gameDesk.getPlayerHouses(playerOne))
                .as("Player %s has correct state.", playerOne)
                .extracting(DeskLocation::getStones)
                .containsExactly(1, 1, 0, 0, 0, 0);
        
            assertThat(gameDesk.getPlayerHouses(playerTwo))
                .as("Player %s has correct state.", playerTwo)
                .extracting(DeskLocation::getStones)
                .containsExactly(1, 1, 2, 0, 1, 1);
        }
    
        private void assertThatStonesArePutOnlyToTurnPlayerStore() {
            assertThat(playerStores[0].getStones())
                .as("Player %s stones has been capture and move to store", playerOne)
                .isEqualTo(3);
        
            assertThat(playerStores[1].getStones())
                .as("Player %s stones has been capture and move to store", playerTwo)
                .isEqualTo(0);
        }
    
        private void assertGameIsFinishedAndTurnPlayerHasWon(final Player turnPlayer) {
            assertThat(objectUnderTest.getStatus())
                .as("Game is finished.")
                .isEqualTo(GameStatus.Finished);
            
            assertThat(objectUnderTest.getWinner())
                .as("The player %s has won the game.", turnPlayer)
                .isEqualTo(turnPlayer);
        }
        
        private void assertThatDeskHasExpectedStateAfterCapturing(final KalahGameDesk gameDesk) {
            assertThat(gameDesk.getPlayerHouses(playerOne))
                .as("Player %s has correct state.", playerOne)
                .extracting(DeskLocation::getStones)
                .containsExactly(0, 0, 1, 0, 0, 0);
            
            assertThat(gameDesk.getPlayerHouses(playerTwo))
                .as("Player %s has correct state.", playerTwo)
                .extracting(DeskLocation::getStones)
                .containsExactly(0, 0, 0, 3, 0, 0);
        }
        
        private void assertThatStonesIsCaptured() {
            assertThat(playerStores[0].getStones())
                .as("Player %s stones has been capture and move to store", playerOne)
                .isEqualTo(6);
        }
        
        private void fillPlayerHouses(final int[][] init) {
            for (int i = 0; i < init.length; i++) {
                int[] houses = init[i];
                Player player = i == 0 ? playerTwo : playerOne;
                for (int j = 0; j < houses.length; j++) {
                    final int playerIndex = 1 - i;
                    final int houseIndex = Math.abs(j - 5 * playerIndex);
                    playerHouses[playerIndex][houseIndex] = new PlayerHouseMock(player, houseIndex, houses[j]);
                }
            }
        }
    }
    
    private static class PlayerHouseBuilderMock implements PlayerHouseBuilder {
        
        private Map<String, Deque<PlayerHouseMock>[]> createdHouse = new HashMap<>();
        
        private int stones;
        private Player activePlayer;
        
        @Override
        public PlayerHouseBuilder withStones(final int stones) {
            this.stones = stones;
            return this;
        }
        
        @Override
        public PlayerHouse andIndex(final int houseIndex) {
            final PlayerHouseMock playerHouseMock = new PlayerHouseMock(activePlayer, houseIndex, stones);
            final Deque<PlayerHouseMock> playerHouseMocks = getPlayerHouseMocks(activePlayer, houseIndex);
            
            playerHouseMocks.add(playerHouseMock);
            
            return playerHouseMock;
        }
        
        private void setActivePlayer(final Player activePlayer) {
            this.activePlayer = activePlayer;
        }
        
        private PlayerHouseMock getLastPlayerHouseMocks(final Player player, final int houseIndex) {
            final Deque<PlayerHouseMock> playerHouseMocks = getPlayerHouseMocks(player, houseIndex);
            return playerHouseMocks == null ? null : playerHouseMocks.getLast();
        }
        
        private Deque<PlayerHouseMock> getPlayerHouseMocks(final Player player, final int houseIndex) {
            Deque<PlayerHouseMock>[] createdPlayerOneHouse = getPlayerHouses(player);
            Deque<PlayerHouseMock> playerHouseMocks = createdPlayerOneHouse[houseIndex];
            if (playerHouseMocks == null) {
                playerHouseMocks = new LinkedList<>();
                createdPlayerOneHouse[houseIndex] = playerHouseMocks;
            }
            return playerHouseMocks;
        }
        
        private Deque<PlayerHouseMock>[] getPlayerHouses(final Player player) {
            return createdHouse.computeIfAbsent(player.getId(), k -> new Deque[6]);
        }
    }
    
    private static class PlayerHouseMock implements PlayerHouse {
        
        private final Player player;
        private final int houseIndex;
        private int stones;
        
        private PlayerHouseMock(final Player player, final int houseIndex, final int stones) {
            this.player = player;
            this.houseIndex = houseIndex;
            this.stones = stones;
        }
        
        private PlayerHouseMock(final Player player) {
            this(player, 0, 0);
        }
        
        private PlayerHouseMock(final Player player, final int houseIndex) {
            this(player, houseIndex, 0);
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
            stones = 0;
            return this;
        }
        
        @Override
        public PlayerHouse putStone() {
            stones++;
            return this;
        }
    }
    
    private static class PlayerStoreMock implements PlayerStore {
        
        private int stones;
        
        private PlayerStoreMock() {stones = 0;}
        
        private PlayerStoreMock(final int stones) {
            this.stones = stones;
        }
        
        @Override
        public int getStones() {
            return stones;
        }
        
        @Override
        public PlayerStore putStone(final int stones) {
            this.stones += stones;
            return this;
        }
    }
}
