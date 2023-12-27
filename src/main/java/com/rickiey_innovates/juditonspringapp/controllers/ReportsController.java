package com.rickiey_innovates.juditonspringapp.controllers;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.core.layout.LayoutManager;
import ar.com.fdvs.dj.domain.CustomExpression;
import ar.com.fdvs.dj.domain.DJCalculation;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.GroupBuilder;
import ar.com.fdvs.dj.domain.constants.*;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.entities.DJGroup;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RGBColor;
import com.rickiey_innovates.juditonspringapp.repositories.ClosingBalanceRepository;
import com.rickiey_innovates.juditonspringapp.repositories.UserRepository;
import com.rickiey_innovates.juditonspringapp.DbConnector;
import com.rickiey_innovates.juditonspringapp.models.Farm;
import com.rickiey_innovates.juditonspringapp.models.ClosingBalance;
import com.rickiey_innovates.juditonspringapp.models.accountsmodel;
import com.rickiey_innovates.juditonspringapp.repositories.ActivityRepository;
import jakarta.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.List;

@Controller
@RequestMapping("reports")
public class ReportsController {
    @Value("${dynamic.template.report.path}")
    public String dynamic_report_template;

    @Value("${portrait.template.report.path}")
    public String portrait_report_template;

    @Value("${vouchers.path}")
    private String vouchersPath;
    Map<String, Object> param = new HashMap<>();
    String orgname = "";
    String projname = "";
    String programe = "";
    String partnertype = "";
    String country = "";
    String bugcurrency = "";
    String echrate = "";
    String title = "";

    int cash = 0;
    int bank = 0;
    String dated = "";
    String username = "";
    String apiKey = "";
    String useid = "";
    String sms_account = "";

    @Autowired
    UserRepository userRepository;

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    ClosingBalanceRepository closingBalanceRepository;

    private Long userId() {
        return LoginController.getLoggedInUserId();
    }

    private Farm farm() {
        return userRepository.findById(userId()).orElse(null).getFarm();
    }

    private JasperPrint generateReport(String query, String jrxmlPath) throws JRException, SQLException {
        System.out.println(query);
        Connection conn = null;
        JasperPrint jp = null;

        DynamicReport dr;

        param.put("orgname", orgname);
        param.put("projname", projname);
        param.put("programme", programe);
        param.put("partnertype", partnertype);
        param.put("country", country);
        param.put("bugcurrency", bugcurrency);
        param.put("echrate", echrate);
        param.put("title", title);
        param.put("dated", dated);
        param.put("schid", "" + 3);

        try {
            conn = DbConnector.getConnection();
            InputStream ip = getClass().getResourceAsStream(jrxmlPath);
            JasperDesign jd = JRXmlLoader.load(ip);
            JRDesignQuery newQuery = new JRDesignQuery();
            newQuery.setText(query);
            jd.setQuery(newQuery);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            jp = JasperFillManager.fillReport(jr, param, conn);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        return jp;
    }

    @RequestMapping(path = {"/printVoucher"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<byte[]> printVoucher(@RequestParam(name = "id") String id) throws IOException, JRException, SQLException {
        String uploadDir = "static/" + farm().getUploadPath() + "/pdf";
        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        String jrxmlPath = vouchersPath;
/*

        String sourceFileName = "/home/rickiey/IdeaProjects/ChurchSpringApp/src/main/resources/reports/companyofficials.jrxml";
        JasperCompileManager.compileReportToFile(sourceFileName);

*/

        String sql = "SELECT `Account #`,`Account`,`Payee Name`,`Date`,`Details`,`Particulars`,`Qty`,`Voucher #`, \n" +
                "`Rate`,`Pv id`,MAX(`Withholding Tax`) AS 'Withholding Tax',MAX(`VAT`) AS VAT,MAX(`Professional Fees`) AS 'Professional Fees' FROM (SELECT `Account #`,\n" +
                " `Account`,`Payee Name`,d.`Date`,`Details`,`Particulars`,`Qty`,`Voucher #`, \n" +
                "`Rate`,`Pv id`,case when `type` = 'Withholding Tax' then `Amount` ELSE 0   end AS `Withholding Tax`, \n" +
                "case when `type` = 'Vat Tax' then `Amount` ELSE 0  end AS `VAT`,case when `type` = 'Professional Fees'  \n" +
                "then `Amount` ELSE 0 end AS `Professional Fees`,id FROM (SELECT `Account #`,`Account`, \n" +
                "`Payee Name`,paymentvouchers.`Date`,`Details`,`Particulars`,`Qty`,`Voucher #`, \n" +
                "`Rate`,`Pv id`  FROM paymentvoucherdetails  \n" +
                " INNER JOIN paymentvouchers ON paymentvoucherdetails.`pv#`  =paymentvouchers.`Pv id`  \n" +
                " INNER JOIN activities ON paymentvouchers.activity = `Account id`  \n" +
                " WHERE `Pv id` ='" + id + "' GROUP BY paymentvoucherdetails.detailid)d \n" +
                " left JOIN tax ON tax.`pv#` = `Pv id`)F \n" +
                " GROUP BY particulars";


        JasperPrint jp = generateReport(sql, jrxmlPath);

        JasperExportManager.exportReportToPdfFile(jp, uploadDir + File.separator +
                RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Disposition", "attachment; filename=" +
                RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = Base64.getEncoder().encode(inFileBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }


    @RequestMapping(path = {"/printReceipt"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<byte[]> printReceipt(@RequestParam(name = "transId") String transId) throws IOException, JRException, SQLException {
        String uploadDir = "static/" + farm().getUploadPath() + "/pdf";
        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        String jrxmlPath = "/reports/Otherreceipt.jrxml";


        String sql = "SELECT IFNULL(first_name, `Payee/Payer`)            as name,\n" +
                "       `Ref #`,\n" +
                "       CONCAT(b.`Account Name`, ' ', b.`Account #`) AS ACCOUNT,\n" +
                "       a.Description,\n" +
                "       SUM(credit)                                  AS amount,\n" +
                "       number_to_words(SUM(credit))                 as amountInWords,\n" +
                "       date\n" +
                "from accounttransactions a\n" +
                "         LEFT JOIN members r ON `Payee/Payer` = `id`\n" +
                "         INNER JOIN bankaccounts b ON b.`Acc id` = a.bank\n" +
                "where `Transaction id` = '" + transId + "';";


        JasperPrint jp = generateReport(sql, jrxmlPath);

        JasperExportManager.exportReportToPdfFile(jp, uploadDir + File.separator +
                RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Disposition", "attachment; filename=" +
                RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = Base64.getEncoder().encode(inFileBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    @RequestMapping(path = {"/printProfile"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<byte[]> printProfile(@RequestParam(name = "id") String id) throws IOException, JRException, SQLException {
        String uploadDir = "static/" + farm().getUploadPath() + "/pdf";
        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        String jrxmlPath = "/reports/profile.jrxml";


        String sql = "SELECT z.zone,\n" +
                "       g.`group`              as member_group,\n" +
                "       cs.service,\n" +
                "       m.id,\n" +
                "       m.first_name,\n" +
                "       m.middle_name,\n" +
                "       m.last_name,\n" +
                "       m.email,\n" +
                "       m.phone,\n" +
                "       m.address,\n" +
                "       m.dob,\n" +
                "       m.baptism_status,\n" +
                "       m.confirmation_status,\n" +
                "       m.marital_status,\n" +
                "       m.occupation,\n" +
                "       m.gender,\n" +
                "       m.image,\n" +
                "       zz.zone                as s_zone,\n" +
                "       a.`group`              as s_member_group,\n" +
                "       css.service            as s_service,\n" +
                "       mm.id                  as s_id,\n" +
                "       mm.first_name          as s_first,\n" +
                "       mm.middle_name         as s_middle,\n" +
                "       mm.last_name           as s_last,\n" +
                "       mm.email               as s_email,\n" +
                "       mm.phone               as s_phone,\n" +
                "       mm.address             as s_address,\n" +
                "       mm.dob                 as s_dob,\n" +
                "       mm.baptism_status      as s_baptism_status,\n" +
                "       mm.confirmation_status as s_confirmation_status,\n" +
                "       mm.marital_status      as s_marital_status,\n" +
                "       mm.occupation          as s_occupation,\n" +
                "       mm.gender              as s_gender,\n" +
                "       mm.image               as s_image\n" +
                "FROM members m\n" +
                "         inner join membergroups g on m.member_group = g.groupid\n" +
                "         inner join farm.zones z on m.zone = z.zoneid\n" +
                "         inner join farm.church_service cs on m.preferred_service = cs.id\n" +
                "         left join farm.spouse s on m.id = s.member_id\n" +
                "         left join members mm on mm.id = s.spouse_id\n" +
                "         left join zones zz on zz.zoneid = mm.zone\n" +
                "         left join church_service css on css.id = mm.preferred_service\n" +
                "         left join membergroups a on m.id = a.groupid\n" +
                "where m.id = " + id + " ";


        JasperPrint jp = generateReport(sql, jrxmlPath);

        JasperExportManager.exportReportToPdfFile(jp, uploadDir + File.separator +
                RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Disposition", "attachment; filename=" +
                RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = Base64.getEncoder().encode(inFileBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }


    @RequestMapping(path = {"/members/generate"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<byte[]> printMembers(@RequestParam(name = "category") String category, @RequestParam(name = "choice") String choice, HttpServletRequest request) throws IOException, JRException, SQLException {
        System.out.println(request.getRequestURL());
        System.out.println(category + " " + choice);
        JasperPrint jp = null;
        String uploadDir = "static/" + "thika" + "/pdf";
        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        if (category.equals("all")) {
            String jrxmlPath = "/reports/members.jrxml";


            String query = "select m.id, m.member_number,\n" +
                    "       concat(first_name, ' ', middle_name, ' ', last_name) as NAME,\n" +
                    "       z.zone,\n" +
                    "       marital_status                                       as maritalstatus,\n" +
                    "       g.`group`                                         as 'group',\n" +
                    "       middle_name                                          as marriage,\n" +
                    "       last_name                                            as position,\n" +
                    "       first_name                                           as parent,\n" +
                    "       phone\n" +
                    "from members m\n" +
                    "         inner join zones z on m.zone = z.zoneid\n" +
                    "         inner join membergroups g on m.member_group = g.groupid where m.farm = " + 3 + ";";

            jp = generateReport(query, jrxmlPath);
        } else if (category.equals("officials")) {
            String query = "select u.username NAME, 'Farm Accountant' AS Position\n" +
                    "FROM companyofficials c inner join users u on c.accountant = u.id\n" +
                    "where u.farm = " + 3 + "\n" +
                    "UNION ALL\n" +
                    "select u.username NAME, 'Senior Pastor' AS Position\n" +
                    "FROM companyofficials c inner join users u on c.senior_pastor = u.id\n" +
                    "where u.farm = " + 3 + "\n" +
                    "UNION ALL\n" +
                    "select u.username NAME, 'Secondary signatory' AS Position\n" +
                    "FROM companyofficials c inner join users u on c.second_signatory = u.id\n" +
                    "where u.farm = " + 3 + "\n" +
                    "UNION ALL\n" +
                    "select u.username NAME, 'Farm Treasurer' AS Position\n" +
                    "FROM companyofficials c inner join users u on c.treasurer = u.id\n" +
                    "where u.farm = " + 3 + ";";

            jp = generateReport(query, "/reports/officials.jrxml");

        } else if (category.equals("group")) {
            String query = "select m.id, m.member_number,\n" +
                    "       concat(first_name, ' ', middle_name, ' ', last_name) as NAME,\n" +
                    "       z.zone,\n" +
                    "       marital_status                                       as maritalstatus,\n" +
                    "       g.`group`                                            as 'group',\n" +
                    "       middle_name                                          as marriage,\n" +
                    "       last_name                                            as position,\n" +
                    "       first_name                                           as parent,\n" +
                    "       phone\n" +
                    "from members m\n" +
                    "         inner join zones z on m.zone = z.zoneid\n" +
                    "         inner join membergroups g on m.member_group = g.groupid\n" +
                    "where m.farm = " + 3 + "\n" +
                    "  and member_group = '" + choice + "';";

            jp = generateReport(query, "/reports/members.jrxml");

        } else if (category.equals("zone")) {
            String query = "select m.id, m.member_number,\n" +
                    "       concat(first_name, ' ', middle_name, ' ', last_name) as NAME,\n" +
                    "       z.zone,\n" +
                    "       marital_status                                       as maritalstatus,\n" +
                    "       g.`group`                                            as 'group',\n" +
                    "       middle_name                                          as marriage,\n" +
                    "       last_name                                            as position,\n" +
                    "       first_name                                           as parent,\n" +
                    "       phone\n" +
                    "from members m\n" +
                    "         inner join zones z on m.zone = z.zoneid\n" +
                    "         inner join membergroups g on m.member_group = g.groupid\n" +
                    "where m.farm = " + 3 + "\n" +
                    "  and z.zoneid = '" + choice + "';";

            jp = generateReport(query, "/reports/members.jrxml");

        } else if (category.equals("department")) {
            String query = "select m.id,\n" +
                    "       concat(first_name, ' ', middle_name, ' ', last_name) as NAME,\n" +
                    "       z.zone,\n" +
                    "       marital_status                                       as maritalstatus,\n" +
                    "       g.`group`                                            as 'group',\n" +
                    "       middle_name                                          as marriage,\n" +
                    "       last_name                                            as position,\n" +
                    "       first_name                                           as parent,\n" +
                    "       phone\n" +
                    "from members m\n" +
                    "         inner join zones z on m.zone = z.zoneid\n" +
                    "         inner join membergroups g on m.member_group = g.groupid\n" +
                    "         inner join allgroups a on a.member = m.id\n" +
                    "         inner join committeesanddepartments b ON b.`id` = a.`group`\n" +
                    "where m.farm = " + 3 + "\n" +
                    "    and b.`committee` = '" + choice + "'\n" +
                    "  and b.`group` = 'DEPARTMENT';";

            jp = generateReport(query, "/reports/members.jrxml");

        } else if (category.equals("committee")) {
            String query = "select m.id, m.member_number,\n" +
                    "       concat(first_name, ' ', middle_name, ' ', last_name) as NAME,\n" +
                    "       z.zone,\n" +
                    "       marital_status                                       as maritalstatus,\n" +
                    "       g.`group`                                            as 'group',\n" +
                    "       middle_name                                          as marriage,\n" +
                    "       last_name                                            as position,\n" +
                    "       first_name                                           as parent,\n" +
                    "       phone\n" +
                    "from members m\n" +
                    "         inner join zones z on m.zone = z.zoneid\n" +
                    "         inner join membergroups g on m.member_group = g.groupid\n" +
                    "         inner join allgroups a on a.member = m.id\n" +
                    "         inner join committeesanddepartments b ON b.`id` = a.`group`\n" +
                    "where m.farm = 3\n" +
                    "    and b.`committee` = '" + 3 + "'\n" +
                    "  and b.`group` = 'COMMITTEE';";

            jp = generateReport(query, "/reports/members.jrxml");

        }

        JasperExportManager.exportReportToPdfFile(jp, uploadDir + File.separator +
                RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Disposition", "attachment; filename=" +
                RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = Base64.getEncoder().encode(inFileBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    @RequestMapping(path = {"/receipts/generate"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<byte[]> printReceipts(@RequestParam(name = "from") String from, @RequestParam(name = "to") String to) throws IOException, JRException, SQLException {
        String uploadDir = "static/" + farm().getUploadPath() + "/pdf";
        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        String jrxmlPath = "/reports/Monthly Expenditure.jrxml";


        String sql = " SELECT date, `Ref #`, `Group name`, a.Account, description, credit AS Debit\n" +
                "FROM accounttransactions t\n" +
                "         LEFT JOIN activities a ON t.Account = a.`Account id`\n" +
                "         LEFT JOIN activitygroups g ON a.accountgroup = g.`Group id`\n" +
                "WHERE date((t.Date)) between '" + from + "' and '" + to + "'\n" +
                "  AND description != 'Deposit to Bank'\n" +
                "  AND description != 'Cash Deposit'\n" +
                "  AND description != 'Cash Withdraw'\n" +
                "  AND description != 'Opening Balance'\n" +
                "AND g.`Group name` != 'EXPENSES';";

        param.put("from", "RECEIPTS FROM  " + from);
        param.put("to", to);

        JasperPrint jp = generateReport(sql, jrxmlPath);

        JasperExportManager.exportReportToPdfFile(jp, uploadDir + File.separator +
                RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Disposition", "attachment; filename=" +
                RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = Base64.getEncoder().encode(inFileBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    @RequestMapping(path = {"/vouchers/generate"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<byte[]> printVouchers(@RequestParam(name = "from") String from, @RequestParam(name = "to") String to) throws IOException, JRException, SQLException {
        String uploadDir = "static/" + farm().getUploadPath() + "/pdf";
        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        String jrxmlPath = "/reports/Monthly Expenditure.jrxml";


        String sql = "SELECT ac.date,`Group name`,a.Account,description,SUM(debit) AS Debit FROM activities a    " +
                "INNER JOIN paymentvouchers p ON p.activity=a.`Account id`  " +
                "INNER JOIN accounttransactions ac ON ac.`Ref #`=p.`Voucher #`  " +
                "INNER JOIN activitygroups g ON g.`Group id`=a.accountgroup  " +
                "WHERE   date((ac.Date))  between '" + from + "'  and  '" + to + "' GROUP BY a.`Account id`";


        param.put("from", "EXPENDITURE FROM  " + from);
        param.put("to", to);

        JasperPrint jp = generateReport(sql, jrxmlPath);

        JasperExportManager.exportReportToPdfFile(jp, uploadDir + File.separator +
                RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Disposition", "attachment; filename=" +
                RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = Base64.getEncoder().encode(inFileBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    @RequestMapping(path = {"/transactions/generate"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<byte[]> printTransactions(@RequestParam(name = "from") String from, @RequestParam(name = "to") String to) throws IOException, JRException, SQLException {
        String uploadDir = "static/" + farm().getUploadPath() + "/pdf";
        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        String jrxmlPath = "/reports/Transaction Listing.jrxml";


        String sql = "SELECT DATE,description,credit,debit FROM accounttransactions"
                + "	WHERE description!='Cash Withdraw' and description!='Cash From Bank'"
                + " and description!='Cash Deposit' and description!='Opening Balance' "
                + " and description!='Deposit to Bank'  "
                + " and date(DATE) between '" + from + "' "
                + " and '" + to + "' order by date";


        param.put("from", " FROM  " + from + "  to  " + to);

        JasperPrint jp = generateReport(sql, jrxmlPath);

        JasperExportManager.exportReportToPdfFile(jp, uploadDir + File.separator +
                RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Disposition", "attachment; filename=" +
                RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = Base64.getEncoder().encode(inFileBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    @RequestMapping(path = {"/trial/generate"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<byte[]> printTrialBalance(@RequestParam(name = "activity") String activity, @RequestParam(name = "type") String type, @RequestParam(name = "date") String date) throws Exception {
        String uploadDir = "static/" + farm().getUploadPath() + "/pdf";
        String EXTENSION = ".pdf";
        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        String jrxmlPath = "/reports/TrialBalance.jrxml";
        String sql = "";

        if (activity.equalsIgnoreCase("all")) {
            if (type.equalsIgnoreCase("year")) {
                title = "Trial Balance as at " + date;
                sql = "SELECT\n" +
                        "    year,\n" +
                        "    month,\n" +
                        "    CONCAT(month, ' ', year) AS yearMonth,\n" +
                        "    SUM(credit) AS credit,\n" +
                        "    SUM(debit) AS debit,\n" +
                        "    SUM(SUM(credit) - SUM(debit)) OVER (ORDER BY year, m) AS balance\n" +
                        "FROM (\n" +
                        "         SELECT\n" +
                        "             MONTH(Date) AS m,\n" +
                        "             MONTHNAME(Date) AS month,\n" +
                        "             YEAR(Date) AS year,\n" +
                        "             credit,\n" +
                        "             debit\n" +
                        "         FROM accounttransactions a\n" +
                        "                  INNER JOIN activities b ON a.Account = b.`Account id`\n" +
                        "         WHERE description NOT IN (\n" +
                        "                                   'Cash Withdraw',\n" +
                        "                                   'Cash From Bank',\n" +
                        "                                   'Cash Deposit',\n" +
                        "                                   'Opening Balance',\n" +
                        "                                   'Deposit to Bank'\n" +
                        "             )\n" +
                        "           AND DATE <= '" + date + "'\n" +
                        "     ) AS subquery\n" +
                        "GROUP BY year, yearMonth\n" +
                        "ORDER BY year, m;\n";
                List<String> sql1 = new ArrayList<>();
                int farm = 3;
                testReport(sql, portrait_report_template, "trial", 0, sql1, title, "", uploadDir + File.separator + RequestContextHolder.
                        currentRequestAttributes().getSessionId() + EXTENSION, farm, "");
            } else {
                title = "Trial Balance as at " + date;
                sql = "SELECT month, year, CONCAT(month, ' ', year) as yearMonth, DATE,\n" +
                        "       description,\n" +
                        "       income,\n" +
                        "       expenditure,\n" +
                        "       SUM(income - expenditure) OVER (ORDER BY DATE, Account) AS balance\n" +
                        "FROM (SELECT DATE, a.Account,\n" +
                        "             MONTHNAME(Date) as month,\n" +
                        "             YEAR(Date) as year,\n" +
                        "             description,\n" +
                        "             credit AS income,\n" +
                        "             debit AS expenditure\n" +
                        "      FROM accounttransactions a\n" +
                        "               INNER JOIN activities b ON a.Account = b.`Account id`\n" +
                        "      WHERE description NOT IN (\n" +
                        "                                'Cash Withdraw',\n" +
                        "                                'Cash From Bank',\n" +
                        "                                'Cash Deposit',\n" +
                        "                                'Opening Balance',\n" +
                        "                                'Deposit to Bank'\n" +
                        "          )\n" +
                        "        AND DATE <= '" + date + "') AS subquery\n" +
                        "order by Date;\n" +
                        "\n";

                List<String> sql1 = new ArrayList<>();
                int farm = 3;
                testReport(sql, portrait_report_template, "trial", 1, sql1, title, "", uploadDir + File.separator + RequestContextHolder.
                        currentRequestAttributes().getSessionId() + EXTENSION, farm, "");
            }
        } else {
            String activityName = activityRepository.findById(Integer.valueOf(activity)).orElse(null).getAccount();
            title = activityName + " ACCOUNT TRIAL BALANCE AS AT " + date;
            if (type.equalsIgnoreCase("year")) {
                sql = "SELECT\n" +
                        "    year,     month,\n" +
                        "    CONCAT(month, ' ', year) AS yearMonth,\n" +
                        "    SUM(credit) AS credit,\n" +
                        "    SUM(debit) AS debit,\n" +
                        "    SUM(SUM(credit) - SUM(debit)) OVER (ORDER BY year, m) AS balance\n" +
                        "FROM (\n" +
                        "         SELECT\n" +
                        "             MONTH(Date) AS m,\n" +
                        "             MONTHNAME(Date) AS month,\n" +
                        "             YEAR(Date) AS year,\n" +
                        "             credit,\n" +
                        "             debit\n" +
                        "         FROM accounttransactions a\n" +
                        "                  INNER JOIN activities b ON a.Account = b.`Account id`\n" +
                        "         WHERE a.Account = " + activity + " AND description NOT IN (\n" +
                        "                                                      'Cash Withdraw',\n" +
                        "                                                      'Cash From Bank',\n" +
                        "                                                      'Cash Deposit',\n" +
                        "                                                      'Opening Balance',\n" +
                        "                                                      'Deposit to Bank'\n" +
                        "             )\n" +
                        "           AND DATE <= '" + date + "'\n" +
                        "     ) AS subquery\n" +
                        "GROUP BY year, yearMonth\n" +
                        "ORDER BY year, m;";
                List<String> sql1 = new ArrayList<>();
                int farm = 3;
                testReport(sql, portrait_report_template, "trial", 0, sql1, title, "", uploadDir + File.separator + RequestContextHolder.
                        currentRequestAttributes().getSessionId() + EXTENSION, farm, "");
            } else {
                sql = "SELECT DATE,\n" +
                        "       month,\n" +
                        "       description,\n" +
                        "       income,\n" +
                        "       expenditure,\n" +
                        "       (@prev := @prev + income - expenditure) AS balance\n" +
                        "FROM (SELECT @prev := 0, DATE, a.Account,\n" +
                        "             MONTHNAME(Date) as month,\n" +
                        "             YEAR(Date)      as year,\n" +
                        "             description,\n" +
                        "             credit          as income,\n" +
                        "             debit           as expenditure\n" +
                        "      FROM accounttransactions a\n" +
                        "               INNER JOIN activities b ON a.Account = b.`Account id`\n" +
                        "      WHERE a.Account = " + activity + "\n" +
                        "        and description NOT IN (\n" +
                        "                                'Cash Withdraw',\n" +
                        "                                'Cash From Bank',\n" +
                        "                                'Cash Deposit',\n" +
                        "                                'Opening Balance',\n" +
                        "                                'Deposit to Bank'\n" +
                        "          )\n" +
                        "\n" +
                        "        AND DATE <= '" + date + "'\n" +
                        "      order by Date) a";

                List<String> sql1 = new ArrayList<>();
                int farm = 3;
                testReport(sql, portrait_report_template, "trial", 1, sql1, title, "", uploadDir + File.separator + RequestContextHolder.
                        currentRequestAttributes().getSessionId() + EXTENSION, farm, "");
            }
        }

        System.out.println(sql);

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Disposition", "attachment; filename=" +
                RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = Base64.getEncoder().encode(inFileBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    @RequestMapping(path = {"/ledger/generate"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<byte[]> printLedger(@RequestParam(name = "from") String from, @RequestParam(name = "to") String to) throws IOException, JRException, SQLException {
        String uploadDir = "static/" + farm().getUploadPath() + "/pdf";
        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        String jrxmlPath = "/reports/generaldetailedleger.jrxml";


        String sql = "SELECT DATE,c.Account,s.description,`Ref #`,  "
                + " sum(debit) AS dr,  "
                + " sum(credit) AS cr  "
                + " FROM activities c  "
                + " INNER JOIN accounttransactions s ON s.account=c.`Account id`"
                + " WHERE date  between '" + from + "' and '" + to + "'  "
                + " GROUP BY `Ref #`,account       "
                + " ORDER BY  account,DATE,FIELD(s.Date,'OPENING BALANCE')";

        JasperPrint jp = generateReport(sql, jrxmlPath);

        JasperExportManager.exportReportToPdfFile(jp, uploadDir + File.separator +
                RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Disposition", "attachment; filename=" +
                RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = Base64.getEncoder().encode(inFileBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    @RequestMapping(path = {"/reconciliation/generate"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<byte[]> printBankReconciliation(@RequestParam("bank") String bank,
                                                          @RequestParam("transactionDate") String transactionDate,
                                                          @RequestParam("statementBalance") Double statementBalance,
                                                          @RequestParam("unrecordedPayments") Double unrecordedPayments,
                                                          @RequestParam("missingReceipts") Double missingReceipts,
                                                          @RequestParam("missingJournalReceipts") Double missingJournalReceipts,
                                                          @RequestParam("unmatchedJournalPayments") Double unmatchedJournalPayments) throws IOException, JRException, SQLException {
        String uploadDir = "static/" + farm().getUploadPath() + "/pdf";
        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        String jrxmlPath = "/reports/BankReconciliation.jrxml";

        String sql = "SELECT " + unrecordedPayments + " as paymentsinbanknotinjoural, " + missingReceipts + " as receiptsinjournalnotinbank ,"
                + " " + missingJournalReceipts + " as rctsinbanknotinjournal, " + unmatchedJournalPayments + " as paymentsinjournalnotyetrecordedinbank ,"
                + " " + statementBalance + " as balanceperstatement,SUM(credit-debit) as systembalance,`ACCOUNT #` FROM accounttransactions "
                + " INNER JOIN bankaccounts ON `Acc id`=bank WHERE bank='" + bank + "' and DATE BETWEEN 1 AND '" + transactionDate + "'  ";

        JasperPrint jp = generateReport(sql, jrxmlPath);

        JasperExportManager.exportReportToPdfFile(jp, uploadDir + File.separator +
                RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Disposition", "attachment; filename=" +
                RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = Base64.getEncoder().encode(inFileBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    @RequestMapping(path = {"/quarterlyReconciliation/generate"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<byte[]> printquarterlyReconciliation(@RequestParam(name = "date") String date) throws IOException, JRException, SQLException {
        String uploadDir = "static/" + farm().getUploadPath() + "/pdf";
        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        String jrxmlPath = "/reports/Quarterly Reconciliation.jrxml";

        try {
            Connection conn = DbConnector.getConnection();
            ResultSet rs = conn.prepareStatement("SELECT `Acc id`,`TYPE` FROM bankaccounts GROUP BY `Acc id`").executeQuery();
            while (rs.next()) {
                if (rs.getString(1).equalsIgnoreCase("cash")) {
                    cash = rs.getInt(0);
                }
                if (rs.getString(1).equalsIgnoreCase("bank")) {
                    bank = rs.getInt(0);
                }
            }
            rs.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        LocalDate localDate = LocalDate.parse(date);
        String string1 = localDate.getMonth().toString() + " " + localDate.getYear();
        String string2 = localDate.getYear() - 1 + "-12-31";
        String string3 = String.valueOf(localDate.getYear());

        dated = "JANUARY TO DECEMBER " + LocalDate.parse(date).getYear();
        String sql = "SELECT"
                + " sum( if( (bank=" + bank + " and DATE BETWEEN 1 AND '" + string2 + "') ||  description ='Opening Balance'  ,(credit-debit), 0 ) ) AS openingbalance,"

                + " sum( if( bank=" + bank + " and DATE BETWEEN '" + string3 + "-01-01' AND '" + string3 + "-03-31'  and description !='Opening Balance'  ,(credit), 0 ) ) AS march,"
                + " sum( if( bank=" + bank + " and DATE BETWEEN '" + string3 + "-04-01' AND '" + string3 + "-06-31'  and description !='Opening Balance' ,(credit), 0 ) ) AS june,"
                + " sum( if( bank=" + bank + " and DATE BETWEEN '" + string3 + "-07-01' AND '" + string3 + "-09-31'  and description !='Opening Balance' ,(credit), 0 ) ) AS september,"
                + " sum( if( bank=" + bank + " and DATE BETWEEN '" + string3 + "-10-01' AND '" + string3 + "-12-31'  and description !='Opening Balance' ,(credit), 0 ) ) AS december,"


                + " sum( if( bank=" + bank + " and Month(DATE) ='01' and description !='Opening Balance' AND year(DATE) ='" + string3 + "' AND SUBSTRING(`ref #`,1,2)='PV' ,(debit), 0 ) ) AS jan,"
                + " sum( if( bank=" + bank + " and Month(DATE) ='02' and description !='Opening Balance' AND year(DATE) ='" + string3 + "' AND SUBSTRING(`ref #`,1,2)='PV' ,(debit), 0 ) ) AS feb,"
                + " sum( if( bank=" + bank + " and Month(DATE) ='03' and description !='Opening Balance' AND year(DATE) ='" + string3 + "' AND SUBSTRING(`ref #`,1,2)='PV' ,(debit), 0 ) ) AS mar,"
                + " sum( if( bank=" + bank + " and Month(DATE) ='04' and description !='Opening Balance' AND year(DATE) ='" + string3 + "' AND SUBSTRING(`ref #`,1,2)='PV' ,(debit), 0 ) ) AS apr,"
                + " sum( if( bank=" + bank + " and Month(DATE) ='05' and description !='Opening Balance' AND year(DATE) ='" + string3 + "' AND SUBSTRING(`ref #`,1,2)='PV' ,(debit), 0 ) ) AS may,"
                + " sum( if( bank=" + bank + " and Month(DATE) ='06' and description !='Opening Balance' AND year(DATE) ='" + string3 + "' AND SUBSTRING(`ref #`,1,2)='PV' ,(debit), 0 ) ) AS jun,"
                + " sum( if( bank=" + bank + " and Month(DATE) ='07' and description !='Opening Balance' AND year(DATE) ='" + string3 + "' AND SUBSTRING(`ref #`,1,2)='PV' ,(debit), 0 ) ) AS jul,"
                + " sum( if( bank=" + bank + " and Month(DATE) ='08' and description !='Opening Balance' AND year(DATE) ='" + string3 + "' AND SUBSTRING(`ref #`,1,2)='PV' ,(debit), 0 ) ) AS aug,"
                + " sum( if( bank=" + bank + " and Month(DATE) ='09' and description !='Opening Balance' AND year(DATE) ='" + string3 + "' AND SUBSTRING(`ref #`,1,2)='PV' ,(debit), 0 ) ) AS sep,"
                + " sum( if( bank=" + bank + " and Month(DATE) ='10' and description !='Opening Balance' AND year(DATE) ='" + string3 + "' AND SUBSTRING(`ref #`,1,2)='PV' ,(debit), 0 ) ) AS octt,"
                + " sum( if( bank=" + bank + " and Month(DATE) ='11' and description !='Opening Balance' AND year(DATE) ='" + string3 + "' AND SUBSTRING(`ref #`,1,2)='PV' ,(debit), 0 ) ) AS nov,"
                + " sum( if( bank=" + bank + " and Month(DATE) ='12' and description !='Opening Balance' AND year(DATE) ='" + string3 + "' AND SUBSTRING(`ref #`,1,2)='PV' ,(debit), 0 ) ) AS dece,"
                + " sum( if( bank=" + bank + " and year(DATE) ='" + string3 + "' AND SUBSTRING(`ref #`,1,2)='PV' ,(debit), 0 ) ) AS totalexpe"
                + "  FROM accounttransactions ";

        JasperPrint jp = generateReport(sql, jrxmlPath);

        JasperExportManager.exportReportToPdfFile(jp, uploadDir + File.separator +
                RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Disposition", "attachment; filename=" +
                RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = Base64.getEncoder().encode(inFileBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    @RequestMapping(path = {"/journal/generate"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<byte[]> printCashJournal(@RequestParam(name = "date") String date) throws IOException, JRException, SQLException {
        String uploadDir = "static/" + farm().getUploadPath() + "/pdf";
        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        String jrxmlPath = "/reports/CashJournal.jrxml";

        dated = LocalDate.parse(date).getMonth().toString() + " " + LocalDate.parse(date).getYear();
        title = dated;

        try {
            Connection conn = DbConnector.getConnection();
            ResultSet rs = conn.prepareStatement("SELECT `Acc id`,`TYPE` FROM bankaccounts GROUP BY `Acc id`").executeQuery();
            while (rs.next()) {
                if (rs.getString(1).equalsIgnoreCase("cash")) {
                    cash = rs.getInt(0);
                }
                if (rs.getString(1).equalsIgnoreCase("bank")) {
                    bank = rs.getInt(0);
                }
            }
            rs.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql = "SELECT `Account #`,DATE,particulars,`ref #`,sum(debitc) AS debitc, "
                + " SUM(creditc) AS creditc,SUM(debitb) AS debitb,SUM(creditb) AS "
                + "creditb,`cheque #` FROM (SELECT a.`Account #`,ac.DATE, "
                + " case when SUBSTRING(`ref #`,0,2)='RF' "
                + " THEN a.Account ELSE ac.Description END AS 	 particulars,`ref #`,   "
                + "  case when ac.bank=" + cash + " then debit END AS debitc,  "
                + "  case when ac.bank=" + cash + " then credit END AS creditc, "
                + "  case when ac.bank=" + bank + " then debit END AS debitb,   "
                + " case when ac.bank=" + bank + " then credit END AS creditb,`cheque #`  "
                + " FROM accounttransactions ac   LEFT JOIN  "
                + "  paymentvouchers pv ON pv.`Voucher #`= ac.`Ref #`    "
                + "  left JOIN activities a ON a.`Account id`= pv.activity   "
                + "   WHERE ac.date between DATE_FORMAT('" + date + "' ,'%Y-%m-01')  "
                + "   AND '" + date + "')f group by `ref #` ORDER BY  date ";

        try {
            Connection conn = DbConnector.getConnection();
            String query = "SELECT * FROM (SELECT"
                    + " case when bankcloasing<0 then bankcloasing ELSE 0 END AS odebitb, "
                    + " case when bankcloasing>0 then bankcloasing ELSE 0 END AS ocreditb,"
                    + " case when cashcloasing<0 then cashcloasing ELSE 0 END AS odebitc, "
                    + " case when cashcloasing>0 then cashcloasing ELSE 0 END AS ocreditc"
                    + " FROM( SELECT a.`Account #`,DATE, ifnull(a.Account,description) AS particulars,`ref #`,"
                    + " sum( if( (ac.bank='" + bank + "' AND DATE BETWEEN 1  AND LAST_DAY"
                    + "('" + date + "' -INTERVAL 1 MONTH)) ,((credit-debit)), 0 ) ) AS bankcloasing,"
                    + " sum( if( (ac.bank='" + cash + "' AND DATE BETWEEN 1  AND LAST_DAY"
                    + "('" + date + "' -INTERVAL 1 MONTH)) ,((credit-debit)), 0 ) ) AS cashcloasing"
                    + " FROM activities a right JOIN accounttransactions ac ON ac.bank= a.`Account id`"
                    + " INNER JOIN bankaccounts b ON b.`Acc id`= ac.bank"
                    + " )k GROUP BY particulars)q";

            System.out.println(query);
            ResultSet rs = conn.prepareStatement(query).executeQuery();
            while (rs.next()) {
                param.put("odebitb", Double.parseDouble(rs.getString("odebitb")));
                param.put("ocreditb", Double.parseDouble(rs.getString("ocreditb")));
                param.put("odebitc", Double.parseDouble(rs.getString("odebitc")));
                param.put("ocreditc", Double.parseDouble(rs.getString("ocreditc")));
            }
            rs.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JasperPrint jp = generateReport(sql, jrxmlPath);

        JasperExportManager.exportReportToPdfFile(jp, uploadDir + File.separator +
                RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Disposition", "attachment; filename=" +
                RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = Base64.getEncoder().encode(inFileBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }


    public void updateCBs(String year) {

        for (int i = 1; i <= 12; i++) {
            String month;
            if (i < 10) {
                month = "0" + i;
            } else {
                month = String.valueOf(i);
            }
            String dateString = year + "-" + month + "-01";

            String sql = "(SELECT MONTH(DATE)                                 AS month,\n" +
                    "        MONTHNAME(DATE)                             as monthName,\n" +
                    "        PAIDTHROUGH,\n" +
                    "        description,\n" +
                    "        DATE,\n" +
                    "        TYPE,\n" +
                    "\n" +
                    "        sum(if(Account = 'TITHE', Amount, 0))       AS TITHE,\n" +
                    "        sum(if(Account = 'OFFERING', Amount, 0))    AS OFFERING,\n" +
                    "        sum(if(Account = 'THANKGIVING', Amount, 0)) AS THANKGIVING,\n" +
                    "        sum(if(Account = 'FIRST FRUIT', Amount, 0)) AS 'FIRST FRUIT',\n" +
                    "        sum(if(Account = 'OTHERS', Amount, 0))      AS OTHERS,\n" +
                    "        sum(if(1 = 1, Amount, 0))                   AS TOTAL\n" +
                    "\n" +
                    "\n" +
                    " FROM (SELECT b.Account   AS Paidthrough,\n" +
                    "              a.Date,\n" +
                    "              a.description,\n" +
                    "              'INCOMES'   AS TYPE,\n" +
                    "              v.Account,\n" +
                    "              SUM(credit) AS Amount\n" +
                    "       FROM accounttransactions a\n" +
                    "                INNER JOIN activities v ON a.Account = v.`Account id`\n" +
                    "                INNER JOIN activities b ON b.`Account id` = a.Bank\n" +
                    "       WHERE a.credit > 0\n" +
                    "         and year(date) = '" + year + "'\n" +
                    "         and month(date) = " + month + "\n" +
                    "         AND a.status = 'Approved' and a.farm = " + farm().getId() + "\n" +
                    "       GROUP BY DATE, v.`Account id`) a\n" +
                    " GROUP BY DATE\n" +
                    "\n" +
                    " UNION ALL\n" +
                    "\n" +
                    " SELECT '',\n" +
                    "        '',\n" +
                    "        '',\n" +
                    "        '',\n" +
                    "        '',\n" +
                    "        'Balance BF',\n" +
                    "        '',\n" +
                    "        '',\n" +
                    "        '',\n" +
                    "        '',\n" +
                    "        '',\n" +
                    "        total\n" +
                    " FROM (SELECT IFNULL(balance, 0) as total\n" +
                    "       from closing_balance\n" +
                    "       where date >= DATE_SUB('" + dateString + "', INTERVAL 1 MONTH)\n" +
                    "         AND date < '" + dateString + "'\n" +
                    "         and farm = '" + farm().getId() + "') a\n" +
                    "\n" +
                    "\n" +
                    " UNION ALL\n" +
                    "\n" +
                    " SELECT MONTH(DATE)                            AS month,\n" +
                    "        MONTHNAME(DATE)                        as monthName,\n" +
                    "        PAIDTHROUGH,\n" +
                    "        description,\n" +
                    "        DATE,\n" +
                    "        TYPE,\n" +
                    "\n" +
                    "        sum(if(Account = 'TITHE', 0, 0))       AS TITHE,\n" +
                    "        sum(if(Account = 'OFFERING', 0, 0))    AS OFFERING,\n" +
                    "        sum(if(Account = 'THANKGIVING', 0, 0)) AS THANKGIVING,\n" +
                    "        sum(if(Account = 'FIRST FRUIT', 0, 0)) AS 'FIRST FRUIT',\n" +
                    "        sum(if(Account = 'OTHERS', 0, 0))      AS OTHERS,\n" +
                    "        sum(if(1 = 1, Amount, 0))              AS TOTAL\n" +
                    " FROM (SELECT b.Account  AS Paidthrough,\n" +
                    "              a.Date,\n" +
                    "              a.description,\n" +
                    "              'EXPENSES' AS TYPE,\n" +
                    "              v.Account,\n" +
                    "              SUM(debit) AS Amount,\n" +
                    "              (@expensesTotal := @expensesTotal + SUM(debit))\n" +
                    "       FROM accounttransactions a\n" +
                    "                INNER JOIN activities v ON a.Account = v.`Account id`\n" +
                    "                INNER JOIN activities b ON b.`Account id` = a.Bank\n" +
                    "       WHERE a.debit > 0\n" +
                    "         and year(a.date) = " + year + "\n" +
                    "         and month(date) = " + month + " and a.farm = " + farm().getId() + "\n" +
                    "       GROUP BY v.`Account id`) a\n" +
                    " GROUP BY account) order by month, date;";


            Map<String, Double> typeSubtotals = new HashMap<>();
            double balanceBF = 0.0;
            double incomeSubtotal = 0.0;
            double expenseSubtotal = 0.0;

            try (Connection connection = DbConnector.getConnection()) {
                Statement statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery(sql);

                while (resultSet.next()) {
                    String type = resultSet.getString("TYPE");
                    double tithe = resultSet.getDouble("TITHE");
                    double offering = resultSet.getDouble("OFFERING");
                    double thanksgiving = resultSet.getDouble("THANKGIVING");
                    double firstFruit = resultSet.getDouble("FIRST FRUIT");
                    double others = resultSet.getDouble("OTHERS");
                    double total = resultSet.getDouble("TOTAL");

                    // Check if the row is an income or an expense
                    if ("INCOMES".equals(type)) {
                        // Update income subtotal
                        incomeSubtotal += total;
                    } else if ("EXPENSES".equals(type)) {
                        // Update expense subtotal
                        expenseSubtotal += total;
                    } else if ("Balance BF".equalsIgnoreCase(type)) {
                        balanceBF += total;
                    }

                    // Update type subtotals
                    typeSubtotals.put(type, typeSubtotals.getOrDefault(type, 0.0) + total);
                }

                double totalIncome = balanceBF + incomeSubtotal;

                double surplusOrDeficit = incomeSubtotal - expenseSubtotal;

                double balanceCarriedDown = totalIncome - expenseSubtotal;

                if (closingBalanceRepository.existsByDateAndChurch(LocalDate.parse(dateString), farm())) {
                    System.out.println("exists");
                    ClosingBalance closingBalance = closingBalanceRepository.findByDateAndChurch(LocalDate.parse(dateString), farm());
                    closingBalance.setBalance((float) balanceCarriedDown);
                    closingBalanceRepository.save(closingBalance);

                    System.out.println(dateString);
                } else {
                    ClosingBalance closingBalance = new ClosingBalance();
                    closingBalance.setBalance((float) balanceCarriedDown);
                    closingBalance.setDate(LocalDate.parse(dateString));
                    closingBalance.setFarm(farm());
                    closingBalanceRepository.save(closingBalance);

                    System.out.println(dateString);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(path = {"/incomeAndExpenditure/generate"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<byte[]> printIncomeAndExpenditure(@RequestParam(name = "year") String year, @RequestParam(name = "month") String month, @RequestParam(name = "activity") String activity) throws Exception {
        if (Integer.parseInt(month) < 10) {
            month = "0" + month;
        }

        updateCBs(year);

        String dateString = year + "-" + month + "-01";
        String sql;
        if (activity.equalsIgnoreCase("all")) {
            sql = "(SELECT MONTH(DATE)                                 AS month,\n" +
                    "        MONTHNAME(DATE)                             as monthName,\n" +
                    "        PAIDTHROUGH,\n" +
                    "        account,\n" +
                    "        DATE,\n" +
                    "        TYPE,\n" +
                    "\n" +
                    "        sum(if(Account = 'TITHE', Amount, 0))       AS TITHE,\n" +
                    "        sum(if(Account = 'OFFERING', Amount, 0))    AS OFFERING,\n" +
                    "        sum(if(Account = 'THANKGIVING', Amount, 0)) AS THANKGIVING,\n" +
                    "        sum(if(Account = 'FIRST FRUIT', Amount, 0)) AS 'FIRST FRUIT',\n" +
                    "        sum(if(Account = 'OTHERS', Amount, 0))      AS OTHERS,\n" +
                    "        sum(if(1 = 1, Amount, 0))                   AS TOTAL\n" +
                    "\n" +
                    "\n" +
                    " FROM (SELECT b.Account AS Paidthrough,\n" +
                    "              a.Date,\n" +
                    "              'INCOMES'   AS TYPE,\n" +
                    "              v.Account as account,\n" +
                    "              SUM(credit) AS Amount\n" +
                    "       FROM accounttransactions a\n" +
                    "                INNER JOIN activities v ON a.Account = v.`Account id`\n" +
                    "                INNER JOIN activities b ON b.`Account id` = a.Bank\n" +
                    "       WHERE a.credit > 0\n" +
                    "         and year(date) = '"+year+"'\n" +
                    "         and month(date) = "+month+" and a.farm = "+farm().getId()+"\n" +
                    "         AND a.status = 'Approved'\n" +
                    "       GROUP BY DATE, v.`Account id`) a\n" +
                    " GROUP BY DATE\n" +
                    "\n" +
                    " UNION ALL\n" +
                    "\n" +
                    " SELECT '',\n" +
                    "        '',\n" +
                    "        '',\n" +
                    "        '',\n" +
                    "        '',\n" +
                    "        'Balance BF',\n" +
                    "        '',\n" +
                    "        '',\n" +
                    "        '',\n" +
                    "        '',\n" +
                    "        '',\n" +
                    "        total\n" +
                    " FROM (SELECT IFNULL(balance, 0) as total\n" +
                    "       from closing_balance\n" +
                    "       where date >= DATE_SUB('" + dateString + "', INTERVAL 1 MONTH)\n" +
                    "         AND date < '" + dateString + "'\n" +
                    "         and farm = '" + farm().getId() + "') a\n" +
                    " UNION ALL\n" +
                    "\n" +
                    " SELECT MONTH(DATE)                            AS month,\n" +
                    "        MONTHNAME(DATE)                        as monthName,\n" +
                    "        PAIDTHROUGH,\n" +
                    "        account,\n" +
                    "        DATE,\n" +
                    "        TYPE,\n" +
                    "\n" +
                    "        sum(if(Account = 'TITHE', 0, 0))       AS TITHE,\n" +
                    "        sum(if(Account = 'OFFERING', 0, 0))    AS OFFERING,\n" +
                    "        sum(if(Account = 'THANKGIVING', 0, 0)) AS THANKGIVING,\n" +
                    "        sum(if(Account = 'FIRST FRUIT', 0, 0)) AS 'FIRST FRUIT',\n" +
                    "        sum(if(Account = 'OTHERS', 0, 0))      AS OTHERS,\n" +
                    "        sum(if(1 = 1, Amount, 0))              AS TOTAL\n" +
                    " FROM (SELECT b.Account  AS Paidthrough,\n" +
                    "              a.Date,\n" +
                    "              'EXPENSES' AS TYPE,\n" +
                    "              v.Account as account,\n" +
                    "              SUM(debit) AS Amount,\n" +
                    "              (@expensesTotal := @expensesTotal + SUM(debit))\n" +
                    "       FROM accounttransactions a\n" +
                    "                INNER JOIN activities v ON a.Account = v.`Account id`\n" +
                    "                INNER JOIN activities b ON b.`Account id` = a.Bank\n" +
                    "       WHERE a.debit > 0\n" +
                    "         and year(a.date) = "+year+"\n" +
                    "         and month(date) = "+month+" and a.farm = "+farm().getId()+"\n" +
                    "       GROUP BY v.`Account id`) a\n" +
                    " GROUP BY account) order by month, date;";
        } else {
            sql = "(SELECT MONTH(DATE)                                 AS month,\n" +
                    "        MONTHNAME(DATE)                             as monthName,\n" +
                    "        PAIDTHROUGH,\n" +
                    "        description,\n" +
                    "        DATE,\n" +
                    "        TYPE,\n" +
                    "\n" +
                    "        sum(if(Account = 'TITHE', Amount, 0))       AS TITHE,\n" +
                    "        sum(if(Account = 'OFFERING', Amount, 0))    AS OFFERING,\n" +
                    "        sum(if(Account = 'THANKGIVING', Amount, 0)) AS THANKGIVING,\n" +
                    "        sum(if(Account = 'FIRST FRUIT', Amount, 0)) AS 'FIRST FRUIT',\n" +
                    "        sum(if(Account = 'OTHERS', Amount, 0))      AS OTHERS,\n" +
                    "        sum(if(1 = 1, Amount, 0))                   AS TOTAL\n" +
                    "\n" +
                    "\n" +
                    " FROM (SELECT b.Account   AS Paidthrough,\n" +
                    "              a.Date,\n" +
                    "              a.description,\n" +
                    "              'INCOMES'   AS TYPE,\n" +
                    "              v.Account,\n" +
                    "              SUM(credit) AS Amount\n" +
                    "       FROM accounttransactions a\n" +
                    "                INNER JOIN activities v ON a.Account = v.`Account id`\n" +
                    "                INNER JOIN activities b ON b.`Account id` = a.Bank\n" +
                    "       WHERE v.accountgroup = "+activity+"\n" +
                    "         AND a.credit > 0\n" +
                    "         and year(date) = '"+year+"'\n" +
                    "         and month(date) = "+month+"\n" +
                    "         AND a.status = 'Approved' and a.farm = "+farm().getId()+"\n" +
                    "       GROUP BY DATE, v.`Account id`) a\n" +
                    " GROUP BY DATE\n" +
                    "\n" +
                    " UNION ALL\n" +
                    "\n" +
                    " SELECT '',\n" +
                    "        '',\n" +
                    "        '',\n" +
                    "        '',\n" +
                    "        '',\n" +
                    "        'Balance BF',\n" +
                    "        '',\n" +
                    "        '',\n" +
                    "        '',\n" +
                    "        '',\n" +
                    "        '',\n" +
                    "        total\n" +
                    " FROM (SELECT IFNULL(balance, 0) as total\n" +
                    "       from closing_balance\n" +
                    "       where date >= DATE_SUB('" + dateString + "', INTERVAL 1 MONTH)\n" +
                    "         AND date < '" + dateString + "'\n" +
                    "         and farm = '" + farm().getId() + "') a\n" +
                    "\n" +
                    "\n" +
                    " UNION ALL\n" +
                    "\n" +
                    " SELECT MONTH(DATE)                            AS month,\n" +
                    "        MONTHNAME(DATE)                        as monthName,\n" +
                    "        PAIDTHROUGH,\n" +
                    "        description,\n" +
                    "        DATE,\n" +
                    "        TYPE,\n" +
                    "\n" +
                    "        sum(if(Account = 'TITHE', 0, 0))       AS TITHE,\n" +
                    "        sum(if(Account = 'OFFERING', 0, 0))    AS OFFERING,\n" +
                    "        sum(if(Account = 'THANKGIVING', 0, 0)) AS THANKGIVING,\n" +
                    "        sum(if(Account = 'FIRST FRUIT', 0, 0)) AS 'FIRST FRUIT',\n" +
                    "        sum(if(Account = 'OTHERS', 0, 0))      AS OTHERS,\n" +
                    "        sum(if(1 = 1, Amount, 0))              AS TOTAL\n" +
                    " FROM (SELECT b.Account  AS Paidthrough,\n" +
                    "              a.Date,\n" +
                    "              a.description,\n" +
                    "              'EXPENSES' AS TYPE,\n" +
                    "              v.Account,\n" +
                    "              SUM(debit) AS Amount,\n" +
                    "              (@expensesTotal := @expensesTotal + SUM(debit))\n" +
                    "       FROM accounttransactions a\n" +
                    "                INNER JOIN activities v ON a.Account = v.`Account id`\n" +
                    "                INNER JOIN activities b ON b.`Account id` = a.Bank\n" +
                    "       WHERE a.debit > 0\n" +
                    "         and v.accountgroup = "+activity+"\n" +
                    "         and year(a.date) = "+year+"\n" +
                    "         and month(date) = "+month+" and a.farm = "+farm().getId()+"\n" +
                    "       GROUP BY v.`Account id`) a\n" +
                    " GROUP BY account) order by month, date;";
        }


        String uploadDir = "static/" + farm().getUploadPath() + "/pdf";
        //String EXTENSION = ".pdf";
        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        //String jrxmlPath = "/reports/statementofreceiptsandpayments.jrxml";
        try {
            // Initialize the PDF document
            Document document = new Document();
            document.setPageSize(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf"));
            document.open();

            // Add content to the PDF
            String title = "INCOME AND EXPENDITURE REPORT FOR THE MONTH OF "+ Month.of(Integer.parseInt(month)) +" 2023";
            addLetterheadContent(writer.getDirectContent(), document);
            addContent(document, title, sql, dateString);

            // Close the document
            document.close();

            System.out.println("Income and Expenditure Report generated successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Disposition", "attachment; filename=" +
                RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = Base64.getEncoder().encode(inFileBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    private void addLetterheadContent(PdfContentByte contentByte, Document document) {
        String sql = "SELECT * FROM churches where churchid = "+farm().getId()+"";
        try (Connection connection = DbConnector.getConnection()) {
            ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
            // Load the image from the provided URL

            while (resultSet.next()) {
                String logo = resultSet.getString("logo");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String zipSt = resultSet.getString("zip");
                String phoneSt = resultSet.getString("phone");
                String emailSt = resultSet.getString("email");

                // Calculate the center position for the image at the top of the page
                Image logoImage = Image.getInstance(logo);
                logoImage.scaleAbsolute(80, 80);
                float centerX = (document.getPageSize().getWidth() - logoImage.getScaledWidth()) / 2;
                float topY = document.getPageSize().getHeight() - logoImage.getScaledHeight() - 50;

                logoImage.setAbsolutePosition(document.leftMargin(), topY);
                contentByte.addImage(logoImage);

                // Add text fields at the right of the image, centered vertically
                float textX = logoImage.getScaledWidth() + 10;

                com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 14, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 12);

                Paragraph companyName = new Paragraph(name, boldFont);
                companyName.setAlignment(Element.ALIGN_LEFT);
                companyName.setIndentationLeft(textX);
                document.add(companyName);

                Paragraph companyAddress = new Paragraph(address, normalFont);
                companyAddress.setAlignment(Element.ALIGN_LEFT);
                companyAddress.setIndentationLeft(textX);
                document.add(companyAddress);

                Paragraph zip = new Paragraph(zipSt, normalFont);
                zip.setAlignment(Element.ALIGN_LEFT);
                zip.setIndentationLeft(textX);
                document.add(zip);

                Paragraph email = new Paragraph(emailSt, normalFont);
                email.setAlignment(Element.ALIGN_LEFT);
                email.setIndentationLeft(textX);
                document.add(email);

                Paragraph phone = new Paragraph(phoneSt, normalFont);
                phone.setAlignment(Element.ALIGN_LEFT);
                phone.setIndentationLeft(textX);
                document.add(phone);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addContent(Document document, String title, String sql, String dateString) throws DocumentException {
        System.out.println(sql);
        document.addTitle(title);

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.UK);
        com.itextpdf.text.Font headersFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        headersFont.setSize(11f);

        com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        headersFont.setSize(15f);

        com.itextpdf.text.Font subtotalsFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        subtotalsFont.setSize(11f);

        com.itextpdf.text.Font profitFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        profitFont.setSize(11f);
        profitFont.setColor(BaseColor.BLUE);

        com.itextpdf.text.Font lossFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        lossFont.setSize(11f);
        lossFont.setColor(BaseColor.RED);


        Paragraph titleText = new Paragraph(title, titleFont);
        titleText.setAlignment(Element.ALIGN_CENTER);
        titleText.setSpacingBefore(21f);

        document.add(titleText);

        try (Connection connection = DbConnector.getConnection()) {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);
            float totalWidth = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();

            // Create a table for income
            PdfPTable incomeTable = new PdfPTable(7);
            incomeTable.setPaddingTop(20);
            incomeTable.setSpacingBefore(21f);
            incomeTable.setTotalWidth(totalWidth);
            incomeTable.setLockedWidth(true);
            PdfPTable expenseTable = new PdfPTable(7);
            expenseTable.setTotalWidth(totalWidth);
            expenseTable.setLockedWidth(true);

            accountsmodel.dbaction(sql, 1, 0, 0, 0);

            PdfPCell INCOMES = new PdfPCell(new Phrase("INCOMES", headersFont));
            INCOMES.setColspan(7);
            INCOMES.setHorizontalAlignment(Element.ALIGN_CENTER);
            incomeTable.addCell(INCOMES);

            PdfPCell expenses = new PdfPCell(new Phrase("EXPENSES", headersFont));
            expenses.setColspan(7);
            expenses.setHorizontalAlignment(Element.ALIGN_CENTER);
            expenseTable.addCell(expenses);

            addTableHeader(incomeTable, accountsmodel.collumns);

            addExpenseTableHeader(expenseTable);

            // Map to store subtotals for each type
            Map<String, Double> typeSubtotals = new HashMap<>();
            double balanceBF = 0.0;
            double incomeSubtotal = 0.0;
            double expenseSubtotal = 0.0;

            while (resultSet.next()) {
                String date = resultSet.getString("DATE");
                String type = resultSet.getString("TYPE");
                double tithe = resultSet.getDouble("TITHE");
                double offering = resultSet.getDouble("OFFERING");
                double thanksgiving = resultSet.getDouble("THANKGIVING");
                double firstFruit = resultSet.getDouble("FIRST FRUIT");
                double others = resultSet.getDouble("OTHERS");
                double total = resultSet.getDouble("TOTAL");

                // Check if the row is an income or an expense
                if ("INCOMES".equals(type)) {
                    // Update income subtotal
                    incomeSubtotal += total;

                    // Add data to the income table
                    incomeTable.addCell(date);
                    incomeTable.addCell(new PdfPCell(new Phrase(numberFormat.format(tithe))));
                    incomeTable.addCell(new PdfPCell(new Phrase(numberFormat.format(offering))));
                    incomeTable.addCell(new PdfPCell(new Phrase(numberFormat.format(thanksgiving))));
                    incomeTable.addCell(new PdfPCell(new Phrase(numberFormat.format(firstFruit))));
                    incomeTable.addCell(new PdfPCell(new Phrase(numberFormat.format(others))));
                    incomeTable.addCell(new PdfPCell(new Phrase(numberFormat.format(total))));
                } else if ("EXPENSES".equals(type)) {
                    // Update expense subtotal
                    expenseSubtotal += total;

                    // Add data to the expense table
                    PdfPCell typeCell = new PdfPCell(new Phrase(resultSet.getString("account")));
                    typeCell.setColspan(6);
                    expenseTable.addCell(typeCell);
                    expenseTable.addCell(new PdfPCell(new Phrase(numberFormat.format(total))));
                } else if ("Balance BF".equalsIgnoreCase(type)) {
                    balanceBF += total;
                }

                // Update type subtotals
                typeSubtotals.put(type, typeSubtotals.getOrDefault(type, 0.0) + total);
            }



            // Display subtotal for incomes
            PdfPCell incSubTotalCell = new PdfPCell(new Phrase("Subtotal for Income", headersFont));
            incSubTotalCell.setColspan(6);
            incomeTable.addCell(incSubTotalCell);
            PdfPCell incomeSubTotal = new PdfPCell(new Phrase(numberFormat.format(incomeSubtotal), subtotalsFont));
            incomeSubTotal.setColspan(6);
            incomeTable.addCell(incomeSubTotal);

            PdfPCell bfCell = new PdfPCell(new Phrase("BALANCE B/F", headersFont));
            bfCell.setColspan(6);
            incomeTable.addCell(bfCell);
            PdfPCell bfCellVal = new PdfPCell(new Phrase(numberFormat.format(balanceBF), subtotalsFont));
            bfCellVal.setColspan(6);
            incomeTable.addCell(bfCellVal);

            double totalIncome = balanceBF + incomeSubtotal;
            PdfPCell totIncSubTotalCell = new PdfPCell(new Phrase("Total Income", headersFont));
            totIncSubTotalCell.setColspan(6);
            incomeTable.addCell(totIncSubTotalCell);
            PdfPCell totIncSubTotal = new PdfPCell(new Phrase(numberFormat.format(totalIncome), subtotalsFont));
            totIncSubTotal.setColspan(6);
            incomeTable.addCell(totIncSubTotal);

            // Display subtotal for expenses

            PdfPCell expSubTotalCell = new PdfPCell(new Phrase("Subtotal for Expenses", headersFont));
            expSubTotalCell.setColspan(6);
            expenseTable.addCell(expSubTotalCell);
            PdfPCell expSubTotal = new PdfPCell(new Phrase(numberFormat.format(expenseSubtotal), subtotalsFont));
            expSubTotal.setColspan(6);
            expenseTable.addCell(expSubTotal);

            double surplusOrDeficit = incomeSubtotal - expenseSubtotal;
            PdfPCell surplus = new PdfPCell(new Phrase("Surplus/Deficit for the month", headersFont));
            surplus.setColspan(6);
            expenseTable.addCell(surplus);

            if (surplusOrDeficit > 0) {
                PdfPCell surplusOrDef = new PdfPCell(new Phrase(numberFormat.format(surplusOrDeficit), profitFont));
                surplusOrDef.setColspan(6);
                expenseTable.addCell(surplusOrDef);
            } else if (surplusOrDeficit < 0){
                PdfPCell surplusOrDef = new PdfPCell(new Phrase(numberFormat.format(surplusOrDeficit), lossFont));
                surplusOrDef.setColspan(6);
                expenseTable.addCell(surplusOrDef);
            } else {
                PdfPCell surplusOrDef = new PdfPCell(new Phrase(numberFormat.format(surplusOrDeficit), subtotalsFont));
                surplusOrDef.setColspan(6);
                expenseTable.addCell(surplusOrDef);
            }

            double balanceCarriedDown = totalIncome - expenseSubtotal;
            PdfPCell balCD = new PdfPCell(new Phrase("Balance Carried Down"));
            balCD.setColspan(6);

            if (closingBalanceRepository.existsByDateAndChurch(LocalDate.parse(dateString), farm())) {
                System.out.println("exists");
                ClosingBalance closingBalance = closingBalanceRepository.findByDateAndChurch(LocalDate.parse(dateString), farm());
                closingBalance.setBalance((float) balanceCarriedDown);
                closingBalanceRepository.save(closingBalance);
            } else {
                ClosingBalance closingBalance = new ClosingBalance();
                closingBalance.setBalance((float) balanceCarriedDown);
                closingBalance.setDate(LocalDate.parse(dateString));
                closingBalance.setFarm(farm());
                closingBalanceRepository.save(closingBalance);
            }

            expenseTable.addCell(balCD);

            if (surplusOrDeficit > 0) {
                PdfPCell balCDVal = new PdfPCell(new Phrase(numberFormat.format(balanceCarriedDown), profitFont));
                balCDVal.setColspan(6);
                expenseTable.addCell(balCDVal);
            } else if (surplusOrDeficit < 0){
                PdfPCell balCDVal = new PdfPCell(new Phrase(numberFormat.format(balanceCarriedDown), lossFont));
                balCDVal.setColspan(6);
                expenseTable.addCell(balCDVal);
            } else {
                PdfPCell balCDVal = new PdfPCell(new Phrase(numberFormat.format(balanceCarriedDown), subtotalsFont));
                balCDVal.setColspan(6);
                expenseTable.addCell(balCDVal);
            }

            // Add the tables to the PDF document
            document.add(incomeTable);
            document.add(expenseTable);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTableHeader(PdfPTable table, List<String> titles) {
        com.itextpdf.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        headerFont.setColor(BaseColor.WHITE);
        headerFont.setSize(11f);

        for (String title: titles) {
            if (    title.equalsIgnoreCase("date") ||
                    title.equalsIgnoreCase("tithe") ||
                    title.equalsIgnoreCase("offering") ||
                    title.equalsIgnoreCase("thankgiving") ||
                    title.equalsIgnoreCase("first fruit") ||
                    title.equalsIgnoreCase("others") ||
                    title.equalsIgnoreCase("total"))  {
                PdfPCell cell = new PdfPCell(new Phrase(title.toUpperCase(), headerFont));
                cell.setBackgroundColor(BaseColor.GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
            }
        }
    }

    private static void addExpenseTableHeader(PdfPTable table) {
        com.itextpdf.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        headerFont.setColor(BaseColor.WHITE);
        headerFont.setSize(11f);

        PdfPCell desc = new PdfPCell(new Phrase("DESCRIPTION", headerFont));
        desc.setColspan(6);
        desc.setBackgroundColor(BaseColor.GRAY);
        desc.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(desc);
        PdfPCell tot = new PdfPCell(new Phrase("TOTAL", headerFont));
        tot.setBackgroundColor(BaseColor.GRAY);
        tot.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(tot);
    }

    @RequestMapping(path = {"/expenditure/generate"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<byte[]> printExpenditure(@RequestParam(name = "activity") String activity, @RequestParam(name = "type") String type, @RequestParam(name = "year") String year, @RequestParam(name = "yearYear") String yearYear) throws Exception {

        System.out.println(activity);
        System.out.println(type);
        System.out.println(year);
        String uploadDir = "static/" + farm().getUploadPath() + "/pdf";
        String EXTENSION = ".pdf";
        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        if (activity.equalsIgnoreCase("all")) {
            if (type.equalsIgnoreCase("month")) {
                String sql = "SELECT date, m,\n" +
                        "       concat(month, ' ', year)                                       as yearMonth,\n" +
                        "       grp,\n" +
                        "       Account,\n" +
                        " CASE WHEN grp = 'expenditure' THEN Account END                 AS AccountName, " +
                        "       SUM(CASE WHEN Account = '1ST' THEN total ELSE 0 END)           AS `1ST`,\n" +
                        "       SUM(CASE WHEN Account = '2ND' THEN total ELSE 0 END)           AS `2ND`,\n" +
                        "       SUM(CASE WHEN Account = 'TITHING' THEN total ELSE 0 END)       AS TITHES,\n" +
                        "       SUM(CASE WHEN Account = 'OFFERING' THEN total ELSE 0 END)      AS OFFERING,\n" +
                        "       SUM(CASE WHEN Account = 'THANKSGIVING' THEN total ELSE 0 END)  AS THANKSGIVING,\n" +
                        "       SUM(CASE WHEN Account = 'FIRST FRUIT' THEN total ELSE 0 END)   AS `FIRST FRUITS`,\n" +
                        "       SUM(CASE WHEN Account = 'OTHERS' THEN total ELSE 0 END)        AS OTHERS,\n" +
                        "       SUM(CASE WHEN Account = 'MID-WEEK' THEN total ELSE 0 END)      AS `MID-WEEK`,\n" +
                        "       SUM(CASE WHEN Account = 'SUNDAY SCHOOL' THEN total ELSE 0 END) AS `SUNDAY SCH`,\n" +
                        "       SUM(total)                                                     as total\n" +
                        "FROM (SELECT date, month(Date) as m, monthname(Date) as month, year(Date) as year, 'income' AS grp, b.Account, credit as total\n" +
                        "      FROM accounttransactions a\n" +
                        "               INNER JOIN activities b ON a.Account = b.`Account id`\n" +
                        "      WHERE credit > 0\n" +
                        "        AND description != 'Deposit to Bank'\n" +
                        "        AND description != 'Cash Deposit'\n" +
                        "        AND description != 'Opening Balance'\n" +
                        "        AND YEAR(date) = '" + yearYear + "'\n" +
                        "\n" +
                        "      UNION ALL\n" +
                        "      SELECT date, month(Date) as m, monthname(date) as month, year(Date) as year, 'expenditure' AS grp, b.Account, debit as total\n" +
                        "      FROM accounttransactions a\n" +
                        "               INNER JOIN activities b ON a.Account = b.`Account id`\n" +
                        "      WHERE debit > 0\n" +
                        "        AND description != 'Deposit to Bank'\n" +
                        "        AND description != 'Cash Deposit'\n" +
                        "        AND description != 'Opening Balance'\n" +
                        "        AND YEAR(date) = '" + yearYear + "') s\n" +
                        "GROUP BY Date, yearMonth, grp\n" +
                        "order by m, grp desc, Date;";

                title = "For the period ending  " + yearYear;
                List<String> sql1 = new ArrayList<>();
                int farm = 3;

                testReport(sql, dynamic_report_template, "feeinvoices", 0, sql1, "", "", uploadDir + File.separator + RequestContextHolder.
                        currentRequestAttributes().getSessionId() + EXTENSION, farm, "");
            } else {
                String sql = "SELECT date, m,\n" +
                        "       concat(month, ' ', year)                                       as yearMonth,\n" +
                        "       grp,\n" +
                        "       Account,\n" +
                        " CASE WHEN grp = 'expenditure' THEN Account END                 AS AccountName, " +
                        "       SUM(CASE WHEN Account = '1ST' THEN total ELSE 0 END)           AS `1ST`,\n" +
                        "       SUM(CASE WHEN Account = '2ND' THEN total ELSE 0 END)           AS `2ND`,\n" +
                        "       SUM(CASE WHEN Account = 'TITHING' THEN total ELSE 0 END)       AS TITHES,\n" +
                        "       SUM(CASE WHEN Account = 'OFFERING' THEN total ELSE 0 END)      AS OFFERING,\n" +
                        "       SUM(CASE WHEN Account = 'THANKSGIVING' THEN total ELSE 0 END)  AS THANKSGIVING,\n" +
                        "       SUM(CASE WHEN Account = 'FIRST FRUIT' THEN total ELSE 0 END)   AS `FIRST FRUITS`,\n" +
                        "       SUM(CASE WHEN Account = 'OTHERS' THEN total ELSE 0 END)        AS OTHERS,\n" +
                        "       SUM(CASE WHEN Account = 'MID-WEEK' THEN total ELSE 0 END)      AS `MID-WEEK`,\n" +
                        "       SUM(CASE WHEN Account = 'SUNDAY SCHOOL' THEN total ELSE 0 END) AS `SUNDAY SCH`,\n" +
                        "       SUM(total)                                                     as total\n" +
                        "FROM (SELECT date, month(Date) as m, monthname(Date) as month, year(Date) as year, 'income' AS grp, b.Account, credit as total\n" +
                        "      FROM accounttransactions a\n" +
                        "               INNER JOIN activities b ON a.Account = b.`Account id`\n" +
                        "      WHERE credit > 0\n" +
                        "        AND description != 'Deposit to Bank'\n" +
                        "        AND description != 'Cash Deposit'\n" +
                        "        AND description != 'Opening Balance'\n" +
                        "        AND YEAR(date) = '" + yearYear + "'\n" +
                        "\n" +
                        "      UNION ALL\n" +
                        "      SELECT date, month(Date) as m, monthname(date) as month, year(Date) as year, 'expenditure' AS grp, b.Account, debit as total\n" +
                        "      FROM accounttransactions a\n" +
                        "               INNER JOIN activities b ON a.Account = b.`Account id`\n" +
                        "      WHERE debit > 0\n" +
                        "        AND description != 'Deposit to Bank'\n" +
                        "        AND description != 'Cash Deposit'\n" +
                        "        AND description != 'Opening Balance'\n" +
                        "        AND YEAR(date) = '" + yearYear + "') s\n" +
                        "GROUP BY Date, yearMonth, grp\n" +
                        "order by m, grp desc, Date;";

                title = "For the period ending  " + yearYear;
                List<String> sql1 = new ArrayList<>();
                int farm = 3;

                testReport(sql, dynamic_report_template, "feeinvoices", 0, sql1, "", "", uploadDir + File.separator + RequestContextHolder.
                        currentRequestAttributes().getSessionId() + EXTENSION, farm, "");
            }
        } else {
            if (type.equalsIgnoreCase("month")) {
                String sql = "SELECT date, m,\n" +
                        "       concat(month, ' ', year)                                       as yearMonth,\n" +
                        "       grp,\n" +
                        "       Account,\n" +
                        " CASE WHEN grp = 'expenditure' THEN Account END                 AS AccountName, " +
                        "       SUM(CASE WHEN Account = '1ST' THEN total ELSE 0 END)           AS `1ST`,\n" +
                        "       SUM(CASE WHEN Account = '2ND' THEN total ELSE 0 END)           AS `2ND`,\n" +
                        "       SUM(CASE WHEN Account = 'TITHING' THEN total ELSE 0 END)       AS TITHES,\n" +
                        "       SUM(CASE WHEN Account = 'OFFERING' THEN total ELSE 0 END)      AS OFFERING,\n" +
                        "       SUM(CASE WHEN Account = 'THANKSGIVING' THEN total ELSE 0 END)  AS THANKSGIVING,\n" +
                        "       SUM(CASE WHEN Account = 'FIRST FRUIT' THEN total ELSE 0 END)   AS `FIRST FRUITS`,\n" +
                        "       SUM(CASE WHEN Account = 'OTHERS' THEN total ELSE 0 END)        AS OTHERS,\n" +
                        "       SUM(CASE WHEN Account = 'MID-WEEK' THEN total ELSE 0 END)      AS `MID-WEEK`,\n" +
                        "       SUM(CASE WHEN Account = 'SUNDAY SCHOOL' THEN total ELSE 0 END) AS `SUNDAY SCH`,\n" +
                        "       SUM(total)                                                     as total\n" +
                        "FROM (SELECT date, month(Date) as m, monthname(Date) as month, year(Date) as year, 'income' AS grp, b.Account, credit as total\n" +
                        "      FROM accounttransactions a\n" +
                        "               INNER JOIN activities b ON a.Account = b.`Account id`\n" +
                        "      WHERE credit > 0\n" +
                        "        AND description != 'Deposit to Bank'\n" +
                        "        AND description != 'Cash Deposit'\n" +
                        "        AND description != 'Opening Balance'\n" +
                        "        AND YEAR(date) = '" + yearYear + "'\n" +
                        "\n" +
                        "      UNION ALL\n" +
                        "      SELECT date, month(Date) as m, monthname(date) as month, year(Date) as year, 'expenditure' AS grp, b.Account, debit as total\n" +
                        "      FROM accounttransactions a\n" +
                        "               INNER JOIN activities b ON a.Account = b.`Account id`\n" +
                        "      WHERE debit > 0\n" +
                        "        AND description != 'Deposit to Bank'\n" +
                        "        AND description != 'Cash Deposit'\n" +
                        "        AND description != 'Opening Balance'\n" +
                        "        AND YEAR(date) = '" + yearYear + "') s\n" +
                        "GROUP BY Date, yearMonth, grp\n" +
                        "order by m, grp desc, Date;";

                title = "For the period ending  " + yearYear;
                List<String> sql1 = new ArrayList<>();
                int farm = 3;

                testReport(sql, dynamic_report_template, "feeinvoices", 0, sql1, "", "", uploadDir + File.separator + RequestContextHolder.
                        currentRequestAttributes().getSessionId() + EXTENSION, farm, "");
            } else {
                String sql = "SELECT date, m,\n" +
                        "       concat(month, ' ', year)                                       as yearMonth,\n" +
                        "       grp,\n" +
                        "       Account,\n" +
                        " CASE WHEN grp = 'expenditure' THEN Account END                 AS AccountName, " +
                        "       SUM(CASE WHEN Account = '1ST' THEN total ELSE 0 END)           AS `1ST`,\n" +
                        "       SUM(CASE WHEN Account = '2ND' THEN total ELSE 0 END)           AS `2ND`,\n" +
                        "       SUM(CASE WHEN Account = 'TITHING' THEN total ELSE 0 END)       AS TITHES,\n" +
                        "       SUM(CASE WHEN Account = 'OFFERING' THEN total ELSE 0 END)      AS OFFERING,\n" +
                        "       SUM(CASE WHEN Account = 'THANKSGIVING' THEN total ELSE 0 END)  AS THANKSGIVING,\n" +
                        "       SUM(CASE WHEN Account = 'FIRST FRUIT' THEN total ELSE 0 END)   AS `FIRST FRUITS`,\n" +
                        "       SUM(CASE WHEN Account = 'OTHERS' THEN total ELSE 0 END)        AS OTHERS,\n" +
                        "       SUM(CASE WHEN Account = 'MID-WEEK' THEN total ELSE 0 END)      AS `MID-WEEK`,\n" +
                        "       SUM(CASE WHEN Account = 'SUNDAY SCHOOL' THEN total ELSE 0 END) AS `SUNDAY SCH`,\n" +
                        "       SUM(total)                                                     as total\n" +
                        "FROM (SELECT date, month(Date) as m, monthname(Date) as month, year(Date) as year, 'income' AS grp, b.Account, credit as total\n" +
                        "      FROM accounttransactions a\n" +
                        "               INNER JOIN activities b ON a.Account = b.`Account id`\n" +
                        "      WHERE credit > 0\n" +
                        "        AND description != 'Deposit to Bank'\n" +
                        "        AND description != 'Cash Deposit'\n" +
                        "        AND description != 'Opening Balance'\n" +
                        "        AND YEAR(date) = '" + yearYear + "'\n" +
                        "\n" +
                        "      UNION ALL\n" +
                        "      SELECT date, month(Date) as m, monthname(date) as month, year(Date) as year, 'expenditure' AS grp, b.Account, debit as total\n" +
                        "      FROM accounttransactions a\n" +
                        "               INNER JOIN activities b ON a.Account = b.`Account id`\n" +
                        "      WHERE debit > 0\n" +
                        "        AND description != 'Deposit to Bank'\n" +
                        "        AND description != 'Cash Deposit'\n" +
                        "        AND description != 'Opening Balance'\n" +
                        "        AND YEAR(date) = '" + yearYear + "') s\n" +
                        "GROUP BY Date, yearMonth, grp\n" +
                        "order by m, grp desc, Date;";

                title = "For the period ending  " + yearYear;
                int farm = 3;

                accountsmodel.dbaction(sql, 1, 0, 0, 0);

                testReport(sql, dynamic_report_template, "feeinvoices", 0, accountsmodel.collumns, "", "", uploadDir + File.separator + RequestContextHolder.
                        currentRequestAttributes().getSessionId() + EXTENSION, farm, "");
            }
        }

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Disposition", "attachment; filename=" +
                RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = Base64.getEncoder().encode(inFileBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    public void testReport(String SQL, String reporttemplate, String report, int month, List<String> sql1,
                           String Headermsg, String subttlestyle, String filepath, int farm, String groupcollumn) throws Exception {
        Connection conn = null;

        try {
            conn = DbConnector.getConnection();

            JasperPrint jp;

            final Map<String, Object> params = new HashMap<>();
            JasperReport jr;
            DynamicReport dr;
            dr = buildReport(SQL, reporttemplate, report, month, sql1, Headermsg, subttlestyle, groupcollumn);
            params.put("schid", "" + farm);

            jr = DynamicJasperHelper.generateJasperReport(dr, getLayoutManager(), params);
            if (conn != null) {
                jp = JasperFillManager.fillReport(jr, params, conn);
            } else {
                jp = JasperFillManager.fillReport(jr, params);
            }

            if (reporttemplate.equals("reports/TemplateReportTestWithStyles.jrxml")) {

                jp.setOrientation(OrientationEnum.PORTRAIT);
            } else {

                jp.setOrientation(OrientationEnum.LANDSCAPE);
                jp.setPageWidth(850);
                jp.setPageHeight(560);
            }

            JasperExportManager.exportReportToPdfFile(jp, filepath);


        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


    }

    private CustomExpression getCustomExpression(String groupcollumn) {
        return new CustomExpression() {

            public Object evaluate(Map fields, Map variables, Map parameters) {
                Integer amount = (Integer) fields.get(groupcollumn);
                return amount;
            }

            public String getClassName() {
                return String.class.getName();
            }

        };
    }

    public DynamicReport buildReport(String SQL, String reporttemplate, String report, int month,
                                     List<String> sql1, String Headermsg, String subttlestyle, String groupcollumn) throws Exception {

        System.out.println(SQL);
        System.out.println(report);
        Style detailStyledontshow = new Style("detail2");
        detailStyledontshow.setFont(new Font(0, null, false));
        detailStyledontshow.setBorder(Border.NO_BORDER());
        detailStyledontshow.setBorderBottom(Border.THIN());

        Style detailStyle = new Style("detail");
        detailStyle.setFont(new Font(7, null, false));
        detailStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        detailStyle.setBorder(Border.THIN());

        Style detailStylegroup = new Style("detailStylegroup");
        detailStylegroup.setFont(new Font(15, null, true));
        detailStylegroup.setHorizontalAlign(HorizontalAlign.LEFT);
        detailStylegroup.setVerticalAlign(VerticalAlign.MIDDLE);
        detailStylegroup.setBorder(Border.THIN());
        detailStylegroup.setTextColor(RGBColor.BLACK);

        Style detailStylegroupMonth = new Style("detailStylegroupmonth");
        detailStylegroupMonth.setFont(new Font(15, null, true));
        detailStylegroupMonth.setHorizontalAlign(HorizontalAlign.LEFT);
        detailStylegroupMonth.setVerticalAlign(VerticalAlign.MIDDLE);
        detailStylegroupMonth.setBackgroundColor(RGBColor.YELLOW);
        detailStylegroupMonth.setBorder(Border.THIN());
        detailStylegroupMonth.setTextColor(RGBColor.BLACK);


        Style detailStylebold = new Style("detail");
        detailStylebold.setFont(new Font(7, null, true));
        detailStylebold.setHorizontalAlign(HorizontalAlign.CENTER);
        detailStylebold.setBorder(Border.THIN());


        Style detailStyle1 = new Style("detail1");
        detailStyle1.setFont(new Font(7, null, true));
        detailStyle1.setHorizontalAlign(HorizontalAlign.LEFT);
        detailStyle1.setBorder(Border.THIN());


        Style headerStyle = new Style("header");
        headerStyle.setFont(new Font(7, null, true));
        headerStyle.setBorderBottom(Border.THIN());
        headerStyle.setBackgroundColor(RGBColor.gray);
        headerStyle.setTextColor(RGBColor.white);
        headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerStyledontshow = new Style("header2");
        headerStyledontshow.setFont(new Font(0, null, true));
        headerStyledontshow.setBorderBottom(Border.NO_BORDER());
        headerStyledontshow.setHorizontalAlign(HorizontalAlign.CENTER);
        headerStyledontshow.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyledontshow.setTransparency(Transparency.OPAQUE);

        Style titleStyle = new Style("titleStyle");
        titleStyle.setFont(new Font(15, null, true));
        titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);

        Style titleStyle1 = new Style("titleStyle1");
        titleStyle1.setFont(new Font(7, null, true));
        titleStyle1.setHorizontalAlign(HorizontalAlign.LEFT);

        Style titleStylegg = new Style("titleStylegg");
        titleStylegg.setFont(new Font(7, null, true));
        titleStylegg.setHorizontalAlign(HorizontalAlign.CENTER);

        Style subtitlestyle = new Style("titleStyle");
        subtitlestyle.setFont(new Font(7, null, true));
        subtitlestyle.setHorizontalAlign(HorizontalAlign.CENTER);


        Style subtitleStyle = Style.createBlankStyle("subtitleStyle", "subtitleParent");
        subtitleStyle.setFont(Font.GEORGIA_SMALL_BOLD);


        Style subtitleStyleParent = new Style("subtitleParent");
        subtitleStyleParent.setBackgroundColor(RGBColor.CYAN);
        subtitleStyleParent.setTransparency(Transparency.OPAQUE);

        Style groupVariables = new Style("groupVariables");
        groupVariables.setFont(new Font(7, null, true));
        groupVariables.setTextColor(RGBColor.BLUE);
        groupVariables.setHorizontalAlign(HorizontalAlign.CENTER);
        groupVariables.setVerticalAlign(VerticalAlign.MIDDLE);
        groupVariables.setBorder(Border.PEN_1_POINT());


        DynamicReportBuilder drb = new DynamicReportBuilder();
        AbstractColumn columnBranch;
        AbstractColumn doub;
        AbstractColumn totalCol;
        AbstractColumn grp = null;
        AbstractColumn monthCol = null;


        int margin = 3;


        drb.setTitleStyle(titleStyle).setTitle(Headermsg).setDetailHeight(0).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin);

        drb.setSubtitleStyle(titleStyle).setSubtitle(subttlestyle).setDetailHeight(0).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin);

        DJGroup g1 = null;
        DJGroup g2 = null;
        DJGroup g3 = null;
        GroupBuilder gb1 = new GroupBuilder();
        GroupBuilder gb2 = new GroupBuilder();
        GroupBuilder gb3 = new GroupBuilder();

        if (report.equalsIgnoreCase("feeinvoices")) {


            for (int i = 0; i < sql1.size(); i++) {

                if (sql1.get(i).replace("`", "").equalsIgnoreCase(groupcollumn) && !groupcollumn.isEmpty()) {

                    grp = ColumnBuilder.getNew().setColumnProperty(groupcollumn, String.class.getName())
                            .setTitle(groupcollumn).setWidth(0).setStyle(detailStylegroup).setHeaderStyle(headerStyle)
                            .build();
                    drb.addColumn(grp);
                    grp.setFixedWidth(true);

                } else if (sql1.get(i).replace("`", "").equalsIgnoreCase("month")) {
                    monthCol = ColumnBuilder.getNew().setColumnProperty("month", String.class.getName())
                            .setTitle("MONTH").setWidth(0).setStyle(detailStylegroup).setHeaderStyle(headerStyle)
                            .build();
                    drb.addColumn(monthCol);
                    monthCol.setFixedWidth(true);
                } else if (
                        sql1.get(i).replace("`", "").equalsIgnoreCase("DATE")) {

                    AbstractColumn date = ColumnBuilder.getNew().setColumnProperty("DATE", String.class.getName())
                            .setTitle("DATE").setWidth(70).setStyle(detailStyle).setHeaderStyle(headerStyle)
                            .build();
                    date.setFixedWidth(true);
                    drb.addColumn(date);

                }else if (
                        sql1.get(i).replace("`", "").equalsIgnoreCase("expt") ||
                                sql1.get(i).replace("`", "").equalsIgnoreCase("inct")) {

                    AbstractColumn other = ColumnBuilder.getNew().setColumnProperty(sql1.get(i).replace("`", ""), String.class.getName())
                            .setTitle(sql1.get(i).replace("`", "")).setWidth(70).setStyle(detailStyle).setHeaderStyle(headerStyle)
                            .build();
                    //drb.addColumn(date);

                }else if (sql1.get(i).replace("`", "").equalsIgnoreCase("grandTotal")){

                    AbstractColumn gt = ColumnBuilder.getNew().setColumnProperty(sql1.get(i).replace("`", ""), Float.class.getName())
                            .setTitle(sql1.get(i).replace("`", "")).setWidth(70).setPattern("#,###.###").setStyle(detailStyle).setHeaderStyle(headerStyle)
                            .build();
                    gt.setFixedWidth(true);
                    drb.addColumn(gt);

                } else if (
                        !sql1.get(i).replace("`", "").equalsIgnoreCase("type") &&
                                !sql1.get(i).replace("`", "").equalsIgnoreCase("inct") &&
                                !sql1.get(i).replace("`", "").equalsIgnoreCase("expt") &&
                                !sql1.get(i).replace("`", "").equalsIgnoreCase("month") &&
                                !sql1.get(i).replace("`", "").equalsIgnoreCase("PAIDTHROUGH") &&
                                !sql1.get(i).replace("`", "").equalsIgnoreCase("date")) {

                    doub = ColumnBuilder.getNew().setColumnProperty(sql1.get(i).replace("`", ""), Float.class.getName()).
                            setTitle(sql1.get(i).replace("`", "")).setWidth(30).setPattern("#,###.###").
                            setStyle(detailStyle).setHeaderStyle(headerStyle)
                            .build();
                    drb.addColumn(doub);

                    /*totalCol = ColumnBuilder.getNew().setColumnProperty(("grandTotal"), Float.class.getName()).
                            setTitle("Grand Total").setWidth(30).setPattern("#,###.###").
                            setStyle(detailStyle).setHeaderStyle(headerStyle)
                            .build();
                    drb.addColumn(totalCol);*/

                    if (!groupcollumn.isEmpty()) {

                        System.out.println(groupcollumn);

                        AbstractColumn finalDoub = doub;
                        g2 = gb2.setCriteriaColumn((PropertyColumn) monthCol).addFooterVariable(
                                        doub, new CustomExpression() {

                                            @Override
                                            public Object[] evaluate(Map fields, Map variables, Map parameters) {
                                                Object[] objects = new Object[2];
                                                if (finalDoub.getTitle().equalsIgnoreCase("total")) {
                                                    objects[0] =  fields.get("grandTotal");
                                                } else if(finalDoub.getTitle().equalsIgnoreCase("tithe")) {
                                                    objects[0] = "Balance";
                                                } else {
                                                    objects[0] = null;
                                                }

                                                return Arrays.stream(objects).toArray();
                                            }

                                            @Override
                                            public String getClassName() {
                                                return Double.class.getName();
                                            }
                                        }, groupVariables)
                                .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                                .setFooterHeight(20, true)
                                .setHeaderVariablesHeight(35)
                                .build();

                        g3 = gb3.setCriteriaColumn((PropertyColumn) monthCol).addFooterVariable(
                                        doub, new CustomExpression() {

                                            @Override
                                            public Object evaluate(Map fields, Map variables, Map parameters) {

                                                if (finalDoub.getTitle().equalsIgnoreCase("total")) {
                                                    return fields.get("grandTotal");
                                                } else if(finalDoub.getTitle().equalsIgnoreCase("tithe")) {
                                                    return "C/D";
                                                } else {
                                                    return null;
                                                }
                                            }

                                            @Override
                                            public String getClassName() {
                                                return Double.class.getName();
                                            }
                                        }, groupVariables)
                                .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                                .setFooterHeight(20, true)
                                .setHeaderVariablesHeight(35)
                                .build();


                        g1 = gb1.setCriteriaColumn((PropertyColumn) grp).addFooterVariable(
                                        doub, DJCalculation.SUM, groupVariables)
                                .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                                .setFooterHeight(20, true)
                                .setHeaderVariablesHeight(35)
                                .build();

                        /*AbstractColumn columnaCustomExpression = ColumnBuilder.getNew()
                                .setCustomExpression(getCustomExpression(groupcollumn))
                                .setCustomExpressionForCalculation(getCustomExpression(groupcollumn))
                                .setTitle("Total").setWidth(91)
                                .setStyle(groupVariables).setHeaderStyle(headerStyle).build();*/

                    }

                } else {
                    System.out.println(sql1.get(i));
                    columnBranch = ColumnBuilder.getNew()
                            .setColumnProperty(sql1.get(i).replace("`", ""), String.class.getName())
                            .setTitle(sql1.get(i).replace("`", "")).setWidth(25)
                            .setStyle(detailStyle).setHeaderStyle(headerStyle).build();
                    drb.addColumn(columnBranch);

                }
            }

            if (!groupcollumn.isEmpty()) {
                //drb.addGroup(g3);
                drb.addGroup(g2);
                drb.addGroup(g1);
            }


            System.out.println(drb.getColumn(5).getTitle());


        } else if (report.equals("expenditure") && month == 1) {

            AbstractColumn date = ColumnBuilder.getNew().setColumnProperty("date", String.class.getName())
                    .setTitle("Date").setWidth(45).setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();
            drb.addColumn(date);

            AbstractColumn yearmonth = ColumnBuilder.getNew().setColumnProperty("yearMonth", String.class.getName())
                    .setTitle("Year Month").setWidth(45).setStyle(detailStylegroup).setHeaderStyle(headerStyle)
                    .build();
            drb.addColumn(yearmonth);

            AbstractColumn first = ColumnBuilder.getNew().setColumnProperty("payee", String.class.getName())
                    .setTitle("Payee").setWidth(50).setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();
            drb.addColumn(first);

            AbstractColumn income = ColumnBuilder.getNew().setColumnProperty("credit", Float.class.getName())
                    .setTitle("Income").setWidth(40).setPattern("#,###.###").setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();
            drb.addColumn(income);

            AbstractColumn expenditure = ColumnBuilder.getNew().setColumnProperty("debit", Float.class.getName())
                    .setTitle("Expenditure").setWidth(45).setPattern("#,###.###").setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();
            drb.addColumn(expenditure);

            AbstractColumn total = ColumnBuilder.getNew().setColumnProperty("total", Float.class.getName())
                    .setTitle("TOTAL").setWidth(40).setPattern("#,###.###").setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            drb.addColumn(total);


            g1 = gb1.setCriteriaColumn((PropertyColumn) yearmonth).addFooterVariable(
                            total, DJCalculation.SUM, groupVariables)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                    .setStartInNewPage(true)
                    .setFooterVariablesHeight(20)
                    .setFooterHeight(50, true)
                    .setHeaderVariablesHeight(35)
                    .build();

            drb.addGroup(g1);
        } else if (report.equals("expenditure") && month == 0) {

            AbstractColumn year = ColumnBuilder.getNew().setColumnProperty("year", String.class.getName())
                    .setTitle("Year").setWidth(45).setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();
            drb.addColumn(year);

            AbstractColumn monthColumn = ColumnBuilder.getNew().setColumnProperty("month", String.class.getName())
                    .setTitle("Month").setWidth(45).setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();
            drb.addColumn(monthColumn);
/*
            AbstractColumn yearmonth = ColumnBuilder.getNew().setColumnProperty("yearMonth", String.class.getName())
                    .setTitle("Year Month").setWidth(45).setStyle(detailStylegroup).setHeaderStyle(headerStyle)
                    .build();
            drb.addColumn(yearmonth);*/

            AbstractColumn credit = ColumnBuilder.getNew().setColumnProperty("credit", Float.class.getName())
                    .setTitle("Income").setWidth(40).setPattern("#,###.###").setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();
            drb.addColumn(credit);

            AbstractColumn debit = ColumnBuilder.getNew().setColumnProperty("debit", Float.class.getName())
                    .setTitle("Expenditure").setWidth(45).setPattern("#,###.###").setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();
            drb.addColumn(debit);

            AbstractColumn total = ColumnBuilder.getNew().setColumnProperty("total", Float.class.getName())
                    .setTitle("TOTAL").setWidth(40).setPattern("#,###.###").setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            drb.addColumn(total);


            g1 = gb1.setCriteriaColumn((PropertyColumn) year).addFooterVariable(
                            total, DJCalculation.SUM, groupVariables)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                    .setStartInNewPage(true)
                    .setFooterVariablesHeight(20)
                    .setFooterHeight(50, true)
                    .setHeaderVariablesHeight(35)
                    .build();

            drb.addGroup(g1);
        } else if (report.equalsIgnoreCase("trial") && month == 1) {

            AbstractColumn yearMonth = ColumnBuilder.getNew().setColumnProperty("month", String.class.getName())
                    .setTitle("MONTH").setWidth(45).setStyle(detailStylegroupMonth).setHeaderStyle(headerStyle)
                    .build();
            drb.addColumn(yearMonth);

            AbstractColumn date = ColumnBuilder.getNew().setColumnProperty("DATE", String.class.getName())
                    .setTitle("DATE").setWidth(45).setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();
            drb.addColumn(date);

            AbstractColumn description = ColumnBuilder.getNew().setColumnProperty("description", String.class.getName())
                    .setTitle("DETAILS").setWidth(45).setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();
            drb.addColumn(description);

            AbstractColumn credit = ColumnBuilder.getNew().setColumnProperty("income", Float.class.getName())
                    .setTitle("INCOME").setWidth(45).setPattern("#,###.###").setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();
            drb.addColumn(credit);

            AbstractColumn debit = ColumnBuilder.getNew().setColumnProperty("expenditure", Float.class.getName())
                    .setTitle("EXPENDITURE").setWidth(40).setPattern("#,###.###").setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();
            drb.addColumn(debit);

            AbstractColumn balance = ColumnBuilder.getNew().setColumnProperty("balance", Float.class.getName())
                    .setTitle("BALANCE").setWidth(40).setPattern("#,###.###").setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            drb.addColumn(balance);


            g1 = gb1.setCriteriaColumn((PropertyColumn) yearMonth)
                    .addFooterVariable(
                            balance, DJCalculation.NOTHING, groupVariables)
                    .addFooterVariable(debit, DJCalculation.SUM, groupVariables)
                    .addFooterVariable(credit, DJCalculation.SUM, groupVariables)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                    .setStartInNewPage(false)
                    .setFooterVariablesHeight(20)
                    .setFooterHeight(50, true)
                    .setHeaderVariablesHeight(35)
                    .build();


            drb.addGroup(g1);
        } else if (report.equalsIgnoreCase("trial") && month == 0) {

            AbstractColumn year = ColumnBuilder.getNew().setColumnProperty("year", String.class.getName())
                    .setTitle("YEAR").setWidth(45).setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();
            drb.addColumn(year);

            AbstractColumn yearMonth = ColumnBuilder.getNew().setColumnProperty("month", String.class.getName())
                    .setTitle("MONTH").setWidth(45).setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();
            drb.addColumn(yearMonth);

            AbstractColumn credit = ColumnBuilder.getNew().setColumnProperty("credit", Float.class.getName())
                    .setTitle("DEBIT").setWidth(45).setPattern("#,###.###").setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();
            drb.addColumn(credit);

            AbstractColumn debit = ColumnBuilder.getNew().setColumnProperty("debit", Float.class.getName())
                    .setTitle("CREDIT").setWidth(40).setPattern("#,###.###").setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();
            drb.addColumn(debit);

            AbstractColumn balance = ColumnBuilder.getNew().setColumnProperty("balance", Float.class.getName())
                    .setTitle("BALANCE").setWidth(40).setPattern("#,###.###").setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            drb.addColumn(balance);


            g1 = gb1.setCriteriaColumn((PropertyColumn) year).addFooterVariable(
                            balance, DJCalculation.NOTHING, groupVariables)
                    .addFooterVariable(debit, DJCalculation.SUM, groupVariables)
                    .addFooterVariable(credit, DJCalculation.SUM, groupVariables)
                    .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                    .setStartInNewPage(false)
                    .setFooterVariablesHeight(20)
                    .setFooterHeight(50, true)
                    .setHeaderVariablesHeight(35)
                    .build();

            drb.addGroup(g1);
        }


        drb.setQuery(SQL, "sql");
        drb.setUseFullPageWidth(true);
        drb.setPageSizeAndOrientation(new Page(825, 523, false));

        drb.setTemplateFile(reporttemplate, true, true, true, false);


        return drb.build();


    }

    protected LayoutManager getLayoutManager() {
        return new ClassicLayoutManager();
    }

}
