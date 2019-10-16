package com.tetrapak.customerhub.core.services.impl;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tetrapak.customerhub.core.beans.financials.results.Document;
import com.tetrapak.customerhub.core.beans.financials.results.DocumentType;
import com.tetrapak.customerhub.core.beans.financials.results.Params;
import com.tetrapak.customerhub.core.beans.financials.results.Record;
import com.tetrapak.customerhub.core.beans.financials.results.Results;
import com.tetrapak.customerhub.core.beans.financials.results.Summary;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.models.FinancialStatementModel;
import com.tetrapak.customerhub.core.services.FinancialResultsPDFService;
import com.tetrapak.customerhub.core.services.UrlService;
import com.tetrapak.customerhub.core.utils.FontUtil;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.PDFUtil2;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Impl class for Financial Results PDF Service
 */
@Component(immediate = true, service = FinancialResultsPDFService.class)
public class FinancialResultsPDFServiceImpl implements FinancialResultsPDFService {

    @Reference
    private UrlService urlService;

    private static final Logger LOGGER = LoggerFactory.getLogger(FinancialResultsPDFServiceImpl.class);

    private Font languageSpecificFont;
    private Font languageSpecificFontBold;
    private FinancialStatementModel financialStatementModel;
    private static final String CUHU_FINANCIAL_PREFIX = "cuhu.financials.";
    private Map<String, String> statusTypeMap;
    private Map<String, String> documentTypeMap;
    private String language;

    private Map<String, String> getMapFromParams(List<DocumentType> docTypeList) {
        Map<String, String> map = new HashMap<>();
        for (DocumentType docType : docTypeList) {
            map.put(docType.getKey(), docType.getDesc());
        }
        return map;
    }

    /**
     * @param request            SlingHttpServletRequest
     * @param response           SlingHttpServletResponse
     * @param resultsResponse    Results
     * @param paramRequest       RequestParams
     * @param financialStatement FinancialStatementModel
     */
    @Override
    public boolean generateFinancialResultsPDF(SlingHttpServletRequest request, SlingHttpServletResponse response,
                                               Results resultsResponse, Params paramRequest, FinancialStatementModel financialStatement) {
        com.itextpdf.text.Document document1 = new com.itextpdf.text.Document(new RectangleReadOnly(620.0F, 842.0F));

        financialStatementModel = financialStatement;
        language = GlobalUtil.getLanguage(request);
        List<Document> documents = resultsResponse.getDocuments();
        String customerName = paramRequest.getCustomerData().getCustomerName();
        String startDate = paramRequest.getStartDate();
        String endDate = paramRequest.getEndDate();
        if (null != paramRequest) {
            statusTypeMap = getMapFromParams(paramRequest.getStatusList());
            documentTypeMap = getMapFromParams(paramRequest.getDocumentTypeList());
        }
        try {
            String fileName;
            if (paramRequest.getStartDate() != null && endDate != null) {
                fileName = CustomerHubConstants.FINANCIALS + customerName + CustomerHubConstants.HYPHEN_STRING + startDate
                        + " " + CustomerHubConstants.HYPHEN_STRING + " " + endDate;
            } else if (paramRequest.getStartDate() != null) {
                fileName = CustomerHubConstants.FINANCIALS + customerName + CustomerHubConstants.HYPHEN_STRING + startDate;
            } else {
                fileName = CustomerHubConstants.FINANCIALS + customerName + CustomerHubConstants.HYPHEN_STRING + endDate;
            }
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".pdf");
            PdfWriter writer = PdfWriter.getInstance(document1, response.getOutputStream());
            document1.open();

            final String FONT_RESOURCES = urlService.getFontsUrl();

            switch (language) {
                case "ja":
                    languageSpecificFont = FontUtil.getJpFont(FONT_RESOURCES + "NotoSerifCJKjp-Light.otf");
                    languageSpecificFontBold = FontUtil.getJpFontBold(FONT_RESOURCES + "NotoSerifCJKjp-Bold.otf");
                    break;
                case "zh":
                    languageSpecificFont = FontUtil.getScFont(FONT_RESOURCES + "NotoSerifCJKsc-Light.otf");
                    languageSpecificFontBold = FontUtil.getScFontBold(FONT_RESOURCES + "NotoSerifCJKsc-Bold.otf");
                    break;
                case "zh_TW":
                    languageSpecificFont = FontUtil.getTcFont(FONT_RESOURCES + "NotoSerifCJKtc-Light.otf");
                    languageSpecificFontBold = FontUtil.getTcFontBold(FONT_RESOURCES + "NotoSerifCJKtc-Bold.otf");
                    break;
                case "ko":
                    languageSpecificFont = FontUtil.getKoFont(FONT_RESOURCES + "NotoSerifCJKkr-Light.otf");
                    languageSpecificFontBold = FontUtil.getKoFontBold(FONT_RESOURCES + "NotoSerifCJKkr-Bold.otf");
                    break;
                default:
                    languageSpecificFont = FontUtil.getEnFont(FONT_RESOURCES + "NotoSerif-Light.ttf");
                    languageSpecificFontBold = FontUtil.getEnFontBold(FONT_RESOURCES + "NotoSerif-Bold.ttf");
                    break;
            }
            languageSpecificFont.setColor(BaseColor.DARK_GRAY);
            languageSpecificFontBold.setColor(BaseColor.DARK_GRAY);

            PDFUtil2.drawImage(document1, urlService.getImagesUrl() + "tetra_pdf.png", 180, 69);

            printHeadLines(request, paramRequest, document1);

            document1.add(new Paragraph("\n", languageSpecificFont));
            document1.add(createAccountServiceTable(request, paramRequest));
            printStatementSummary(request, paramRequest, document1);

            if(null != resultsResponse.getSummary() && !resultsResponse.getSummary().isEmpty()) {
                document1.add(new Paragraph("\n", languageSpecificFont));
                document1.add(createSummaryTable(writer, request, resultsResponse.getSummary()));
            }

            document1.add(new Paragraph("\n", languageSpecificFont));
            document1.add(createDocumentNameTable(request));

            document1.add(new Paragraph("\n", languageSpecificFont));
            printDeliveryDetails(writer, request, documents, document1);

            document1.close();
            return true;
        } catch (IOException | DocumentException e) {
            LOGGER.error("IOException ", e);
            return false;
        }
    }

    private void printHeadLines(SlingHttpServletRequest request, Params paramRequest, com.itextpdf.text.Document document1)
            throws DocumentException {
        languageSpecificFont.setSize(13);
        languageSpecificFontBold.setSize(13);
        Paragraph p = new Paragraph("\n" + GlobalUtil.getI18nValueForThisLanguage(
                request, StringUtils.EMPTY, financialStatementModel.getStatementOfAccount(), language), languageSpecificFont);
        document1.add(p);

        p = new Paragraph("\n" + GlobalUtil.getI18nValueForThisLanguage(
                request, StringUtils.EMPTY, financialStatementModel.getAccountNumber(), language)
                + ":" + " " + paramRequest.getCustomerData().getInfo().getAccountNo(), languageSpecificFontBold);
        document1.add(p);

        p = new Paragraph(paramRequest.getCustomerData().getInfo().getName1(), languageSpecificFontBold);
        document1.add(p);

        p = new Paragraph(paramRequest.getCustomerData().getInfo().getName2(), languageSpecificFontBold);
        document1.add(p);

        p = new Paragraph(paramRequest.getCustomerData().getInfo().getStreet(), languageSpecificFont);
        document1.add(p);

        p = new Paragraph(paramRequest.getCustomerData().getInfo().getCity()
                + ", " + paramRequest.getCustomerData().getInfo().getState()
                + ", " + paramRequest.getCustomerData().getInfo().getPostalcode()
                + ", " + paramRequest.getCustomerData().getInfo().getCountry(), languageSpecificFont);
        document1.add(p);

        p = new Paragraph(GlobalUtil.getI18nValueForThisLanguage(
                request, StringUtils.EMPTY, financialStatementModel.getAccountService(), language) + ":", languageSpecificFont);
        document1.add(p);
    }

    private PdfPTable createDocumentNameTable(SlingHttpServletRequest request) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(new float[]{160});
        table.setLockedWidth(true);
        table.setHorizontalAlignment(0);
        table.setSpacingBefore(0);
        final int HEIGHT = 15;

        PdfPCell cell = getPdfPCell(GlobalUtil.getI18nValueForThisLanguage(
                request, StringUtils.EMPTY, financialStatementModel.getDocumentHeadingI18n(), language), HEIGHT, languageSpecificFontBold);
        table.addCell(cell);
        return table;
    }

    private void printStatementSummary(SlingHttpServletRequest request, Params paramRequest, com.itextpdf.text.Document document1)
            throws DocumentException {
        languageSpecificFont.setSize(13);
        if (paramRequest.getStartDate() != null && paramRequest.getEndDate() != null) {
            Paragraph p = new Paragraph("\n" + GlobalUtil.getI18nValueForThisLanguage(
                    request, StringUtils.EMPTY, financialStatementModel.getSummaryHeadingI18n(), language)
                    + ":" + " " + " " + paramRequest.getStartDate() + " " + CustomerHubConstants.HYPHEN_STRING
                    + " " + paramRequest.getEndDate(), languageSpecificFont);
            document1.add(p);
        } else if (paramRequest.getEndDate() != null) {
            Paragraph p = new Paragraph("\n" + GlobalUtil.getI18nValueForThisLanguage(
                    request, StringUtils.EMPTY, financialStatementModel.getSummaryHeadingI18n(), language)
                    + ":" + " " + " " + paramRequest.getEndDate(), languageSpecificFont);
            document1.add(p);
        } else {
            Paragraph p = new Paragraph("\n" + GlobalUtil.getI18nValueForThisLanguage(
                    request, StringUtils.EMPTY, financialStatementModel.getSummaryHeadingI18n(), language)
                    + ":" + " " + " " + paramRequest.getStartDate(), languageSpecificFont);
            document1.add(p);
        }
    }

    private PdfPTable createAccountServiceTable(SlingHttpServletRequest request, Params paramRequest)
            throws DocumentException {
        languageSpecificFont.setSize(9);
        languageSpecificFontBold.setSize(9);
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(new float[]{90, 160});
        table.setLockedWidth(true);
        table.setHorizontalAlignment(0);
        table.setSpacingBefore(0);
        final int HEIGHT = 15;

        PdfPCell cell = getPdfPCell(statusTypeMap.get(paramRequest.getStatus().getKey()), HEIGHT, languageSpecificFont);
        table.addCell(cell);

        if (paramRequest.getStartDate() != null && paramRequest.getEndDate() != null) {
            cell = getPdfPCell(paramRequest.getStartDate() + " " + CustomerHubConstants.HYPHEN_STRING + " "
                    + paramRequest.getEndDate(), HEIGHT, languageSpecificFont);
            table.addCell(cell);

        } else if (paramRequest.getStartDate() != null) {
            cell = getPdfPCell(paramRequest.getStartDate(), HEIGHT, languageSpecificFont);
            table.addCell(cell);
        } else {
            cell = getPdfPCell(paramRequest.getEndDate(), HEIGHT, languageSpecificFont);
            table.addCell(cell);
        }

        cell = getPdfPCell(GlobalUtil.getI18nValueForThisLanguage(request, StringUtils.EMPTY,
                financialStatementModel.getSelectDocumentTypeLabel(), language), HEIGHT, languageSpecificFontBold);
        table.addCell(cell);

        cell = getPdfPCell(paramRequest.getDocumentType().getDesc(), HEIGHT, languageSpecificFont);
        table.addCell(cell);

        cell = getPdfPCell(GlobalUtil.getI18nValueForThisLanguage(request, StringUtils.EMPTY,
                financialStatementModel.getPlaceholderForDocumentNumber(), language), HEIGHT, languageSpecificFontBold);
        table.addCell(cell);

        cell = getPdfPCell(paramRequest.getDocumentNumber(), HEIGHT, languageSpecificFont);
        table.addCell(cell);
        return table;
    }

    private PdfPCell getPdfPCell(String text, int height, Font font) {
        PdfPCell cell;
        cell = new PdfPCell(new Phrase(text, font));
        cell.setFixedHeight(height);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private PdfPTable createSummaryTable(PdfWriter writer, SlingHttpServletRequest request, List<Summary> summaries)
            throws DocumentException {
        languageSpecificFont.setSize(9);
        languageSpecificFontBold.setSize(9);
        PdfPTable table = new PdfPTable(9);
        table.setTotalWidth(new float[]{60, 60, 60, 70, 50, 50, 50, 70, 80});
        table.setLockedWidth(true);
        table.setHorizontalAlignment(0);
        table.setSpacingBefore(0);
        final int HEIGHT = 15;

        PdfPCell cell = getPdfPCell(GlobalUtil.getI18nValueForThisLanguage(request, CUHU_FINANCIAL_PREFIX, "customer",
                language), HEIGHT, languageSpecificFontBold);
        table.addCell(cell);
        cell = getPdfPCell(GlobalUtil.getI18nValueForThisLanguage(request, CUHU_FINANCIAL_PREFIX, "currency",
                language), HEIGHT, languageSpecificFontBold);
        table.addCell(cell);
        cell = getPdfPCell(GlobalUtil.getI18nValueForThisLanguage(request, CUHU_FINANCIAL_PREFIX, "current",
                language), HEIGHT, languageSpecificFontBold);
        table.addCell(cell);
        cell = getPdfPCell(GlobalUtil.getI18nValueForThisLanguage(request, CUHU_FINANCIAL_PREFIX, "overdue",
                language), HEIGHT, languageSpecificFontBold);
        table.addCell(cell);
        cell = getPdfPCell(GlobalUtil.getI18nValueForThisLanguage(request, CUHU_FINANCIAL_PREFIX, "thirty",
                language), HEIGHT, languageSpecificFontBold);
        table.addCell(cell);
        cell = getPdfPCell(GlobalUtil.getI18nValueForThisLanguage(request, CUHU_FINANCIAL_PREFIX, "sixty",
                language), HEIGHT, languageSpecificFontBold);
        table.addCell(cell);
        cell = getPdfPCell(GlobalUtil.getI18nValueForThisLanguage(request, CUHU_FINANCIAL_PREFIX, "ninty",
                language), HEIGHT, languageSpecificFontBold);
        table.addCell(cell);
        cell = getPdfPCell(GlobalUtil.getI18nValueForThisLanguage(request, CUHU_FINANCIAL_PREFIX, "nintyPlus",
                language), HEIGHT, languageSpecificFontBold);
        table.addCell(cell);
        cell = getPdfPCell(GlobalUtil.getI18nValueForThisLanguage(request, CUHU_FINANCIAL_PREFIX, "total",
                language), HEIGHT, languageSpecificFontBold);
        table.addCell(cell);

        double height = (double) writer.getVerticalPosition(false) - (double) HEIGHT;
        double height2 = height - (double) HEIGHT;
        for (Summary summary : summaries) {
            PDFUtil2.drawLine(writer, 36, 580, (float) height);
            PDFUtil2.drawLine(writer, 36, 580, (float) height2);
            cell = getPdfPCell(summary.getCustomer(), HEIGHT, languageSpecificFont);
            table.addCell(cell);
            cell = getPdfPCell(summary.getCurrency(), HEIGHT, languageSpecificFont);
            table.addCell(cell);
            cell = getPdfPCell(summary.getCurrent(), HEIGHT, languageSpecificFont);
            table.addCell(cell);
            cell = getPdfPCell(summary.getOverdue(), HEIGHT, languageSpecificFont);
            table.addCell(cell);
            cell = getPdfPCell(summary.getThirty(), HEIGHT, languageSpecificFont);
            table.addCell(cell);
            cell = getPdfPCell(summary.getSixty(), HEIGHT, languageSpecificFont);
            table.addCell(cell);
            cell = getPdfPCell(summary.getNinty(), HEIGHT, languageSpecificFont);
            table.addCell(cell);
            cell = getPdfPCell(summary.getNintyPlus(), HEIGHT, languageSpecificFont);
            table.addCell(cell);
            cell = getPdfPCell(summary.getTotal(), HEIGHT, languageSpecificFont);
            table.addCell(cell);
        }

        return table;
    }

    private PdfPTable createDeliveryDetailTable(Document documentList) throws DocumentException {
        languageSpecificFont.setSize(9);
        languageSpecificFontBold.setSize(9);
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(new float[]{500, 60});
        table.setLockedWidth(true);
        table.setHorizontalAlignment(0);
        table.setSpacingBefore(0);
        final int HEIGHT = 15;

        PdfPCell cell = getPdfPCell(documentList.getSalesOffice(), HEIGHT, languageSpecificFontBold);
        table.addCell(cell);

        cell = getPdfPCell(documentList.getTotalAmount(), HEIGHT, languageSpecificFontBold);
        table.addCell(cell);

        return table;
    }

    private PdfPTable createRecordTable(PdfWriter writer, SlingHttpServletRequest request, List<Record> records) throws DocumentException {
        languageSpecificFont.setSize(9);
        languageSpecificFontBold.setSize(9);
        PdfPTable table = new PdfPTable(8);
        table.setTotalWidth(new float[]{60, 70, 80, 60, 80, 60, 50, 60});
        table.setLockedWidth(true);
        table.setHorizontalAlignment(0);
        table.setSpacingBefore(0);
        final int HEIGHT = 15;

        double height = (double) writer.getVerticalPosition(false);
        double height2 = height - (double) HEIGHT;
        PDFUtil2.drawLine(writer, 36, 580, (float) height);
        PDFUtil2.drawLine(writer, 36, 580, (float) height2);
        PdfPCell cell =
                getPdfPCell(GlobalUtil.getI18nValueForThisLanguage(request, CUHU_FINANCIAL_PREFIX, "documentNumber",
                        language), HEIGHT, languageSpecificFontBold);
        table.addCell(cell);
        cell = getPdfPCell(GlobalUtil.getI18nValueForThisLanguage(request, CUHU_FINANCIAL_PREFIX, "desc",
                language), HEIGHT, languageSpecificFontBold);
        table.addCell(cell);
        cell = getPdfPCell(GlobalUtil.getI18nValueForThisLanguage(request, CUHU_FINANCIAL_PREFIX, "invoiceReference",
                language), HEIGHT, languageSpecificFontBold);
        table.addCell(cell);
        cell = getPdfPCell(GlobalUtil.getI18nValueForThisLanguage(request, CUHU_FINANCIAL_PREFIX, "poNumber",
                language), HEIGHT, languageSpecificFontBold);
        table.addCell(cell);
        cell = getPdfPCell(GlobalUtil.getI18nValueForThisLanguage(request, CUHU_FINANCIAL_PREFIX, "docDate",
                language), HEIGHT, languageSpecificFontBold);
        table.addCell(cell);
        cell = getPdfPCell(GlobalUtil.getI18nValueForThisLanguage(request, CUHU_FINANCIAL_PREFIX, "dueDate",
                language), HEIGHT, languageSpecificFontBold);
        table.addCell(cell);
        cell = getPdfPCell(GlobalUtil.getI18nValueForThisLanguage(request, CUHU_FINANCIAL_PREFIX, "currency",
                language), HEIGHT, languageSpecificFontBold);
        table.addCell(cell);
        cell = getPdfPCell(GlobalUtil.getI18nValueForThisLanguage(request, CUHU_FINANCIAL_PREFIX, "orgAmount",
                language), HEIGHT, languageSpecificFontBold);
        table.addCell(cell);
        PDFUtil2.drawLine(writer, 36, 580, writer.getVerticalPosition(false));

        for (Record record : records) {
            cell = getPdfPCell(record.getDocumentNumber(), HEIGHT, languageSpecificFont);
            table.addCell(cell);
            cell = getPdfPCell(documentTypeMap.get(record.getDocumentType()), HEIGHT, languageSpecificFont);
            table.addCell(cell);
            cell = getPdfPCell(record.getInvoiceReference(), HEIGHT, languageSpecificFont);
            table.addCell(cell);
            cell = getPdfPCell(record.getPoNumber(), HEIGHT, languageSpecificFont);
            table.addCell(cell);
            cell = getPdfPCell(record.getDocDate(), HEIGHT, languageSpecificFont);
            table.addCell(cell);
            cell = getPdfPCell(record.getDueDate(), HEIGHT, languageSpecificFont);
            table.addCell(cell);
            cell = getPdfPCell(record.getCurrency(), HEIGHT, languageSpecificFont);
            table.addCell(cell);
            cell = getPdfPCell(record.getOrgAmount(), HEIGHT, languageSpecificFont);
            table.addCell(cell);
        }
        return table;
    }

    private void printDeliveryDetails(PdfWriter writer, SlingHttpServletRequest request,
                                      List<Document> documents, com.itextpdf.text.Document document1) throws DocumentException {
        for (Document documentDetail : documents) {
            document1.add(new Paragraph("\n", languageSpecificFont));
            document1.add(createDeliveryDetailTable(documentDetail));

            document1.add(new Paragraph("\n", languageSpecificFont));
            document1.add(createRecordTable(writer, request, documentDetail.getRecords()));
        }
    }
}
