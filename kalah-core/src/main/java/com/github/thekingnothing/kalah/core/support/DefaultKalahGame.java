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
import com.github.thekingnothing.kalah.core.exception.GameAlreadyStartedException;
import com.github.thekingnothing.kalah.core.model.GameData;
import com.github.thekingnothing.kalah.core.model.GameStatus;
import com.github.thekingnothing.kalah.core.exception.IllegalPlayerException;
import com.github.thekingnothing.kalah.core.exception.IllegalTurnException;
import com.github.thekingnothing.kalah.core.KalahGame;
import com.github.thekingnothing.kalah.core.KalahGameDesk;
import com.github.thekingnothing.kalah.core.PlayerHouse;
import com.github.thekingnothing.kalah.core.PlayerStore;
import com.github.thekingnothing.kalah.core.Player;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

class DefaultKalahGame implements KalahGame {
    
    static final int HOUSES_PER_PLAYER = 6;
    static final int MAX_HOUSES_INDEX = HOUSES_PER_PLAYER - 1;
    
    private final int initStonesCount;
    private final int halfOfStones;
    
    private Players players;
    private GameStatus status;
    private KalahGameDesk gameDesk;
    private Player nextPlayer;
    private Player winner;
    private boolean captureResult;
    
    DefaultKalahGame(final int initStonesCount) {
        this.initStonesCount = initStonesCount;
        this.halfOfStones = calculateHalfOfStones(initStonesCount);
    }
    
    DefaultKalahGame(final int initStonesCount, final GameStatus status, final Players players, final KalahGameDesk gameDesk,
                     final Player nextPlayer, final Player winnerPlayer) {
        this.initStonesCount = initStonesCount;
        this.halfOfStones = calculateHalfOfStones(initStonesCount);
        this.status = status;
        this.players = players;
        this.gameDesk = gameDesk;
        this.nextPlayer = nextPlayer;
        this.winner = winnerPlayer;
    }
    
    private int calculateHalfOfStones(final int initStonesCount) {
        return HOUSES_PER_PLAYER * initStonesCount;
    }
    
    @Override
    public void start(final Player playerOne, final Player playerTwo) {
        if (status == GameStatus.Started) {
            throw new GameAlreadyStartedException("Game is already started.");
        }
        if (playerOne.equals(playerTwo)){
            throw new IllegalArgumentException("Players should be different.");
        }
        
        
        players = new Players(playerOne, playerTwo);
        nextPlayer = playerOne;
        gameDesk = new ArrayKalahGameDesk(players, initStonesCount);
        status = GameStatus.Started;
    }
    
    @Override
    public KalahGameDesk getGameDesk() {
        return gameDesk;
    }
    
    @Override
    public GameData toGameData() {
        final GameDataImpl gameData = new GameDataImpl();
    
        final Player playerOne = players.getPlayerOne();
        final Player playerTwo = players.getPlayerTwo();
    
        gameData.setStatus(status);
        gameData.setPlayerOneId(playerOne.getId());
        gameData.setPlayerTwoId(playerTwo.getId());
        gameData.setNextPlayerId(nextPlayer.getId());
        gameData.setPlayerOneStore(gameDesk.getPlayerStore(playerOne).getStones());
        gameData.setPlayerTwoStore(gameDesk.getPlayerStore(playerTwo).getStones());
        
        gameData.setPlayerOneHouses(
            gameDesk.getPlayerHouses(playerOne).stream()
                    .mapToInt(DeskLocation::getStones)
                    .boxed()
                    .collect(Collectors.toList())
        );
        
        gameData.setPlayerTwoHouses(
            gameDesk.getPlayerHouses(playerTwo).stream()
                    .mapToInt(DeskLocation::getStones)
                    .boxed()
                    .collect(Collectors.toList())
        );
        
        return gameData;
    }
    
    @Override
    public GameStatus getStatus() {
        return status;
    }
    
    @Override
    public void makeTurn(final PlayerHouse startHouse) {
        validateTurn(startHouse);
        
        final Player turnPlayer = startHouse.getPlayer();
        final Player opponent = players.getOpponent(turnPlayer);
        
        pickUpAllStones(startHouse, turnPlayer);
        moveStones(startHouse, turnPlayer, opponent);
        tryToCaptureStones(turnPlayer, opponent);
        determinateNextPlayer(turnPlayer, opponent);
        finishTheGameIfRequired(turnPlayer, opponent);
    }
    
    Player getWinner() {
        return winner;
    }
    
    Player getPlayerOne() {
        return players.getPlayerOne();
    }
    
    Player getPlayerTwo() {
        return players.getPlayerTwo();
    }
    
    Player getNextPlayer() {
        return nextPlayer;
    }
    
    private void finishTheGameIfRequired(final Player turnPlayer, final Player opponent) {
        if (nextPlayerDoesNotHaveStones()) {
            this.status = GameStatus.Finished;
            gameDesk = gameDesk.pickUpAllStones()
                               .putAllStoneToPlayerStore(turnPlayer);
            checkWinningConditions(turnPlayer, opponent);
        } else if (checkWinningConditions(turnPlayer)) {
            this.status = GameStatus.Finished;
            this.winner = turnPlayer;
        }
    }
    
    private boolean nextPlayerDoesNotHaveStones() {
        final List<PlayerHouse> playerHouses = gameDesk.getPlayerHouses(nextPlayer);
        
        boolean turnPlayerWin = true;
        int index = 0;
        while (turnPlayerWin && index < playerHouses.size()) {
            if (playerHouses.get(index).getStones() > 0) {
                turnPlayerWin = false;
            }
            index++;
        }
        return turnPlayerWin;
    }
    
    private void checkWinningConditions(final Player turnPlayer, final Player opponent) {
        if (checkWinningConditions(turnPlayer)) {
            winner = turnPlayer;
        } else if (checkWinningConditions(opponent)) {
            winner = opponent;
        }
    }
    
    private boolean checkWinningConditions(final Player turnPlayer) {
        return new WinCondition(gameDesk, turnPlayer, halfOfStones).check();
    }
    
    private void pickUpAllStones(final PlayerHouse startHouse, final Player turnPlayer) {
        gameDesk = gameDesk.pickUpAllStones(turnPlayer, startHouse.getHouseIndex());
        assertThatHouseHasStones();
    }
    
    private void validateTurn(final PlayerHouse startHouse) {
        new TurnValidator(startHouse).validate();
    }
    
    
    private void tryToCaptureStones(final Player turnPlayer, final Player opponent) {
        captureResult = false;
        if (gameDesk.getLastLocation() instanceof PlayerHouse) {
            final PlayerHouse lastLocation = (PlayerHouse) gameDesk.getLastLocation();
            final int houseIndex = lastLocation.getHouseIndex();
            final int opponentHouseIndex = Math.abs(MAX_HOUSES_INDEX - houseIndex);
            final PlayerHouse opponentHouse = gameDesk.getPlayerHouses(opponent).get(opponentHouseIndex);
            
            if (turnPlayer.equals(lastLocation.getPlayer()) && opponentHouse.getStones() > 0) {
                captureStones(turnPlayer, opponent, houseIndex, opponentHouseIndex);
                captureResult = true;
            }
        }
    }
    
    private void captureStones(final Player turnPlayer, final Player opponent, final int houseIndex, final int opponentHouseIndex) {
        gameDesk = gameDesk.pickUpAllStones(turnPlayer, houseIndex)
                           .pickUpAllStones(opponent, opponentHouseIndex)
                           .putAllStoneToPlayerStore(turnPlayer);
    }
    
    void setGameDesk(final KalahGameDesk gameDesk) {
        this.gameDesk = gameDesk;
    }
    
    private void determinateNextPlayer(final Player turnPlayer, final Player opponent) {
        if (captureResult) {
            nextPlayer = opponent;
        } else {
            if (gameDesk.getLastLocation() instanceof PlayerStore) {
                nextPlayer = turnPlayer;
            } else {
                nextPlayer = opponent;
            }
        }
    }
    
    private void moveStones(final PlayerHouse startHouse, final Player player, final Player opponent) {
        int startIndex = startHouse.getHouseIndex() + 1;
        do {
            gameDesk = gameDesk.putStonesToPlayerHouses(player, startIndex)
                               .putStoneToPlayerStore(player)
                               .putStonesToPlayerHouses(opponent, 0);
            startIndex = 0;
        }
        while (gameDesk.hasStonesOutOfDesk());
    }
    
    private void assertThatHouseHasStones() {
        if (!gameDesk.hasStonesOutOfDesk()) {
            throw new IllegalTurnException("You cannot start your turn from empty house.");
        }
    }
    
    private class TurnValidator {
        private final PlayerHouse startHouse;
        
        private TurnValidator(final PlayerHouse startHouse) {this.startHouse = startHouse;}
        
        private void validate() {
            assertHouseIndexInARange(startHouse);
            assertPlayerInAGame(startHouse.getPlayer());
            assertThatPlayerMakeTurnInOrder(startHouse.getPlayer());
        }
        
        private void assertHouseIndexInARange(final PlayerHouse startHouse) {
            if (startHouse.getHouseIndex() < 0 || MAX_HOUSES_INDEX < startHouse.getHouseIndex()) {
                throw new IllegalTurnException("House index is out of range.");
            }
        }
        
        private void assertPlayerInAGame(final Player player) {
            if (!players.contains(player)) {
                throw new IllegalPlayerException("Player " + player + " cannot make turn, because he did not part of the game.");
            }
        }
        
        private void assertThatPlayerMakeTurnInOrder(final Player turnPlayer) {
            if (!nextPlayer.equals(turnPlayer)) {
                throw new IllegalTurnException("Player " + turnPlayer.getId() + " is tying to make turn out of order.");
            }
        }
    }
}
