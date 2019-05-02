
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
    private boolean newPage = false;
    
    /**
     * @param request  SlingHttpServletRequest 
     * @param response SlingHttpServletResponse
     * @param resultsResponse Results
     * @param paramRequest RequestParams
     * @param financialStatement FinancialStatementModel
     */
    @Override
    public boolean generateFinancialsResultsPDF(SlingHttpServletRequest request, SlingHttpServletResponse response,
            Results resultsResponse, Params paramRequest, FinancialStatementModel financialStatement) {
        financialStatementModel = financialStatement;
        List<Document> documents = resultsResponse.getDocuments();
        String customerName = paramRequest.getCustomerData().getInfo().getTitle();
        String startDate = paramRequest.getStartDate();
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
            
            PDFUtil.writeContent(document, contentStream, MARGIN, 420, Color.DARK_GRAY, getDocumentName(request));
            contentStream = printDeliveryDetails(request, document, contentStream, documents);                       
            return true;
        } catch (IOException e) {
            LOGGER.error("IOException {}", e);
            return false;
        } finally {
            try {
                if(null != contentStream) {
                    contentStream.close(); 
                    PDFUtil.writeOutput(response, document, "Financials-" + customerName+ "-" +startDate);
                }
                if(null != document) {
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
                LOGGER.error("IOException {}", e);
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
                + ":" + paramRequest.getCustomerData().getInfo().getAcountNo(), 10, muliBold, 11));
        rows.add(new Row("", 15, muliRegular, 12));
        rows.add(new Row(paramRequest.getCustomerData().getInfo().getTitle(), 10, muliBold, 11));
        rows.add(new Row("", 15, muliRegular, 12));
        rows.add(new Row(paramRequest.getCustomerData().getInfo().getAddress(), 10, muliRegular, 11));
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
                            + ":" + " " + paramRequest.getEndDate() + "-" + paramRequest.getStartDate(),
                    10, muliRegular, 12));
        } else if (paramRequest.getEndDate() != null) {
            rows.add(new Row(
                    GlobalUtil.getI18nValue(request, StringUtils.EMPTY, financialStatementModel.getSummaryHeadingI18n())
                            + ":" + " " + paramRequest.getEndDate(),
                    10, muliRegular, 12));
        } else {
            rows.add(new Row(
                    GlobalUtil.getI18nValue(request, StringUtils.EMPTY, financialStatementModel.getSummaryHeadingI18n())
                            + ":" + " " + paramRequest.getStartDate(),
                    10, muliRegular, 12));
        }
        rows.add(new Row("", 20, muliRegular, 8));
        return rows;
    }
    
    private Table createAccountServicetable(SlingHttpServletRequest request, Params paramRequest) {
        List<Column> columns = new ArrayList<>();
        columns.add(new Column(paramRequest.getStatus().getDesc(), 90));
        if (paramRequest.getStartDate() != null && paramRequest.getEndDate() != null) {
            columns.add(new Column(paramRequest.getEndDate() + "-" + paramRequest.getStartDate(), 60));
        }
        if (paramRequest.getStartDate() != null) {
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
        columns.add(new Column(
                CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request, "cuhu.financials.", "currency"),
                60));
        columns.add(new Column(
                CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request, "cuhu.financials.", "current"),
                60));
        columns.add(new Column(
                CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request, "cuhu.financials.", "overdue"),
                100));
        columns.add(new Column(
                CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request, "cuhu.financials.", "thirty"),
                50));
        columns.add(new Column(
                CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request, "cuhu.financials.", "sixty"),
                50));
        columns.add(new Column(
                CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request, "cuhu.financials.", "ninty"),
                50));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER
                + GlobalUtil.getI18nValue(request, "cuhu.financials.", "nintyPlus"), 70));
        columns.add(new Column(
                CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request, "cuhu.financials.", "total"),
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
        int height = 480;
        int newPageheight = 720;
        int pageSize1 = 0;
        int pageSize2 = 0;
        int spaceIndex = 35;
        int endIndex = 30;
        
        for (Document documentDetail : documents) {
            int nextTableHeight = getNextTableHeight(documentDetail.getRecords());
            int size = documentDetail.getRecords().size() * 10;
            if (newPage) {
                if (size == height || size > height) {
                    pageSize1 = (((newPageheight - height) / 10) - 2);
                    pageSize2 = documentDetail.getRecords().size() - pageSize1;
                } else {
                    pageSize1 = documentDetail.getRecords().size();
                }
            } else {
                if (size / 10 > 28) {
                    pageSize1 = 28;
                    pageSize2 = documentDetail.getRecords().size() - pageSize1;
                    height = height - 50;
                } else {
                    height = height - nextTableHeight;
                    pageSize1 = documentDetail.getRecords().size();
                }
            }
            if (newPage == true) {
                contentStream = getNewPageTable(request, contentStream, height, documentDetail, pageSize1);                
                height = ((height - 42) - pageSize1 * 10) - spaceIndex;
            } else if (newPage == false) {
                PDFUtil.drawLine(contentStream, MARGIN, 560, height - 66, Color.LIGHT_GRAY, 0.01f);
                PDFUtil.drawLine(contentStream, MARGIN, 560, height - 78, Color.LIGHT_GRAY, 0.01f);
                
                PDFUtil.drawTable(contentStream, createDeliveryDetailTable(documentDetail), height - 54);
                
                PDFUtil.drawTable(contentStream, createRecordTable(request, documentDetail.getRecords(), pageSize1),
                        height - 68);
                
            }
            if (documentDetail.getRecords().size() > pageSize1 && pageSize2 > 0) {
                
                contentStream = getNewPageRecordsTable(request, document, contentStream, newPageheight, documentDetail,
                        pageSize2);
                height = (newPageheight - pageSize2 * 10) - endIndex;
            }
            
        }
        return contentStream;
    }
    
    private Table createRecordTable(SlingHttpServletRequest request, List<Record> records, int pageSize1) {
        List<Column> columns = new ArrayList<>();
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER
                + GlobalUtil.getI18nValue(request, "cuhu.financials.", "documentNumber"), 60));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER
                + GlobalUtil.getI18nValue(request, "cuhu.financials.", "salesLocalData"), 60));
        columns.add(new Column(
                CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request, "cuhu.financials.", "desc"),
                60));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER
                + GlobalUtil.getI18nValue(request, "cuhu.financials.", "invoiceReference"), 80));
        columns.add(new Column(
                CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request, "cuhu.financials.", "poNumber"),
                60));
        columns.add(new Column(
                CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request, "cuhu.financials.", "docDate"),
                80));
        columns.add(new Column(
                CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request, "cuhu.financials.", "dueDate"),
                60));
        columns.add(new Column(
                CustomerHubConstants.BOLD_IDENTIFIER + GlobalUtil.getI18nValue(request, "cuhu.financials.", "currency"),
                40));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER
                + GlobalUtil.getI18nValue(request, "cuhu.financials.", "orgAmount"), 10));
        
        String[][] content = new String[pageSize1][9];
        
        for (int i = 0; i < pageSize1; i++) {
            content[i][0] = records.get(i).getDocumentNumber();
            content[i][1] = records.get(i).getSalesLocalData();
            content[i][2] = records.get(i).getDesc();
            content[i][3] = records.get(i).getInvoiceReference();
            content[i][4] = records.get(i).getPoNumber();
            content[i][5] = records.get(i).getDocDate();
            content[i][6] = records.get(i).getDueDate();
            content[i][7] = records.get(i).getCurrency();
            content[i][8] = records.get(i).getOrgAmount();
            
        }
        return PDFUtil.getTable(columns, content, 12, muliRegular, muliBold, 8, MARGIN);
    }
    
    private int getNextTableHeight(List<Record> documentDetail) {
        return documentDetail.size() * 10 + 30;
    }
    
    private Table createDeliveryDetailTable(Document documentList) {
        // Total size of columns must not be greater than table width.
        List<Column> columns = new ArrayList<>();
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + documentList.getSalesOffice(), 500));
        columns.add(new Column(CustomerHubConstants.BOLD_IDENTIFIER + documentList.getTotalAmount(), 40));
        String[][] content = new String[0][0];
        return PDFUtil.getTable(columns, content, 14, muliRegular, muliBold, 8, MARGIN);
    }
    
    private PDPageContentStream getNewPageTable(SlingHttpServletRequest request,
            PDPageContentStream contentStream, int height, Document documentDetail, int pageSize1) throws IOException {
        PDFUtil.drawLine(contentStream, MARGIN, 560, height - 30, Color.LIGHT_GRAY, 0.01f);
        PDFUtil.drawLine(contentStream, MARGIN, 560, height - 40, Color.LIGHT_GRAY, 0.01f);
        PDFUtil.drawTable(contentStream, createDeliveryDetailTable(documentDetail), height - 20);
        PDFUtil.drawTable(contentStream, createRecordTable(request, documentDetail.getRecords(), pageSize1),
                height - 30);
        return contentStream;
    }
    
    private PDPageContentStream getNewPageRecordsTable(SlingHttpServletRequest request, PDDocument document,
            PDPageContentStream contentStream, int newPageheight, Document documentDetail, int pageSize2)
            throws IOException {
        PDPage page = new PDPage();
        document.addPage(page);
        contentStream.close();
        contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);
        PDFUtil.drawTable(contentStream, createRecordTable(request, documentDetail.getRecords(), pageSize2),
                newPageheight);
        PDFUtil.drawLine(contentStream, MARGIN, 560, newPageheight - 12, Color.LIGHT_GRAY, 0.01f);
        newPage = true;
        return contentStream;
    }
    
}
