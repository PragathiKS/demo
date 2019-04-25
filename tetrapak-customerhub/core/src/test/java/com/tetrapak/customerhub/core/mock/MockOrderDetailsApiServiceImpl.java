package com.tetrapak.customerhub.core.mock;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.OrderDetailsApiService;
import org.apache.commons.lang3.StringUtils;

public class MockOrderDetailsApiServiceImpl implements OrderDetailsApiService {

    final String RES = "{\n" +
            "    \"orderDetails\": {\n" +
            "        \"orderNumber\": \"1234\",\n" +
            "        \"customerName\": \"Swedish Milk AB\",\n" +
            "        \"customerNumber\": 16000000,\n" +
            "        \"purchaseOrderNumber\": 1000000,\n" +
            "        \"customerReference\": 16000000,\n" +
            "        \"placedOn\": \"2018-09-05\",\n" +
            "        \"webRefID\": 100045677,\n" +
            "        \"status\": \"Partial Delivery\"\n" +
            "    },\n" +
            "    \"customerSupportCenter\": {\n" +
            "        \"email\": \"customersupport@tetrapak.com\",\n" +
            "        \"mobile\": \"+46 36 1000\"\n" +
            "    },\n" +
            "    \"deliveryList\": [\n" +
            "        {\n" +
            "            \"deliveryOrder\": \"1\",\n" +
            "            \"deliveryNumber\": \"01234\",\n" +
            "            \"ETD\": \"2019-02-20\",\n" +
            "            \"deliveryAddress\": {\n" +
            "                \"name\": \"Swedish Milk AB\",\n" +
            "                \"name2\": \"Extra AB\",\n" +
            "                \"city\": \"Malmö\",\n" +
            "                \"state\": \"Skåne\",\n" +
            "                \"postalcode\": \"11122\",\n" +
            "                \"country\": \"Sweden\"\n" +
            "            },\n" +
            "            \"invoiceAddress\": {\n" +
            "                \"name\": \"Swedish Milk AB\",\n" +
            "                \"name2\": \"Extra AB\",\n" +
            "                \"city\": \"Malmö\",\n" +
            "                \"state\": \"Skåne\",\n" +
            "                \"postalcode\": \"11122\",\n" +
            "                \"country\": \"Sweden\"\n" +
            "            },\n" +
            "            \"totalProductsForQuery\": \"299\",\n" +
            "            \"products\": [\n" +
            "                {\n" +
            "                    \"productName\": \"string\",\n" +
            "                    \"orderQuantity\": \"100\",\n" +
            "                    \"deliveredQuantity\": \"50\",\n" +
            "                    \"price\": \"150000$\",\n" +
            "                    \"remainingQuantity\": \"50\",\n" +
            "                    \"orderNumber\": 1234,\n" +
            "                    \"productID\": \"number\",\n" +
            "                    \"weight\": \"5 kg\",\n" +
            "                    \"ETA\": \"2017-12-23\",\n" +
            "                    \"unitPrice\": \"1500$\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"productName\": \"string3\",\n" +
            "                    \"orderQuantity\": \"100\",\n" +
            "                    \"deliveredQuantity\": \"50\",\n" +
            "                    \"price\": \"150000$\",\n" +
            "                    \"remainingQuantity\": \"50\",\n" +
            "                    \"orderNumber\": 1234,\n" +
            "                    \"productID\": \"number1\",\n" +
            "                    \"weight\": \"5 kg\",\n" +
            "                    \"ETA\": \"2017-12-23\",\n" +
            "                    \"unitPrice\": \"1500$\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"productName\": \"string2\",\n" +
            "                    \"orderQuantity\": \"100\",\n" +
            "                    \"deliveredQuantity\": \"50\",\n" +
            "                    \"price\": \"150000$\",\n" +
            "                    \"remainingQuantity\": \"50\",\n" +
            "                    \"orderNumber\": 1234,\n" +
            "                    \"productID\": \"number2\",\n" +
            "                    \"weight\": \"5 kg\",\n" +
            "                    \"ETA\": \"2017-12-23\",\n" +
            "                    \"unitPrice\": \"1500$\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"orderNumber\": \"1234\",\n" +
            "            \"carrier\": \"DHL\",\n" +
            "            \"carrierTrackingID\": \"1239858674\",\n" +
            "            \"totalWeight\": \"100kg\",\n" +
            "            \"totalPricePreVAT\": \"30000€\",\n" +
            "            \"totalVAT\": \"1500€\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"deliveryOrder\": \"2\",\n" +
            "            \"deliveryNumber\": \"01234\",\n" +
            "            \"deliveryAddress\": {\n" +
            "                \"name\": \"Swedish Milk AB\",\n" +
            "                \"name2\": \"Extra AB\",\n" +
            "                \"city\": \"Malmö\",\n" +
            "                \"state\": \"Skåne\",\n" +
            "                \"postalcode\": \"11122\",\n" +
            "                \"country\": \"Sweden\"\n" +
            "            },\n" +
            "            \"invoiceAddress\": {\n" +
            "                \"name\": \"Swedish Milk AB\",\n" +
            "                \"name2\": \"Extra AB\",\n" +
            "                \"city\": \"Malmö\",\n" +
            "                \"state\": \"Skåne\",\n" +
            "                \"postalcode\": \"11122\",\n" +
            "                \"country\": \"Sweden\"\n" +
            "            },\n" +
            "            \"products\": [\n" +
            "                {\n" +
            "                    \"productName\": \"string\",\n" +
            "                    \"orderQuantity\": \"100\",\n" +
            "                    \"deliveredQuantity\": \"50\",\n" +
            "                    \"price\": \"150000$\",\n" +
            "                    \"remainingQuantity\": \"50\",\n" +
            "                    \"orderNumber\": 1234,\n" +
            "                    \"productID\": \"number\",\n" +
            "                    \"weight\": \"5 kg\",\n" +
            "                    \"ETA\": \"2017-12-23\",\n" +
            "                    \"unitPrice\": \"1500$\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"productName\": \"string3\",\n" +
            "                    \"orderQuantity\": \"100\",\n" +
            "                    \"deliveredQuantity\": \"50\",\n" +
            "                    \"price\": \"150000$\",\n" +
            "                    \"remainingQuantity\": \"50\",\n" +
            "                    \"orderNumber\": 1234,\n" +
            "                    \"productID\": \"number1\",\n" +
            "                    \"weight\": \"5 kg\",\n" +
            "                    \"ETA\": \"2017-12-23\",\n" +
            "                    \"unitPrice\": \"1500$\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"productName\": \"string2\",\n" +
            "                    \"orderQuantity\": \"100\",\n" +
            "                    \"deliveredQuantity\": \"50\",\n" +
            "                    \"price\": \"150000$\",\n" +
            "                    \"remainingQuantity\": \"50\",\n" +
            "                    \"orderNumber\": 1234,\n" +
            "                    \"productID\": \"number2\",\n" +
            "                    \"weight\": \"5 kg\",\n" +
            "                    \"ETA\": \"2017-12-23\",\n" +
            "                    \"unitPrice\": \"1500$\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"orderNumber\": \"1234\",\n" +
            "            \"carrier\": \"DHL\",\n" +
            "            \"carrierTrackingID\": \"1239858674\",\n" +
            "            \"totalWeight\": \"100kg\",\n" +
            "            \"totalPricePreVAT\": \"30000€\",\n" +
            "            \"totalVAT\": \"1500€\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    final String RES_PACK_MAT = "{\n" +
            "    \"orderDetails\": {\n" +
            "        \"orderNumber\": 116078377,\n" +
            "        \"customerName\": \"Swedish Milk AB\",\n" +
            "        \"customerNumber\": 16000000,\n" +
            "        \"purchaseOrderNumber\": 1000000,\n" +
            "        \"customerReference\": 16000000,\n" +
            "        \"placedOn\": \"2018-09-05\",\n" +
            "        \"webRefID\": 100045677,\n" +
            "        \"status\": \"Partial Delivery\"\n" +
            "    },\n" +
            "    \"customerSupportCenter\": {\n" +
            "        \"email\": \"customersupport@tetrapak.com\",\n" +
            "        \"mobile\": \"+46 36 1000\"\n" +
            "    },\n" +
            "    \"deliveryList\": [\n" +
            "        {\n" +
            "            \"deliveryOrder\": \"1\",\n" +
            "            \"deliveryNumber\": \"01234\",\n" +
            "            \"ETD\": \"2019-02-20\",\n" +
            "            \"deliveryAddress\": {\n" +
            "                \"name\": \"Swedish Milk AB\",\n" +
            "                \"name2\": \"Extra AB\",\n" +
            "                \"city\": \"Malmö\",\n" +
            "                \"state\": \"Skåne\",\n" +
            "                \"postalcode\": \"11122\",\n" +
            "                \"country\": \"Sweden\"\n" +
            "            },\n" +
            "            \"invoiceAddress\": {\n" +
            "                \"name\": \"Swedish Milk AB\",\n" +
            "                \"name2\": \"Extra AB\",\n" +
            "                \"city\": \"Malmö\",\n" +
            "                \"state\": \"Skåne\",\n" +
            "                \"postalcode\": \"11122\",\n" +
            "                \"country\": \"Sweden\"\n" +
            "            },\n" +
            "            \"products\": [\n" +
            "                {\n" +
            "                    \"productName\": \"Product 1\",\n" +
            "                    \"orderQuantity\": \"100\",\n" +
            "                    \"deliveredQuantity\": \"50\",\n" +
            "                    \"price\": \"$1500.00\",\n" +
            "                    \"remainingQuantity\": \"50\",\n" +
            "                    \"materialCode\": \"9045-03715\",\n" +
            "                    \"SKU\": \"1\",\n" +
            "                    \"orderNumber\": 1234\n" +
            "                },\n" +
            "                {\n" +
            "                    \"productName\": \"Product 2\",\n" +
            "                    \"orderQuantity\": \"100\",\n" +
            "                    \"deliveredQuantity\": \"50\",\n" +
            "                    \"price\": \"$1500.00\",\n" +
            "                    \"remainingQuantity\": \"50\",\n" +
            "                    \"materialCode\": \"9045-03715\",\n" +
            "                    \"SKU\": \"2\",\n" +
            "                    \"orderNumber\": 1234\n" +
            "                },\n" +
            "                {\n" +
            "                    \"productName\": \"Product 3\",\n" +
            "                    \"orderQuantity\": \"100\",\n" +
            "                    \"deliveredQuantity\": \"50\",\n" +
            "                    \"price\": \"$1500.00\",\n" +
            "                    \"remainingQuantity\": \"50\",\n" +
            "                    \"materialCode\": \"9045-03715\",\n" +
            "                    \"SKU\": \"3\",\n" +
            "                    \"orderNumber\": 1234\n" +
            "                }\n" +
            "            ],\n" +
            "            \"deliveryStatus\": \"Partial Delivery\",\n" +
            "            \"productPlace\": \"Text\",\n" +
            "            \"requestedDelivery\": \"2018-10-15\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"deliveryOrder\": \"2\",\n" +
            "            \"deliveryNumber\": \"01234\",\n" +
            "            \"deliveryAddress\": {\n" +
            "                \"name\": \"Swedish Milk AB\",\n" +
            "                \"name2\": \"Extra AB\",\n" +
            "                \"city\": \"Malmö\",\n" +
            "                \"state\": \"Skåne\",\n" +
            "                \"postalcode\": \"11122\",\n" +
            "                \"country\": \"Sweden\"\n" +
            "            },\n" +
            "            \"invoiceAddress\": {\n" +
            "                \"name\": \"Swedish Milk AB\",\n" +
            "                \"name2\": \"Extra AB\",\n" +
            "                \"city\": \"Malmö\",\n" +
            "                \"state\": \"Skåne\",\n" +
            "                \"postalcode\": \"11122\",\n" +
            "                \"country\": \"Sweden\"\n" +
            "            },\n" +
            "            \"products\": [\n" +
            "                {\n" +
            "                    \"productName\": \"Product 1\",\n" +
            "                    \"orderQuantity\": \"100\",\n" +
            "                    \"deliveredQuantity\": \"50\",\n" +
            "                    \"price\": \"$1500.00\",\n" +
            "                    \"remainingQuantity\": \"50\",\n" +
            "                    \"materialCode\": \"9045-03715\",\n" +
            "                    \"SKU\": \"1\",\n" +
            "                    \"orderNumber\": 1234\n" +
            "                },\n" +
            "                {\n" +
            "                    \"productName\": \"Product 2\",\n" +
            "                    \"orderQuantity\": \"100\",\n" +
            "                    \"deliveredQuantity\": \"50\",\n" +
            "                    \"price\": \"$1500.00\",\n" +
            "                    \"remainingQuantity\": \"50\",\n" +
            "                    \"materialCode\": \"9045-03715\",\n" +
            "                    \"SKU\": \"2\",\n" +
            "                    \"orderNumber\": 1234\n" +
            "                },\n" +
            "                {\n" +
            "                    \"productName\": \"Product 3\",\n" +
            "                    \"orderQuantity\": \"100\",\n" +
            "                    \"deliveredQuantity\": \"50\",\n" +
            "                    \"price\": \"$1500.00\",\n" +
            "                    \"remainingQuantity\": \"50\",\n" +
            "                    \"materialCode\": \"9045-03715\",\n" +
            "                    \"SKU\": \"3\",\n" +
            "                    \"orderNumber\": 1234\n" +
            "                }\n" +
            "            ],\n" +
            "            \"deliveryStatus\": \"Partial Delivery\",\n" +
            "            \"productPlace\": \"Text\",\n" +
            "            \"requestedDelivery\": \"2018-10-15\",\n" +
            "            \"ETD\": \"2019-02-20\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"orderSummary\": [\n" +
            "        {\n" +
            "            \"product\": \"Product 1\",\n" +
            "            \"orderQuantity\": \"100 KPK\",\n" +
            "            \"deliveredQuantity\": \"50 KPK\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"product\": \"Product 2\",\n" +
            "            \"orderQuantity\": \"200 KPK\",\n" +
            "            \"deliveredQuantity\": \"50 KPK\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"product\": \"Product 3\",\n" +
            "            \"orderQuantity\": \"200 KPK\",\n" +
            "            \"deliveredQuantity\": \"50 KPK\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";
    @Override
    public JsonObject getOrderDetails(String orderNumber, String token, String orderType) {
        JsonObject jsonResponse = new JsonObject();
        if(StringUtils.equalsIgnoreCase(orderType, "parts")) {
            jsonResponse.addProperty(CustomerHubConstants.RESULT, RES);
        }else{
            jsonResponse.addProperty(CustomerHubConstants.RESULT, RES_PACK_MAT);
        }
        jsonResponse.addProperty("status", 200);
        return jsonResponse;
    }
}
