package com.tetrapak.customerhub.core.utils;

import com.tetrapak.customerhub.core.beans.pdf.Column;
import com.tetrapak.customerhub.core.beans.pdf.Row;
import com.tetrapak.customerhub.core.beans.pdf.Table;
import com.tetrapak.customerhub.core.beans.pdf.TableBuilder;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionURI;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDBorderStyleDictionary;
import org.apache.sling.api.SlingHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Utility class for pdf related methods
 *
 * @author Nitin Kumar
 */
public final class PDFUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PDFUtil.class);

    /**
     * private constructor
     */
    private PDFUtil() {
        //adding private constructor
    }

    /**
     * This method is used to print lines of string into a pdf file
     *
     * @param document      document
     * @param contentStream content stream
     * @param margin        margin
     * @param height        height of row
     * @param color         color
     * @param rows          list of string lines to be printed on document
     */
    public static void writeContent(PDDocument document, PDPageContentStream contentStream, int margin, int height,
            Color color, List<Row> rows) {
        try {
            for (Row row : rows) {
                height -= row.getHeight();
                contentStream.beginText();
                contentStream.setFont(row.getFont(), row.getFontSize());
                contentStream.newLineAtOffset(margin, height);
                String rowContent = row.getContent();
                String[] message = { rowContent };
                if (row.isHref()) {
                    contentStream.setNonStrokingColor(Color.BLUE);
                    setTextWithLink(document.getPage(0), margin, height, rowContent, row.getLink());
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

    private static void setTextWithLink(PDPage page, int margin, int height, String text, String link)
            throws IOException {
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
        double finalMargin = (double) margin + 100;
        double finalHeight = (double) height + 10;
        position.setUpperRightX((float) finalMargin);
        position.setUpperRightY((float) finalHeight);
        txtLink.setRectangle(position);
        page.getAnnotations().add(txtLink);
    }

    /**
     * Method to draw image
     *
     * @param contentStream content stream
     * @param image         image
     * @param x             x position
     * @param y             y position
     * @param width         width
     * @param height        height
     * @throws IOException IO Exception
     */
    public static void drawImage(PDPageContentStream contentStream, PDImageXObject image, int x, int y, int width,
            int height) throws IOException {
        contentStream.drawImage(image, x, y, width, height);
    }

    /**
     * Method to write output to page
     *
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
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName + ".pdf");
            response.addHeader("Content-Length", Integer.toString(in.available()));

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
     * Method to draw table
     *
     * @param contentStream content stream
     * @param table         table
     * @param startY        start y position
     * @return int height
     */
    public static int drawTable(PDPageContentStream contentStream, Table table, int startY) {
        double nextTextY = 0;
        try {
            String[][] currentPageContent = table.getContent();

            double nextTextX = (double) table.getMargin() + (double) table.getCellMargin();
            nextTextY = startY - ((double) table.getRowHeight() / 2) - (
                    ((double) table.getTextFont().getFontDescriptor().getFontBoundingBox().getHeight() / 1000
                            * (double) table.getFontSize()) / 4);

            writeContentLine(table.getColumnsNamesAsArray(), contentStream, (float) nextTextX, (float) nextTextY,
                    table);
            nextTextY -= table.getRowHeight();
            nextTextX = (double) table.getMargin() + (double) table.getCellMargin();

            for (int i = 0; i < currentPageContent.length; i++) {
                writeContentLine(currentPageContent[i], contentStream, (float) nextTextX, (float) nextTextY, table);
                nextTextY -= (double) table.getRowHeight();
                nextTextX = (double) table.getMargin() + (double) table.getCellMargin();
            }
        } catch (IOException e) {
            LOGGER.error("IOException in PDF Util class while printing table {}", e);
        }
        return (int) nextTextY;
    }

    private static void writeContentLine(String[] lineContent, PDPageContentStream contentStream, double nextTextX,
            float nextTextY, Table table) throws IOException {
        for (int i = 0; i < table.getNumberOfColumns(); i++) {
            String text = lineContent[i];
            if(null == text){
                text = StringUtils.EMPTY;
            }
            boolean isBold = text.startsWith(CustomerHubConstants.BOLD_IDENTIFIER);
            contentStream.beginText();
            contentStream.newLineAtOffset((float) nextTextX, nextTextY);
            contentStream.setFont(isBold ? table.getTextFontBold() : table.getTextFont(), table.getFontSize());
            contentStream
                    .showText(isBold ? StringUtils.substringAfter(text, CustomerHubConstants.BOLD_IDENTIFIER) : text);
            contentStream.endText();
            nextTextX += table.getColumns().get(i).getWidth();
        }
    }

    /**
     * Method to draw a line
     *
     * @param contentStream content stream
     * @param margin        margin
     * @param length        length
     * @param height        height
     * @param color         color
     * @param thickness     thickness
     * @throws IOException IO Exception
     */
    public static void drawLine(PDPageContentStream contentStream, int margin, int length, int height, Color color,
            float thickness) throws IOException {
        contentStream.setStrokingColor(color);
        contentStream.setLineWidth(thickness);
        contentStream.moveTo(margin, height);
        double finalMargin = (double) margin + (double) length;
        contentStream.lineTo((float) finalMargin, (float) height);
        contentStream.stroke();
    }

    /**
     * Method to create table
     *
     * @param columns     columns
     * @param content     content
     * @param rowHeight   row height
     * @param muliRegular regular font
     * @param muliBold    bold font
     * @param fontSize    font size
     * @return table table
     */
    public static Table getTable(List<Column> columns, String[][] content, double rowHeight, PDFont muliRegular,
            PDFont muliBold, double fontSize) {
        final float MARGIN = 65;
        return getTable(columns, content, rowHeight, muliRegular, muliBold, fontSize, MARGIN);
    }

    /**
     * Method to create table
     *
     * @param columns     columns
     * @param content     content
     * @param rowHeight   row height
     * @param muliRegular regular font
     * @param muliBold    bold font
     * @param fontSize    font size
     * @param margin      margin
     * @return table table
     */
    public static Table getTable(List<Column> columns, String[][] content, double rowHeight, PDFont muliRegular,
            PDFont muliBold, double fontSize, float margin) {
        final float MARGIN = margin;
        final float FONT_SIZE = (float) fontSize;

        final float ROW_HEIGHT = (float) rowHeight;
        final float CELL_MARGIN = 0;

        double width = 8.5 * 72;
        double height = (double) 11 * 72;

        return new TableBuilder().setCellMargin(CELL_MARGIN).setColumns(columns).setContent(content)
                .setNumberOfRows(content.length).setRowHeight(ROW_HEIGHT).setMargin(MARGIN)
                .setPageSize(new PDRectangle((float) width, (float) height)).setTextFont(muliRegular)
                .setTextFontBold(muliBold).setFontSize(FONT_SIZE).build();
    }

    /**
     * Method to get a new page on a PDF document. It closes current content stream and opens
     * a new one and returns
     *
     * @param document      document
     * @param contentStream content stream
     * @return content stream
     * @throws IOException IO Exception
     */
    public static PDPageContentStream getNewPage(PDDocument document, PDPageContentStream contentStream)
            throws IOException {
        PDPage page = new PDPage();
        document.addPage(page);
        contentStream.close();
        contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true, true);
        return contentStream;
    }
}
