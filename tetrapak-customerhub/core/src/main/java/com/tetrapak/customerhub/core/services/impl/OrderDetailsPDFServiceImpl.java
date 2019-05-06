
package com.tetrapak.customerhub.core.services.impl;

import com.tetrapak.customerhub.core.beans.oderdetails.CustomerSupportCenter;
import com.tetrapak.customerhub.core.beans.oderdetails.DeliveryAddress;
import com.tetrapak.customerhub.core.beans.oderdetails.DeliveryList;
import com.tetrapak.customerhub.core.beans.oderdetails.InvoiceAddress;
import com.tetrapak.customerhub.core.beans.oderdetails.OrderDetails;
import com.tetrapak.customerhub.core.beans.oderdetails.OrderDetailsData;
import com.tetrapak.customerhub.core.beans.oderdetails.OrderSummary;
import com.tetrapak.customerhub.core.beans.oderdetails.Product;
import com.tetrapak.customerhub.core.beans.pdf.Column;
import com.tetrapak.customerhub.core.beans.pdf.Row;
import com.tetrapak.customerhub.core.beans.pdf.Table;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.models.OrderDetailsModel;
import com.tetrapak.customerhub.core.services.OrderDetailsPDFService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.PDFUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.encoding.Encoding;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Impl class for Order Details PDF Service
 *
 * @author Nitin Kumar
 */
@Component(immediate = true, service = OrderDetailsPDFService.class)
public class OrderDetailsPDFServiceImpl implements OrderDetailsPDFService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDetailsPDFServiceImpl.class);

    private PDFont muliRegular;
    private PDFont muliBold;
    private static final int MARGIN = 65;
    private OrderDetailsModel orderDetailsModel;
    private static final String ORDER_DETAIL_I18_PREFIX = "cuhu.orderDetail.";
    private static final String ORDER_DETAIL_SUMMARY_PREFIX = "cuhu.orderDetail.orderSummary.";
    private static final String ORDER_DETAIL_DELIVERY_PREFIX = "cuhu.orderDetail.deliveryList.products.";
    private static final String ORDER_QUANTITY = "orderQuantity";
    private static final String DELIVERED_QUANTITY = "deliveredQuantity";
    private String[] partsDeliveryColumn;
    private String[] packMatDeliveryColumn;
    private String[] packMatColumns;

    /**
     * @param request             request
     * @param response            response
     * @param orderType           order type
     * @param orderDetailResponse order detail response
     */
    @Override
    public boolean generateOrderDetailsPDF(SlingHttpServletRequest request, SlingHttpServletResponse response,
                                           String orderType, OrderDetailsData orderDetailResponse, OrderDetailsModel orderDetailsModel) {
        this.orderDetailsModel = orderDetailsModel;
        OrderDetails orderDetails = orderDetailResponse.getOrderDetails();
        List<DeliveryList> deliveryList = orderDetailResponse.getDeliveryList();

        String partsDeliveryColumnString = orderDetailsModel.getPartsDeliveryTableCols();
        partsDeliveryColumn = partsDeliveryColumnString.split(",");

        String packMatDeliveryColumnString = orderDetailsModel.getPackagingDeliveryTableCols();
        packMatDeliveryColumn = packMatDeliveryColumnString.split(",");

        String packMatColumnString = orderDetailsModel.getPackagingProductsTableCols();
        packMatColumns = packMatColumnString.split(",");

        InputStream in1 = null;
        InputStream in2 = null;
        InputStream image1 = null;
        InputStream image2 = null;
        PDPageContentStream contentStream = null;
        PDDocument document = new PDDocument();

        try {
            PDPage page = new PDPage();
            document.addPage(page);
            contentStream = new PDPageContentStream(
                    document, page, PDPageContentStream.AppendMode.OVERWRITE, true, true);

            in1 = getClass().getResourceAsStream("/fonts/muli-light-webfont.ttf");
            in2 = getClass().getResourceAsStream("/fonts/muli-bold-webfont.ttf");

            muliRegular = PDTrueTypeFont.load(document, in1, Encoding.getInstance(COSName.WIN_ANSI_ENCODING));
            muliBold = PDTrueTypeFont.load(document, in2, Encoding.getInstance(COSName.WIN_ANSI_ENCODING));

            image1 = getClass().getResourceAsStream("/images/tetra_pdf.png");
            image2 = getClass().getResourceAsStream("/images/small_logo.png");
            BufferedImage bufferedImage1 = ImageIO.read(image1);
            BufferedImage bufferedImage2 = ImageIO.read(image2);

            PDImageXObject img1 = LosslessFactory.createFromImage(document, bufferedImage1);
            PDImageXObject img2 = LosslessFactory.createFromImage(document, bufferedImage2);

            PDFUtil.drawImage(contentStream, img1, 405, 710, 130, 50);
            PDFUtil.drawImage(contentStream, img2, 60, 570, 40, 40);

            PDFUtil.writeContent(document, contentStream, 65, 750, Color.DARK_GRAY, getHeadLines(request, orderDetails));
            PDFUtil.writeContent(document, contentStream, 105, 610, Color.DARK_GRAY,
                    getContactLines(request, orderDetailResponse.getCustomerSupportCenter()));

            PDFUtil.drawLine(contentStream, 65, 460, 625, Color.LIGHT_GRAY, 0.1f);

            PDFUtil.drawTable(contentStream, createOrderDetailTable(request, orderDetails), 665);

            if (StringUtils.equalsIgnoreCase("parts", orderType)) {
                contentStream = printPartsDeliveryDetails(request, document, contentStream, deliveryList);
            } else {
                List<OrderSummary> orderSummaryList = orderDetailResponse.getOrderSummary();
                PDFUtil.drawTable(contentStream, createOrderSummaryTable(request, orderSummaryList), 550);
                PDFUtil.drawLine(contentStream, MARGIN, 460, 535, Color.DARK_GRAY, 0.1f);
                PDFUtil.drawLine(contentStream, MARGIN, 460, 535 - (15 * orderSummaryList.size()), Color.LIGHT_GRAY, 0.1f);

                contentStream = printPackMatDeliveryDetails(request, document, contentStream, orderDetailResponse.getDeliveryList());
            }
            contentStream.close();
            PDFUtil.writeOutput(response, document, orderDetails.getOrderNumber());
            return true;
        } catch (IOException e) {
            LOGGER.error("IOException {}", e);
            return false;
        } finally {
            try {
                if (null != contentStream) {
                    contentStream.close();
                }
                if (null != in1) {
                    in1.close();
                }
                if (null != in2) {
                    in2.close();
                }
                if (null != image1) {
                    image1.close();
                }
                if (null != image2) {
                    image2.close();
                }
            } catch (IOException e) {
                LOGGER.error("IOException {}", e);
            }
        }
    }

    private PDPageContentStream printPartsDeliveryDetails(SlingHttpServletRequest request, PDDocument document,
                                                          PDPageContentStream contentStream, List<DeliveryList> deliveryList)
            throws IOException {
        int height = 895;
        for (DeliveryList deliveryDetail : deliveryList) {
            int nextTableHeight = getNextTableHeight(deliveryDetail.getProducts()) + 200;
            height = height - nextTableHeight;
            if (height < nextTableHeight) {
                height = 750;
                PDPage page = new PDPage();
                document.addPage(page);
                contentStream.close();
                contentStream = new PDPageContentStream(
                        document, page, PDPageContentStream.AppendMode.OVERWRITE, true, true);
            }
            PDFUtil.writeContent(document, contentStream, MARGIN, height, Color.DARK_GRAY,
                    getDeliveryDetailHeader(request, deliveryDetail.getDeliveryNumber()));
            PDFUtil.drawLine(contentStream, MARGIN, 460, height - 95, Color.LIGHT_GRAY, 0.1f);
            PDFUtil.drawLine(contentStream, MARGIN, 460, height - 125, Color.black, 0.1f);

            PDFUtil.drawTable(contentStream, createDeliveryDetailTable(request, deliveryDetail), height - 30);

            PDFUtil.drawTable(contentStream, createProductTable(request, deliveryDetail.getProducts()), height - 110);

            PDFUtil.drawTable(contentStream, createProductSummaryTable(deliveryDetail), height - nextTableHeight + 60);
            PDFUtil.drawLine(contentStream, MARGIN, 460, height - nextTableHeight + 20, Color.LIGHT_GRAY, 0.1f);
        }
        return contentStream;
    }

    private PDPageContentStream printPackMatDeliveryDetails(
            SlingHttpServletRequest request, PDDocument document, PDPageContentStream contentStream,
            List<DeliveryList> deliveryList) throws IOException {
        int height = 710;
        for (DeliveryList deliveryDetail : deliveryList) {
            int nextTableHeight = getNextTableHeight(deliveryDetail.getProducts()) + 200;
            height = height - nextTableHeight;
            if (height < nextTableHeight) {
                height = 750;
                PDPage page = new PDPage();
                document.addPage(page);
                contentStream.close();
                contentStream = new PDPageContentStream(
                        document, page, PDPageContentStream.AppendMode.OVERWRITE, true, true);
            }
            PDFUtil.writeContent(document, contentStream, MARGIN, height, Color.DARK_GRAY,
                    getPackMatDeliveryDetailHeader(request, deliveryDetail));
            PDFUtil.drawLine(contentStream, MARGIN, 460, height - 95, Color.LIGHT_GRAY, 0.1f);
            PDFUtil.drawLine(contentStream, MARGIN, 460, height - 125, Color.black, 0.1f);

            PDFUtil.drawTable(contentStream, createPackMatDeliveryDetailTable(request, deliveryDetail), height - 30);

            PDFUtil.drawTable(contentStream, createPackMatProductTable(request, deliveryDetail.getProducts()), height - 110);

            PDFUtil.drawLine(contentStream, MARGIN, 460, height - nextTableHeight + 40, Color.LIGHT_GRAY, 0.1f);
        }
        return contentStream;
    }

    private int getNextTableHeight(List<Product> deliveryDetail) {
        return deliveryDetail.size() * 15;
    }

    private List<Row> getDeliveryDetailHeader(SlingHttpServletRequest request, String deliveryNumber) {
        List<Row> rows = new ArrayList<>();
        rows.add(new Row(GlobalUtil.getI18nValue(request, StringUtils.EMPTY,
                orderDetailsModel.getDeliveryOrder()) + ": " + deliveryNumber, 20, muliRegular, 11));
        rows.add(new Row(StringUtils.EMPTY, 30, muliRegular, 12));
        return rows;
    }

    private List<Row> getPackMatDeliveryDetailHeader(SlingHttpServletRequest request, DeliveryList deliveryDetail) {
        List<Row> rows = new ArrayList<>();
        rows.add(new Row(GlobalUtil.getI18nValue(request, StringUtils.EMPTY,
                orderDetailsModel.getDeliveryOrder()) + ": " + deliveryDetail.getDeliveryNumber() + " - " +
                deliveryDetail.getDeliveryStatus(), 20, muliRegular, 11));
        rows.add(new Row(StringUtils.EMPTY, 30, muliRegular, 12));
        return rows;
    }

    private List<Row> getHeadLines(SlingHttpServletRequest request, OrderDetails orderDetails) {
        List<Row> rows = new ArrayList<>();
        rows.add(new Row("Order details", 20, muliRegular, 18));
        rows.add(new Row(StringUtils.EMPTY, 30, muliRegular, 12));
        rows.add(new Row(GlobalUtil.getI18nValue(request, StringUtils.EMPTY,
                orderDetailsModel.getOrderNo()) + ": " + orderDetails.getOrderNumber() + " - " +
                orderDetails.getStatus(), 20, muliRegular, 11));
        rows.add(new Row(StringUtils.EMPTY, 10, muliRegular, 12));
        return rows;
    }

    private List<Row> getContactLines(SlingHttpServletRequest request, CustomerSupportCenter customerSupportCenter) {
        List<Row> rows = new ArrayList<>();
        rows.add(new Row(GlobalUtil.getI18nValue(request, StringUtils.EMPTY,
                orderDetailsModel.getCustSupCentreLabel()), 9, muliBold, 7));
        rows.add(new Row(customerSupportCenter.getEmail(), 9, muliRegular, 6, true, "mailto:" + customerSupportCenter.getEmail()));
        rows.add(new Row(customerSupportCenter.getMobile(), 9, muliRegular, 6));
        return rows;
    }

    private Table createOrderDetailTable(SlingHttpServletRequest request, OrderDetails orderDetails) {
        List<Column> columns = new ArrayList<>();
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request, StringUtils.EMPTY,
                orderDetailsModel.getCustomerNameLabel()), 70));
        columns.add(new Column(orderDetails.getCustomerName(), 90));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request, StringUtils.EMPTY,
                orderDetailsModel.getPurchaseOrderNumberLabel()), 80));
        columns.add(new Column(orderDetails.getOrderNumber(), 120));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request, StringUtils.EMPTY,
                orderDetailsModel.getOrderDateLabel()), 60));
        columns.add(new Column(orderDetails.getPlacedOn(), 80));

        String[][] content = {
                {CustomerHubConstants.BOLD_IDENTIFIER +
                        GlobalUtil.getI18nValue(request, StringUtils.EMPTY, orderDetailsModel.getCustomerNumberLabel()),
                        orderDetails.getCustomerNumber().toString(),
                        CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request, StringUtils.EMPTY,
                                orderDetailsModel.getCustomerReferenceLabel()),
                        orderDetails.getCustomerReference().toString(),
                        CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request, StringUtils.EMPTY,
                                orderDetailsModel.getWebRefLabel()), orderDetails.getWebRefID().toString()}
        };

        return PDFUtil.getTable(columns, content, 15, muliRegular, muliBold, 7);
    }

    private Table createDeliveryDetailTable(SlingHttpServletRequest request, DeliveryList deliveryList) {
        List<Column> columns = new ArrayList<>();
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER +
                GlobalUtil.getI18nValue(request, StringUtils.EMPTY, orderDetailsModel.getShippingLabel()), 70));
        columns.add(new Column(deliveryList.getCarrier(), 240));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER +
                GlobalUtil.getI18nValue(request, StringUtils.EMPTY, orderDetailsModel.getDeliveryAddrLabel()), 80));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER +
                GlobalUtil.getI18nValue(request, StringUtils.EMPTY, orderDetailsModel.getInvoiceAddrLabel()), 120));

        DeliveryAddress deliveryAddress = deliveryList.getDeliveryAddress();
        InvoiceAddress invoiceAddress = deliveryList.getInvoiceAddress();

        String[][] content = {
                {
                        CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request,
                                StringUtils.EMPTY, orderDetailsModel.getTrackOrderLabel()), deliveryList.getCarrierTrackingID(),
                        deliveryAddress.getName(), invoiceAddress.getName()
                },
                {
                        StringUtils.EMPTY, StringUtils.EMPTY, deliveryAddress.getCity(), invoiceAddress.getCity()
                },
                {
                        StringUtils.EMPTY, StringUtils.EMPTY, deliveryAddress.getState() + ", " + deliveryAddress.getPostalcode() + " "
                        + deliveryAddress.getCountry(),
                        invoiceAddress.getState() + ", " + invoiceAddress.getPostalcode() + " " + invoiceAddress.getCountry()
                }
        };

        return PDFUtil.getTable(columns, content, 12, muliRegular, muliBold, 7);
    }

    private Table createPackMatDeliveryDetailTable(SlingHttpServletRequest request, DeliveryList deliveryDetail) {
        List<Column> columns = new ArrayList<>();
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER +
                GlobalUtil.getI18nValue(request, StringUtils.EMPTY, orderDetailsModel.getDeliveryOrder()), 70));
        columns.add(new Column(deliveryDetail.getDeliveryOrder(), 240));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER +
                GlobalUtil.getI18nValue(request, StringUtils.EMPTY, orderDetailsModel.getDeliveryAddrLabel()), 80));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER +
                GlobalUtil.getI18nValue(request, StringUtils.EMPTY, orderDetailsModel.getInvoiceAddrLabel()), 120));

        DeliveryAddress deliveryAddress = deliveryDetail.getDeliveryAddress();
        InvoiceAddress invoiceAddress = deliveryDetail.getInvoiceAddress();

        String[][] content = {
                {
                        CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request,
                                StringUtils.EMPTY, orderDetailsModel.getProductionPlace()), deliveryDetail.getProductPlace(),
                        deliveryAddress.getName(), invoiceAddress.getName()
                },
                {
                        CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request,
                                StringUtils.EMPTY, orderDetailsModel.getRequested()), deliveryDetail.getRequestedDelivery(),
                        deliveryAddress.getCity(), invoiceAddress.getCity()
                },
                {
                        CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request,
                                StringUtils.EMPTY, orderDetailsModel.getEtd()), deliveryDetail.getETD(),
                        deliveryAddress.getState() + ", " + deliveryAddress.getPostalcode() + " " + deliveryAddress.getCountry(),
                        invoiceAddress.getState() + ", " + invoiceAddress.getPostalcode() + " " + invoiceAddress.getCountry()
                }
        };

        return PDFUtil.getTable(columns, content, 12, muliRegular, muliBold, 7);
    }

    private Table createProductTable(SlingHttpServletRequest request, List<Product> products) {

        List<Column> columns = new ArrayList<>();
        for (String columnName : partsDeliveryColumn) {
            int width = 40;
            if (StringUtils.equalsIgnoreCase(columnName, "productName")) {
                width = 110;
            } else if (StringUtils.equalsIgnoreCase(columnName, "serialNo")) {
                width = 10;
            } else if (StringUtils.equalsIgnoreCase(columnName, "price")) {
                width = 45;
            } else if (StringUtils.equalsIgnoreCase(columnName, ORDER_QUANTITY) ||
                    StringUtils.equalsIgnoreCase(columnName, DELIVERED_QUANTITY) ||
                    StringUtils.equalsIgnoreCase(columnName, "remainingQuantity")) {
                width = 35;
            }
            columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER +
                    GlobalUtil.getI18nValue(request, ORDER_DETAIL_I18_PREFIX, columnName), width));
        }

        String[][] content = new String[products.size()][partsDeliveryColumn.length];

        for (int i = 0; i < products.size(); i++) {
            int j = 0;
            for (String columnName : partsDeliveryColumn) {
                content[i][j] = getProductValueForTheHeader(products.get(i), columnName, i);
                j++;
            }
        }

        return PDFUtil.getTable(columns, content, 15, muliRegular, muliBold, 7);
    }

    private Table createPackMatProductTable(SlingHttpServletRequest request, List<Product> products) {

        List<Column> columns = new ArrayList<>();
        for (String packMatColumn : packMatDeliveryColumn) {
            int width = 120;
            if (StringUtils.equalsIgnoreCase(packMatColumn, "SKU") ||
                    StringUtils.equalsIgnoreCase(packMatColumn, "quantityKPK")) {
                width = 80;
            }
            columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER +
                    GlobalUtil.getI18nValue(request, ORDER_DETAIL_DELIVERY_PREFIX, packMatColumn), width));
        }

        String[][] content = new String[products.size()][packMatDeliveryColumn.length];

        for (int i = 0; i < products.size(); i++) {
            int j = 0;
            for (String packmatcolumn : packMatDeliveryColumn) {
                content[i][j] = getProductValueForTheHeader(products.get(i), packmatcolumn, i);
                j++;
            }
        }

        return PDFUtil.getTable(columns, content, 15, muliRegular, muliBold, 7);
    }

    private String getProductValueForTheHeader(Product product, String columnName, int i) {
        Map<String, String> map = new HashMap<>();
        map.put("productName", product.getProductName());
        map.put(ORDER_QUANTITY, product.getOrderQuantity());
        map.put(DELIVERED_QUANTITY, product.getDeliveredQuantity());
        map.put("price", product.getPrice());
        map.put("remainingQuantity", product.getRemainingQuantity());
        map.put("orderNumber", product.getOrderNumber().toString());
        map.put("productID", product.getProductID());
        map.put("weight", product.getWeight());
        map.put("ETA", product.getETA());
        map.put("unitPrice", product.getUnitPrice());
        map.put("materialCode", product.getMaterialCode());
        map.put("SKU", product.getSKU());
        map.put("serialNo", Integer.toString(i + 1));
        map.put("quantityKPK", product.getOrderQuantity() + CustomerHubConstants.PATH_SEPARATOR + product.getRemainingQuantity()
                + CustomerHubConstants.PATH_SEPARATOR + product.getDeliveredQuantity());

        if (map.containsKey(columnName)) {
            return map.get(columnName);
        }
        return StringUtils.EMPTY;
    }

    private Table createProductSummaryTable(DeliveryList deliveryList) {
        List<Column> columns = new ArrayList<>();
        columns.add(new Column(StringUtils.EMPTY, 375));
        columns.add(new Column("  Total weight", 55));
        columns.add(new Column(deliveryList.getTotalWeight(), 45));

        String[][] content = {
                {StringUtils.EMPTY, "Total pre VAT", deliveryList.getTotalPricePreVAT()},
                {StringUtils.EMPTY, CustomerHubConstants.BOLD_IDENTIFIER + "                VAT", deliveryList.getTotalVAT()}
        };

        return PDFUtil.getTable(columns, content, 12, muliRegular, muliBold, 7);
    }

    private Table createOrderSummaryTable(SlingHttpServletRequest request, List<OrderSummary> orderSummaryList) {
        List<Column> columns = new ArrayList<>();
        for (String packMatColumnName : packMatColumns) {
            int width = 70;
            if (StringUtils.equalsIgnoreCase(packMatColumnName, "product")) {
                width = 300;
            }
            columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER +
                    GlobalUtil.getI18nValue(request, ORDER_DETAIL_SUMMARY_PREFIX, packMatColumnName), width));
        }

        String[][] content = new String[orderSummaryList.size()][packMatColumns.length];

        for (int i = 0; i < orderSummaryList.size(); i++) {
            int j = 0;
            for (String packMatColumnName : packMatColumns) {
                content[i][j] = getOrderSummaryValueForTheHeader(orderSummaryList.get(i), packMatColumnName);
                j++;
            }
        }

        return PDFUtil.getTable(columns, content, 15, muliRegular, muliBold, 7);
    }

    private String getOrderSummaryValueForTheHeader(OrderSummary orderSummary, String packMatColumnName) {
        Map<String, String> map = new HashMap<>();
        map.put("product", orderSummary.getProduct());
        map.put(ORDER_QUANTITY, orderSummary.getOrderQuantity());
        map.put(DELIVERED_QUANTITY, orderSummary.getDeliveredQuantity());

        if (map.containsKey(packMatColumnName)) {
            return map.get(packMatColumnName);
        }
        return StringUtils.EMPTY;
    }
}
