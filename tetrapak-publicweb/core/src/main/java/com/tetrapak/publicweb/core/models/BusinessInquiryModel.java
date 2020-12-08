package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.tetrapak.publicweb.core.beans.DropdownOption;
import com.tetrapak.publicweb.core.services.CountryDetailService;
import com.tetrapak.publicweb.core.services.PardotService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;
import com.tetrapak.publicweb.core.utils.PageUtil;

/**
 * The Class BusinessInquiryModel.
 *
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BusinessInquiryModel extends FormModel {

    /** The resource. */
    @Self
    private Resource resource;
    
    

    /** The pardot service. */
    @OSGiService
    private PardotService pardotService;

    private FormConfigModel formConfig;

    private FormConsentConfigModel consentConfig;
    
    /** The country options. */
    private List<DropdownOption> countryOptions;
    
    /** The country detail service. */
    @OSGiService
    private CountryDetailService countryDetailService;
    
    
    
    /**
     * The init method.
     */
    @PostConstruct
    protected void init() {
        setCountryOptions();
        setFormConfig();
  }

    /**
     * Sets the form configs.
     */
    public void setFormConfig() {

        final Resource formConfigResource = GlobalUtil.fetchConfigResource(resource,
                "/jcr:content/root/responsivegrid/businessinquiryformc");
        if (Objects.nonNull(formConfigResource))
            this.formConfig = formConfigResource.adaptTo(FormConfigModel.class);

        final Resource consentConfigResource = GlobalUtil.fetchConfigResource(resource,
                "/jcr:content/root/responsivegrid/formconsenttextsconf");
        if (Objects.nonNull(consentConfigResource))
            this.consentConfig = consentConfigResource.adaptTo(FormConsentConfigModel.class);
    }
    
    /**
     * Gets the tags Value and sorting it with Alphabetical order.
     *
     */
    
	public Map<String, String> getTagTitles() {
		

		String rootTag = formConfig.getTags(); 
		ResourceResolver resolver = resource.getResourceResolver();
		TagManager tagManager = resolver.adaptTo(TagManager.class);
		Tag tag = tagManager.resolve(rootTag);
		Iterator<Tag> tagIterator = tag.listChildren();
		Map<String, String> tagsVal = new HashMap<>();
		while (tagIterator.hasNext()) {

			Tag childtag = tagIterator.next();
			String tagName = childtag.getName();
			String defaulTitle = childtag.getTitle();
			String localizedTitle = childtag
					.getLocalizedTitle(PageUtil.getPageLocale(PageUtil.getCurrentPage(resource)));
			if (localizedTitle == null) {
				tagsVal.put(tagName, defaulTitle);
			} else {
				tagsVal.put(tagName, localizedTitle);
			}

		}

		// sorting of map alphabetically with values:
		TreeMap<String, String> sorted = new TreeMap<>(tagsVal);
		Set<Entry<String, String>> mappings = sorted.entrySet();
		
		Map<String, String> tagsValues = new LinkedHashMap<>();
		
		Comparator<Entry<String, String>> valueComparator = (e1, e2) -> e1.getValue().compareTo(e2.getValue());
		
		List<Entry<String, String>> listOfEntries = new ArrayList<>(mappings);
		Collections.sort(listOfEntries, valueComparator);
		
		LinkedHashMap<String, String> sortedByValue = new LinkedHashMap<>(listOfEntries.size());
		for (Entry<String, String> entry : listOfEntries) {
			sortedByValue.put(entry.getKey(), entry.getValue());
		}
		Set<Entry<String, String>> entrySetSortedByValue = sortedByValue.entrySet();

		for (Entry<String, String> mapping : entrySetSortedByValue) {
			String sortedTagKeys = mapping.getKey();
			String sortedTagValues = mapping.getValue();
			tagsValues.put(sortedTagKeys, sortedTagValues);
		}
		return tagsValues;

	}  

    public String getApiUrl() {
        return resource.getPath() + ".pardotbusinessenquiry.json";
    }  

    /**
     * Gets the site language.
     *
     * @return the site language
     */
    public String getSiteLanguage() {
        return PageUtil.getLanguageCodeFromResource(resource);
    }

    /**
     * Gets the site country.
     *
     * @return the site country
     */
    public String getSiteCountry() {
        return PageUtil.getMarketCode(resource);
    }

    /**
     * Gets the form config.
     *
     * @return the form config
     */
    public FormConfigModel getFormConfig() {
        return formConfig;
    }

    /**
     * Gets the consent config.
     *
     * @return the consent config
     */
    public FormConsentConfigModel getConsentConfig() {
        return consentConfig;
    }
    
    /**
     * Gets the country options.
     *
     * @return the country options
     */
    public List<DropdownOption> getCountryOptions() {
        return countryOptions;
    }


    /**
     * Fetches country list from content fragments.
     *
     */
    private void setCountryOptions() {
        this.countryOptions = countryDetailService.fetchCountryList(resource.getResourceResolver());
       
    }

}
