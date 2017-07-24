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

import com.github.thekingnothing.kalah.core.Player;

import java.util.Objects;

public class PlayerImpl implements Player {
    private final String id;
    
    public PlayerImpl(final String id) {this.id = id;}
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) { return true; }
        if (!(o instanceof Player)) { return false; }
        final Player player = (Player) o;
        return Objects.equals(getId(), player.getId());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Player{");
        sb.append("id='").append(id).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
