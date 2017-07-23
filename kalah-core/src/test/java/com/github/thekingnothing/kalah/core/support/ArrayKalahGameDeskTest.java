package com.github.thekingnothing.kalah.core.support;

import com.github.thekingnothing.kalah.core.KalahGameDesk;
import com.github.thekingnothing.kalah.core.PlayerHouse;
import com.github.thekingnothing.kalah.core.PlayerStore;
import com.github.thekingnothing.kalah.core.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ArrayKalahGameDeskTest {
    
    private static final int INIT_STONES_COUNT = 6;
    
    private KalahGameDesk objectUnderTest;
    private Player playerOne;
    private Player playerTwo;
    
    @Before
    public void setUp() throws Exception {
        playerOne = new PlayerImpl("A");
        playerTwo = new PlayerImpl("B");
        objectUnderTest = new ArrayKalahGameDesk(new Players(playerOne, playerTwo), INIT_STONES_COUNT);
    }
    
    @Test
    public void should_have_an_6_stones_in_each_slot_after_start() {
        final Integer[] expectedStones = Stream.generate(() -> INIT_STONES_COUNT).limit(6).toArray(Integer[]::new);
        
        assertPlayerHousesHasStones(playerOne, expectedStones);
        assertPlayerHousesHasStones(playerTwo, expectedStones);
    }
    
    @Test
    public void should_have_player_store_after_start() {
        assertThatPlayerStoreHasStones(playerOne, 0);
        assertThatPlayerStoreHasStones(playerTwo, 0);
    }
    
    @Test
    public void should_move_stones_out_of_desk_when_pick_up_from_house() {
        final ArrayKalahGameDesk kalahGameDesk = (ArrayKalahGameDesk) objectUnderTest.pickUpAllStones(playerOne, 0);
        
        assertThat(kalahGameDesk.getStonesOutOfDesk())
            .as("Stones moved out of desk")
            .isEqualTo(INIT_STONES_COUNT);
    }
    
    @Test
    public void should_return_a_instance_when_pick_up_stones() {
        final KalahGameDesk kalahGameDesk = objectUnderTest.pickUpAllStones(playerOne, 0);
        
        assertThat(kalahGameDesk)
            .as("A new instance is returned")
            .isNotSameAs(objectUnderTest);
    }
    
    @Test
    public void should_move_stones_counter_clockwise() {
        final KalahGameDesk kalahGameDesk = objectUnderTest.pickUpAllStones(playerOne, 0)
                                                           .putStonesToPlayerHouses(playerOne, 1);
        
        final Integer[] expectedStones = {0, 7, 7, 7, 7, 7};
        assertPlayerHousesHasStones(kalahGameDesk, playerOne, expectedStones);
    }
    
    @Test
    public void should_keep_last_house_after_moving() {
        final KalahGameDesk kalahGameDesk = objectUnderTest.pickUpAllStones(playerOne, 1)
                                                           .putStonesToPlayerHouses(playerOne, 2)
                                                           .putStoneToPlayerStore(playerOne)
                                                           .putStonesToPlayerHouses(playerTwo, 0);
        
        assertThatLastLocationIsExpectedPlayerHouse(kalahGameDesk);
    }
    
    @Test
    public void should_put_stone_to_player_store_when_stones_out_of_desk_more_than_counter_clockwise_houses() {
        final KalahGameDesk kalahGameDesk = objectUnderTest.pickUpAllStones(playerOne, 0)
                                                           .putStonesToPlayerHouses(playerOne, 1);
        assertThatPlayerStoreHasStones(kalahGameDesk, playerOne, 0);
    }
    
    @Test
    public void should_keep_player_store_as_last_location_after_putting_stones_to_store() {
        final KalahGameDesk kalahGameDesk = objectUnderTest.pickUpAllStones(playerOne, 1)
                                                           .putStonesToPlayerHouses(playerOne, 2)
                                                           .putStoneToPlayerStore(playerOne);
        
        assertThat(kalahGameDesk.getLastLocation())
            .as("Player house is last location")
            .isInstanceOf(PlayerStore.class);
    }
    
    @Test
    public void should_keep_stones_out_of_desk_if_stones_more_than_counter_clockwise_houses() {
        final ArrayKalahGameDesk kalahGameDesk = (ArrayKalahGameDesk) objectUnderTest.pickUpAllStones(playerOne, 1)
                                                                                     .putStonesToPlayerHouses(playerOne, 2);
        
        assertThat(kalahGameDesk.getStonesOutOfDesk())
            .as("Stones are kept out of desk")
            .isEqualTo(2);
    }
    
    private void assertThatLastLocationIsExpectedPlayerHouse(final KalahGameDesk kalahGameDesk) {
        assertThat(kalahGameDesk.getLastLocation())
            .as("Player house is last location")
            .isInstanceOf(PlayerHouse.class);
        
        
        assertThat((PlayerHouse) kalahGameDesk.getLastLocation())
            .as("Player %s house with index %s is last location.", playerTwo, 0)
            .extracting(PlayerHouse::getPlayer, PlayerHouse::getHouseIndex)
            .contains(playerTwo, 0);
    }
    
    
    private void assertPlayerHousesHasStones(final Player player, final Integer[] stonesCount) {
        assertPlayerHousesHasStones(objectUnderTest, player, stonesCount);
    }
    
    private void assertPlayerHousesHasStones(final KalahGameDesk gameDesk, final Player player,
                                             final Integer[] stonesCount) {
        final List<PlayerHouse> playerOneHouses = gameDesk.getPlayerHouses(player);
        assertThatPlayerHousesHasStones(player, playerOneHouses, stonesCount);
    }
    
    private void assertThatPlayerHousesHasStones(final Player player, final List<PlayerHouse> playerHouses,
                                                 final Integer[] stonesCount) {
        assertThat(playerHouses)
            .as("Player % houses have % stones", player, stonesCount)
            .extracting(PlayerHouse::getStones)
            .containsExactly(stonesCount);
    }
    
    private void assertThatPlayerStoreHasStones(final Player player, final int expectedStoneCount) {
        assertThatPlayerStoreHasStones(objectUnderTest, player, expectedStoneCount);
    }
    
    private void assertThatPlayerStoreHasStones(final KalahGameDesk gameDesk, final Player player, final int expectedStoneCount) {
        PlayerStore playerOneStore = gameDesk.getPlayerStore(player);
        assertThat(playerOneStore.getStones())
            .as("Player %s store is has %s stones.", player, expectedStoneCount)
            .isEqualTo(expectedStoneCount);
    }
    
}