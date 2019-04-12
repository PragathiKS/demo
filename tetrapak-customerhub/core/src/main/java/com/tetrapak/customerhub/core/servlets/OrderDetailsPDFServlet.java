package com.tetrapak.customerhub.core.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.pdf.*;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.OrderDetailsService;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import com.tetrapak.customerhub.core.utils.PDFUtil;
import com.tetrapak.customerhub.core.utils.TableBuilder;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.encoding.Encoding;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * PDF Generator Servlet
 *
 * @author Nitin Kumar
 */
@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=PDF Generator Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/bin/customerhub/order-detail-pdf"
        })
public class OrderDetailsPDFServlet extends SlingSafeMethodsServlet {

    @Reference
    OrderDetailsService orderDetailsService;

    private static final long serialVersionUID = 2;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDetailsPDFServlet.class);

    PDDocument document = new PDDocument();
    PDFont muli_regular;
    PDFont muli_bold;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        LOGGER.debug("HTTP GET request from OrderDetailsPDFServlet");

        final String orderNumber = request.getParameter("orderNumber");
        final String token = request.getParameter("token");

        JsonObject jsonResponse = orderDetailsService.getOrderDetails(orderNumber, token);

        JsonElement status = jsonResponse.get(CustomerHubConstants.STATUS);
        if (!status.toString().equalsIgnoreCase("200")) {
            LOGGER.error("could not get successful response from API");
        } else {
            JsonElement result = jsonResponse.get(CustomerHubConstants.RESULT);

            Gson gson = new Gson();
            OrderDetailResponse orderDetailResponse = gson.fromJson(HttpUtil.getStringFromJsonWithoutEscape(result), OrderDetailResponse.class);
            OrderDetails orderDetails = orderDetailResponse.getOrderDetails();
            try {
                InputStream in = getClass().getResourceAsStream("/fonts/muli-light-webfont.ttf");

                muli_regular = PDTrueTypeFont.load(document, in, Encoding.getInstance(COSName.STANDARD_ENCODING));

                in = getClass().getResourceAsStream("/fonts/muli-bold-webfont.ttf");
                muli_bold = PDTrueTypeFont.load(document, in, Encoding.getInstance(COSName.STANDARD_ENCODING));

                PDPage page = new PDPage();
                document.addPage(page);

                document = PDFUtil.writeContent(document, 750, getHeadLines(orderNumber, orderDetails));

                Table table = createOrderDetailTable(orderDetails);
                PDFUtil.drawTableOnSamePage(document, table);

                document = PDFUtil.writeContent(document, 600, getContactLines());

                PDFUtil.writeOutput(response, document, orderNumber);
            } catch (IOException e) {
                LOGGER.error("IOException {}", e);
            } finally {
                try {
                    document.close();
                } catch (IOException e) {
                    LOGGER.error("IOException {}", e);
                }
            }
        }
    }

    private List<Row> getHeadLines(String orderNumber, OrderDetails orderDetails) {
        List<Row> rows = new ArrayList<>();
        rows.add(new Row("Order details", 20, muli_regular, 18));
        rows.add(new Row("", 30, muli_regular, 12));
        rows.add(new Row("Tetra Pak Order number: " + orderNumber + " - " + orderDetails.getStatus(), 20, muli_regular, 11));
        rows.add(new Row("", 10, muli_regular, 12));
        return rows;
    }

    private List<Row> getContactLines() {
        List<Row> rows = new ArrayList<>();
        rows.add(new Row("Customer support center", 10, muli_bold, 8));
        rows.add(new Row("customersupporter@teatrapak.com", 10, muli_regular, 8));
        rows.add(new Row("+4635123456", 10, muli_regular, 8));
        return rows;
    }

    private Table createOrderDetailTable(OrderDetails orderDetails) {
        // Total size of columns must not be greater than table width.
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

        final float MARGIN = 65;
        final boolean IS_LANDSCAPE = false;
        final float FONT_SIZE = 7;

        final float ROW_HEIGHT = 15;
        final float CELL_MARGIN = 0;

        float tableHeight = 500;

        double width = 8.5 * 72;
        double height = (double) 11 * 72;

        Table table = new TableBuilder()
                .setCellMargin(CELL_MARGIN)
                .setColumns(columns)
                .setContent(content)
                .setHeight(tableHeight)
                .setNumberOfRows(content.length)
                .setRowHeight(ROW_HEIGHT)
                .setMargin(MARGIN)
                .setPageSize(new PDRectangle((float) width,
                        (float) height))
                .setLandscape(IS_LANDSCAPE)
                .setTextFont(muli_regular)
                .setTextFontBold(muli_bold)
                .setFontSize(FONT_SIZE)
                .build();
        return table;
    }
}
