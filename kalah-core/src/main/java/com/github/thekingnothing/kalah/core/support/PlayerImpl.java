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
