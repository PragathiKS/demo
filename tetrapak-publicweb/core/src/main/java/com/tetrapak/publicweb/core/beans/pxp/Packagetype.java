
package com.tetrapak.publicweb.core.beans.pxp;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class Packagetype.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "name", "shapes", "openingclosures", "fillingmachines" })
public class Packagetype {

    /** The id. */
    @JsonProperty("id")
    private String id;

    /** The name. */
    @JsonProperty("name")
    private String name;

    /** The shapes. */
    @JsonProperty("shapes")
    private List<Shape> shapes = null;

    /** The openingclosures. */
    @JsonProperty("openingclosures")
    private List<Openingclosure> openingclosures = null;

    /** The fillingmachines. */
    @JsonProperty("fillingmachines")
    private List<FillingMachine> fillingmachines = null;

    /**
     * Gets the id.
     *
     * @return the id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the shapes.
     *
     * @return the shapes
     */
    @JsonProperty("shapes")
    public List<Shape> getShapes() {
        return shapes;
    }

    /**
     * Sets the shapes.
     *
     * @param shapes
     *            the new shapes
     */
    @JsonProperty("shapes")
    public void setShapes(List<Shape> shapes) {
        this.shapes = shapes;
    }

    /**
     * Gets the fillingmachines.
     *
     * @return the fillingmachines
     */
    @JsonProperty("fillingmachines")
    public List<FillingMachine> getFillingmachines() {
        return fillingmachines;
    }

    /**
     * Sets the openingclosures.
     *
     * @param openingclosures
     *            the new openingclosures
     */
    @JsonProperty("openingclosures")
    public void setOpeningclosures(List<Openingclosure> openingclosures) {
        this.openingclosures = openingclosures;
    }

    /**
     * Gets the openingclosures.
     *
     * @return the openingclosures
     */
    @JsonProperty("openingclosures")
    public List<Openingclosure> getOpeningclosures() {
        return openingclosures;
    }

    /**
     * Sets the filling machines.
     *
     * @param fillingmachines
     *            the new filling machines
     */
    @JsonProperty("fillingmachines")
    public void setFillingMachines(List<FillingMachine> fillingmachines) {
        this.fillingmachines = fillingmachines;
    }

}
