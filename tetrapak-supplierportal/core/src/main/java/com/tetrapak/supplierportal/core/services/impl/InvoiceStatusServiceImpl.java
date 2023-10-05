package com.tetrapak.supplierportal.core.services.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.supplierportal.core.services.InvoiceStatusService;
import com.tetrapak.supplierportal.core.services.config.InvoiceStatusConfiguration;


/**
 * Impl class for Invoice Status Service
 * @author Sunil Kumar Yadav
 */
@Component(immediate = true, service = InvoiceStatusService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = InvoiceStatusConfiguration.class)
public class InvoiceStatusServiceImpl implements InvoiceStatusService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceStatusServiceImpl.class);
	private static final String COLON = ":";
	private static final String HYPHEN = "-";

    private InvoiceStatusConfiguration config;
    
    /**
     * activate method
     * @param config Invoice Status Configuration
     */
    @Activate
    public void activate(InvoiceStatusConfiguration config) {
        this.config = config;
    }

    @Override
	public Map<String, List<String>> invoiceStatusCodeMap(){
		Map<String, List<String>> map = new HashMap<>();
		String[] mappings = config.invoiceStatusCodeMappings();
		if(Objects.nonNull(mappings)) {
		 for(String mapping : mappings) {
			 String[] invoiceMap = mapping.split(HYPHEN);
			 List<String> list = Arrays.asList(invoiceMap[1].split(COLON)).stream().collect(Collectors.toList());
			 LOGGER.info("Invoice Status Description:{}, values:{}",invoiceMap[0], list);
			 map.put(invoiceMap[0], list);
		 }
		}
		return map;
	}
    
    public int getFromToDateGapInMonthsVal() {
    	return Integer.parseInt(this.config.paymentsFromToDateGapInMonths());
    }
}
