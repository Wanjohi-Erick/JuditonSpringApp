package com.rickiey_innovates.juditonspringapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rickiey_innovates.juditonspringapp.DbConnector;
import com.rickiey_innovates.juditonspringapp.models.Farm;
import com.rickiey_innovates.juditonspringapp.repositories.UserRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import com.rickiey_innovates.juditonspringapp.models.inventory.*;
import com.rickiey_innovates.juditonspringapp.repositories.inventory.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.Date;

@Controller
public class InventoryController {

    @Value("${purchase.orders.history.path}")
    private String purchase_orders_history_path;

    @Value("${purchase.receive.history.path}")
    private String purchase_receive_history_path;

    @Value("${purchase.orders.path}")
    private String purchase_orders_path;

    @Value("${requisition.form.path}")
    private String requisition_form;

    @Value("${active.purchase.orders.path}")
    private String active_purchase_orders_path;

    @Value("${inventory.overdue.path}")
    private String inventory_overdue_path;

    @Value("${low.stock.items.path}")
    private String low_stock_items_path;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    AssetDepreciationRepository assetDepreciationRepository;
    @Autowired
    VendorRepository vendorRepository;
    @Autowired
    ItemAdjustmentRepository itemAdjustmentRepository;
    @Autowired
    ItemStockRepository itemStockRepository;
    @Autowired
    ItemGroupRepository itemGroupRepository;
    @Autowired
    private IssuedItemRepository issuedItemRepository;
    @Autowired
    private PaymentRequestRepository paymentRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AssetsRepository assetsRepository;

    public InventoryController() {
    }

    private Long userId() {
        return LoginController.getLoggedInUserId();
    }

    private Farm farm() {
        return userRepository.findById(userId()).orElse(null).getFarm();
    }


    private int farmId() {

        return farm().getId();
    }

    @RequestMapping(value = {"/items"}, method = {RequestMethod.GET})
    private String items() {

        return "inventory/items";
    }

    @RequestMapping(value = {"/vendors"}, method = {RequestMethod.GET})
    private String vendors() {
        return "inventory/vendors";
    }

    @RequestMapping(value = {"/requisition"}, method = {RequestMethod.GET})
    private String requisition() {
        return "inventory/requisition";
    }

    @RequestMapping(value = {"/purchaseOrders"}, method = {RequestMethod.GET})
    private String purchaseOrders() {
        return "inventory/purchaseOrders";
    }

    @RequestMapping(value = {"/bills"}, method = {RequestMethod.GET})
    private String bills() {
        return "inventory/bills";
    }

    @RequestMapping(value = {"/requestPayment"}, method = {RequestMethod.GET})
    private String RequestPayment() {
        return "inventory/paymentRequest";
    }

    @RequestMapping(value = {"/assetManagement"}, method = {RequestMethod.GET})
    private String assetManagement() {
        return "inventory/assetManagement";
    }

    @RequestMapping(value = {"/inventoryAdjustment"}, method = {RequestMethod.GET})
    private String inventoryAdjustment() {
        return "inventory/inventoryAdjustment";
    }

    @RequestMapping(value = {"/issueItems"}, method = {RequestMethod.GET})
    public String goToIssueItems() {
        return "inventory/issueItems";
    }

    @RequestMapping(value = {"/openAddRequisitionFragment"}, method = {RequestMethod.GET})
    public String openAddRequisitionFragment() {
        return "inventory/addRequisition";
    }

    @RequestMapping(value = {"/addPurchaseOrder/{requisitionId}"}, method = {RequestMethod.GET})
    public String openAddPurchaseOrder(@PathVariable Integer requisitionId, Model model) {
        model.addAttribute("requisitionId", requisitionId);
        return "inventory/addPurchaseOrder";
    }

    @RequestMapping(value = {"/receiveItems/{id}"}, method = {RequestMethod.GET})
    public String openReceiveItems(@PathVariable Integer id, Model model) {

        model.addAttribute("poId", id);

        return "inventory/receiveItems";
    }

    @RequestMapping(value = {"/returnItems"}, method = {RequestMethod.GET})
    public String goToReturnItems() {
        return "inventory/returnItems";
    }

    private ResultSet getResults(String query) {

        Connection conn = null;
        try {
            conn = DbConnector.getConnection();
            return conn.prepareStatement(query).executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }/* finally {
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException sQLException) {
                    sQLException.printStackTrace();
                }
        }*/

    }

    @ResponseBody
    @GetMapping(value = "/api/inv/initDashboard", produces = "application/json")
    private String dashboard() {
        JSONObject dashboard = new JSONObject();
        try {

            ResultSet rs = getResults(
                    "SELECT COUNT(*) AS items_held FROM issued_items WHERE `status` = 'Not Returned' and farm = " + farmId() + "");
            while (rs.next()) {
                dashboard.put("items_held", rs.getString("items_held"));
            }

            rs = getResults("SELECT COUNT(*) AS items_held FROM issued_items WHERE `status` = 'Non Refundable' and farm = " + farmId() + "");
            while (rs.next()) {
                dashboard.put("non_refundable", rs.getString("items_held"));
            }
            rs = getResults("SELECT SUM(amount) AS total_amount FROM item_stock where farm = " + farmId() + "");
            while (rs.next()) {
                dashboard.put("total_amount", rs.getString("total_amount"));
            }
            rs = getResults(
                    "SELECT  ((SELECT SUM(item_quantity) FROM purchase_order_items where farm = " + farmId() + ") - (SELECT SUM(quantity_received) from purchase_order_receives where farm = " + farmId() + ")) h");
            while (rs.next()) {
                dashboard.put("to_be_received", rs.getString("h"));
                dashboard.put("to_be_delivered", rs.getString("h"));
            }
            rs = getResults("SELECT COUNT(*) as item_groups FROM item_group where farm = " + farmId() + "");
            while (rs.next()) {
                dashboard.put("all_item_groups", rs.getString("item_groups"));
            }
            rs = getResults("SELECT COUNT(*) AS counts FROM purchase_order_receives WHERE `status` = 'Not Paid' and farm = " + farmId() + "");
            while (rs.next()) {
                dashboard.put("to_be_invoiced", rs.getString("counts"));
            }
            rs = getResults("SELECT COUNT(*) as items FROM items  where farm = " + farmId() + "");
            while (rs.next()) {
                dashboard.put("all_items", rs.getString("items"));
            }
            rs = getResults(
                    "SELECT  COUNT(CASE WHEN reoder_status = 'Low Stock' then 1 ELSE NULL END) AS reoder_status FROM (SELECT items.id,item_name,item_group,reoder_level,image,SUM(amount) AS units,case when reoder_level>=SUM(amount) then 'Low Stock' ELSE 'Not Low' END AS reoder_status  FROM items LEFT JOIN item_stock ON item_id = items.id where items.farm = " + farmId() + " GROUP BY items.id order by id) c ");
            while (rs.next()) {
                dashboard.put("reorder_status", rs.getString("reoder_status"));
            }
            rs = getResults(
                    "SELECT item_quantity, (item_price * item_quantity) AS total, day(created_on) AS day FROM purchase_order_items INNER JOIN purchase_order ON purchase_order.id = purchase_order_id WHERE month(created_on) = MONTH(NOW()) AND  year(created_on) = year(NOW()) AND `status`!='Cancelled' AND purchase_order_items.farm = " + farmId() + " ");
            JSONArray monthArray = new JSONArray();
            while (rs.next()) {
                JSONObject monthOb = new JSONObject();
                monthOb.put("day", rs.getString("day"));
                monthOb.put("item_quantity", rs.getDouble("item_quantity"));
                monthOb.put("total", rs.getDouble("total"));
                monthArray.add(monthOb);
            }
            dashboard.put("this_month_purchase_orders", monthArray);
            rs = getResults(
                    "SELECT item_quantity, (item_price * item_quantity) AS total, month(created_on) AS month FROM purchase_order_items INNER JOIN purchase_order ON purchase_order.id = purchase_order_id WHERE year(created_on) = year(NOW()) AND `status`!='Cancelled' and purchase_order_items.farm = " + farmId() + " ");
            JSONArray yearArray = new JSONArray();
            while (rs.next()) {
                JSONObject yearOb = new JSONObject();
                yearOb.put("month", rs.getString("month"));
                yearOb.put("item_quantity", rs.getDouble("item_quantity"));
                yearOb.put("total", rs.getDouble("total"));
                yearArray.add(yearOb);
            }
            dashboard.put("this_year_purchase_orders", yearArray);

            rs = getResults(
                    "SELECT COUNT(*) as counts,item_name,image FROM issued_items INNER JOIN items ON items.id = issued_items.item_id WHERE issued_items.farm = " + farmId() + " GROUP BY item_id ORDER BY COUNT(*) DESC LIMIT 5");

            JSONArray itemsArray = new JSONArray();
            while (rs.next()) {
                JSONObject item = new JSONObject();
                item.put("item_name", rs.getString("item_name"));
                item.put("counts", "Issued " + rs.getString("counts") + " times");
                item.put("image", rs.getString("image"));

                itemsArray.add(item);
                dashboard.put("fastest_moving_items", itemsArray);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dashboard.toJSONString();
    }

    @ResponseBody
    @GetMapping("/api/inv/getAllItems")
    private List<ItemsModel> allItems() throws SQLException {
        List<ItemsModel> itemslist = new ArrayList<>();
        String sql = "SELECT items.id as code,item_name,item_group,reoder_level,image,concat(ifnull(SUM(amount),0),' ',uom) AS units FROM items LEFT\n" +
                "    JOIN item_stock ON item_id = items.id where items.farm = " + farmId() + " GROUP BY items.id order by items.id desc";
        Connection conn = DbConnector.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            int itemId = rs.getInt("code");
            String itemName = rs.getString("item_name");
            String itemGroup = rs.getString("item_group");
            int reorderLevel = rs.getInt("reoder_level");
            String image = rs.getString("image");
            String units = rs.getString("units");
            String itemsStatus;
            if (rs.getDouble("reoder_level") >= Double.valueOf(rs.getString("units").split(" ")[0])) {
                itemsStatus = "Low on Stock";
            } else {
                itemsStatus = "Sufficient Stock";

            }
            ItemsModel itemsModel = new ItemsModel(itemId, itemName, itemGroup, reorderLevel, image, units, itemsStatus);
            itemslist.add(itemsModel);
        }
        rs.close();
        conn.close();
        return itemslist;
    }

    @ResponseBody
    @GetMapping(value = "/api/inv/getApprovedRequisition/{id}", produces = "application/json")
    private List<ItemDataModel> getApprovedRequisition(@PathVariable String id) throws SQLException {
        List<ItemDataModel> itemslist = new ArrayList<>();
        String sql = "SELECT * FROM requisition_items INNER JOIN items ON items.id = item_id WHERE requisition_id = " + id + " and requisition_items.farm = " + farmId() + "";
        Connection conn = DbConnector.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            String image = rs.getString("image");
            String itemName = rs.getString("item_name");
            int itemId = rs.getInt("item_id");
            double price = rs.getDouble("price");
            String quantity = rs.getString("quantity");
            String uom = rs.getString("uom");
            double total = rs.getDouble("price") * rs.getDouble("quantity");
            String details = rs.getString("details");

            ItemDataModel itemsModel = new ItemDataModel(Integer.parseInt(id), image, itemId, itemName, price, quantity, uom, total, details);
            itemslist.add(itemsModel);
        }
        rs.close();
        conn.close();
        return itemslist;
    }

    @ResponseBody
    @GetMapping("/api/inv/getPendingPO/{id}")
    private List<PendingPO> getPendingPO(@PathVariable String id) throws SQLException {
        List<PendingPO> itemslist = new ArrayList<>();
        String sql =
                "SELECT purchase_order_items.id, purchase_order_items.purchase_order_id, item_id,item_name,purchase_order_items.item_quantity,ifnull(sum(quantity_received),0) AS quantity_received ,"
                        + "purchase_order_items.item_quantity-ifnull(sum(quantity_received),0) AS outstanding,ifnull(purchase_order_receives.comments,'') AS comments,uom,"
                        + "expected_date,company  FROM purchase_order_items LEFT JOIN purchase_order_receives ON purchase_order_items.id = purchase_order_item_id INNER "
                        + "JOIN items ON items.id = item_id  INNER JOIN purchase_order ON purchase_order.id = purchase_order_items.purchase_order_id INNER JOIN vendors ON "
                        + "vendors.id = purchase_order.vendor_id   WHERE purchase_order_items.purchase_order_id = "
                        + id + " GROUP BY purchase_order_items.id";
        System.out.println(sql);
        Connection conn = DbConnector.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            int poItemsId = rs.getInt("id");
            int poid = rs.getInt("purchase_order_id");
            String company = rs.getString("company");
            String expectedDate = rs.getString("expected_date");
            int itemId = rs.getInt("item_id");
            String itemName = rs.getString("item_name");
            String quantity = new DecimalFormat("#,###.##").format(rs.getDouble("item_quantity"));
            String quantityReceived = new DecimalFormat("#,###.##").format(rs.getDouble("quantity_received")) + " "
                    + rs.getString("uom");
            String outstanding = new DecimalFormat("#,###.##").format(rs.getDouble("outstanding")) + " " + rs.getString("uom");

            PendingPO pendingPO = new PendingPO(poItemsId, poid, company, expectedDate, itemId, itemName, quantity, quantityReceived, outstanding);
            itemslist.add(pendingPO);
        }
        rs.close();
        conn.close();
        return itemslist;
    }


    @ResponseBody
    @GetMapping(value = "/api/inv/getAllVendors", produces = "application/json")
    private List<VendorsData> allVendors() {
        List<VendorsData> itemslist = new ArrayList<>();
        ResultSet rs = getResults(
                "SELECT id,contact_name,company,phone,email,item_group FROM vendors WHERE farm = " + farmId() + " ORDER BY id desc");
        try {
            while (rs.next()) {
                double payables = 0.0;
                double receivables = 0;
                ResultSet rs2 = getResults(
                        "SELECT SUM(item_price*item_quantity) AS totals FROM purchase_order_receives INNER JOIN purchase_order_items ON purchase_order_item_id = "
                                + "purchase_order_items.id  INNER JOIN purchase_order ON purchase_order.id = purchase_order_id WHERE purchase_order_receives.farm = " + farmId() + " AND purchase_order_receives.`status` = 'Not Paid' and vendor_id = '"
                                + rs.getInt("id") + "'");
                while (rs2.next()) {
                    payables = rs2.getDouble("totals");
                }
                rs2 = getResults("SELECT SUM(outstanding*item_price) AS totals FROM  (SELECT purchase_order_items.id,item_name,purchase_order_items.item_price,purchase_order_items.item_quantity,ifnull(sum(quantity_received),0) AS quantity_received , \r\n"
                        + "							   purchase_order_items.item_quantity-ifnull(quantity_received,0) AS outstanding,ifnull(purchase_order_receives.comments,'') AS comments,uom, \r\n"
                        + "							   expected_date,company  FROM purchase_order_items LEFT JOIN purchase_order_receives ON purchase_order_items.id = purchase_order_item_id INNER  \r\n"
                        + "							   JOIN items ON items.id = item_id  INNER JOIN purchase_order ON purchase_order.id = purchase_order_items.purchase_order_id INNER JOIN vendors ON  \r\n"
                        + "							   vendors.id = purchase_order.vendor_id   WHERE vendors.id =  \r\n"
                        + "							  '" + rs.getInt("id") + "'   GROUP BY purchase_order_items.id )h");
                while (rs2.next()) {
                    receivables = rs2.getDouble("totals");
                }
                itemslist.add(new VendorsData(rs.getString("contact_name"), rs.getString("item_group"),
                        new DecimalFormat("#,###.##").format(receivables), rs.getInt("id"), rs.getString("company"),
                        rs.getString("email"), rs.getString("phone"), new DecimalFormat("#,###.##").format(payables),
                        null));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return itemslist;
    }

    @ResponseBody
    @GetMapping("/api/inv/getItemStock")
    private List<ItemStock> itemsStock() {

        return itemStockRepository.findByFarm(farmId());
    }

    @ResponseBody
    @GetMapping(value = "/api/inv/getAllAdjustments", produces = "application/json")
    private List<ItemAdjustment> allAdjustments() {

        return itemAdjustmentRepository.findByFarm(farmId());
    }

    @ResponseBody
    @GetMapping("/api/inv/getAllRequisitions")
    private List<RequisitionData> allRequisitions() throws SQLException {
        String sql = "SELECT requisition.id,department,requested_by,DATE(requested_on) AS requested_on,requisition.status, SUM(price*quantity) AS cost,\n" +
                "\t\t\t\t\t\tCOUNT(requisition_items.id) AS items from requisition  INNER JOIN requisition_items ON requisition.id = requisition_items.requisition_id\n" +
                "\t\t\t\t\t\t GROUP BY requisition.id ORDER BY requisition.id desc";
        Connection conn = DbConnector.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        List<RequisitionData> requisitionDataList = new ArrayList<>();

        while (rs.next()) {
            int id = rs.getInt("id");
            String department = rs.getString("department");
            String requestedBy = rs.getString("requested_by");
            Date requestedOn = rs.getDate("requested_on");
            String status = rs.getString("status");
            double cost = rs.getDouble("cost");
            int items = rs.getInt("items");

            RequisitionData requisitionData = new RequisitionData(id, department, requestedBy, requestedOn, status, cost, items);
            requisitionDataList.add(requisitionData);
        }
        rs.close();
        conn.close();
        return requisitionDataList;
    }

    @ResponseBody
    @GetMapping(value = "/api/inv/getAllPurchaseOrders", produces = "application/json")
    private List<PurchaseOrderData> allPurchaseOrders() throws SQLException {
        String sql = "SELECT purchase_order.id,DATE(created_on) AS created_on,expected_date,COUNT(item_id)AS items,SUM(item_price*item_quantity) AS cost, purchase_order.`status`,comments\n" +
                "\t\t\t\t\t\tFROM purchase_order LEFT JOIN purchase_order_items ON purchase_order.id = purchase_order_items.purchase_order_id where purchase_order.farm = " + farmId() + " GROUP BY purchase_order.id  ORDER BY id desc";
        Connection conn = DbConnector.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        List<PurchaseOrderData> purchaseOrderDataList = new ArrayList<>();

        while (rs.next()) {
            int id = rs.getInt("id");
            Date createdOn = rs.getDate("created_on");
            Date expectedDate = rs.getDate("expected_date");
            int items = rs.getInt("items");
            double cost = rs.getDouble("cost");
            String status = rs.getString("status");
            String comments = rs.getString("comments");

            PurchaseOrderData purchaseOrderData = new PurchaseOrderData();
            purchaseOrderData.setId(id);
            purchaseOrderData.setCreatedOn(createdOn);
            purchaseOrderData.setExpectedDate(expectedDate);
            purchaseOrderData.setItems(items);
            purchaseOrderData.setCost(cost);
            purchaseOrderData.setStatus(status);
            purchaseOrderData.setComments(comments);

            purchaseOrderDataList.add(purchaseOrderData);
        }
        rs.close();
        conn.close();
        return purchaseOrderDataList;
    }

    @ResponseBody
    @GetMapping("/api/inv/getAllItemGroups")
    private List<ItemGroup> allItemGroups() {

        return itemGroupRepository.findByFarm(farmId());
    }

    @ResponseBody
    @GetMapping("/api/inv/getAllAssets")
    private List<Assets> getAllAssets() {
        return assetsRepository.findByFarm(farm());
    }

    @ResponseBody
    @GetMapping("/api/inv/getAllBills")
    private List<PurchaseBillModel> getAllBills() throws SQLException {
        List<PurchaseBillModel> itemslist = new ArrayList<>();
        String sql = "SELECT billid,\n" +
                "       billno,\n" +
                "       IFNULL(vendors.`company`, suplier)              AS 'name',\n" +
                "       phone,\n" +
                "       items.item_name                                 AS Item,\n" +
                "       format(amt * qty, 2)                            AS amt,\n" +
                "       format(ifnull(SUM(credit), 0), 2)               AS paid,\n" +
                "       FORMAT((amt * qty) - ifnull(SUM(credit), 0), 2) AS pending,\n" +
                "       purchasesbills.billdate\n" +
                "FROM purchasesbills\n" +
                "         INNER JOIN items ON items.id = item_code\n" +
                "         LEFT JOIN vendors ON vendors.id = suplier\n" +
                "         LEFT JOIN\n" +
                "     accounttransactions ON accounttransactions.`transaction id` = billno\n" +
                "where purchasesbills.farm = '"+farmId()+"'\n" +
                "GROUP BY billid\n" +
                "order by billid desc;";
        Connection conn = DbConnector.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            int billId = rs.getInt("billid");
            String billNo = rs.getString("billno");
            String name = rs.getString("name");
            String phone = rs.getString("phone");
            String item = rs.getString("Item");
            String amt = rs.getString("amt");
            String paid = rs.getString("paid");
            String pending = rs.getString("pending");
            String billDate = rs.getString("billdate");

            String status = "";
            if (pending.equals("0.00")) {
                status = "Paid";
            }

            if (!pending.equals("0.00") && !amt.equals(paid)) {
                status = "Partially Paid";
            }
            if (paid.equals("0.00")) {
                status = "Not Paid";
            }

            // Create an Item object with the retrieved values
            PurchaseBillModel itemObj = new PurchaseBillModel(billId, billNo, name, phone, item, amt, paid, pending, billDate, status);
            itemslist.add(itemObj);
        }
        rs.close();
        conn.close();
        return itemslist;
    }

    @ResponseBody
    @GetMapping("/api/inv/getAllPaymentRequests")
    private List<PaymentRequest> getAllPaymentRequests() {
        return paymentRequestRepository.findByFarm(farmId());
    }

    @PostMapping("/api/inv/searchItems")
    @ResponseBody
    public List<Item> searchItems(@RequestParam("query") String query) {
        // TODO: 30/10/2023 return search function
        System.out.println(query);
        List<Item> items = itemRepository.findByTitleContainingIgnoreCase(query, farmId());
        return items;
    }

    @GetMapping("/api/inv/getItem/{itemId}")
    @ResponseBody
    public Optional<Item> getItem(@PathVariable("itemId") String id) {

        return itemRepository.findByIdAndFarm(Integer.valueOf(id), farmId());
    }

    /* --------------Add Methods --------------*/

    @PostMapping(value = "/api/inv/addItemGroup", produces = "application/json")
    @ResponseBody
    private String addItemGroup(@RequestParam String groupName) {
        ItemGroup itemGroup = new ItemGroup();
        itemGroup.setGroupName(groupName);
        itemGroup.setfarm(farmId());
        JSONObject jsonObject = new JSONObject();

        try {
            itemGroupRepository.save(itemGroup);
            jsonObject.put("response", "Group saved successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toJSONString();
    }

    @PostMapping(value = "/api/inv/addPaymentRequest", produces = "application/json")
    @ResponseBody
    private String addPaymentRequest(@RequestParam String amount, String description) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(Double.valueOf(amount));
        paymentRequest.setDetails(description);
        paymentRequest.setfarm(farmId());
        paymentRequest.setDisbursedBy("");
        paymentRequest.setCreatedBy(String.valueOf(userId()));
        paymentRequest.setCreatedOn(Instant.now());
        paymentRequest.setReviewedBy("");
        paymentRequest.setStatus("Pending");
        JSONObject jsonObject = new JSONObject();

        try {
            paymentRequestRepository.save(paymentRequest);
            jsonObject.put("response", "Payment requested successfully");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("response", "an error occurred");
        }
        return jsonObject.toJSONString();
    }

    @PostMapping(value = "/api/inv/addItem", produces = "application/json")
    @ResponseBody
    private String addItem(@RequestParam String itemName, double itemPrice, String itemCategory, String sku, String uom, String returnable, String itemGroup, int openingStock, int reorderLevel, String preferredVendor, String imageUrl) {
        String response = "";
        Item item = new Item();
        item.setfarm(farmId());
        item.setItemCategory(itemCategory);
        item.setItemGroup(itemGroup);
        item.setItemName(itemName);
        item.setImage(imageUrl);
        item.setUom(uom);
        item.setItemPrice(itemPrice);
        item.setReoderLevel(reorderLevel);
        item.setPreferredVendor(Integer.valueOf(preferredVendor));
        System.out.println(returnable);
        if (returnable != null && returnable.equalsIgnoreCase("on")) {
            item.setReturnable(1);
        } else {
            item.setReturnable(0);
        }
        item.setCreateDate(Instant.now());
        item.setPreferredVendor(0);


        try {
            itemRepository.save(item);
            ItemStock itemStock = new ItemStock();
            itemStock.setAmount(openingStock);
            itemStock.setDescription("Opening Stock");
            itemStock.setItemId(item.getId());
            itemStock.setRegDate(Instant.now());
            itemStock.setfarm(farmId());
            itemStockRepository.save(itemStock);
            response = "New item created successfully";
        } catch (Exception e) {
            e.printStackTrace();
            response = "Item failed to create";
        }
        JSONObject object = new JSONObject();
        object.put("response", response);
        return object.toJSONString();
    }

    @PostMapping(value = "/api/inv/addVendor", produces = "application/json")
    @ResponseBody
    private String addVendor(@RequestParam String name, String company, String openingBal, String kraPin, String itemGroup, String phone, String email, String address) {
        String response = "";
        Vendor vendor = new Vendor();
        vendor.setAddress(address);
        vendor.setItemGroup(itemGroup);
        vendor.setfarm(farmId());
        vendor.setCompany(company);
        vendor.setPhone(phone);
        vendor.setEmail(email);
        vendor.setRegDate(Instant.now());
        vendor.setKraPin(kraPin);
        vendor.setContactName(name);

        System.out.println(vendor);
        try {
            vendorRepository.save(vendor);
            response = "New vendor created successfully";
        } catch (Exception e) {
            e.printStackTrace();
            response = "Vendor failed to create";
        }
        JSONObject object = new JSONObject();
        object.put("response", response);
        return object.toJSONString();
    }

    @PostMapping("/api/inv/addAsset")
    @ResponseBody
    private String addAsset(@RequestParam String name, String serial, String type, String status, String deprecationMethod, double rate, String period, String usefulLife, String description, String location, String usedBy, Double openingBalance, String balanceAt) {
        String response = "";
        Assets assets = new Assets();
        assets.setName(name);
        assets.setSerial(serial);
        assets.setType(type);
        assets.setStatus(status);
        assets.setDepreciation(deprecationMethod);
        assets.setRate(rate);
        assets.setPeriod(period);
        assets.setAssetLife(usefulLife);
        assets.setDescription(description);
        assets.setFarm(farm());
        assets.setLocation(location);
        assets.setUsedBy(usedBy);
        assets.setOpeningBalance(openingBalance);
        assets.setBalanceAsAt(LocalDate.parse(balanceAt));

        try {
            assetsRepository.save(assets);
            response = "New asset created successfully";
        } catch (Exception e) {
            e.printStackTrace();
            response = "Asset failed to create";
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", response);
        return jsonObject.toJSONString();
    }

    @PostMapping("/api/inv/addAdjustment")
    @ResponseBody
    private String addAdjustment(@RequestParam String adjustmentType, String item, String date, String account, String reason, String description, double quantity, double newQuantity, double adjusted) {
        String response = "";
        ItemAdjustment itemAdjustment = new ItemAdjustment();
        itemAdjustment.setfarm(farmId());
        itemAdjustment.setAdjustmentType(adjustmentType);
        itemAdjustment.setItemId(Integer.valueOf(item));
        itemAdjustment.setDate(LocalDate.parse(date).atStartOfDay(ZoneOffset.UTC).toInstant());
        itemAdjustment.setAccount(account);
        itemAdjustment.setReason(reason);
        itemAdjustment.setDescription(description);
        itemAdjustment.setInitialValue(quantity);
        itemAdjustment.setAdjustedValue(newQuantity);
        itemAdjustment.setAdjustedBy("root");

        try {
            itemAdjustmentRepository.save(itemAdjustment);
            response = "Item adjusted successfully";
        } catch (Exception e) {
            e.printStackTrace();
            response = "Item failed to adjust";
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", response);
        return jsonObject.toJSONString();
    }

    @PostMapping("/api/inv/addRequisition")
    @ResponseBody
    private String addRequisition(@RequestBody String jsonString) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        String itemId, quantity, price, details, department, requestedBy;

        JSONObject response = new JSONObject();
        System.out.println(jsonNode);

        department = jsonNode.get("department").asText();
        requestedBy = jsonNode.get("requestedBy").asText();
        try {
            Connection connection = DbConnector.getConnection();
            String insert = "INSERT INTO requisition( `department`, `requested_by`, `farm`, requested_on, status) values ('" + department
                    + "','" + requestedBy + "','" + farmId() + "', CURRENT_TIMESTAMP, 'Pending')";
            PreparedStatement preparedStatement = connection.prepareStatement(insert);
            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                JsonNode itemsArray = jsonNode.get("items");
                for (JsonNode item : itemsArray) {
                    itemId = item.get("itemId").asText();
                    price = item.get("price").asText();
                    quantity = item.get("quantity").asText();
                    details = item.get("details").asText();


                    String insertItems = "INSERT INTO requisition_items(`requisition_id`,`item_id`,`quantity`,`price`, `details`, `farm`, status, removal_reason) select id,'"
                            + itemId + "','" + quantity + "','" + price + "','" + details + "','" + farmId()
                            + "', '', '' from requisition where farm = " + farmId() + " order by id desc limit 1";
                    System.out.println(insertItems);
                    preparedStatement = connection.prepareStatement(insertItems);
                    int insertedRows = preparedStatement.executeUpdate();
                    if (insertedRows > 0) {
                        response.put("response", "Requisition made successfully");
                    }
                }
            }
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.put("response", "Requisition failed");
        }

        return response.toJSONString();
    }

    @PostMapping("/api/inv/addPurchaseOrder")
    @ResponseBody
    private String addPurchaseOrder(@RequestBody String jsonString) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        String requisition_id, quantity, price, vendor, expectedOn, comments, itemName;

        JSONObject response = new JSONObject();
        System.out.println(jsonNode);

        vendor = jsonNode.get("vendor").asText();
        expectedOn = jsonNode.get("expectedOn").asText();
        comments = jsonNode.get("comments").asText();
        JsonNode itemsArray = jsonNode.get("items");
        itemName = itemsArray.get(0).get("itemName").asText();
        int itemId = itemsArray.get(0).get("itemId").asInt();
        requisition_id = itemsArray.get(0).get("requisition_id").asText();
        try {
            Connection connection = DbConnector.getConnection();
            String insert = "INSERT INTO purchase_order(`created_by`, `comments`, `expected_date`, created_on, requisition_id, vendor_id, status, farm) VALUES ('root', '" + comments + "', '" + expectedOn + "', CURRENT_TIMESTAMP, " + requisition_id + ", " + vendor + ", 'Not Received', '" + farmId() + "')";
            PreparedStatement preparedStatement = connection.prepareStatement(insert);
            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                for (JsonNode item : itemsArray) {
                    price = item.get("price").asText();
                    quantity = item.get("quantity").asText();

                    String insertItems = "INSERT INTO purchase_order_items(`purchase_order_id`,`item_id`,`item_price`,`item_quantity`, `farm`) select id,'"
                            + itemId + "','" + price.replace(",", "") + "','" + quantity + "','" + farmId()
                            + "' from purchase_order where `farm` = " + farmId() + " order by id desc limit 1";
                    preparedStatement = connection.prepareStatement(insertItems);
                    int insertedRows = preparedStatement.executeUpdate();
                    if (insertedRows > 0) {
                        String updateRequisition = "Update requisition set status = 'Ordered' where id = " + requisition_id;
                        preparedStatement = connection.prepareStatement(updateRequisition);
                        int updated = preparedStatement.executeUpdate();
                        if (updated > 0) {
                            response.put("response", "Purchase order made successfully");
                        }
                    }
                }
            }
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.put("response", "Purchase order failed");
        }
        return response.toJSONString();
    }

    @PostMapping("/api/inv/issueItem")
    @ResponseBody
    private String issueItem(@RequestBody String jsonString) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        String imageUrl, title, itemId, quantity, date;

        JSONObject response = new JSONObject();
        System.out.println(jsonNode);

        String recipient = jsonNode.get("recipient").asText();
        JsonNode booksArray = jsonNode.get("items");
        for (JsonNode item : booksArray) {
            itemId = item.get("itemId").asText();
            date = item.get("date").asText();
            quantity = item.get("quantity").asText();
            IssuedItem issuedItem = new IssuedItem();
            issuedItem.setItemId(Integer.valueOf(itemId));
            issuedItem.setIndividualId(recipient);
            issuedItem.setIndividualCategory("member");
            if (itemRepository.findByIdAndFarm(Integer.valueOf(itemId), farmId()).get().getReturnable() == 1) {
                issuedItem.setReturnDate(LocalDate.parse(date));
            }
            issuedItem.setIssuedBy("root");
            issuedItem.setIssuedOn(Instant.now());
            issuedItem.setfarm(farmId());
            issuedItem.setQuantity(Double.valueOf(quantity));

            try {
                Item itemDb = getItem(itemId).get();
                if (itemDb.getReturnable() == 0) {
                    ItemStock itemStock = new ItemStock();
                    itemStock.setItemId(Integer.valueOf(itemId));
                    itemStock.setfarm(farmId());
                    itemStock.setRegDate(Instant.now());
                    itemStock.setAmount(-Integer.parseInt(quantity));
                    String description = "Stock issued to " + recipient + " on " + LocalDate.now() + "";
                    itemStock.setDescription(description);
                    itemStockRepository.save(itemStock);
                    issuedItem.setStatus("Non Refundable");
                } else {
                    issuedItem.setStatus("Not Returned");
                }
                issuedItemRepository.save(issuedItem);
                response.put("response", "Item(s) Issued Successfully");
            } catch (Exception e) {
                e.printStackTrace();
                response.put("response", "An error occurred issuing the item");
            }
        }

        return response.toJSONString();
    }

    @GetMapping("/api/inv/getIssuedItems")
    @ResponseBody
    public List<Object[]> getIssuedItems() {

        return issuedItemRepository.findAllIssuedItems(farmId());
    }

    @PostMapping("/api/inv/returnIssuedItem")
    @ResponseBody
    private String returnIssuedItem(@RequestBody String jsonString) throws JsonProcessingException {
        String response = "";

        System.out.println(jsonString);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        String comment = jsonNode.get("comment").asText();
        //String quantity = jsonNode.get("quantity").asText();
        JsonNode selectedIssuedItem = jsonNode.get("selectedIssuedItem");
        int id = Integer.parseInt(selectedIssuedItem.get("id").asText());
        try {
            IssuedItem issuedItem = issuedItemRepository.findByItemIdAndStatusAndFarm(id, "Not Returned", farmId());
            issuedItem.setStatus("Returned");
            issuedItem.setReturnedOn(LocalDate.now());
            issuedItem.setComments(comment);
            issuedItem.setReturnedBy("Root");

            issuedItemRepository.save(issuedItem);
            response = "Item returned successfully";
        } catch (Exception e) {
            e.printStackTrace();
            response = "Issue returning item";
        }
        JSONObject responseObject = new JSONObject();
        responseObject.put("response", response);

        return responseObject.toJSONString();
    }

    /* ----*****-----Add Methods -----*****----*/

    /* ----*****-----Delete Methods -----*****----*/

    @GetMapping(value = "api/inv/deleteItem/{id}", produces = "application/json")
    @ResponseBody
    public String deleteItem(@PathVariable("id") Integer id) {
        String response = "";
        try {
            itemRepository.deleteById(id);
            response = "Item deleted successfully";
        } catch (Exception e) {
            e.printStackTrace();
            response = "Item failed to delete";
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", response);
        return jsonObject.toJSONString();
    }

    @GetMapping(value = "api/inv/deleteAsset/{id}", produces = "application/json")
    @ResponseBody
    public String deleteAsset(@PathVariable("id") Integer id) {
        String response = "";
        try {
            assetsRepository.deleteById(id);
            response = "Asset deleted successfully";
        } catch (Exception e) {
            e.printStackTrace();
            response = "Asset failed to delete";
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", response);
        return jsonObject.toJSONString();
    }

    @PostMapping(value = "api/inv/depreciateAll", produces = "application/json")
    @ResponseBody
    public String depreciateAll() {
        try {
            Connection conn = DbConnector.getConnection();
            ResultSet rs = conn.prepareStatement("SELECT id, rate, depreciation, period, asset_life FROM assets where farm = " + farmId()).executeQuery();
            StringBuilder responseBuilder = new StringBuilder();

            while (rs.next()) {
                int assetId = rs.getInt("id");
                ResultSet rs2 = conn.prepareStatement(
                        "SELECT *, DATE_ADD(depreciate_from, INTERVAL periods MONTH) AS next_depreciation FROM (SELECT CASE WHEN asset_depreciation.date IS NULL THEN TIMESTAMPDIFF(MONTH, balance_as_at, DATE(NOW())) ELSE TIMESTAMPDIFF(MONTH, asset_depreciation.date, DATE(NOW())) END AS last_depreciation, CASE WHEN period='Yearly' THEN 12 WHEN period='Half-Yearly' THEN 6 WHEN period='Quarterly' THEN 3 WHEN period='Monthly' THEN 1 END AS periods, CASE WHEN asset_depreciation.date IS NULL THEN balance_as_at ELSE MAX(asset_depreciation.date) END AS depreciate_from, opening_balance - IFNULL(SUM(amount), 0) AS book_value, asset_life - SUM(CASE WHEN DATE IS NULL THEN 0 ELSE 1 END) AS remaining_life FROM assets LEFT JOIN asset_depreciation ON assets.id = asset_depreciation.asset_id WHERE assets.farm = " + farmId() + " AND assets.id = "
                                + assetId + " ORDER BY asset_depreciation.id DESC LIMIT 1) H").executeQuery();

                while (rs2.next()) {
                    int lastDepreciation = rs2.getInt("last_depreciation");
                    int periods = rs2.getInt("periods");

                    if (lastDepreciation >= periods) {
                        String depreciationType = rs.getString("depreciation");
                        double rate = rs.getDouble("rate");
                        double bookValue = rs2.getDouble("book_value");
                        String nextDepreciation = rs2.getString("next_depreciation");
                        String insert = "";

                        System.out.println(depreciationType);
                        if (depreciationType.equals("Straight Line")) {
                            insert = "INSERT INTO asset_depreciation(asset_id, date, amount, farm) VALUES (" + assetId + ", '" + nextDepreciation + "', " + rate + ", " + farmId() + ")";
                            System.out.println(insert);
                        } else if (depreciationType.equals("Reducing Balance")) {
                            double amount = Math.round(rate * bookValue / 100);
                            insert = "INSERT INTO asset_depreciation(asset_id, date, amount, farm) VALUES (" + assetId + ", '" + nextDepreciation + "', " + amount + ", " + farmId() + ")";
                            System.out.println(insert);
                        } else if (depreciationType.equals("Double Declining")) {
                            double amount = Math.round(2 * 1 / rs.getDouble("asset_life") * bookValue);
                            insert = "INSERT INTO asset_depreciation(asset_id, date, amount, farm) VALUES (" + assetId + ", '" + nextDepreciation + "', " + amount + ", " + farmId() + ")";
                            System.out.println(insert);
                        } else if (depreciationType.equals("Sum of Years")) {
                            double remainingLife = rs2.getDouble("remaining_life");
                            double assetLife = rs.getDouble("asset_life");
                            double deprate = Math.round(remainingLife / ((assetLife * (assetLife + 1)) / 2));
                            insert = "INSERT INTO asset_depreciation(asset_id, date, amount, farm) VALUES (" + assetId + ", '" + nextDepreciation + "', " + deprate + ", " + farmId() + ")";
                            System.out.println(insert);
                        }

                        System.out.println(insert);
                        conn.prepareStatement(insert).execute();
                    }
                }
            }

            conn.close();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response", "Depreciation completed");
            return jsonObject.toJSONString();

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception and return an appropriate error response
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("error", "An error occurred");
            return jsonObject.toJSONString();
        }
    }

    @GetMapping(value = "api/inv/rejectRequisition/{id}", produces = "application/json")
    @ResponseBody
    public String rejectRequisition(@PathVariable("id") Integer id) {
        String response = "";
        try {
            Connection connection = DbConnector.getConnection();
            String query = "Update requisition set `status` = 'Rejected' where id = " + id + " and farm = " + farmId() + "";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                response = "Item requisition rejected";
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            response = "Item requisition failed to reject";
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", response);
        return jsonObject.toJSONString();
    }

    @GetMapping(value = "api/inv/cancelPurchaseOrder/{id}", produces = "application/json")
    @ResponseBody
    public String cancelPurchaseOrder(@PathVariable("id") Integer id) {
        String response = "";
        try {
            Connection connection = DbConnector.getConnection();
            String query = "update purchase_order set status = 'Cancelled' where id = '" + id + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                response = "Purchase Order Cancelled";
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            response = "Purchase order failed to cancel";
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", response);
        return jsonObject.toJSONString();
    }

    private String getId(String query) {
        String id = null;
        try (Connection connection = DbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet rs = preparedStatement.executeQuery()) {
            if (rs.next()) {
                id = rs.getString("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    @PostMapping("/api/inv/receiveItems")
    @ResponseBody
    private String receiveItems(@RequestBody String jsonString) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        String poid, poItemsId, quantity, price, vendor, expectedOn, comments, itemName;

        JSONObject response = new JSONObject();
        System.out.println(jsonNode);

        //vendor = jsonNode.get("vendor").asText();
        //expectedOn = jsonNode.get("expectedOn").asText();
        JsonNode itemsArray = jsonNode.get("items");
        try {
            Connection connection = DbConnector.getConnection();
            PreparedStatement preparedStatement = null;

            for (JsonNode item : itemsArray) {
                double outstanding = 0.0;
                itemName = item.get("itemName").asText();
                poid = item.get("poId").asText();
                poItemsId = item.get("poItemsId").asText();
                int itemId = item.get("itemId").asInt();
                String getOut = item.get("outstanding").asText().split(" ")[0];
                outstanding += Double.parseDouble(getOut);
                String items_received = item.get("quantityReceiving").asText();

                if (!items_received.isEmpty()) {
                    String insert = "INSERT INTO purchase_order_receives(id, `purchase_order_item_id`,`quantity_received`,received_by, farm) values(null, ?,?,?,?)";
                    preparedStatement = connection.prepareStatement(insert);
                    preparedStatement.setInt(1, itemId);
                    preparedStatement.setString(2, items_received);
                    preparedStatement.setString(3, "root");
                    preparedStatement.setInt(4, farmId());
                    preparedStatement.executeUpdate();
                    System.out.println(preparedStatement);

                    insert = "INSERT INTO item_stock(amount,description,item_id, farm, reg_date) values (?,?,?,?,?)";
                    preparedStatement = connection.prepareStatement(insert);
                    preparedStatement.setString(1, items_received);
                    preparedStatement.setString(2, "Goods received for purchase order item #" + itemId + "  on " + LocalDate.now());
                    preparedStatement.setInt(3, itemId);
                    preparedStatement.setInt(4, farmId());
                    preparedStatement.setDate(5, java.sql.Date.valueOf(LocalDate.now()));
                    preparedStatement.executeUpdate();
                    System.out.println(preparedStatement);

                    insert = "Insert into purchasesbills(billdate,suplier,duedate,notes,item_code,qty,amt, farm, indentify, account) values(?,?,?,?,?,?,?,?, '', '')";
                    preparedStatement = connection.prepareStatement(insert);
                    preparedStatement.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
                    preparedStatement.setString(2, getId("select vendor_id as id from purchase_order inner join purchase_order_items where purchase_order_items.id = " + poItemsId));
                    preparedStatement.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
                    preparedStatement.setString(4, "");
                    preparedStatement.setInt(5, itemId);
                    preparedStatement.setString(6, items_received);
                    preparedStatement.setString(7, getId("select item_price as id from purchase_order_items where id = " + poItemsId));
                    preparedStatement.setInt(8, farmId());
                    preparedStatement.executeUpdate();
                    System.out.println(preparedStatement);

                    preparedStatement = connection.prepareStatement("update purchasesbills set billno = CONCAT('BILL-',billid) order by billid desc limit 1");
                    System.out.println(preparedStatement);
                    preparedStatement.executeUpdate();
                    System.out.println(preparedStatement);
                }
                System.out.println("outstanding: " + outstanding);
                System.out.println("received: " + Double.parseDouble(items_received));
                if (outstanding == Double.parseDouble(items_received)) {
                    String insert = "update purchase_order set status = 'Fully Received' where id = " + poid + "";
                    preparedStatement = connection.prepareStatement(insert);
                    preparedStatement.executeUpdate();
                    System.out.println(preparedStatement);
                } else if (outstanding > Double.parseDouble(items_received)) {
                    String insert = "update purchase_order set status = 'Partially Received' where id = " + poid + " ";
                    preparedStatement = connection.prepareStatement(insert);
                    preparedStatement.executeUpdate();
                    System.out.println(preparedStatement);
                }
            }

            response.put("response", "Item(s) received successfully");
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("response", "Items receive failed");
        }

        return response.toJSONString();
    }


    @GetMapping(value = "api/inv/approveRequisition/{id}", produces = "application/json")
    @ResponseBody
    public String approveRequisition(@PathVariable("id") Integer id) {
        String response = "";
        try {
            Connection connection = DbConnector.getConnection();
            String query = "Update requisition set `status` = 'Approved' where id = " + id + " and farm = " + farmId() + "";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                response = "Item requisition approved";
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            response = "Item requisition failed to approve";
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", response);
        return jsonObject.toJSONString();
    }


    @GetMapping(value = "api/inv/undoLastDepreciation/{id}", produces = "application/json")
    @ResponseBody
    public String undoLastDepreciation(@PathVariable("id") Integer id) {
        String response = "";
        try {
            if (assetDepreciationRepository.existsById(id)) {
                assetDepreciationRepository.deleteById(id);
                response = "Last depreciation deleted!";
            } else {
                response = "Depreciation record not found!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = "Last depreciation failed to delete!";
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", response);
        return jsonObject.toJSONString();
    }

    @GetMapping(value = "api/inv/deleteVendor/{id}", produces = "application/json")
    @ResponseBody
    public String deleteVendor(@PathVariable("id") Integer id) {
        String response = "";
        try {
            vendorRepository.deleteById(id);
            response = "Item deleted successfully";
        } catch (Exception e) {
            e.printStackTrace();
            response = "Item failed to delete";
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", response);
        return jsonObject.toJSONString();
    }

    /* ----*****-----End Delete Methods -----*****----*/

    /* ----------------Reports ----------------*/

    private JasperPrint generateReport(String query, String jrxmlPath) throws JRException, SQLException {
        Connection conn = null;
        JasperPrint jp = null;

        try {
            conn = DbConnector.getConnection();
            InputStream ip = getClass().getResourceAsStream(jrxmlPath);
            JasperDesign jd = JRXmlLoader.load(ip);
            Map<String, Object> param = new HashMap<>();
            param.put("schid", "" + farmId());
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

    @RequestMapping(path = {"/api/inv/printActivePurchaseOrders"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<byte[]> printActivePurchaseOrders() throws IOException, JRException, SQLException {
        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        String jrxmlPath = active_purchase_orders_path;

        String query = "SELECT purchase_order.id,SUM(item_price*item_quantity) AS totals,COUNT(item_id) AS items,company,DATE(created_on) AS created_on,phone  FROM purchase_order_items inner JOIN purchase_order ON "
                + "purchase_order .id = purchase_order_id JOIN vendors ON vendors.id = vendor_id WHERE purchase_order.status !='Fully Received' AND    purchase_order.status !='Cancelled'  GROUP BY  purchase_order .id";

        JasperPrint jp = generateReport(query, jrxmlPath);

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
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;
    }

    @RequestMapping(path = {"/api/inv/printPurchaseOrderHistory"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<byte[]> printPurchaseOrderHistory() throws IOException, JRException, SQLException {
        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        String jrxmlPath = purchase_orders_history_path;

        String query = "SELECT id, created_on, company, `status`, expected_date, sum(item_quantity) item_quantity, " +
                "sum(quantity_received) quantity_received, SUM(price) AS totals FROM " +
                "( SELECT purchase_order.id, purchase_order.`status`, date(purchase_order.created_on) AS created_on, " +
                "item_name, purchase_order_items.item_quantity, ifnull(sum(quantity_received),0) * purchase_order_items.item_price AS price, " +
                "ifnull(sum(quantity_received),0) AS quantity_received, " +
                "purchase_order_items.item_quantity - ifnull(quantity_received,0) AS outstanding, " +
                "ifnull(purchase_order_receives.comments,'') AS comments, uom, expected_date, company " +
                "FROM purchase_order_items LEFT JOIN purchase_order_receives " +
                "ON purchase_order_items.id = purchase_order_item_id " +
                "INNER JOIN items ON items.id = item_id " +
                "INNER JOIN purchase_order ON purchase_order.id = purchase_order_items.purchase_order_id " +
                "INNER JOIN vendors ON vendors.id = purchase_order.vendor_id WHERE purchase_order_items.farm = " + farmId() + " " +
                "GROUP BY purchase_order_items.id " +
                "ORDER BY purchase_order_items.id DESC ) h " +
                "GROUP BY id " +
                "ORDER BY id DESC";

        JasperPrint jp = generateReport(query, jrxmlPath);

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
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;
    }

    @RequestMapping(path = {"/api/inv/printLowStockItems"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<byte[]> printLowStockItems() throws IOException, JRException, SQLException {
        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        String jrxmlPath = purchase_orders_history_path;

        String query = "SELECT * FROM (SELECT items.id,item_price,item_name,items.item_group,reoder_level,"
                + " image,ifnull(SUM(amount),0) AS units,ifnull(company,'')AS company,"
                + " case when reoder_level>=ifnull(SUM(amount),0) then 'Low Stock' ELSE 'Not Low' END AS reoder_status  "
                + " FROM items LEFT JOIN item_stock ON item_id = items.id "
                + " LEFT JOIN vendors ON vendors.id = items.preferred_vendor GROUP BY items.id order by id) h WHERE "
                + " reoder_status = 'Low Stock' AND item_stock.farm = " + farmId() + "";

        JasperPrint jp = generateReport(query, jrxmlPath);

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
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;
    }

    @RequestMapping(path = {"/api/inv/printPurchaseReceiveHistory"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<byte[]> printPurchaseReceiveHistory() throws IOException, JRException, SQLException {
        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        String jrxmlPath = purchase_receive_history_path;

        String query = "SELECT received_on,item_name,purchase_order_receives.quantity_received AS item_quantity,purchase_order_items.item_price,company,phone,purchase_order_receives.status FROM purchase_order_receives "
                + "INNER JOIN purchase_order_items ON purchase_order_item_id = purchase_order_items.id INNER JOIN purchase_order ON purchase_order_id = purchase_order.id INNER "
                + "JOIN items ON items.id = item_id INNER JOIN vendors ON vendors.id = vendor_id WHERE purchase_order.farm = " + farmId() + " ORDER BY purchase_order_receives.id DESC LIMIT 100";

        JasperPrint jp = generateReport(query, jrxmlPath);

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
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;
    }

    @RequestMapping(path = {"/api/inv/printPurchaseOrder/{id}"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<byte[]> printPurchaseOrder(@PathVariable Integer id) throws IOException, JRException, SQLException {
        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        String jrxmlPath = purchase_orders_path;

        String query = "SELECT purchase_order.id,company,address,DATE(created_on) created_on,expected_date,items.id AS `code`,item_name,comments,item_quantity,"
                + "purchase_order_items.item_price FROM purchase_order_items INNER JOIN purchase_order ON purchase_order_id  = purchase_order.id INNER JOIN vendors ON "
                + "vendor_id = vendors.id INNER JOIN items ON items.id = item_id WHERE purchase_order.id = "
                + id + " and purchase_order.farm = " + farmId() + "";

        JasperPrint jp = generateReport(query, jrxmlPath);

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
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;
    }

    @RequestMapping(path = {"/api/inv/printRequisitionForm/{id}"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<byte[]> printRequisitionForm(@PathVariable Integer id) throws IOException, JRException, SQLException {
        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        String jrxmlPath = requisition_form;

        String query = "SELECT requisition_id,DATE(requested_on) AS requested_on,item_name,details,quantity,price,requested_by FROM requisition_items INNER JOIN requisition ON"
                + " requisition.id = requisition_items.requisition_id INNER JOIN items ON items.id = item_id WHERE requisition_id = "
                + id + " and requisition_items.farm = " + farmId() + "";

        JasperPrint jp = generateReport(query, jrxmlPath);

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
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;
    }

    @RequestMapping(path = {"/api/inv/printInventoryOverdue"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<byte[]> printInventoryOverdue() throws IOException, JRException, SQLException {
        String uploadDir = "static/" + farm().getUploadPath() + "/otheruploads";
        File file = new File(uploadDir + File.separator + RequestContextHolder.currentRequestAttributes().getSessionId() + ".pdf");
        String jrxmlPath = inventory_overdue_path;

        String query = "SELECT item_name,\n" +
                "       individual,\n" +
                "       individual_category,\n" +
                "       DATE(issued_on)                    AS issued_on,\n" +
                "       quantity,\n" +
                "       DATEDIFF(DATE(NOW()), return_date) as overdue_days\n" +
                "FROM (SELECT issued_items.*, item_name\n" +
                "      FROM issued_items\n" +
                "               INNER JOIN items ON items.id = item_id) h\n" +
                "         INNER JOIN (SELECT `id`,\n" +
                "                            CONCAT(`first_name`, ' ', `middle_name`, ' ', `last_name`)\n" +
                "                                AS individual\n" +
                "                     FROM members\n" +
                "                     UNION ALL\n" +
                "                     SELECT `id`, CONCAT(`first_name`, ' ', `middle_name`, ' ', `last_name`)\n" +
                "                     FROM members) g ON h.individual_id = g.`id`\n" +
                "WHERE farm = '"+farmId()+"'\n" +
                "  AND `status` = 'Not returned'\n" +
                "  AND DATE(NOW()) > return_date\n" +
                "ORDER BY return_date;\n";

        JasperPrint jp = generateReport(query, jrxmlPath);

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
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;
    }

    /* ----*****-----Reports -----*****----*/
}
