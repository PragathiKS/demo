package com.tetrapak.customerhub.core.utils;

import com.tetrapak.customerhub.core.beans.pdf.Column;
import com.tetrapak.customerhub.core.beans.pdf.Row;
import com.tetrapak.customerhub.core.beans.pdf.Table;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionURI;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDBorderStyleDictionary;
import org.apache.pdfbox.util.Matrix;
import org.apache.sling.api.SlingHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class for pdf related methods
 *
 * @author Nitin Kumar
 */
public class PDFUtil {

    /**
     * private constructor
     */
    private PDFUtil() {
        //adding private constructor
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(PDFUtil.class);

    /**
     * This method is used to print lines of string into a pdf file
     *
     * @param document document
     * @param margin   margin
     * @param height   height of row
     * @param color    color
     * @param rows     list of string lines to be printed on document
     */
    public static void writeContent(PDDocument document, PDPageContentStream contentStream, int margin, int height, Color color, List<Row> rows) {
        try {
            for (Row row : rows) {
                height -= row.getHeight();
                contentStream.beginText();
                contentStream.setFont(row.getFont(), row.getFontSize());
                contentStream.newLineAtOffset(margin, height);
                String rowContent = row.getContent();
                String[] message = {rowContent};
                if (row.isHref()) {
                    contentStream.setNonStrokingColor(Color.BLUE);
                    setTextWithLink(document.getPage(0), margin, height, rowContent, rowContent);
                } else {
                    contentStream.setNonStrokingColor(color);
                }
                contentStream.showTextWithPositioning(message);
                contentStream.endText();
                contentStream.stroke();

            }
        } catch (IOException e) {
            LOGGER.error("IOException in PDFUtil class {}", e);
        }
    }

    private static void setTextWithLink(PDPage page, int margin, int height, String text, String link) throws IOException {
        PDAnnotationLink txtLink = new PDAnnotationLink();
        txtLink.setAnnotationName(text);
        PDBorderStyleDictionary borderULine = new PDBorderStyleDictionary();
        borderULine.setStyle(PDBorderStyleDictionary.STYLE_UNDERLINE);
        borderULine.setWidth(0);
        txtLink.setBorderStyle(borderULine);

        PDActionURI action = new PDActionURI();
        action.setURI(link);
        txtLink.setAction(action);
        PDRectangle position = new PDRectangle();
        position.setLowerLeftX(margin);
        position.setLowerLeftY(height);
        position.setUpperRightX((float) margin + 150);
        position.setUpperRightY((float) height + 10);
        txtLink.setRectangle(position);
        page.getAnnotations().add(txtLink);
    }

    /**
     * @param document document
     * @param image    image
     * @param x        x position
     * @param y        y position
     * @param width    width
     * @param height   height
     * @throws IOException IO Exception
     */
    public static void drawImage(PDDocument document, PDPageContentStream contentStream, PDImageXObject image, int x, int y, int width, int height) throws IOException {
        contentStream.drawImage(image, x, y, width, height);
    }

    /**
     * @param response response
     * @param document document
     * @param fileName file name
     */
    public static void writeOutput(SlingHttpServletResponse response, PDDocument document, String fileName) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            document.save(out);
            byte[] docBytes = out.toByteArray();
            ByteArrayInputStream in = new ByteArrayInputStream(docBytes);

            response.setContentType("application/pdf");

            //use inline to open pdf in browser //use attachment to download pdf into the system
            response.setHeader("Content-Disposition", "inline; filename=" + fileName + ".pdf");
            // response.addHeader("Content-Disposition", "attachment; filename=" + fileName + ".pdf");

            int read;
            OutputStream os = response.getOutputStream();
            while ((read = in.read(docBytes)) != -1) {
                os.write(docBytes, 0, read);
            }

            os.flush();
            os.close();
            document.close();
        } catch (IOException e) {
            LOGGER.error("IOException in PDFUtil class {}", e);
        }
    }

    /**
     * @param doc document
     * @param contentStream content stream
     * @param table table
     * @param startY start y position
     */
    public static void drawTable(PDDocument doc, PDPageContentStream contentStream, Table table, int startY) {
        // Calculate pagination
        double d1 = Math.floor((double) table.getHeight() / (double) table.getRowHeight());
        Integer rowsPerPage = (int) d1 - 1;

        double d2 = Math.ceil((double) table.getNumberOfRows().floatValue() / rowsPerPage);
        Integer numberOfPages = (int) d2;

        // Generate each page, get the content and draw it
        for (int pageCount = 0; pageCount < numberOfPages; pageCount++) {
            try {
                String[][] currentPageContent = getContentForCurrentPage(table, rowsPerPage, pageCount);
                drawOnSamePage(table, currentPageContent, contentStream, startY);
            } catch (IOException e) {
                LOGGER.error("IOException in PDF Util class while printing table {}", e);
            }
        }
    }

    private static void drawOnSamePage(Table table, String[][] currentPageContent, PDPageContentStream contentStream, int startY)
            throws IOException {
        double widthLandscape = (double) table.getPageSize().getWidth() - (double) table.getMargin();
        double widthPortrait = (double) table.getPageSize().getHeight() - (double) table.getMargin();
        double tableTopY = table.isLandscape() ? widthLandscape : widthPortrait;

        double nextTextX = (double) table.getMargin() + (double) table.getCellMargin();
        double nextTextY = tableTopY - ((double) table.getRowHeight() / 2)
                - (((double) table.getTextFont().getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * (double) table.getFontSize()) / 4) - startY;

        writeContentLine(table.getColumnsNamesAsArray(), contentStream, (float) nextTextX, (float) nextTextY, table);
        nextTextY -= table.getRowHeight();
        nextTextX = (double) table.getMargin() + (double) table.getCellMargin();

        for (int i = 0; i < currentPageContent.length; i++) {
            writeContentLine(currentPageContent[i], contentStream, (float) nextTextX, (float) nextTextY, table);
            nextTextY -= (double) table.getRowHeight();
            nextTextX = (double) table.getMargin() + (double) table.getCellMargin();
        }

    }

    private static void writeContentLine(String[] lineContent, PDPageContentStream contentStream, double nextTextX, float nextTextY,
                                         Table table) throws IOException {
        for (int i = 0; i < table.getNumberOfColumns(); i++) {
            String text = lineContent[i];
            contentStream.beginText();
            contentStream.newLineAtOffset((float) nextTextX, nextTextY);
            contentStream.setFont(table.getColumns().get(i).isBold() ? table.getTextFontBold() : table.getTextFont(), table.getFontSize());
            contentStream.showText(null == text ? "" : text);
            contentStream.endText();
            nextTextX += table.getColumns().get(i).getWidth();
        }
    }

    private static String[][] getContentForCurrentPage(Table table, Integer rowsPerPage, int pageCount) {
        int startRange = pageCount * rowsPerPage;
        int endRange = (pageCount * rowsPerPage) + rowsPerPage;
        if (endRange > table.getNumberOfRows()) {
            endRange = table.getNumberOfRows();
        }
        return Arrays.copyOfRange(table.getContent(), startRange, endRange);
    }

    /**
     * @param document document
     * @param margin   margin
     * @param height   height
     * @param color    color
     * @throws IOException IO Exception
     */
    public static void drawLine(PDDocument document, PDPageContentStream contentStream, int margin, int length, int height, Color color, float thickness) throws IOException {
        contentStream.setStrokingColor(color);
        contentStream.moveTo(margin, height);
        contentStream.lineTo(margin + length, (float) height);
        contentStream.setLineWidth(thickness);
        contentStream.stroke();
    }

    public static Table getTable(List<Column> columns, String[][] content, PDFont muli_regular, PDFont muli_bold) {
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
