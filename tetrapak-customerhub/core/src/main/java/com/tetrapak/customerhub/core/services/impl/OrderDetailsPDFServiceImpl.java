
package com.tetrapak.customerhub.core.services.impl;

import com.tetrapak.customerhub.core.beans.oderdetails.*;
import com.tetrapak.customerhub.core.beans.pdf.Column;
import com.tetrapak.customerhub.core.beans.pdf.Row;
import com.tetrapak.customerhub.core.beans.pdf.Table;
import com.tetrapak.customerhub.core.services.OrderDetailsPDFService;
import com.tetrapak.customerhub.core.utils.PDFUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
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

    PDDocument document = new PDDocument();
    PDFont muli_regular;
    PDFont muli_bold;

    @Override
    public void generateOrderDetailsPDF(SlingHttpServletResponse response,
                                        String orderType, OrderDetails orderDetails, CustomerSupportCenter customerSupportCenter, List<DeliveryList> deliveryList, List<OrderSummary> orderSummaryList) {
        // InputStream in1 = null;
        // InputStream in2 = null;
        InputStream image1 = null;
        InputStream image2 = null;
        PDPageContentStream contentStream;
        try {
            PDPage page = new PDPage();
            document.addPage(page);
            contentStream = new PDPageContentStream(
                    document, page, PDPageContentStream.AppendMode.OVERWRITE, true, true);

            //   in1 = getClass().getResourceAsStream("/fonts/muli-light-webfont.ttf");
            //   in2 = getClass().getResourceAsStream("/fonts/muli-bold-webfont.ttf");

            //    muli_regular = PDTrueTypeFont.load(document, in1, Encoding.getInstance(COSName.STANDARD_ENCODING));
            //   muli_bold = PDTrueTypeFont.load(document, in2, Encoding.getInstance(COSName.STANDARD_ENCODING));

            muli_regular = PDType1Font.HELVETICA;
            muli_bold = PDType1Font.HELVETICA_BOLD;


            image1 = getClass().getResourceAsStream("/images/tetra_pdf.png");
            image2 = getClass().getResourceAsStream("/images/small_logo.png");
            BufferedImage bufferedImage1 = ImageIO.read(image1);
            BufferedImage bufferedImage2 = ImageIO.read(image2);

            PDImageXObject img1 = LosslessFactory.createFromImage(document, bufferedImage1);
            PDImageXObject img2 = LosslessFactory.createFromImage(document, bufferedImage2);

            PDFUtil.drawImage(document, contentStream, img1, 405, 700, 130, 50);
            PDFUtil.drawImage(document, contentStream, img2, 60, 570, 40, 40);

            PDFUtil.writeContent(document, contentStream, 65, 750, Color.DARK_GRAY, getHeadLines(orderDetails));
            PDFUtil.writeContent(document, contentStream, 105, 610, Color.DARK_GRAY, getContactLines(customerSupportCenter));

            PDFUtil.drawLine(document, contentStream, 65, 460, 625, Color.LIGHT_GRAY, 0.01f);

            PDFUtil.drawTable(document, contentStream, createOrderDetailTable(orderDetails), 60);

            if (StringUtils.equalsIgnoreCase("parts", orderType)) {
                for (DeliveryList deliveryDetail : deliveryList) {
                    int count = deliveryList.indexOf(deliveryDetail) + 1;
                    int height = 800 - 240 * count; //565
                    PDFUtil.writeContent(document, contentStream, 65, height, Color.DARK_GRAY, getDeliveryDetailHeader("" + deliveryList.indexOf(deliveryDetail)));
                    PDFUtil.drawLine(document, contentStream, 65, 460, height - 110, Color.LIGHT_GRAY, 0.01f);

                    PDFUtil.drawTable(document, contentStream, createDeliveryDetailTable(deliveryDetail), 765 - height);
                    PDFUtil.drawLine(document, contentStream, 65, 460, height - 128, Color.black, 0.01f);

                    Table productTable = createProductTable(deliveryDetail.getProducts());
                    PDFUtil.drawTable(document, contentStream, productTable, 840 - height);
                    //PDFUtil.drawTableGrid(document, contentStream , productTable, productTable.getContent(),750 - height);
                    PDFUtil.drawTable(document, contentStream, createProductSummaryTable(deliveryDetail), 920 - height);
                }
            } else {
                PDFUtil.drawTable(document, contentStream, createOrderSummaryTable(orderSummaryList), 180);
                PDFUtil.drawLine(document, contentStream, 65, 460, 190, Color.LIGHT_GRAY, 0.01f);


            }

            contentStream.close();
            PDFUtil.writeOutput(response, document, orderDetails.getOrderNumber());
        } catch (IOException e) {
            LOGGER.error("IOException {}", e);
        } finally {
            try {
              /*  if (null != in1) {
                    in1.close();
                }
                if (null != in2) {
                    in2.close();
                }*/
                if (null != image1) {
                    image1.close();
                }
                if (null != image2) {
                    image2.close();
                }
                document.close();
            } catch (IOException e) {
                LOGGER.error("IOException {}", e);
            }
        }
    }

    private List<Row> getDeliveryDetailHeader(String deliveryNumber) {
        List<Row> rows = new ArrayList<>();
        rows.add(new Row("Delivery number: " + deliveryNumber, 20, muli_regular, 11));
        rows.add(new Row("", 30, muli_regular, 12));
        return rows;
    }

    private List<Row> getHeadLines(OrderDetails orderDetails) {
        List<Row> rows = new ArrayList<>();
        rows.add(new Row("Order details", 20, muli_regular, 18));
        rows.add(new Row("", 30, muli_regular, 12));
        rows.add(new Row("Tetra Pak Order number: " + orderDetails.getOrderNumber() + " - " + orderDetails.getStatus(), 20, muli_regular, 11));
        rows.add(new Row("", 10, muli_regular, 12));
        return rows;
    }

    private List<Row> getContactLines(CustomerSupportCenter customerSupportCenter) {
        List<Row> rows = new ArrayList<>();
        rows.add(new Row("Customer support center", 9, muli_bold, 7));
        rows.add(new Row(customerSupportCenter.getEmail(), 9, muli_regular, 6, true));
        rows.add(new Row(customerSupportCenter.getMobile(), 9, muli_regular, 6));
        return rows;
    }

    private Table createOrderDetailTable(OrderDetails orderDetails) {
        List<Column> columns = new ArrayList<>();
        columns.add(new Column("Customer name", 70, true));
        columns.add(new Column(orderDetails.getCustomerName(), 90, false));
        columns.add(new Column("Purchase order no.", 80, true));
        columns.add(new Column(orderDetails.getOrderNumber(), 120, false));
        columns.add(new Column("Order date", 60, true));
        columns.add(new Column(orderDetails.getPlacedOn(), 80, false));

        String[][] content = {
                {"Customer number", orderDetails.getCustomerNumber().toString(), "Customer reference", orderDetails.getCustomerReference().toString(), "Web ref.", orderDetails.getWebRefID().toString()}
        };

        return PDFUtil.getTable(columns, content, muli_regular, muli_bold);
    }

    private Table createDeliveryDetailTable(DeliveryList deliveryList) {
        // Total size of columns must not be greater than table width.
        List<Column> columns = new ArrayList<>();
        columns.add(new Column("Shipping", 70, true));
        columns.add(new Column(deliveryList.getCarrier(), 240, false));
        columns.add(new Column("Delivery address:", 80, false));
        columns.add(new Column("Invoice address:", 120, false));

        DeliveryAddress deliveryAddress = deliveryList.getDeliveryAddress();
        InvoiceAddress invoiceAddress = deliveryList.getInvoiceAddress();

        String[][] content = {
                {
                        "Track order", deliveryList.getDeliveryOrder(), deliveryAddress.getName(),
                        invoiceAddress.getName()},
                {"", "", deliveryAddress.getCity(), invoiceAddress.getCity()},
                {"", "", deliveryAddress.getState() + ", " + deliveryAddress.getPostalcode() + " "
                        + deliveryAddress.getCountry(), invoiceAddress.getState() + ", "
                        + invoiceAddress.getPostalcode() + " " + invoiceAddress.getCountry()}
        };

        return PDFUtil.getTable(columns, content, muli_regular, muli_bold);
    }

    private Table createProductTable(List<Product> products) {
        List<Column> columns = new ArrayList<>();
        columns.add(new Column("#", 10, false));
        columns.add(new Column("Product", 120, false));
        columns.add(new Column("Product ID", 40, false));
        columns.add(new Column("Quantity", 40, false));
        columns.add(new Column("Weight", 40, false));
        columns.add(new Column("Sent", 40, false));
        columns.add(new Column("Open", 40, false));
        columns.add(new Column("ETA", 45, false));
        columns.add(new Column("Unit Price", 45, false));
        columns.add(new Column("Price", 45, false));

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

        return PDFUtil.getTable(columns, content, muli_regular, muli_bold);
    }

    private Table createProductSummaryTable(DeliveryList deliveryList) {
        List<Column> columns = new ArrayList<>();
        columns.add(new Column("", 375, false));
        columns.add(new Column("Total weight", 55, false));
        columns.add(new Column(deliveryList.getTotalWeight(), 45, false));

        String[][] content = {
                {"", "Total pre VAT", deliveryList.getTotalPricePreVAT()},
                {"", "VAT", deliveryList.getTotalVAT()}
        };

        return PDFUtil.getTable(columns, content, muli_regular, muli_bold);
    }

    private Table createOrderSummaryTable(List<OrderSummary> orderSummaryList) {
        List<Column> columns = new ArrayList<>();
        columns.add(new Column("Product", 330, false));
        columns.add(new Column("Order Quantity", 70, false));
        columns.add(new Column("Quantity delivered so far", 80, false));

        String[][] content = new String[orderSummaryList.size()][10];

        for (int i = 0; i < orderSummaryList.size(); i++) {
            content[i][0] = orderSummaryList.get(i).getProduct();
            content[i][1] = orderSummaryList.get(i).getOrderQuantity();
            content[i][2] = orderSummaryList.get(i).getDeliveredQuantity();
        }

        return PDFUtil.getTable(columns, content, muli_regular, muli_bold);
    }
}
