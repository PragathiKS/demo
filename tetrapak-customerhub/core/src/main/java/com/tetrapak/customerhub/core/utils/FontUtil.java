package com.tetrapak.customerhub.core.utils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

import java.io.IOException;

/**
 * Util class to provide Singleton Font objects
 *
 * @author Nitin Kumar
 */
public final class FontUtil {
    private static BaseFont jp;
    private static BaseFont sc;
    private static BaseFont tc;
    private static BaseFont kr;
    private static BaseFont en;
    private static Font jpFont;
    private static Font scFont;
    private static Font tcFont;
    private static Font koFont;
    private static Font enFont;
    private static final int SIZE = 10;

    /**
     * private constructor
     */
    private FontUtil() {
        //adding private constructor
    }

    /**
     * Method to get Japanese font
     *
     * @param fontUrl Font URL
     * @return Font
     * @throws IOException       IO Exception
     * @throws DocumentException Document Exception
     */
    public static Font getJpFont(String fontUrl) throws IOException, DocumentException {
        if (null == jp) {
            jp = BaseFont.createFont(fontUrl,
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            if (null == jpFont) {
                jpFont = new Font(jp, SIZE);
            }
        }
        return jpFont;
    }

    /**
     * Method to get Simplified Chinese font
     *
     * @param fontUrl Font URL
     * @return Font
     * @throws IOException       IO Exception
     * @throws DocumentException Document Exception
     */
    public static Font getScFont(String fontUrl) throws IOException, DocumentException {
        if (null == sc) {
            sc = BaseFont.createFont(fontUrl,
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            if (null == scFont) {
                scFont = new Font(sc, SIZE);
            }
        }
        return scFont;
    }

    /**
     * Method to get Traditional Chinese font
     *
     * @param fontUrl Font URL
     * @return Font
     * @throws IOException       IO Exception
     * @throws DocumentException Document Exception
     */
    public static Font getTcFont(String fontUrl) throws IOException, DocumentException {
        if (null == tc) {
            tc = BaseFont.createFont(fontUrl,
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            if (null == tcFont) {
                tcFont = new Font(tc, SIZE);
            }
        }
        return tcFont;
    }

    /**
     * Method to get Korean font
     *
     * @param fontUrl Font URL
     * @return Font
     * @throws IOException       IO Exception
     * @throws DocumentException Document Exception
     */
    public static Font getKoFont(String fontUrl) throws IOException, DocumentException {
        if (null == kr) {
            kr = BaseFont.createFont(fontUrl,
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            if (null == koFont) {
                koFont = new Font(kr, SIZE);
            }
        }
        return koFont;
    }

    /**
     * Method to get English font
     *
     * @param fontUrl Font URL
     * @return Font
     * @throws IOException       IO Exception
     * @throws DocumentException Document Exception
     */
    public static Font getEnFont(String fontUrl) throws IOException, DocumentException {
        if (null == en) {
            en = BaseFont.createFont(fontUrl,
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            if (null == enFont) {
                enFont = new Font(en, SIZE);
            }
        }
        return enFont;
    }
}
