package com.tetrapak.supplierportal.core.utils;

import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;

public class PDFUtil {
	
	private PDFUtil() {}

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
        image.setAlignment(Element.ALIGN_RIGHT);
        document.add(image);
    }
}
