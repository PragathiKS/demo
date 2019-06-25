package com.tetrapak.customerhub.core.services.impl;

import com.tetrapak.customerhub.core.beans.financials.results.Document;
import com.tetrapak.customerhub.core.beans.financials.results.Params;
import com.tetrapak.customerhub.core.beans.financials.results.Record;
import com.tetrapak.customerhub.core.beans.financials.results.Results;
import com.tetrapak.customerhub.core.beans.financials.results.Summary;
import com.tetrapak.customerhub.core.beans.pdf.Column;
import com.tetrapak.customerhub.core.beans.pdf.Row;
import com.tetrapak.customerhub.core.beans.pdf.Table;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.models.FinancialStatementModel;
import com.tetrapak.customerhub.core.services.FinancialsResultsPDFService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.PDFUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.encoding.Encoding;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Impl class for Financials Results PDF Service
 */
@Component(immediate = true, service = FinancialsResultsPDFService.class)
public class FinancialsResultsPDFServiceImpl implements FinancialsResultsPDFService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FinancialsResultsPDFServiceImpl.class);

    private PDFont muliRegular;
    private PDFont muliBold;
    private static final int MARGIN = 20;
    private FinancialStatementModel financialStatementModel;
    private static final String CUHU_FINANCIAL_PREFIX = "cuhu.financials.";

    /**
     * @param request            SlingHttpServletRequest
     * @param response           SlingHttpServletResponse
     * @param resultsResponse    Results
     * @param paramRequest       RequestParams
     * @param financialStatement FinancialStatementModel
     */
    @Override
    public boolean generateFinancialsResultsPDF(SlingHttpServletRequest request, SlingHttpServletResponse response,
                                                Results resultsResponse, Params paramRequest, FinancialStatementModel financialStatement) {
        financialStatementModel = financialStatement;
        List<Document> documents = resultsResponse.getDocuments();
        String customerName = paramRequest.getCustomerData().getCustomerName();
        String startDate = paramRequest.getStartDate();
        String endDate = paramRequest.getEndDate();
        InputStream in1 = null;
        InputStream in2 = null;
        InputStream image1 = null;
        PDPageContentStream contentStream = null;
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        try {
            contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true,
                    true);
            in1 = getClass().getResourceAsStream("/fonts/muli-light-webfont.ttf");
            in2 = getClass().getResourceAsStream("/fonts/muli-bold-webfont.ttf");

            muliRegular = PDTrueTypeFont.load(document, in1, Encoding.getInstance(COSName.WIN_ANSI_ENCODING));
            muliBold = PDTrueTypeFont.load(document, in2, Encoding.getInstance(COSName.WIN_ANSI_ENCODING));

            image1 = getClass().getResourceAsStream("/images/tetra_pdf.png");

            BufferedImage bufferedImage1 = ImageIO.read(image1);

            PDImageXObject img1 = LosslessFactory.createFromImage(document, bufferedImage1);

            PDFUtil.drawImage(contentStream, img1, 20, 710, 180, 69);
            PDFUtil.writeContent(document, contentStream, MARGIN, 700, Color.DARK_GRAY,
                    getHeadLines(request, paramRequest));
            PDFUtil.drawTable(contentStream, createAccountServicetable(request, paramRequest), 570);
            PDFUtil.writeContent(document, contentStream, MARGIN, 510, Color.DARK_GRAY,
                    getstatementSummary(request, paramRequest));
            PDFUtil.drawTable(contentStream, createSummaryTable(request, resultsResponse.getSummary()), 490);
            PDFUtil.drawLine(contentStream, MARGIN, 500, 475, Color.DARK_GRAY, 0.01f);
            PDFUtil.drawLine(contentStream, MARGIN, 500, 450, Color.LIGHT_GRAY, 0.01f);

            PDFUtil.writeContent(document, contentStream, MARGIN, 430, Color.DARK_GRAY, getDocumentName(request));
            contentStream = printDeliveryDetails(request, document, contentStream, documents);
            return true;
        } catch (IOException e) {
            LOGGER.error("IOException ", e);
            return false;
        } finally {
            try {
                if (null != contentStream) {
                    contentStream.close();
                    if (paramRequest.getStartDate() != null && endDate != null) {
                        PDFUtil.writeOutput(response, document,
                                CustomerHubConstants.FINANCIALS + customerName + CustomerHubConstants.HYPHEN_STRING
                                        + startDate + " " + CustomerHubConstants.HYPHEN_STRING + " " + endDate);
                    } else if (paramRequest.getStartDate() != null) {
                        PDFUtil.writeOutput(response, document, CustomerHubConstants.FINANCIALS + customerName
                                + CustomerHubConstants.HYPHEN_STRING + startDate);
                    } else {
                        PDFUtil.writeOutput(response, document, CustomerHubConstants.FINANCIALS + customerName
                                + CustomerHubConstants.HYPHEN_STRING + endDate);
                    }
                }
                if (null != document) {
                    document.close();
                }
                if (null != contentStream) {
                    contentStream.close();
                }
                if (null != in1) {
                    in1.close();
                }
                if (null != in2) {
                    in2.close();
                }
                if (null != image1) {
                    image1.close();
                }
            } catch (IOException e) {
                LOGGER.error("IOException", e);
            }
        }
    }

    private List<Row> getDocumentName(SlingHttpServletRequest request) {
        List<Row> rows = new ArrayList<>();
        rows.add(new Row(
                GlobalUtil.getI18nValue(request, StringUtils.EMPTY, financialStatementModel.getDocumentHeadingI18n()),
                10, muliRegular, 12));
        return rows;
    }

    private List<Row> getHeadLines(SlingHttpServletRequest request, Params paramRequest) {
        List<Row> rows = new ArrayList<>();
        rows.add(new Row(
                GlobalUtil.getI18nValue(request, StringUtils.EMPTY, financialStatementModel.getStatementOfAccount())
                        + ":",
                10, muliRegular, 12));
        rows.add(new Row("", 20, muliRegular, 12));
        rows.add(new Row(GlobalUtil.getI18nValue(request, StringUtils.EMPTY, financialStatementModel.getAccountNumber())
                + ":" + " " + paramRequest.getCustomerData().getInfo().getAccountNo(), 10, muliBold, 11));
        rows.add(new Row("", 15, muliRegular, 12));
        rows.add(new Row(paramRequest.getCustomerData().getInfo().getName1(), 10, muliBold, 11));
        rows.add(new Row("", 15, muliRegular, 12));
        rows.add(new Row(paramRequest.getCustomerData().getInfo().getStreet(), 10, muliRegular, 11));
        rows.add(new Row("", 15, muliRegular, 12));
        rows.add(new Row(
                GlobalUtil.getI18nValue(request, StringUtils.EMPTY, financialStatementModel.getAccountService()) + ":",
                10, muliRegular, 11));
        rows.add(new Row("", 15, muliRegular, 12));
        return rows;
    }

    private List<Row> getstatementSummary(SlingHttpServletRequest request, Params paramRequest) {
        List<Row> rows = new ArrayList<>();
        if (paramRequest.getStartDate() != null && paramRequest.getEndDate() != null) {
            rows.add(new Row(
                    GlobalUtil.getI18nValue(request, StringUtils.EMPTY, financialStatementModel.getSummaryHeadingI18n())
                            + ":" + " " + " " + paramRequest.getEndDate() + " " + CustomerHubConstants.HYPHEN_STRING
                            + " " + paramRequest.getStartDate(),
                    10, muliRegular, 12));
        } else if (paramRequest.getEndDate() != null) {
            rows.add(new Row(
                    GlobalUtil.getI18nValue(request, StringUtils.EMPTY, financialStatementModel.getSummaryHeadingI18n())
                            + ":" + " " + " " + paramRequest.getEndDate(),
                    10, muliRegular, 12));
        } else {
            rows.add(new Row(
                    GlobalUtil.getI18nValue(request, StringUtils.EMPTY, financialStatementModel.getSummaryHeadingI18n())
                            + ":" + " " + " " + paramRequest.getStartDate(),
                    10, muliRegular, 12));
        }
        rows.add(new Row("", 20, muliRegular, 8));
        return rows;
    }

    private Table createAccountServicetable(SlingHttpServletRequest request, Params paramRequest) {
        List<Column> columns = new ArrayList<>();
        columns.add(new Column(paramRequest.getStatus().getDesc(), 90));
        if (paramRequest.getStartDate() != null && paramRequest.getEndDate() != null) {
            columns.add(new Column(paramRequest.getEndDate() + " " + CustomerHubConstants.HYPHEN_STRING + " "
                    + paramRequest.getStartDate(), 60));
        } else if (paramRequest.getStartDate() != null) {
            columns.add(new Column(paramRequest.getStartDate(), 60));
        } else {
            columns.add(new Column(paramRequest.getEndDate(), 60));
        }
        String[][] content = {
                {
                        CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request, StringUtils.EMPTY,
                                financialStatementModel.getSelectDocumentTypeLabel()),
                        paramRequest.getDocumentType().getDesc(),
                }, {
                CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request, StringUtils.EMPTY,
                        financialStatementModel.getPlaceholderForDocumentNumber()),
                paramRequest.getDocumentNumber()
        }
        };

        return PDFUtil.getTable(columns, content, 17, muliRegular, muliBold, 8, MARGIN);
    }

    private Table createSummaryTable(SlingHttpServletRequest request, List<Summary> summary) {
        List<Column> columns = new ArrayList<>();
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER
                + GlobalUtil.getI18nValue(request, CUHU_FINANCIAL_PREFIX, "currency"), 60));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER
                + GlobalUtil.getI18nValue(request, CUHU_FINANCIAL_PREFIX, "current"), 60));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER
                + GlobalUtil.getI18nValue(request, CUHU_FINANCIAL_PREFIX, "overdue"), 100));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER
                + GlobalUtil.getI18nValue(request, CUHU_FINANCIAL_PREFIX, "thirty"), 50));
        columns.add(new Column(
                CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request, CUHU_FINANCIAL_PREFIX, "sixty"),
                50));
        columns.add(new Column(
                CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request, CUHU_FINANCIAL_PREFIX, "ninty"),
                50));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER
                + GlobalUtil.getI18nValue(request, CUHU_FINANCIAL_PREFIX, "nintyPlus"), 70));
        columns.add(new Column(
                CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request, CUHU_FINANCIAL_PREFIX, "total"),
                10));

        String[][] content = new String[summary.size()][8];

        for (int i = 0; i < summary.size(); i++) {
            content[i][0] = summary.get(i).getCurrency();
            content[i][1] = summary.get(i).getCurrent();
            content[i][2] = summary.get(i).getOverdue();
            content[i][3] = summary.get(i).getThirty();
            content[i][4] = summary.get(i).getSixty();
            content[i][5] = summary.get(i).getNinty();
            content[i][6] = summary.get(i).getNintyPlus();
            content[i][7] = summary.get(i).getTotal();

        }

        return PDFUtil.getTable(columns, content, 20, muliRegular, muliBold, 8, MARGIN);
    }

    private PDPageContentStream printDeliveryDetails(SlingHttpServletRequest request, PDDocument document,
                                                     PDPageContentStream contentStream, List<Document> documents) throws IOException {
        int height = 400;
        int newPageHeight;

        for (Document documentDetail : documents) {
            if ((height > getNextTableHeight(documentDetail.getRecords()))) {
                PDFUtil.drawLine(contentStream, MARGIN, 570, height - 24, Color.LIGHT_GRAY, 0.01f);
                PDFUtil.drawLine(contentStream, MARGIN, 570, height - 8, Color.LIGHT_GRAY, 0.01f);

                PDFUtil.drawTable(contentStream, createDeliveryDetailTable(documentDetail), height + 5);

                height = PDFUtil.drawTable(contentStream, createProductTable(request, documentDetail.getRecords()),
                        height - 10);
                height -= 15;
            } else {
                newPageHeight = 730;
                int totalRows = documentDetail.getRecords().size();
                int rowsForCurrentPage = newPageHeight / 15;

                if (totalRows < rowsForCurrentPage && totalRows < 26) {
                    contentStream = PDFUtil.getNewPage(document, contentStream);
                    rowsForCurrentPage = totalRows;
                    height = newPageHeight;
                } else {
                    rowsForCurrentPage = height / 15;
                }
                PDFUtil.drawLine(contentStream, MARGIN, 570, height - 24, Color.LIGHT_GRAY, 0.01f);
                PDFUtil.drawLine(contentStream, MARGIN, 570, height - 6, Color.LIGHT_GRAY, 0.01f);

                PDFUtil.drawTable(contentStream, createDeliveryDetailTable(documentDetail), height + 5);
                height = PDFUtil.drawTable(contentStream,
                        createRecordTable(request, (documentDetail.getRecords()), 0, rowsForCurrentPage - 1),
                        height - 10);
                height -= 15;
                for (int start = rowsForCurrentPage; start < totalRows; start += 40) {
                    int end = start + 39;
                    end = getEnd(totalRows, end);

                    contentStream = PDFUtil.getNewPage(document, contentStream);

                    height = PDFUtil.drawTable(contentStream,
                            createRecordTable(request, documentDetail.getRecords(), start, end - 1), 730);
                    PDFUtil.drawLine(contentStream, MARGIN, 570, 735, Color.LIGHT_GRAY, 0.01f);
                    PDFUtil.drawLine(contentStream, MARGIN, 570, 718, Color.LIGHT_GRAY, 0.01f);
                }

            }
        }
        return contentStream;
    }

    private int getEnd(int totalRows, int end) {
        if (end > totalRows) {
            end = totalRows;
        }
        return end;
    }

    private Table createRecordTable(SlingHttpServletRequest request, List<Record> records, int start, int end) {
        List<Column> columns = new ArrayList<>();
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER
                + GlobalUtil.getI18nValue(request, CUHU_FINANCIAL_PREFIX, "documentNumber"), 60));
        columns.add(new Column(
                CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request, CUHU_FINANCIAL_PREFIX, "desc"),
                60));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER
                + GlobalUtil.getI18nValue(request, CUHU_FINANCIAL_PREFIX, "invoiceReference"), 80));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER
                + GlobalUtil.getI18nValue(request, CUHU_FINANCIAL_PREFIX, "poNumber"), 60));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER
                + GlobalUtil.getI18nValue(request, CUHU_FINANCIAL_PREFIX, "docDate"), 80));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER
                + GlobalUtil.getI18nValue(request, CUHU_FINANCIAL_PREFIX, "dueDate"), 60));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER
                + GlobalUtil.getI18nValue(request, CUHU_FINANCIAL_PREFIX, "currency"), 50));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER
                + GlobalUtil.getI18nValue(request, CUHU_FINANCIAL_PREFIX, "salesLocalData"), 60));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER
                + GlobalUtil.getI18nValue(request, CUHU_FINANCIAL_PREFIX, "orgAmount"), 10));

        String[][] content = new String[end - start + 1][9];

        for (int i = 0; i < end - start + 1; i++) {
            content[i][0] = records.get(i).getDocumentNumber();
            content[i][1] = records.get(i).getDocumentType();
            content[i][2] = records.get(i).getInvoiceReference();
            content[i][3] = records.get(i).getPoNumber();
            content[i][4] = records.get(i).getDocDate();
            content[i][5] = records.get(i).getDueDate();
            content[i][6] = records.get(i).getCurrency();
            content[i][7] = records.get(i).getSalesLocalData();
            content[i][8] = records.get(i).getOrgAmount();

        }
        return PDFUtil.getTable(columns, content, 14, muliRegular, muliBold, 8, MARGIN);
    }

    private int getNextTableHeight(List<Record> documentDetail) {
        return documentDetail.size() * 15 + 10;
    }

    private Table createDeliveryDetailTable(Document documentList) {
        // Total size of columns must not be greater than table width.
        List<Column> columns = new ArrayList<>();
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + documentList.getSalesOffice(), 510));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + documentList.getTotalAmount(), 40));
        String[][] content = new String[0][0];
        return PDFUtil.getTable(columns, content, 14, muliRegular, muliBold, 8, MARGIN);
    }

    private Table createProductTable(SlingHttpServletRequest request, List<Record> records) {
        return createRecordTable(request, records, 0, records.size() - 1);
    }

}
