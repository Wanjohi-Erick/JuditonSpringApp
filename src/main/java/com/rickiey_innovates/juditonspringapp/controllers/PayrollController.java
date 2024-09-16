package com.rickiey_innovates.juditonspringapp.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


import com.rickiey_innovates.juditonspringapp.models.*;
import com.rickiey_innovates.juditonspringapp.models.payrolls.DeductionModel;
import com.rickiey_innovates.juditonspringapp.models.payrolls.Deductions;
import com.rickiey_innovates.juditonspringapp.models.payrolls.Payroll;
import com.rickiey_innovates.juditonspringapp.repositories.DeductionsRepository;
import com.rickiey_innovates.juditonspringapp.repositories.PayrollRepository;
import com.rickiey_innovates.juditonspringapp.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lowagie.text.pdf.RGBColor;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.core.layout.LayoutManager;
import ar.com.fdvs.dj.domain.DJCalculation;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.GroupBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Page;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.entities.DJGroup;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import com.rickiey_innovates.juditonspringapp.DbConnector;
import com.rickiey_innovates.juditonspringapp.repositories.AllowancesRepository;


@Controller
public class PayrollController {

    @Value("${dynamic.template.report.path}")
    public String dynamicreport_template;

    @Value("${payslip.template.path}")
    public String payslip_template;

    @Value("${payslipV.template.path}")
    public String payslipV_template;

    @Value("${payee.returns.path}")
    public String paree_returns;

    @Value("${p9a.path}")
    public String p9a;

    @Value("${p10a.path}")
    public String p10a;


    @Value("${p9.path}")
    public String p9;


    @Value("${nhif.path}")
    public String nhif;

    @Value("${housing.path}")
    public String housing;


    @Value("${nita.path}")
    public String nita;

    @Value("${nssf.path}")
    public String nssf;

    @Value("${banktotals.path}")
    public String banktotals;

    @Value("${payrolltotals.path}")
    public String payrolltotals;

    @Value("${salariestobank.path}")
    public String notestothebank;

    @Value("${salariestobankexcel.path}")
    public String notestothebankexcel;


    @Value("${net.pay.path}")
    public String net_pay;

    @Value("${negative.pay.path}")
    public String negative_pay;

    @Value("${deductions.report.path}")
    public String Deductions_Report;

    @Value("${earnings.report.path}")
    public String Earnings_Report;


    @Autowired
    private AllowancesRepository allowanceRepository;

    @Autowired
    private DeductionsRepository deductionRepository;

    @Autowired
    private PayrollRepository payrollRepository;

    @Autowired
    private UserRepository userRepository;

    private Long userId() {
        return LoginController.getLoggedInUserId();
    }

    private Farm farm() {
        return userRepository.findById(userId()).orElse(null).getFarm();
    }

    private boolean isAuthorised() {
        Set<Role> roles = userRepository.findById(userId()).get().getRoles();
        boolean authorised = false;
        for (Role role : roles) {
            if (role.getName().equals(ERole.SHAREHOLDER)) {
                authorised = true;
            } else {
                authorised = false;
            }
        }
        return authorised;
    }

    @RequestMapping(value = "/api/hr/getlistofemployees", method = RequestMethod.GET)
    @ResponseBody
    public String loadlistofstudents() {

        accountsmodel.dbaction("select * from (SELECT a.id,format(round(SUM(if(b.type='EARNING' "
                + " and (date(now()) BETWEEN b.startdate AND b.enddate OR  YEAR(NOW()) "
                + " between YEAR(b.startdate) AND  YEAR(b.startdate) and 	MONTH(NOW()) "
                + " BETWEEN MONTH(b.startdate) AND MONTH(b.enddate)) "
                + " and b.status='Approved',amount,0))),1) AS amount,"
                + " case when simage='' then 'static/blankprofile.png' when  "
                + " LEFT(simage, LENGTH(1)) = '/'  then simage  else CONCAT('/',simage) END as simage,"
                + " concat(a.`payno`,' ',`fname`,' ',`sname`,' ',`surname`) Name FROM employees a "
                + " left JOIN deductionsandearningsexpirydat b ON a.id=b.payno "
                + " WHERE a.farm=" + farm().getId() + " and a.status='Active'  "
                + " GROUP BY a.id ORDER BY LPAD(LOWER(a.payno), 600,0))k ", 1, 0, 0, 0);
        return accountsmodel.jsondata;
    }


    @RequestMapping(value = "/api/hr/gettheme", method = RequestMethod.GET)
    @ResponseBody
    public String gettheme() {
        accountsmodel.dbaction("SELECT theme  from  users where id=" + userId() + "   ", 1, 0, 0, 0);
        return accountsmodel.jsondata;
    }


    @GetMapping("/api/hr/employees")
    public String employees(Model model) {

        int farm = farm().getId();

        accountsmodel.dbaction("SELECT *\n" +
                "from (SELECT s.id,\n" +
                "             name,\n" +
                "             SUBSTRING_INDEX(name, ' ', 2) as\n" +
                "                                              shortname,\n" +
                "             logo,\n" +
                "             s.address,\n" +
                "             region,\n" +
                "             phone,\n" +
                "             username,\n" +
                "             logo                          as image\n" +
                "      FROM users u\n" +
                "               inner join farm s on u.farm = s.id\n" +
                "      where sessionid = '" + RequestContextHolder.currentRequestAttributes().getSessionId() + "') d   ", 1, 0, 0, 0);

        List<accountsmodel> newListdf = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("email", newListdf.get(0).activeProperty("username").getValue());
        model.addAttribute("image", newListdf.get(0).activeProperty("image").getValue());


        accountsmodel.dbaction("SELECT *\n" +
                "FROM (\n" +
                "         SELECT\n" +
                "             a.id,\n" +
                "             Payno,\n" +
                "             CONCAT(fname, ' ', sname, ' ', surname) AS Employee,\n" +
                "             Gender,\n" +
                "             a.Status,\n" +
                "             Simage AS Image,\n" +
                "             employeetype\n" +
                "         FROM employees a\n" +
                "         WHERE a.farm = '" + farm + "'\n" +
                "         GROUP BY a.id\n" +
                "     ) a\n" +
                "ORDER BY Status;\n", 1, 0, 0, 0);

        List<accountsmodel> newListofstudents = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("employees", newListofstudents);


        accountsmodel.dbaction("SELECT * FROM (SELECT id ,earning  "
                + " FROM earnings a WHERE `farm` = '" + farm + "' )b ", 1, 0, 0, 0);
        List<accountsmodel> allownces = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("allownces", allownces);

        accountsmodel.dbaction("SELECT * FROM (SELECT id ,deduction  "
                + " FROM deductions a WHERE `farm` = '" + farm + "' )b ", 1, 0, 0, 0);
        List<accountsmodel> deductions = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("deductions", deductions);


        accountsmodel.dbaction("SELECT * from (SELECT biometricsid,"
                + " concat(`First name`,' ',`Second name`,' ',surname) teacher "
                + "	FROM teacherregistration where status='Active' and farm='" + farm + "')a", 1, 0, 0, 0);
        List<accountsmodel> teacher = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("teachers", teacher);


        accountsmodel.dbaction("SELECT * from (SELECT biometricsid,"
                + " concat(`First name`,' ',`Second name`,' ',surname) staff "
                + "	FROM subregistration where status='Active' and farm='" + farm + "')a", 1, 0, 0, 0);
        List<accountsmodel> staff = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("staff", staff);


        return "payroll/employees";
    }


    @SuppressWarnings("unchecked")
    @PostMapping(value = "/api/hr/addorupdateemployee")
    @ResponseBody
    public String addexamcategory(@RequestBody JSONObject search, HttpServletRequest request) {
        int farm = farm().getId();


        if (search.get("id") == "" || search.get("id") == null) {
            search.put("farm", farm);
            jsontosql.jsontosqlaction("employees", search, 2, "");
        } else {
            jsontosql.jsontosqlaction("employees", search, 3, "");
        }


        if (accountsmodel.response.equals("an error occured")) {
            return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";
        }

        return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";

    }


    @RequestMapping(value = "/api/hr/getemployee/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String getexamcategory(@PathVariable("id") Integer id) {
        accountsmodel.dbaction("SELECT * FROM (select * from employees where id=" + id + ")b ", 1, 0, 0, 0);
        return accountsmodel.jsondata.replace("[", "").replace("]", "");
    }


    @RequestMapping(value = "/api/hr/approveemployee/{id}", method = RequestMethod.GET)
    public String approveemployee(@PathVariable("id") Integer id, Model model) {

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();
            conn.prepareStatement("update employees set status='Active' where id=" + id + " ").execute();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }

        return "redirect:/api/hr/employees";
    }


    @RequestMapping(value = "/api/hr/activateordeactivateemployee/{id}/{status}", method = RequestMethod.GET)
    public String activateordeactivateemployee(@PathVariable("id") Integer id, @PathVariable("status") String status, Model model) {

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            conn.prepareStatement("update employees set status='" + status + "' where id=" + id + " ").execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }

        return "redirect:/api/hr/employees";

    }


    @RequestMapping(value = "/api/hr/deleteemployee/{id}", method = RequestMethod.GET)
    public String deleteexamcategory(@PathVariable("id") Integer id, Model model) {

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();
            conn.prepareStatement("delete from employees  where id=" + id + " ").execute();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }

        return "redirect:/api/hr/employees";
    }


    @GetMapping("/api/hr/allowances")
    public String allowances(Model model) {

        accountsmodel.dbaction("SELECT *\n" +
                "from (SELECT s.id,\n" +
                "             name,\n" +
                "             SUBSTRING_INDEX(name, ' ', 2) as\n" +
                "                 shortname,\n" +
                "             logo,\n" +
                "             s.address,\n" +
                "             region,\n" +
                "             phone,\n" +
                "             username\n" +
                "      FROM users u\n" +
                "               inner join farm s on u.farm = s.id\n" +
                "      where sessionid = '"+RequestContextHolder.currentRequestAttributes().getSessionId()+"') d", 1, 0, 0, 0);

        List<accountsmodel> newListdf = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("email", newListdf.get(0).activeProperty("username").getValue());
        model.addAttribute("image", newListdf.get(0).activeProperty("logo").getValue());


        accountsmodel.dbaction("SELECT * from (SELECT a.id,Earning,type,costperunit,Visible FROM earnings a"
                + " where farm='" + farm().getId() + "')a ", 1, 0, 0, 0);

        List<accountsmodel> newListofstudents = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("earnings", newListofstudents);


        return "payroll/allowances";
    }


    @PostMapping("/api/hr/addallowance")
    public String addSubject(Allowances allowance, RedirectAttributes redirectAttributes) {
        allowance.setFarm(farm());

        try {

            if (allowance.getId() == null) {
                allowanceRepository.save(allowance);
                redirectAttributes.addFlashAttribute("message", "Allowance has been saved successfully!");

            } else {

                allowanceRepository.updateEarningById(allowance.getEarning(), allowance.getType(), allowance.getCostperunit(),
                        allowance.getVisible(), allowance.getId());
                redirectAttributes.addFlashAttribute("message", "Allowance has been Updated successfully!");

            }

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addAttribute("message", e.getMessage());
        }

        return "redirect:/api/hr/allowances";
    }


    @RequestMapping(value = "api/hr/getAllowance/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String getAllowance(@PathVariable("id") Integer id) {
        accountsmodel.dbaction("SELECT * FROM (select * from earnings where id=" + id + ")b ", 1, 0, 0, 0);
        return accountsmodel.jsondata.replace("[", "").replace("]", "");
    }


    @RequestMapping(value = "/api/hr/deleteallowanceordeduction/{id}/{type}", method = RequestMethod.GET)
    public String deleteallowance(@PathVariable("id") Integer id, @PathVariable("type") String type, Model model) {

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();
            if (type.equals("allowance")) {
                conn.prepareStatement("delete from earnings  where id=" + id + " ").execute();
            } else {
                conn.prepareStatement("delete from deductions  where id=" + id + " ").execute();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }

        if (type.equals("allowance")) {
            return "redirect:/api/hr/allowances";
        } else {
            return "redirect:/api/hr/deductions";
        }
    }


    @RequestMapping(value = "/api/hr/setallowanceordeductionstatus/{id}/{type}/{status}", method = RequestMethod.GET)
    public String activateReason(@PathVariable("id") Integer id, @PathVariable("type") String type,
                                 @PathVariable("status") String status, Model model) {

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            if (type.equals("Allowance")) {
                conn.prepareStatement("update earnings set visible='" + status + "' where id=" + id + " ").execute();
            } else {
                conn.prepareStatement("update deductions set visible='" + status + "' where id=" + id + " ").execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }

        if (type.equals("Allowance")) {
            return "redirect:/api/hr/allowances";
        } else {
            return "redirect:/api/hr/deductions";
        }
    }


    @GetMapping("/api/hr/deductions")
    public String deductions(Model model) {

        accountsmodel.dbaction("SELECT *\n" +
                "from (SELECT s.id,\n" +
                "             name,\n" +
                "             SUBSTRING_INDEX(name, ' ', 2) as\n" +
                "                                                    shortname,\n" +
                "             logo,\n" +
                "             s.address,\n" +
                "             region,\n" +
                "             phone,\n" +
                "             username\n" +
                "            \n" +
                "      FROM users u\n" +
                "               inner join farm s on u.farm = s.id\n" +
                "      where sessionid = '"+RequestContextHolder.currentRequestAttributes().getSessionId()+"') d   ", 1, 0, 0, 0);

        List<accountsmodel> newListdf = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("email", newListdf.get(0).activeProperty("username").getValue());
        model.addAttribute("image", newListdf.get(0).activeProperty("logo").getValue());


        accountsmodel.dbaction("SELECT * from (SELECT a.id,Deduction,type,costperunit,Visible FROM deductions a"
                + " where farm='" + farm().getId() + "')a ", 1, 0, 0, 0);

        List<accountsmodel> newListofstudents = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("deductions", newListofstudents);


        return "payroll/deductions";
    }


    @PostMapping("/api/hr/adddeduction")
    public String adddeduction(DeductionModel deductions, RedirectAttributes redirectAttributes) {
        Deductions deduction = new Deductions();
        deduction.setFarm(farm());
        deduction.setType(deductions.getType());
        deduction.setDeduction(deductions.getDeduction());
        deduction.setCalculationtype(deductions.getCalculationtype());
        deduction.setVisible(deductions.getVisible());
        deduction.setCostperunit(deductions.getCostperunit());
        deduction.setId(deductions.getId());

        System.out.println(deduction);
        try {

            if (deduction.getId() == null) {
                deductionRepository.save(deduction);
                redirectAttributes.addFlashAttribute("message", "Deduction has been saved successfully!");

            } else {

                deductionRepository.updateDeductionById(deduction.getDeduction(), deduction.getType(),
                        deduction.getCalculationtype(), deduction.getCostperunit()
                        , deduction.getVisible(), deduction.getId());
                redirectAttributes.addFlashAttribute("message", "Deduction has been Updated successfully!");

            }

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addAttribute("message", e.getMessage());
        }

        return "redirect:/api/hr/deductions";
    }


    @RequestMapping(value = "api/hr/getDeduction/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String getDeduction(@PathVariable("id") Integer id) {
        accountsmodel.dbaction("SELECT * FROM (select id,Deduction,type,calculationtype,costperunit,"
                + " visible from deductions where id=" + id + ")b ", 1, 0, 0, 0);
        return accountsmodel.jsondata.replace("[", "").replace("]", "");
    }


    @GetMapping("/api/hr/taxbands")
    public String taxbands(Model model) {

        accountsmodel.dbaction("SELECT *\n" +
                "from (SELECT s.id,\n" +
                "             name,\n" +
                "             SUBSTRING_INDEX(name, ' ', 2) as\n" +
                "                                                    shortname,\n" +
                "            \n" +
                "             logo,\n" +
                "             s.address,\n" +
                "             region,\n" +
                "             phone,\n" +
                "             username\n" +
                "      FROM users u\n" +
                "               inner join farm s on u.farm = s.id\n" +
                "      where sessionid = '"+RequestContextHolder.currentRequestAttributes().getSessionId()+"') d   ", 1, 0, 0, 0);

        List<accountsmodel> newListdf = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("email", newListdf.get(0).activeProperty("username").getValue());
        model.addAttribute("image", newListdf.get(0).activeProperty("logo").getValue());


        accountsmodel.dbaction("SELECT * from (SELECT a.id,concat(format(low,0),' - ',format(top,2)) as Band,"
                + " concat(Ratio,' %') Ratio,Amount as Amo FROM krarates a"
                + " where farm='" + farm().getId() + "')a ", 1, 0, 0, 0);

        List<accountsmodel> newListofstudents = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("krarates", newListofstudents);


        return "payroll/kra";
    }


    @SuppressWarnings("unchecked")
    @PostMapping("/api/hr/addorupdatekra")
    @ResponseBody
    public String addkra(@RequestBody JSONObject search, HttpServletRequest request) {

        int farm = farm().getId();

        if (search.get("id") == "" || search.get("id") == null) {
            search.put("farm", farm);
            jsontosql.jsontosqlaction("krarates", search, 2, "");
        } else {
            jsontosql.jsontosqlaction("krarates", search, 3, "");
        }

        if (accountsmodel.response.equals("an error occured")) {
            return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";
        }

        return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";

    }


    @PostMapping("/api/hr/getbankamount")
    @ResponseBody
    public String getbankamount(@RequestBody JSONObject search, HttpServletRequest request) {

        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            String SQL = "";
            String SQL2 = "";
            String SQL3 = "";

            List<String> SUMMAIN = new ArrayList<String>();
            List<String> SUMSEC = new ArrayList<String>();
            List<String> nna = new ArrayList<String>();
            List<String> totaldeductions = new ArrayList<String>();


            ResultSet rs = conn.prepareStatement("SELECT e.id,e.`deduction` FROM `other deductions` a  "
                    + " inner join deductions e on e.id=a.deduction  "
                    + " where farm=" + farm + " GROUP BY e.id").executeQuery();

            while (rs.next()) {

                SUMMAIN.add("SUM(`" + rs.getString("deduction") + "`) as `" + rs.getString("deduction") + "`");
                nna.add("SUM(IF(AMOUNT> 0 AND deduction=" + rs.getString("id") + ","
                        + " (amount), 0)) AS `" + rs.getString("deduction") + "`");
                SUMSEC.add("SUM(0) AS `" + rs.getString("deduction") + "`");
                totaldeductions.add("SUM(`" + rs.getString("deduction") + "`)+");
            }


            String ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();
            String ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();
            String dd = nna.toString().replace("[", "").replace("]", "").trim();
            String totaldeduction = totaldeductions.toString().replace("[", "").replace("+]", "").replace("+,", "+").trim();

            SQL = " SELECT Payno,Taxable,Paye,Nhif,Nssf,Housing," + ddsumMAIN + ","
                    + " (nhif+nssf+housing+paye+" + totaldeduction + ") as 'Total deductions',"
                    + " round(`GROSS SALARY`-(nhif+nssf+housing+paye+" + totaldeduction + ")) as 'Net pay'"
                    + "  FROM (select employees.Payno, concat(fname,'  ',sname,'  ',surname) as name, "
                    + " `monthly pays`.`gross salary`,taxable,`kra tax` AS Paye,`monthly pays`.nhif,"
                    + " `monthly pays`.housing,`monthly pays`.nssf,"
                    + "  " + ddsumsec + "  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + search.get("id") + " and bankname='" + search.get("bank") + "' group by employees.id "

                    + " union all "

                    + " select e.Payno,0,0,0,0,0,0,0 ," + dd + " from `other deductions` a "
                    + " inner join employees e on e.id=a.`payno` "
                    + " where payroll='" + search.get("id") + "' and bankname='" + search.get("bank") + "' "
                    + " group by a.`payno`)h  group by payno";


            SUMMAIN = new ArrayList<String>();
            SUMSEC = new ArrayList<String>();
            nna = new ArrayList<String>();

            rs = conn.prepareStatement("SELECT allowance,`earning` FROM `allowances` a  "
                    + " inner join earnings e on e.id=allowance  "
                    + " where farm=" + farm + " GROUP BY allowance").executeQuery();

            while (rs.next()) {

                SUMMAIN.add("SUM(`" + rs.getString("earning") + "`) as `" + rs.getString("earning") + "`");
                nna.add("SUM(IF(AMOUNT> 0 AND allowance=" + rs.getString("allowance") + ","
                        + " (amount), 0)) AS `" + rs.getString("earning") + "`");
                SUMSEC.add("SUM(0) AS `" + rs.getString("earning") + "`");


            }


            ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();
            ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();
            dd = nna.toString().replace("[", "").replace("]", "").trim();


            SQL2 = " SELECT Payno,Name,idnu,bankname,accountnumber,branchname,`MONTH`,YEAR,"
                    + " " + ddsumMAIN + ",`GROSS SALARY` AS `Gross`"
                    + " FROM (select employees.Payno,idnu,bankname,accountnumber,branchname, "
                    + " MONTHNAME(STR_TO_DATE(`month`, '%m')) AS `MONTH`,YEAR,  employees.id, "
                    + " concat(fname,'  ',sname,'  ',surname) as name, "
                    + " `monthly pays`.`gross salary`, " + ddsumsec + ", sum(0) as aamount  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + search.get("id") + " and bankname='" + search.get("bank") + "' group by employees.id "

                    + " union all "

                    + " select Payno,`pay no`,0,0,0,0,0,0,0,0 ," + dd + ",sum(amount) from allowances a "
                    + " inner join employees e on e.id=`pay no` "
                    + " where payroll='" + search.get("id") + "' and bankname='" + search.get("bank") + "' "
                    + " group by `pay no`)h  group by payno";


            SQL3 = " Select * from (Select ifnull(SUM(`Net pay`),0) as amount from (" + SQL + ")a "
                    + " left join (" + SQL2 + ")b on a.payno=b.payno "
                    + " left join payrollpayments s on s.payroll=" + search.get("id") + " "
                    + " and bank='" + search.get("bank") + "')a ";

            System.out.println(SQL3);

            accountsmodel.dbaction(SQL3, 1, 0, 0, 0);

        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        return accountsmodel.jsondata;

    }


    @RequestMapping(value = "api/hr/getkraband/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String getkra(@PathVariable("id") Integer id) {
        accountsmodel.dbaction("SELECT * FROM (select * from krarates where id=" + id + ")b ", 1, 0, 0, 0);
        return accountsmodel.jsondata.replace("[", "").replace("]", "");
    }


    @RequestMapping(value = "/api/hr/deletekranhifornssf/{id}/{type}", method = RequestMethod.GET)
    public String deletekranhifornssf(@PathVariable("id") Integer id, @PathVariable("type") String type, Model model) {

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            if (type.equals("kra")) {
                conn.prepareStatement("delete from krarates  where id=" + id + " ").execute();
            } else if (type.equals("nhif")) {
                conn.prepareStatement("delete from nhif  where id=" + id + " ").execute();
            } else if (type.equals("nssf")) {
                conn.prepareStatement("delete from nssf  where id=" + id + " ").execute();
            } else if (type.equals("housing")) {
                conn.prepareStatement("delete from housing  where id=" + id + " ").execute();
            }


        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }

        if (type.equals("kra")) {
            return "redirect:/api/hr/taxbands";
        } else if (type.equals("nhif")) {
            return "redirect:/api/hr/nhifbands";
        } else if (type.equals("nssf")) {
            return "redirect:/api/hr/nssfbands";
        } else if (type.equals("housing")) {
            return "redirect:/api/hr/housebands";
        } else {
            return "redirect:/api/hr/nssfbands";
        }


    }


    @GetMapping("/api/hr/nhifbands")
    public String nhifbands(Model model) {

        accountsmodel.dbaction("SELECT *\n" +
                "from (SELECT s.id,\n" +
                "             name,\n" +
                "             SUBSTRING_INDEX(name, ' ', 2) as\n" +
                "                                                    shortname,\n" +
                "             logo,\n" +
                "             s.address,\n" +
                "             region,\n" +
                "             phone,\n" +
                "             username\n" +
                "      FROM users u\n" +
                "               inner join farm s on u.farm = s.id\n" +
                "      where sessionid = '"+RequestContextHolder.currentRequestAttributes().getSessionId()+"') d", 1, 0, 0, 0);

        List<accountsmodel> newListdf = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("email", newListdf.get(0).activeProperty("username").getValue());
        model.addAttribute("image", newListdf.get(0).activeProperty("logo").getValue());


        accountsmodel.dbaction("SELECT * from (SELECT a.id,concat(format(low,0),' - ',format(top,2)) as Band,concat(Ratio,' %') Ratio,"
                + " Amount as Amo FROM nhif a where farm='" + farm().getId() + "')a ", 1, 0, 0, 0);

        List<accountsmodel> newListofstudents = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("nhifrates", newListofstudents);


        return "payroll/nhif";
    }


    @SuppressWarnings("unchecked")
    @PostMapping("/api/hr/addorupdatenhif")
    @ResponseBody
    public String addnhif(@RequestBody JSONObject search, HttpServletRequest request) {

        int farm = farm().getId();

        if (search.get("id") == "" || search.get("id") == null) {
            search.put("farm", farm);
            jsontosql.jsontosqlaction("nhif", search, 2, "");
        } else {
            jsontosql.jsontosqlaction("nhif", search, 3, "");
        }

        if (accountsmodel.response.equals("an error occured")) {
            return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";
        }

        return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";

    }


    @RequestMapping(value = "api/hr/getnhifband/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String getnhif(@PathVariable("id") Integer id) {
        accountsmodel.dbaction("SELECT * FROM (select * from nhif where id=" + id + ")b ", 1, 0, 0, 0);
        return accountsmodel.jsondata.replace("[", "").replace("]", "");
    }


    @GetMapping("/api/hr/nssfbands")
    public String nssfbands(Model model) {

        accountsmodel.dbaction("SELECT *\n" +
                "from (SELECT s.id,\n" +
                "             name,\n" +
                "             SUBSTRING_INDEX(name, ' ', 2) as\n" +
                "                                                    shortname,\n" +
                "             logo,\n" +
                "             s.address,\n" +
                "             region,\n" +
                "             phone,\n" +
                "             username\n" +
                "      FROM users u\n" +
                "               inner join farm s on u.farm = s.id\n" +
                "      where sessionid = '"+RequestContextHolder.currentRequestAttributes().getSessionId()+"') d", 1, 0, 0, 0);

        List<accountsmodel> newListdf = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("email", newListdf.get(0).activeProperty("username").getValue());
        model.addAttribute("image", newListdf.get(0).activeProperty("logo").getValue());


        accountsmodel.dbaction("SELECT * from (SELECT a.id,concat(format(low,0),' - ',format(top,2)) as Band,concat(Ratio,' %') "
                + " Ratio,Amount as Amo FROM nssf a where farm='" + farm().getId() + "')a ", 1, 0, 0, 0);

        List<accountsmodel> newListofstudents = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("nssfrates", newListofstudents);


        return "payroll/nssf";
    }


    @SuppressWarnings("unchecked")
    @PostMapping("/api/hr/addorupdatenssf")
    @ResponseBody
    public String addnssf(@RequestBody JSONObject search, HttpServletRequest request) {

        int farm = farm().getId();

        if (search.get("id") == "" || search.get("id") == null) {
            search.put("farm", farm);
            jsontosql.jsontosqlaction("nssf", search, 2, "");
        } else {
            jsontosql.jsontosqlaction("nssf", search, 3, "");
        }

        if (accountsmodel.response.equals("an error occured")) {
            return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";
        }

        return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";

    }


    @RequestMapping(value = "api/hr/getnssfband/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String getnssf(@PathVariable("id") Integer id) {
        accountsmodel.dbaction("SELECT * FROM (select * from nssf where id=" + id + ")b ", 1, 0, 0, 0);
        return accountsmodel.jsondata.replace("[", "").replace("]", "");
    }


    @GetMapping("/api/hr/housebands")
    public String housebands(Model model) {

        accountsmodel.dbaction("SELECT *\n" +
                "from (SELECT s.id,\n" +
                "             name,\n" +
                "             SUBSTRING_INDEX(name, ' ', 2) as\n" +
                "                                                    shortname,\n" +
                "             logo,\n" +
                "             s.address,\n" +
                "             region,\n" +
                "             phone,\n" +
                "             username\n" +
                "      FROM users u\n" +
                "               inner join farm s on u.farm = s.id\n" +
                "      where sessionid = '"+RequestContextHolder.currentRequestAttributes().getSessionId()+"') d", 1, 0, 0, 0);

        List<accountsmodel> newListdf = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("email", newListdf.get(0).activeProperty("username").getValue());
        model.addAttribute("image", newListdf.get(0).activeProperty("logo").getValue());


        accountsmodel.dbaction("SELECT * from (SELECT a.id,concat(format(low,0),' - ',format(top,2)) as Band,"
                + " concat(Ratio,' %') Ratio,Amount as Amo FROM housing a where "
                + " farm='" + farm().getId() + "')a ", 1, 0, 0, 0);

        List<accountsmodel> newListofstudents = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("housingrates", newListofstudents);


        return "payroll/housing";
    }


    @SuppressWarnings("unchecked")
    @PostMapping("/api/hr/addorupdatehousing")
    @ResponseBody
    public String addhousing(@RequestBody JSONObject search, HttpServletRequest request) {

        int farm = farm().getId();

        if (search.get("id") == "" || search.get("id") == null) {
            search.put("farm", farm);
            jsontosql.jsontosqlaction("housing", search, 2, "");
        } else {
            jsontosql.jsontosqlaction("housing", search, 3, "");
        }

        if (accountsmodel.response.equals("an error occured")) {
            return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";
        }

        return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";

    }


    @RequestMapping(value = "api/hr/gethousingband/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String gethousing(@PathVariable("id") Integer id) {
        accountsmodel.dbaction("SELECT * FROM (select * from housing where id=" + id + ")b ", 1, 0, 0, 0);
        return accountsmodel.jsondata.replace("[", "").replace("]", "");
    }


    @GetMapping("/api/hr/leave")
    public String leave(Model model) {

        accountsmodel.dbaction("SELECT *\n" +
                "from (SELECT s.id,\n" +
                "             name,\n" +
                "             SUBSTRING_INDEX(name, ' ', 2) as\n" +
                "                                                    shortname,\n" +
                "             logo,\n" +
                "             s.address,\n" +
                "             region,\n" +
                "             phone,\n" +
                "             username\n" +
                "      FROM users u\n" +
                "               inner join farm s on u.farm = s.id\n" +
                "      where sessionid = '"+RequestContextHolder.currentRequestAttributes().getSessionId()+"') d", 1, 0, 0, 0);

        List<accountsmodel> newListdf = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("email", newListdf.get(0).activeProperty("username").getValue());
        model.addAttribute("image", newListdf.get(0).activeProperty("logo").getValue());


        accountsmodel.dbaction("SELECT * from (SELECT a.id,e.Payno,concat(`fname`,' ',`sname`,' ',surname) as Employee,"
                + " Category,concat(`from`,' - ',`to`) as Daterange, Comments,a.Status FROM `leave` a "
                + " inner join employees e on e.id=a.payno where farm='" + farm().getId() + "')a ", 1, 0, 0, 0);

        List<accountsmodel> newListofstudents = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("leave", newListofstudents);


        return "payroll/leave";
    }


    @SuppressWarnings("unchecked")
    @PostMapping("/api/hr/addorupdateleave")
    @ResponseBody
    public String addorupdateleave(@RequestBody JSONObject search, HttpServletRequest request) {

        int farm = farm().getId();

        if (search.get("id") == "" || search.get("id") == null) {
            Connection conn = null;
            try {
                conn = DbConnector.getConnection();
                ResultSet rt = conn.prepareStatement(" SELECT id  FROM `employees` where "
                        + " payno='" + search.get("payno").toString().split(" ")[0] + "' and farm=" + farm + " ").executeQuery();
                while (rt.next()) {
                    search.remove("payno");
                    search.put("payno", rt.getInt("id"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                accountsmodel.response = "an error occured";
                return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";

            } finally {
                if (conn != null) try {
                    conn.close();
                } catch (SQLException ignore) {
                }
            }


            jsontosql.jsontosqlaction("leave", search, 2, "");
        } else {
            search.remove("searchemployee");
            jsontosql.jsontosqlaction("leave", search, 3, "");
        }


        if (accountsmodel.response.equals("an error occured")) {
            return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";
        }

        return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";

    }


    @RequestMapping(value = "api/hr/getleave/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String getleave(@PathVariable("id") Integer id) {
        accountsmodel.dbaction("SELECT * FROM (select a.id,concat(b.payno,' ',`fname`,' ',`sname`,' ',`surname`) "
                + " as searchemployee,b.payno,category,`from`,`to`,comments,a.status from `leave` a "
                + " inner join employees b on b.id=a.payno where a.id=" + id + ")b ", 1, 0, 0, 0);
        return accountsmodel.jsondata.replace("[", "").replace("]", "");
    }


    @RequestMapping(value = "/api/hr/deleteleave/{id}", method = RequestMethod.GET)
    public String deleteleave(@PathVariable("id") Integer id, Model model) {

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();
            conn.prepareStatement("delete from `leave` where id=" + id + " ").execute();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }

        return "redirect:/api/hr/leave";


    }


    @RequestMapping(value = "/api/hr/approveordeclineleave/{id}/{status}", method = RequestMethod.GET)
    public String activateReason(@PathVariable("id") Integer id, @PathVariable("status") String status, Model model) {

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            conn.prepareStatement("update `leave` set status='" + status + "' where id=" + id + " ").execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }

        return "redirect:/api/hr/leave";

    }


    @GetMapping("/api/hr/settings")
    public String hrsettings(Model model) {

        accountsmodel.dbaction("SELECT *\n" +
                "from (SELECT s.id,\n" +
                "             name,\n" +
                "             SUBSTRING_INDEX(name, ' ', 2) as\n" +
                "                                                    shortname,\n" +
                "             logo,\n" +
                "             s.address,\n" +
                "             region,\n" +
                "             phone,\n" +
                "             username\n" +
                "      FROM users u\n" +
                "               inner join farm s on u.farm = s.id\n" +
                "      where sessionid = '"+RequestContextHolder.currentRequestAttributes().getSessionId()+"') d", 1, 0, 0, 0);

        List<accountsmodel> newListdf = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("email", newListdf.get(0).activeProperty("username").getValue());
        model.addAttribute("image", newListdf.get(0).activeProperty("logo").getValue());


        accountsmodel.dbaction("SELECT * from (SELECT Employercode,TaxRelief,payeeoption,nhifoption,usebiometricsforpayroll,\n" +
                "                      nssfoption from farm WHERE id=" + farm().getId() + ")a ", 1, 0, 0, 0);

        List<accountsmodel> newListofstudents = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("leave", newListofstudents);

        model.addAttribute("Employercode", newListofstudents.get(0).activeProperty("Employercode").getValue());
        model.addAttribute("TaxRelief", newListofstudents.get(0).activeProperty("TaxRelief").getValue());
        model.addAttribute("payeeoption", newListofstudents.get(0).activeProperty("payeeoption").getValue());
        model.addAttribute("nhifoption", newListofstudents.get(0).activeProperty("nhifoption").getValue());
        model.addAttribute("nssfoption", newListofstudents.get(0).activeProperty("nssfoption").getValue());
        model.addAttribute("usebiometricsforpayroll", newListofstudents.get(0).activeProperty("usebiometricsforpayroll").getValue());


        return "payroll/settings";
    }


    @RequestMapping(value = "api/hr/getpayrollsettings", method = RequestMethod.POST)
    @ResponseBody
    public String getpayrollsettings() {
        accountsmodel.dbaction("SELECT * from (SELECT Employercode,TaxRelief,payeeoption,nssfoption from farm WHERE id='"+farm().getId()+"')a\n", 1, 0, 0, 0);
        return accountsmodel.jsondata.replace("[", "").replace("]", "");
    }


    @RequestMapping(value = "api/hr/updatepayeeoption", method = RequestMethod.POST)
    @ResponseBody
    public String updatepayeeoption(@RequestBody JSONObject search, HttpServletRequest request) {

        String response = "";

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();
            conn.prepareStatement("update farm set payeeoption='" + search.get("value") + "' where  id=" + farm().getId() + " ").execute();
            response = "Operation Successfull";
        } catch (Exception e) {
            e.printStackTrace();
            response = "an error occured";
            return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";

        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }

        return accountsmodel.jsondata = "[{\"querystatus\" : \"" + response + "\"}]";

    }


    @RequestMapping(value = "api/hr/updatenhifoption", method = RequestMethod.POST)
    @ResponseBody
    public String updatenhifoption(@RequestBody JSONObject search, HttpServletRequest request) {

        String response = "";

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();
            conn.prepareStatement("update farm set nhifoption='" + search.get("value") + "' where  id=" + farm().getId() + " ").execute();
            response = "Operation Successfull";
        } catch (Exception e) {
            e.printStackTrace();
            response = "an error occured";
            return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";

        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }

        return accountsmodel.jsondata = "[{\"querystatus\" : \"" + response + "\"}]";

    }


    @RequestMapping(value = "api/hr/updatebiometricsoption", method = RequestMethod.POST)
    @ResponseBody
    public String updatebiometricsoption(@RequestBody JSONObject search, HttpServletRequest request) {

        String response = "";

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();
            conn.prepareStatement("update farm set usebiometricsforpayroll='" + search.get("value") + "' where  id=" + farm().getId() + " ").execute();
            response = "Operation Successfull";
        } catch (Exception e) {
            e.printStackTrace();
            response = "an error occured";
            return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";

        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }

        return accountsmodel.jsondata = "[{\"querystatus\" : \"" + response + "\"}]";

    }


    @RequestMapping(value = "api/hr/updateusertheme", method = RequestMethod.POST)
    @ResponseBody
    public String updateusertheme(@RequestBody JSONObject search, HttpServletRequest request) {

        String response = "";

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();


            conn.prepareStatement("update users set theme='" + search.get("value") + "' where  id=" + userId() + " ").execute();
            response = "Operation Successfull";
        } catch (Exception e) {
            e.printStackTrace();
            response = "an error occured";
            return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";

        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }

        return accountsmodel.jsondata = "[{\"querystatus\" : \"" + response + "\"}]";

    }


    @RequestMapping(value = "api/hr/updatenssfoption", method = RequestMethod.POST)
    @ResponseBody
    public String updatenssfoption(@RequestBody JSONObject search, HttpServletRequest request) {

        String response = "";

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();
            conn.prepareStatement("update farm set nssfoption='" + search.get("value") + "' where  id=" + farm().getId() + " ").execute();
            response = "Operation Successfull";
        } catch (Exception e) {
            e.printStackTrace();
            response = "an error occured";
            return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";

        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }

        return accountsmodel.jsondata = "[{\"querystatus\" : \"" + response + "\"}]";

    }


    @RequestMapping(value = "api/hr/updateotherpayrosettings", method = RequestMethod.POST)
    @ResponseBody
    public String updateotherpayrosettings(@RequestBody JSONObject search, HttpServletRequest request) {

        String response = "";

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();
            if (search.get("type").equals("taxnu")) {
                conn.prepareStatement("update farm set Employercode='" + search.get("value") + "' where  id=" + farm().getId() + " ").execute();
            } else if (search.get("type").equals("taxrelief")) {
                conn.prepareStatement("update farm set TaxRelief='" + search.get("value") + "' where  id=" + farm().getId() + " ").execute();
            }

            response = "Operation Successfull";
        } catch (Exception e) {
            e.printStackTrace();
            response = "an error occured";
            return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";

        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }

        return accountsmodel.jsondata = "[{\"querystatus\" : \"" + response + "\"}]";

    }


    @GetMapping("/api/hr/payrolls")
    public String payrolls(Model model) {

        int farm = farm().getId();

        accountsmodel.dbaction("SELECT *\n" +
                "from (SELECT s.id,\n" +
                "             name,\n" +
                "             SUBSTRING_INDEX(name, ' ', 2) as\n" +
                "                                                    shortname,\n" +
                "             logo,\n" +
                "             s.address,\n" +
                "             region,\n" +
                "             phone,\n" +
                "             username,\n" +
                "             logo               as image\n" +
                "      FROM users u\n" +
                "               inner join farm s on u.farm = s.id\n" +
                "      where sessionid = '" + RequestContextHolder.currentRequestAttributes().getSessionId() + "') d", 1, 0, 0, 0);

        List<accountsmodel> newListdf = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("email", newListdf.get(0).activeProperty("username").getValue());
        model.addAttribute("image", newListdf.get(0).activeProperty("image").getValue());

        accountsmodel.dbaction("SELECT *\n" +
                "from (SELECT *,\n" +
                "             case\n" +
                "                 when SIGN(`change`) = '-' then\n" +
                "                     CONCAT('-', IFNULL(ROUND(`change` / ABS(Amt - `change`) * 100), 0), '%')\n" +
                "                 when `CHANGE` = Amt then ''\n" +
                "                 ELSE CONCAT('+', IFNULL(ROUND(`change` / ABS(Amt - `change`) * 100), 0), '%')\n" +
                "                 end AS percentage\n" +
                "      FROM (SELECT a.*, a.Amt - ifnull(s.amount, 0) as `Change`\n" +
                "            FROM (SELECT a.id,\n" +
                "                         concat(MONTHNAME(DATE), ' ', YEAR(DATE)) payroll,\n" +
                "                         Date,\n" +
                "                         COUNT(*)                       AS        Employees,\n" +
                "                         ifnull(SUM(`gross salary`), 0) AS        Amt,\n" +
                "                         a.Status\n" +
                "                  FROM payrolls a\n" +
                "                           left JOIN `monthly pays` b ON a.id = b.payroll\n" +
                "                  where farm = " + farm().getId() + "\n" +
                "                  GROUP BY a.id\n" +
                "                  ORDER BY date) a\n" +
                "                     LEFT JOIN (SELECT a.id,\n" +
                "                                       Date,\n" +
                "                                       COUNT(*)                       AS Employees,\n" +
                "                                       ifnull(SUM(`gross salary`), 0) AS\n" +
                "                                                                         Amount,\n" +
                "                                       a.Status\n" +
                "                                FROM payrolls a\n" +
                "                                         left JOIN `monthly pays` b ON a.id = b.payroll\n" +
                "                                where farm = " + farm().getId() + "\n" +
                "                                GROUP BY a.id\n" +
                "                                ORDER BY date) s ON (a.id = s.id + 1)) a) a\n" +
                "ORDER BY DATE desc", 1, 0, 0, 0);


        List<accountsmodel> newListofstudents = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("payrolls", newListofstudents);


        accountsmodel.dbaction("SELECT *\n" +
                "from (SELECT DISTINCT bankname as id, bankname\n" +
                "      FROM `employees`\n" +
                "      where farm = " + farm + ") a", 1, 0, 0, 0);
        List<accountsmodel> bankaccounts = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("bankname", bankaccounts);

        accountsmodel.dbaction("SELECT * FROM (SELECT id ,earning  FROM earnings a WHERE `farm` = '" + farm + "' )b ", 1, 0, 0, 0);
        List<accountsmodel> allownces = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("allownces", allownces);

        accountsmodel.dbaction("SELECT * FROM (SELECT id ,deduction  FROM deductions a WHERE `farm` = '" + farm + "' )b ", 1, 0, 0, 0);
        List<accountsmodel> deductions = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("deductions", deductions);


        return "payroll/payrolls";
    }


    @PostMapping("/api/hr/addpayroll")
    public String addpayroll(Payroll payroll, RedirectAttributes redirectAttributes) {
        payroll.setFarm(farm());
        payroll.setStatus("Pending");

        try {

            if (payroll.getId() == null) {
                payrollRepository.save(payroll);
                redirectAttributes.addFlashAttribute("message", "Payroll has been saved successfully!");

            } else {
                payrollRepository.updateEarningById(String.valueOf(payroll.getDate()), payroll.getId());
                redirectAttributes.addFlashAttribute("message", "Payroll has been Updated successfully!");
            }

        } catch (Exception e) {
            redirectAttributes.addAttribute("message", e.getMessage());
        }

        return "redirect:/api/hr/payrolls";
    }


    @RequestMapping(value = "api/hr/getPayroll/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String getPayroll(@PathVariable("id") Integer id) {
        accountsmodel.dbaction("SELECT * FROM (select * from payrolls where id=" + id + ")b ", 1, 0, 0, 0);
        return accountsmodel.jsondata.replace("[", "").replace("]", "");
    }


    @RequestMapping(value = "/api/hr/deletepayroll/{id}", method = RequestMethod.GET)
    public String deleteallowance(@PathVariable("id") Integer id, Model model) {

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();
            conn.prepareStatement("delete from payrolls  where id=" + id + " ").execute();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }

        return "redirect:/api/hr/payrolls";

    }


    @RequestMapping(value = "/api/hr/setpayrollstatus/{id}/{status}", method = RequestMethod.GET)
    public String setpayrollstatus(@PathVariable("id") Integer id,
                                   @PathVariable("status") String status, Model model) {

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            conn.prepareStatement("update payrolls set status='" + status + "' where id=" + id + " ").execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }

        return "redirect:/api/hr/payrolls";

    }


    @RequestMapping(value = "/api/hr/getemployeegrosssalary/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getemployeegrosssalary(@PathVariable("id") Integer id, Model model) {

        accountsmodel.dbaction("select * from (select a.id,FORMAT("
                + " sum(if(b.type='EARNING' and b.status='Approved',amount,0)),1) as amount,"
                + " case when simage='' then 'https://www.shutterstock.com/image-vector/"
                + " male-user-icon-vector-260nw-175066871.jpg' "
                + " when  LEFT(simage, LENGTH(1)) = '/'  then simage  "
                + " else CONCAT('/',simage) END as simage,paye,nhif,nssf,"
                + " concat(a.`payno`,' ',`fname`,' ',`sname`,' ',`surname`) Name from `employees` a "
                + " Left JOIN deductionsandearningsexpirydat b on a.id=b.payno "
                + " where a.id=" + id + " AND (date(now()) BETWEEN b.startdate AND b.enddate OR  "
                + " YEAR(NOW()) between YEAR(b.startdate) AND  YEAR(b.startdate) and "
                + "	MONTH(NOW()) BETWEEN MONTH(b.startdate) AND MONTH(b.enddate)) group by a.id)k ", 1, 0, 0, 0);

        return accountsmodel.jsondata;
    }


    @RequestMapping(value = "api/hr/getemployeeallowances", method = RequestMethod.POST)
    @ResponseBody
    public String getemployeepayslip(@RequestBody JSONObject search, HttpServletRequest request) {

        accountsmodel.dbaction("SELECT * FROM (SELECT b.id,e.earning AS Item,Amount,Status,'' as Action FROM "
                + " deductionsandearningsexpirydat b "
                + " inner join earnings e on e.id=b.item WHERE `PAYNO` = '" + search.get("id") + "' "
                + " AND b.TYPE='EARNING' )b ", 1, 0, 0, 0);

        return accountsmodel.jsondata;

    }


    @RequestMapping(value = "api/hr/getemployeedeductions", method = RequestMethod.POST)
    @ResponseBody
    public String getemployeedeductions(@RequestBody JSONObject search, HttpServletRequest request) {

        accountsmodel.dbaction("SELECT * FROM (SELECT b.id,d.Deduction,Amount,Status,'' as Action FROM deductionsandearningsexpirydat b "
                + " inner join deductions d on d.id=b.item WHERE `PAYNO` = '" + search.get("id") + "' "
                + " AND b.TYPE='DEDUCTION' )b ", 1, 0, 0, 0);

        return accountsmodel.jsondata;

    }


    @SuppressWarnings("unchecked")
    @PostMapping("/api/hr/addorupdatemployeeallowanceordeduction")
    @ResponseBody
    public String addorupdatemployeeallowanceordeduction(@RequestBody JSONObject search, HttpServletRequest request) {

        if (search.get("reccurrent").equals("1")) {
            search.remove("enddate");
            search.put("enddate", "2050-01-01");
        }


        if (search.get("id") == "" || search.get("id") == null) {
            jsontosql.jsontosqlaction("deductionsandearningsexpirydat", search, 2, "");
        } else {
            search.remove("payno");
            jsontosql.jsontosqlaction("deductionsandearningsexpirydat", search, 3, "");
        }


        if (accountsmodel.response.equals("an error occured")) {
            return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";
        }

        return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";

    }


    @RequestMapping(value = "/api/hr/getemployeeallowanceordeduction/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String getemployeeallowance(@PathVariable("id") Integer id) {
        accountsmodel.dbaction("SELECT * FROM (SELECT * FROM deductionsandearningsexpirydat where id=" + id + ")b ", 1, 0, 0, 0);
        return accountsmodel.jsondata.replace("[", "").replace("]", "");
    }


    @RequestMapping(value = "/api/hr/deleteemployeeallowanceordeduction/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String deleteemployeeallowanceordeduction(@PathVariable("id") Integer id, Model model) {

        String response = "";
        Connection conn = null;
        try {
            conn = DbConnector.getConnection();
            conn.prepareStatement("delete from deductionsandearningsexpirydat  where id=" + id + " ").execute();
            response = "Operation successful";
        } catch (Exception e) {
            e.printStackTrace();
            response = "An error occured";

        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }

        return response;
    }


    @RequestMapping(value = "/api/hr/approverefundorrejectalowanceordeduction/{id}/{status}", method = RequestMethod.GET)
    @ResponseBody
    public String approverefundorrejectalowanceordeduction(@PathVariable("id") Integer id,
                                                           @PathVariable("status") String status, Model model) {
        String response = "";
        if (!isAuthorised()) {
            response = "You are not allowed to Approve/Disaprove Employee details"
                    + " Please contact the administrator for permissions";
            return response;
        }
        Connection conn = null;
        try {
            conn = DbConnector.getConnection();
            conn.prepareStatement("update deductionsandearningsexpirydat set status='" + status + "' where id=" + id + " ").execute();
            response = "Operation successful";

        } catch (Exception e) {
            e.printStackTrace();
            response = "An error occured";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }

        return accountsmodel.jsondata = "[{\"querystatus\" : \"" + response + "\"}]";

    }


    @GetMapping("/api/hr/employeeattendance/{id}")
    public String employeeattendance(Model model) {

        int farm = farm().getId();

        accountsmodel.dbaction("SELECT *\n" +
                "from (SELECT s.id,\n" +
                "             name,\n" +
                "             SUBSTRING_INDEX(name, ' ', 2) as\n" +
                "                                                    shortname,\n" +
                "             logo,\n" +
                "             s.address,\n" +
                "             region,\n" +
                "             phone,\n" +
                "             username\n" +
                "      FROM users u\n" +
                "               inner join farm s on u.farm = s.id\n" +
                "      where sessionid = '"+RequestContextHolder.currentRequestAttributes().getSessionId()+"') d   ", 1, 0, 0, 0);

        List<accountsmodel> newListdf = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("email", newListdf.get(0).activeProperty("username").getValue());
        model.addAttribute("image", newListdf.get(0).activeProperty("logo").getValue());


        accountsmodel.dbaction("SELECT * from (SELECT a.id,Payno,concat(fname,' ',sname,' ',surname) Employee,"
                + " Gender,Status,paymeth as Category,Simage as Image FROM employees a"
                + " where farm='" + farm + "' )a order by status ", 1, 0, 0, 0);

        return "payroll/employeeattendance";
    }


    @RequestMapping(value = "api/hr/chargeorexcemptemployeepayee", method = RequestMethod.POST)
    @ResponseBody
    public String chargeorexcemptemployeepayee(@RequestBody JSONObject search, HttpServletRequest request) {

        String response = "";

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();
            conn.prepareStatement("update employees set PAYE='" + search.get("value") + "' where  id=" + search.get("id") + " ").execute();
            response = "Operation Successfull";
        } catch (Exception e) {
            e.printStackTrace();
            response = "an error occured";
            return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";

        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }

        return accountsmodel.jsondata = "[{\"querystatus\" : \"" + response + "\"}]";

    }


    @RequestMapping(value = "api/hr/chargeorexcemptemployeenhif", method = RequestMethod.POST)
    @ResponseBody
    public String chargeorexcemptemployeenhif(@RequestBody JSONObject search, HttpServletRequest request) {

        String response = "";

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();
            conn.prepareStatement("update employees set nhif='" + search.get("value") + "' where  id=" + search.get("id") + " ").execute();
            response = "Operation Successfull";
        } catch (Exception e) {
            e.printStackTrace();
            response = "an error occured";
            return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";

        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }

        return accountsmodel.jsondata = "[{\"querystatus\" : \"" + response + "\"}]";

    }


    @RequestMapping(value = "api/hr/chargeorexcemptemployeenssf", method = RequestMethod.POST)
    @ResponseBody
    public String chargeorexcemptemployeenssf(@RequestBody JSONObject search, HttpServletRequest request) {

        String response = "";

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();
            conn.prepareStatement("update employees set nssf='" + search.get("value") + "' where  id=" + search.get("id") + " ").execute();
            response = "Operation Successfull";
        } catch (Exception e) {
            e.printStackTrace();
            response = "an error occured";
            return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";

        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }

        return accountsmodel.jsondata = "[{\"querystatus\" : \"" + response + "\"}]";

    }


    @GetMapping("/api/hr/proccesspayroll/{id}")
    public String proccesspayroll(@PathVariable("id") Integer id, Model model) {

        int farm = farm().getId();


        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            int month = 0;
            int year = 0;
            String date = "";

            int prevmonth = 0;
            int prevyear = 0;


            int usebiometricsforpayroll = 0;

            ResultSet payrolldetails = conn.prepareStatement("SELECT payeeoption,"
                    + " YEAR(LAST_DAY(date - INTERVAL 1 MONTH)) AS prevyear,"
                    + " MONTH(LAST_DAY(date - INTERVAL 1 MONTH)) AS prevmonth,nhifoption"
                    + ",nssfoption,usebiometricsforpayroll,date,month(date) month,year(date) year from payrolls a "
                    + " inner join farm s on s.id=a.farm where a.id='" + id + "'").executeQuery();
            while (payrolldetails.next()) {

                month = payrolldetails.getInt("month");
                year = payrolldetails.getInt("year");
                date = payrolldetails.getString("date");

                prevmonth = payrolldetails.getInt("prevmonth");
                prevyear = payrolldetails.getInt("prevyear");

                usebiometricsforpayroll = payrolldetails.getInt("usebiometricsforpayroll");
                ;

            }


            conn.prepareStatement("delete  from `monthly pays` where payroll='" + id + "'  ;"

                    + " delete  from `allowances` where payroll='" + id + "'  ;"

                    + " delete   from `other deductions`  where payroll='" + id + "' ;"


                    // Reading based deductions

                    + " insert into `other deductions`(payroll,`payno`,`deduction`,`amount`,`date`)"
                    + " SELECT '" + id + "',a.id,item,round(unitsused*costperunit,2) AS amount,'" + date + "' FROM ("
                    + " SELECT id,c.item,IFNULL(IFNULL(c.readings,0)-IFNULL(b.readings,0),0) AS unitsused FROM  "
                    + " (SELECT id FROM employees e WHERE farm=" + farm + " AND STATUS='active')a "
                    + " LEFT JOIN (SELECT item,employeeid,readings from readingbaseddeductions "
                    + " WHERE (MONTH=" + prevmonth + "  and YEAR=" + prevyear + ")  "
                    + " GROUP BY employeeid,item)b ON a.id=b.employeeid "
                    + " left JOIN (SELECT item,employeeid,readings from readingbaseddeductions "
                    + " WHERE  (MONTH=" + month + " and YEAR=" + year + ") "
                    + " GROUP BY employeeid,item)c ON a.id=c.employeeid AND b.item=c.item "
                    + " GROUP BY id,c.item)a "
                    + " INNER JOIN deductions d ON d.id=a.item  HAVING amount>0 ; "


                    // Reading based allowances

                    + " insert into `allowances`(payroll,`pay no`,`allowance`,`amount`,`date`)"
                    + " SELECT '" + id + "',a.id,item,round(unitsused*costperunit,2) AS amount,'" + date + "' FROM ("
                    + " SELECT id,c.item,IFNULL(IFNULL(c.readings,0)-IFNULL(b.readings,0),0) AS unitsused FROM  "
                    + " (SELECT id FROM employees e WHERE farm=" + farm + " AND STATUS='active')a "
                    + " LEFT JOIN (SELECT item,employeeid,readings from readingbasedallowances "
                    + " WHERE (MONTH=" + prevmonth + "  and YEAR=" + prevyear + ")  "
                    + " GROUP BY employeeid,item)b ON a.id=b.employeeid "
                    + " left JOIN (SELECT item,employeeid,readings from readingbasedallowances "
                    + " WHERE  (MONTH=" + month + " and YEAR=" + year + ") "
                    + " GROUP BY employeeid,item)c ON a.id=c.employeeid AND b.item=c.item "
                    + " GROUP BY id,c.item)a "
                    + " INNER JOIN earnings d ON d.id=a.item  HAVING amount>0 ; "


                    // other allowances

                    + " insert into `allowances`(payroll,`PAY NO`,`ALLOWANCE`,`amount`,`date`)"
                    + " SELECT '" + id + "',e.id,item,amount,'" + date + "' AS date FROM deductionsandearningsexpirydat d"
                    + " INNER JOIN employees e ON d.payno=e.id WHERE farm=" + farm + ""
                    + " and e.status='Active' AND '" + date + "' BETWEEN "
                    + " (last_day(d.startdate - interval 1 month)+interval 1 DAY) AND LAST_DAY(d.enddate)"
                    + " AND d.`status`='Approved' AND TYPE='EARNING' ;"


                    // other deductions

                    + " insert into `other deductions`(payroll,`payno`,`deduction`,`amount`,`date`)"
                    + " SELECT '" + id + "',e.id,item,amount,'" + date + "' AS date FROM deductionsandearningsexpirydat d"
                    + " INNER JOIN employees e ON d.payno=e.id WHERE farm=" + farm + ""
                    + " and e.status='Active' AND '" + date + "' BETWEEN "
                    + " (last_day(d.startdate - interval 1 month)+interval 1 DAY) AND LAST_DAY(d.enddate)"
                    + " AND d.`status`='Approved' AND TYPE='DEDUCTION' ;"


                    // insert employees into monthly pays

                    + " insert ignore into `monthly pays`(payroll,`payno`,`gross salary`,`month`,`year`)  "
                    + " SELECT '" + id + "',e.id,ifnull(sum(amount),0) AS amount," + month + "," + year + " FROM allowances a "
                    + " INNER JOIN employees e ON a.`pay no`=e.id WHERE  payroll=" + id + " "
                    + " and e.status='Active' GROUP BY e.id;	"

                    // update Nhif
                    + " UPDATE  `monthly pays` m "
                    + " INNER JOIN employees e ON m.payno=e.id AND payroll=" + id + ""
                    + " inner JOIN nhif n ON  n.farm=e.farm "
                    + " INNER JOIN farm s ON  s.id=e.farm "
                    + " SET m.nhif=case when s.nhifoption=0 then "
                    + " ROUND((`GROSS SALARY`/100)*ratio) ELSE n.amount end "
                    + " WHERE m.payroll=" + id + " AND e.nhif=1 and round(m.`GROSS SALARY`) BETWEEN low AND top;"


                    // update Nssf
                    + " UPDATE  `monthly pays` m "
                    + " INNER JOIN employees e ON m.payno=e.id AND payroll=" + id + ""
                    + " INNER JOIN nssf n ON  n.farm=e.farm "
                    + " INNER JOIN farm s ON  s.id=e.farm "
                    + " SET m.nssf=case when n.nssfoptionperband=0 then "
                    + " ROUND((`GROSS SALARY`/100)*ratio) ELSE n.amount end "
                    + " WHERE m.payroll=" + id + " AND e.nssf=1 and round(m.`GROSS SALARY`) BETWEEN low AND top;"

                    // update Housing
                    + " UPDATE  `monthly pays` m "
                    + " INNER JOIN employees e ON m.payno=e.id AND payroll=" + id + ""
                    + " INNER JOIN housing n ON  n.farm=e.farm "
                    + " INNER JOIN farm s ON  s.id=e.farm "
                    + " SET m.housing=case when n.nssfoptionperband=0 then "
                    + " ROUND(((`GROSS SALARY`/100)*ratio)+0.1) ELSE n.amount end "
                    + " WHERE m.payroll=" + id + " and e.housinglev=1 and round(m.`GROSS SALARY`) BETWEEN low AND top;"


                    // update Taxable with housed
                    + " UPDATE  `monthly pays` m "
                    + " INNER JOIN employees e ON m.payno=e.id AND payroll=" + id + ""
                    + " INNER JOIN farm s ON s.id=e.farm"
                    + " SET TAXABLE=case when housed=1  then round(`gross salary`*1.15) "
                    + " ELSE  `gross salary` end WHERE payroll=" + id + ";"

                    // update Taxable with health insurance
                    + " UPDATE  `monthly pays` m "
                    + " INNER JOIN employees e ON m.payno=e.id AND payroll=" + id + ""
                    + " INNER JOIN (SELECT payno,round(SUM(amount)*0.15) AS amount FROM "
                    + " deductionsandearningsexpirydat t "
                    + " INNER JOIN deductions d ON t.item=d.id WHERE t.`TYPE`='DEDUCTION' "
                    + " AND calculationtype=1 GROUP BY payno)s on s.payno=m.payno and m.payroll=" + id + ""
                    + " SET TAXABLE=(TAXABLE-s.amount) WHERE e.housed=1 and payroll=" + id + ";"

                    // Deduct nssf from Taxable
                    + " UPDATE  `monthly pays` m "
                    + " INNER JOIN employees e ON m.payno=e.id AND payroll=" + id + " "
                    + " INNER JOIN farm s ON s.id=e.farm"
                    + " SET TAXABLE=case when s.payeeoption=0 AND paye=1 "
                    + " then (TAXABLE-m.nssf) ELSE  TAXABLE end WHERE payroll=" + id + ";"


                    // update Paye
                    + " UPDATE `monthly pays` m "
                    + " INNER JOIN (SELECT a.payno,case when round(SUM(tax)-TaxRelief)>0 then "
                    + " round(SUM(tax)-TaxRelief) ELSE 0 end AS tax FROM ("
                    + " SELECT payno,farm,case "
                    + " when taxable>top then round((top-low)*ratio/100,2)"
                    + " when taxable < top && taxable > low then  ROUND((taxable-low)*(ratio/100),2)  "
                    + " ELSE 0 END AS tax from krarates k"
                    + " cross JOIN  `monthly pays` m  "
                    + " WHERE payroll=" + id + " AND taxable>0 and farm=" + farm + " )a "
                    + " INNER JOIN farm s ON s.id=a.farm"
                    + " INNER JOIN employees e ON a.payno=e.id where e.paye=1"
                    + " GROUP by a.payno)b ON m.payno=b.payno"
                    + " SET `kra tax`=tax WHERE m.payroll=" + id + " ; "


                    + " update payrolls set status='Processed' where id=" + id + "").execute();


        } catch (Exception e) {
            e.printStackTrace();
            return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";

        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        return "redirect:/api/hr/payrolls";

    }


    @RequestMapping(value = "/api/hr/getbulkinsertvalues", method = RequestMethod.POST)
    @ResponseBody
    public String getbulkinsertvalues(@RequestBody JSONObject search, HttpServletRequest request) {

        accountsmodel.dbaction("select * from (SELECT p.id,p.Payno,"
                + " CONCAT(p.fname,' ',p.sname,' ',surname) AS Name, "
                + " IFNULL(readings,'') Readings FROM employees p  "
                + " left JOIN (SELECT * FROM readingbasedallowances "
                + " WHERE MONTH='" + search.get("month") + "' AND YEAR='" + search.get("year") + "' "
                + " AND item='" + search.get("item") + "') a ON a.employeeid=p.id "
                + " left JOIN earnings e ON a.item=e.id WHERE p.farm=" + farm().getId() + ")k ", 1, 0, 0, 0);

        return accountsmodel.jsondata;
    }


    @PostMapping(value = "/api/hr/postbulkearnings")
    @ResponseBody
    public String postbulkearnings(@RequestBody JSONObject search, HttpServletRequest request) {

        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            List<String> array = (List<String>) search.get("values");


            conn.prepareStatement("DROP TABLE IF EXISTS "
                    + "  `" + userId() + "_hr_" + farm + "`;"

                    + "    CREATE TABLE `" + userId() + "_hr_" + farm + "` ( "
                    + "    employeeid INT NOT NULL DEFAULT 0, "
                    + "    item INT NOT NULL  DEFAULT 0, "
                    + "    month INT NOT NULL  DEFAULT 0, "
                    + "    year INT NOT NULL  DEFAULT 0, "
                    + "    value double  DEFAULT 0)").execute();

            List<String> nna = new ArrayList<>();

            for (Object object : array) {
                LinkedHashMap record = (LinkedHashMap) object;

                String val = "";
                if (record.get("value").toString().isEmpty()) {
                    val = "0";
                } else {
                    val = record.get("value").toString();
                }

                nna.add("('" + record.get("employeeid") + "','" + record.get("item") + "','" + record.get("month") + "','" +
                        record.get("year") + "','" + val + "')");
            }


            String insertquery = nna.toString().replace("[", "").replace("]", "").trim();

            conn.prepareStatement("INSERT INTO `" + userId() + "_hr_" + farm + "` "
                    + " VALUES " + insertquery + ";"

                    + " insert ignore into readingbasedallowances(employeeid,item,month,year)"
                    + " select id,'" + search.get("item").toString().replace("[", "")
                    .replace("]", "") + "','" + search.get("month").toString().replace("[", "").replace("]", "") + "',"
                    + " '" + search.get("year").toString().replace("[", "").replace("]", "") + "' "
                    + " from employees where farm=" + farm + " and status='Active';"
                    + " UPDATE readingbasedallowances e "
                    + " inner join `" + userId() + "_hr_" + farm + "` c"
                    + " on c.employeeid=e.employeeid and c.item=e.item and c.month=e.month"
                    + " and c.year=e.year set e.readings=value; "

                    + " DROP TABLE `" + userId() + "_hr_" + farm + "` ").execute();
            ;


            accountsmodel.response = "Operation successfull";


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
            return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";


    }


    @RequestMapping(value = "/api/hr/getbulkinsertvaluesdeductions", method = RequestMethod.POST)
    @ResponseBody
    public String getbulkinsertvaluesdeductions(@RequestBody JSONObject search, HttpServletRequest request) {

        accountsmodel.dbaction("select * from (SELECT p.id,p.Payno,"
                + " CONCAT(p.fname,' ',p.sname,' ',surname) AS Name, "
                + " IFNULL(readings,'') Readings FROM employees p  "
                + " left JOIN (SELECT * FROM readingbaseddeductions "
                + " WHERE MONTH='" + search.get("month") + "' AND YEAR='" + search.get("year") + "' "
                + " AND item='" + search.get("item") + "') a ON a.employeeid=p.id "
                + " left JOIN deductions e ON a.item=e.id WHERE p.farm=" + farm().getId() + ")k ", 1, 0, 0, 0);

        return accountsmodel.jsondata;
    }


    @PostMapping(value = "/api/hr/postbulkdeductions")
    @ResponseBody
    public String postbulkdeductions(@RequestBody JSONObject search, HttpServletRequest request) {

        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            List<String> array = (List<String>) search.get("values");


            conn.prepareStatement("DROP TABLE IF EXISTS "
                    + "  `" + userId() + "_hr_" + farm + "`;"

                    + "    CREATE TABLE `" + userId() + "_hr_" + farm + "` ( "
                    + "    employeeid INT NOT NULL DEFAULT 0, "
                    + "    item INT NOT NULL  DEFAULT 0, "
                    + "    month INT NOT NULL  DEFAULT 0, "
                    + "    year INT NOT NULL  DEFAULT 0, "
                    + "    value double  DEFAULT 0)").execute();

            List<String> nna = new ArrayList<>();

            for (Object object : array) {
                LinkedHashMap record = (LinkedHashMap) object;

                String val = "";
                if (record.get("value").toString().isEmpty()) {
                    val = "0";
                } else {
                    val = record.get("value").toString();
                }

                nna.add("('" + record.get("employeeid") + "','" + record.get("item") + "','" + record.get("month") + "','" +
                        record.get("year") + "','" + val + "')");
            }


            String insertquery = nna.toString().replace("[", "").replace("]", "").trim();

            conn.prepareStatement("INSERT INTO `" + userId() + "_hr_" + farm + "` "
                    + " VALUES " + insertquery + ";"

                    + " insert ignore into readingbaseddeductions(employeeid,item,month,year)"
                    + " select id,'" + search.get("item").toString().replace("[", "")
                    .replace("]", "") + "','" + search.get("month").toString().replace("[", "").replace("]", "") + "',"
                    + " '" + search.get("year").toString().replace("[", "").replace("]", "") + "' "
                    + " from employees where farm=" + farm + " and status='Active';"

                    + " UPDATE readingbaseddeductions e "
                    + " inner join `" + userId() + "_hr_" + farm + "` c"
                    + " on c.employeeid=e.employeeid and c.item=e.item and c.month=e.month"
                    + " and c.year=e.year set e.readings=value; "

                    + " DROP TABLE `" + userId() + "_hr_" + farm + "` ").execute();
            ;


            accountsmodel.response = "Operation successfull";


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
            return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";


    }


    @RequestMapping(value = "api/hr/getpayrolldata", method = RequestMethod.POST)
    @ResponseBody
    public String getpayrolldata(@RequestBody JSONObject search, HttpServletRequest request) {

        String SQL = "";

        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();


            List<String> SUMMAIN = new ArrayList<String>();
            List<String> SUMSEC = new ArrayList<String>();
            List<String> nna = new ArrayList<String>();

            ResultSet rs = conn.prepareStatement("SELECT allowance,`earning` FROM `allowances` a  "
                    + " inner join earnings e on e.id=allowance  "
                    + " where farm=" + farm + " GROUP BY allowance").executeQuery();

            while (rs.next()) {

                SUMMAIN.add("format(SUM(`" + rs.getString("earning") + "`),0) as `" + rs.getString("earning") + "`");

                nna.add("SUM(IF(AMOUNT> 0 AND allowance=" + rs.getString("allowance") + ","
                        + " (amount), 0)) AS `" + rs.getString("earning") + "`");

                SUMSEC.add("SUM(0) AS `" + rs.getString("earning") + "`");

            }


            String ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();

            String ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();

            String dd = nna.toString().replace("[", "").replace("]", "").trim();


            SQL = " SELECT id,Payno,Name," + ddsumMAIN + ",format(`GROSS SALARY`,0) AS `Gross`, '' as Action"
                    + "  FROM (select employees.Payno,  employees.id, concat(fname,'  ',sname,'  ',surname) as name, "
                    + " `monthly pays`.`gross salary`, " + ddsumsec + ", sum(0) as aamount  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + search.get("id") + " and employees.status='Active' group by employees.id "

                    + " union all "

                    + " select Payno,`pay no`,0,0 ," + dd + ",sum(amount) from allowances a "
                    + " inner join employees e on e.id=`pay no` "
                    + " where payroll='" + search.get("id") + "' and e.status='Active' "
                    + " group by `pay no`)h  group by payno";


            accountsmodel.dbaction("SELECT * FROM (" + SQL + ")b ", 1, 0, 0, 0);

            System.out.println("SELECT * FROM (" + SQL + ")b ");


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
            return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        return accountsmodel.jsondata;

    }


    @RequestMapping(path = "/api/hr/printregisterfrombasictogross", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<byte[]> printregisterfrombasictogross(@RequestBody JSONObject search, HttpServletRequest request) throws IOException {

        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        String EXTENSION = ".pdf";

        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION);

        String SQL = "";

        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();


            List<String> SUMMAIN = new ArrayList<String>();
            List<String> SUMSEC = new ArrayList<String>();
            List<String> nna = new ArrayList<String>();

            ResultSet rs = conn.prepareStatement("SELECT allowance,`earning` FROM `allowances` a  "
                    + " inner join earnings e on e.id=allowance  "
                    + " where farm=" + farm + " GROUP BY allowance "
                    + " order by case when `earning`='Basic income' then 1 else 2 end ").executeQuery();

            while (rs.next()) {

                SUMMAIN.add("SUM(`" + rs.getString("earning") + "`) as `" + rs.getString("earning") + "`");

                nna.add("SUM(IF(AMOUNT> 0 AND allowance=" + rs.getString("allowance") + ","
                        + " (amount), 0)) AS `" + rs.getString("earning") + "`");

                SUMSEC.add("SUM(0) AS `" + rs.getString("earning") + "`");

            }


            String ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();

            String ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();

            String dd = nna.toString().replace("[", "").replace("]", "").trim();


            SQL = " SELECT Payno,'' as groupby,Name," + ddsumMAIN + ",`GROSS SALARY` AS `Gross`"
                    + "  FROM (select employees.Payno,  employees.id, concat(fname,'  ',sname,'  ',surname) as name, "
                    + " `monthly pays`.`gross salary`, " + ddsumsec + ", sum(0) as aamount  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + search.get("id") + " and employees.status='Active' group by employees.id "

                    + " union all "

                    + " select Payno,`pay no`,0,0 ," + dd + ",sum(amount) from allowances a "
                    + " inner join employees e on e.id=`pay no` "
                    + " where payroll='" + search.get("id") + "' and e.status='Active' "
                    + " group by `pay no`)h  group by payno order by LPAD(lower(payno), 1000,0);";

            System.out.println(SQL);

            testReport(SQL, dynamicreport_template, "registergrombasictogross", "", "", null, "", "",
                    uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION,
                    farm, Integer.parseInt(search.get("id").toString()));


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = java.util.Base64.getEncoder().encode(inFileBytes);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION;
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;

    }


    @RequestMapping(path = "/api/hr/printpayregfromgrosstonet", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<byte[]> printpayregfromgrosstonet(@RequestBody JSONObject search, HttpServletRequest request) throws IOException {

        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        String EXTENSION = ".pdf";

        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION);

        String SQL = "";

        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();


            List<String> SUMMAIN = new ArrayList<String>();
            List<String> SUMSEC = new ArrayList<String>();
            List<String> nna = new ArrayList<String>();

            List<String> totaldeductions = new ArrayList<String>();

            ResultSet rs = conn.prepareStatement("SELECT e.id,e.`deduction` FROM `other deductions` a  "
                    + " inner join deductions e on e.id=a.deduction  "
                    + " where farm=" + farm + " GROUP BY e.id").executeQuery();

            while (rs.next()) {

                SUMMAIN.add("SUM(`" + rs.getString("deduction") + "`) as `" + rs.getString("deduction") + "`");

                nna.add("SUM(IF(AMOUNT> 0 AND deduction=" + rs.getString("id") + ","
                        + " (amount), 0)) AS `" + rs.getString("deduction") + "`");

                SUMSEC.add("SUM(0) AS `" + rs.getString("deduction") + "`");

                totaldeductions.add("SUM(`" + rs.getString("deduction") + "`)+");

            }


            String ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();

            String ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();

            String dd = nna.toString().replace("[", "").replace("]", "").trim();

            String totaldeduction = totaldeductions.toString().replace("[", "").replace("+]", "").replace("+,", "+").trim();


            SQL = " SELECT Payno,'' as groupby,Name,`GROSS SALARY` AS `Gross`,Taxable,Paye,Nhif,Housing,Nssf," + ddsumMAIN + ","
                    + " (nhif+nssf+housing+paye+" + totaldeduction + ") as 'Total deductions',"
                    + " round(`GROSS SALARY`-(nhif+nssf+housing+paye+" + totaldeduction + ")) as 'Net pay'"
                    + "  FROM (select employees.Payno, concat(fname,'  ',sname,'  ',surname) as name, "
                    + " `monthly pays`.`gross salary`,taxable,`kra tax` AS Paye,`monthly pays`.nhif,`monthly pays`.housing,`monthly pays`.nssf,"
                    + "  " + ddsumsec + "  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + search.get("id") + " and employees.status='Active' group by employees.id "

                    + " union all "

                    + " select e.Payno,0,0,0,0,0,0,0 ," + dd + " from `other deductions` a "
                    + " inner join employees e on e.id=a.`payno` "
                    + " where payroll='" + search.get("id") + "' and e.status='Active' "
                    + " group by a.`payno`)h  group by payno order by LPAD(lower(payno), 1000,0);";


            testReport(SQL.replace(",,", ",").replace("+])", ")").
                            replace(", from `other deductions`", "from `other deductions`").
                            replace("nssf,    from employees", "nssf  from employees"), dynamicreport_template, "registergromgrosstonet", "", "", null, "", "",
                    uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION,
                    farm, Integer.parseInt(search.get("id").toString()));


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = java.util.Base64.getEncoder().encode(inFileBytes);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION;
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;

    }


    @RequestMapping(path = "/api/hr/printpayregcombined", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<byte[]> printpayregcombined(@RequestBody JSONObject search, HttpServletRequest request) throws IOException {

        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        String EXTENSION = ".pdf";

        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION);

        String SQL = "";
        String SQL2 = "";
        String SQL3 = "";

        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();


            List<String> SUMMAIN = new ArrayList<String>();
            List<String> SUMSEC = new ArrayList<String>();
            List<String> nna = new ArrayList<String>();
            List<String> totaldeductions = new ArrayList<String>();

            List<String> collumnsallowances = new ArrayList<String>();

            List<String> collumnsdeductions = new ArrayList<String>();

            ResultSet rs = conn.prepareStatement("SELECT e.id,e.`deduction` FROM `other deductions` a  "
                    + " inner join deductions e on e.id=a.deduction  "
                    + " where farm=" + farm + " GROUP BY e.id").executeQuery();

            while (rs.next()) {

                SUMMAIN.add("SUM(`" + rs.getString("deduction") + "`) as `" + rs.getString("deduction") + "`");
                nna.add("SUM(IF(AMOUNT> 0 AND deduction=" + rs.getString("id") + ","
                        + " (amount), 0)) AS `" + rs.getString("deduction") + "`");
                SUMSEC.add("SUM(0) AS `" + rs.getString("deduction") + "`");
                totaldeductions.add("SUM(`" + rs.getString("deduction") + "`)+");
                collumnsdeductions.add("`" + rs.getString("deduction") + "`");
            }


            String ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();
            String ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();
            String dd = nna.toString().replace("[", "").replace("]", "").trim();
            String totaldeduction = totaldeductions.toString().replace("[", "").replace("+]", "").replace("+,", "+").trim();
            String collsdeductions = collumnsdeductions.toString().replace("[", "").replace("]", "").trim();

            SQL = " SELECT Payno,Taxable,Paye,Nhif,Nssf,housing," + ddsumMAIN + ","
                    + " (nhif+nssf+housing+paye+" + totaldeduction + ") as 'Total deductions',"
                    + " round(`GROSS SALARY`-(nhif+nssf+housing+paye+" + totaldeduction + ")) as 'Net pay'"
                    + "  FROM (select employees.Payno, concat(fname,'  ',sname,'  ',surname) as name, "
                    + " `monthly pays`.`gross salary`,taxable,`kra tax` AS Paye,`monthly pays`.nhif,"
                    + " `monthly pays`.housing,`monthly pays`.nssf,"
                    + "  " + ddsumsec + "  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + search.get("id") + " and employees.status='Active' group by employees.id "

                    + " union all "

                    + " select e.Payno,0,0,0,0,0,0,0 ," + dd + " from `other deductions` a "
                    + " inner join employees e on e.id=a.`payno` "
                    + " where payroll='" + search.get("id") + "' and e.status='Active' "
                    + " group by a.`payno`)h  group by payno";


            SUMMAIN = new ArrayList<String>();
            SUMSEC = new ArrayList<String>();
            nna = new ArrayList<String>();

            rs = conn.prepareStatement("SELECT allowance,`earning` FROM `allowances` a  "
                    + " inner join earnings e on e.id=allowance  "
                    + " where farm=" + farm + " GROUP BY allowance"
                    + " order by case when `earning`='Basic income' then 1 else 2 end ").executeQuery();

            while (rs.next()) {

                SUMMAIN.add("SUM(`" + rs.getString("earning") + "`) as `" + rs.getString("earning") + "`");
                nna.add("SUM(IF(AMOUNT> 0 AND allowance=" + rs.getString("allowance") + ","
                        + " (amount), 0)) AS `" + rs.getString("earning") + "`");
                SUMSEC.add("SUM(0) AS `" + rs.getString("earning") + "`");
                collumnsallowances.add("`" + rs.getString("earning") + "`");


            }


            ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();
            ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();
            dd = nna.toString().replace("[", "").replace("]", "").trim();
            String collsallowances = collumnsallowances.toString().replace("[", "").replace("]", "").trim();


            SQL2 = " SELECT Payno,'' as groupby,Name," + ddsumMAIN + ",`GROSS SALARY` AS `Gross`"
                    + "  FROM (select employees.Payno,  employees.id, concat(fname,'  ',sname,'  ',surname) as name, "
                    + " `monthly pays`.`gross salary`, " + ddsumsec + ", sum(0) as aamount  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + search.get("id") + " and employees.status='Active' group by employees.id "

                    + " union all "

                    + " select Payno,`pay no`,0,0 ," + dd + ",sum(amount) from allowances a "
                    + " inner join employees e on e.id=`pay no` "
                    + " where payroll='" + search.get("id") + "' and e.status='Active' "
                    + " group by `pay no`)h  group by payno";


            SQL3 = "Select a.Payno,'' as groupby,Name," + collsallowances + ",Gross,Taxable,Paye,Nhif,housing,Nssf," + collsdeductions + ","
                    + " `Total deductions`,`Net pay` from (" + SQL + ")a left join (" + SQL2 + ")b on a.payno=b.payno "
                    + " order by LPAD(lower(a.payno), 1000,0);";

            System.out.println(SQL3);

            testReport(SQL3.replace(",,", ",").replace("+])", ")").
                            replace(", from `other deductions`", "from `other deductions`").
                            replace("nssf,    from employees", "nssf  from employees"), dynamicreport_template, "registercombined", "", "", null, "", "",
                    uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION,
                    farm, Integer.parseInt(search.get("id").toString()));


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = java.util.Base64.getEncoder().encode(inFileBytes);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION;
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;

    }


    @RequestMapping(path = "/api/hr/horizontalpayslips", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<byte[]> horizontalpayslips(@RequestBody JSONObject search, HttpServletRequest request) throws IOException {

        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        String EXTENSION = ".pdf";

        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION);


        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            String SQL = "";
            String SQL2 = "";
            String SQL3 = "";

            List<String> SUMMAIN = new ArrayList<String>();
            List<String> SUMSEC = new ArrayList<String>();
            List<String> nna = new ArrayList<String>();
            List<String> totaldeductions = new ArrayList<String>();


            ResultSet rs = conn.prepareStatement("SELECT e.id,e.`deduction` FROM `other deductions` a  "
                    + " inner join deductions e on e.id=a.deduction  "
                    + " where farm=" + farm + " GROUP BY e.id").executeQuery();

            while (rs.next()) {

                SUMMAIN.add("SUM(`" + rs.getString("deduction") + "`) as `" + rs.getString("deduction") + "`");
                nna.add("SUM(IF(AMOUNT> 0 AND deduction=" + rs.getString("id") + ","
                        + " (amount), 0)) AS `" + rs.getString("deduction") + "`");
                SUMSEC.add("SUM(0) AS `" + rs.getString("deduction") + "`");
                totaldeductions.add("SUM(`" + rs.getString("deduction") + "`)+");
            }


            String ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();
            String ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();
            String dd = nna.toString().replace("[", "").replace("]", "").trim();
            String totaldeduction = totaldeductions.toString().replace("[", "").replace("+]", "").replace("+,", "+").trim();

            SQL = " SELECT Payno,Taxable,Paye,Nhif,Nssf,Housing," + ddsumMAIN + ","
                    + " (nhif+nssf+housing+paye+" + totaldeduction + ") as 'Total deductions',"
                    + " round(`GROSS SALARY`-(nhif+nssf+housing+paye+" + totaldeduction + ")) as 'Net pay'"
                    + "  FROM (select employees.Payno, concat(fname,'  ',sname,'  ',surname) as name, "
                    + " `monthly pays`.`gross salary`,taxable,`kra tax` AS Paye,`monthly pays`.nhif,`monthly pays`.housing,`monthly pays`.nssf,"
                    + "  " + ddsumsec + "  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + search.get("id") + " and employees.status='Active' group by employees.id "

                    + " union all "

                    + " select e.Payno,0,0,0,0,0,0,0 ," + dd + " from `other deductions` a "
                    + " inner join employees e on e.id=a.`payno` "
                    + " where payroll='" + search.get("id") + "' and e.status='Active' "
                    + " group by a.`payno`)h  group by payno";


            SUMMAIN = new ArrayList<String>();
            SUMSEC = new ArrayList<String>();
            nna = new ArrayList<String>();

            rs = conn.prepareStatement("SELECT allowance,`earning` FROM `allowances` a  "
                    + " inner join earnings e on e.id=allowance  "
                    + " where farm=" + farm + " GROUP BY allowance").executeQuery();

            while (rs.next()) {

                SUMMAIN.add("SUM(`" + rs.getString("earning") + "`) as `" + rs.getString("earning") + "`");
                nna.add("SUM(IF(AMOUNT> 0 AND allowance=" + rs.getString("allowance") + ","
                        + " (amount), 0)) AS `" + rs.getString("earning") + "`");
                SUMSEC.add("SUM(0) AS `" + rs.getString("earning") + "`");


            }


            ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();
            ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();
            dd = nna.toString().replace("[", "").replace("]", "").trim();


            SQL2 = " SELECT Payno,pn,Name,`NSSF NUMBER`,`NHIF NUMBER`,`KRA PIN`,`MONTH`,YEAR,"
                    + " " + ddsumMAIN + ",`GROSS SALARY` AS `Gross`"
                    + "  FROM (select employees.Payno,employees.id as pn,`NSSF NUMBER`,`NHIF NUMBER`,`KRA PIN`, "
                    + " MONTHNAME(STR_TO_DATE(`month`, '%m')) AS `MONTH`,YEAR,  employees.id, "
                    + " concat(fname,'  ',sname,'  ',surname) as name, "
                    + " `monthly pays`.`gross salary`, " + ddsumsec + ", sum(0) as aamount  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + search.get("id") + " and employees.status='Active' group by employees.id "

                    + " union all "

                    + " select Payno,`pay no`,0,0,0,0,0,0,0,0 ," + dd + ",sum(amount) from allowances a "
                    + " inner join employees e on e.id=`pay no` "
                    + " where payroll='" + search.get("id") + "' and e.status='Active' "
                    + " group by `pay no`)h  group by payno";


            SQL3 = "Select s.name as Schoolname,concat(address,' ',region) as address,phone,TaxRelief,"
                    + " " + search.get("id") + " as payroll,a.Payno,`NSSF NUMBER`,`NHIF NUMBER`,`KRA PIN`,`MONTH`,YEAR"
                    + " ,b.Name,Gross,Taxable,Paye,pn,Nhif,Nssf,Housing,`Total deductions`,`Net pay` from "
                    + " (" + SQL + ")a left join (" + SQL2 + ")b on a.payno=b.payno "
                    + " inner join farm s on s.id=" + farm + " ";


            System.out.println(SQL3.replace(",,", ",").replace("+])", ")").
                    replace(", from `other deductions`", "from `other deductions`").
                    replace("nssf,    from employees", "nssf  from employees"));

            testReport(SQL3.replace(",,", ",").replace("+])", ")").
                            replace(", from `other deductions`", "from `other deductions`").
                            replace("nssf,    from employees", "nssf  from employees"), payslip_template, "horizontalpayslips", "", "", null, "", "",
                    uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION,
                    farm, Integer.parseInt(search.get("id").toString()));


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = java.util.Base64.getEncoder().encode(inFileBytes);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION;
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;

    }


    @RequestMapping(path = "/api/hr/horizontalpayslipindividual", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<byte[]> horizontalpayslipindividual(@RequestBody JSONObject search, HttpServletRequest request) throws IOException {

        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        String EXTENSION = ".pdf";

        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION);


        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            String SQL = "";
            String SQL2 = "";
            String SQL3 = "";

            List<String> SUMMAIN = new ArrayList<String>();
            List<String> SUMSEC = new ArrayList<String>();
            List<String> nna = new ArrayList<String>();
            List<String> totaldeductions = new ArrayList<String>();


            ResultSet rs = conn.prepareStatement("SELECT e.id,e.`deduction` FROM `other deductions` a  "
                    + " inner join deductions e on e.id=a.deduction  "
                    + " where farm=" + farm + " GROUP BY e.id").executeQuery();

            while (rs.next()) {

                SUMMAIN.add("SUM(`" + rs.getString("deduction") + "`) as `" + rs.getString("deduction") + "`");
                nna.add("SUM(IF(AMOUNT> 0 AND deduction=" + rs.getString("id") + ","
                        + " (amount), 0)) AS `" + rs.getString("deduction") + "`");
                SUMSEC.add("SUM(0) AS `" + rs.getString("deduction") + "`");
                totaldeductions.add("SUM(`" + rs.getString("deduction") + "`)+");
            }


            String ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();
            String ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();
            String dd = nna.toString().replace("[", "").replace("]", "").trim();
            String totaldeduction = totaldeductions.toString().replace("[", "").replace("+]", "").replace("+,", "+").trim();

            SQL = " SELECT Payno,Taxable,Paye,Nhif,Nssf,Housing," + ddsumMAIN + ","
                    + " (nhif+nssf+housing+paye+" + totaldeduction + ") as 'Total deductions',"
                    + " round(`GROSS SALARY`-(nhif+nssf+housing+paye+" + totaldeduction + ")) as 'Net pay'"
                    + "  FROM (select employees.Payno, concat(fname,'  ',sname,'  ',surname) as name, "
                    + " `monthly pays`.`gross salary`,taxable,`kra tax` AS Paye,`monthly pays`.nhif,`monthly pays`.housing,`monthly pays`.nssf,"
                    + "  " + ddsumsec + "  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + search.get("id") + " and employees.id='" + search.get("payno") + "' "
                    + " group by employees.id "

                    + " union all "

                    + " select e.Payno,0,0,0,0,0,0,0 ," + dd + " from `other deductions` a "
                    + " inner join employees e on e.id=a.`payno` "
                    + " where payroll='" + search.get("id") + "' and e.id='" + search.get("payno") + "' "
                    + " group by a.`payno`)h  group by payno";


            SUMMAIN = new ArrayList<String>();
            SUMSEC = new ArrayList<String>();
            nna = new ArrayList<String>();

            rs = conn.prepareStatement("SELECT allowance,`earning` FROM `allowances` a  "
                    + " inner join earnings e on e.id=allowance  "
                    + " where farm=" + farm + " GROUP BY allowance").executeQuery();

            while (rs.next()) {

                SUMMAIN.add("SUM(`" + rs.getString("earning") + "`) as `" + rs.getString("earning") + "`");
                nna.add("SUM(IF(AMOUNT> 0 AND allowance=" + rs.getString("allowance") + ","
                        + " (amount), 0)) AS `" + rs.getString("earning") + "`");
                SUMSEC.add("SUM(0) AS `" + rs.getString("earning") + "`");


            }


            ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();
            ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();
            dd = nna.toString().replace("[", "").replace("]", "").trim();


            SQL2 = " SELECT Payno,pn,Name,`NSSF NUMBER`,`NHIF NUMBER`,`KRA PIN`,`MONTH`,YEAR," + ddsumMAIN + ",`GROSS SALARY` AS `Gross`"
                    + "  FROM (select employees.Payno,employees.id as pn,`NSSF NUMBER`,`NHIF NUMBER`,`KRA PIN`, "
                    + " MONTHNAME(STR_TO_DATE(`month`, '%m')) AS `MONTH`,YEAR,  employees.id, "
                    + " concat(fname,'  ',sname,'  ',surname) as name, "
                    + " `monthly pays`.`gross salary`, " + ddsumsec + ", sum(0) as aamount  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + search.get("id") + " and employees.id='" + search.get("payno") + "' "
                    + " group by employees.id "

                    + " union all "

                    + " select Payno,`pay no`,0,0,0,0,0,0,0,0 ," + dd + ",sum(amount) from allowances a "
                    + " inner join employees e on e.id=`pay no` "
                    + " where payroll='" + search.get("id") + "' and e.id='" + search.get("payno") + "' "
                    + " group by `pay no`)h  group by payno";


            SQL3 = "Select Schoolname,concat(address,' ',city) as address,phone,TaxRelief,"
                    + " " + search.get("id") + " as payroll,a.Payno,pn,`NSSF NUMBER`,`NHIF NUMBER`,`KRA PIN`,`MONTH`,YEAR"
                    + " ,Name,`Basic income`,Gross,Taxable,Paye,Nhif,Nssf,Housing,`Total deductions`,`Net pay` from "
                    + " (" + SQL + ")a left join (" + SQL2 + ")b on a.payno=b.payno "
                    + " inner join farm s on s.id=" + farm + " ";


            testReport(SQL3.replace(",,", ",").replace("+])", ")").
                            replace(", from `other deductions`", "from `other deductions`").
                            replace("nssf,    from employees", "nssf  from employees"), payslip_template, "horizontalpayslips", "", "", null, "", "",
                    uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION,
                    farm, Integer.parseInt(search.get("id").toString()));


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = java.util.Base64.getEncoder().encode(inFileBytes);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION;
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;

    }


    @RequestMapping(path = "/api/hr/verticalpayslips", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<byte[]> verticalpayslips(@RequestBody JSONObject search, HttpServletRequest request) throws IOException {

        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        String EXTENSION = ".pdf";

        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION);


        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            String SQL = "";
            String SQL2 = "";
            String SQL3 = "";

            List<String> SUMMAIN = new ArrayList<String>();
            List<String> SUMSEC = new ArrayList<String>();
            List<String> nna = new ArrayList<String>();
            List<String> totaldeductions = new ArrayList<String>();


            ResultSet rs = conn.prepareStatement("SELECT e.id,e.`deduction` FROM `other deductions` a  "
                    + " inner join deductions e on e.id=a.deduction  "
                    + " where farm=" + farm + " GROUP BY e.id").executeQuery();

            while (rs.next()) {
                SUMMAIN.add("SUM(`" + rs.getString("deduction") + "`) as `" + rs.getString("deduction") + "`");
                nna.add("SUM(IF(AMOUNT> 0 AND deduction=" + rs.getString("id") + ","
                        + " (amount), 0)) AS `" + rs.getString("deduction") + "`");
                SUMSEC.add("SUM(0) AS `" + rs.getString("deduction") + "`");
                totaldeductions.add("SUM(`" + rs.getString("deduction") + "`)+");
            }


            String ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();
            String ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();
            String dd = nna.toString().replace("[", "").replace("]", "").trim();
            String totaldeduction = totaldeductions.toString().replace("[", "").replace("+]", "").replace("+,", "+").trim();

            SQL = " SELECT Payno,Taxable,Paye,Nhif,Nssf,Housing," + ddsumMAIN + ","
                    + " (nhif+nssf+housing+paye+" + totaldeduction + ") as 'Total deductions',"
                    + " round(`GROSS SALARY`-(nhif+nssf+housing+paye+" + totaldeduction + ")) as 'Net pay'"
                    + "  FROM (select employees.Payno, concat(fname,'  ',sname,'  ',surname) as name, "
                    + " `monthly pays`.`gross salary`,taxable,`kra tax` AS Paye,`monthly pays`.nhif,"
                    + " `monthly pays`.housing,`monthly pays`.nssf," + ddsumsec + "  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + search.get("id") + " and employees.status='Active' group by employees.id "

                    + " union all "

                    + " select e.Payno,0,0,0,0,0,0,0 ," + dd + " from `other deductions` a "
                    + " inner join employees e on e.id=a.`payno` "
                    + " where payroll='" + search.get("id") + "' and e.status='Active' "
                    + " group by a.`payno`)h  group by payno";


            SUMMAIN = new ArrayList<String>();
            SUMSEC = new ArrayList<String>();
            nna = new ArrayList<String>();

            rs = conn.prepareStatement("SELECT allowance,`earning` FROM `allowances` a  "
                    + " inner join earnings e on e.id=allowance  "
                    + " where farm=" + farm + " GROUP BY allowance").executeQuery();

            while (rs.next()) {

                SUMMAIN.add("SUM(`" + rs.getString("earning") + "`) as `" + rs.getString("earning") + "`");
                nna.add("SUM(IF(AMOUNT> 0 AND allowance=" + rs.getString("allowance") + ","
                        + " (amount), 0)) AS `" + rs.getString("earning") + "`");
                SUMSEC.add("SUM(0) AS `" + rs.getString("earning") + "`");


            }


            ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();
            ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();
            dd = nna.toString().replace("[", "").replace("]", "").trim();


            SQL2 = " SELECT Payno,pn,Name,`NSSF NUMBER`,`NHIF NUMBER`,`KRA PIN`,`MONTH`,YEAR," + ddsumMAIN + ","
                    + " `GROSS SALARY` AS `Gross` FROM (select employees.Payno,employees.id as pn,"
                    + " `NSSF NUMBER`,`NHIF NUMBER`,`KRA PIN`,MONTHNAME(STR_TO_DATE(`month`, '%m')) AS `MONTH`,"
                    + " YEAR,  employees.id,concat(fname,'  ',sname,'  ',surname) as name, "
                    + " `monthly pays`.`gross salary`, " + ddsumsec + ", sum(0) as aamount  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + search.get("id") + " and employees.status='Active' group by employees.id "

                    + " union all "

                    + " select Payno,`pay no`,0,0,0,0,0,0,0,0 ," + dd + ",sum(amount) from allowances a "
                    + " inner join employees e on e.id=`pay no` "
                    + " where payroll='" + search.get("id") + "' and e.status='Active' "
                    + " group by `pay no`)h  group by payno";


            SQL3 = "Select s.name as Schoolname,concat(address,' ',region) as address,phone,TaxRelief,"
                    + " " + search.get("id") + " as payroll,a.Payno,pn,`NSSF NUMBER`,`NHIF NUMBER`,`KRA PIN`,`MONTH`,YEAR"
                    + " ,b.Name,`Basic income`,Gross,Taxable,Paye,Nhif,Nssf,Housing,`Total deductions`,`Net pay` from "
                    + " (" + SQL + ")a left join (" + SQL2 + ")b on a.payno=b.payno "
                    + " inner join farm s on s.id=" + farm + "";

            System.out.println(SQL3);

            testReport(SQL3.replace(",,", ",").replace("+])", ")").
                            replace(", from `other deductions`", "from `other deductions`").
                            replace("nssf,    from employees", "nssf  from employees"), payslipV_template, "horizontalpayslips", "", "", null, "", "",
                    uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION,
                    farm, Integer.parseInt(search.get("id").toString()));


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = java.util.Base64.getEncoder().encode(inFileBytes);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION;
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;

    }


    @RequestMapping(path = "/api/hr/verticalpayslipindividual", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<byte[]> verticalpayslipindividual(@RequestBody JSONObject search, HttpServletRequest request) throws IOException {

        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        String EXTENSION = ".pdf";

        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION);


        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            String SQL = "";
            String SQL2 = "";
            String SQL3 = "";

            List<String> SUMMAIN = new ArrayList<String>();
            List<String> SUMSEC = new ArrayList<String>();
            List<String> nna = new ArrayList<String>();
            List<String> totaldeductions = new ArrayList<String>();


            ResultSet rs = conn.prepareStatement("SELECT e.id,e.`deduction` FROM `other deductions` a  "
                    + " inner join deductions e on e.id=a.deduction  "
                    + " where farm=" + farm + " GROUP BY e.id").executeQuery();

            while (rs.next()) {

                SUMMAIN.add("SUM(`" + rs.getString("deduction") + "`) as `" + rs.getString("deduction") + "`");
                nna.add("SUM(IF(AMOUNT> 0 AND deduction=" + rs.getString("id") + ","
                        + " (amount), 0)) AS `" + rs.getString("deduction") + "`");
                SUMSEC.add("SUM(0) AS `" + rs.getString("deduction") + "`");
                totaldeductions.add("SUM(`" + rs.getString("deduction") + "`)+");
            }


            String ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();
            String ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();
            String dd = nna.toString().replace("[", "").replace("]", "").trim();
            String totaldeduction = totaldeductions.toString().replace("[", "").replace("+]", "").replace("+,", "+").trim();

            SQL = " SELECT Payno,Taxable,Paye,Nhif,Nssf,Housing," + ddsumMAIN + ","
                    + " (nhif+nssf+housing+paye+" + totaldeduction + ") as 'Total deductions',"
                    + " round(`GROSS SALARY`-(nhif+nssf+housing+paye+" + totaldeduction + ")) as 'Net pay'"
                    + "  FROM (select employees.Payno, concat(fname,'  ',sname,'  ',surname) as name, "
                    + " `monthly pays`.`gross salary`,taxable,`kra tax` AS Paye,`monthly pays`.nhif,"
                    + " `monthly pays`.housing,`monthly pays`.nssf," + ddsumsec + "  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + search.get("id") + " and employees.id='" + search.get("payno") + "' "
                    + " group by employees.id "

                    + " union all "

                    + " select e.Payno,0,0,0,0,0,0,0 ," + dd + " from `other deductions` a "
                    + " inner join employees e on e.id=a.`payno` "
                    + " where payroll='" + search.get("id") + "' and e.id='" + search.get("payno") + "' "
                    + " group by a.`payno`)h  group by payno";


            SUMMAIN = new ArrayList<String>();
            SUMSEC = new ArrayList<String>();
            nna = new ArrayList<String>();

            rs = conn.prepareStatement("SELECT allowance,`earning` FROM `allowances` a  "
                    + " inner join earnings e on e.id=allowance  "
                    + " where farm=" + farm + " GROUP BY allowance").executeQuery();

            while (rs.next()) {

                SUMMAIN.add("SUM(`" + rs.getString("earning") + "`) as `" + rs.getString("earning") + "`");
                nna.add("SUM(IF(AMOUNT> 0 AND allowance=" + rs.getString("allowance") + ","
                        + " (amount), 0)) AS `" + rs.getString("earning") + "`");
                SUMSEC.add("SUM(0) AS `" + rs.getString("earning") + "`");


            }


            ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();
            ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();
            dd = nna.toString().replace("[", "").replace("]", "").trim();


            SQL2 = " SELECT Payno,pn,Name,`NSSF NUMBER`,`NHIF NUMBER`,`KRA PIN`,`MONTH`,YEAR," + ddsumMAIN + ","
                    + " `GROSS SALARY` AS `Gross` FROM (select employees.Payno,employees.id as pn,"
                    + " `NSSF NUMBER`,`NHIF NUMBER`,`KRA PIN`,MONTHNAME(STR_TO_DATE(`month`, '%m')) AS `MONTH`,"
                    + " YEAR,  employees.id,concat(fname,'  ',sname,'  ',surname) as name, "
                    + " `monthly pays`.`gross salary`, " + ddsumsec + ", sum(0) as aamount  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + search.get("id") + " and employees.id='" + search.get("payno") + "' "
                    + " group by employees.id "

                    + " union all "

                    + " select Payno,`pay no`,0,0,0,0,0,0,0,0 ," + dd + ",sum(amount) from allowances a "
                    + " inner join employees e on e.id=`pay no` "
                    + " where payroll='" + search.get("id") + "' and e.id='" + search.get("payno") + "' "
                    + " group by `pay no`)h  group by payno";


            SQL3 = "Select Schoolname,concat(address,' ',city) as address,phone,TaxRelief,"
                    + " " + search.get("id") + " as payroll,a.Payno,pn,`NSSF NUMBER`,`NHIF NUMBER`,`KRA PIN`,`MONTH`,YEAR"
                    + " ,Name,`Basic income`,Gross,Taxable,Paye,Nhif,Nssf,Housing,`Total deductions`,`Net pay` from "
                    + " (" + SQL + ")a left join (" + SQL2 + ")b on a.payno=b.payno "
                    + " inner join farm s on s.id=" + farm + " ";


            testReport(SQL3.replace(",,", ",").replace("+])", ")").
                            replace(", from `other deductions`", "from `other deductions`").
                            replace("nssf,    from employees", "nssf  from employees"), payslipV_template, "horizontalpayslips", "", "", null, "", "",
                    uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION,
                    farm, Integer.parseInt(search.get("id").toString()));


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = java.util.Base64.getEncoder().encode(inFileBytes);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION;
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;

    }


    @RequestMapping(path = "/api/hr/payeereturns", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<byte[]> payeereturns(@RequestBody JSONObject search, HttpServletRequest request) throws IOException {

        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        String EXTENSION = ".pdf";

        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION);


        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            String SQL = "select fname,sname,surname,employees.id,employees.PAYNO,`KRA TAX`,`KRA PIN` from `monthly pays` "
                    + " inner join employees on `monthly pays`.payno = employees.id left join `other deductions` on  "
                    + " `monthly pays`.payno =  `other deductions`.payno WHERE `monthly pays`.payroll=" + search.get("id") + " "
                    + " group by `monthly pays`.payno HAVING `kra tax`>0";


            testReport(SQL, paree_returns, "horizontalpayslips", "", "", null, "", "",
                    uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION,
                    farm, Integer.parseInt(search.get("id").toString()));


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = java.util.Base64.getEncoder().encode(inFileBytes);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION;
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;

    }


    @RequestMapping(path = "/api/hr/p9a", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<byte[]> p9a(@RequestBody JSONObject search, HttpServletRequest request) throws IOException {

        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        String EXTENSION = ".pdf";

        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION);


        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();


            int usebiometricsforpayroll = 0;

            int year = 0;

            ResultSet payrolldetails = conn.prepareStatement("SELECT payeeoption,nhifoption"
                    + ",nssfoption,usebiometricsforpayroll,date,month(date) month,year(date) year from payrolls a "
                    + " inner join farm s on s.id=a.farm where a.id='" + search.get("id") + "'").executeQuery();
            while (payrolldetails.next()) {
                year = payrolldetails.getInt("year");

                usebiometricsforpayroll = payrolldetails.getInt("usebiometricsforpayroll");
                ;

            }

            String SQL = "SELECT name as Schoolname,\n" +
                    "       Employercode,\n" +
                    "       TaxRelief,\n" +
                    "       concat('static /', upload_path, ' / taxauthorityimage.jpeg ') as image,\n" +
                    "       `monthly pays`.`house`,\n" +
                    "       `monthly pays`.`taxable`,\n" +
                    "       `monthly pays`.`net salary`,\n" +
                    "       `monthly pays`.`gross salary`,\n" +
                    "       MONTHNAME(STR_TO_DATE(month, ' % m '))                        as month,\n" +
                    "       `monthly pays`.nssf,\n" +
                    "       employees.`KRA PIN`,\n" +
                    "       year,\n" +
                    "       surname,\n" +
                    "       fname,\n" +
                    "       sname,\n" +
                    "       `monthly pays`.`PAYNO`,\n" +
                    "       SUM(`NET SALARY`),\n" +
                    "       SUM(`monthly pays`.`KRA TAX`)\n" +
                    "FROM `monthly pays`\n" +
                    "         inner join employees on `monthly pays`.`payno` = employees.id\n" +
                    "         inner join farm s on employees.farm = s.id\n" +
                    "where employees.farm = '"+farm+"'\n" +
                    "  and year = '"+year+"'\n" +
                    "group by `monthly pays`.`month`, `monthly pays`.`payno`\n" +
                    "order by `monthly pays`.`payno`,\n" +
                    "         field(monthname(str_to_date(month, ' % m ')), ' january ', ' february ', ' march ',\n" +
                    "               ' april ', ' may ', ' june ', ' july ', ' august ', ' september ', ' october ', ' november\n" +
                    "               ', ' december ') asc, `monthly pays`.`id`;";


            testReport(SQL, p9a, "horizontalpayslips", "", "", null, "", "",
                    uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION,
                    farm, Integer.parseInt(search.get("id").toString()));


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = java.util.Base64.getEncoder().encode(inFileBytes);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION;
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;

    }


    @RequestMapping(path = "/api/hr/p10a", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<byte[]> p10a(@RequestBody JSONObject search, HttpServletRequest request) throws IOException {

        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        String EXTENSION = ".pdf";

        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION);


        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();


            int year = 0;

            ResultSet payrolldetails = conn.prepareStatement("SELECT payeeoption,nhifoption"
                    + ",nssfoption,usebiometricsforpayroll,date,month(date) month,year(date) year from payrolls a "
                    + " inner join farm s on s.id=a.farm where a.id='" + search.get("id") + "'").executeQuery();
            while (payrolldetails.next()) {
                year = payrolldetails.getInt("year");
            }

            String SQL = "SELECT name as Schoolname,\n" +
                    "       Employercode,\n" +
                    "       TaxRelief,\n" +
                    "       concat('static/', upload_path, '/taxauthorityimage.jpeg') as image,\n" +
                    "       employees.`KRA PIN`,\n" +
                    "       year,\n" +
                    "       surname,\n" +
                    "       fname,\n" +
                    "       sname,\n" +
                    "       `monthly pays`.`PAYNO`,\n" +
                    "       SUM(`NET SALARY`),\n" +
                    "       SUM(`monthly pays`.`KRA TAX`)\n" +
                    "FROM `monthly pays`\n" +
                    "         INNER JOIN employees ON `monthly pays`.`PAYNO` = employees.id\n" +
                    "         inner join farm s on employees.farm = s.id\n" +
                    "WHERE employees.farm = '"+farm+"'\n" +
                    "  and year = '"+year+"'\n" +
                    "GROUP BY `monthly pays`.`PAYNO`";


            testReport(SQL, p10a, "horizontalpayslips", "", "", null, "", "",
                    uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION,
                    farm, Integer.parseInt(search.get("id").toString()));


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        //System.out.println("yoooo yoooh "+file.getAbsolutePath());


        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = java.util.Base64.getEncoder().encode(inFileBytes);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION;
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;

    }


    @RequestMapping(path = "/api/hr/p9", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<byte[]> p9(@RequestBody JSONObject search, HttpServletRequest request) throws IOException {

        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        String EXTENSION = ".pdf";

        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION);


        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            int year = 0;
            ResultSet payrolldetails = conn.prepareStatement("SELECT payeeoption,nhifoption"
                    + ",nssfoption,usebiometricsforpayroll,date,month(date) month,year(date) year from payrolls a "
                    + " inner join farm s on s.id=a.farm where a.id='" + search.get("id") + "'").executeQuery();
            while (payrolldetails.next()) {
                year = payrolldetails.getInt("year");
            }

            String SQL = "SELECT a.*, total\n" +
                    "from (SELECT name                                                      as Schoolname,\n" +
                    "             concat(address, ' ', s.region)                            as address,\n" +
                    "             Employercode,\n" +
                    "             TaxRelief,\n" +
                    "             concat('static/', upload_path, '/taxauthorityimage.jpeg') as image,\n" +
                    "             `year`,\n" +
                    "             `month`,\n" +
                    "             round(sum(`kra tax`),2)\n" +
                    "      from `monthly pays`\n" +
                    "               INNER JOIN employees ON `monthly pays`.`PAYNO` = employees.id\n" +
                    "               inner join farm s on employees.farm = s.id\n" +
                    "      where year = '"+year+"'\n" +
                    "        and farm = '"+farm+"'\n" +
                    "      group by month) a\n" +
                    "         inner join (SELECT `year`, round(sum(`kra tax`), 2) as total\n" +
                    "                     from `monthly pays`\n" +
                    "                              INNER JOIN employees ON `monthly pays`.`PAYNO` = employees.id\n" +
                    "                     where year = '"+year+"'\n" +
                    "                       and farm = '"+farm+"') b on a.year = b.year";


            testReport(SQL, p9, "horizontalpayslips", "", "", null, "", "",
                    uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION,
                    farm, Integer.parseInt(search.get("id").toString()));


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = java.util.Base64.getEncoder().encode(inFileBytes);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION;
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;

    }


    @RequestMapping(path = "/api/hr/nhif", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<byte[]> nhif(@RequestBody JSONObject search, HttpServletRequest request) throws IOException {

        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        String EXTENSION = ".pdf";

        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION);


        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            String SQL = "select name                                                      as Schoolname,\n" +
                    "       concat(address, ' ', s.region)                            as address,\n" +
                    "       Employercode,\n" +
                    "       TaxRelief,\n" +
                    "       concat('static/', upload_path, '/taxauthorityimage.jpeg') as image,\n" +
                    "       fname,\n" +
                    "       sname,\n" +
                    "       surname,\n" +
                    "       employees.idnu                                            as id,\n" +
                    "       employees.PAYNO,\n" +
                    "       `monthly pays`.`NHIF`,\n" +
                    "       `NHIF NUMBER`,\n" +
                    "       MONTHNAME(STR_TO_DATE(`month`, '%m'))                     AS MONTH,\n" +
                    "       YEAR\n" +
                    "from `monthly pays`\n" +
                    "         inner join employees on `monthly pays`.payno = employees.id\n" +
                    "         inner join farm s on employees.farm = s.id\n" +
                    "WHERE payroll = '"+search.get("id")+"'\n" +
                    "group by `monthly pays`.payno";


            System.out.println(SQL);

            testReport(SQL, nhif, "horizontalpayslips", "", "", null, "", "",
                    uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION,
                    farm, Integer.parseInt(search.get("id").toString()));


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = java.util.Base64.getEncoder().encode(inFileBytes);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION;
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;

    }


    @RequestMapping(path = "/api/hr/housing", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<byte[]> housing(@RequestBody JSONObject search, HttpServletRequest request) throws IOException {

        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        String EXTENSION = ".pdf";

        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION);


        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            String SQL = "select name as  Schoolname,concat(address,' ',s.region) as address, Employercode,TaxRelief,"
                    + " concat('static/',upload_path,'/taxauthorityimage.jpeg') as image, "
                    + " fname,sname,surname,employees.idnu,employees.PAYNO,`monthly pays`.`HOUSING`,employees.`kra pin`, "
                    + " MONTHNAME(STR_TO_DATE(`month`, '%m')) AS MONTH,YEAR FROM `monthly pays`  "
                    + " inner join employees on `monthly pays`.payno = employees.id  "
                    + " inner join farm s on employees.farm = s.id "
                    + " WHERE payroll='" + search.get("id") + "' group by `monthly pays`.payno";


            System.out.println(SQL);

            testReport(SQL, housing, "horizontalpayslips", "", "", null, "", "",
                    uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION,
                    farm, Integer.parseInt(search.get("id").toString()));


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = java.util.Base64.getEncoder().encode(inFileBytes);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION;
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;

    }


    @RequestMapping(path = "/api/hr/nita", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<byte[]> nita(@RequestBody JSONObject search, HttpServletRequest request) throws IOException {

        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        String EXTENSION = ".pdf";

        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION);


        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            String SQL = "select name as Schoolname,concat(address,' ',s.region) as address,"
                    + " Employercode,TaxRelief,concat('static/',upload_path,'/taxauthorityimage.jpeg') as image,"
                    + " fname,sname,surname,employees.idnu as id,employees.PAYNO,50 AS `NHIF`,`NHIF NUMBER`,"
                    + " MONTHNAME(STR_TO_DATE(`month`, '%m')) AS MONTH,YEAR from `monthly pays` "
                    + " inner join employees on `monthly pays`.payno = employees.id "
                    + " inner join farm s on employees.farm = s.id"
                    + " WHERE payroll='" + search.get("id") + "' group by `monthly pays`.payno";


            System.out.println(SQL);

            testReport(SQL, nita, "horizontalpayslips", "", "", null, "", "",
                    uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION,
                    farm, Integer.parseInt(search.get("id").toString()));


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = java.util.Base64.getEncoder().encode(inFileBytes);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION;
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;

    }


    @RequestMapping(path = "/api/hr/nssf", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<byte[]> nssf(@RequestBody JSONObject search, HttpServletRequest request) throws IOException {

        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        String EXTENSION = ".pdf";

        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION);


        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            String SQL = "select name as Schoolname,concat(address,' ',s.region) as address,"
                    + " Employercode,TaxRelief,concat('static/',upload_path,'/taxauthorityimage.jpeg') as image,"
                    + " fname,sname,surname,employees.idnu as id,employees.PAYNO,`monthly pays`.`NSSF`,`NSSF NUMBER`, "
                    + " MONTHNAME(STR_TO_DATE(`month`, '%m')) AS MONTH,YEAR,`KRA PIN` from  `monthly pays` "
                    + " inner join employees on `monthly pays`.payno = employees.id "
                    + " inner join farm s on employees.farm = s.id"
                    + " WHERE   payroll='" + search.get("id") + "' group by `monthly pays`.payno HAVING nssf>0";


            testReport(SQL, nssf, "horizontalpayslips", "", "", null, "", "",
                    uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION,
                    farm, Integer.parseInt(search.get("id").toString()));


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = java.util.Base64.getEncoder().encode(inFileBytes);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION;
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;

    }


    @RequestMapping(path = "/api/hr/banktotals", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<byte[]> banktotals(@RequestBody JSONObject search, HttpServletRequest request) throws IOException {

        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        String EXTENSION = ".pdf";

        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION);


        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            String SQL = "";
            String SQL2 = "";
            String SQL3 = "";

            List<String> SUMMAIN = new ArrayList<String>();
            List<String> SUMSEC = new ArrayList<String>();
            List<String> nna = new ArrayList<String>();
            List<String> totaldeductions = new ArrayList<String>();


            ResultSet rs = conn.prepareStatement("SELECT e.id,e.`deduction` FROM `other deductions` a  "
                    + " inner join deductions e on e.id=a.deduction  "
                    + " where farm=" + farm + " GROUP BY e.id").executeQuery();

            while (rs.next()) {

                SUMMAIN.add("SUM(`" + rs.getString("deduction") + "`) as `" + rs.getString("deduction") + "`");
                nna.add("SUM(IF(AMOUNT> 0 AND deduction=" + rs.getString("id") + ","
                        + " (amount), 0)) AS `" + rs.getString("deduction") + "`");
                SUMSEC.add("SUM(0) AS `" + rs.getString("deduction") + "`");
                totaldeductions.add("SUM(`" + rs.getString("deduction") + "`)+");
            }


            String ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();
            String ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();
            String dd = nna.toString().replace("[", "").replace("]", "").trim();
            String totaldeduction = totaldeductions.toString().replace("[", "").replace("+]", "").replace("+,", "+").trim();

            SQL = " SELECT Payno,Taxable,Paye,Nhif,Nssf,Housing," + ddsumMAIN + ","
                    + " (nhif+nssf+housing+paye+" + totaldeduction + ") as 'Total deductions',"
                    + " round(`GROSS SALARY`-(nhif+nssf+housing+paye+" + totaldeduction + ")) as 'Net pay'"
                    + "  FROM (select employees.Payno, bankname, "
                    + " `monthly pays`.`gross salary`,taxable,`kra tax` AS Paye,`monthly pays`.nhif,`monthly pays`.housing,`monthly pays`.nssf,"
                    + "  " + ddsumsec + "  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + search.get("id") + " group by employees.id "

                    + " union all "

                    + " select e.Payno,0,0,0,0,0,0,0 ," + dd + " from `other deductions` a "
                    + " inner join employees e on e.id=a.`payno` "
                    + " where payroll='" + search.get("id") + "' group by a.`payno`)h  group by payno";


            SUMMAIN = new ArrayList<String>();
            SUMSEC = new ArrayList<String>();
            nna = new ArrayList<String>();

            rs = conn.prepareStatement("SELECT allowance,`earning` FROM `allowances` a  "
                    + " inner join earnings e on e.id=allowance  "
                    + " where farm=" + farm + " GROUP BY allowance").executeQuery();

            while (rs.next()) {

                SUMMAIN.add("SUM(`" + rs.getString("earning") + "`) as `" + rs.getString("earning") + "`");
                nna.add("SUM(IF(AMOUNT> 0 AND allowance=" + rs.getString("allowance") + ","
                        + " (amount), 0)) AS `" + rs.getString("earning") + "`");
                SUMSEC.add("SUM(0) AS `" + rs.getString("earning") + "`");


            }


            ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();
            ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();
            dd = nna.toString().replace("[", "").replace("]", "").trim();


            SQL2 = " SELECT Payno,bankname,`NSSF NUMBER`,`NHIF NUMBER`,`KRA PIN`,`MONTH`,YEAR," + ddsumMAIN + ","
                    + " `GROSS SALARY` AS `Gross`"
                    + " FROM (select employees.Payno,`NSSF NUMBER`,`NHIF NUMBER`,`KRA PIN`, "
                    + " MONTHNAME(STR_TO_DATE(`month`, '%m')) AS `MONTH`,YEAR,  employees.id, "
                    + " bankname,`monthly pays`.`gross salary`, " + ddsumsec + ", sum(0) as aamount  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + search.get("id") + " group by employees.id "

                    + " union all "

                    + " select Payno,`pay no`,0,0,0,0,0,0,0 ," + dd + ",sum(amount) from allowances a "
                    + " inner join employees e on e.id=`pay no` "
                    + " where payroll='" + search.get("id") + "' "
                    + " group by `pay no`)h  group by payno";


            SQL3 = "Select bankname,sum(`Net pay`) as amount"
                    + " from (" + SQL + ")a left join (" + SQL2 + ")b on a.payno=b.payno group by bankname ";


            testReport(SQL3.replace(",,", ",").replace("+])", ")").
                            replace(", from `other deductions`", "from `other deductions`").
                            replace("nssf,    from employees", "nssf  from employees"), banktotals, "horizontalpayslips", "", "", null, "", "",
                    uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION,
                    farm, Integer.parseInt(search.get("id").toString()));


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = java.util.Base64.getEncoder().encode(inFileBytes);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION;
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;

    }


    @RequestMapping(path = "/api/hr/payrolltotals", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<byte[]> payrolltotals(@RequestBody JSONObject search, HttpServletRequest request) throws IOException {

        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        String EXTENSION = ".pdf";

        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION);


        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            String SQL = "SELECT 'EARNINGS' AS EARNING, earning AS DEDUCTION,SUM(AMOUNT) FROM allowances  a "
                    + " inner JOIN earnings e ON e.id=a.ALLOWANCE"
                    + " WHERE payroll='" + search.get("id") + "' GROUP BY allowance "
                    + " UNION ALL"
                    + " SELECT 'DEDUCTIONS' AS DEDUCTIONS, e.DEDUCTION,SUM(AMOUNT) FROM `other deductions` a "
                    + " inner JOIN deductions e ON e.id=a.deduction"
                    + " WHERE payroll='" + search.get("id") + "'  GROUP BY a.DEDUCTION ";


            testReport(SQL, payrolltotals, "horizontalpayslips", "", "", null, "", "",
                    uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION,
                    farm, Integer.parseInt(search.get("id").toString()));


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = java.util.Base64.getEncoder().encode(inFileBytes);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION;
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;

    }


    @PostMapping(value = "/api/hr/makepayrollpayment")
    @ResponseBody
    public String makepayrollpayment(@RequestBody JSONObject search, HttpServletRequest request) {

        String payroll = search.get("payroll").toString();

        jsontosql.jsontosqlaction("payrollpayments", search, 2, "");

        if (accountsmodel.response.equals("an error occured")) {
            return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";
        } else {

            Connection conn = null;
            try {
                conn = DbConnector.getConnection();

                conn.prepareStatement("update payrolls set status='Paid' where id=" + payroll + " ").execute();

            } catch (Exception e1) {
                e1.printStackTrace();
                accountsmodel.response = "an error occured";
            } finally {
                if (conn != null) try {
                    conn.close();
                } catch (SQLException ignore) {
                }
            }

        }

        return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";


    }


    @RequestMapping(value = "/api/hr/sendpayrollnotification/{id}", method = RequestMethod.GET)
    public String sendpayrollnotification(@PathVariable("id") Integer id, Model model) {

        System.out.println("Payroll id: " + id);

        String SQL = "";

        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();


            List<String> SUMMAIN = new ArrayList<String>();
            List<String> SUMSEC = new ArrayList<String>();
            List<String> nna = new ArrayList<String>();

            ResultSet rs = conn.prepareStatement("SELECT allowance,`earning` FROM `allowances` a  "
                    + " inner join earnings e on e.id=allowance  "
                    + " where farm=" + farm + " GROUP BY allowance").executeQuery();

            while (rs.next()) {

                SUMMAIN.add("format(SUM(`" + rs.getString("earning") + "`),0) as `" + rs.getString("earning") + "`");

                nna.add("SUM(IF(AMOUNT> 0 AND allowance=" + rs.getString("allowance") + ","
                        + " (amount), 0)) AS `" + rs.getString("earning") + "`");

                SUMSEC.add("SUM(0) AS `" + rs.getString("earning") + "`");

            }


            String ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();

            String ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();

            String dd = nna.toString().replace("[", "").replace("]", "").trim();


            SQL = " SELECT id,Payno,Name, phone," + ddsumMAIN + ",format(`GROSS SALARY`,0) AS `Gross`, '' as Action"
                    + "  FROM (select employees.Payno,  employees.id, concat(fname,'  ',sname,'  ',surname) as name, phone, "
                    + " `monthly pays`.`gross salary`, " + ddsumsec + ", sum(0) as aamount  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + id + " and employees.status='Active' group by employees.id "

                    + " union all "

                    + " select Payno,`pay no`,0,0, 0 ," + dd + ",sum(amount) from allowances a "
                    + " inner join employees e on e.id=`pay no` "
                    + " where payroll='" + id + "' and e.status='Active' "
                    + " group by `pay no`)h  group by payno";



            System.out.println("SELECT * FROM (" + SQL + ")b ");

            ResultSet resultSet = conn.prepareStatement("SELECT * FROM (" + SQL + ")b ").executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                String phone = resultSet.getString("phone");
                String basicIncome = resultSet.getString("Basic income");
                String gross = resultSet.getString("Gross");

                String message = "Dear " + name + ", \nyour payment has been processed. \n"
                        + "Basic Income: " + basicIncome + ", "
                        + "\nGross Income: " + gross + ". "
                        + "\nPlease confirm receipt. Thank you!";

                SmsController smsController = new SmsController(userRepository);

                String last9Digits = phone.substring(Math.max(0, phone.length() - 9));
                phone = "254" + last9Digits;

                //String response = smsController.sendHostpinacle(phone, message);
            }




        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
            return accountsmodel.jsondata = "[{\"querystatus\" : \"" + accountsmodel.response + "\"}]";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        String jsondata = accountsmodel.jsondata;
        System.out.println("Jsondata: " + jsondata);

        return "redirect:/api/hr/payrolls";

    }


    @RequestMapping(value = "api/hr/viewpayrollpayments", method = RequestMethod.POST)
    @ResponseBody
    public String viewpayrollpayments(@RequestBody JSONObject search, HttpServletRequest request) {

        accountsmodel.dbaction("SELECT * FROM (SELECT id,Amount,transnu,Date,'' as Action FROM payrollpayments "
                + " WHERE payroll=" + search.get("id") + ")b ", 1, 0, 0, 0);

        return accountsmodel.jsondata;

    }


    @RequestMapping(path = "/api/hr/notestothebank", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<byte[]> notestothebank(@RequestBody JSONObject search, HttpServletRequest request) throws IOException {

        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        String EXTENSION = ".pdf";


        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION);


        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            String SQL = "";
            String SQL2 = "";
            String SQL3 = "";

            List<String> SUMMAIN = new ArrayList<String>();
            List<String> SUMSEC = new ArrayList<String>();
            List<String> nna = new ArrayList<String>();
            List<String> totaldeductions = new ArrayList<String>();


            ResultSet rs = conn.prepareStatement("SELECT e.id,e.`deduction` FROM `other deductions` a  "
                    + " inner join deductions e on e.id=a.deduction  "
                    + " where farm=" + farm + " GROUP BY e.id").executeQuery();

            while (rs.next()) {

                SUMMAIN.add("SUM(`" + rs.getString("deduction") + "`) as `" + rs.getString("deduction") + "`");
                nna.add("SUM(IF(AMOUNT> 0 AND deduction=" + rs.getString("id") + ","
                        + " (amount), 0)) AS `" + rs.getString("deduction") + "`");
                SUMSEC.add("SUM(0) AS `" + rs.getString("deduction") + "`");
                totaldeductions.add("SUM(`" + rs.getString("deduction") + "`)+");
            }


            String ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();
            String ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();
            String dd = nna.toString().replace("[", "").replace("]", "").trim();
            String totaldeduction = totaldeductions.toString().replace("[", "").replace("+]", "").replace("+,", "+").trim();

            SQL = " SELECT Payno,Taxable,Paye,Nhif,Nssf,Housing," + ddsumMAIN + ","
                    + " (nhif+nssf+housing+paye+" + totaldeduction + ") as 'Total deductions',"
                    + " round(`GROSS SALARY`-(nhif+nssf+housing+paye+" + totaldeduction + ")) as 'Net pay'"
                    + "  FROM (select employees.Payno, concat(fname,'  ',sname,'  ',surname) as name, "
                    + " `monthly pays`.`gross salary`,taxable,`kra tax` AS Paye,`monthly pays`.nhif,`monthly pays`.housing,`monthly pays`.nssf,"
                    + "  " + ddsumsec + "  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + search.get("id") + " and bankname='" + search.get("itembank") + "' "
                    + " group by employees.id "

                    + " union all "

                    + " select e.Payno,0,0,0,0,0,0,0 ," + dd + " from `other deductions` a "
                    + " inner join employees e on e.id=a.`payno` "
                    + " where payroll='" + search.get("id") + "'  "
                    + " and bankname='" + search.get("itembank") + "' group by a.`payno`)h  group by payno";


            SUMMAIN = new ArrayList<String>();
            SUMSEC = new ArrayList<String>();
            nna = new ArrayList<String>();

            rs = conn.prepareStatement("SELECT allowance,`earning` FROM `allowances` a  "
                    + " inner join earnings e on e.id=allowance  "
                    + " where farm=" + farm + " GROUP BY allowance").executeQuery();

            while (rs.next()) {

                SUMMAIN.add("SUM(`" + rs.getString("earning") + "`) as `" + rs.getString("earning") + "`");

                nna.add("SUM(IF(AMOUNT> 0 AND allowance=" + rs.getString("allowance") + ","
                        + " (amount), 0)) AS `" + rs.getString("earning") + "`");

                SUMSEC.add("SUM(0) AS `" + rs.getString("earning") + "`");

            }


            ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();
            ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();
            dd = nna.toString().replace("[", "").replace("]", "").trim();


            SQL2 = " SELECT Payno,Name,idnu,bankname,accountnumber,branchname,`MONTH`,YEAR,"
                    + " " + ddsumMAIN + ",`GROSS SALARY` AS `Gross`"
                    + " FROM (select employees.Payno,idnu,bankname,accountnumber,branchname, "
                    + " MONTHNAME(STR_TO_DATE(`month`, '%m')) AS `MONTH`,YEAR,  employees.id, "
                    + " concat(fname,'  ',sname,'  ',surname) as name, "
                    + " `monthly pays`.`gross salary`, " + ddsumsec + ", sum(0) as aamount  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + search.get("id") + " and bankname='" + search.get("itembank") + "' "
                    + " group by employees.id "

                    + " union all "

                    + " select Payno,`pay no`,0,0,0,0,0,0,0,0 ," + dd + ",sum(amount) from allowances a "
                    + " inner join employees e on e.id=`pay no` "
                    + " where payroll='" + search.get("id") + "' and bankname='" + search.get("itembank") + "'  "
                    + " group by `pay no`)h  group by payno";


            SQL3 = " Select * from (Select ifnull(amount,0) as amount,ifnull(transnu,'') as transnu,a.Payno,idnu,"
                    + " '" + search.get("itembank") + "' as bankname,accountnumber,branchname,`MONTH`,YEAR"
                    + " ,Name,`Net pay` from (" + SQL + ")a left join (" + SQL2 + ")b on a.payno=b.payno "
                    + " left join payrollpayments s on s.payroll=" + search.get("id") + " "
                    + " and bank='" + search.get("itembank") + "')a ";


            testReport(SQL3.replace(",,", ",").replace("+])", ")").
                            replace(", from `other deductions`", "from `other deductions`").
                            replace("nssf,    from employees", "nssf  from employees"), notestothebank, "horizontalpayslips", "", "", null, "", "",
                    uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION,
                    farm, Integer.parseInt(search.get("id").toString()));


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = java.util.Base64.getEncoder().encode(inFileBytes);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION;
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;

    }


    @RequestMapping(path = "/api/hr/notestothebankexcel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<byte[]> notestothebankexcel(@RequestBody JSONObject search, HttpServletRequest request) throws IOException {

        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        String EXTENSION = ".csv";

        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION);


        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            String SQL = "";
            String SQL2 = "";
            String SQL3 = "";

            List<String> SUMMAIN = new ArrayList<String>();
            List<String> SUMSEC = new ArrayList<String>();
            List<String> nna = new ArrayList<String>();
            List<String> totaldeductions = new ArrayList<String>();


            ResultSet rs = conn.prepareStatement("SELECT e.id,e.`deduction` FROM `other deductions` a  "
                    + " inner join deductions e on e.id=a.deduction  "
                    + " where farm=" + farm + " GROUP BY e.id").executeQuery();

            while (rs.next()) {

                SUMMAIN.add("SUM(`" + rs.getString("deduction") + "`) as `" + rs.getString("deduction") + "`");
                nna.add("SUM(IF(AMOUNT> 0 AND deduction=" + rs.getString("id") + ","
                        + " (amount), 0)) AS `" + rs.getString("deduction") + "`");
                SUMSEC.add("SUM(0) AS `" + rs.getString("deduction") + "`");
                totaldeductions.add("SUM(`" + rs.getString("deduction") + "`)+");
            }


            String ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();
            String ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();
            String dd = nna.toString().replace("[", "").replace("]", "").trim();
            String totaldeduction = totaldeductions.toString().replace("[", "").replace("+]", "").replace("+,", "+").trim();

            SQL = " SELECT Payno,Taxable,Paye,Nhif,Nssf,Housing," + ddsumMAIN + ","
                    + " (nhif+nssf+housing+paye+" + totaldeduction + ") as 'Total deductions',"
                    + " round(`GROSS SALARY`-(nhif+nssf+housing+paye+" + totaldeduction + ")) as 'Net pay'"
                    + "  FROM (select employees.Payno, concat(fname,'  ',sname,'  ',surname) as name, "
                    + " `monthly pays`.`gross salary`,taxable,`kra tax` AS Paye,`monthly pays`.nhif,`monthly pays`.housing,`monthly pays`.nssf,"
                    + "  " + ddsumsec + "  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + search.get("id") + " and bankname='" + search.get("itembank") + "' "
                    + " group by employees.id "

                    + " union all "

                    + " select e.Payno,0,0,0,0,0,0,0 ," + dd + " from `other deductions` a "
                    + " inner join employees e on e.id=a.`payno` "
                    + " where payroll='" + search.get("id") + "'  "
                    + " and bankname='" + search.get("itembank") + "' group by a.`payno`)h  group by payno";


            SUMMAIN = new ArrayList<String>();
            SUMSEC = new ArrayList<String>();
            nna = new ArrayList<String>();

            rs = conn.prepareStatement("SELECT allowance,`earning` FROM `allowances` a  "
                    + " inner join earnings e on e.id=allowance  "
                    + " where farm=" + farm + " GROUP BY allowance").executeQuery();

            while (rs.next()) {

                SUMMAIN.add("SUM(`" + rs.getString("earning") + "`) as `" + rs.getString("earning") + "`");

                nna.add("SUM(IF(AMOUNT> 0 AND allowance=" + rs.getString("allowance") + ","
                        + " (amount), 0)) AS `" + rs.getString("earning") + "`");

                SUMSEC.add("SUM(0) AS `" + rs.getString("earning") + "`");

            }


            ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();
            ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();
            dd = nna.toString().replace("[", "").replace("]", "").trim();


            SQL2 = " SELECT Payno,Name,idnu,bankname,accountnumber,branchname,`MONTH`,YEAR,"
                    + " " + ddsumMAIN + ",`GROSS SALARY` AS `Gross`"
                    + " FROM (select employees.Payno,idnu,bankname,accountnumber,branchname, "
                    + " MONTHNAME(STR_TO_DATE(`month`, '%m')) AS `MONTH`,YEAR,  employees.id, "
                    + " concat(fname,'  ',sname,'  ',surname) as name, "
                    + " `monthly pays`.`gross salary`, " + ddsumsec + ", sum(0) as aamount  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + search.get("id") + " and bankname='" + search.get("itembank") + "' "
                    + " group by employees.id "

                    + " union all "

                    + " select Payno,`pay no`,0,0,0,0,0,0,0,0 ," + dd + ",sum(amount) from allowances a "
                    + " inner join employees e on e.id=`pay no` "
                    + " where payroll='" + search.get("id") + "' and bankname='" + search.get("itembank") + "'  "
                    + " group by `pay no`)h  group by payno";


            SQL3 = " Select * from (Select ifnull(amount,0) as amount,ifnull(transnu,'') as transnu,a.Payno,idnu,bankname,"
                    + " accountnumber,branchname,`MONTH`,YEAR"
                    + " ,Name,`Net pay` from (" + SQL + ")a left join (" + SQL2 + ")b on a.payno=b.payno "
                    + " left join payrollpayments s on s.payroll=" + search.get("id") + ""
                    + " and bank='" + search.get("itembank") + "')a ";


            testReport(SQL3.replace(",,", ",").replace("+])", ")").
                            replace(", from `other deductions`", "from `other deductions`").
                            replace("nssf,    from employees", "nssf  from employees"), notestothebank, "notestothebankxlsx", "", "", null, "", "",
                    uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION,
                    farm, Integer.parseInt(search.get("id").toString()));


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = java.util.Base64.getEncoder().encode(inFileBytes);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION;
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;

    }


    @RequestMapping(path = "/api/hr/netpay", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<byte[]> netpay(@RequestBody JSONObject search, HttpServletRequest request) throws IOException {

        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        String EXTENSION = ".pdf";

        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION);


        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            String SQL = "";
            String SQL2 = "";
            String SQL3 = "";

            List<String> SUMMAIN = new ArrayList<String>();
            List<String> SUMSEC = new ArrayList<String>();
            List<String> nna = new ArrayList<String>();
            List<String> totaldeductions = new ArrayList<String>();


            ResultSet rs = conn.prepareStatement("SELECT e.id,e.`deduction` FROM `other deductions` a  "
                    + " inner join deductions e on e.id=a.deduction  "
                    + " where farm=" + farm + " GROUP BY e.id").executeQuery();

            while (rs.next()) {

                SUMMAIN.add("SUM(`" + rs.getString("deduction") + "`) as `" + rs.getString("deduction") + "`");
                nna.add("SUM(IF(AMOUNT> 0 AND deduction=" + rs.getString("id") + ","
                        + " (amount), 0)) AS `" + rs.getString("deduction") + "`");
                SUMSEC.add("SUM(0) AS `" + rs.getString("deduction") + "`");
                totaldeductions.add("SUM(`" + rs.getString("deduction") + "`)+");
            }


            String ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();
            String ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();
            String dd = nna.toString().replace("[", "").replace("]", "").trim();
            String totaldeduction = totaldeductions.toString().replace("[", "").replace("+]", "").replace("+,", "+").trim();

            SQL = " SELECT Payno,Taxable,Paye,Nhif,Nssf,Housing," + ddsumMAIN + ","
                    + " (nhif+nssf+housing+paye+" + totaldeduction + ") as 'Total deductions',"
                    + " round(`GROSS SALARY`-(nhif+nssf+housing+paye+" + totaldeduction + ")) as 'Net pay'"
                    + "  FROM (select employees.Payno, concat(fname,'  ',sname,'  ',surname) as name, "
                    + " `monthly pays`.`gross salary`,taxable,`kra tax` AS Paye,`monthly pays`.nhif,`monthly pays`.housing,`monthly pays`.nssf,"
                    + "  " + ddsumsec + "  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + search.get("id") + " group by employees.id "

                    + " union all "

                    + " select e.Payno,0,0,0,0,0,0,0 ," + dd + " from `other deductions` a "
                    + " inner join employees e on e.id=a.`payno` "
                    + " where payroll='" + search.get("id") + "'  group by a.`payno`)h  group by payno";


            SUMMAIN = new ArrayList<String>();
            SUMSEC = new ArrayList<String>();
            nna = new ArrayList<String>();

            rs = conn.prepareStatement("SELECT allowance,`earning` FROM `allowances` a  "
                    + " inner join earnings e on e.id=allowance  "
                    + " where farm=" + farm + " GROUP BY allowance").executeQuery();

            while (rs.next()) {

                SUMMAIN.add("SUM(`" + rs.getString("earning") + "`) as `" + rs.getString("earning") + "`");
                nna.add("SUM(IF(AMOUNT> 0 AND allowance=" + rs.getString("allowance") + ","
                        + " (amount), 0)) AS `" + rs.getString("earning") + "`");
                SUMSEC.add("SUM(0) AS `" + rs.getString("earning") + "`");


            }


            ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();
            ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();
            dd = nna.toString().replace("[", "").replace("]", "").trim();


            SQL2 = " SELECT Payno,Name,idnu,bankname,accountnumber,branchname,`MONTH`,YEAR,"
                    + " " + ddsumMAIN + ",`GROSS SALARY` AS `Gross`"
                    + " FROM (select employees.Payno,idnu,bankname,accountnumber,branchname, "
                    + " MONTHNAME(STR_TO_DATE(`month`, '%m')) AS `MONTH`,YEAR,  employees.id, "
                    + " concat(fname,'  ',sname,'  ',surname) as name, "
                    + " `monthly pays`.`gross salary`, " + ddsumsec + ", sum(0) as aamount  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + search.get("id") + " group by employees.id "

                    + " union all "

                    + " select Payno,`pay no`,0,0,0,0,0,0,0,0 ," + dd + ",sum(amount) from allowances a "
                    + " inner join employees e on e.id=`pay no` "
                    + " where payroll='" + search.get("id") + "' group by `pay no`)h  group by payno";


            SQL3 = " Select * from (Select ifnull(amount,0) as amount,transnu,a.Payno,idnu,bankname,accountnumber,branchname,`MONTH`,YEAR"
                    + " ,Name,`Net pay` from (" + SQL + ")a left join (" + SQL2 + ")b on a.payno=b.payno "
                    + " left join payrollpayments s on s.payroll=" + search.get("id") + ")a ";


            testReport(SQL3.replace(",,", ",").replace("+])", ")").
                            replace(", from `other deductions`", "from `other deductions`").
                            replace("nssf,    from employees", "nssf  from employees"), net_pay, "horizontalpayslips", "", "", null, "", "",
                    uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION,
                    farm, Integer.parseInt(search.get("id").toString()));


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = java.util.Base64.getEncoder().encode(inFileBytes);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION;
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;

    }


    @RequestMapping(path = "/api/hr/netgativepay", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<byte[]> netgativepay(@RequestBody JSONObject search, HttpServletRequest request) throws IOException {

        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        String EXTENSION = ".pdf";

        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION);


        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            String SQL = "";
            String SQL2 = "";
            String SQL3 = "";

            List<String> SUMMAIN = new ArrayList<String>();
            List<String> SUMSEC = new ArrayList<String>();
            List<String> nna = new ArrayList<String>();
            List<String> totaldeductions = new ArrayList<String>();


            ResultSet rs = conn.prepareStatement("SELECT e.id,e.`deduction` FROM `other deductions` a  "
                    + " inner join deductions e on e.id=a.deduction  "
                    + " where farm=" + farm + " GROUP BY e.id").executeQuery();

            while (rs.next()) {

                SUMMAIN.add("SUM(`" + rs.getString("deduction") + "`) as `" + rs.getString("deduction") + "`");
                nna.add("SUM(IF(AMOUNT> 0 AND deduction=" + rs.getString("id") + ","
                        + " (amount), 0)) AS `" + rs.getString("deduction") + "`");
                SUMSEC.add("SUM(0) AS `" + rs.getString("deduction") + "`");
                totaldeductions.add("SUM(`" + rs.getString("deduction") + "`)+");
            }


            String ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();
            String ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();
            String dd = nna.toString().replace("[", "").replace("]", "").trim();
            String totaldeduction = totaldeductions.toString().replace("[", "").replace("+]", "").replace("+,", "+").trim();

            SQL = " SELECT Payno,Taxable,Paye,Nhif,Nssf,Housing," + ddsumMAIN + ","
                    + " (nhif+nssf+housing+paye+" + totaldeduction + ") as 'Total deductions',"
                    + " round(`GROSS SALARY`-(nhif+nssf+housing+paye+" + totaldeduction + ")) as 'Net pay'"
                    + "  FROM (select employees.Payno, concat(fname,'  ',sname,'  ',surname) as name, "
                    + " `monthly pays`.`gross salary`,taxable,`kra tax` AS Paye,`monthly pays`.nhif,`monthly pays`.housing,`monthly pays`.nssf,"
                    + "  " + ddsumsec + "  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + search.get("id") + " group by employees.id "

                    + " union all "

                    + " select e.Payno,0,0,0,0,0,0,0 ," + dd + " from `other deductions` a "
                    + " inner join employees e on e.id=a.`payno` "
                    + " where payroll='" + search.get("id") + "'  group by a.`payno`)h  group by payno";


            SUMMAIN = new ArrayList<String>();
            SUMSEC = new ArrayList<String>();
            nna = new ArrayList<String>();

            rs = conn.prepareStatement("SELECT allowance,`earning` FROM `allowances` a  "
                    + " inner join earnings e on e.id=allowance  "
                    + " where farm=" + farm + " GROUP BY allowance").executeQuery();

            while (rs.next()) {

                SUMMAIN.add("SUM(`" + rs.getString("earning") + "`) as `" + rs.getString("earning") + "`");
                nna.add("SUM(IF(AMOUNT> 0 AND allowance=" + rs.getString("allowance") + ","
                        + " (amount), 0)) AS `" + rs.getString("earning") + "`");
                SUMSEC.add("SUM(0) AS `" + rs.getString("earning") + "`");


            }


            ddsumMAIN = SUMMAIN.toString().replace("[", "").replace("]", "").trim();
            ddsumsec = SUMSEC.toString().replace("[", "").replace("]", "").trim();
            dd = nna.toString().replace("[", "").replace("]", "").trim();


            SQL2 = " SELECT Payno,Name,idnu,bankname,accountnumber,branchname,`MONTH`,YEAR,"
                    + " " + ddsumMAIN + ",`GROSS SALARY` AS `Gross`"
                    + " FROM (select employees.Payno,idnu,bankname,accountnumber,branchname, "
                    + " MONTHNAME(STR_TO_DATE(`month`, '%m')) AS `MONTH`,YEAR,  employees.id, "
                    + " concat(fname,'  ',sname,'  ',surname) as name, "
                    + " `monthly pays`.`gross salary`, " + ddsumsec + ", sum(0) as aamount  from employees "
                    + " inner join `monthly pays` on `monthly pays`.payno=employees.id "
                    + " where payroll=" + search.get("id") + " group by employees.id "

                    + " union all "

                    + " select Payno,`pay no`,0,0,0,0,0,0,0,0 ," + dd + ",sum(amount) from allowances a "
                    + " inner join employees e on e.id=`pay no` "
                    + " where payroll='" + search.get("id") + "' group by `pay no`)h  group by payno";


            SQL3 = " Select * from (Select ifnull(amount,0) as amount,transnu,a.Payno,idnu,bankname,"
                    + " accountnumber,branchname,`MONTH`,YEAR"
                    + " ,Name,`Net pay` from (" + SQL + ")a left join (" + SQL2 + ")b on a.payno=b.payno "
                    + " left join payrollpayments s on s.payroll=" + search.get("id") + ")a where `Net pay`<0 ";


            testReport(SQL3.replace(",,", ",").replace("+])", ")").
                            replace(", from `other deductions`", "from `other deductions`").
                            replace("nssf,    from employees", "nssf  from employees"), negative_pay, "horizontalpayslips", "", "", null, "", "",
                    uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION,
                    farm, Integer.parseInt(search.get("id").toString()));


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = java.util.Base64.getEncoder().encode(inFileBytes);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION;
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;

    }


    @RequestMapping(path = "/api/hr/Deductions_Report", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<byte[]> Deductions_Report(@RequestBody JSONObject search, HttpServletRequest request) throws IOException {

        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        String EXTENSION = ".pdf";

        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION);


        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            String SQL = "select fname,sname,surname,employees.id,"
                    + " employees.PAYNO,AMOUNT,d.DEDUCTION, "
                    + " month(DATE) AS MONTH, year(DATE) AS YEAR  from `other deductions` "
                    + " inner join employees on `other deductions`.payno = employees.id "
                    + " inner join deductions d on d.id=`other deductions`.deduction"
                    + " WHERE d.id='" + search.get("item") + "'  and payroll=" + search.get("id") + " "
                    + " group by `other deductions`.payno HAVING amount>0";


            testReport(SQL, Deductions_Report, "horizontalpayslips", "", "", null, "", "",
                    uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION,
                    farm, Integer.parseInt(search.get("id").toString()));


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = java.util.Base64.getEncoder().encode(inFileBytes);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION;
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;

    }


    @RequestMapping(path = "/api/hr/Earnings_Report", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<byte[]> Earnings_Report(@RequestBody JSONObject search, HttpServletRequest request) throws IOException {

        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        String EXTENSION = ".pdf";

        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION);


        int farm = farm().getId();

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();

            String SQL = "select fname,sname,surname,e.id,"
                    + " e.PAYNO,a.AMOUNT,n.Earning, "
                    + " month(DATE) AS MONTH, year(DATE) AS YEAR  from allowances a "
                    + " inner join employees e ON a.`PAY NO` = e.id "
                    + " inner join earnings n on n.id=a.ALLOWANCE "
                    + " WHERE n.id='" + search.get("item") + "'  and payroll='" + search.get("id") + "' "
                    + " group by a.`PAY NO` HAVING amount>0";


            testReport(SQL, Earnings_Report, "horizontalpayslips", "", "", null, "", "",
                    uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + EXTENSION,
                    farm, Integer.parseInt(search.get("id").toString()));


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.response = "an error occured";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        byte[] inFileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        byte[] contents = java.util.Base64.getEncoder().encode(inFileBytes);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = RequestContextHolder.
                currentRequestAttributes().getSessionId() + EXTENSION;
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;

    }


    public void testReport(String SQL, String reporttemplate, String report, String remarks, String param2, List<String> sql1,
                           String Headermsg, String subttlestyle, String filepath, int farm, int payroll) throws Exception {
        Connection conn = null;
        System.out.println(SQL);
        try {
            conn = DbConnector.getConnection();

            JasperPrint jp = null;

            if (report.equals("registergrombasictogross") || report.equals("registergromgrosstonet") ||
                    report.equals("registercombined")) {

                final Map<String, Object> params = new HashMap<>();
                JasperReport jr;
                DynamicReport dr;
                dr = buildReport(SQL, reporttemplate, report, sql1, Headermsg, subttlestyle, payroll);
                params.put("schid", "" + farm);

                jr = DynamicJasperHelper.generateJasperReport(dr, getLayoutManager(), params);
                if (conn != null) {
                    jp = JasperFillManager.fillReport(jr, params, conn);
                } else {
                    jp = JasperFillManager.fillReport(jr, params);
                }

                jp.setOrientation(OrientationEnum.LANDSCAPE);
                jp.setPageWidth(850);
                jp.setPageHeight(560);


                JasperExportManager.exportReportToPdfFile(jp, filepath);


            } else if (report.equals("horizontalpayslips")) {

                Map<String, Object> param = new HashMap<>();

                param.put("schid", "" + farm);

                final InputStream ip = getClass().getResourceAsStream(reporttemplate);

                JasperDesign jd = JRXmlLoader.load(ip);

                JRDesignQuery newQuery = new JRDesignQuery();
                newQuery.setText(SQL);
                jd.setQuery(newQuery);
                JasperReport jr = JasperCompileManager.compileReport(jd);

                jp = JasperFillManager.fillReport(jr, param, conn);

                JasperExportManager.exportReportToPdfFile(jp, filepath);

            } else if (report.equals("notestothebankxlsx")) {

                Map<String, Object> param = new HashMap<>();

                param.put("schid", "" + farm);

                final InputStream ip = getClass().getResourceAsStream(reporttemplate);

                JasperDesign jd = JRXmlLoader.load(ip);

                JRDesignQuery newQuery = new JRDesignQuery();
                newQuery.setText(SQL);
                jd.setQuery(newQuery);
                JasperReport jr = JasperCompileManager.compileReport(jd);

                jp = JasperFillManager.fillReport(jr, param, conn);

                JRCsvExporter exporter = new JRCsvExporter();

                exporter.setExporterInput(new SimpleExporterInput(jp));
                exporter.setExporterOutput(new SimpleWriterExporterOutput(filepath));
                exporter.exportReport();

            }


        } catch (Exception e1) {
            e1.printStackTrace();
            accountsmodel.jsondata = "[{\"querystatus\" : \"" + e1.getMessage() + "\"}]";
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }


    }

    protected LayoutManager getLayoutManager() {
        return (LayoutManager) new ClassicLayoutManager();
    }


    public int getYear() {
        return Calendar.getInstance().get(1);
    }


    public DynamicReport buildReport(String SQL, String reporttemplate, String report,
                                     List<String> sql1, String Headermsg, String subttlestyle, int payroll) throws Exception {

        Style detailStyledontshow = new Style("detail2");
        detailStyledontshow.setFont(new Font(0, null, false));
        detailStyledontshow.setBorder(Border.NO_BORDER());
        detailStyledontshow.setBorderBottom(Border.THIN());

        Style detailStyle = new Style("detail");
        detailStyle.setFont(new Font(7, null, false));
        detailStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        detailStyle.setBorder(Border.THIN());


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
        titleStyle.setFont(new Font(7, null, true));
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

        Integer margin = 3;


        drb.setTitleStyle(titleStyle).setTitle(Headermsg).setDetailHeight((new Integer(0)).intValue()).setLeftMargin(margin.intValue())
                .setRightMargin(margin.intValue()).setTopMargin(margin.intValue()).setBottomMargin(margin.intValue());

        drb.setSubtitleStyle(titleStyle).setSubtitle(subttlestyle).setDetailHeight((new Integer(0)).intValue()).setLeftMargin(margin.intValue())
                .setRightMargin(margin.intValue()).setTopMargin(margin.intValue()).setBottomMargin(margin.intValue());

        DJGroup g1 = null;
        GroupBuilder gb1 = new GroupBuilder();

        GroupBuilder gb2 = new GroupBuilder();
        DJGroup g2 = null;


        if (report.equals("registergrombasictogross")) {

            AbstractColumn rlf = ColumnBuilder.getNew()
                    .setColumnProperty("groupby", String.class.getName())
                    .setWidth(new Integer(0)).build();
            drb.addColumn(rlf);

            AbstractColumn columnBranch = ColumnBuilder.getNew()
                    .setColumnProperty("PAYNO", String.class.getName())
                    .setTitle("STAFF NO").setWidth(new Integer(25))
                    .setStyle(detailStyle).setHeaderStyle(headerStyle).build();
            drb.addColumn(columnBranch);

            columnBranch = ColumnBuilder.getNew()
                    .setColumnProperty("NAME", String.class.getName())
                    .setTitle("NAME").setWidth(new Integer(110))
                    .setStyle(detailStyle1).setHeaderStyle(headerStyle).build();
            drb.addColumn(columnBranch);


            AbstractColumn deductions = null;

            Connection conn = null;
            try {
                conn = DbConnector.getConnection();

                ResultSet rs = conn.prepareStatement("SELECT earning from allowances  a "
                        + " inner join earnings e ON a.ALLOWANCE=e.id     "
                        + " where payroll=" + payroll + "  and visible='visible' group BY e.id"
                        + " order by case when `earning`='Basic income' then 1 else 2 end ").executeQuery();

                while (rs.next()) {

                    deductions = ColumnBuilder
                            .getNew()
                            .setColumnProperty(rs.getString("earning"),
                                    Float.class.getName()).setTitle(rs.getString("earning"))
                            .setWidth(new Integer(40)).setPattern("#,###.###")
                            .setStyle(detailStyle).setHeaderStyle(headerStyle)
                            .build();
                    drb.addColumn(deductions);

                    g2 = gb2.setCriteriaColumn((PropertyColumn) rlf)
                            .addFooterVariable(deductions, DJCalculation.SUM,
                                    groupVariables).build();
                }

                AbstractColumn GROSS = ColumnBuilder.getNew()
                        .setColumnProperty("Gross", Float.class.getName())
                        .setTitle("GROSS PAY").setWidth(new Integer(40))
                        .setPattern("#,###.###").setStyle(detailStyle)
                        .setHeaderStyle(headerStyle).build();
                drb.addColumn(GROSS);

                g2 = gb2.setCriteriaColumn((PropertyColumn) rlf)
                        .addFooterVariable(GROSS, DJCalculation.SUM, groupVariables)
                        .build();


            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (conn != null) try {
                    conn.close();
                } catch (SQLException ignore) {
                }
            }


            drb.addGroup(g2);

        } else if (report.equals("registergromgrosstonet")) {

            AbstractColumn rlf = ColumnBuilder.getNew()
                    .setColumnProperty("groupby", String.class.getName())
                    .setWidth(new Integer(0)).build();
            drb.addColumn(rlf);

            AbstractColumn columnBranch = ColumnBuilder.getNew()
                    .setColumnProperty("PAYNO", String.class.getName())
                    .setTitle("Payno").setWidth(new Integer(25))
                    .setStyle(detailStyle).setHeaderStyle(headerStyle).build();
            drb.addColumn(columnBranch);

            columnBranch = ColumnBuilder.getNew()
                    .setColumnProperty("NAME", String.class.getName())
                    .setTitle("Name").setWidth(new Integer(110))
                    .setStyle(detailStyle1).setHeaderStyle(headerStyle).build();
            drb.addColumn(columnBranch);

            AbstractColumn GROSS = ColumnBuilder.getNew()
                    .setColumnProperty("Gross", Float.class.getName())
                    .setTitle("Gross pay").setWidth(new Integer(40))
                    .setPattern("#,###.###").setStyle(detailStyle)
                    .setHeaderStyle(headerStyle).build();
            drb.addColumn(GROSS);

            g2 = gb2.setCriteriaColumn((PropertyColumn) rlf)
                    .addFooterVariable(GROSS, DJCalculation.SUM, groupVariables)
                    .build();

            GROSS = ColumnBuilder.getNew()
                    .setColumnProperty("Taxable", Float.class.getName())
                    .setTitle("Taxable").setWidth(new Integer(40))
                    .setPattern("#,###.###").setStyle(detailStyle)
                    .setHeaderStyle(headerStyle).build();
            drb.addColumn(GROSS);

            g2 = gb2.setCriteriaColumn((PropertyColumn) rlf)
                    .addFooterVariable(GROSS, DJCalculation.SUM, groupVariables)
                    .build();

            GROSS = ColumnBuilder.getNew()
                    .setColumnProperty("Paye", Float.class.getName())
                    .setTitle("Paye").setWidth(new Integer(40))
                    .setPattern("#,###.###").setStyle(detailStyle)
                    .setHeaderStyle(headerStyle).build();
            drb.addColumn(GROSS);

            g2 = gb2.setCriteriaColumn((PropertyColumn) rlf)
                    .addFooterVariable(GROSS, DJCalculation.SUM, groupVariables)
                    .build();

            GROSS = ColumnBuilder.getNew()
                    .setColumnProperty("Nhif", Float.class.getName())
                    .setTitle("Nhif").setWidth(new Integer(40))
                    .setPattern("#,###.###").setStyle(detailStyle)
                    .setHeaderStyle(headerStyle).build();
            drb.addColumn(GROSS);

            g2 = gb2.setCriteriaColumn((PropertyColumn) rlf)
                    .addFooterVariable(GROSS, DJCalculation.SUM, groupVariables)
                    .build();

            GROSS = ColumnBuilder.getNew()
                    .setColumnProperty("Nssf", Float.class.getName())
                    .setTitle("Nssf").setWidth(new Integer(40))
                    .setPattern("#,###.###").setStyle(detailStyle)
                    .setHeaderStyle(headerStyle).build();
            drb.addColumn(GROSS);

            GROSS = ColumnBuilder.getNew()
                    .setColumnProperty("Housing", Float.class.getName())
                    .setTitle("Housing").setWidth(new Integer(40))
                    .setPattern("#,###.###").setStyle(detailStyle)
                    .setHeaderStyle(headerStyle).build();
            drb.addColumn(GROSS);

            g2 = gb2.setCriteriaColumn((PropertyColumn) rlf)
                    .addFooterVariable(GROSS, DJCalculation.SUM, groupVariables)
                    .build();


            AbstractColumn deductions = null;

            Connection conn = null;
            try {
                conn = DbConnector.getConnection();

                ResultSet rs = conn.prepareStatement("SELECT e.`deduction` FROM `other deductions` a  "
                        + " inner join deductions e on e.id=a.deduction  "
                        + " where payroll=" + payroll + " GROUP BY e.id").executeQuery();

                while (rs.next()) {

                    deductions = ColumnBuilder
                            .getNew()
                            .setColumnProperty(rs.getString("deduction"),
                                    Float.class.getName()).setTitle(rs.getString("deduction"))
                            .setWidth(new Integer(40)).setPattern("#,###.###")
                            .setStyle(detailStyle).setHeaderStyle(headerStyle)
                            .build();
                    drb.addColumn(deductions);

                    g2 = gb2.setCriteriaColumn((PropertyColumn) rlf)
                            .addFooterVariable(deductions, DJCalculation.SUM,
                                    groupVariables).build();
                }


                GROSS = ColumnBuilder.getNew()
                        .setColumnProperty("Total deductions", Float.class.getName())
                        .setTitle("Total deductions").setWidth(new Integer(40))
                        .setPattern("#,###.###").setStyle(detailStyle)
                        .setHeaderStyle(headerStyle).build();
                drb.addColumn(GROSS);

                g2 = gb2.setCriteriaColumn((PropertyColumn) rlf)
                        .addFooterVariable(GROSS, DJCalculation.SUM, groupVariables)
                        .build();

                GROSS = ColumnBuilder.getNew()
                        .setColumnProperty("Net Pay", Float.class.getName())
                        .setTitle("Net Pay").setWidth(new Integer(40))
                        .setPattern("#,###.###").setStyle(detailStyle)
                        .setHeaderStyle(headerStyle).build();
                drb.addColumn(GROSS);

                g2 = gb2.setCriteriaColumn((PropertyColumn) rlf)
                        .addFooterVariable(GROSS, DJCalculation.SUM, groupVariables)
                        .build();


            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (conn != null) try {
                    conn.close();
                } catch (SQLException ignore) {
                }
            }


            drb.addGroup(g2);

        } else if (report.equals("registercombined")) {

            AbstractColumn rlf = ColumnBuilder.getNew()
                    .setColumnProperty("groupby", String.class.getName())
                    .setWidth(new Integer(0)).build();
            drb.addColumn(rlf);

            AbstractColumn columnBranch = ColumnBuilder.getNew()
                    .setColumnProperty("PAYNO", String.class.getName())
                    .setTitle("Payno").setWidth(new Integer(25))
                    .setStyle(detailStyle).setHeaderStyle(headerStyle).build();
            drb.addColumn(columnBranch);

            columnBranch = ColumnBuilder.getNew()
                    .setColumnProperty("NAME", String.class.getName())
                    .setTitle("Name").setWidth(new Integer(110))
                    .setStyle(detailStyle1).setHeaderStyle(headerStyle).build();
            drb.addColumn(columnBranch);


            AbstractColumn deductions = null;

            Connection conn = null;
            try {
                conn = DbConnector.getConnection();


                ResultSet rs = conn.prepareStatement("SELECT earning from allowances  a "
                        + " inner join earnings e ON a.ALLOWANCE=e.id     "
                        + " where payroll=" + payroll + "  and visible='visible' group BY e.id"
                        + " order by case when `earning`='Basic income' then 1 else 2 end ").executeQuery();

                while (rs.next()) {

                    deductions = ColumnBuilder
                            .getNew()
                            .setColumnProperty(rs.getString("earning"),
                                    Float.class.getName()).setTitle(rs.getString("earning"))
                            .setWidth(new Integer(40)).setPattern("#,###.###")
                            .setStyle(detailStyle).setHeaderStyle(headerStyle)
                            .build();
                    drb.addColumn(deductions);

                    g2 = gb2.setCriteriaColumn((PropertyColumn) rlf)
                            .addFooterVariable(deductions, DJCalculation.SUM,
                                    groupVariables).build();
                }


                AbstractColumn GROSS = ColumnBuilder.getNew()
                        .setColumnProperty("Gross", Float.class.getName())
                        .setTitle("Gross pay").setWidth(new Integer(40))
                        .setPattern("#,###.###").setStyle(detailStyle)
                        .setHeaderStyle(headerStyle).build();
                drb.addColumn(GROSS);

                g2 = gb2.setCriteriaColumn((PropertyColumn) rlf)
                        .addFooterVariable(GROSS, DJCalculation.SUM, groupVariables)
                        .build();

                GROSS = ColumnBuilder.getNew()
                        .setColumnProperty("Taxable", Float.class.getName())
                        .setTitle("Taxable").setWidth(new Integer(40))
                        .setPattern("#,###.###").setStyle(detailStyle)
                        .setHeaderStyle(headerStyle).build();
                drb.addColumn(GROSS);

                g2 = gb2.setCriteriaColumn((PropertyColumn) rlf)
                        .addFooterVariable(GROSS, DJCalculation.SUM, groupVariables)
                        .build();

                GROSS = ColumnBuilder.getNew()
                        .setColumnProperty("Paye", Float.class.getName())
                        .setTitle("Paye").setWidth(new Integer(40))
                        .setPattern("#,###.###").setStyle(detailStyle)
                        .setHeaderStyle(headerStyle).build();
                drb.addColumn(GROSS);

                g2 = gb2.setCriteriaColumn((PropertyColumn) rlf)
                        .addFooterVariable(GROSS, DJCalculation.SUM, groupVariables)
                        .build();

                GROSS = ColumnBuilder.getNew()
                        .setColumnProperty("Nhif", Float.class.getName())
                        .setTitle("Nhif").setWidth(new Integer(40))
                        .setPattern("#,###.###").setStyle(detailStyle)
                        .setHeaderStyle(headerStyle).build();
                drb.addColumn(GROSS);

                g2 = gb2.setCriteriaColumn((PropertyColumn) rlf)
                        .addFooterVariable(GROSS, DJCalculation.SUM, groupVariables)
                        .build();

                GROSS = ColumnBuilder.getNew()
                        .setColumnProperty("Nssf", Float.class.getName())
                        .setTitle("Nssf").setWidth(new Integer(40))
                        .setPattern("#,###.###").setStyle(detailStyle)
                        .setHeaderStyle(headerStyle).build();
                drb.addColumn(GROSS);

                g2 = gb2.setCriteriaColumn((PropertyColumn) rlf)
                        .addFooterVariable(GROSS, DJCalculation.SUM, groupVariables)
                        .build();


                GROSS = ColumnBuilder.getNew()
                        .setColumnProperty("Housing", Float.class.getName())
                        .setTitle("Housing").setWidth(new Integer(40))
                        .setPattern("#,###.###").setStyle(detailStyle)
                        .setHeaderStyle(headerStyle).build();
                drb.addColumn(GROSS);

                g2 = gb2.setCriteriaColumn((PropertyColumn) rlf)
                        .addFooterVariable(GROSS, DJCalculation.SUM, groupVariables)
                        .build();


                ResultSet rs1 = conn.prepareStatement("SELECT e.`deduction` FROM `other deductions` a  "
                        + " inner join deductions e on e.id=a.deduction  "
                        + " where payroll=" + payroll + " GROUP BY e.id").executeQuery();

                while (rs1.next()) {

                    deductions = ColumnBuilder
                            .getNew()
                            .setColumnProperty(rs1.getString("deduction"),
                                    Float.class.getName()).setTitle(rs1.getString("deduction"))
                            .setWidth(new Integer(40)).setPattern("#,###.###")
                            .setStyle(detailStyle).setHeaderStyle(headerStyle)
                            .build();
                    drb.addColumn(deductions);

                    g2 = gb2.setCriteriaColumn((PropertyColumn) rlf)
                            .addFooterVariable(deductions, DJCalculation.SUM,
                                    groupVariables).build();
                }


                GROSS = ColumnBuilder.getNew()
                        .setColumnProperty("Total deductions", Float.class.getName())
                        .setTitle("Total deductions").setWidth(new Integer(40))
                        .setPattern("#,###.###").setStyle(detailStyle)
                        .setHeaderStyle(headerStyle).build();
                drb.addColumn(GROSS);

                g2 = gb2.setCriteriaColumn((PropertyColumn) rlf)
                        .addFooterVariable(GROSS, DJCalculation.SUM, groupVariables)
                        .build();

                GROSS = ColumnBuilder.getNew()
                        .setColumnProperty("Net Pay", Float.class.getName())
                        .setTitle("Net Pay").setWidth(new Integer(40))
                        .setPattern("#,###.###").setStyle(detailStyle)
                        .setHeaderStyle(headerStyle).build();
                drb.addColumn(GROSS);

                g2 = gb2.setCriteriaColumn((PropertyColumn) rlf)
                        .addFooterVariable(GROSS, DJCalculation.SUM, groupVariables)
                        .build();


            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (conn != null) try {
                    conn.close();
                } catch (SQLException ignore) {
                }
            }


            drb.addGroup(g2);

        }


        drb.setQuery(SQL, "sql");
        drb.setUseFullPageWidth(true);
        drb.setPageSizeAndOrientation(new Page(825, 523, false));

        drb.setTemplateFile(reporttemplate, true, true, true, false);


        DynamicReport dr = drb.build();
        return dr;


    }

}
