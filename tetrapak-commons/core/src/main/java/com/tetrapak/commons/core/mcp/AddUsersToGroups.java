package com.tetrapak.commons.core.mcp;

import com.adobe.acs.commons.fam.ActionManager;
import com.adobe.acs.commons.mcp.ProcessDefinition;
import com.adobe.acs.commons.mcp.ProcessInstance;
import com.adobe.acs.commons.mcp.form.FileUploadComponent;
import com.adobe.acs.commons.mcp.form.FormField;
import com.adobe.acs.commons.mcp.model.GenericBlobReport;
import com.adobe.acs.commons.mcp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.ResourceResolver;

import javax.jcr.RepositoryException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;

/**
 * This MCP Process will accept an excel sheet with one to one mapping
 * between users and groups, removes user from existing groups and
 * adds to the group as mentioned in input excel.
 */
public class AddUsersToGroups extends ProcessDefinition implements Serializable {

    @FormField(
            name = "Excel File",
            description = "Provide the .xlsx file that defines the user and group mapping",
            component = FileUploadComponent.class,
            options = {"mimeTypes=application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "required"}
    )
    public transient InputStream excelFile = null;


    private final transient GenericBlobReport report = new GenericBlobReport();

    private final transient ArrayList<EnumMap<ReportColumns, Object>> reportRows = new ArrayList<>();

    private enum ReportColumns {
        USER_NAME,
        GROUP_NAME,
        MAPPING_STATUS
    }

    public enum ReportRowSatus {
        MAPPING_SUCCESSFUL,
        MAPPING_FAILED
    }


    @Override
    public void buildProcess(ProcessInstance processInstance, ResourceResolver resourceResolver)
            throws LoginException, RepositoryException {
        report.setName("User_Group_Mapping_Status");
        processInstance.getInfo().setDescription("Adding users to groups based on the input");
        processInstance.defineAction("Add Users to Groups", resourceResolver, this::addUsersToGroups);


    }

    private void addUsersToGroups(ActionManager actionManager) throws Exception {
        actionManager.withResolver(rr -> {
            final XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
            final XSSFSheet sheet = workbook.getSheetAt(0);
            final Iterator<Row> rows = sheet.rowIterator();
            boolean isFirstRow = true;
            while(rows.hasNext()) {
                if(isFirstRow){
                    rows.next();
                    isFirstRow = false;
                    continue;
                }
                final Row row = rows.next();
                final Iterator<Cell> cells = row.cellIterator();
                Cell firstCell = cells.next();
                Cell secondCell = cells.next();
                final String userName = StringUtils.trimToNull(firstCell.getStringCellValue());
                final String groupName = StringUtils.trimToNull(secondCell.getStringCellValue());
                UserManager userManager = rr.adaptTo(UserManager.class);
                if(userManager==null){
                    return;
                }
                Authorizable user = userManager.getAuthorizable(userName);
                Authorizable group = userManager.getAuthorizable(groupName);
                if(user!=null && !user.isGroup() && group!=null && group.isGroup()){
                    Iterator<Group> existingGroups = user.memberOf();
                    while (existingGroups.hasNext()){
                        Group existingGroup = existingGroups.next();
                        if(existingGroup.getID().equals("everyone")){
                            continue;
                        }
                        existingGroup.removeMember(user);
                    }
                    Group groupToBeMapped = (Group) group;
                    groupToBeMapped.addMember(user);
                    rr.commit();
                    record(ReportRowSatus.MAPPING_SUCCESSFUL,userName,groupName);
                }else{
                    record(ReportRowSatus.MAPPING_FAILED,userName,groupName);
                }
            }
        });
    }

    private void record(ReportRowSatus status, String userName, String groupName) {
        final EnumMap<ReportColumns, Object> row = new EnumMap<>(ReportColumns.class);

        row.put(ReportColumns.MAPPING_STATUS, StringUtil.getFriendlyName(status.name()));
        row.put(ReportColumns.USER_NAME, userName);
        row.put(ReportColumns.GROUP_NAME, groupName);

        reportRows.add(row);
    }


    @Override
    public void storeReport(ProcessInstance processInstance, ResourceResolver resourceResolver)
            throws RepositoryException, PersistenceException {
        report.setRows(reportRows, ReportColumns.class);
        report.persist(resourceResolver, processInstance.getPath() + "/jcr:content/report");

    }

    @Override
    public void init() throws RepositoryException {

    }
}
