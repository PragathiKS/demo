
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
public class DeltaPackageType {

    @JsonProperty("delta")
    private List<Packagetype> packageType;
    @JsonProperty("deleted")
    private List<String> deleted;

    @JsonProperty("delta")
    public List<Packagetype> getPackagetype() {
        return new ArrayList<>(packageType);
    }

    @JsonProperty("delta")
    public void setPackageType(List<Packagetype> packageType) {
        this.packageType = new ArrayList<>(packageType);
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
