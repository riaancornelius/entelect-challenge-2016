package com.riaancornelius.bot.data;

import com.riaancornelius.bot.state.Entity;
import com.riaancornelius.bot.state.GameBlock;

/**
 * @author Riaan
 */
public enum MapEntity {
    INDESTRUCTIBLE_WALL("Domain.Entities.IndestructibleWallEntity, Domain", 1000),
    WALL("Domain.Entities.DestructibleWallEntity, Domain", 100),
    PLAYER("Domain.Entities.PlayerEntity, Domain", 500),
    EXPLOSION("", 50),
    BOMB("", 3000), //Bomb":{"Owner":{"Name":"Jamie wants big boom","Key":"A","Points":0,"Killed":false,"BombBag":1,"BombRadius":1,"Location":{"X":3,"Y":2}},"BombRadius":1,"BombTimer":4,"IsExploding":false,"Location":{"X":3,"Y":2}},"PowerUp":null,"Exploding":false,"Location":{"X":3,"Y":2}}
    POWERUP_SUPER("Domain.Entities.PowerUps.SuperPowerUp, Domain", 1),
    POWERUP_BOMB_RADIUS("Domain.Entities.PowerUps.BombRaduisPowerUpEntity, Domain", 1),
    POWERUP_BOMB_BAG("Domain.Entities.PowerUps.BombBagPowerUpEntity, Domain", 1),
    OPEN("", 10);

    String domainString;
    private int cost;

    MapEntity(String domainString, int cost) {
        this.domainString = domainString;
        this.cost = cost;
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

    public int getCost() {
        return cost;
    }
}
