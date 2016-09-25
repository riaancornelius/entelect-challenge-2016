
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
    "Name",
    "Key",
    "Points",
    "Killed",
    "BombBag",
    "BombRadius",
    "Location"
})
public class RegisteredPlayerEntity {

    @JsonProperty("Name")
    private String name;
    @JsonProperty("Key")
    private String key;
    @JsonProperty("Points")
    private Integer points;
    @JsonProperty("Killed")
    private Boolean killed;
    @JsonProperty("BombBag")
    private Integer bombBag;
    @JsonProperty("BombRadius")
    private Integer bombRadius;
    @JsonProperty("Location")
    private Location location;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The name
     */
    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The Name
     */
    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The key
     */
    @JsonProperty("Key")
    public String getKey() {
        return key;
    }

    /**
     * 
     * @param key
     *     The Key
     */
    @JsonProperty("Key")
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 
     * @return
     *     The points
     */
    @JsonProperty("Points")
    public Integer getPoints() {
        return points;
    }

    /**
     * 
     * @param points
     *     The Points
     */
    @JsonProperty("Points")
    public void setPoints(Integer points) {
        this.points = points;
    }

    /**
     * 
     * @return
     *     The killed
     */
    @JsonProperty("Killed")
    public Boolean getKilled() {
        return killed;
    }

    /**
     * 
     * @param killed
     *     The Killed
     */
    @JsonProperty("Killed")
    public void setKilled(Boolean killed) {
        this.killed = killed;
    }

    /**
     * 
     * @return
     *     The bombBag
     */
    @JsonProperty("BombBag")
    public Integer getBombBag() {
        return bombBag;
    }

    /**
     * 
     * @param bombBag
     *     The BombBag
     */
    @JsonProperty("BombBag")
    public void setBombBag(Integer bombBag) {
        this.bombBag = bombBag;
    }

    /**
     * 
     * @return
     *     The bombRadius
     */
    @JsonProperty("BombRadius")
    public Integer getBombRadius() {
        return bombRadius;
    }

    /**
     * 
     * @param bombRadius
     *     The BombRadius
     */
    @JsonProperty("BombRadius")
    public void setBombRadius(Integer bombRadius) {
        this.bombRadius = bombRadius;
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
