package com.tetrapak.customerhub.core.services;
import java.util.List;
/**
 * Rebuilding Kit Details Service class
 *  @author Bhakti Nagagvekar
 */
public interface RebuildingKitsDetailsService {
    /**
     * Method to get Ebiz Request Access URLs
     *
     * @return String result
     */
    String getEbizUrl(final String selectedLanguage, final List<String> userGroups);

    /**
     * Method to get Parts External URLs
     *
     * @return String result
     */
    String getPartServiceUrl();
}
