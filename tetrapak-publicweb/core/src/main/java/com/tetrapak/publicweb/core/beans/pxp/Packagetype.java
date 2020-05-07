
package com.tetrapak.publicweb.core.beans.pxp;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "name",
    "shapes",
    "openingclosures",
    "fillingmachines"
})
public class Packagetype {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("shapes")
    private List<Shape> shapes;
    @JsonProperty("openingclosures")
    private List<Openingclosure> openingclosures;
    @JsonProperty("fillingmachines")
    private List<FillingMachine> fillingmachines;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("shapes")
    public List<Shape> getShapes() {
        return new ArrayList<>(shapes);
    }

    @JsonProperty("shapes")
    public void setShapes(List<Shape> shapes) {
        this.shapes = new ArrayList<>(shapes);
    }
    
    @JsonProperty("fillingmachines")
    public List<FillingMachine> getFillingmachines() {
        return new ArrayList<>(fillingmachines);
    }

    @JsonProperty("openingclosures")
    public void setOpeningclosures(List<Openingclosure> openingclosures) {
        this.openingclosures = new ArrayList<>(openingclosures);
    }
    
    @JsonProperty("openingclosures")
    public List<Openingclosure> getOpeningclosures() {
        return new ArrayList<>(openingclosures);
    }

    @JsonProperty("fillingmachines")
    public void setFillingMachines(List<FillingMachine> fillingmachines) {
        this.fillingmachines = new ArrayList<>(fillingmachines);
    }

}
