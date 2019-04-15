/**
 * 
 */
package com.tetrapak.customerhub.core.models;

import static org.junit.Assert.*;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * @author swalamba
 *
 */
public class OrderDetailsModelTest {

	private static final String CONTENT_ROOT = "/content/tetrapak/customerhub/en/ordering/order-history/order-details-parts";
	private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/en/ordering/order-history/order-details-parts/jcr:content/root/responsivegrid/orderdetails";
	private static final String RESOURCE_JSON = "order-detailspage.json";
	private OrderDetailsModel orderDetailsModel;

	@Rule
	public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON, CONTENT_ROOT);

	@Before
	public void setup() {
		Resource resource = aemContext.currentResource(COMPONENT_PATH);
		orderDetailsModel = resource.adaptTo(OrderDetailsModel.class);
	}

	/**
	 * Test method for
	 * {@link com.tetrapak.customerhub.core.models.OrderDetailsModel#getI18nKeys()}.
	 */
	@Test
	public void testGetI18nKeysContainsValidKey() {
		assertTrue(orderDetailsModel.getI18nKeys().contains("deliveryNumberLabel"));
	}

	/**
	 * Test method for
	 * {@link com.tetrapak.customerhub.core.models.OrderDetailsModel#getPackagingDeliveryTableCols()}.
	 */
	@Test
	public void testGetPackagingDeliveryTable() {
		assertTrue(orderDetailsModel.getPackagingDeliveryTableCols().contains("orderno"));
	}

	/**
	 * Test method for
	 * {@link com.tetrapak.customerhub.core.models.OrderDetailsModel#getPackagingProductsTableCols()}.
	 */
	@Test
	public void testGetPackagingProductsTable() {
		assertTrue(orderDetailsModel.getPackagingProductsTableCols().contains("orderno"));
	}

	/**
	 * Test method for
	 * {@link com.tetrapak.customerhub.core.models.OrderDetailsModel#getPartsDeliveryTableCols()}.
	 */
	@Test
	public void testGetPartsDeliveryTable() {
		assertTrue(orderDetailsModel.getPartsDeliveryTableCols().contains("orderno"));
	}

	/**
	 * Test method for
	 * {@link com.tetrapak.customerhub.core.models.OrderDetailsModel#getPackagingDeliveryTableCols()}.
	 */
	@Test
	public void testGetPackagingDeliveryTableInvalidColumn() {
		assertFalse(orderDetailsModel.getPackagingDeliveryTableCols().contains("orderno1"));
	}

	/**
	 * Test method for
	 * {@link com.tetrapak.customerhub.core.models.OrderDetailsModel#getPackagingProductsTableCols()}.
	 */
	@Test
	public void testGetPackagingProductsTableInvalidColumn() {
		assertFalse(orderDetailsModel.getPackagingProductsTableCols().contains("orderno1"));
	}

	/**
	 * Test method for
	 * {@link com.tetrapak.customerhub.core.models.OrderDetailsModel#getPartsDeliveryTableCols()}.
	 */
	@Test
	public void testGetPartsDeliveryTableInvalidColumn() {
		assertFalse(orderDetailsModel.getPartsDeliveryTableCols().contains("orderno1"));
	}

	/**
	 * Test method for
	 * {@link com.tetrapak.customerhub.core.models.OrderDetailsModel#getI18nKeys()}.
	 */
	@Test
	public void testGetI18nKeysDoesntContainKey() {
		assertFalse(orderDetailsModel.getI18nKeys().contains("deliveryNumberLabel1"));
	}

}
