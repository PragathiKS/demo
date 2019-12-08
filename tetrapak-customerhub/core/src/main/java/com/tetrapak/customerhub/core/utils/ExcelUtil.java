package com.tetrapak.customerhub.core.utils;

import com.tetrapak.customerhub.core.beans.excel.ExcelFileData;
import com.tetrapak.customerhub.core.exceptions.ExcelReportRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.SlingHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This utility is used to generate and download an excel file based on the 2D String array provided as a parameter
 *
 * @author Nitin Kumar
 * @author Swati Lamba
 */
public final class ExcelUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);
    private static final Pattern REMOVE_TAGS = Pattern.compile("<.+?>");

    public static final String MERGE_ROW_TAG = "<mergerow>";
    public static final String LIGHT_GREY_LEFT_TAG = "<lightGreyLeft>";
    public static final String LIGHT_GREY_BG_TAG = "<lightGreyBG>";
    public static final String BOLD_TAG = "<bold>";
    public static final String HALF_BOLD_TAG = "<halfBold>";
    public static final String DARK_GREY_BG_TAG = "<darkGreyBG>";
    public static final String MERGE_2_CELLS_TAG = "<merge2cells>";
    public static final String RIGHT_ALIGN_TAG = "<rightAlign>";
    public static final String ALIGN_CENTER_TAG = "<aligncenter>";
    public static final String HALF_BOLD_IDENTIFIER = ":";
    public static final String NEW_LINE_DETECTOR = "\n";
    private static final String DEFAULT_FONT_NAME = "Microsoft Sans Serif";
    private static final short DEFAULT_FONT_HEIGHT = (short) 9;
    private static final String LIGHT_GREY_BG_CELL_STYLE = "lightGreyBackgroudStyle";
    private static final String REGULAR_CELL_STYLE = "regularStyle";
    private static final String EMPTY_CELL_STYLE = "emptyCellStyle";
    private static final String REGULAR_CENTER_CELL_STYLE = "regularCenterStyle";
    private static final String REGULAR_RIGHT_CELL_STYLE = "regularRightStyle";
    private static final String DARK_GREY_BG_CELL_STYLE = "darkGreyBackgroudStyle";
    private static final String LIGHT_GREY_LEFT_CELL_STYLE = "lightGreyLeftStyle";
    private static final String LIME_BG_CELL_STYLE = "limeBGStyle";
    public static final String LIME_BG_TAG = "<limeBG>";
    private static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String FILE_EXTENSION = ".xlsx";
    private static final String RESP_HEADER_DATA = "attachment; filename=";
    public static final String NO_BORDER_TAG = "<noborder>";

    private ExcelUtil() {
        // Disallowed to create Object out of class
    }

    /**
     * @param workBook
     * @param sheetName
     * @return Sheet
     */
    private static Sheet getExcelSheet(Workbook workBook, String sheetName) {
        Sheet sheet = null;
        if (Objects.nonNull(workBook) && Objects.nonNull(sheetName)) {
            sheet = workBook.createSheet(sheetName);
        }
        return sheet;
    }

    /**
     * @param sheet
     * @param rowNum
     * @return Row
     */
    private static Row getRow(Sheet sheet, int rowNum) {
        Row row = null;
        if (Objects.nonNull(sheet)) {
            row = sheet.createRow(rowNum);
        }
        return row;
    }

    /**
     * @param sheet
     * @param borderColor
     */
    private static void resizeCellToFitContent(Sheet sheet, int borderColor) {
        short columnCount = sheet.getRow(0).getLastCellNum();

        // Resize all columns to fit the content size
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
        List<CellRangeAddress> regionList = sheet.getMergedRegions();
        for (CellRangeAddress cellRangeAddress : regionList) {
            RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
            RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
            RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
            RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
            RegionUtil.setBottomBorderColor(borderColor, cellRangeAddress, sheet);
            RegionUtil.setLeftBorderColor(borderColor, cellRangeAddress, sheet);
            RegionUtil.setRightBorderColor(borderColor, cellRangeAddress, sheet);
            RegionUtil.setTopBorderColor(borderColor, cellRangeAddress, sheet);
        }

        if (sheet.getColumnWidth(0) < 2600) {
            sheet.setColumnWidth(0, 2600);
        }
    }

    /**
     * @param workBook
     * @throws IOException
     */
    private static void closeWorkbook(Workbook workBook) throws IOException {
        if (Objects.nonNull(workBook)) {
            workBook.close();
        }
    }

    /**
     * Generate the excel file using the below parameters
     *
     * @param response        Response
     * @param excelReportData Excel Bean Data
     * @return boolean
     */
    public static boolean generateExcelReport(SlingHttpServletResponse response, ExcelFileData excelReportData) {

        if (Objects.nonNull(excelReportData)) {
            XSSFWorkbook workBook = new XSSFWorkbook();
            Sheet sheet = getExcelSheet(workBook, excelReportData.getExcelSheetName());
            if (Objects.nonNull(sheet) && Objects.nonNull(excelReportData.getData())) {
                sheet.setDisplayGridlines(false);
                prepareReportData(workBook, sheet, excelReportData.getData(), excelReportData.getCellBorderColor());
                if (excelReportData.isHasMargin()) {
                    setMarginsToSheet(sheet, excelReportData.getCellBorderColor());
                }
                resizeCellToFitContent(sheet, excelReportData.getCellBorderColor());
            }
            try {
                downloadExcel(response, workBook, excelReportData);
                return true;
            } catch (ExcelReportRuntimeException e) {
                LOGGER.error("ExcelReportRuntimeException", e);
            }
        } else {
            LOGGER.warn("ExcelReportData is null!!");
        }
        return false;

    }

    private static void setMarginsToSheet(Sheet sheet, short borderColor) {
        int rowCount = sheet.getLastRowNum();
        int columnCount = sheet.getRow(0).getLastCellNum();

        CellRangeAddress region = new CellRangeAddress(0, rowCount, 0, columnCount - 1);
        RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
        RegionUtil.setTopBorderColor(borderColor, region, sheet);
        RegionUtil.setBottomBorderColor(borderColor, region, sheet);
        RegionUtil.setLeftBorderColor(borderColor, region, sheet);
        RegionUtil.setRightBorderColor(borderColor, region, sheet);
    }

    /**
     * @param workBook    work book
     * @param sheet       sheet
     * @param reportData  report data
     * @param borderColor
     */
    private static void prepareReportData(Workbook workBook, Sheet sheet, String[][] reportData, short borderColor) {
        int rowCount = 0;

        Cell cell;
        Row row;
        int columnCount;
        Map<String, CellStyle> cellStyles = getCellStyleMap(workBook, borderColor);
        for (String[] data : reportData) {
            row = getRow(sheet, rowCount);
            columnCount = 0;
            if (Objects.nonNull(row)) {
                for (String field : data) {
                    cell = row.createCell(columnCount);
                    columnCount++;
                    setRowHeight(row, field, sheet.getDefaultRowHeightInPoints());
                    processFieldToCellValue(sheet, cell, field, cellStyles,
                            reportData[0].length - 1, workBook.createFont());
                }
            }
            ++rowCount;
        }
    }

    /**
     * @param sheet      sheet
     * @param cell       cell
     * @param field      field
     * @param cellStyles cell style
     * @param length     length
     * @param customFont font
     */
    private static void processFieldToCellValue(Sheet sheet, Cell cell, String field, Map<String, CellStyle> cellStyles,
                                                int length, Font customFont) {

        if (StringUtils.isNotBlank(field)) {

            List<String> tags = getTagsFromField(field);
            XSSFRichTextString richText = applyCustomStyles(field, tags, customFont);

            if (tags.contains(MERGE_ROW_TAG)) {
                sheet.addMergedRegion(
                        new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex(), cell.getColumnIndex(), length));
            } else if (tags.contains(MERGE_2_CELLS_TAG)) {
                sheet.addMergedRegion(
                        new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex(), cell.getColumnIndex() - 1,
                                cell.getColumnIndex()));
            }

            applyCellStyling(cell, tags, cellStyles);
            cell.setCellValue(richText);
        } else {
            cell.setCellStyle(cellStyles.get(EMPTY_CELL_STYLE));
        }
    }

    /**
     * @param cell       cell
     * @param tags       tags
     * @param cellStyles style
     */
    private static void applyCellStyling(Cell cell, List<String> tags, Map<String, CellStyle> cellStyles) {
        if (tags.contains(LIGHT_GREY_BG_TAG)) {
            cell.setCellStyle(cellStyles.get(LIGHT_GREY_BG_CELL_STYLE));
        } else if (tags.contains(ALIGN_CENTER_TAG)) {
            cell.setCellStyle(cellStyles.get(REGULAR_CENTER_CELL_STYLE));
        } else if (tags.contains(DARK_GREY_BG_TAG)) {
            cell.setCellStyle(cellStyles.get(DARK_GREY_BG_CELL_STYLE));
        } else if (tags.contains(RIGHT_ALIGN_TAG)) {
            cell.setCellStyle(cellStyles.get(REGULAR_RIGHT_CELL_STYLE));
        } else if (tags.contains(LIME_BG_TAG)) {
            cell.setCellStyle(cellStyles.get(LIME_BG_CELL_STYLE));
        } else if (tags.contains(LIGHT_GREY_LEFT_TAG)) {
            cell.setCellStyle(cellStyles.get(LIGHT_GREY_LEFT_CELL_STYLE));
        } else if (tags.contains(NO_BORDER_TAG)) {
            cell.setCellStyle(cellStyles.get(EMPTY_CELL_STYLE));
        } else {
            cell.setCellStyle(cellStyles.get(REGULAR_CELL_STYLE));
        }

    }

    /**
     * Setting the row height if the new line is present
     *
     * @param row           row
     * @param field         field
     * @param defaultHeight height
     */
    private static void setRowHeight(Row row, String field, double defaultHeight) {
        if (!StringUtils.isBlank(field) && field.contains(NEW_LINE_DETECTOR)) {
            row.setHeightInPoints((float) (3 * defaultHeight));
        }
    }

    /**
     * Get list of CellStyles in form a map
     *
     * @param workbook    workbook
     * @param borderColor border color
     * @return getCellStyleMap style map
     */
    private static Map<String, CellStyle> getCellStyleMap(Workbook workbook, short borderColor) {
        Map<String, CellStyle> cellStyles = new HashMap<>();
        CellStyle cellStyle = workbook.createCellStyle();
        CellStyle borderStyle = setBorderStyle(BorderStyle.THIN, borderColor, cellStyle);
        cellStyles.put(REGULAR_CELL_STYLE, borderStyle);

        cellStyle = workbook.createCellStyle();
        CellStyle emptyCellStyle = setBorderStyle(BorderStyle.THIN, borderColor, cellStyle);
        cellStyles.put(EMPTY_CELL_STYLE, emptyCellStyle);

        cellStyle = workbook.createCellStyle();
        CellStyle regularCenterStyle = setBorderStyle(BorderStyle.THIN, borderColor, cellStyle);
        regularCenterStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyles.put(REGULAR_CENTER_CELL_STYLE, regularCenterStyle);

        cellStyle = workbook.createCellStyle();
        CellStyle regularRightStyle = setBorderStyle(BorderStyle.THIN, borderColor, cellStyle);
        regularRightStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellStyles.put(REGULAR_RIGHT_CELL_STYLE, regularRightStyle);

        cellStyle = workbook.createCellStyle();
        CellStyle limeBGStyle = setBorderStyle(BorderStyle.THIN, borderColor, cellStyle);
        limeBGStyle.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.index);
        limeBGStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        limeBGStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellStyles.put(LIME_BG_CELL_STYLE, limeBGStyle);

        cellStyle = workbook.createCellStyle();
        CellStyle lightGreyLeftStyle = setBorderStyle(BorderStyle.THIN, borderColor, cellStyle);
        lightGreyLeftStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        lightGreyLeftStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        lightGreyLeftStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyles.put(LIGHT_GREY_LEFT_CELL_STYLE, lightGreyLeftStyle);

        cellStyle = workbook.createCellStyle();
        CellStyle lightGreyBackgroudStyle = setBorderStyle(BorderStyle.THIN, borderColor, cellStyle);
        lightGreyBackgroudStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        lightGreyBackgroudStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        lightGreyBackgroudStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyles.put(LIGHT_GREY_BG_CELL_STYLE, lightGreyBackgroudStyle);

        cellStyle = workbook.createCellStyle();
        CellStyle darkGreyBackgroudStyle = setBorderStyle(BorderStyle.THIN, borderColor, cellStyle);
        darkGreyBackgroudStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.index);
        darkGreyBackgroudStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        darkGreyBackgroudStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyles.put(DARK_GREY_BG_CELL_STYLE, darkGreyBackgroudStyle);

        return cellStyles;
    }

    /**
     * @param borderStyle
     * @param color
     * @param cellStyle
     * @return
     */
    private static CellStyle setBorderStyle(BorderStyle borderStyle, short color, CellStyle cellStyle) {
        cellStyle.setBorderLeft(borderStyle);
        cellStyle.setBorderRight(borderStyle);
        cellStyle.setBorderTop(borderStyle);
        cellStyle.setBorderBottom(borderStyle);
        cellStyle.setTopBorderColor(color);
        cellStyle.setRightBorderColor(color);
        cellStyle.setLeftBorderColor(color);
        cellStyle.setBottomBorderColor(color);
        cellStyle.setWrapText(true);
        return cellStyle;
    }

    /**
     * @param response        response
     * @param workBook        work book
     * @param excelReportData excel data
     * @throws ExcelReportRuntimeException
     */
    private static void downloadExcel(SlingHttpServletResponse response, Workbook workBook,
                                      ExcelFileData excelReportData) {
        OutputStream os = null;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            workBook.write(out);
            byte[] docBytes = out.toByteArray();
            ByteArrayInputStream in = new ByteArrayInputStream(docBytes);
            response.setContentType(CONTENT_TYPE);
            response.setHeader(CONTENT_DISPOSITION,
                    RESP_HEADER_DATA + excelReportData.getFileName() + FILE_EXTENSION + "; size=" + in.available());
            response.addHeader("Content-Length", Integer.toString(in.available()));
            int read;
            os = response.getOutputStream();
            while ((read = in.read(docBytes)) != -1) {
                os.write(docBytes, 0, read);
            }
        } catch (IOException e) {
            LOGGER.error("\nA run-time exception occured while generating excel report.");
            throw new ExcelReportRuntimeException("Something went wrong while generating Excel Report", e);
        } finally {
            if (Objects.nonNull(os)) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    LOGGER.error("IOException occured while closing the stream.", e);
                }
            }
            try {
                closeWorkbook(workBook);
            } catch (IOException e) {
                LOGGER.error("IOException occured while closing the workbook.", e);
            }
        }
    }

    /**
     * Remove all the custom tags
     *
     * @param string string
     * @return string
     */
    private static String stripTags(String string) {
        if (StringUtils.isBlank(string)) {
            return string;
        }
        Matcher m = REMOVE_TAGS.matcher(string);
        return m.replaceAll(StringUtils.EMPTY);
    }

    /**
     * Apply custom font styling to the richtextString based on the list of tags
     *
     * @param field      : raw data to be set in the cell
     * @param tags       : list of tags based on which the manipulation would be done
     * @param customFont fint
     * @return manipulated richTextString from the string data to be put in the cell
     */
    private static XSSFRichTextString applyCustomStyles(String field, List<String> tags, Font customFont) {
        boolean isWhite = field.startsWith("<white>");
        field = stripTags(field);
        XSSFRichTextString richTextString = new XSSFRichTextString(field);
        customFont.setFontName(DEFAULT_FONT_NAME);
        customFont.setFontHeightInPoints(DEFAULT_FONT_HEIGHT);
        customFont.setColor(isWhite ? HSSFColor.HSSFColorPredefined.WHITE.getIndex() : HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        if (!tags.isEmpty()) {
            if (tags.contains(BOLD_TAG)) {
                customFont.setBold(true);
            }
            richTextString.applyFont(customFont);
            if (tags.contains(HALF_BOLD_TAG)) {
                customFont.setBold(true);
                int index = 1;
                if (field.contains(HALF_BOLD_IDENTIFIER)) {
                    index = field.indexOf(HALF_BOLD_IDENTIFIER);
                } else {
                    index = field.length() - 1;
                }
                richTextString.applyFont(0, index, customFont);
            }
        } else {
            richTextString.applyFont(customFont);
        }

        return richTextString;
    }

    /**
     * get the tags set in the field and return as a list
     *
     * @param field field
     * @return list
     */
    private static List<String> getTagsFromField(String field) {
        List<String> tagList = new ArrayList<>();
        if (field.contains(BOLD_TAG)) {
            tagList.add(BOLD_TAG);
        }
        if (field.contains(LIGHT_GREY_BG_TAG)) {
            tagList.add(LIGHT_GREY_BG_TAG);
        }
        if (field.contains(HALF_BOLD_TAG)) {
            tagList.add(HALF_BOLD_TAG);
        }
        if (field.contains(DARK_GREY_BG_TAG)) {
            tagList.add(DARK_GREY_BG_TAG);
        }
        if (field.contains(NO_BORDER_TAG)) {
            tagList.add(NO_BORDER_TAG);
        }
        if (field.contains(LIGHT_GREY_LEFT_TAG)) {
            tagList.add(LIGHT_GREY_LEFT_TAG);
        }
        if (field.contains(LIME_BG_TAG)) {
            tagList.add(LIME_BG_TAG);
        }
        if (field.contains(RIGHT_ALIGN_TAG)) {
            tagList.add(RIGHT_ALIGN_TAG);
        }
        if (field.contains(MERGE_ROW_TAG)) {
            tagList.add(MERGE_ROW_TAG);
        }
        if (field.contains(ALIGN_CENTER_TAG)) {
            tagList.add(ALIGN_CENTER_TAG);
        }
        if (field.contains(MERGE_2_CELLS_TAG)) {
            tagList.add(MERGE_2_CELLS_TAG);
        }
        return tagList;
    }

}
