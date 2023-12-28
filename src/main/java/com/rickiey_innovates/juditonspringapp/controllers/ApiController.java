package com.rickiey_innovates.juditonspringapp.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.rickiey_innovates.juditonspringapp.DbConnector;
import com.rickiey_innovates.juditonspringapp.models.*;
import com.rickiey_innovates.juditonspringapp.repositories.AccounttransactionRepository;
import com.rickiey_innovates.juditonspringapp.repositories.PaymentvoucherRepository;
import com.rickiey_innovates.juditonspringapp.repositories.RoleRepository;
import com.rickiey_innovates.juditonspringapp.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/rest")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ApiController {

    private final UserRepository userRepository;
    private final PaymentvoucherRepository paymentvoucherRepository;
    private final AccounttransactionRepository accounttransactionRepository;
    private final RoleRepository roleRepository;

    public ApiController(UserRepository userRepository,
                           PaymentvoucherRepository paymentvoucherRepository,
                           AccounttransactionRepository accounttransactionRepository,
                           RoleRepository roleRepository) {

        this.userRepository = userRepository;
        this.paymentvoucherRepository = paymentvoucherRepository;
        this.accounttransactionRepository = accounttransactionRepository;
        this.roleRepository = roleRepository;
    }

    private Long userId() {
        return LoginController.getLoggedInUserId();
    }

    private Farm farm() {
        return  userRepository.findById(userId()).orElse(null).getFarm();
    }

    @GetMapping("/dashboard")
    public ResponseEntity<SummaryDTO> getDashboardSummary() {
        try {
            Connection connection = DbConnector.getConnection();
            String sql = "SELECT 3 AS memberTotal,\n" +
                    "(SELECT IFNULL(FORMAT(SUM(credit), 0), 0)\n" +
                    " FROM accounttransactions\n" +
                    " WHERE farm = " + farm().getId() + " AND YEAR(Date) = YEAR(CURDATE()) AND MONTH(Date) = MONTH(CURDATE())) AS income,\n" +
                    "(SELECT IFNULL(FORMAT(SUM(debit), 0), 0)\n" +
                    " FROM accounttransactions\n" +
                    " WHERE farm = " + farm().getId() + " AND YEAR(Date) = YEAR(CURDATE()) AND MONTH(Date) = MONTH(CURDATE())) AS expenses,\n" +
                    "(SELECT IFNULL(FORMAT((SUM(credit) - SUM(debit)), 0), 0)\n" +
                    " FROM accounttransactions\n" +
                    " WHERE farm = " + farm().getId() + " AND YEAR(Date) = YEAR(CURDATE()) AND MONTH(Date) = MONTH(CURDATE())) AS balance,\n" +
                    "(SELECT IFNULL(FORMAT(SUM(debit), 0), 0)\n" +
                    " FROM accounttransactions\n" +
                    " WHERE farm = " + farm().getId() + "\n" +
                    " AND YEAR(Date) = YEAR(CURDATE()) AND MONTH(Date) = MONTH(CURDATE())) AS currentMonthExpenses,\n" +
                    "(SELECT IFNULL(FORMAT(SUM(debit), 0), 0)\n" +
                    " FROM accounttransactions\n" +
                    " WHERE farm = " + farm().getId() + "\n" +
                    " AND MONTH(Date) = MONTH(CURDATE() - INTERVAL 1 MONTH)) AS previousMonthExpenses,\n" +
                    " IFNULL(\n" +
                    " ((SELECT SUM(debit)\n" +
                    " FROM accounttransactions\n" +
                    " WHERE farm = " + farm().getId() + "\n" +
                    " AND MONTH(Date) = MONTH(CURDATE())) /\n" +
                    " (SELECT SUM(debit)\n" +
                    " FROM accounttransactions\n" +
                    " WHERE farm = " + farm().getId() + "\n" +
                    " AND MONTH(Date) = MONTH(CURDATE() - INTERVAL 1 MONTH))) * 100 - 100,\n" +
                    " 0) AS percentageIncrease;";

            ResultSet rs = connection.prepareStatement(sql).executeQuery();
            SummaryDTO summaryDTO = new SummaryDTO();
            while (rs.next()) {
                summaryDTO.setMemberTotal(rs.getInt("memberTotal"));
                summaryDTO.setIncome(rs.getString("income"));
                summaryDTO.setExpenses(rs.getString("expenses"));
                summaryDTO.setBalance(rs.getString("balance"));
                summaryDTO.setPercentageExpensesIncrease(rs.getDouble("percentageIncrease"));
            }
            rs.close();
            connection.close();

            return ResponseEntity.ok(summaryDTO);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    private boolean isAuthorised() {
        Set<Role> roles = userRepository.findById(userId()).get().getRoles();
        Role role = roleRepository.findByName(ERole.ROLE_ACCOUNTANT).orElse(null);
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

    @GetMapping("/getTheme")
    public ResponseEntity<?> getTheme() {
        int theme = userRepository.findById(userId()).orElse(null).getTheme();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("theme", theme);
        return ResponseEntity.ok(jsonObject.toString());
    }

    @PostMapping("/updateTheme")
    public ResponseEntity<?> updateTheme(@RequestBody String requestBody) {
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

        return ResponseEntity.ok("success");
    }

    @GetMapping("/getMonthlyExpenses")
    public ResponseEntity<?> getMonthlyExpenses() {
        List<MonthlyTithes> monthlyExpensesList = new ArrayList<>();
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
                    "ORDER BY year, month desc ;\n";

            ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
            while(resultSet.next()) {
                MonthlyTithes monthlyExpenses = new MonthlyTithes(resultSet.getString("year"), resultSet.getString("month"), resultSet.getString("credit"));
                monthlyExpensesList.add(monthlyExpenses);
            }

            resultSet.close();
            connection.close();
            return ResponseEntity.ok(monthlyExpensesList);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/getMonthlyIncome")
    public ResponseEntity<?> getMonthlyIncome() {
        List<MonthlyTithes> monthlyIncomeList = new ArrayList<>();
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
                    "ORDER BY year, month desc ;\n";
            ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
            while(resultSet.next()) {
                MonthlyTithes monthlyExpenses = new MonthlyTithes(resultSet.getString("year"), resultSet.getString("month"), resultSet.getString("credit"));
                monthlyIncomeList.add(monthlyExpenses);
            }

            resultSet.close();
            connection.close();
            return ResponseEntity.ok(monthlyIncomeList);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getLocalizedMessage());
        }

    }

    @GetMapping("/getTithe")
    public ResponseEntity<?> getTithe() {
        List<TitheAndOff> titheAndOffList = new ArrayList<>();
        try {
            Connection connection = DbConnector.getConnection();
            String sql = "SELECT\n" +
                    "    Account,\n" +
                    "    YEAR(`Date`) AS year,\n" +
                    "    DATE_FORMAT(`Date`, '%b') AS month,\n" +
                    "    SUM(credit) AS total\n" +
                    "FROM accounttransactions\n" +
                    "WHERE Account IN (1, 2) and farm = "+farm().getId()+"\n" +
                    "GROUP BY Account, YEAR(`Date`), MONTH(`Date`)\n" +
                    "ORDER BY Account, YEAR(`Date`), MONTH(`Date`);";
            ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
            while(resultSet.next()) {
                TitheAndOff titheAndOff = new TitheAndOff();

                if (resultSet.getInt("Account") == 1 || resultSet.getInt("Account") == 439) {
                    MonthlyTithes titheDTO = new MonthlyTithes(resultSet.getString("year"), resultSet.getString("month"), resultSet.getString("total"));
                    titheAndOff.setMonthlyTithes(titheDTO);
                } else if (resultSet.getInt("Account") == 2 || resultSet.getInt("Account") == 440) {
                    MonthlyOffering offeringDTO = new MonthlyOffering(resultSet.getString("year"), resultSet.getString("month"), resultSet.getString("total"));;
                    titheAndOff.setMonthlyOffering(offeringDTO);
                }
                titheAndOffList.add(titheAndOff);
            }

            resultSet.close();
            connection.close();
            return ResponseEntity.ok(titheAndOffList);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getLocalizedMessage());
        }
    }

    /*@GetMapping("/hr/dashboard")
    public ResponseEntity<String> getHRDashboard(Model model) {
        // Existing HR dashboard logic...

        if (!isAuthorised()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        // Additional logic...

        return ResponseEntity.ok("HR dashboard data");
    }

    @GetMapping("/store/dashboard")
    public ResponseEntity<String> getStoreDashboard(Model model) {
        // Existing store dashboard logic...

        // Additional logic...

        return ResponseEntity.ok("Store dashboard data");
    }*/
}

