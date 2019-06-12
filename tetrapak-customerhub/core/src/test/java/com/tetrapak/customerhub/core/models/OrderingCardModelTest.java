package com.tetrapak.customerhub.core.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.GenericServiceType;
import com.tetrapak.customerhub.core.services.AzureTableStorageService;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.services.impl.AzureTableStorageServiceImpl;
import com.tetrapak.customerhub.core.services.impl.UserPreferenceServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;

@RunWith(MockitoJUnitRunner.class)
public class OrderingCardModelTest {

	private OrderingCardModel orderingCardModel = null;

	private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/global/en";
	private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/global/en/ordering/jcr:content/root/responsivegrid/orderingcard";
	private static final String RESOURCE_JSON = "allContent.json";

	@Rule
	public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(RESOURCE_JSON, CONTENT_ROOT,
			getMultipleMockedService());

	@Before
	public void setup() {
		aemContext.load().json("/" + "user.json", "/home");
		aemContext.addModelsForClasses(OrderingCardModel.class);
		Resource resource = aemContext.currentResource(COMPONENT_PATH);
		orderingCardModel = resource.adaptTo(OrderingCardModel.class);
	}

	private <T> List<GenericServiceType<T>> getMultipleMockedService() {
		GenericServiceType<UserPreferenceService> userPreferenceServiceType = new GenericServiceType<>();
		userPreferenceServiceType.setClazzType(UserPreferenceService.class);
		userPreferenceServiceType.set(new UserPreferenceServiceImpl());

		GenericServiceType<AzureTableStorageService> azureTableStorageServiceGenericServiceType = new GenericServiceType<>();
		azureTableStorageServiceGenericServiceType.setClazzType(AzureTableStorageService.class);
		azureTableStorageServiceGenericServiceType.set(new AzureTableStorageServiceImpl());

		List<GenericServiceType<T>> serviceTypes = new ArrayList<>();
		serviceTypes.add((GenericServiceType<T>) userPreferenceServiceType);
		serviceTypes.add((GenericServiceType<T>) azureTableStorageServiceGenericServiceType);
		return serviceTypes;
	}

	@Test
	public void testModel() {
		String i18nKeys = orderingCardModel.getI18nKeys();
		Assert.assertTrue("title is not empty", i18nKeys.contains("cuhu.ordering.title"));
		Assert.assertEquals("size of enabled fields", 3, orderingCardModel.getEnabledFields().size());
		Assert.assertEquals("size of default fields", 3, orderingCardModel.getDefaultFields().size());
		Assert.assertEquals("preference url",
				"/content/tetrapak/customerhub/global/en/ordering/jcr:content/root/responsivegrid/orderingcard.preference.json",
				orderingCardModel.getPreferencesURL());
		Assert.assertEquals("all orders detail link", "/content/tetrapak/customerhub/global/en/ordering/order-history.html",
				orderingCardModel.getAllOrdersLink());
		Assert.assertEquals("order details link", "/content/tetrapak/customerhub/global/en/ordering/order-history.html",
				orderingCardModel.getOrderDetailLink());
		Assert.assertEquals("saved preference size", 3, orderingCardModel.getSavedPreferences().size());
	}

}
