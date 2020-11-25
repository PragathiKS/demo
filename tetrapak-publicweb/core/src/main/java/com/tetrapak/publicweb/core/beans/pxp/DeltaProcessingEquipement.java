
package com.tetrapak.publicweb.core.beans.pxp;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class DeltaProcessingEquipement.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "delta", "deleted" })
public class DeltaProcessingEquipement {

    /** The equipement. */
    @JsonProperty("delta")
    private List<ProcessingEquipement> equipement;

    /** The deleted. */
    @JsonProperty("deleted")
    private List<String> deleted;

    /**
     * Gets the processing equipement.
     *
     * @return the processing equipement
     */
    @JsonProperty("delta")
    public List<ProcessingEquipement> getProcessingEquipement() {
        return equipement;
    }

    /**
     * Sets the processing equipement.
     *
     * @param equipement
     *            the new processing equipement
     */
    @JsonProperty("delta")
    public void setProcessingEquipement(List<ProcessingEquipement> equipement) {
        this.equipement = equipement;
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
