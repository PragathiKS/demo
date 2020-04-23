
package com.tetrapak.publicweb.core.beans.pxp;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "benefits",
    "features",
    "name",
    "benefitsimage",
    "header",
    "id",
    "packagetypes",
    "options"
})
public class FillingMachine {

    @JsonProperty("benefits")
    private String benefits;
    @JsonProperty("features")
    private List<Feature> features = null;
    @JsonProperty("name")
    private String name;
    @JsonProperty("benefitsimage")
    private String benefitsimage;
    @JsonProperty("header")
    private String header;
    @JsonProperty("id")
    private String id;
    @JsonProperty("packagetypes")
    private List<Packagetype> packagetypes = null;
    @JsonProperty("options")
    private List<Option> options = null;

    @JsonProperty("benefits")
    public String getBenefits() {
        return benefits;
    }

    @JsonProperty("benefits")
    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    @JsonProperty("features")
    public List<Feature> getFeatures() {
        return features;
    }

    @JsonProperty("features")
    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("benefitsimage")
    public String getBenefitsimage() {
        return benefitsimage;
    }

    @JsonProperty("benefitsimage")
    public void setBenefitsimage(String benefitsimage) {
        this.benefitsimage = benefitsimage;
    }

    @JsonProperty("header")
    public String getHeader() {
        return header;
    }

    @JsonProperty("header")
    public void setHeader(String header) {
        this.header = header;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("packagetypes")
    public List<Packagetype> getPackagetypes() {
        return packagetypes;
    }

    @JsonProperty("packagetypes")
    public void setPackagetypes(List<Packagetype> packagetypes) {
        this.packagetypes = packagetypes;
    }

    @JsonProperty("options")
    public List<Option> getOptions() {
        return options;
    }

    @JsonProperty("options")
    public void setOptions(List<Option> options) {
        this.options = options;
    }

}
