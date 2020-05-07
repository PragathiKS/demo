
package com.tetrapak.publicweb.core.beans.pxp;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "delta",
    "deleted"
})
public class DeltaProcessingEquipement {

    @JsonProperty("delta")
    private List<ProcessingEquipement> equipement;
    @JsonProperty("deleted")
    private List<String> deleted;

    @JsonProperty("delta")
    public List<ProcessingEquipement> getProcessingEquipement() {
        return new ArrayList<>(equipement);
    }

    @JsonProperty("delta")
    public void setProcessingEquipement(List<ProcessingEquipement> equipement) {
        this.equipement = new ArrayList<>(equipement);
    }

    @JsonProperty("deleted")
    public List<String> getDeleted() {
        return new ArrayList<>(deleted);
    }

    @JsonProperty("deleted")
    public void setDeleted(List<String> deleted) {
        this.deleted = new ArrayList<>(deleted);
    }

}
