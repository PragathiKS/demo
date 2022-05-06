package com.tetrapak.customerhub.core.services.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.customerhub.core.beans.equipmentlist.Equipments;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.EquipmentListExcelService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;

@Component(immediate = true, service = EquipmentListExcelService.class)
public class EquipmentListExcelServiceImpl implements EquipmentListExcelService {

	private SlingHttpServletRequest request;
	private String language;
	private static final String CSV_FILE_NAME = "List of Equipment.csv";
	private static final String I18N_PREFIX = "cuhu.myequipment.csvheader.";
	private static final String POSITION_DEFAULT_VALUE = "0000";
	private static final String POSITION_FORMATTER = "%04d";
	private static final String DATE_PATTERN = "uuuu MM dd";
	// This is a mapping between i18n keys of CSV column headings to corresponding
	// Equipments bean fields
	static final List<String> csvHeaderMapping = new ArrayList<>();
	// This determines the sequence of the columns
	static {
		csvHeaderMapping.add(CustomerHubConstants.COUNTRY_CODE_EQUIPMENTS_API);
		csvHeaderMapping.add(CustomerHubConstants.CUSTOMER_NAME);
		csvHeaderMapping.add(CustomerHubConstants.LOCATION);
		csvHeaderMapping.add(CustomerHubConstants.FUNCTIONAL_LOCATION);
		csvHeaderMapping.add(CustomerHubConstants.POSITION);
		csvHeaderMapping.add(CustomerHubConstants.EQUIPMENT_TYPE);
		csvHeaderMapping.add(CustomerHubConstants.EQUIPMENT_DESC_CSVHEADER);
		csvHeaderMapping.add(CustomerHubConstants.MACHINE_SYSTEM);
		csvHeaderMapping.add(CustomerHubConstants.SERIAL_NUMBER);
		csvHeaderMapping.add(CustomerHubConstants.PERMANENT_VOLUME_CONV);
		csvHeaderMapping.add(CustomerHubConstants.MATERIAL);
		csvHeaderMapping.add(CustomerHubConstants.EQUIPMENT_STATUS);
		csvHeaderMapping.add(CustomerHubConstants.CONSTRUCTION_YEAR);
		csvHeaderMapping.add(CustomerHubConstants.CUSTOMER_WARRANTY_START_DATE);
		csvHeaderMapping.add(CustomerHubConstants.CUSTOMER_WARRANTY_END_DATE);
		csvHeaderMapping.add(CustomerHubConstants.MANUFACTURER_SERIAL_NUMBER);
		csvHeaderMapping.add(CustomerHubConstants.MANUFACTURER_MODEL_NUMBER);
		csvHeaderMapping.add(CustomerHubConstants.MANUFACTURER);
		csvHeaderMapping.add(CustomerHubConstants.MANUFACTURER_COUNTRY);
		csvHeaderMapping.add(CustomerHubConstants.CONFIRMATION_DATE);
		csvHeaderMapping.add(CustomerHubConstants.VALID_FROM_DATE);
		csvHeaderMapping.add(CustomerHubConstants.ID);
		csvHeaderMapping.add(CustomerHubConstants.EQUIPMENT_CATEGORY);
		csvHeaderMapping.add(CustomerHubConstants.EQUIPMENT_TYPE_DESCRIPTION);
		csvHeaderMapping.add(CustomerHubConstants.EQUIPMENT_MACHINESYSTEMDESC_CSVHEADER);
		csvHeaderMapping.add(CustomerHubConstants.MATERIAL_DESCRIPTION);
		csvHeaderMapping.add(CustomerHubConstants.EQUIPMENT_STATUS_DESCRIPTION);
		csvHeaderMapping.add(CustomerHubConstants.EQUIPMENT_CATEGORY_DESCRIPTION);
		csvHeaderMapping.add(CustomerHubConstants.LINE_NAME);
		csvHeaderMapping.add(CustomerHubConstants.COUNTRY_NAME);
		csvHeaderMapping.add(CustomerHubConstants.CUSTOMER_NUMBER);
		csvHeaderMapping.add(CustomerHubConstants.SITE_NAME);
		csvHeaderMapping.add(CustomerHubConstants.SITE_DESCRIPTION);
		csvHeaderMapping.add(CustomerHubConstants.BUSINESS_TYPE);
		csvHeaderMapping.add(CustomerHubConstants.SUPERIOR_EQUIPMENT_SERIAL_NUMBER);
		csvHeaderMapping.add(CustomerHubConstants.SUPERIOR_EQUIPMENT_NAME);
		csvHeaderMapping.add(CustomerHubConstants.SUPERIOR_EQUIPMENT);
	}
	private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentListExcelServiceImpl.class);

	/**
	 * This method returns the list of equipments in CSV format
	 *
	 * @param equipments
	 * @param response
	 * @return list of equipment in CSV format
	 * @throws IOException
	 */
	public boolean generateCSV(List<Equipments> equipments, SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws IOException {

		if (Objects.nonNull(equipments)) {
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
			List<Equipments> sortedEquipments = sortEquipmentRecordsinCSV(equipments);
			for (Equipments equipment : sortedEquipments) {
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
	 * This method sorts the CSV rows.
	 *
	 * @param List<Equipments> Unsorted Equipment list
	 * @return List<Equipments> Sorted Equipment list
	 */
	private List<Equipments> sortEquipmentRecordsinCSV(List<Equipments> unsortedEquipments) {

		Function<Equipments, String> getCustomerName = t -> {
			String name = t.getCustomerName();
			return (name != null && (name.isEmpty()
					|| name.equals(CustomerHubConstants.QUOTE_ESCAPED + CustomerHubConstants.QUOTE_ESCAPED))) ? null
							: name;
		};

		Comparator<Equipments> compareByLocation = Comparator.comparing(Equipments::getLocation);

		Comparator<Equipments> compareByFunctionalLocation = Comparator.comparing(Equipments::getFunctionalLocation);

		Comparator<Equipments> compareByPosition = Comparator.comparing(Equipments::getPosition);

		Comparator<Equipments> cumulativeComparison = Comparator
				.comparing(getCustomerName, Comparator.nullsLast(Comparator.naturalOrder()))
				.thenComparing(compareByLocation).thenComparing(compareByFunctionalLocation)
				.thenComparing(compareByPosition);

		return unsortedEquipments.stream().sorted(cumulativeComparison).collect(Collectors.toList());
	}

	/**
	 * This method converts an Equipment object to comma separated String
	 * representation. The order of adding properties to list determines the order
	 * of columns in CSV file.
	 *
	 * @param Equipments equipment
	 * @return String
	 */
	private String convertToCSVRow(Equipments equipment) {
		List<String> equipmentPropertiesList = new ArrayList<>();
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getCountryCode()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getCustomerName()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getLocation()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getFunctionalLocation()));
		equipmentPropertiesList.add(tidyCSVOutput(formatPosition(equipment.getPosition())));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getEquipmentType()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getEquipmentNameSub()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getMachineSystemCode()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getSerialNumber()));
		equipmentPropertiesList
				.add(tidyCSVOutput(formatPermanentVolumeConversion(equipment.getPermanentVolumeConversion())));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getMaterial()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getEquipmentStatus()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getConstructionYear()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getCustomerWarrantyStartDate()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getCustomerWarrantyEndDate()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getManufacturerSerialNumber()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getManufacturerModelNumber()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getManufacturer()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getManufacturerCountry()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getEofsConfirmationDate()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getEofsValidFromDate()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getId()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getEquipmentCategory()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getEquipmentTypeDesc()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getMachineSystem()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getMaterialDesc()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getEquipmentStatusDescription()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getEquipmentCategoryDesc()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getFunctionalLocationDesc()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getCountryName()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getCustomerNumber()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getSiteName()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getSiteDesc()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getBusinessType()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getSuperiorEquipmentSerialNumber()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getSuperiorEquipmentName()));
		equipmentPropertiesList.add(tidyCSVOutput(equipment.getSuperiorEquipment()));
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
