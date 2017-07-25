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

package com.github.thekingnothing.kalah.repository.impl;


import com.github.thekingnothing.kalah.core.Player;
import com.github.thekingnothing.kalah.core.model.GameData;
import com.github.thekingnothing.kalah.core.support.PlayerImpl;
import com.github.thekingnothing.kalah.repository.GameRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class HashMapGameRepository implements GameRepository {
    
    private Map<String, Player> players = new ConcurrentHashMap<>();
    private Map<String, GameData> gameDatas = new ConcurrentHashMap<>();
    
    @Override
    public Player getPlayer(final String playerId) {
        return players.computeIfAbsent(playerId, PlayerImpl::new);
    }
    
    @Override
    public void save(final GameData gameData) {
        gameDatas.put(gameData.getGameId(), gameData);
    }
    
    @Override
    public GameData load(final String gameId) {
        return gameDatas.get(gameId);
    }
}
