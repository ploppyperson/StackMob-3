package me.healpotion.stackmob.events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class StackKilledEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player p;
    private int numberKilled;
    private EntityType type;

    public StackKilledEvent(Player killer, EntityType type, int numberKilled) {
        p = killer;
        this.numberKilled = numberKilled;
        this.type = type;
    }

    static public HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return p;
    }

    public int getNumberKilled() {
        return this.numberKilled;
    }

    public EntityType getType() {
        return this.type;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
