
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
public class DeltaFillingMachine {

    @JsonProperty("delta")
    private List<FillingMachine> fillingMachine;
    @JsonProperty("deleted")
    private List<String> deleted;

    @JsonProperty("delta")
    public List<FillingMachine> getFillingMachine() {
        return new ArrayList<>(fillingMachine);
    }

    @JsonProperty("delta")
    public void setFillingMachine(List<FillingMachine> fillingMachine) {
        this.fillingMachine = new ArrayList<>(fillingMachine);
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
