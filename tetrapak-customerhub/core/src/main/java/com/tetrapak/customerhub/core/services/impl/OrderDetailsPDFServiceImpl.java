
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
import com.tetrapak.customerhub.core.services.OrderDetailsPDFService;
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
import java.util.List;

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
    final int MARGIN = 65;

    @Override
    public void generateOrderDetailsPDF(SlingHttpServletResponse response,
                                        String orderType, OrderDetailsData orderDetailResponse) {
        OrderDetails orderDetails = orderDetailResponse.getOrderDetails();
        List<DeliveryList> deliveryList = orderDetailResponse.getDeliveryList();

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

            PDFUtil.drawImage(contentStream, img1, 405, 700, 130, 50);
            PDFUtil.drawImage(contentStream, img2, 60, 570, 40, 40);

            PDFUtil.writeContent(document, contentStream, 65, 750, Color.DARK_GRAY, getHeadLines(orderDetails));
            PDFUtil.writeContent(document, contentStream, 105, 610, Color.DARK_GRAY,
                    getContactLines(orderDetailResponse.getCustomerSupportCenter()));

            PDFUtil.drawLine(contentStream, 65, 460, 625, Color.LIGHT_GRAY, 0.01f);

            PDFUtil.drawTable(contentStream, createOrderDetailTable(orderDetails), 665);

            if (StringUtils.equalsIgnoreCase("parts", orderType)) {
                contentStream = printDeliveryDetails(document, contentStream, deliveryList);
            } else {
                PDFUtil.drawTable(contentStream, createOrderSummaryTable(orderDetailResponse.getOrderSummary()), 500);
                PDFUtil.drawLine(contentStream, MARGIN, 460, 550, Color.DARK_GRAY, 0.01f);
            }
            contentStream.close();
            PDFUtil.writeOutput(response, document, orderDetails.getOrderNumber());
        } catch (IOException e) {
            LOGGER.error("IOException {}", e);
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

    private PDPageContentStream printDeliveryDetails(PDDocument document, PDPageContentStream contentStream,
                                                     List<DeliveryList> deliveryList) throws IOException {
        int height = 815;
        for (DeliveryList deliveryDetail : deliveryList) {
            int nextTableHeight = getNextTableHeight(deliveryDetail.getProducts());
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
                    getDeliveryDetailHeader("" + deliveryDetail.getDeliveryNumber()));
            PDFUtil.drawLine(contentStream, MARGIN, 460, height - 95, Color.LIGHT_GRAY, 0.01f);
            PDFUtil.drawLine(contentStream, MARGIN, 460, height - 125, Color.black, 0.01f);

            PDFUtil.drawTable(contentStream, createDeliveryDetailTable(deliveryDetail), height - 30);

            PDFUtil.drawTable(contentStream, createProductTable(deliveryDetail.getProducts()), height - 110);

            PDFUtil.drawTable(contentStream, createProductSummaryTable(deliveryDetail), height - nextTableHeight + 60);
            PDFUtil.drawLine(contentStream, MARGIN, 460, height - nextTableHeight + 20, Color.GRAY, 0.01f);
        }
        return contentStream;
    }

    private int getNextTableHeight(List<Product> deliveryDetail) {
        return deliveryDetail.size() * 30 + 160;
    }

    private List<Row> getDeliveryDetailHeader(String deliveryNumber) {
        List<Row> rows = new ArrayList<>();
        rows.add(new Row("Delivery number: " + deliveryNumber, 20, muliRegular, 11));
        rows.add(new Row("", 30, muliRegular, 12));
        return rows;
    }

    private List<Row> getHeadLines(OrderDetails orderDetails) {
        List<Row> rows = new ArrayList<>();
        rows.add(new Row("Order details", 20, muliRegular, 18));
        rows.add(new Row("", 30, muliRegular, 12));
        rows.add(new Row("Tetra Pak Order number: " + orderDetails.getOrderNumber() + " - " +
                orderDetails.getStatus(), 20, muliRegular, 11));
        rows.add(new Row("", 10, muliRegular, 12));
        return rows;
    }

    private List<Row> getContactLines(CustomerSupportCenter customerSupportCenter) {
        List<Row> rows = new ArrayList<>();
        rows.add(new Row("Customer support center", 9, muliBold, 7));
        rows.add(new Row(customerSupportCenter.getEmail(), 9, muliRegular, 6, true, "mailto:" + customerSupportCenter.getEmail()));
        rows.add(new Row(customerSupportCenter.getMobile(), 9, muliRegular, 6));
        return rows;
    }

    private Table createOrderDetailTable(OrderDetails orderDetails) {
        List<Column> columns = new ArrayList<>();
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + "Customer name", 70));
        columns.add(new Column(orderDetails.getCustomerName(), 90));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + "Purchase order no.", 80));
        columns.add(new Column(orderDetails.getOrderNumber(), 120));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + "Order date", 60));
        columns.add(new Column(orderDetails.getPlacedOn(), 80));

        String[][] content = {
                {CustomerHubConstants.BOLD_IDENTIFIER + "Customer number",
                        orderDetails.getCustomerNumber().toString(),
                        CustomerHubConstants.BOLD_IDENTIFIER + "Customer reference",
                        orderDetails.getCustomerReference().toString(),
                        CustomerHubConstants.BOLD_IDENTIFIER + "Web ref.",
                        orderDetails.getWebRefID().toString()}
        };

        return PDFUtil.getTable(columns, content, 15, muliRegular, muliBold, 7);
    }

    private Table createDeliveryDetailTable(DeliveryList deliveryList) {
        // Total size of columns must not be greater than table width.
        List<Column> columns = new ArrayList<>();
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + "Shipping", 70));
        columns.add(new Column(deliveryList.getCarrier(), 240));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + "Delivery address:", 80));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + "Invoice address:", 120));

        DeliveryAddress deliveryAddress = deliveryList.getDeliveryAddress();
        InvoiceAddress invoiceAddress = deliveryList.getInvoiceAddress();

        String[][] content = {
                {
                        CustomerHubConstants.BOLD_IDENTIFIER + "Track order", deliveryList.getCarrierTrackingID(),
                        deliveryAddress.getName(), invoiceAddress.getName()
                },
                {
                        "", "", deliveryAddress.getCity(), invoiceAddress.getCity()
                },
                {
                        "", "", deliveryAddress.getState() + ", " + deliveryAddress.getPostalcode() + " "
                        + deliveryAddress.getCountry(),
                        invoiceAddress.getState() + ", " + invoiceAddress.getPostalcode() + " " + invoiceAddress.getCountry()
                }
        };

        return PDFUtil.getTable(columns, content, 12, muliRegular, muliBold, 6);
    }

    private Table createProductTable(List<Product> products) {
        List<Column> columns = new ArrayList<>();
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + "#", 10));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + "Product", 120));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + "Product ID", 40));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + "Quantity", 40));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + "Weight", 40));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + "Sent", 40));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + "Open", 40));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + "ETA", 45));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + "Unit Price", 45));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + "Price", 45));

        String[][] content = new String[products.size()][10];

        for (int i = 0; i < products.size(); i++) {
            content[i][0] = Integer.toString(i + 1);
            content[i][1] = products.get(i).getProductName();
            content[i][2] = products.get(i).getProductID();
            content[i][3] = products.get(i).getOrderQuantity();
            content[i][4] = products.get(i).getWeight();
            content[i][5] = products.get(i).getDeliveredQuantity();
            content[i][6] = products.get(i).getRemainingQuantity();
            content[i][7] = products.get(i).getETA();
            content[i][8] = products.get(i).getUnitPrice();
            content[i][9] = products.get(i).getPrice();
        }

        return PDFUtil.getTable(columns, content, 15, muliRegular, muliBold, 6);
    }

    private Table createProductSummaryTable(DeliveryList deliveryList) {
        List<Column> columns = new ArrayList<>();
        columns.add(new Column("", 375));
        columns.add(new Column("  Total weight", 55));
        columns.add(new Column(deliveryList.getTotalWeight(), 45));

        String[][] content = {
                {"", "Total pre VAT", deliveryList.getTotalPricePreVAT()},
                {"", CustomerHubConstants.BOLD_IDENTIFIER + "                VAT", deliveryList.getTotalVAT()}
        };

        return PDFUtil.getTable(columns, content, 12, muliRegular, muliBold, 6);
    }

    private Table createOrderSummaryTable(List<OrderSummary> orderSummaryList) {
        List<Column> columns = new ArrayList<>();
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + "Product", 325));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + "Order Quantity", 70));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + "Quantity delivered so far", 80));

        String[][] content = new String[orderSummaryList.size()][10];

        for (int i = 0; i < orderSummaryList.size(); i++) {
            content[i][0] = orderSummaryList.get(i).getProduct();
            content[i][1] = orderSummaryList.get(i).getOrderQuantity();
            content[i][2] = orderSummaryList.get(i).getDeliveredQuantity();
        }

        return PDFUtil.getTable(columns, content, 15, muliRegular, muliBold, 7);
    }
}
