package com.riaancornelius.bot.state;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Riaan
 */
public class Bomb {
    @JsonProperty("Owner")
    private RegisteredPlayerEntity owner;
    @JsonProperty("BombRadius")
    private Integer bombRadius;
    @JsonProperty("BombTimer")
    private Integer bombTimer;
    @JsonProperty("IsExploding")
    private Boolean exploding;
    @JsonProperty("Location")
    private Location location;

    public RegisteredPlayerEntity getOwner() {
        return owner;
    }

    public void setOwner(RegisteredPlayerEntity owner) {
        this.owner = owner;
    }

    public Integer getBombRadius() {
        return bombRadius;
    }

    public void setBombRadius(Integer bombRadius) {
        this.bombRadius = bombRadius;
    }

    public Integer getBombTimer() {
        return bombTimer;
    }

    public void setBombTimer(Integer bombTimer) {
        this.bombTimer = bombTimer;
    }

    public Boolean getExploding() {
        return exploding;
    }

    public void setExploding(Boolean exploding) {
        this.exploding = exploding;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
