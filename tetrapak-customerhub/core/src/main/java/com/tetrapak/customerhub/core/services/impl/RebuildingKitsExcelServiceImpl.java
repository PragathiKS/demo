package com.tetrapak.customerhub.core.services.impl;

import com.tetrapak.customerhub.core.beans.rebuildingkits.RebuildingKits;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.RebuildingKitsExcelService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component(immediate = true, service = RebuildingKitsExcelServiceImpl.class)
public class RebuildingKitsExcelServiceImpl implements RebuildingKitsExcelService {

	private SlingHttpServletRequest request;
	private String language;
	private static final String CSV_FILE_NAME = "List of Rebuilding Kits.csv";
	private static final String I18N_PREFIX = "cuhu.myequipment.rk.csvheader.";
	private static final String POSITION_DEFAULT_VALUE = "0000";
	private static final String POSITION_FORMATTER = "%04d";
	private static final String DATE_PATTERN = "uuuu MM dd";
	// This is a mapping between i18n keys of CSV column headings to corresponding
	// RebuildingKits bean fields
	static final List<String> csvHeaderMapping = new ArrayList<>();
	// This determines the sequence of the columns
	static {
		csvHeaderMapping.add(CustomerHubConstants.COUNTRY_CODE_EQUIPMENTS_API);
		csvHeaderMapping.add(CustomerHubConstants.CUSTOMER_NAME);
		csvHeaderMapping.add(CustomerHubConstants.LOCATION);
		csvHeaderMapping.add(CustomerHubConstants.POSITION);
		csvHeaderMapping.add(CustomerHubConstants.EQUIPMENT_TYPE);
		csvHeaderMapping.add(CustomerHubConstants.EQUIPMENT_DESC_CSVHEADER);
		csvHeaderMapping.add(CustomerHubConstants.MACHINE_SYSTEM);
		csvHeaderMapping.add(CustomerHubConstants.SERIAL_NUMBER);
		csvHeaderMapping.add(CustomerHubConstants.PERMANENT_VOLUME_CONV);
		csvHeaderMapping.add(CustomerHubConstants.EQUIPMENT_STATUS);
		csvHeaderMapping.add(CustomerHubConstants.EQUIPMENT_TYPE_DESCRIPTION);
		csvHeaderMapping.add(CustomerHubConstants.EQUIPMENT_MACHINESYSTEMDESC_CSVHEADER);
		csvHeaderMapping.add(CustomerHubConstants.EQUIPMENT_STATUS_DESCRIPTION);
		csvHeaderMapping.add(CustomerHubConstants.LINE_NAME);
		csvHeaderMapping.add(CustomerHubConstants.COUNTRY_NAME);
		csvHeaderMapping.add(CustomerHubConstants.CUSTOMER_NUMBER);
		csvHeaderMapping.add(CustomerHubConstants.RK_NUMBER);
		csvHeaderMapping.add(CustomerHubConstants.RK_DESC);
		csvHeaderMapping.add(CustomerHubConstants.IMPL_DATE);
		csvHeaderMapping.add(CustomerHubConstants.IMPL_STATUS);
		csvHeaderMapping.add(CustomerHubConstants.IMPL_STATUS_DATE);
		csvHeaderMapping.add(CustomerHubConstants.REBUILDING_KITS_STATUS);
		csvHeaderMapping.add(CustomerHubConstants.RK_RELEASE_DATE_FIRST);
		csvHeaderMapping.add(CustomerHubConstants.RELEASE_DATE);
		csvHeaderMapping.add(CustomerHubConstants.RK_MECHANICAL_SKILLS);
		csvHeaderMapping.add(CustomerHubConstants.RK_AUTOMATIONS_KILLS);
		csvHeaderMapping.add(CustomerHubConstants.RK_ELECTRICAL_SKILLS);
		csvHeaderMapping.add(CustomerHubConstants.RK_MACHINE_SYSTEM_DESC);
	}
	private static final Logger LOGGER = LoggerFactory.getLogger(RebuildingKitsExcelServiceImpl.class);

	/**
	 * This method returns the list of rebuildingKits in CSV format
	 *
	 * @param equipments
	 * @param response
	 * @return list of equipment in CSV format
	 * @throws IOException
	 */
	public boolean generateCSV(List<RebuildingKits> rbk, SlingHttpServletRequest request,
							   SlingHttpServletResponse response) throws IOException {

		if (Objects.nonNull(rbk)) {
			this.language = GlobalUtil.getLanguage(request);
			this.request = request;
			response.setContentType(CustomerHubConstants.TEXT_CSV);
			response.setHeader(CustomerHubConstants.CONTENT_DISPOSITION,
					CustomerHubConstants.ATTACHMENT_FILENAME + CustomerHubConstants.EQUALS_CHAR + 
					getCurrentDate() + CustomerHubConstants.SPACE + CSV_FILE_NAME);
			ServletOutputStream csvFileOutputStream = response.getOutputStream();
			StringBuilder csvFileContent = new StringBuilder();
			String[][] headerRowArray = getColumnHeaderArray();
			String[] headerRow = headerRowArray[0];
			csvFileContent
					.append(CustomerHubConstants.CSV_BYTE_ORDER_MARK);
			for (String columnHeading : headerRow) {
				csvFileContent.append(columnHeading).append(CustomerHubConstants.TAB);
				LOGGER.debug("Equipment list CSV File Column heading : {}", columnHeading);
			}
			csvFileContent.append(CustomerHubConstants.NEWLINE);
			for (RebuildingKits equipment : rbk) {
				csvFileContent.append(convertToCSVRow(equipment));
			}
			csvFileOutputStream.write(csvFileContent.toString().getBytes(StandardCharsets.UTF_16LE));
			csvFileOutputStream.flush();
			csvFileOutputStream.close();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Method to generate CSV file header array
	 * 
	 * @return String array of header row
	 */
	private String[][] getColumnHeaderArray() {
		String[][] columnNames = new String[1][csvHeaderMapping.size()];
		int headerColumnIndex = 0;
		Iterator<String> iterator = csvHeaderMapping.iterator();
		while (iterator.hasNext()) {
			columnNames[0][headerColumnIndex] = getI18nVal(iterator.next());
			headerColumnIndex++;
		}
		return columnNames;
	}

	/**
	 * getI18nVal
	 *
	 * @param value value
	 * @return string
	 */
	private String getI18nVal(String value) {
		if (null != request && !StringUtils.isBlank(value)) {
			value = GlobalUtil.getI18nValueForThisLanguage(request, I18N_PREFIX, value, language);
		}
		return value;
	}

	/**
	 * This method converts an Equipment object to comma separated String
	 * representation. The order of adding properties to list determines the order
	 * of columns in CSV file.
	 *
	 * @param RebuildingKits rbk
	 * @return String
	 */
	private String convertToCSVRow(RebuildingKits rbk) {
		List<String> equipmentPropertiesList = new ArrayList<>();
		equipmentPropertiesList.add(tidyCSVOutput(rbk.getCountryCode()));
		equipmentPropertiesList.add(tidyCSVOutput(rbk.getCustomerName()));
		equipmentPropertiesList.add(tidyCSVOutput(rbk.getLocation()));
		equipmentPropertiesList.add(tidyCSVOutput(formatPosition(rbk.getPosition())));
		equipmentPropertiesList.add(tidyCSVOutput(rbk.getEquipmentType()));
		equipmentPropertiesList.add(tidyCSVOutput(rbk.getSerialNumber()));
		equipmentPropertiesList
				.add(tidyCSVOutput(formatPermanentVolumeConversion(rbk.getPermanentVolumeConversion())));
		equipmentPropertiesList.add(tidyCSVOutput(rbk.getEquipmentStatus()));
		equipmentPropertiesList.add(tidyCSVOutput(rbk.getEquipmentTypeDesc()));
		equipmentPropertiesList.add(tidyCSVOutput(rbk.getMachineSystem()));
		equipmentPropertiesList.add(tidyCSVOutput(rbk.getEquipmentStatusDescription()));
		equipmentPropertiesList.add(tidyCSVOutput(rbk.getCountryName()));
		equipmentPropertiesList.add(tidyCSVOutput(rbk.getCustomerNumber()));
		return equipmentPropertiesList.stream().collect(Collectors.joining(CustomerHubConstants.TAB))
				.concat(CustomerHubConstants.NEWLINE);
	}

	private String tidyCSVOutput(String field) {
		if (Objects.isNull(field)) {
			return org.apache.commons.lang.StringUtils.EMPTY;
		}
		return CustomerHubConstants.QUOTE_ESCAPED + field + CustomerHubConstants.QUOTE_ESCAPED;
	}

	private String formatPosition(String position) {
		if (StringUtils.isNotEmpty(position)) {
			return CustomerHubConstants.TAB + formatNumber(Integer.parseInt(position), POSITION_FORMATTER);
		} else {
			return CustomerHubConstants.TAB + POSITION_DEFAULT_VALUE;
		}
	}

	private String formatNumber(int number, String format) {
		return String.format(format, number);
	}

	private String formatPermanentVolumeConversion(String pvc) {
		if (pvc.equals(CustomerHubConstants.FALSE)) {
			return StringUtils.EMPTY;
		} else {
			return CustomerHubConstants.X_UNAVAILABILITY_SYMBOL;
		}
	}
	
	private String getCurrentDate() {
		DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern(DATE_PATTERN);
		return dtfDate.format(LocalDate.now());
	}
}
