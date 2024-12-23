package com.rickiey_innovates.juditonspringapp.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.rickiey_innovates.juditonspringapp.models.*;
import com.rickiey_innovates.juditonspringapp.repositories.*;
import com.rickiey_innovates.juditonspringapp.DbConnector;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.*;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
public class IndexController implements ErrorController {

    private final UserRepository userRepository;
    private final PaymentvoucherRepository paymentvoucherRepository;
    private final AccounttransactionRepository accounttransactionRepository;
    private Long userId() {
        return LoginController.getLoggedInUserId();
    }

    private Farm farm() {
        return userRepository.findById(userId()).orElse(null).getFarm();
    }

    public IndexController(UserRepository userRepository,
                           PaymentvoucherRepository paymentvoucherRepository,
                           AccounttransactionRepository accounttransactionRepository,
                           RoleRepository roleRepository,
                           PendingTransactionRepository pendingTransactionRepository,
                           PayrollRepository payrollRepository,
                           EmployeeRepository employeeRepository) {

        this.userRepository = userRepository;
        this.paymentvoucherRepository = paymentvoucherRepository;
        this.accounttransactionRepository = accounttransactionRepository;
        this.roleRepository = roleRepository;
        this.pendingTransactionRepository = pendingTransactionRepository;
        this.payrollRepository = payrollRepository;
        this.employeeRepository = employeeRepository;
    }

    public static final String PATH = "/error";

    private final RoleRepository roleRepository;
    private final PendingTransactionRepository pendingTransactionRepository;
    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;


    @RequestMapping(value = PATH)
    public String error() {
        return "redirect:/";
    }

    private boolean isAuthorised() {
        Set<Role> roles = userRepository.findById(userId()).get().getRoles();
        Role role = roleRepository.findByName(ERole.SHAREHOLDER).orElse(null);
        boolean authorised;
        if (roles.contains(role)) {
            authorised = true;
            System.out.println(authorised);
        } else {
            authorised = false;
            System.out.println(authorised);
        }
        return authorised;
    }

    @RequestMapping(value = "/getTheme", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String gettheme() {
        int theme = userRepository.findById(userId()).orElse(null).getTheme();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("theme", theme);
        return jsonObject.toString();
    }

    @RequestMapping(value="/updateTheme", method=RequestMethod.POST)
    @ResponseBody
    public String updateusertheme(@RequestBody String requestBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(requestBody);
            int theme = node.get("value").asInt();
            try {
                User user = userRepository.findById(userId()).orElse(null);
                user.setTheme(theme);
                userRepository.save(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "success";
    }

    @GetMapping(value = "/api/getMonthlyExpenses", produces = "application/json")
    @ResponseBody
    public List<MonthlyTransactions> MonthlyExpenses () {
        List<MonthlyTransactions> monthlyExpensesList = new ArrayList<>();
        try {
            Connection connection = DbConnector.getConnection();
            String sql = "SELECT\n" +
                         "    year,\n" +
                         "    month,\n" +
                         "    credit\n" +
                         "FROM (\n" +
                         "         SELECT\n" +
                         "             YEAR(`Date`) AS year,\n" +
                         "             DATE_FORMAT(`Date`, '%b') AS month,\n" +
                         "             SUM(debit) AS credit,\n" +
                         "             ROW_NUMBER() OVER (ORDER BY YEAR(`Date`) DESC, MONTH(`Date`) DESC) AS row_num\n" +
                         "         FROM accounttransactions where farm = '"+farm().getId()+"' \n" +
                         "         GROUP BY YEAR(`Date`), MONTH(`Date`)\n" +
                         "         ORDER BY YEAR(`Date`) DESC, MONTH(`Date`) DESC\n" +
                         "     ) AS subquery\n" +
                         "WHERE row_num <= 6\n" +
                         "ORDER BY year, month ;\n";

            System.out.println(sql);

            ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
            while(resultSet.next()) {
                MonthlyTransactions monthlyExpenses = new MonthlyTransactions(resultSet.getString("year"), resultSet.getString("month"), resultSet.getString("credit"));
                monthlyExpensesList.add(monthlyExpenses);
            }

            resultSet.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return monthlyExpensesList;
    }

    @GetMapping(value = "/api/getMonthlyIncome", produces = "application/json")
    @ResponseBody
    public List<MonthlyTransactions> MonthlyIncome () {
        List<MonthlyTransactions> monthlyTransactionsList = new ArrayList<>();
        try {
            Connection connection = DbConnector.getConnection();
            String sql = "SELECT\n" +
                         "    year,\n" +
                         "    month,\n" +
                         "    credit\n" +
                         "FROM (\n" +
                         "         SELECT\n" +
                         "             YEAR(`Date`) AS year,\n" +
                         "             DATE_FORMAT(`Date`, '%b') AS month,\n" +
                         "             SUM(credit) AS credit,\n" +
                         "             ROW_NUMBER() OVER (ORDER BY YEAR(`Date`) DESC, MONTH(`Date`) DESC) AS row_num\n" +
                         "         FROM accounttransactions where farm = "+farm().getId()+" \n" +
                         "         GROUP BY YEAR(`Date`), MONTH(`Date`)\n" +
                         "         ORDER BY YEAR(`Date`) DESC, MONTH(`Date`) DESC\n" +
                         "     ) AS subquery\n" +
                         "WHERE row_num <= 6\n" +
                         "ORDER BY year, month ;\n";

            System.out.println(sql);

            ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
            while(resultSet.next()) {
                MonthlyTransactions monthlyExpenses = new MonthlyTransactions(resultSet.getString("year"), resultSet.getString("month"), resultSet.getString("credit"));
                monthlyTransactionsList.add(monthlyExpenses);
            }

            resultSet.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return monthlyTransactionsList;
    }

/*    @GetMapping("/api/getIncomeAndExpenses")
    public ResponseEntity<?> getIncomAndExpenses() {
        List<IncomeAndExpenditure> incomeAndExpenditures = new ArrayList<>();
        try {
            IncomeAndExpenditure incomeAndExpenditure = new IncomeAndExpenditure();
            MonthlyTransactions incomes = new MonthlyTransactions( resultSet.getString("year"), resultSet.getString("month"), resultSet.getString("total"));
            incomeAndExpenditure.setIncome(incomes);
            MonthlyTransactions expenses = new MonthlyTransactions(resultSet.getString("year"), resultSet.getString("month"), resultSet.getString("total"));;
            incomeAndExpenditure.setExpenditure(expenses);
            incomeAndExpenditures.add(incomeAndExpenditure);
            return ResponseEntity.ok(incomeAndExpenditures);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getLocalizedMessage());
        }
    }*/


    @GetMapping("/home")
    public String index(Model model) {
        Connection conn = null;
        try {
            conn = DbConnector.getConnection();


            ResultSet rs = conn.prepareStatement("SELECT *,\n" +
                    "       (case\n" +
                    "            when convert(NOW(), TIME) >= '05:00:00' and\n" +
                    "                 convert(NOW(), TIME) < '12:00:00' then 'Good Morning'\n" +
                    "            when convert(NOW(), TIME) >= '12:00:00' AND convert(NOW(), TIME) < '17:00:00'\n" +
                    "                then 'Good Afternoon'\n" +
                    "            else 'Good Evening' END) AS Greetings\n" +
                    "from (SELECT s.id,\n" +
                    "             name,\n" +
                    "             SUBSTRING_INDEX(name, ' ', 2) as shortname,\n" +
                    "             logo,\n" +
                    "             s.address,\n" +
                    "             region,\n" +
                    "             username,\n" +
                    "             phone,\n" +
                    "             image\n" +
                    "      FROM users u\n" +
                    "               inner join farm s on u.farm = s.id\n" +
                    "      where sessionid ='" + RequestContextHolder.currentRequestAttributes().getSessionId() + "')d  ").executeQuery();
            while (rs.next()) {
                model.addAttribute("schoolname", rs.getString("name"));
                model.addAttribute("shortname", rs.getString("name"));
                model.addAttribute("pobox", rs.getString("address"));
                model.addAttribute("city", rs.getString("region"));
                model.addAttribute("phone", rs.getString("phone"));
                model.addAttribute("logo", rs.getString("logo"));
                model.addAttribute("email", rs.getString("username"));
                model.addAttribute("image", rs.getString("image"));
                model.addAttribute("Greetings", rs.getString("Greetings"));
            }

            double balance;

            List<Accounttransaction> accounttransactions = accounttransactionRepository.findByFarm(farm());
            double creditSum = accounttransactions.stream()
                    .filter(transaction -> transaction.getCredit() != null)
                    .mapToDouble(Accounttransaction::getCredit)
                    .sum();

            double debitSum = accounttransactions.stream()
                    .filter(transaction -> transaction.getDebit() != null)
                    .mapToDouble(Accounttransaction::getDebit)
                    .sum();

            if (Double.isNaN(creditSum) && Double.isNaN(debitSum)) {
                balance = 0.0;
            }

            // If creditSum is null, consider it as 0
            creditSum = Double.isNaN(creditSum) ? 0.0 : creditSum;

            // If debitSum is null, consider it as 0
            debitSum = Double.isNaN(debitSum) ? 0.0 : debitSum;

            balance = creditSum - debitSum;

            int pendingVouchers = pendingTransactionRepository.findAll().size();
            int pendingPayrolls = payrollRepository.findByStatusAndFarm("Paid", farm()).size();
            int employees = employeeRepository.findByFarm(farm()).size();

            model.addAttribute("balance", formattedDouble(balance));
            model.addAttribute("income", formattedDouble(creditSum));
            model.addAttribute("expenses", formattedDouble(debitSum));
            model.addAttribute("pendingVouchers", pendingVouchers);
            model.addAttribute("pendingPayrolls", pendingPayrolls);
            model.addAttribute("employees", employees);

            System.out.println(model);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException sQLException) {}
        }
        return "index";
    }

    private String formattedDouble(double amount) {
        Locale kenyaLocale = new Locale("en", "KE");

        // Create a NumberFormat for the Kenyan currency
        NumberFormat kenyanCurrencyFormat = NumberFormat.getCurrencyInstance(kenyaLocale);

        // Set currency to Kenyan Shillings
        Currency kenyanCurrency = Currency.getInstance("KES");
        kenyanCurrencyFormat.setCurrency(kenyanCurrency);

        return kenyanCurrencyFormat.format(amount);
    }

    @GetMapping({"/crop"})
    public String crop(Model model, HttpServletRequest request) {
        User user = userRepository.findById(userId()).get();
        try {
            Connection connection = DbConnector.getConnection();
            String sql = "SELECT (SELECT count(id)\n" +
                    "        FROM users\n" +
                    "        where farm = "+farm().getId()+")          AS memberTotal,\n" +
                    "       (SELECT ifnull(FORMAT(SUM(credit), 0), 0)\n" +
                    "        FROM accounttransactions\n" +
                    "        where farm = "+farm().getId()+")                                        AS income,\n" +
                    "       (SELECT ifnull(FORMAT(SUM(debit), 0), 0)\n" +
                    "        FROM accounttransactions\n" +
                    "        where farm = "+farm().getId()+")                                        AS expenses,\n" +
                    "       (SELECT ifnull(FORMAT((SUM(credit) - SUM(debit)), 0), 0)\n" +
                    "        FROM accounttransactions\n" +
                    "        where farm = "+farm().getId()+")                                        AS balance,\n" +
                    "       (SELECT ifnull(FORMAT(SUM(debit), 0), 0)\n" +
                    "        FROM accounttransactions\n" +
                    "        WHERE farm = "+farm().getId()+"\n" +
                    "         )                    AS currentMonthExpenses,\n" +
                    "       (SELECT ifnull(FORMAT(SUM(debit), 0), 0)\n" +
                    "        FROM accounttransactions\n" +
                    "        WHERE farm = "+farm().getId()+"\n" +
                    "          and MONTH(Date) = MONTH(CURDATE() - INTERVAL 1 MONTH)) AS previousMonthExpenses,\n" +
                    "       IFNULL(\n" +
                    "                       ((SELECT SUM(debit)\n" +
                    "                         FROM accounttransactions\n" +
                    "                         WHERE farm = "+farm().getId()+"\n" +
                    "                           and MONTH(Date) = MONTH(CURDATE())) /\n" +
                    "                        (SELECT SUM(debit)\n" +
                    "                         FROM accounttransactions\n" +
                    "                         WHERE farm = "+farm().getId()+"\n" +
                    "                           and MONTH(Date) = MONTH(CURDATE() - INTERVAL 1 MONTH))) * 100 - 100,\n" +
                    "                       0)                                        AS percentageIncrease;";
            ResultSet rs = connection.prepareStatement(sql).executeQuery();
            SummaryDTO summaryDTO = new SummaryDTO();
            while(rs.next()) {
                summaryDTO.setMemberTotal(rs.getInt("memberTotal"));
                summaryDTO.setIncome(rs.getString("income"));
                summaryDTO.setExpenses(rs.getString("expenses"));
                summaryDTO.setBalance(rs.getString("balance"));
                summaryDTO.setPercentageExpensesIncrease(rs.getDouble("percentageIncrease"));
            }
            rs.close();
            connection.close();
            model.addAttribute("summary", summaryDTO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Accounttransaction> accounttransactions = accounttransactionRepository.findByPayeeNameNotLikeAndChurchLikeOrderByIdDesc("MERU CENTRAL COFFEE CO-OPERATIVE UNION LTD", farm());
        model.addAttribute("transactions", accounttransactions);
        model.addAttribute("user", user);
        model.addAttribute("page", "dashboard");
        model.addAttribute("main", "farm");
        model.addAttribute("requestURI", request.getRequestURI());
        return "crop/crops";
    }

    @GetMapping({"/finance"})
    public String index(Model model, HttpServletRequest request) {
        User user = userRepository.findById(userId()).get();
        try (Connection connection = DbConnector.getConnection()) {
            String sql = "SELECT (SELECT count(id)\n" +
                    "        FROM users\n" +
                    "        where farm = "+farm().getId()+")          AS memberTotal,\n" +
                    "       (SELECT ifnull(FORMAT(SUM(credit), 0), 0)\n" +
                    "        FROM accounttransactions\n" +
                    "        where farm = "+farm().getId()+")                                        AS income,\n" +
                    "       (SELECT ifnull(FORMAT(SUM(debit), 0), 0)\n" +
                    "        FROM accounttransactions\n" +
                    "        where farm = "+farm().getId()+")                                        AS expenses,\n" +
                    "       (SELECT ifnull(FORMAT((SUM(credit) - SUM(debit)), 0), 0)\n" +
                    "        FROM accounttransactions\n" +
                    "        where farm = "+farm().getId()+")                                        AS balance,\n" +
                    "       (SELECT ifnull(FORMAT(SUM(debit), 0), 0)\n" +
                    "        FROM accounttransactions\n" +
                    "        WHERE farm = "+farm().getId()+"\n" +
                    "         )                    AS currentMonthExpenses,\n" +
                    "       (SELECT ifnull(FORMAT(SUM(debit), 0), 0)\n" +
                    "        FROM accounttransactions\n" +
                    "        WHERE farm = "+farm().getId()+"\n" +
                    "          and MONTH(Date) = MONTH(CURDATE() - INTERVAL 1 MONTH)) AS previousMonthExpenses,\n" +
                    "       IFNULL(\n" +
                    "                       ((SELECT SUM(debit)\n" +
                    "                         FROM accounttransactions\n" +
                    "                         WHERE farm = "+farm().getId()+"\n" +
                    "                           and MONTH(Date) = MONTH(CURDATE())) /\n" +
                    "                        (SELECT SUM(debit)\n" +
                    "                         FROM accounttransactions\n" +
                    "                         WHERE farm = "+farm().getId()+"\n" +
                    "                           and MONTH(Date) = MONTH(CURDATE() - INTERVAL 1 MONTH))) * 100 - 100,\n" +
                    "                       0)                                        AS percentageIncrease;";
            ResultSet rs = connection.prepareStatement(sql).executeQuery();
            SummaryDTO summaryDTO = new SummaryDTO();
            while(rs.next()) {
                summaryDTO.setMemberTotal(rs.getInt("memberTotal"));
                summaryDTO.setIncome(rs.getString("income"));
                summaryDTO.setExpenses(rs.getString("expenses"));
                summaryDTO.setBalance(rs.getString("balance"));
                summaryDTO.setPercentageExpensesIncrease(rs.getDouble("percentageIncrease"));
            }
            rs.close();
            connection.close();
            model.addAttribute("summary", summaryDTO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Accounttransaction> accounttransactions = accounttransactionRepository.findByPayeeNameNotLikeAndChurchLikeOrderByIdDesc("MERU CENTRAL COFFEE CO-OPERATIVE UNION LTD", farm());
        model.addAttribute("transactions", accounttransactions);
        model.addAttribute("user", user);
        model.addAttribute("page", "dashboard");
        model.addAttribute("main", "farm");
        model.addAttribute("requestURI", request.getRequestURI());
        return "finance/dashboard";
    }

    @GetMapping("/hr")
    public String gotohr(Model model) {

        if(!isAuthorised()) {
            return "accessdenied";
        }

        accountsmodel.dbaction("SELECT *\n" +
                "from (SELECT s.id as id,\n" +
                "             name,\n" +
                "             SUBSTRING_INDEX(name, ' ', 2) as\n" +
                "                                              shortname,\n" +
                "             logo,\n" +
                "             s.address,\n" +
                "             region,\n" +
                "             phone,\n" +
                "             username,\n" +
                "             logo         as image\n" +
                "      FROM users u\n" +
                "               inner join farm s on u.farm = s.id\n" +
                "      where u.sessionid= '"+RequestContextHolder.currentRequestAttributes().getSessionId()+"') d; ",1, 0, 0,0);

        List<accountsmodel> newListdf = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("email", 	 	 newListdf.get(0).activeProperty("username").getValue());
        model.addAttribute("image", 	 	 newListdf.get(0).activeProperty("image").getValue());
        model.addAttribute("school", farm().getId());


        accountsmodel.dbaction("SELECT * from (SELECT\n" +
                "                   sum(case when a.STATUS='Active' then 1 ELSE 0 END) AS activeemployees,\n" +
                "                   sum(case when a.STATUS='Active' AND `pay cycle`='MONTHLY' then 1 ELSE 0 END) AS activepermanentemployees,\n" +
                "                   sum(case when a.STATUS='Active' AND `pay cycle`!='MONTHLY' then 1 ELSE 0 END) AS activecasualemployees,\n" +
                "                   sum(case when a.STATUS='Active' AND gender='Male' then 1 ELSE 0 END) AS activemaleemployees,\n" +
                "                   sum(case when a.STATUS='Active' AND gender='Female' then 1 ELSE 0 END) AS activefemaleemployees,\n" +
                "                   sum(case when a.STATUS='Active' AND gender='Male' AND `pay cycle`='MONTHLY' then 1 ELSE 0 END) AS activemalepermanentemployees,\n" +
                "                   sum(case when a.STATUS='Active' AND gender='Male' AND `pay cycle`!='MONTHLY' then 1 ELSE 0 END) AS activemalecasualemployees,\n" +
                "                   sum(case when a.STATUS='Active' AND gender='Female' AND `pay cycle`='MONTHLY' then 1 ELSE 0 END) AS activefemalepermanentemployees,\n" +
                "                   sum(case when a.STATUS='Active' AND gender='Female' AND `pay cycle`!='MONTHLY' then 1 ELSE 0 END) AS activefemalecasualemployees,\n" +
                "                   sum(case when a.STATUS='Active' AND date(NOW()) BETWEEN b.`from` and `to` then 1 ELSE 0 END) AS onleave,\n" +
                "                   sum(case when a.STATUS='Active' AND gender='Male' AND date(NOW()) BETWEEN b.`from` and `to` then 1 ELSE 0 END) AS onleavemale,\n" +
                "                   sum(case when a.STATUS='Active' AND gender='Female' AND date(NOW()) BETWEEN b.`from` and `to` then 1 ELSE 0 END) AS onleavefemale\n" +
                "               from employees a LEFT JOIN `leave` b ON a.id=b.payno WHERE farm="+farm().getId()+")a",1, 0, 0,0);

        List<accountsmodel> newListofstudents = new ArrayList<accountsmodel>(accountsmodel.datanew);

        model.addAttribute("activeemployees", newListofstudents.get(0).activeProperty("activeemployees").getValue());

        model.addAttribute("activepermanentemployees", newListofstudents.get(0).activeProperty("activepermanentemployees").getValue());
        model.addAttribute("activecasualemployees", newListofstudents.get(0).activeProperty("activecasualemployees").getValue());

        model.addAttribute("activemaleemployees", newListofstudents.get(0).activeProperty("activemaleemployees").getValue());
        model.addAttribute("activemalepermanentemployees", newListofstudents.get(0).activeProperty("activemalepermanentemployees").getValue());
        model.addAttribute("activemalecasualemployees", newListofstudents.get(0).activeProperty("activemalecasualemployees").getValue());

        model.addAttribute("activefemaleemployees", newListofstudents.get(0).activeProperty("activefemaleemployees").getValue());
        model.addAttribute("activefemalepermanentemployees", newListofstudents.get(0).activeProperty("activefemalepermanentemployees").getValue());
        model.addAttribute("activefemalecasualemployees", newListofstudents.get(0).activeProperty("activefemalecasualemployees").getValue());

        model.addAttribute("onleave", newListofstudents.get(0).activeProperty("onleave").getValue());
        model.addAttribute("onleavemale", newListofstudents.get(0).activeProperty("onleavemale").getValue());
        model.addAttribute("onleavefemale", newListofstudents.get(0).activeProperty("onleavefemale").getValue());

        User user = userRepository.findById(userId()).get();
        model.addAttribute("user", user);

        return "payroll/dashboard";

    }

    @GetMapping("/store")
    public String gotostore(Model model) {
        /*if(LoginController.getloggedinuseridtype(7)==0) {
            return "accessdenied";
        }*/

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
                "      where sessionid = '"+RequestContextHolder.currentRequestAttributes().getSessionId()+"') d",1, 0, 0,0);

        List<accountsmodel> newListdf = new ArrayList<accountsmodel>(accountsmodel.datanew);
        model.addAttribute("email", 	 	 newListdf.get(0).activeProperty("username").getValue());
        model.addAttribute("image", 	 	 newListdf.get(0).activeProperty("image").getValue());
        model.addAttribute("school", farm().getId());

        User user = userRepository.findById(userId()).get();
        model.addAttribute("user", user);

        return "inventory/dashboard";
    }

}
