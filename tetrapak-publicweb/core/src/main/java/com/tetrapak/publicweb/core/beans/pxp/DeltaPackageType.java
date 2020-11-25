
package com.tetrapak.publicweb.core.beans.pxp;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class DeltaPackageType.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "delta", "deleted" })
public class DeltaPackageType {

    /** The package type. */
    @JsonProperty("delta")
    private List<Packagetype> packageType;

    /** The deleted. */
    @JsonProperty("deleted")
    private List<String> deleted;

    /**
     * Gets the packagetype.
     *
     * @return the packagetype
     */
    @JsonProperty("delta")
    public List<Packagetype> getPackagetype() {
        return packageType;
    }

    /**
     * Sets the package type.
     *
     * @param packageType
     *            the new package type
     */
    @JsonProperty("delta")
    public void setPackageType(List<Packagetype> packageType) {
        this.packageType = packageType;
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
