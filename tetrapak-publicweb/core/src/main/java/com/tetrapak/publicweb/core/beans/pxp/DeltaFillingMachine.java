
package com.tetrapak.publicweb.core.beans.pxp;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class DeltaFillingMachine.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "delta", "deleted" })
public class DeltaFillingMachine {

    /** The filling machine. */
    @JsonProperty("delta")
    private List<FillingMachine> fillingMachine;

    /** The deleted. */
    @JsonProperty("deleted")
    private List<String> deleted;

    /**
     * Gets the filling machine.
     *
     * @return the filling machine
     */
    @JsonProperty("delta")
    public List<FillingMachine> getFillingMachine() {
        return fillingMachine;
    }

    /**
     * Sets the filling machine.
     *
     * @param fillingMachine
     *            the new filling machine
     */
    @JsonProperty("delta")
    public void setFillingMachine(List<FillingMachine> fillingMachine) {
        this.fillingMachine = fillingMachine;
    }

    /**
     * Gets the deleted.
     *
     * @return the deleted
     */
    @JsonProperty("deleted")
    public List<String> getDeleted() {
        return deleted;
    }

    /**
     * Sets the deleted.
     *
     * @param deleted
     *            the new deleted
     */
    @JsonProperty("deleted")
    public void setDeleted(List<String> deleted) {
        this.deleted = deleted;
    }

}
