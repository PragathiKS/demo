package com.tetrapak.customerhub.core.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;

/**
 * Utility class for pdf related methods
 *
 * @author Nitin Kumar
 */
public final class PDFUtil2 {

    /**
     * private constructor
     */
    private PDFUtil2() {
        //adding private constructor
    }

    /**
     * Method to draw image
     *
     * @param document  document
     * @param imagePath complete image path
     * @param width     width
     * @param height    height
     * @throws IOException       IO Exception
     * @throws DocumentException Document Exception
     */
    public static void drawImage(Document document, String imagePath, float width, float height)
            throws IOException, DocumentException {
        Image image = Image.getInstance(imagePath);
        image.scaleToFit(width, height);
        document.add(image);
    }

    /**
     * Method to draw line
     *
     * @param writer PD writer
     * @param x1     x1
     * @param x2     x2
     * @param y      height
     */
    public static void drawLine(PdfWriter writer, int x1, int x2, float y) {
        PdfContentByte canvas = writer.getDirectContent();
        final float WIDTH = 0.01f;
        canvas.setLineWidth(WIDTH);
        canvas.moveTo(x1, y);
        canvas.lineTo(x2, y);
        canvas.closePathStroke();
    }
}
