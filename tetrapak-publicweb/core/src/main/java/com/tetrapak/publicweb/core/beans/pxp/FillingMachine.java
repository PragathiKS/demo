
package com.tetrapak.publicweb.core.beans.pxp;

import java.util.ArrayList;
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
    "thumbnail",
    "id",
    "packagetypes",
    "options"
})
public class FillingMachine {

    @JsonProperty("benefits")
    private String benefits;
    @JsonProperty("features")
    private List<FeatureOption> features;
    @JsonProperty("name")
    private String name;
    @JsonProperty("benefitsimage")
    private String benefitsimage;
    @JsonProperty("header")
    private String header;
    @JsonProperty("thumbnail")
    private String thumbnail;
    @JsonProperty("id")
    private String id;
    @JsonProperty("packagetypes")
    private List<Packagetype> packagetypes;
    @JsonProperty("options")
    private List<FeatureOption> options;

    @JsonProperty("benefits")
    public String getBenefits() {
        return benefits;
    }

    @JsonProperty("benefits")
    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    @JsonProperty("features")
    public List<FeatureOption> getFeatures() {
        return new ArrayList<>(features);
    }

    @JsonProperty("features")
    public void setFeatures(List<FeatureOption> features) {
        this.features = new ArrayList<>(features);
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
    
    @JsonProperty("thumbnail")
    public String getThumbnail() {
        return thumbnail;
    }

    @JsonProperty("thumbnail")
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
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
        return new ArrayList<>(packagetypes);
    }

    @JsonProperty("packagetypes")
    public void setPackagetypes(List<Packagetype> packagetypes) {
        this.packagetypes = new ArrayList<>(packagetypes);
    }

    @JsonProperty("options")
    public List<FeatureOption> getOptions() {
        return new ArrayList<>(options);
    }

    @JsonProperty("options")
    public void setOptions(List<FeatureOption> options) {
        this.options = new ArrayList<>(options);
    }

}
