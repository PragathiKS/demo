package com.tetrapak.customerhub.core.beans.rebuildingkits;

import com.google.gson.annotations.SerializedName;

public class RKLiabilityConditionsPDF {

    @SerializedName("preferred-language-pdf")
    private PDFLink preferredLanguagePDF;

    @SerializedName("english-pdf")
    private PDFLink englishPDF;

    public PDFLink getPreferredLanguagePDF() {
        return preferredLanguagePDF;
    }

    public void setPreferredLanguagePDF(PDFLink preferredLanguagePDF) {
        this.preferredLanguagePDF = preferredLanguagePDF;
    }

    public PDFLink getEnglishPDF() {
        return englishPDF;
    }

    public void setEnglishPDF(PDFLink englishPDF) {
        this.englishPDF = englishPDF;
    }
}
