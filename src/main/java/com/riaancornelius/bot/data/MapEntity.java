package com.riaancornelius.bot.data;

/**
 * @author Riaan
 */
public enum MapEntity {
    INDESTRUCTIBLE_WALL("Domain.Entities.IndestructibleWallEntity, Domain"),
    WALL("Domain.Entities.DestructibleWallEntity, Domain"),
    PLAYER("Domain.Entities.PlayerEntity, Domain"),
    OPEN("");

    String domainString;

    MapEntity(String domainString) {
        this.domainString = domainString;
    }

    public static MapEntity parse(String domainString){
        for (MapEntity mapEntity : MapEntity.values()) {
            if (mapEntity.domainString.equals(domainString)){
                return mapEntity;
            }
        }
        System.out.println(domainString);
        throw new RuntimeException("Unexpected domain object found: " + domainString);
    }
}
