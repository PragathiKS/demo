package com.tetrapak.customerhub.core.servlets;

import com.tetrapak.customerhub.core.beans.pdf.Column;
import com.tetrapak.customerhub.core.beans.pdf.Table;
import com.tetrapak.customerhub.core.utils.TableBuilder;
import com.tetrapak.customerhub.core.utils.PDFUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;
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
                "sling.servlet.paths=" + "/bin/customerhub/pdf-generator"
        })
public class PDFGeneratorServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1;

    private static final Logger LOGGER = LoggerFactory.getLogger(PDFGeneratorServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        LOGGER.debug("HTTP GET request from PDFGeneratorServlet");

        final String FILE_NAME = "abcd.pdf";
        PDFont font = PDType1Font.HELVETICA;
        int fontSize = 12;
        List<String> lines = getLines();

        String option = request.getParameter("q");
        if (StringUtils.equalsIgnoreCase(option, "1")) {
            //Use method writeContent to print multiple lines
           // PDFUtil.writeContent(response, FILE_NAME, font, fontSize, lines);
        } else if (StringUtils.equalsIgnoreCase(option, "2")) {
            Table table = createTableContent();

            //Use method generateTablePDF to print table
           // PDFUtil.generateTablePDF(response, FILE_NAME, table);
        }
    }

    private List<String> getLines() {
        String line1 = "This is first line.";
        String line2 = "This is second line.";
        String line3 = "This is third line.";
        String line4 = "This is fourth line.";
        List<String> lines = new ArrayList<>();
        lines.add(line1);
        lines.add(line2);
        lines.add(line3);
        lines.add(line4);
        lines.add(line1);
        lines.add(line4);
        return lines;
    }

    private Table createTableContent() {
        // Total size of columns must not be greater than table width.
        List<Column> columns = new ArrayList<>();
        columns.add(new Column("TetraPak order No.", 100, true));
        columns.add(new Column("Purchase Order No.", 100, true));
        columns.add(new Column("Order date", 80, true));
        columns.add(new Column("Request Delivery", 90, true));
        columns.add(new Column("ETA", 100, true));
        columns.add(new Column("Status", 90, true));
        columns.add(new Column("Order type", 100, true));

        String[][] content = {
                {"12566676885654", "4444666768834", "2018-09-05", "2018-09-05", "2018-09-05", "Confirmed", "Packaging material"},
                {"12566676885654", "4444666768834", "2018-09-05", "2018-09-05", "2018-09-05", "Confirmed", "Packaging material"},
                {"12566676885654", "4444666768834", "2018-09-05", "2018-09-05", "2018-09-05", "Confirmed", "Packaging material"},
                {"12566676885654", "4444666768834", "2018-09-05", "2018-09-05", "2018-09-05", "Confirmed", "Packaging material"},
                {"12566676885654", "4444666768834", "2018-09-05", "2018-09-05", "2018-09-05", "Confirmed", "Packaging material"},
                {"12566676885654", "4444666768834", "2018-09-05", "2018-09-05", "2018-09-05", "Confirmed", "Packaging material"},
                {"12566676885654", "4444666768834", "2018-09-05", "2018-09-05", "2018-09-05", "Confirmed", "Packaging material"},
                {"12566676885654", "4444666768834", "2018-09-05", "2018-09-05", "2018-09-05", "Confirmed", "Packaging material"},
                {"12566676885654", "4444666768834", "2018-09-05", "2018-09-05", "2018-09-05", "Confirmed", "Packaging material"},
                {"12566676885654", "4444666768834", "2018-09-05", "2018-09-05", "2018-09-05", "Confirmed", "Packaging material"},
                {"12566676885654", "4444666768834", "2018-09-05", "2018-09-05", "2018-09-05", "Confirmed", "Packaging material"},
                {"12566676885654", "4444666768834", "2018-09-05", "2018-09-05", "2018-09-05", "Confirmed", "Packaging material"},
                {"12566676885654", "4444666768834", "2018-09-05", "2018-09-05", "2018-09-05", "Confirmed", "Packaging material"},
                {"12566676885654", "4444666768834", "2018-09-05", "2018-09-05", "2018-09-05", "Confirmed", "Packaging material"},
                {"12566676885654", "4444666768834", "2018-09-05", "2018-09-05", "2018-09-05", "Confirmed", "Packaging material"},
                {"12566676885654", "4444666768834", "2018-09-05", "2018-09-05", "2018-09-05", "Confirmed", "Packaging material"}
        };

        final float MARGIN = 40;
        final boolean IS_LANDSCAPE = true;
        final PDFont font = PDType1Font.HELVETICA;
        final float FONT_SIZE = 10;

        final float ROW_HEIGHT = 20;
        final float CELL_MARGIN = 5;

        float tableHeight = 1000;

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
                .setTextFont(font)
                .setFontSize(FONT_SIZE)
                .build();
        return table;
    }
}
