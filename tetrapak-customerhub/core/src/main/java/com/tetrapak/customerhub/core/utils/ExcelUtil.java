package com.tetrapak.customerhub.core.utils;

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

import org.apache.commons.lang3.StringUtils;
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

import com.tetrapak.customerhub.core.beans.excel.ExcelFileData;
import com.tetrapak.customerhub.core.exceptions.ExcelReportRuntimeException;

/**
 * 
 * This utility is used to generate and download an excel file based on the 2D String array provided as a parameter
 * 
 * @author dhitiwar
 * @author swalamba
 *
 */
public class ExcelUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);
	private static final Pattern REMOVE_TAGS = Pattern.compile("<.+?>");

	public static final String MERGE_ROW_TAG = "<mergerow>";
	public static final String REGULAR_STYLE_TAG = "<regularStyle>";
	public static final String LIGHT_GREY_BG_TAG = "<lightGreyBG>";
	public static final String BOLD_TAG = "<bold>";
	public static final String HALF_BOLD_TAG = "<halfBold>";
	public static final String DARK_GREY_BG_TAG = "<darkGreyBG>";
	public static final String ALIGN_CENTER_TAG = "<aligncenter>";
	public static final String HALF_BOLD_IDENTIFIER = ":";
	public static final String NEW_LINE_DETECTOR = "\n";
	private static final String DEFAULT_FONT_NAME = "Microsoft Sans Serif";
	private static final short DEFAULT_FONT_HEIGHT = (short) 9;
	private static final String LIGHT_GREY_BG_CELL_STYLE = "lightGreyBackgroudStyle";
	private static final String REGULAR_CELL_STYLE = "regularStyle";
	private static final String EMPTY_CELL_STYLE = "emptyCellStyle";
	private static final String REGULAR_CENTER_CELL_STYLE = "regularCenterStyle";
	private static final String DARK_GREY_BG_CELL_STYLE = "darkGreyBackgroudStyle";
	private static final String CONTENT_TYPE = "application/x-ms-excel";
	private static final String CONTENT_DISPOSITION = "Content-Disposition";
	private static final String FILE_EXTENSION = ".xlsx";
	private static final String RESP_HEADER_DATA = "attachment; filename=";

	private ExcelUtil() {
		// Disallowed to create Object out of class
	}

	/**
	 * 
	 * @param workBook
	 * @param sheetName
	 * @return Sheet
	 * 
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
			rowNum++;
		}
		return row;
	}

	/**
	 * @param sheet
	 */
	private static void resizeCellToFitContent(Sheet sheet) {
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
	 */
	public static void generateExcelReport(SlingHttpServletResponse response, ExcelFileData excelReportData) {
		if (Objects.nonNull(excelReportData)) {
			try {
				XSSFWorkbook workBook = new XSSFWorkbook();
				Sheet sheet = getExcelSheet(workBook, excelReportData.getExcelSheetName());
				if (Objects.nonNull(sheet) && Objects.nonNull(excelReportData.getData())) {
					sheet.setDisplayGridlines(false);
					prepareReportData(workBook, sheet, excelReportData.getData());
					setMarginsToSheet(sheet);
					resizeCellToFitContent(sheet);
				}
				downloadExcel(response, workBook, excelReportData);
			} catch (IOException e) {
				LOGGER.error("\nA run-time exception occured while generating excel report.");
				throw new ExcelReportRuntimeException("Something went wrong while generating Excel Report", e);
			}
		} else {
			LOGGER.warn("ExcelReportData is null!!");
		}

	}

	private static void setMarginsToSheet(Sheet sheet) {
		int rowCount = sheet.getLastRowNum();
		int columnCount = sheet.getRow(0).getLastCellNum();

		CellRangeAddress region = new CellRangeAddress(0, rowCount, 0, columnCount);
		RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);

	}

	/**
	 * @param workBook
	 * @param sheet
	 * @param row
	 * @param String[][] reportData
	 * 
	 */
	private static void prepareReportData(Workbook workBook, Sheet sheet, String[][] reportData) {
		int rowCount = 0;

		for (String[] data : reportData) {
			Row row = getRow(sheet, rowCount);
			int columnCount = 0;
			if (Objects.nonNull(row)) {
				for (String field : data) {
					Cell cell = row.createCell(columnCount);
					columnCount++;
					setRowHeight(row, field, sheet.getDefaultRowHeightInPoints());
					processFieldToCellValue(sheet, cell, field, getCellStyleMap(workBook), reportData[0].length,
							workBook.createFont());
				}
			}
			++rowCount;
		}
	}

	/**
	 * @param workBook
	 * @param sheet
	 * @param cell
	 * @param field
	 * @param row
	 * @param cellStyles
	 * @param length
	 */
	private static void processFieldToCellValue(Sheet sheet, Cell cell, String field, Map<String, CellStyle> cellStyles,
			int length, Font customFont) {

		if (StringUtils.isNotBlank(field)) {

			List<String> tags = getTagsFromField(field);
			XSSFRichTextString richText = applyCustomStyles(field, tags, customFont);

			if (tags.contains(MERGE_ROW_TAG)) {
				sheet.addMergedRegion(
						new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex(), cell.getColumnIndex(), length));
			}

			applyCellStyling(cell, tags, cellStyles);
			cell.setCellValue(richText);
		} else {
			cell.setCellStyle(cellStyles.get(EMPTY_CELL_STYLE));
		}
	}

	/**
	 * @param cell
	 * @param tags
	 * @param cellStyles
	 */
	private static void applyCellStyling(Cell cell, List<String> tags, Map<String, CellStyle> cellStyles) {
		if (tags.contains(LIGHT_GREY_BG_TAG)) {
			cell.setCellStyle(cellStyles.get(LIGHT_GREY_BG_CELL_STYLE));
		} else if (tags.contains(ALIGN_CENTER_TAG)) {
			cell.setCellStyle(cellStyles.get(REGULAR_CENTER_CELL_STYLE));
		} else if (tags.contains(DARK_GREY_BG_TAG)) {
			cell.setCellStyle(cellStyles.get(DARK_GREY_BG_CELL_STYLE));
		} else {
			cell.setCellStyle(cellStyles.get(REGULAR_CELL_STYLE));
		}

	}

	/**
	 * 
	 * Setting the row height if the new line is present
	 * 
	 * @param row
	 * @param field
	 * @param defaultHeight
	 */
	private static void setRowHeight(Row row, String field, double defaultHeight) {
		if (!StringUtils.isBlank(field) && field.contains(NEW_LINE_DETECTOR)) {
			row.setHeightInPoints(3 * (float)defaultHeight);
		}
	}

	/**
	 * Get list of CellStyles in form a map
	 * 
	 * @param workBook
	 * @return getCellStyleMap
	 */
	private static Map<String, CellStyle> getCellStyleMap(Workbook workBook) {
		Map<String, CellStyle> cellStyles = new HashMap<>();

		CellStyle borderStyle = workBook.createCellStyle();
		borderStyle.setBorderLeft(BorderStyle.THIN);
		borderStyle.setBorderRight(BorderStyle.THIN);
		borderStyle.setBorderTop(BorderStyle.THIN);
		borderStyle.setBorderBottom(BorderStyle.THIN);
		borderStyle.setWrapText(true);
		cellStyles.put(REGULAR_CELL_STYLE, borderStyle);

		CellStyle emptyCellStyle = workBook.createCellStyle();
		emptyCellStyle.setBorderLeft(BorderStyle.NONE);
		emptyCellStyle.setBorderRight(BorderStyle.NONE);
		emptyCellStyle.setBorderTop(BorderStyle.NONE);
		emptyCellStyle.setBorderBottom(BorderStyle.NONE);
		emptyCellStyle.setWrapText(true);
		cellStyles.put(EMPTY_CELL_STYLE, emptyCellStyle);

		CellStyle regularCenterStyle = workBook.createCellStyle();
		regularCenterStyle.setBorderLeft(BorderStyle.THIN);
		regularCenterStyle.setBorderRight(BorderStyle.THIN);
		regularCenterStyle.setBorderTop(BorderStyle.THIN);
		regularCenterStyle.setBorderBottom(BorderStyle.THIN);
		regularCenterStyle.setAlignment(HorizontalAlignment.CENTER);
		regularCenterStyle.setWrapText(true);
		cellStyles.put(REGULAR_CENTER_CELL_STYLE, regularCenterStyle);

		CellStyle lightGreyBackgroudStyle = workBook.createCellStyle();
		lightGreyBackgroudStyle.setBorderLeft(BorderStyle.THIN);
		lightGreyBackgroudStyle.setBorderRight(BorderStyle.THIN);
		lightGreyBackgroudStyle.setBorderTop(BorderStyle.THIN);
		lightGreyBackgroudStyle.setBorderBottom(BorderStyle.THIN);
		lightGreyBackgroudStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
		lightGreyBackgroudStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		lightGreyBackgroudStyle.setAlignment(HorizontalAlignment.CENTER);
		lightGreyBackgroudStyle.setWrapText(true);
		cellStyles.put(LIGHT_GREY_BG_CELL_STYLE, lightGreyBackgroudStyle);

		CellStyle darkGreyBackgroudStyle = workBook.createCellStyle();
		darkGreyBackgroudStyle.setBorderLeft(BorderStyle.THIN);
		darkGreyBackgroudStyle.setBorderRight(BorderStyle.THIN);
		darkGreyBackgroudStyle.setBorderTop(BorderStyle.THIN);
		darkGreyBackgroudStyle.setBorderBottom(BorderStyle.THIN);
		darkGreyBackgroudStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.index);
		darkGreyBackgroudStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		darkGreyBackgroudStyle.setAlignment(HorizontalAlignment.CENTER);
		darkGreyBackgroudStyle.setWrapText(true);
		cellStyles.put(DARK_GREY_BG_CELL_STYLE, darkGreyBackgroudStyle);

		return cellStyles;
	}

	/**
	 * @param response
	 * @param workBook
	 * @param ExcelFileData excelReportData
	 * 
	 */
	private static void downloadExcel(SlingHttpServletResponse response, Workbook workBook,
			ExcelFileData excelReportData) throws IOException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		workBook.write(out);
		byte[] docBytes = out.toByteArray();
		ByteArrayInputStream in = new ByteArrayInputStream(docBytes);
		response.setContentType(CONTENT_TYPE);
		response.setHeader(CONTENT_DISPOSITION, RESP_HEADER_DATA + excelReportData.getFileName() + FILE_EXTENSION);
		int read;
		OutputStream os = response.getOutputStream();
		while ((read = in.read(docBytes)) != -1) {
			os.write(docBytes, 0, read);
		}
		os.flush();
		os.close();
		closeWorkbook(workBook);

	}

	/**
	 * Remove all the custom tags
	 * 
	 * @param string
	 * @return
	 */
	private static String stripTags(String string) {
		if (StringUtils.isBlank(string)) {
			return string;
		}
		Matcher m = REMOVE_TAGS.matcher(string);
		return m.replaceAll(StringUtils.EMPTY);
	}

	/**
	 * 
	 * Apply custom font styling to the richtextString based on the list of tags
	 * 
	 * @param field    : raw data to be set in the cell
	 * @param tags     : list of tags based on which the manipulation would be done
	 * @param workBook : the workbook object to create a customfont
	 * @return manipulated richTextString from the string data to be put in the cell
	 */
	private static XSSFRichTextString applyCustomStyles(String field, List<String> tags, Font customFont) {
		field = stripTags(field);
		XSSFRichTextString richTextString = new XSSFRichTextString(field);
		customFont.setFontName(DEFAULT_FONT_NAME);
		customFont.setFontHeightInPoints(DEFAULT_FONT_HEIGHT);
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
	 * 
	 * get the tags set in the field and return as a list
	 * 
	 * @param field
	 * @return
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
		if (field.contains(MERGE_ROW_TAG)) {
			tagList.add(MERGE_ROW_TAG);
		}
		if (field.contains(ALIGN_CENTER_TAG)) {
			tagList.add(ALIGN_CENTER_TAG);
		}
		return tagList;
	}

}