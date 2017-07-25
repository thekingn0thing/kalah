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

package com.github.thekingnothing.kalah.rest;


import com.github.thekingnothing.kalah.core.model.GameData;
import com.github.thekingnothing.kalah.rest.mappers.GameDataMapper;
import com.github.thekingnothing.kalah.rest.models.GameTurnData;
import com.github.thekingnothing.kalah.rest.models.JsonGameData;
import com.github.thekingnothing.kalah.rest.models.GameStartData;
import com.github.thekingnothing.kalah.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KalahRestController {
    
    @Autowired
    private GameService gameService;
    
    @Autowired
    private GameDataMapper gameDataMapper;
    
    @RequestMapping(
        value = "/game/start",
        method = RequestMethod.POST,
        headers = "Accept=application/json",
        produces = "application/json"
    )
    public @ResponseBody JsonGameData startGame(@RequestBody final GameStartData data) {
        final GameData gameData = gameService.startGame(data.getPlayerOne(), data.getPlayerTwo());
        return gameDataMapper.map(gameData);
    }
    
    @RequestMapping(
        value = "/game/turn",
        method = RequestMethod.POST,
        headers = "Accept=application/json",
        produces = "application/json"
    )
    public @ResponseBody JsonGameData startGame(@RequestBody final GameTurnData data) {
        final GameData gameData = gameService.makeTurn(data.getGameId(), data.getPlayerId(), data.getHouseIndex());
        return gameDataMapper.map(gameData);
    }
}
