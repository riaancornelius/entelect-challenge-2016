package com.riaancornelius.bot.data;

import com.riaancornelius.bot.state.Entity;
import com.riaancornelius.bot.state.GameBlock;

/**
 * @author Riaan
 */
public enum MapEntity {
    INDESTRUCTIBLE_WALL("Domain.Entities.IndestructibleWallEntity, Domain"),
    WALL("Domain.Entities.DestructibleWallEntity, Domain"),
    PLAYER("Domain.Entities.PlayerEntity, Domain"),
    EXPLOSION(""),
    BOMB(""), //Bomb":{"Owner":{"Name":"Jamie wants big boom","Key":"A","Points":0,"Killed":false,"BombBag":1,"BombRadius":1,"Location":{"X":3,"Y":2}},"BombRadius":1,"BombTimer":4,"IsExploding":false,"Location":{"X":3,"Y":2}},"PowerUp":null,"Exploding":false,"Location":{"X":3,"Y":2}}
    POWERUP_SUPER("Domain.Entities.PowerUps.SuperPowerUp, Domain"),
    POWERUP_BOMB_RADIUS("Domain.Entities.PowerUps.BombRaduisPowerUpEntity, Domain"),
    POWERUP_BOMB_BAG("Domain.Entities.PowerUps.BombBagPowerUpEntity, Domain"),
    OPEN("");

    String domainString;

    MapEntity(String domainString) {
        this.domainString = domainString;
    }

    public static MapEntity parse(GameBlock data){
        if (data.getEntity()==null) {
            if (data.getExploding()) {
                return EXPLOSION;
            } else if (data.getBomb() != null) {
                return BOMB;
            } else if (data.getPowerUp() != null) {
                Entity powerUp = data.getPowerUp();
                if (POWERUP_SUPER.domainString.equals(powerUp.get$type())) {
                    return POWERUP_SUPER;
                } else if (POWERUP_BOMB_BAG.domainString.equals(powerUp.get$type())) {
                    return POWERUP_BOMB_BAG;
                } else if (POWERUP_BOMB_RADIUS.domainString.equals(powerUp.get$type())) {
                    return POWERUP_BOMB_RADIUS;
                } else {
                    System.out.println("UNKNOWN POWERUP: " + powerUp.get$type());
                }
                return OPEN;
            } else {
                return OPEN;
            }
        } else {
            String domainString = data.getEntity().get$type();
            for (MapEntity mapEntity : MapEntity.values()) {
                if (mapEntity.domainString.equals(domainString)) {
                    return mapEntity;
                }
            }
            System.out.println(domainString);
            throw new RuntimeException("Unexpected domain object found: " + domainString);
        }
    }
}
