import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.util.*;
import org.apache.poi.ss.usermodel.*;
import java.io.*;
import com.adobe.granite.asset.api.*;
import javax.jcr.Node
import groovy.transform.Field


/**
 set DRY_RUN = false to actually make and save changes in crx.
 **/
@Field final DRY_RUN = true

/**
 *  IMPORTANT!
 *  Specify the correct dictionary path as per use case.
 *  Specify the correct DAM path for excel file
 */
@Field final PATHS = [
        TARGET_DICTIONARY_PATH      : "/apps/customerhub/i18n",
        I18N_TRANSLATION_EXCEL_PATH_: "/content/dam/tetrapak/customerhub/english/My Tetra Pak Content translations (2).xlsx"
]

/**
 *  Modify this to reflect the correct position for translation sheet in excel file
 */
@Field final int sheetPosition = 1;

/**
 *  Modify this to reflect the correct position for english language column in excel sheet
 */
@Field final int enLanguageColumnPosition = 1;


@Field final PROPS = [
        NAME: [
                ORIGINAL_RENDITION : "original",
                SLING_MESSAGE      : "sling:message",
                JCR_LANGUAGE       : "jcr:language",
                SLING_MESSAGE_ENTRY: "sling:MessageEntry",
                SLING_FOLDER       : "sling:Folder"
        ]
]

languageCodes = getLanguageCodeSet(PATHS.TARGET_DICTIONARY_PATH);

Resource excelFile = resourceResolver.getResource(PATHS.I18N_TRANSLATION_EXCEL_PATH_);
Asset excelFileAsset = excelFile.adaptTo(Asset.class);
Rendition rendition = excelFileAsset.getRendition(PROPS.NAME.ORIGINAL_RENDITION);

InputStream inputStream = rendition.getStream();
Workbook workbook = WorkbookFactory.create(inputStream);
Sheet sheet = workbook.getSheetAt(sheetPosition);
Iterator rowIterator = sheet.rowIterator();
rowIterator.next()
Row row;
def rowCounter = 0;
def rowsData = []
def headerList = []
while (rowIterator.hasNext()) {
    row = rowIterator.next()
    def rowIndex = row.getRowNum()
    def colIndex;
    def rowData = [];
    String i18nKey = "";
    for (Cell cell : row) {
        colIndex = cell.getColumnIndex();
        def cellValue = cell.getRichStringCellValue().getString();
        if (colIndex == 0) {
            i18nKey = cellValue;
        }
        if (cellValue == null || cellValue.equals("")) {
            continue;
        }
        rowData[colIndex] = cellValue;

        if (rowCounter == 0) {
            // extract column headers
            headerList.add(extractLanguageCode(cellValue));
        } else if (colIndex > 0) {
            def languageCode = headerList[colIndex];
            if (languageCode == null || languageCode.equals("")) {
                continue;
            }
            if (colIndex == enLanguageColumnPosition) {
                languageCode = "en";
            }
            // Create parent language folder if it does not exist
            if (resourceResolver.getResource(PATHS.TARGET_DICTIONARY_PATH + "/" + languageCode) == null) {
                Resource i18nParentFolder = resourceResolver.getResource(PATHS.TARGET_DICTIONARY_PATH);
                Node i18nParentFolderNode = i18nParentFolder.adaptTo(javax.jcr.Node.class);
                Node i18nNode = i18nParentFolderNode.addNode(languageCode, PROPS.NAME.SLING_FOLDER);
                i18nNode.setProperty(PROPS.NAME.JCR_LANGUAGE, languageCode);
                println "Create language folder :" + languageCode;
                if (!DRY_RUN) {
                    session.save();
                }
            }

            // Create i18n node
            Resource languageDictionary = resourceResolver.getResource(PATHS.TARGET_DICTIONARY_PATH + "/" + languageCode);
            Node languageDictionaryNode = languageDictionary.adaptTo(javax.jcr.Node.class);
            if (!languageDictionaryNode.hasNode(i18nKey)) {

                Node i18nNode = languageDictionaryNode.addNode(i18nKey, PROPS.NAME.SLING_MESSAGE_ENTRY);
                i18nNode.setProperty(PROPS.NAME.SLING_MESSAGE, cellValue);
                println "ADDED node for key :" + i18nKey + " under language folder : " + languageCode;
                if (!DRY_RUN) {
                    session.save();
                }
            } else {
                println "The i18n key : " + i18nKey + " for language : " + languageCode + " already exists";
            }
        }
    }
    rowCounter++;
}

def extractLanguageCode(heading) {
    String languageCode = "";
    String[] headingSplitBySpace = heading.split(" ");
    if (headingSplitBySpace.length == 3) {
        languageCode = headingSplitBySpace[1];
    } else {
        headingSplitBySpace.each { headingToken ->
            if (languageCodes.contains(headingToken)) {
                languageCode = headingToken;
            }
        }
    }
    languageCode
}

def getLanguageCodeSet(dictionaryPath) {
    Set<String> languageCodeSet = [];
    Resource parentDictionary = resourceResolver.getResource(dictionaryPath);
    parentDictionary.getChildren().each {
        folder ->
            languageCodeSet.add(folder.getName());
    }
    languageCodeSet
}
