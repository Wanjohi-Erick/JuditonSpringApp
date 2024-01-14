package com.rickiey_innovates.juditonspringapp.controllers;

import com.google.gson.JsonObject;
import com.rickiey_innovates.juditonspringapp.DuplicateRoleException;
import com.rickiey_innovates.juditonspringapp.UnauthorisedAccessException;
import com.rickiey_innovates.juditonspringapp.models.*;
import com.rickiey_innovates.juditonspringapp.repositories.*;
import com.rickiey_innovates.juditonspringapp.DbConnector;
import com.rickiey_innovates.juditonspringapp.models.*;
import com.rickiey_innovates.juditonspringapp.repositories.*;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/finance")
public class FinanceController {

    private final ActivityRepository activityRepository;
    private final ActivitygroupRepository activitygroupRepository;
    private final BankaccountRepository bankaccountRepository;
    private final AccounttransactionRepository accounttransactionRepository;
    private final ReferenceRepository referenceRepository;

    private final UserRepository userRepository;

    public FinanceController(ActivityRepository activityRepository,
                             ActivitygroupRepository activitygroupRepository,
                             BankaccountRepository bankaccountRepository,
                             AccounttransactionRepository accounttransactionRepository,
                             ReferenceRepository referenceRepository,
                             PaymentvoucherRepository paymentvoucherRepository,
                             UserRepository userRepository,
                             PaymentvoucherdetailRepository paymentvoucherdetailRepository,
                             TaxRepository taxRepository) {
        this.activityRepository = activityRepository;
        this.activitygroupRepository = activitygroupRepository;
        this.bankaccountRepository = bankaccountRepository;
        this.accounttransactionRepository = accounttransactionRepository;
        this.referenceRepository = referenceRepository;
        this.paymentvoucherRepository = paymentvoucherRepository;
        this.userRepository = userRepository;
        this.paymentvoucherdetailRepository = paymentvoucherdetailRepository;
        this.taxRepository = taxRepository;
    }

    @Value("${vouchers.path}")
    private String vouchersPath;
    private final PaymentvoucherRepository paymentvoucherRepository;
    private final PaymentvoucherdetailRepository paymentvoucherdetailRepository;
    private final TaxRepository taxRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    PendingTransactionRepository pendingTransactionRepository;

    @Autowired
    VoucherSignatoryRepository voucherSignatoryRepository;

    private Long userId() {
        return LoginController.getLoggedInUserId();
    }

    private Farm farm() {
        return userRepository.findById(userId()).orElse(null).getFarm();
    }

    @GetMapping("/chart")
    public String chart(Model model, HttpServletRequest request) {
        List<Activity> activityList = activityRepository.findByChurch(farm());
        List<Activitygroup> activitygroups = activitygroupRepository.findByChurch(farm());
        User user = userRepository.findById(userId()).get();
        model.addAttribute("user", user);
        model.addAttribute("activities", activityList);
        model.addAttribute("groups", activitygroups);
        model.addAttribute("page", "chart of accounts");
        model.addAttribute("main", "finance");
        model.addAttribute("requestURI", request.getRequestURI());
        return "finance/chart_of_accounts";
    }

    @GetMapping(value = "/chart/get/all", produces = "application/json")
    @ResponseBody
    public List<Activity> activities() {
        return activityRepository.findByChurch(farm());
    }

    @PostMapping("/chart/add")
    public String addChart(RedirectAttributes redirectAttributes, Activity activity) {
        String message = "";
        try {
            activity.setFarm(farm());
            activityRepository.save(activity);
            message = "Account added Successfully";
            redirectAttributes.addFlashAttribute("message", message);
        } catch (Exception e) {
            e.printStackTrace();
            message = "Account failed to add";
            redirectAttributes.addFlashAttribute("error", message);
        }

        // Return the same view to display the response message
        return "redirect:/finance/chart";
    }

    @GetMapping(value = "/account/get/{id}", produces = "application/json")
    @ResponseBody
    public Activity getAccount(@PathVariable int id) {

        return activityRepository.findById(id).orElse(null);
    }

    @PostMapping("/account/update")
    public String updateAccount(Activity updatedActivity, RedirectAttributes redirectAttributes) {
        String message, error;
        try {
            Activity existingActivity = activityRepository.findById(updatedActivity.getId()).orElse(null);
            if (existingActivity != null) {
                existingActivity.setAccountgroup(updatedActivity.getAccountgroup());
                existingActivity.setAccount(updatedActivity.getAccount());

                activityRepository.save(existingActivity);

                message = "Account updated successfully";
                redirectAttributes.addFlashAttribute("message", message);

            } else {
                error = "Account not found. Update failed.";
                redirectAttributes.addFlashAttribute("error", error);
            }
        } catch (Exception e) {
            e.printStackTrace();
            error = "Account update failed";
            redirectAttributes.addFlashAttribute("error", error);
        }

        return "redirect:/finance/chart";
    }

    @GetMapping("/account/delete/{id}")
    public String deleteTimetable(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            activityRepository.deleteById(id);

            redirectAttributes.addFlashAttribute("message", "The activity has been deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/finance/chart";
    }

    @GetMapping(value = "/chart/groups/get/all", produces = "application/json")
    @ResponseBody
    public List<Activitygroup> activitygroups() {
        return activitygroupRepository.findByChurch(farm());
    }

    @GetMapping("/groups")
    public String activityGroups(Model model, HttpServletRequest request) {

        List<Activitygroup> activitygroups = activitygroupRepository.findByChurch(farm());
        User user = userRepository.findById(userId()).get();
        model.addAttribute("user", user);
        model.addAttribute("groups", activitygroups);
        model.addAttribute("page", "groups");
        model.addAttribute("main", "finance");
        model.addAttribute("requestURI", request.getRequestURI());
        return "finance/activity_groups";
    }

    @PostMapping("/group/add")
    public String addGroup(Activitygroup activitygroup, RedirectAttributes redirectAttributes) {
        try {
            activitygroup.setFarm(farm());
            activitygroupRepository.save(activitygroup);
            redirectAttributes.addFlashAttribute("message", "Activity group added successfully");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "An error occured while adding activity group");
        }
        return "redirect:/finance/groups";
    }

    @GetMapping("/group/get/{id}")
    @ResponseBody
    public Activitygroup activitygroup(@PathVariable Integer id) {
        return activitygroupRepository.findById(id).orElse(null);
    }

    @PostMapping("/group/update")
    public String updateGroup(Activitygroup activitygroup, RedirectAttributes redirectAttributes) {
        try {
            Activitygroup old = activitygroupRepository.findById(activitygroup.getId()).get();
            old.setGroupName(activitygroup.getGroupName());
            activitygroupRepository.save(old);
            redirectAttributes.addFlashAttribute("message", "Activity group updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "An error occurred while adding activity group");
        }
        return "redirect:/finance/groups";
    }

    @GetMapping("/group/delete/{id}")
    public String deleteGroup(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            activitygroupRepository.deleteById(id);

            redirectAttributes.addFlashAttribute("message", "The activity group has been deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/finance/groups";
    }

    @GetMapping("/banking")
    public String banking(Model model, HttpServletRequest request) {
        List<Bankaccount> bankAccounts = bankaccountRepository.findByChurch(farm());
        User user = userRepository.findById(userId()).get();
        model.addAttribute("user", user);
        model.addAttribute("accounts", bankAccounts);
        model.addAttribute("page", "banking");
        model.addAttribute("main", "finance");
        model.addAttribute("requestURI", request.getRequestURI());
        return "finance/list_of_banks";
    }

    @GetMapping(value = "/banking/get/all", produces = "application/json")
    @ResponseBody
    public List<Bankaccount> bankaccounts() {
        return bankaccountRepository.findByChurch(farm());
    }

    @GetMapping("/banking/view/{id}")
    public String banking(Model model, @PathVariable Integer id) {
        List<Accounttransaction> accounttransactions = accounttransactionRepository.findByBank_Id(id);
        List<Bankaccount> bankaccounts = bankaccountRepository.findByChurch(farm());
        User user = userRepository.findById(userId()).get();
        model.addAttribute("user", user);
        model.addAttribute("bankId", bankaccountRepository.findById(id).orElse(null).getId());
        model.addAttribute("bankName", "Transactions for account: " + bankaccountRepository.findById(id).get().getBankName());
        model.addAttribute("accountTransactions", accounttransactions);
        model.addAttribute("bankAccounts", bankaccounts);
        model.addAttribute("page", "transactions");
        model.addAttribute("main", "finance");

        return "finance/list_of_transactions";
    }

    @GetMapping(value = "/banking/transaction/get/{id}", produces = "application/json")
    @ResponseBody
    public AccountTransactionDTO getTransactions(@PathVariable int id) {
        Accounttransaction accounttransaction = accounttransactionRepository.findById(id).orElse(null);
        Bankaccount bankaccount = accounttransaction.getBank();
        AccountTransactionDTO accountTransactionDTO = new AccountTransactionDTO(accounttransaction, bankaccount);

        return accountTransactionDTO;
    }

    @PostMapping("/banking/transaction/update")
    public String updateTransaction(@ModelAttribute("accounttransaction") Accounttransaction updatedTransaction, RedirectAttributes redirectAttributes) {
        String message = "", error = "";
        try {
            Accounttransaction existingTransaction = accounttransactionRepository.findById(updatedTransaction.getId()).orElse(null);
            if (existingTransaction != null) {
                existingTransaction.setDate(updatedTransaction.getDate());
                existingTransaction.setBank(updatedTransaction.getBank());
                existingTransaction.setPayeePayer(updatedTransaction.getPayeePayer());
                existingTransaction.setDescription(updatedTransaction.getDescription());
                existingTransaction.setCheque(updatedTransaction.getCheque());
                existingTransaction.setCredit(updatedTransaction.getCredit());
                existingTransaction.setDebit(updatedTransaction.getDebit());

                accounttransactionRepository.save(existingTransaction);

                message = "Transaction updated successfully";
                redirectAttributes.addFlashAttribute("message", message);

            } else {
                error = "Transaction not found. Update failed.";
                redirectAttributes.addFlashAttribute("error", error);
            }
        } catch (Exception e) {
            e.printStackTrace();
            error = "Transaction update failed";
            redirectAttributes.addFlashAttribute("error", error);
        }

        return "redirect:/finance/banking/view/" + updatedTransaction.getBank().getId();
    }

    @PostMapping("/banking/openingBalance/update")
    public String updateOpeningBalance(@RequestParam String date, @RequestParam Integer bank, @RequestParam double amount, RedirectAttributes redirectAttributes) {
        String message = "", error = "";
        try {
            Bankaccount bankaccount = bankaccountRepository.findById(bank).orElse(null);
            bankaccount.setOpeningBalance(amount);
            bankaccountRepository.save(bankaccount);
            Accounttransaction existingTransaction = accounttransactionRepository.findByBankAndDescription(bankaccountRepository.findById(bank).orElse(null), "Opening Balance");
            if (existingTransaction != null) {
                existingTransaction.setDate(LocalDate.parse(date));
                existingTransaction.setCredit(amount);

                accounttransactionRepository.save(existingTransaction);

                message = "Opening balance updated successfully";
                redirectAttributes.addFlashAttribute("message", message);

            } else {
                Reference reference = referenceRepository.findByChurch(farm());
                if (reference != null) {
                    int ref = reference.getRef() + 1;

                    Accounttransaction accounttransaction = new Accounttransaction();
                    accounttransaction.setDescription("Opening Balance");
                    accounttransaction.setPayeePayer("Opening Balance");
                    accounttransaction.setRef("RF" + ref);
                    accounttransaction.setAccount(activityRepository.findByAccount("Opening Balance").getId());
                    if (amount > 0) {
                        accounttransaction.setCredit(amount);
                        accounttransaction.setDebit(0.0);
                    } else {
                        accounttransaction.setDebit(amount);
                        accounttransaction.setCredit(0.0);
                    }

                    accounttransaction.setBank(bankaccount);
                    accounttransaction.setStatus("Approved");
                    accounttransaction.setDate(LocalDate.parse(date));

                    reference.setRef(ref);

                    referenceRepository.save(reference);
                    accounttransactionRepository.save(accounttransaction);

                    message = "Opening balance added Successfully";
                    redirectAttributes.addFlashAttribute("message", message);
                }
                /*error = "Transaction not found. Update failed.";
                redirectAttributes.addFlashAttribute("error", error);*/
            }
        } catch (Exception e) {
            e.printStackTrace();
            error = "Opening balance update failed";
            redirectAttributes.addFlashAttribute("error", error);
        }

        return "redirect:/finance/banking/view/" + bank;
    }

    @PostMapping("/bank/add")
    public String addBank(Model model, Bankaccount bankaccount) {
        String message = "";
        try {
            bankaccount.setFarm(farm());
            Bankaccount savedBankAccount = bankaccountRepository.save(bankaccount);

            if (savedBankAccount.getId() != null) {

                Reference reference = referenceRepository.findByChurch(farm());
                if (reference != null) {
                    int ref = reference.getRef() + 1;
                    Accounttransaction accounttransaction = new Accounttransaction();
                    accounttransaction.setDescription("Opening Balance");
                    accounttransaction.setPayeePayer("Opening Balance");
                    accounttransaction.setRef("RF" + ref);
                    accounttransaction.setFarm(farm());
                    accounttransaction.setAccount(activityRepository.findByAccount("Opening Balance").getId());
                    if (savedBankAccount.getOpeningBalance() > 0) {
                        accounttransaction.setCredit(savedBankAccount.getOpeningBalance());
                        accounttransaction.setDebit(0.0);
                    } else {
                        accounttransaction.setDebit(savedBankAccount.getOpeningBalance());
                        accounttransaction.setCredit(0.0);
                    }

                    accounttransaction.setBank(savedBankAccount);
                    accounttransaction.setStatus("Approved");
                    accounttransaction.setDate(LocalDate.now());

                    reference.setRef(ref);

                    referenceRepository.save(reference);
                    accounttransactionRepository.save(accounttransaction);

                    message = "Account added Successfully";

                } else {
                    Reference newReference = new Reference();
                    newReference.setRef(0);
                    newReference.setPv(0);
                    newReference.setRct(0);
                    newReference.setFarm(farm());

                    Reference savedReference = referenceRepository.save(newReference);
                    int ref = savedReference.getRef() + 1;
                    Accounttransaction accounttransaction = new Accounttransaction();
                    accounttransaction.setDescription("Opening Balance");
                    accounttransaction.setPayeePayer("Opening Balance");
                    accounttransaction.setRef("RF" + ref);
                    accounttransaction.setFarm(farm());
                    accounttransaction.setAccount(activityRepository.findByAccount("Opening Balance").getId());
                    if (savedBankAccount.getOpeningBalance() > 0) {
                        accounttransaction.setCredit(savedBankAccount.getOpeningBalance());
                        accounttransaction.setDebit(0.0);
                    } else {
                        accounttransaction.setDebit(savedBankAccount.getOpeningBalance());
                        accounttransaction.setCredit(0.0);
                    }

                    accounttransaction.setBank(savedBankAccount);
                    accounttransaction.setStatus("Approved");
                    accounttransaction.setDate(LocalDate.now());

                    savedReference.setRef(ref);

                    referenceRepository.save(savedReference);
                    accounttransactionRepository.save(accounttransaction);

                    message = "Account added Successfully";
                }
            } else {
                message = "Account failed to add";
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "Account failed to add";
        }
        model.addAttribute("message", message);
        model.addAttribute("page", "banking");
        model.addAttribute("main", "finance");

        // Return the same view to display the response message
        return "redirect:/finance/banking";
    }

    @GetMapping(value = "/bank/get/{id}", produces = "application/json")
    @ResponseBody
    public Bankaccount getBank(@PathVariable int id) {

        return bankaccountRepository.findById(id).orElse(null);
    }

    @PostMapping("/bank/update")
    public String update(@ModelAttribute("bankaccount") Bankaccount updatedBankaccount, RedirectAttributes redirectAttributes) {
        String message = "", error = "";
        try {
            Bankaccount existingBankaccount = bankaccountRepository.findById(updatedBankaccount.getId()).orElse(null);
            if (existingBankaccount != null) {
                existingBankaccount.setAccountName(updatedBankaccount.getAccountName());
                existingBankaccount.setAccount(updatedBankaccount.getAccount());
                existingBankaccount.setBankName(updatedBankaccount.getBankName());
                existingBankaccount.setBranch(updatedBankaccount.getBranch());
                existingBankaccount.setType(updatedBankaccount.getType());
                existingBankaccount.setBankcode(updatedBankaccount.getBankcode());
                existingBankaccount.setSwiftcode(updatedBankaccount.getSwiftcode());

                bankaccountRepository.save(existingBankaccount);

                message = "Account updated successfully";
                redirectAttributes.addFlashAttribute("message", message);

            } else {
                error = "Account not found. Update failed.";
                redirectAttributes.addFlashAttribute("error", error);
            }
        } catch (Exception e) {
            e.printStackTrace();
            error = "Account update failed";
            redirectAttributes.addFlashAttribute("error", error);
        }

        return "redirect:/finance/banking";
    }

    @PostMapping("/bank/transfer")
    public String transfer(@RequestParam String date, String source, String dest, String transRef, double amount, RedirectAttributes redirectAttributes) {
        int from = Integer.parseInt(source);
        int to = Integer.parseInt(dest);
        Bankaccount sourceBank = bankaccountRepository.findById(from).orElse(null);
        Bankaccount destinationBank = bankaccountRepository.findById(to).orElse(null);
        double balance = sourceBank.getBalance();
        if (from == to) {
            redirectAttributes.addAttribute("error", "Can not deposit to the same account");
        } else if (balance < amount) {
            redirectAttributes.addAttribute("error", "Can not overdraw");
        } else {
            if (sourceBank.getType().equalsIgnoreCase("bank")) {
                Reference reference = referenceRepository.findByChurch(farm());
                int ref = reference.getRef() + 1;
                Accounttransaction sourceTransaction = new Accounttransaction();
                Accounttransaction destinationTransaction = new Accounttransaction();
                sourceTransaction.setDescription("Cash Withdraw");
                sourceTransaction.setPayeePayer("Cash Withdraw");
                sourceTransaction.setAccount(433);
                sourceTransaction.setAccount(activityRepository.findByAccount("Withdraw").getId());
                sourceTransaction.setRef("RF" + ref);
                sourceTransaction.setDebit(amount);
                sourceTransaction.setCheque(transRef);

                sourceTransaction.setBank(sourceBank);
                sourceTransaction.setStatus("Approved");
                sourceTransaction.setFarm(farm());
                sourceTransaction.setDate(LocalDate.parse(date));

                destinationTransaction.setDescription("Cash From Bank");
                destinationTransaction.setPayeePayer("Cash From Bank");
                destinationTransaction.setAccount(434);
                destinationTransaction.setAccount(activityRepository.findByAccount("Deposit").getId());
                destinationTransaction.setRef("RF" + ref);
                destinationTransaction.setCredit(amount);
                destinationTransaction.setCheque(transRef);

                destinationTransaction.setBank(destinationBank);
                destinationTransaction.setStatus("Approved");
                destinationTransaction.setDate(LocalDate.parse(date));
                destinationTransaction.setFarm(farm());

                reference.setRef(ref);

                accounttransactionRepository.save(sourceTransaction);
                accounttransactionRepository.save(destinationTransaction);
                referenceRepository.save(reference);

                redirectAttributes.addFlashAttribute("message", "Operation completed successfully");
            } else {
                Reference reference = referenceRepository.findByChurch(farm());
                int ref = reference.getRef() + 1;
                Accounttransaction sourceTransaction = new Accounttransaction();
                Accounttransaction destinationTransaction = new Accounttransaction();
                sourceTransaction.setDescription("Deposit to Bank");
                sourceTransaction.setPayeePayer("Deposit to Bank");
                sourceTransaction.setRef("RF" + ref);
                sourceTransaction.setDebit(amount);
                sourceTransaction.setCheque(transRef);

                sourceTransaction.setBank(sourceBank);
                sourceTransaction.setStatus("Approved");
                sourceTransaction.setDate(LocalDate.parse(date));
                sourceTransaction.setFarm(farm());

                destinationTransaction.setDescription("Cash Deposit");
                destinationTransaction.setPayeePayer("Cash Deposit");
                destinationTransaction.setRef("RF" + ref);
                destinationTransaction.setCredit(amount);
                destinationTransaction.setCheque(transRef);

                destinationTransaction.setBank(destinationBank);
                destinationTransaction.setStatus("Approved");
                destinationTransaction.setDate(LocalDate.parse(date));
                destinationTransaction.setFarm(farm());

                reference.setRef(ref);

                accounttransactionRepository.save(sourceTransaction);
                accounttransactionRepository.save(destinationTransaction);
                referenceRepository.save(reference);

                redirectAttributes.addFlashAttribute("message", "Operation completed successfully");
            }
        }

        return "redirect:/finance/banking";
    }

    @GetMapping(value = "/api/getAllReceipts", produces = "application/json")
    @ResponseBody
    public List<Receipts> getReceipts() {
        List<Receipts> receipts = new ArrayList<>();
        try {
            Connection connection = DbConnector.getConnection();
            String sql = "SELECT * FROM (SELECT `Transaction id`, `Ref #`, DATE_FORMAT(p.DATE, '%m/%d/%Y') AS DATE, " +
                    "`Payee/Payer` AS 'Received from', GROUP_CONCAT(q.Account)  'Description', Description 'Details', " +
                    "SUM(Credit) AS Amount FROM accounttransactions p " +
                    "INNER JOIN activities q ON q.`Account id` = p.account " +
                    "INNER JOIN bankaccounts b ON `Acc id` = p.Bank " +
                    "WHERE credit != '0' and p.farm = " + farm().getId() + " GROUP BY `Ref #` order by `Transaction id` desc) k";
            ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
            while (resultSet.next()) {
                Receipts receipt = new Receipts();
                receipt.setTransactionId(resultSet.getInt("Transaction id"));
                receipt.setReferenceNumber(resultSet.getString("Ref #"));
                receipt.setDate(resultSet.getString("DATE"));
                receipt.setReceivedFrom(resultSet.getString("Received from"));
                receipt.setDescription(resultSet.getString("Description"));
                receipt.setDetails(resultSet.getString("Details"));
                receipt.setAmount(resultSet.getDouble("Amount"));

                receipts.add(receipt);
            }

            resultSet.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return receipts;
    }

    @GetMapping("/receipts")
    public String receipts(Model model, HttpServletRequest request) {
        List<Receipts> receipts = getReceipts();
        List<Activity> activityList = activityRepository.findByChurch(farm());
        List<Bankaccount> bankaccounts = bankaccountRepository.findByChurch(farm());
        User user = userRepository.findById(userId()).get();
        model.addAttribute("user", user);
        model.addAttribute("receipts", receipts);
        model.addAttribute("activities", activityList);
        model.addAttribute("banks", bankaccounts);
        model.addAttribute("page", "receipts");
        model.addAttribute("main", "finance");
        model.addAttribute("requestURI", request.getRequestURI());
        return "finance/receipts";
    }


    @GetMapping("/api/getAllVouchers")
    @ResponseBody
    public List<PaymentVoucherResult> paymentvouchers() {
        approveVouchers();
        List<PaymentVoucherResult> paymentVoucherResults = new ArrayList<>();
        try {
            Connection connection = DbConnector.getConnection();
            String sql = "SELECT *\n" +
                    "FROM (\n" +
                    "         SELECT `Pv id`, `Voucher #`, p.status, p.Date AS DATE, `Payee Name`, a.Account, Details, Debit AS Amount\n" +
                    "         FROM paymentvouchers p\n" +
                    "                  INNER JOIN activities a ON `account id` = p.activity\n" +
                    "                  INNER JOIN accounttransactions ac ON ac.`Ref #` = p.`Voucher #`\n" +
                    "                  INNER JOIN bankaccounts b ON b.`Acc id` = ac.Bank\n" +
                    "         WHERE p.farm = " + farm().getId() + " AND ac.farm = " + farm().getId() + "\n" +
                    "         GROUP BY `Pv id`\n" +
                    "\n" +
                    "         UNION ALL\n" +
                    "\n" +
                    "         SELECT `Pv id`, `Voucher #`, p.status, p.Date AS DATE, `Payee Name`, a.Account, Details, Debit AS Amount\n" +
                    "         FROM paymentvouchers p\n" +
                    "                  INNER JOIN activities a ON `account id` = p.activity\n" +
                    "                  INNER JOIN pending_transaction ac ON ac.`Ref #` = p.`Voucher #`\n" +
                    "                  INNER JOIN bankaccounts b ON b.`Acc id` = ac.Bank\n" +
                    "         WHERE p.farm = " + farm().getId() + " AND ac.farm = " + farm().getId() + "\n" +
                    "         GROUP BY `Pv id`\n" +
                    "     ) k ORDER BY DATE DESC;\n";
            System.out.println(sql);
            ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
            while (resultSet.next()) {
                int pvId = resultSet.getInt("Pv id");
                String voucherNumber = resultSet.getString("Voucher #");
                Date date = resultSet.getDate("DATE");
                String status = resultSet.getString("status");
                String payeeName = resultSet.getString("Payee Name");
                String account = resultSet.getString("Account");
                String details = resultSet.getString("Details");
                double amount = resultSet.getDouble("Amount");

                PaymentVoucherResult result = new PaymentVoucherResult(pvId, voucherNumber, date, status, payeeName, account, details, amount);
                paymentVoucherResults.add(result);
            }

            resultSet.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paymentVoucherResults;
    }

    @GetMapping(value = "/api/vouchers", produces = "application/json")
    @ResponseBody
    public Model model(Model model) {
        List<PaymentVoucherResult> paymentvouchers = paymentvouchers();
        List<Activity> activityList = activityRepository.findByChurch(farm());
        List<Bankaccount> bankaccounts = bankaccountRepository.findByChurch(farm());
        User user = userRepository.findById(userId()).get();
        model.addAttribute("user", user);
        model.addAttribute("paymentVouchers", paymentvouchers);
        model.addAttribute("activities", activityList);
        model.addAttribute("banks", bankaccounts);
        model.addAttribute("page", "vouchers");
        model.addAttribute("main", "finance");

        return model;
    }

    @GetMapping("/vouchers")
    public String vouchers(Model model, HttpServletRequest request) {
        List<PaymentVoucherResult> paymentvouchers = paymentvouchers();
        List<Activity> activityList = activityRepository.findByChurch(farm());
        List<Bankaccount> bankaccounts = bankaccountRepository.findByChurch(farm());
        User user = userRepository.findById(userId()).get();
        model.addAttribute("user", user);
        model.addAttribute("paymentVouchers", paymentvouchers);
        model.addAttribute("activities", activityList);
        model.addAttribute("banks", bankaccounts);
        model.addAttribute("page", "vouchers");
        model.addAttribute("main", "finance");
        model.addAttribute("requestURI", request.getRequestURI());
        return "finance/payment_vouchers";
    }

    @PostMapping("/voucher/add")
    @ResponseBody
    public String saveData(@RequestBody VoucherRequest request, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        JsonObject response = new JsonObject();
        httpServletRequest.getRequestURI();
        try {
            System.out.println("Received data: " + request.toString());

            LocalDate date = request.getDate();
            int activity = request.getActivity();
            String payee = request.getPayee();
            String details = request.getDetails();
            int bank = request.getBank();
            String transRef = request.getTransRef();
            double withholding = request.getWithholding();
            double vat = request.getVat();
            double profFees = request.getProfFees();
            Double amountBeforeTax = request.getAmountBeforeTax();
            Double totalPayable = request.getTotalPayable();
            List<VoucherTableRow> voucherTableData = request.getVoucherTableData();
            System.out.println(voucherTableData.toString());

            int pv = referenceRepository.findByChurch(farm()).getPv() + 1;

            Paymentvoucher paymentvoucherToProvider = new Paymentvoucher();
            paymentvoucherToProvider.setVoucher("PV" + pv);
            paymentvoucherToProvider.setDate(date);
            paymentvoucherToProvider.setActivity(activity);
            paymentvoucherToProvider.setPayeeName(payee);
            paymentvoucherToProvider.setDetails(details);
            paymentvoucherToProvider.setStatus("Pending");
            paymentvoucherToProvider.setFarm(farm());

            Paymentvoucher savedVoucher = paymentvoucherRepository.save(paymentvoucherToProvider);

            VoucherSignatory voucherSignatory = new VoucherSignatory();
            voucherSignatory.setVoucher(savedVoucher);
            voucherSignatory.setSecondSignatory(0);
            voucherSignatory.setAccountant(0);
            voucherSignatory.setSeniorPastor(0);
            voucherSignatory.setTreasurer(0);

            voucherSignatoryRepository.save(voucherSignatory);

            // changing to pending transaction

            PendingTransaction pendingTransaction = new PendingTransaction();

            pendingTransaction.setDate(date);
            pendingTransaction.setRef("PV" + pv);
            pendingTransaction.setVoucher(savedVoucher);
            pendingTransaction.setAccount(activity);
            //pendingTransaction.setAccount(activityRepository.findById(activity).orElse(null));
            pendingTransaction.setBank(bankaccountRepository.findById(bank).orElse(null));
            pendingTransaction.setPayeePayer(payee);
            pendingTransaction.setDescription(details);
            pendingTransaction.setCheque(transRef);
            pendingTransaction.setActivity(0);
            pendingTransaction.setDebit(totalPayable);
            pendingTransaction.setCredit(0.0);
            pendingTransaction.setStatus("Pending");
            pendingTransaction.setFarm(farm());

            pendingTransactionRepository.save(pendingTransaction);

            /*Accounttransaction transactToProvider = new Accounttransaction();
            transactToProvider.setDate(date);
            transactToProvider.setRef("PV" + pv);
            transactToProvider.setAccount(activity);
            //transactToProvider.setAccount(activityRepository.findById(activity).orElse(null));
            transactToProvider.setBank(bankaccountRepository.findById(bank).orElse(null));
            transactToProvider.setPayeePayer(payee);
            transactToProvider.setDescription(details);
            transactToProvider.setCheque(transRef);
            transactToProvider.setActivity(0);
            transactToProvider.setDebit(totalPayable);
            transactToProvider.setCredit(0.0);
            transactToProvider.setStatus("Approved");
            transactToProvider.setFarm(farm());

            accounttransactionRepository.save(transactToProvider);*/

            for (VoucherTableRow voucherTableRow : voucherTableData) {
                Paymentvoucherdetail paymentvoucherdetailForTableRows = new Paymentvoucherdetail();
                paymentvoucherdetailForTableRows.setParticulars(voucherTableRow.getParticulars());
                paymentvoucherdetailForTableRows.setPv(paymentvoucherToProvider);
                paymentvoucherdetailForTableRows.setQty(voucherTableRow.getQuantity());
                paymentvoucherdetailForTableRows.setRate(voucherTableRow.getRate());
                paymentvoucherdetailForTableRows.setFarm(farm());

                paymentvoucherdetailRepository.save(paymentvoucherdetailForTableRows);
            }

            referenceRepository.updatePvByChurch(pv, farm());
            pv++;

            if (withholding > 0) {
                Tax withholdingTax = new Tax();
                withholdingTax.setDate(date);
                withholdingTax.setPv(paymentvoucherToProvider);
                withholdingTax.setType("Withholding Tax");
                withholdingTax.setAmount(withholding);
                withholdingTax.setPaymentpv(pv);
                withholdingTax.setStatus("Pending Payment");

                taxRepository.save(withholdingTax);

                Double withholdingTaxValue = (withholding / 100) * amountBeforeTax;
                Accounttransaction withholdingTaxTransaction = new Accounttransaction();
                withholdingTaxTransaction.setDate(date);
                withholdingTaxTransaction.setRef("PV" + pv);
                withholdingTaxTransaction.setAccount(activity);
                //withholdingTaxTransaction.setAccount(activityRepository.findById(activity).orElse(null));
                withholdingTaxTransaction.setBank(bankaccountRepository.findById(bank).orElse(null));
                withholdingTaxTransaction.setPayeePayer("MERU CENTRAL COFFEE CO-OPERATIVE UNION LTD");
                withholdingTaxTransaction.setDescription("Withholding Tax");
                //withholdingTaxTransaction.setCheque(transRef);
                withholdingTaxTransaction.setActivity(0);
                withholdingTaxTransaction.setDebit(withholdingTaxValue);
                withholdingTaxTransaction.setCredit(0.0);
                withholdingTaxTransaction.setStatus("Approved");
                withholdingTaxTransaction.setFarm(farm());

                Paymentvoucher WithholdingTaxPaymentvoucher = new Paymentvoucher();
                WithholdingTaxPaymentvoucher.setVoucher("PV" + pv);
                WithholdingTaxPaymentvoucher.setDate(date);
                WithholdingTaxPaymentvoucher.setActivity(activity);
                WithholdingTaxPaymentvoucher.setPayeeName("MERU CENTRAL COFFEE CO-OPERATIVE UNION LTD");
                WithholdingTaxPaymentvoucher.setDetails("Withholding Tax");
                WithholdingTaxPaymentvoucher.setFarm(farm());

                Paymentvoucherdetail Withholdingpaymentvoucherdetail = new Paymentvoucherdetail();
                Withholdingpaymentvoucherdetail.setParticulars("Withholding Tax");
                Withholdingpaymentvoucherdetail.setPv(WithholdingTaxPaymentvoucher);
                Withholdingpaymentvoucherdetail.setQty(1.0);
                Withholdingpaymentvoucherdetail.setRate(withholdingTaxValue);
                Withholdingpaymentvoucherdetail.setFarm(farm());

                accounttransactionRepository.save(withholdingTaxTransaction);

                paymentvoucherRepository.save(WithholdingTaxPaymentvoucher);

                paymentvoucherdetailRepository.save(Withholdingpaymentvoucherdetail);

                referenceRepository.updatePvByChurch(pv, farm());
                pv++;

            }

            if (vat > 0) {
                Tax vatTax = new Tax();
                vatTax.setDate(date);
                vatTax.setPv(paymentvoucherToProvider);
                vatTax.setType("Vat Tax");
                vatTax.setAmount(vat);
                vatTax.setPaymentpv(pv);
                vatTax.setStatus("Pending Payment");

                taxRepository.save(vatTax);

                Double vatValue = (vat / 100) * amountBeforeTax;
                Accounttransaction vatTaxTransaction = new Accounttransaction();
                vatTaxTransaction.setDate(date);
                vatTaxTransaction.setRef("PV" + pv);
                vatTaxTransaction.setAccount(activity);
                //vatTaxTransaction.setAccount(activityRepository.findById(activity).orElse(null));
                vatTaxTransaction.setBank(bankaccountRepository.findById(bank).orElse(null));
                vatTaxTransaction.setPayeePayer("MERU CENTRAL COFFEE CO-OPERATIVE UNION LTD");
                vatTaxTransaction.setDescription("VAT Tax");
                //vatTaxTransaction.setCheque(transRef);
                vatTaxTransaction.setActivity(0);
                vatTaxTransaction.setDebit(vatValue);
                vatTaxTransaction.setCredit(0.0);
                vatTaxTransaction.setStatus("Approved");
                vatTaxTransaction.setFarm(farm());

                Paymentvoucher vatTaxPaymentvoucher = new Paymentvoucher();
                vatTaxPaymentvoucher.setVoucher("PV" + pv);
                vatTaxPaymentvoucher.setDate(date);
                vatTaxPaymentvoucher.setActivity(activity);
                vatTaxPaymentvoucher.setPayeeName("MERU CENTRAL COFFEE CO-OPERATIVE UNION LTD");
                vatTaxPaymentvoucher.setDetails("VAT Tax");
                vatTaxPaymentvoucher.setFarm(farm());

                Paymentvoucherdetail vatpaymentvoucherdetail = new Paymentvoucherdetail();
                vatpaymentvoucherdetail.setParticulars("VAT Tax");
                vatpaymentvoucherdetail.setPv(vatTaxPaymentvoucher);
                vatpaymentvoucherdetail.setQty(1.0);
                vatpaymentvoucherdetail.setRate(vatValue);
                vatpaymentvoucherdetail.setFarm(farm());

                accounttransactionRepository.save(vatTaxTransaction);

                paymentvoucherRepository.save(vatTaxPaymentvoucher);

                paymentvoucherdetailRepository.save(vatpaymentvoucherdetail);

                referenceRepository.updatePvByChurch(pv, farm());
                pv++;
            }

            if (profFees > 0) {
                Tax profFeesTax = new Tax();
                profFeesTax.setDate(date);
                profFeesTax.setPv(paymentvoucherToProvider);
                profFeesTax.setType("Professional fees");
                profFeesTax.setAmount(profFees);
                profFeesTax.setPaymentpv(pv);
                profFeesTax.setStatus("Pending Payment");

                taxRepository.save(profFeesTax);

                Double profFeesValue = (profFees / 100) * amountBeforeTax;
                Accounttransaction profFeesTransaction = new Accounttransaction();
                profFeesTransaction.setDate(date);
                profFeesTransaction.setRef("PV" + pv);
                profFeesTransaction.setAccount(activity);
                profFeesTransaction.setAccount(activityRepository.findById(activity).orElse(null).getId());
                profFeesTransaction.setBank(bankaccountRepository.findById(bank).orElse(null));
                profFeesTransaction.setPayeePayer("MERU CENTRAL COFFEE CO-OPERATIVE UNION LTD");
                profFeesTransaction.setDescription("Professional fees");
                //profFeesTransaction.setCheque(transRef);
                profFeesTransaction.setActivity(0);
                profFeesTransaction.setDebit(profFeesValue);
                profFeesTransaction.setCredit(0.0);
                profFeesTransaction.setStatus("Approved");
                profFeesTransaction.setFarm(farm());

                Paymentvoucher WithholdingTaxPaymentvoucher = new Paymentvoucher();
                WithholdingTaxPaymentvoucher.setVoucher("PV" + pv);
                WithholdingTaxPaymentvoucher.setDate(date);
                WithholdingTaxPaymentvoucher.setActivity(activity);
                WithholdingTaxPaymentvoucher.setPayeeName("MERU CENTRAL COFFEE CO-OPERATIVE UNION LTD");
                WithholdingTaxPaymentvoucher.setDetails("Professional fees");
                WithholdingTaxPaymentvoucher.setFarm(farm());

                Paymentvoucherdetail Withholdingpaymentvoucherdetail = new Paymentvoucherdetail();
                Withholdingpaymentvoucherdetail.setParticulars("Professional fees");
                Withholdingpaymentvoucherdetail.setPv(WithholdingTaxPaymentvoucher);
                Withholdingpaymentvoucherdetail.setQty(1.0);
                Withholdingpaymentvoucherdetail.setRate(profFeesValue);
                Withholdingpaymentvoucherdetail.setFarm(farm());

                accounttransactionRepository.save(profFeesTransaction);

                paymentvoucherRepository.save(WithholdingTaxPaymentvoucher);

                paymentvoucherdetailRepository.save(Withholdingpaymentvoucherdetail);

                referenceRepository.updatePvByChurch(pv, farm());
                pv++;
            }

            redirectAttributes.addFlashAttribute("message", "Data saved successfully.");
            response.addProperty("response", "Data saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", e.getLocalizedMessage());
            response.addProperty("response", e.getLocalizedMessage());
        }

        return response.toString();
    }

    @PostMapping("/voucher/update")
    @ResponseBody
    public String updateVoucher(@RequestBody UpdateVoucherRequest request, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        JsonObject response = new JsonObject();
        httpServletRequest.getRequestURI();
        System.out.println("Received data: " + request.toString());
        try {

            int pvId = request.getPv();
            LocalDate date = request.getDate();
            int activity = request.getActivity();
            String payee = request.getPayee();
            String details = request.getDetails();
            int bank = request.getBank();
            String transRef = request.getTransRef();
            double withholding = request.getWithholding();
            double vat = request.getVat();
            double profFees = request.getProfFees();
            Double amountBeforeTax = request.getAmountBeforeTax();
            Double totalPayable = request.getTotalPayable();
            List<VoucherTableRow> voucherTableData = request.getVoucherTableData();
            System.out.println(voucherTableData.toString());

            int pv = pvId;

            Paymentvoucher paymentVoucherBeingEdited = paymentvoucherRepository.findById(pv).orElse(null);
            //paymentVoucherBeingEdited.setVoucher("PV" + pv);
            paymentVoucherBeingEdited.setDate(date);
            paymentVoucherBeingEdited.setActivity(activity);
            paymentVoucherBeingEdited.setPayeeName(payee);
            paymentVoucherBeingEdited.setDetails(details);

            if (accounttransactionRepository.existsByRefAndChurch(paymentVoucherBeingEdited.getVoucher(), farm())) {
                Accounttransaction transactToProvider = accounttransactionRepository.findByRefAndChurch(paymentVoucherBeingEdited.getVoucher(), farm());
                transactToProvider.setDate(date);
                transactToProvider.setAccount(activity);
                transactToProvider.setBank(bankaccountRepository.findById(bank).orElse(null));
                transactToProvider.setPayeePayer(payee);
                transactToProvider.setDescription(details);
                transactToProvider.setCheque(transRef);
                transactToProvider.setActivity(0);
                transactToProvider.setDebit(totalPayable);
                transactToProvider.setStatus("Approved");

                accounttransactionRepository.save(transactToProvider);
            } else {
                PendingTransaction transactToProvider = pendingTransactionRepository.findByRefAndChurch(paymentVoucherBeingEdited.getVoucher(), farm());
                transactToProvider.setDate(date);
                transactToProvider.setAccount(activity);
                transactToProvider.setBank(bankaccountRepository.findById(bank).orElse(null));
                transactToProvider.setPayeePayer(payee);
                transactToProvider.setDescription(details);
                transactToProvider.setCheque(transRef);
                transactToProvider.setActivity(0);
                transactToProvider.setDebit(totalPayable);

                pendingTransactionRepository.save(transactToProvider);
            }

            paymentvoucherRepository.save(paymentVoucherBeingEdited);

            paymentvoucherdetailRepository.deleteByPv(paymentVoucherBeingEdited);

            for (VoucherTableRow voucherTableRow : voucherTableData) {
                Paymentvoucherdetail paymentvoucherdetailForTableRows = new Paymentvoucherdetail();
                paymentvoucherdetailForTableRows.setParticulars(voucherTableRow.getParticulars());
                paymentvoucherdetailForTableRows.setPv(paymentVoucherBeingEdited);
                paymentvoucherdetailForTableRows.setQty(voucherTableRow.getQuantity());
                paymentvoucherdetailForTableRows.setRate(voucherTableRow.getRate());
                paymentvoucherdetailForTableRows.setFarm(farm());

                paymentvoucherdetailRepository.save(paymentvoucherdetailForTableRows);
            }
            if (withholding > 0) {
                Tax withholdingTax = taxRepository.findByPvAndType(paymentVoucherBeingEdited, "Withholding Tax");
                if (withholdingTax == null) {
                    withholdingTax = new Tax();
                    pv = referenceRepository.findByChurch(farm()).getPv() + 1;
                    referenceRepository.updatePvByChurch(pv, farm());
                    withholdingTax.setDate(date);
                    withholdingTax.setPv(paymentVoucherBeingEdited);
                    withholdingTax.setType("Withholding Tax");
                    withholdingTax.setAmount(withholding);
                    withholdingTax.setPaymentpv(pv);
                    withholdingTax.setStatus("Pending Payment");

                    taxRepository.save(withholdingTax);

                    Double withholdingTaxValue = (withholding / 100) * amountBeforeTax;
                    Accounttransaction withholdingTaxTransaction = new Accounttransaction();
                    withholdingTaxTransaction.setDate(date);
                    withholdingTaxTransaction.setRef("PV" + pv);
                    withholdingTaxTransaction.setAccount(activity);
                    //withholdingTaxTransaction.setAccount(activityRepository.findById(activity).orElse(null));
                    withholdingTaxTransaction.setBank(bankaccountRepository.findById(bank).orElse(null));
                    withholdingTaxTransaction.setPayeePayer("MERU CENTRAL COFFEE CO-OPERATIVE UNION LTD");
                    withholdingTaxTransaction.setDescription("Withholding Tax");
                    //withholdingTaxTransaction.setCheque(transRef);
                    withholdingTaxTransaction.setActivity(0);
                    withholdingTaxTransaction.setDebit(withholdingTaxValue);
                    withholdingTaxTransaction.setStatus("Approved");
                    withholdingTaxTransaction.setFarm(farm());

                    Paymentvoucher WithholdingTaxPaymentvoucher = new Paymentvoucher();
                    WithholdingTaxPaymentvoucher.setVoucher("PV" + pv);
                    WithholdingTaxPaymentvoucher.setDate(date);
                    WithholdingTaxPaymentvoucher.setActivity(activity);
                    WithholdingTaxPaymentvoucher.setPayeeName("MERU CENTRAL COFFEE CO-OPERATIVE UNION LTD");
                    WithholdingTaxPaymentvoucher.setDetails("Withholding Tax");
                    WithholdingTaxPaymentvoucher.setFarm(farm());

                    paymentvoucherRepository.save(WithholdingTaxPaymentvoucher);

                    Paymentvoucherdetail Withholdingpaymentvoucherdetail = new Paymentvoucherdetail();
                    Withholdingpaymentvoucherdetail.setParticulars("Withholding Tax");
                    Withholdingpaymentvoucherdetail.setPv(paymentvoucherRepository.findByVoucherAndChurch(WithholdingTaxPaymentvoucher.getVoucher(), farm()));
                    Withholdingpaymentvoucherdetail.setQty(1.0);
                    Withholdingpaymentvoucherdetail.setRate(withholdingTaxValue);
                    Withholdingpaymentvoucherdetail.setFarm(farm());

                    accounttransactionRepository.save(withholdingTaxTransaction);

                    paymentvoucherdetailRepository.save(Withholdingpaymentvoucherdetail);
                } else {
                    withholdingTax.setDate(date);
                    //withholdingTax.setPv(paymentVoucherBeingEdited);
                    //withholdingTax.setType("Withholding Tax");
                    withholdingTax.setAmount(withholding);
                    //withholdingTax.setPaymentpv("");
                    //withholdingTax.setStatus("Pending Payment");

                    taxRepository.save(withholdingTax);

                    Double withholdingTaxValue = (withholding / 100) * amountBeforeTax;
                    Accounttransaction withholdingTaxTransaction = accounttransactionRepository.findByRefAndChurch("PV" + withholdingTax.getPaymentpv(), farm());
                    withholdingTaxTransaction.setDate(date);
                    //withholdingTaxTransaction.setRef("PV" + pv);
                    withholdingTaxTransaction.setAccount(activity);
                    //withholdingTaxTransaction.setAccount(activityRepository.findById(activity).orElse(null));
                    withholdingTaxTransaction.setBank(bankaccountRepository.findById(bank).orElse(null));
                    withholdingTaxTransaction.setPayeePayer("MERU CENTRAL COFFEE CO-OPERATIVE UNION LTD");
                    withholdingTaxTransaction.setDescription("Withholding Tax");
                    //withholdingTaxTransaction.setCheque(transRef);
                    withholdingTaxTransaction.setActivity(0);
                    withholdingTaxTransaction.setDebit(withholdingTaxValue);
                    withholdingTaxTransaction.setStatus("Approved");

                    Paymentvoucherdetail Withholdingpaymentvoucherdetail = paymentvoucherdetailRepository.findByPvAndChurch(paymentvoucherRepository.findByVoucherAndChurch("PV" + withholdingTax.getPaymentpv(), farm()).getId(), farm());
                    Withholdingpaymentvoucherdetail.setRate(withholdingTaxValue);

                    accounttransactionRepository.save(withholdingTaxTransaction);
                    paymentvoucherdetailRepository.save(Withholdingpaymentvoucherdetail);
                }

            }
            if (vat > 0) {
                Tax vatTax = taxRepository.findByPvAndType(paymentVoucherBeingEdited, "vat Tax");
                if (vatTax == null) {
                    vatTax = new Tax();
                    pv = referenceRepository.findByChurch(farm()).getPv() + 1;
                    referenceRepository.updatePvByChurch(pv, farm());
                    vatTax.setDate(date);
                    vatTax.setPv(paymentVoucherBeingEdited);
                    vatTax.setType("vat Tax");
                    vatTax.setAmount(vat);
                    vatTax.setPaymentpv(pv);
                    vatTax.setStatus("Pending Payment");

                    taxRepository.save(vatTax);

                    Double vatTaxValue = (vat / 100) * amountBeforeTax;
                    Accounttransaction vatTaxTransaction = new Accounttransaction();
                    vatTaxTransaction.setDate(date);
                    vatTaxTransaction.setRef("PV" + pv);
                    vatTaxTransaction.setAccount(activity);
                    //vatTaxTransaction.setAccount(activityRepository.findById(activity).orElse(null));
                    vatTaxTransaction.setBank(bankaccountRepository.findById(bank).orElse(null));
                    vatTaxTransaction.setPayeePayer("MERU CENTRAL COFFEE CO-OPERATIVE UNION LTD");
                    vatTaxTransaction.setDescription("vat Tax");
                    //vatTaxTransaction.setCheque(transRef);
                    vatTaxTransaction.setActivity(0);
                    vatTaxTransaction.setDebit(vatTaxValue);
                    vatTaxTransaction.setStatus("Approved");
                    vatTaxTransaction.setFarm(farm());

                    Paymentvoucher vatTaxPaymentvoucher = new Paymentvoucher();
                    vatTaxPaymentvoucher.setVoucher("PV" + pv);
                    vatTaxPaymentvoucher.setDate(date);
                    vatTaxPaymentvoucher.setActivity(activity);
                    vatTaxPaymentvoucher.setPayeeName("MERU CENTRAL COFFEE CO-OPERATIVE UNION LTD");
                    vatTaxPaymentvoucher.setDetails("vat Tax");
                    vatTaxPaymentvoucher.setFarm(farm());

                    paymentvoucherRepository.save(vatTaxPaymentvoucher);

                    Paymentvoucherdetail vatpaymentvoucherdetail = new Paymentvoucherdetail();
                    vatpaymentvoucherdetail.setParticulars("vat Tax");
                    vatpaymentvoucherdetail.setPv(paymentvoucherRepository.findByVoucherAndChurch(vatTaxPaymentvoucher.getVoucher(), farm()));
                    vatpaymentvoucherdetail.setQty(1.0);
                    vatpaymentvoucherdetail.setRate(vatTaxValue);
                    vatpaymentvoucherdetail.setFarm(farm());

                    accounttransactionRepository.save(vatTaxTransaction);

                    paymentvoucherdetailRepository.save(vatpaymentvoucherdetail);
                } else {
                    vatTax.setDate(date);
                    //vatTax.setPv(paymentVoucherBeingEdited);
                    //vatTax.setType("vat Tax");
                    vatTax.setAmount(vat);
                    //vatTax.setPaymentpv("");
                    //vatTax.setStatus("Pending Payment");

                    taxRepository.save(vatTax);

                    Double vatTaxValue = (vat / 100) * amountBeforeTax;
                    Accounttransaction vatTaxTransaction = accounttransactionRepository.findByRefAndChurch("PV" + vatTax.getPaymentpv(), farm());
                    vatTaxTransaction.setDate(date);
                    //vatTaxTransaction.setRef("PV" + pv);
                    vatTaxTransaction.setAccount(activity);
                    //vatTaxTransaction.setAccount(activityRepository.findById(activity).orElse(null));
                    vatTaxTransaction.setBank(bankaccountRepository.findById(bank).orElse(null));
                    vatTaxTransaction.setPayeePayer("MERU CENTRAL COFFEE CO-OPERATIVE UNION LTD");
                    vatTaxTransaction.setDescription("vat Tax");
                    //vatTaxTransaction.setCheque(transRef);
                    vatTaxTransaction.setActivity(0);
                    vatTaxTransaction.setDebit(vatTaxValue);
                    vatTaxTransaction.setStatus("Approved");

                    Paymentvoucherdetail vatpaymentvoucherdetail = paymentvoucherdetailRepository.findByPvAndChurch(paymentvoucherRepository.findByVoucherAndChurch("PV" + vatTax.getPaymentpv(), farm()).getId(), farm());
                    vatpaymentvoucherdetail.setRate(vatTaxValue);

                    accounttransactionRepository.save(vatTaxTransaction);
                    paymentvoucherdetailRepository.save(vatpaymentvoucherdetail);
                }

            }
            if (profFees > 0) {
                Tax profFeesTax = taxRepository.findByPvAndType(paymentVoucherBeingEdited, "Professional fees");
                if (profFeesTax == null) {
                    profFeesTax = new Tax();
                    pv = referenceRepository.findByChurch(farm()).getPv() + 1;
                    referenceRepository.updatePvByChurch(pv, farm());
                    profFeesTax.setDate(date);
                    profFeesTax.setPv(paymentVoucherBeingEdited);
                    profFeesTax.setType("Professional fees");
                    profFeesTax.setAmount(profFees);
                    profFeesTax.setPaymentpv(pv);
                    profFeesTax.setStatus("Pending Payment");

                    taxRepository.save(profFeesTax);

                    Double profFeesTaxValue = (profFees / 100) * amountBeforeTax;
                    Accounttransaction profFeesTaxTransaction = new Accounttransaction();
                    profFeesTaxTransaction.setDate(date);
                    profFeesTaxTransaction.setRef("PV" + pv);
                    profFeesTaxTransaction.setAccount(activity);
                    //profFeesTaxTransaction.setAccount(activityRepository.findById(activity).orElse(null));
                    profFeesTaxTransaction.setBank(bankaccountRepository.findById(bank).orElse(null));
                    profFeesTaxTransaction.setPayeePayer("MERU CENTRAL COFFEE CO-OPERATIVE UNION LTD");
                    profFeesTaxTransaction.setDescription("Professional fees");
                    //profFeesTaxTransaction.setCheque(transRef);
                    profFeesTaxTransaction.setActivity(0);
                    profFeesTaxTransaction.setDebit(profFeesTaxValue);
                    profFeesTaxTransaction.setStatus("Approved");
                    profFeesTaxTransaction.setFarm(farm());

                    Paymentvoucher profFeesTaxPaymentvoucher = new Paymentvoucher();
                    profFeesTaxPaymentvoucher.setVoucher("PV" + pv);
                    profFeesTaxPaymentvoucher.setDate(date);
                    profFeesTaxPaymentvoucher.setActivity(activity);
                    profFeesTaxPaymentvoucher.setPayeeName("MERU CENTRAL COFFEE CO-OPERATIVE UNION LTD");
                    profFeesTaxPaymentvoucher.setDetails("Professional fees");
                    profFeesTaxPaymentvoucher.setFarm(farm());

                    paymentvoucherRepository.save(profFeesTaxPaymentvoucher);

                    Paymentvoucherdetail profFeespaymentvoucherdetail = new Paymentvoucherdetail();
                    profFeespaymentvoucherdetail.setParticulars("Professional fees");
                    profFeespaymentvoucherdetail.setPv(paymentvoucherRepository.findByVoucherAndChurch("PV" + profFeesTax.getPaymentpv(), farm()));
                    profFeespaymentvoucherdetail.setQty(1.0);
                    profFeespaymentvoucherdetail.setRate(profFeesTaxValue);
                    profFeespaymentvoucherdetail.setFarm(farm());

                    accounttransactionRepository.save(profFeesTaxTransaction);

                    paymentvoucherdetailRepository.save(profFeespaymentvoucherdetail);
                } else {
                    profFeesTax.setDate(date);
                    //profFeesTax.setPv(paymentVoucherBeingEdited);
                    //profFeesTax.setType("Professional fees");
                    profFeesTax.setAmount(profFees);
                    //profFeesTax.setPaymentpv("");
                    //profFeesTax.setStatus("Pending Payment");

                    taxRepository.save(profFeesTax);

                    Double profFeesTaxValue = (profFees / 100) * amountBeforeTax;
                    Accounttransaction profFeesTaxTransaction = accounttransactionRepository.findByRefAndChurch("PV" + profFeesTax.getPaymentpv(), farm());
                    profFeesTaxTransaction.setDate(date);
                    //profFeesTaxTransaction.setRef("PV" + pv);
                    profFeesTaxTransaction.setAccount(activity);
                    //profFeesTaxTransaction.setAccount(activityRepository.findById(activity).orElse(null));
                    profFeesTaxTransaction.setBank(bankaccountRepository.findById(bank).orElse(null));
                    profFeesTaxTransaction.setPayeePayer("MERU CENTRAL COFFEE CO-OPERATIVE UNION LTD");
                    profFeesTaxTransaction.setDescription("Professional fees");
                    //profFeesTaxTransaction.setCheque(transRef);
                    profFeesTaxTransaction.setActivity(0);
                    profFeesTaxTransaction.setDebit(profFeesTaxValue);
                    profFeesTaxTransaction.setStatus("Approved");

                    Paymentvoucherdetail profFeespaymentvoucherdetail = paymentvoucherdetailRepository.findByPvAndChurch(paymentvoucherRepository.findByVoucherAndChurch("PV" + profFeesTax.getPaymentpv(), farm()).getId(), farm());
                    profFeespaymentvoucherdetail.setRate(profFeesTaxValue);

                    accounttransactionRepository.save(profFeesTaxTransaction);
                    paymentvoucherdetailRepository.save(profFeespaymentvoucherdetail);
                }

            }

            redirectAttributes.addFlashAttribute("message", "Data updated successfully.");
            response.addProperty("response", "Data updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", e.getLocalizedMessage());
            response.addProperty("response", e.getLocalizedMessage());
        }

        return response.toString();
    }

    private void approveVouchers() {

        try {
            List<VoucherSignatory> voucherSignatories = voucherSignatoryRepository.findByVoucher_Church(farm());
            System.out.println(voucherSignatories.size());

            for (VoucherSignatory voucherSignatory : voucherSignatories) {
                System.out.println("voucher: " + voucherSignatory.getId());
                ;
                voucherSignatory.setAccountant(1);
                voucherSignatory.setAccountantDate(LocalDate.now());
                voucherSignatory.setTreasurer(1);
                voucherSignatory.setTrDate(LocalDate.now());
                voucherSignatory.setSecondSignatory(1);
                voucherSignatory.setSsDate(LocalDate.now());
                voucherSignatory.setSeniorPastor(1);
                voucherSignatory.setSpDate(LocalDate.now());
                VoucherSignatory approvedVoucherSignatory = voucherSignatoryRepository.save(voucherSignatory);
                System.out.println("approved voucher signatory; " + approvedVoucherSignatory.getVoucher().getId());

                Paymentvoucher approvedPaymentvoucher = paymentvoucherRepository.findById(approvedVoucherSignatory.getVoucher().getId()).get();
                approvedPaymentvoucher.setStatus("Approved");
                System.out.println("approved payment voucher; " + approvedPaymentvoucher.getId());
                paymentvoucherRepository.save(approvedPaymentvoucher);

                if (pendingTransactionRepository.existsByVoucher_Id(approvedPaymentvoucher.getId())) {
                    PendingTransaction pendingTransaction = pendingTransactionRepository.findByVoucher_Id(approvedPaymentvoucher.getId());
                    System.out.println("Pending tr id: " + pendingTransaction.getId());
                    Accounttransaction accounttransaction = new Accounttransaction();
                    accounttransaction.setDate(pendingTransaction.getDate());
                    accounttransaction.setRef(pendingTransaction.getRef());
                    accounttransaction.setAccount(pendingTransaction.getAccount());
                    accounttransaction.setBank(pendingTransaction.getBank());
                    accounttransaction.setPayeePayer(pendingTransaction.getPayeePayer());
                    accounttransaction.setDescription(pendingTransaction.getDescription());
                    accounttransaction.setCheque(pendingTransaction.getCheque());
                    accounttransaction.setActivity(pendingTransaction.getActivity());
                    accounttransaction.setCredit(pendingTransaction.getCredit());
                    accounttransaction.setDebit(pendingTransaction.getDebit());
                    accounttransaction.setStatus("Approved");
                    accounttransaction.setFarm(pendingTransaction.getFarm());

                    accounttransactionRepository.save(accounttransaction);

                    pendingTransactionRepository.delete(pendingTransaction);

                    System.out.println("voucher approved: " + pendingTransaction.getVoucher().getId());
                }
            }
            System.out.println("success Message:  Success");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Message:  Error");
        }
    }

    /*@GetMapping(value = "/voucher/approve/{pvId}", produces = "application/json")
    @ResponseBody
    private String approveVoucher(@PathVariable Integer pvId) {
        JsonObject jsonObject = new JsonObject();

        try {
            Set<Role> roles = userRepository.findById(userId()).get().getRoles();
            VoucherSignatory voucherSignatory = voucherSignatoryRepository.findByVoucher_Id(pvId);

            Set<ERole> processedRoles = new HashSet<>();
            for (Role role : roles) {
                if (!processedRoles.add(role.getName())) {
                    throw new DuplicateRoleException("Duplicate role found: " + role.getName());
                }
                System.out.println(role.getName());
                switch (role.getName()) {
                    case ROLE_ACCOUNTANT -> {
                        voucherSignatory.setAccountant(1);
                        voucherSignatory.setAccountantDate(LocalDate.now());
                    }
                    case ROLE_TREASURER -> {
                        voucherSignatory.setTreasurer(1);
                        voucherSignatory.setTrDate(LocalDate.now());
                    }
                    case ROLE_SENIOR_PASTOR -> {
                        voucherSignatory.setSeniorPastor(1);
                        voucherSignatory.setSpDate(LocalDate.now());
                    }
                    case ROLE_SECOND_SIGNATORY -> {
                        voucherSignatory.setSecondSignatory(1);
                        voucherSignatory.setSsDate(LocalDate.now());
                    }
                    default ->
                            throw new UnauthorisedAccessException("You do not have the rights to approve this voucher!");
                }
            }

            VoucherSignatory approvedVoucher = voucherSignatoryRepository.save(voucherSignatory);
            // TODO: 03/11/2023 addother signatories
            int signatories = 0;
            if (approvedVoucher.getAccountant() == 1) {
                signatories++;
            }
            if (approvedVoucher.getSeniorPastor() == 1) {
                signatories++;
            }
            if (approvedVoucher.getTreasurer() == 1) {
                signatories++;
            }
            if (approvedVoucher.getSecondSignatory() == 1) {
                signatories++;
            }
            if (signatories > 1) {
                Paymentvoucher paymentvoucher = paymentvoucherRepository.findById(pvId).get();
                paymentvoucher.setStatus("Approved");
                paymentvoucherRepository.save(paymentvoucher);

                PendingTransaction pendingTransaction = pendingTransactionRepository.findByVoucher_Id(pvId);
                Accounttransaction accounttransaction = new Accounttransaction();
                accounttransaction.setDate(pendingTransaction.getDate());
                accounttransaction.setRef(pendingTransaction.getRef());
                accounttransaction.setAccount(pendingTransaction.getAccount());
                accounttransaction.setBank(pendingTransaction.getBank());
                accounttransaction.setPayeePayer(pendingTransaction.getPayeePayer());
                accounttransaction.setDescription(pendingTransaction.getDescription());
                accounttransaction.setCheque(pendingTransaction.getCheque());
                accounttransaction.setActivity(pendingTransaction.getActivity());
                accounttransaction.setCredit(pendingTransaction.getCredit());
                accounttransaction.setDebit(pendingTransaction.getDebit());
                accounttransaction.setStatus("Approved");
                accounttransaction.setFarm(pendingTransaction.getFarm());

                accounttransactionRepository.save(accounttransaction);

                pendingTransactionRepository.delete(pendingTransaction);
            }

            jsonObject.addProperty("response", "Success");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.addProperty("response", "Failed to approve");
        }

        return jsonObject.toString();
    }*/

    /*@GetMapping(value = "/voucher/reject/{pvId}", produces = "application/json")
    @ResponseBody
    private String rejectVoucher(@PathVariable Integer pvId) {
        JsonObject jsonObject = new JsonObject();

        try {
            Set<Role> roles = userRepository.findById(userId()).get().getRoles();
            VoucherSignatory voucherSignatory = voucherSignatoryRepository.findByVoucher_Id(pvId);

            for (Role role : roles) {
                if (role.getName().equals(ERole.ROLE_ACCOUNTANT)) {
                    voucherSignatory.setAccountant(2);
                }
                if (role.getName().equals(ERole.ROLE_TREASURER)) {
                    voucherSignatory.setTreasurer(2);
                }
                if (role.getName().equals(ERole.ROLE_SENIOR_PASTOR)) {
                    voucherSignatory.setAccountant(2);
                }
                if (role.getName().equals(ERole.ROLE_SECOND_SIGNATORY)) {
                    voucherSignatory.setSecondSignatory(2);
                }
            }

            VoucherSignatory approvedVoucher = voucherSignatoryRepository.save(voucherSignatory);
            if (approvedVoucher.getAccountant() == 2 || approvedVoucher.getTreasurer() == 2 || approvedVoucher.getSeniorPastor() == 2 || approvedVoucher.getSecondSignatory() == 2) {
                Paymentvoucher paymentvoucher = paymentvoucherRepository.findById(pvId).get();
                paymentvoucher.setStatus("Rejected");
                paymentvoucherRepository.save(paymentvoucher);

                PendingTransaction pendingTransaction = pendingTransactionRepository.findByVoucher_Id(pvId);
                pendingTransaction.setStatus("Rejected");
                pendingTransactionRepository.save(pendingTransaction);
            } else {
                Paymentvoucher paymentvoucher = paymentvoucherRepository.findById(pvId).get();
                paymentvoucher.setStatus("Pending");
                paymentvoucherRepository.save(paymentvoucher);

                PendingTransaction pendingTransaction = pendingTransactionRepository.findByVoucher_Id(pvId);
                pendingTransaction.setStatus("Pending");
                pendingTransactionRepository.save(pendingTransaction);
            }

            jsonObject.addProperty("response", "Success");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.addProperty("response", "Failed to reject");
        }

        return jsonObject.toString();
    }*/

    @GetMapping(value = "/voucher/delete/{pvId}", produces = "application/json")
    @ResponseBody
    private String deleteVoucher(@PathVariable Integer pvId) {
        JsonObject jsonObject = new JsonObject();

        try {
            System.out.println(pvId);
            if (paymentvoucherRepository.findById(pvId).get().getStatus().equalsIgnoreCase("approved")) {
                paymentvoucherRepository.deleteById(pvId);
                accounttransactionRepository.deleteByRefAndFarm("PV" + pvId, farm());
            } else {
                paymentvoucherRepository.deleteById(pvId);
            }


            jsonObject.addProperty("response", "Success");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.addProperty("response", "Failed to reject");
        }

        return jsonObject.toString();
    }

    @PostMapping("/receipt/add")
    @ResponseBody
    public String addReceipt(@RequestBody ReceiptRequest request, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        JsonObject response = new JsonObject();
        httpServletRequest.getRequestURI();
        try {
            System.out.println("Received data: " + request.toString());

            LocalDate date = request.getDate();
            String payee = request.getReceivedFrom();
            String details = request.getDetails();
            int bank = request.getBank();
            String transRef = request.getTransRef();
            List<ReceiptTableRow> voucherTableData = request.getReceiptTableRowList();
            System.out.println(voucherTableData.toString());

            int rctRef = referenceRepository.findByChurch(farm()).getRct() + 1;

            for (ReceiptTableRow receiptTableRow : voucherTableData) {
                Accounttransaction transactToProvider = new Accounttransaction();
                transactToProvider.setDate(date);
                transactToProvider.setRef("RCT" + rctRef);
                transactToProvider.setAccount(receiptTableRow.getActivity());
                //transactToProvider.setAccount(activityRepository.findById(activity).orElse(null));
                transactToProvider.setBank(bankaccountRepository.findById(bank).orElse(null));
                transactToProvider.setPayeePayer(payee);
                transactToProvider.setDescription(details);
                transactToProvider.setCheque(transRef);
                transactToProvider.setActivity(0);
                transactToProvider.setCredit(receiptTableRow.getAmount());
                transactToProvider.setDebit(0.0);
                transactToProvider.setStatus("Approved");
                transactToProvider.setFarm(farm());

                accounttransactionRepository.save(transactToProvider);
            }

            referenceRepository.updateRctByChurch(rctRef, farm());

            redirectAttributes.addFlashAttribute("message", "Data saved successfully.");
            response.addProperty("response", "Data saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", e.getLocalizedMessage());
            response.addProperty("response", e.getLocalizedMessage());
        }

        return response.toString();
    }


    @GetMapping("voucher/get/{pv}")
    @ResponseBody
    public List<PaymentVoucherMapper> voucher(@PathVariable Integer pv) {
        List<PaymentVoucherMapper> paymentVouchers = new ArrayList<>();
        try {
            Connection conn = DbConnector.getConnection();
            String getVoucherQuery = "select `Pv id`,\n" +
                    "       `Voucher #`,\n" +
                    "       DATE,\n" +
                    "       `Payee Name`,\n" +
                    "       detailid,\n" +
                    "       Particulars,\n" +
                    "       Qty,\n" +
                    "       Rate,\n" +
                    "       total,\n" +
                    "       Account,\n" +
                    "       details," +
                    "       `cheque #` as transRef,\n" +
                    "       activ,\n" +
                    "       ifnull(MAX(`Withholding Tax`), ''),\n" +
                    "       ifnull(MAX(`Vat Tax`), ''),\n" +
                    "       ifnull(MAX(`Professional fees`), '')\n" +
                    "\n" +
                    "FROM (SELECT `Pv id`\n" +
                    "           , `Voucher #`\n" +
                    "           , p.DATE\n" +
                    "           , `Payee Name`\n" +
                    "           , detailid\n" +
                    "           , Particulars\n" +
                    "           , Qty\n" +
                    "           , Rate\n" +
                    "           , (Qty * Rate)                                               AS total\n" +
                    "           , `Account id`                                               AS Account\n" +
                    "           , details" +
                    "           , `cheque #`\n" +
                    "           , `Acc id`                                                   AS activ\n" +
                    "           , case when t.`type` = 'Withholding Tax' then t.amount END   AS 'Withholding Tax'\n" +
                    "           , case when t.`type` = 'Vat Tax' then t.amount END           AS 'Vat Tax'\n" +
                    "           , case when t.`type` = 'Professional fees' then t.amount END AS 'Professional fees'\n" +
                    "      from paymentvouchers p\n" +
                    "               INNER JOIN paymentvoucherdetails pd ON pd.`pv#` = `Pv id`\n" +
                    "               INNER JOIN activities a ON `account id` = p.activity\n" +
                    "               INNER JOIN accounttransactions ac ON ac.`Ref #` = p.`Voucher #`\n" +
                    "               INNER JOIN bankaccounts b ON `Acc id` = ac.Bank\n" +
                    "               left JOIN tax t ON t.`pv#` = p.`Pv id`\n" +
                    "      WHERE pd.`pv#` = " + pv + " \n" +
                    "        and p.farm = " + farm().getId() + "\n" +
                    "        and ac.farm = " + farm().getId() + ") f\n" +
                    "GROUP BY detailid\n" +
                    "\n" +
                    "union all\n" +
                    "select `Pv id`,\n" +
                    "       `Voucher #`,\n" +
                    "       DATE,\n" +
                    "       `Payee Name`,\n" +
                    "       detailid,\n" +
                    "       Particulars,\n" +
                    "       Qty,\n" +
                    "       Rate,\n" +
                    "       total,\n" +
                    "       Account,\n" +
                    "       details," +
                    "       `cheque #`,\n" +
                    "       activ,\n" +
                    "       ifnull(MAX(`Withholding Tax`), ''),\n" +
                    "       ifnull(MAX(`Vat Tax`), ''),\n" +
                    "       ifnull(MAX(`Professional fees`), '')\n" +
                    "\n" +
                    "FROM (SELECT `Pv id`\n" +
                    "           , `Voucher #`\n" +
                    "           , p.DATE\n" +
                    "           , `Payee Name`\n" +
                    "           , detailid\n" +
                    "           , Particulars\n" +
                    "           , Qty\n" +
                    "           , Rate\n" +
                    "           , (Qty * Rate)                                               AS total\n" +
                    "           , `Account id`                                               AS Account\n" +
                    "           , details" +
                    "           , `cheque #`\n" +
                    "           , `Acc id`                                                   AS activ\n" +
                    "           , case when t.`type` = 'Withholding Tax' then t.amount END   AS 'Withholding Tax'\n" +
                    "           , case when t.`type` = 'Vat Tax' then t.amount END           AS 'Vat Tax'\n" +
                    "           , case when t.`type` = 'Professional fees' then t.amount END AS 'Professional fees'\n" +
                    "      from paymentvouchers p\n" +
                    "               INNER JOIN paymentvoucherdetails pd ON pd.`pv#` = `Pv id`\n" +
                    "               INNER JOIN activities a ON `account id` = p.activity\n" +
                    "               INNER JOIN pending_transaction ac ON ac.`Ref #` = p.`Voucher #`\n" +
                    "               INNER JOIN bankaccounts b ON `Acc id` = ac.Bank\n" +
                    "               left JOIN tax t ON t.`pv#` = p.`Pv id`\n" +
                    "      WHERE pd.`pv#` = " + pv + "\n" +
                    "        and p.farm = " + farm().getId() + "\n" +
                    "        and ac.farm = " + farm().getId() + ") f\n" +
                    "GROUP BY detailid";

            System.out.println(getVoucherQuery);

            ResultSet resultSet = conn.prepareStatement(getVoucherQuery).executeQuery();
            while (resultSet.next()) {
                PaymentVoucherMapper paymentVoucher = new PaymentVoucherMapper();
                paymentVoucher.setPvId(resultSet.getString("Pv id"));
                paymentVoucher.setVoucherNumber(resultSet.getString("Voucher #"));
                paymentVoucher.setDate(resultSet.getDate("DATE"));
                paymentVoucher.setPayeeName(resultSet.getString("Payee Name"));
                paymentVoucher.setDetailId(resultSet.getString("detailid"));
                paymentVoucher.setParticulars(resultSet.getString("Particulars"));
                paymentVoucher.setQuantity(resultSet.getInt("Qty"));
                paymentVoucher.setRate(resultSet.getDouble("Rate"));
                paymentVoucher.setTotal(resultSet.getDouble("total"));
                paymentVoucher.setAccount(resultSet.getString("Account"));
                paymentVoucher.setDetails(resultSet.getString("details"));
                paymentVoucher.setTransRef(resultSet.getString("transRef"));
                paymentVoucher.setActivity(resultSet.getString("activ"));
                paymentVoucher.setWithholdingTax(resultSet.getDouble("ifnull(MAX(`Withholding Tax`), '')"));
                paymentVoucher.setVatTax(resultSet.getDouble("ifnull(MAX(`Vat Tax`), '')"));
                paymentVoucher.setProfessionalFees(resultSet.getDouble("ifnull(MAX(`Professional fees`), '')"));

                paymentVouchers.add(paymentVoucher);
            }
            resultSet.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return paymentVouchers;
    }

    @GetMapping("receipt/get/{rct}")
    @ResponseBody
    public ReceiptsEdit receipt(@PathVariable String rct) {
        ReceiptsEdit receipt = new ReceiptsEdit();
        try {
            Connection conn = DbConnector.getConnection();
            String getVoucherQuery = "SELECT *\n" +
                    "FROM (SELECT `Transaction id`,\n" +
                    "             `Ref #`,\n" +
                    "             `Acc id`,\n" +
                    "             p.Date,\n" +
                    "             `Payee/Payer`                   AS 'Received from',\n" +
                    "             q.`Account`            'Description',\n" +
                    "             q.`Account id`            'account',\n" +
                    "             Description                        'Details',\n" +
                    "             credit                     AS Amount," +
                    "               `cheque #`               AS transRef \n" +
                    "      FROM accounttransactions p\n" +
                    "               INNER JOIN activities q ON q.`Account id` = p.account\n" +
                    "               INNER JOIN bankaccounts b ON `Acc id` = p.Bank\n" +
                    "      WHERE p.farm = " + farm().getId() + " and credit != '0'  and `Ref #` = '" + rct + "'\n" +
                    "      ) k";
            ResultSet resultSet = conn.prepareStatement(getVoucherQuery).executeQuery();
            List<ReceiptTableRowEdit> receiptTableRows = new ArrayList<>();
            while (resultSet.next()) {
                receipt.setReferenceNumber(resultSet.getString("Ref #"));
                receipt.setBank(resultSet.getInt("Acc id"));
                receipt.setDate(resultSet.getDate("DATE").toLocalDate());
                receipt.setReceivedFrom(resultSet.getString("Received from"));
                receipt.setDetails(resultSet.getString("Details"));
                receipt.setTransRef(resultSet.getString("transRef"));
                ReceiptTableRowEdit receiptTableRow = new ReceiptTableRowEdit(resultSet.getInt("Transaction id"), resultSet.getInt("account"), resultSet.getDouble("Amount"));
                receiptTableRows.add(receiptTableRow);
            }
            receipt.setReceiptTableRows(receiptTableRows);
            resultSet.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return receipt;
    }

    @PostMapping("/receipt/update")
    @ResponseBody
    public String updateReceipt(@RequestBody ReceiptRequestUpdate request, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        JsonObject response = new JsonObject();
        httpServletRequest.getRequestURI();
        try {
            System.out.println("Received data: " + request.toString());

            String referenceNumber = request.getReferenceNumber();
            LocalDate date = request.getDate();
            String payee = request.getReceivedFrom();
            String details = request.getDetails();
            int bank = request.getBank();
            String transRef = request.getTransRef();
            List<ReceiptTableRowEdit> voucherTableData = request.getReceiptTableRowList();
            System.out.println(voucherTableData.toString());

            //int rctRef = referenceRepository.findByChurch(farm()).getRct() + 1;

            for (ReceiptTableRowEdit receiptTableRow : voucherTableData) {
                Accounttransaction receipt = accounttransactionRepository.findByIdAndRef(receiptTableRow.getTransactionId(), referenceNumber);
                if (receipt != null) {
                    receipt.setDate(date);
                    //receipt.setRef("RCT" + rctRef);
                    receipt.setAccount(receiptTableRow.getActivity());
                    //receipt.setAccount(activityRepository.findById(activity).orElse(null));
                    receipt.setBank(bankaccountRepository.findById(bank).orElse(null));
                    receipt.setPayeePayer(payee);
                    receipt.setDescription(details);
                    receipt.setCheque(transRef);
                    receipt.setActivity(0);
                    receipt.setCredit(receiptTableRow.getAmount());
                    receipt.setStatus("Approved");

                    accounttransactionRepository.save(receipt);
                } else {
                    response.addProperty("response", "Could not find the receipt to update");
                    return response.toString();
                }
            }

            //referenceRepository.updateRctById(rctRef, 1);

            redirectAttributes.addFlashAttribute("message", "Data updated successfully.");
            response.addProperty("response", "Data updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", e.getLocalizedMessage());
            response.addProperty("response", e.getLocalizedMessage());
        }
        return response.toString();
    }


}
