
package com.riaancornelius.bot.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "RegisteredPlayerEntities",
    "CurrentRound",
    "MapHeight",
    "MapWidth",
    "GameBlocks",
    "MapSeed"
})
public class State {

    @JsonProperty("RegisteredPlayerEntities")
    private List<RegisteredPlayerEntity> registeredPlayerEntities = new ArrayList<RegisteredPlayerEntity>();
    @JsonProperty("CurrentRound")
    private Integer currentRound;
    @JsonProperty("MapHeight")
    private Integer mapHeight;
    @JsonProperty("MapWidth")
    private Integer mapWidth;
    @JsonProperty("GameBlocks")
    private List<List<GameBlock>> gameBlocks = new ArrayList<List<GameBlock>>();
    @JsonProperty("MapSeed")
    private Integer mapSeed;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The registeredPlayerEntities
     */
    @JsonProperty("RegisteredPlayerEntities")
    public List<RegisteredPlayerEntity> getRegisteredPlayerEntities() {
        return registeredPlayerEntities;
    }

    /**
     * 
     * @param registeredPlayerEntities
     *     The RegisteredPlayerEntities
     */
    @JsonProperty("RegisteredPlayerEntities")
    public void setRegisteredPlayerEntities(List<RegisteredPlayerEntity> registeredPlayerEntities) {
        this.registeredPlayerEntities = registeredPlayerEntities;
    }

    /**
     * 
     * @return
     *     The currentRound
     */
    @JsonProperty("CurrentRound")
    public Integer getCurrentRound() {
        return currentRound;
    }

    /**
     * 
     * @param currentRound
     *     The CurrentRound
     */
    @JsonProperty("CurrentRound")
    public void setCurrentRound(Integer currentRound) {
        this.currentRound = currentRound;
    }

    /**
     * 
     * @return
     *     The mapHeight
     */
    @JsonProperty("MapHeight")
    public Integer getMapHeight() {
        return mapHeight;
    }

    /**
     * 
     * @param mapHeight
     *     The MapHeight
     */
    @JsonProperty("MapHeight")
    public void setMapHeight(Integer mapHeight) {
        this.mapHeight = mapHeight;
    }

    /**
     * 
     * @return
     *     The mapWidth
     */
    @JsonProperty("MapWidth")
    public Integer getMapWidth() {
        return mapWidth;
    }

    /**
     * 
     * @param mapWidth
     *     The MapWidth
     */
    @JsonProperty("MapWidth")
    public void setMapWidth(Integer mapWidth) {
        this.mapWidth = mapWidth;
    }

    /**
     * 
     * @return
     *     The gameBlocks
     */
    @JsonProperty("GameBlocks")
    public List<List<GameBlock>> getGameBlocks() {
        return gameBlocks;
    }

    /**
     * 
     * @param gameBlocks
     *     The GameBlocks
     */
    @JsonProperty("GameBlocks")
    public void setGameBlocks(List<List<GameBlock>> gameBlocks) {
        this.gameBlocks = gameBlocks;
    }

    /**
     * 
     * @return
     *     The mapSeed
     */
    @JsonProperty("MapSeed")
    public Integer getMapSeed() {
        return mapSeed;
    }

    /**
     * 
     * @param mapSeed
     *     The MapSeed
     */
    @JsonProperty("MapSeed")
    public void setMapSeed(Integer mapSeed) {
        this.mapSeed = mapSeed;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
