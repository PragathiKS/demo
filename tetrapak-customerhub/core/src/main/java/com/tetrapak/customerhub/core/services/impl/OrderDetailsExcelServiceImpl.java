
package com.tetrapak.customerhub.core.services.impl;

import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.customerhub.core.beans.oderdetails.CustomerSupportCenter;
import com.tetrapak.customerhub.core.beans.oderdetails.DeliveryList;
import com.tetrapak.customerhub.core.beans.oderdetails.OrderDetails;
import com.tetrapak.customerhub.core.services.OrderDetailsExcelService;

/**
 * Implemention class for Odrder Details Excel Service
 *
 * @author Tushar
 */
@Component(immediate = true, service = OrderDetailsExcelService.class)
public class OrderDetailsExcelServiceImpl implements OrderDetailsExcelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDetailsExcelServiceImpl.class);

    @Override
	public void generateOrderDetailsExcel(SlingHttpServletRequest request, SlingHttpServletResponse response,
			OrderDetails orderDetails, CustomerSupportCenter customerSupportCenter, List<DeliveryList> deliveryList,
			String orderType) {
    	

	}

}
