
package com.riaancornelius.bot.state;

import java.util.HashMap;
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
    "Entity",
    "Bomb",
    "PowerUp",
    "Exploding",
    "Location"
})
public class GameBlock {

    @JsonProperty("Entity")
    private Entity entity;
    @JsonProperty("Bomb")
    private Object bomb;
    @JsonProperty("PowerUp")
    private Object powerUp;
    @JsonProperty("Exploding")
    private Boolean exploding;
    @JsonProperty("Location")
    private Location location;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The entity
     */
    @JsonProperty("Entity")
    public Entity getEntity() {
        return entity;
    }

    /**
     * 
     * @param entity
     *     The Entity
     */
    @JsonProperty("Entity")
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    /**
     * 
     * @return
     *     The bomb
     */
    @JsonProperty("Bomb")
    public Object getBomb() {
        return bomb;
    }

    /**
     * 
     * @param bomb
     *     The Bomb
     */
    @JsonProperty("Bomb")
    public void setBomb(Object bomb) {
        this.bomb = bomb;
    }

    /**
     * 
     * @return
     *     The powerUp
     */
    @JsonProperty("PowerUp")
    public Object getPowerUp() {
        return powerUp;
    }

    /**
     * 
     * @param powerUp
     *     The PowerUp
     */
    @JsonProperty("PowerUp")
    public void setPowerUp(Object powerUp) {
        this.powerUp = powerUp;
    }

    /**
     * 
     * @return
     *     The exploding
     */
    @JsonProperty("Exploding")
    public Boolean getExploding() {
        return exploding;
    }

    /**
     * 
     * @param exploding
     *     The Exploding
     */
    @JsonProperty("Exploding")
    public void setExploding(Boolean exploding) {
        this.exploding = exploding;
    }

    /**
     * 
     * @return
     *     The location
     */
    @JsonProperty("Location")
    public Location getLocation() {
        return location;
    }

    /**
     * 
     * @param location
     *     The Location
     */
    @JsonProperty("Location")
    public void setLocation(Location location) {
        this.location = location;
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
