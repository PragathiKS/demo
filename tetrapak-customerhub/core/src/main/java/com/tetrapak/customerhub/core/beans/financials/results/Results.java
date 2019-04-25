
package com.tetrapak.customerhub.core.beans.financials.results;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Results {

    @SerializedName("summary")
    @Expose
    private List<Summary> summary = null;
    @SerializedName("documents")
    @Expose
    private List<Document> documents = null;

    public List<Summary> getSummary() {
        return summary;
    }

    public void setSummary(List<Summary> summary) {
        this.summary = summary;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

}
