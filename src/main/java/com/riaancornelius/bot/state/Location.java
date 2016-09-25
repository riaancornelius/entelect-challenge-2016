
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
    "X",
    "Y"
})
public class Location {

    @JsonProperty("X")
    private Integer x;
    @JsonProperty("Y")
    private Integer y;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The x
     */
    @JsonProperty("X")
    public Integer getX() {
        return x;
    }

    /**
     * 
     * @param x
     *     The X
     */
    @JsonProperty("X")
    public void setX(Integer x) {
        this.x = x;
    }

    /**
     * 
     * @return
     *     The y
     */
    @JsonProperty("Y")
    public Integer getY() {
        return y;
    }

    /**
     * 
     * @param y
     *     The Y
     */
    @JsonProperty("Y")
    public void setY(Integer y) {
        this.y = y;
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
