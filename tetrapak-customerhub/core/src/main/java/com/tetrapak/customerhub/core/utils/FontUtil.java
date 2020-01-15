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
    private static BaseFont jpBold;
    private static BaseFont sc;
    private static BaseFont scBold;
    private static BaseFont tc;
    private static BaseFont tcBold;
    private static BaseFont kr;
    private static BaseFont krBold;
    private static BaseFont en;
    private static BaseFont enBold;
    private static Font jpFont;
    private static Font jpFontBold;
    private static Font scFont;
    private static Font scFontBold;
    private static Font tcFont;
    private static Font tcFontBold;
    private static Font koFont;
    private static Font koFontBold;
    private static Font enFont;
    private static Font enFontBold;
    private static final int SIZE = 9;

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
     * Method to get Japanese font
     *
     * @param fontUrl Font URL
     * @return Font
     * @throws IOException       IO Exception
     * @throws DocumentException Document Exception
     */
    public static Font getJpFontBold(String fontUrl) throws IOException, DocumentException {
        if (null == jpBold) {
            jpBold = BaseFont.createFont(fontUrl,
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            if (null == jpFontBold) {
                jpFontBold = new Font(jpBold, SIZE);
            }
        }
        return jpFontBold;
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
     * Method to get Simplified Chinese font
     *
     * @param fontUrl Font URL
     * @return Font
     * @throws IOException       IO Exception
     * @throws DocumentException Document Exception
     */
    public static Font getScFontBold(String fontUrl) throws IOException, DocumentException {
        if (null == scBold) {
            scBold = BaseFont.createFont(fontUrl,
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            if (null == scFontBold) {
                scFontBold = new Font(scBold, SIZE);
            }
        }
        return scFontBold;
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
     * Method to get Traditional Chinese font
     *
     * @param fontUrl Font URL
     * @return Font
     * @throws IOException       IO Exception
     * @throws DocumentException Document Exception
     */
    public static Font getTcFontBold(String fontUrl) throws IOException, DocumentException {
        if (null == tcBold) {
            tcBold = BaseFont.createFont(fontUrl,
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            if (null == tcFontBold) {
                tcFontBold = new Font(tcBold, SIZE);
            }
        }
        return tcFontBold;
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
     * Method to get Korean font
     *
     * @param fontUrl Font URL
     * @return Font
     * @throws IOException       IO Exception
     * @throws DocumentException Document Exception
     */
    public static Font getKoFontBold(String fontUrl) throws IOException, DocumentException {
        if (null == krBold) {
            krBold = BaseFont.createFont(fontUrl,
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            if (null == koFontBold) {
                koFontBold = new Font(krBold, SIZE);
            }
        }
        return koFontBold;
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

    /**
     * Method to get English font
     *
     * @param fontUrl Font URL
     * @return Font
     * @throws IOException       IO Exception
     * @throws DocumentException Document Exception
     */
    public static Font getEnFontBold(String fontUrl) throws IOException, DocumentException {
        if (null == enBold) {
            enBold = BaseFont.createFont(fontUrl,
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            if (null == enFontBold) {
                enFontBold = new Font(enBold, SIZE);
            }
        }
        return enFontBold;
    }
}
