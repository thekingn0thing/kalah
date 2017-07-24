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
import com.github.thekingnothing.kalah.core.PlayerStore;
import com.github.thekingnothing.kalah.core.Player;

import java.util.List;

class WinCondition {
    
    private final Player player;
    private final KalahGameDesk gameDesk;
    private final int halfOfStones;
    
    WinCondition(final KalahGameDesk gameDesk, final Player player, final int halfOfStones) {
        this.gameDesk = gameDesk;
        this.player = player;
        this.halfOfStones = halfOfStones;
    }
    
    boolean check() {
        return  playerCollectedMoreThanHalfOfStones();
    }
    
    private boolean playerCollectedMoreThanHalfOfStones() {
        final PlayerStore turnPlayerStore = gameDesk.getPlayerStore(player);
        return turnPlayerStore.getStones() > halfOfStones;
    }

}
