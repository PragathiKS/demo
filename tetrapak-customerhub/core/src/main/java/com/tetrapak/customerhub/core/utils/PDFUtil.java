package com.tetrapak.customerhub.core.utils;

import com.drew.lang.annotations.NotNull;
import com.tetrapak.customerhub.core.pdf.Table;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.util.Matrix;
import org.apache.sling.api.SlingHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class
 */
public class PDFUtil {

    /**
     * private constructor
     */
    private PDFUtil(){
        //adding private constructor
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(PDFUtil.class);

    private static final PDPage page = new PDPage();

    /**This method is used to print lines of string into a pdf file
     * @param response http servlet response
     * @param fileName string file name
     * @param font font for document
     * @param fontSize font size
     * @param lines list of string lines to be printed on document
     */
    public static void printTextPDF(SlingHttpServletResponse response, @NotNull final String fileName, PDFont font, int fontSize, List<String> lines) {
        PDDocument document = new PDDocument();
        int count = 0;
        List<String> tempLines = new ArrayList<>();
        for (String line : lines) {
            count++;
            tempLines.add(line);
            if (count == 14) {
                document.addPage(page);
                printPage(document, page, font, fontSize, tempLines);
                count = 0;
                tempLines = new ArrayList<>();
            }
        }
        if (!tempLines.isEmpty()) {
            document.addPage(page);
            printPage(document, page, font, fontSize, tempLines);
        }
        printOutput(response, document, fileName);
    }

    private static void printPage(PDDocument document, PDPage page, PDFont font, int fontSize, List<String> lines) {
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            int height = 750;
            for (String line : lines) {
                height -= 50;
                contentStream.beginText();
                contentStream.setFont(font, fontSize);
                contentStream.newLineAtOffset(100, height);
                String[] message = {line};
                contentStream.showTextWithPositioning(message);
                contentStream.endText();
            }

            // Make sure that the content stream is closed:
            contentStream.close();
        } catch (IOException e) {
            LOGGER.error("IOException in PDFUtil class {}", e);
        }
    }

    private static void printOutput(SlingHttpServletResponse response, PDDocument document, String fileName) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            document.save(out);

            byte[] docBytes = out.toByteArray();
            ByteArrayInputStream in = new ByteArrayInputStream(docBytes);

            response.setContentType("application/pdf");

            //use inline to open pdf in browser
            response.setHeader("Content-Disposition", "inline; filename=" + fileName);

            //use attachment to download pdf into the system
            // response.addHeader("Content-Disposition", "attachment; filename=" + fileName);

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

    /**This method is used to create pdf file printing in tabular format
     * @param response http servlet response
     * @param fileName string file name
     * @param table table object
     * @throws IOException IO Exception
     */
    public static void generateTablePDF(SlingHttpServletResponse response, String fileName, Table table) throws IOException {
        PDDocument document = null;
        try {
            document = new PDDocument();
            drawTable(document, table);
            printOutput(response, document, fileName);

        } finally {
            if (document != null) {
                document.close();
            }
        }
    }


    /**This method is used to draw table based on table content passed as parameter
     * @param doc pdf document
     * @param table table object
     * @throws IOException IO Exception
     */
    public static void drawTable(PDDocument doc, Table table) throws IOException {
        // Calculate pagination
        double d1 = Math.floor((double) table.getHeight() / (double) table.getRowHeight());
        Integer rowsPerPage = (int) d1 - 1;

        double d2 = Math.ceil((double) table.getNumberOfRows().floatValue() / rowsPerPage);
        Integer numberOfPages = (int) d2;

        // Generate each page, get the content and draw it
        for (int pageCount = 0; pageCount < numberOfPages; pageCount++) {
            PDPage page = generatePage(doc, table);
            PDPageContentStream contentStream = generateContentStream(doc, page, table);
            String[][] currentPageContent = getContentForCurrentPage(table, rowsPerPage, pageCount);
            drawCurrentPage(table, currentPageContent, contentStream);
        }
    }

    // Draws current page table grid and border lines and content
    private static void drawCurrentPage(Table table, String[][] currentPageContent, PDPageContentStream contentStream)
            throws IOException {
        double widthLandscape = (double) table.getPageSize().getWidth() - (double) table.getMargin();
        double widthPortrait = (double) table.getPageSize().getHeight() - (double) table.getMargin();
        double tableTopY = table.isLandscape() ? widthLandscape : widthPortrait;

        // Draws grid and borders
        drawTableGrid(table, currentPageContent, contentStream, tableTopY);

        // Position cursor to start drawing content
        double nextTextX = (double) table.getMargin() + (double) table.getCellMargin();
        // Calculate center alignment for text in cell considering font height
        double nextTextY = tableTopY - ((double) table.getRowHeight() / 2)
                - (((double) table.getTextFont().getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * (double) table.getFontSize()) / 4);

        // Write column headers
        writeContentLine(table.getColumnsNamesAsArray(), contentStream, (float) nextTextX, (float) nextTextY, table);
        nextTextY -= table.getRowHeight();
        nextTextX = (double) table.getMargin() + (double) table.getCellMargin();

        // Write content
        for (int i = 0; i < currentPageContent.length; i++) {
            writeContentLine(currentPageContent[i], contentStream, (float) nextTextX, (float) nextTextY, table);
            nextTextY -= (double) table.getRowHeight();
            nextTextX = (double) table.getMargin() + (double) table.getCellMargin();
        }

        contentStream.close();
    }

    // Writes the content for one line
    private static void writeContentLine(String[] lineContent, PDPageContentStream contentStream, double nextTextX, float nextTextY,
                                         Table table) throws IOException {
        for (int i = 0; i < table.getNumberOfColumns(); i++) {
            String text = lineContent[i];
            contentStream.beginText();
            contentStream.newLineAtOffset((float) nextTextX, nextTextY);
            contentStream.showText(null == text ? "" : text);
            contentStream.endText();
            nextTextX += table.getColumns().get(i).getWidth();
        }
    }

    private static void drawTableGrid(Table table, String[][] currentPageContent, PDPageContentStream contentStream, double tableTopY)
            throws IOException {
        // Draw row lines
        double nextY = tableTopY;
        for (int i = 0; i <= currentPageContent.length + 1; i++) {

            contentStream.moveTo(table.getMargin(), (float) nextY);
            double width = (double) table.getMargin() + (double) table.getWidth();
            contentStream.lineTo((float) width, (float) nextY);
            contentStream.stroke();

            nextY -= table.getRowHeight();
        }

        // Draw column lines
     /*   final float tableYLength = table.getRowHeight() + (table.getRowHeight() * currentPageContent.length);
        final float tableBottomY = tableTopY - tableYLength;
        float nextX = table.getMargin();
        for (int i = 0; i < table.getNumberOfColumns(); i++) {

            contentStream.moveTo(nextX, tableTopY);
            contentStream.lineTo(nextX, tableBottomY);
            contentStream.stroke();

            nextX += table.getColumns().get(i).getWidth();
        }
        contentStream.moveTo(nextX, tableTopY);
        contentStream.lineTo(nextX, tableBottomY);
        contentStream.stroke();*/
    }

    private static String[][] getContentForCurrentPage(Table table, Integer rowsPerPage, int pageCount) {
        int startRange = pageCount * rowsPerPage;
        int endRange = (pageCount * rowsPerPage) + rowsPerPage;
        if (endRange > table.getNumberOfRows()) {
            endRange = table.getNumberOfRows();
        }
        return Arrays.copyOfRange(table.getContent(), startRange, endRange);
    }

    private static PDPage generatePage(PDDocument doc, Table table) {
        PDPage page = new PDPage();
        page.setMediaBox(table.getPageSize());
        page.setRotation(table.isLandscape() ? 90 : 0);
        doc.addPage(page);
        return page;
    }

    private static PDPageContentStream generateContentStream(PDDocument doc, PDPage page, Table table) throws IOException {
        PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, false);
        if (table.isLandscape()) {
            contentStream.transform(new Matrix(0, 1, -1, 0, table.getPageSize().getWidth(), 0));
        }
        contentStream.setFont(table.getTextFont(), table.getFontSize());
        return contentStream;
    }
}
