package com.rickiey_innovates.juditonspringapp.controllers;

import com.rickiey_innovates.juditonspringapp.DbConnector;
import com.rickiey_innovates.juditonspringapp.models.Farm;
import com.rickiey_innovates.juditonspringapp.models.LoggedInUser;
import com.rickiey_innovates.juditonspringapp.models.User;
import com.rickiey_innovates.juditonspringapp.models.sms.AccModel;
import com.rickiey_innovates.juditonspringapp.repositories.UserRepository;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/api/sms")
public class SmsController {

    private final UserRepository userRepository;


    public SmsController(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    static Calendar cal = Calendar.getInstance();
    static int day = Integer.parseInt(new SimpleDateFormat("dd").format(cal.getTime()));




    private User loggedInUser() {

        return userRepository.findById(userId()).orElse(null);
    }

    private Long userId() {
        return LoginController.getLoggedInUserId();
    }

    private Farm farm() {
        return userRepository.findById(userId()).orElse(null).getFarm();
    }
    @GetMapping("/history")
    @PreAuthorize("hasRole('ADMIN')")
    private ResponseEntity<?> getMessagingHistory() {

        try {

            String sql = "SELECT *\n" +
                    "from (SELECT sms_group                                 AS id,\n" +
                    "             Date_sent,\n" +
                    "             ifnull(CONCAT(id, ' ', name), Recipients) AS Recipients,\n" +
                    "             Message,\n" +
                    "             Action\n" +
                    "      FROM (SELECT sms_group, `Date_sent`, Recipients, Message, '' as Action\n" +
                    "            FROM sms_history s\n" +
                    "            where s.farm = "+ farm().getId()+") a\n" +
                    "               LEFT JOIN member r ON r.id = Recipients) a\n" +
                    "GROUP BY id\n" +
                    "ORDER BY id desc;";

            JSONObject response = AccModel.dbaction(sql, 1, 0, 0, 0);

            return ResponseEntity.ok(response.get("jsondata").toString().replace("\n", "").replace("/", ""));


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/balance")
    @PreAuthorize("hasRole('ADMIN')")
    private ResponseEntity<?> getBalance() throws SQLException {
        try (Connection connection = DbConnector.getConnection()) {

            String sql = "SELECT sms_balance FROM farm WHERE id = 1";
            ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

            double balance = 0.0;
            while (resultSet.next()) {
                balance = resultSet.getDouble("sms_balance");
            }
            return ResponseEntity.ok((new DecimalFormat("#,###.##")).format(balance));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getLocalizedMessage());
        }
    }

    @RequestMapping(value = "/getsearchstudentsempty", method = RequestMethod.GET)
    @ResponseBody
    public String getsearchstudentsempty() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", "");
        jsonObject.put("name", "");

        jsonArray.add(jsonObject);
        return jsonArray.toString();
    }

    @RequestMapping({"/sendthattext"})
    @ResponseBody
    public String sendthattext(@RequestBody JSONObject search, HttpServletRequest request) {

        String response = "";

        List<String> roles = new ArrayList<>();
        roles.add("ROLE_Access_SMS");
        roles.add("ROLE_All");


        String username = "";
        String smsid = "";
        String apiKey = "";
        String sms_account = "";
        int grpmain = 0;
        String groups = search.get("groups").toString().replace("[", "(").replace("]", ")");

        if (groups.length() < 3) {
            groups = search.get("recipients").toString();
        } else {
            String sql = "SELECT IF(0 IN " + groups + ", CONCAT('[', 'All Groups', ']'),\n" +
                    "          CONCAT('[', GROUP_CONCAT(name ORDER BY id), ']')) AS formatted_names\n" +
                    "FROM recipient_group\n" +
                    "WHERE id IN " + groups + ";\n";

            try (Connection connection = DbConnector.getConnection()) {
                ResultSet rs = connection.prepareStatement(sql).executeQuery();

                while (rs.next()) {
                    groups = rs.getString("formatted_names");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        double smsbalance = 0;
        double costpersms = 0;

        Connection conn = null;

        try {
            conn = DbConnector.getConnection();
            ResultSet rt1 = conn.prepareStatement("select sms_balance,\n" +
                    "       cost_per_sms,\n" +
                    "       cost_per_sms,\n" +
                    "       ifnull(MAX(`sms_group`), 0) + 1 AS grp,\n" +
                    "       s.SMSKEY,\n" +
                    "       s.SMSUSERNAME,\n" +
                    "       s.SMSID,\n" +
                    "       s.sms_account\n" +
                    "from farm s\n" +
                    "         left join sms_history h on h.farm = s.id\n" +
                    "where s.id = '" + farm().getId() + "'").executeQuery();
            while (rt1.next()) {
                username = rt1.getString("smsusername");
                smsid = rt1.getString("smsid");
                apiKey = rt1.getString("smskey");
                sms_account = rt1.getString("sms_account");
                smsbalance = rt1.getDouble("sms_balance");
                costpersms = rt1.getDouble("cost_per_sms");
                grpmain = rt1.getInt("grp");
            }


            double totalsms = Double.parseDouble(search.get("totalsms").toString());

            System.out.println(totalsms + "  hvhvjvjv  " + (smsbalance * costpersms));


            if (smsbalance < (totalsms * costpersms)) {
                response = "Insufficient sms balance";
                return "[{\"querystatus\" : \"" + response + "\",\"alredysent\" : \"0\"}]";
            }

            long sentby = userId();

            String textmsg = search.get("sms").toString().trim();

            String[] stringArray = search.get("recipients").toString().replace(" ", "").split(",");

            if (sms_account.equals("HostPinacle")) {
                response = sendHostpinacle(search.get("recipients").toString().replaceAll("\\s+", ""), textmsg);
            } else {
                response = "User doesint have a registered account";
                int alredysent = 0;
                return "[{\"querystatus\" : \"" + response + "\",\"alredysent\" : \"" + alredysent + "\"}]";
            }


        } catch (Exception e1) {
            e1.printStackTrace();
            response = e1.getMessage();
        } finally {
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException sQLException) {
                }
        }

        return response;
    }


    public static String stringdate = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US) + " " +
            day + SettingsController.suffixes[day] + " " +
            new SimpleDateFormat("MMM").format(cal.getTime()) + " " +
            new SimpleDateFormat("yyyy").format(cal.getTime()) + " " +
            new SimpleDateFormat("hh:mm aa").format(cal.getTime());

    public String sendHostpinacle(String phone, String message) throws SQLException {

        double totalcost;

        int messageLength = message.length();
        int oneSmsLength = 160;
        double costpersms = 0.0;
        int smsBalance = 0;


        JSONObject response = null;
        int alredysent = 0;
        String respotext = "";
        JSONParser parser = new JSONParser();

        Connection conn = DbConnector.getConnection();

        try  {

            int grp = 0;
            ResultSet rt1 = conn.prepareStatement("select sms_balance,\n" +
                    "       cost_per_sms,\n" +
                    "       cost_per_sms,\n" +
                    "       ifnull(MAX(`sms_group`), 0) + 1 AS grp,\n" +
                    "       s.SMSKEY,\n" +
                    "       s.SMSUSERNAME,\n" +
                    "       s.SMSID,\n" +
                    "       s.sms_account\n" +
                    "from farm s\n" +
                    "         left join sms_history h on h.farm = s.id\n" +
                    "where s.id = '" + farm().getId() + "'").executeQuery();
            while (rt1.next()) {
                grp = rt1.getInt("grp");
                costpersms = rt1.getDouble("cost_per_sms");
                smsBalance = rt1.getInt("sms_balance");
            }

            int smsCount = (int) Math.ceil((double) messageLength / oneSmsLength);
            totalcost = smsCount * costpersms;


            System.out.println("total cost: " + totalcost);

            if (smsBalance < totalcost) {
                String res  = "Insufficient sms balance";
                return "[{\"querystatus\" : \"" + res + "\",\"alredysent\" : \"0\"}]";
            }


            String ServerDomain = "https://smsportal.hostpinnacle.co.ke/SMSApi/send";
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(ServerDomain);
            List<NameValuePair> params = new ArrayList<NameValuePair>(2);
            params.add(new BasicNameValuePair("userid", farm().getSmsUsername()));
            params.add(new BasicNameValuePair("password", farm().getSmsKey()));
            params.add(new BasicNameValuePair("msg", message));
            params.add(new BasicNameValuePair("type", "text"));
            params.add(new BasicNameValuePair("senderid", farm().getSmsId()));
            params.add(new BasicNameValuePair("msgType", "text"));
            params.add(new BasicNameValuePair("sendMethod", "quick"));
            params.add(new BasicNameValuePair("duplicatecheck", "true"));
            params.add(new BasicNameValuePair("output", "json"));
            params.add(new BasicNameValuePair("mobile", phone));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse respo = httpclient.execute(httppost);
            HttpEntity entity = respo.getEntity();

            if (entity != null) {
                try (InputStream instream = entity.getContent()) {
                    String jsonString = EntityUtils.toString(entity, "utf-8");
                    response = (JSONObject) parser.parse(jsonString);
                }
            }


            ServerDomain = "https://smsportal.hostpinnacle.co.ke/SMSApi/report/status";
            httpclient = HttpClients.createDefault();
            httppost = new HttpPost(ServerDomain);
            params = new ArrayList<NameValuePair>(2);
            params.add(new BasicNameValuePair("userid", "kenmutwiri"));
            params.add(new BasicNameValuePair("password", "nuC1tAk3"));
            params.add(new BasicNameValuePair("uuid", response.get("transactionId").toString()));
            params.add(new BasicNameValuePair("output", "json"));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            respo = httpclient.execute(httppost);
            entity = respo.getEntity();

            if (entity != null) {
                try (InputStream instream = entity.getContent()) {
                    String jsonString = EntityUtils.toString(entity, "utf-8");
                    response = (JSONObject) parser.parse(jsonString);
                }
            }

            System.out.println(response);


            JSONObject first = (JSONObject) response.get("response");
            JSONArray reportStatusList = (JSONArray) first.get("report_statusList");



            conn = DbConnector.getConnection();
            rt1 = conn.prepareStatement("select sms_balance,\n" +
                    "       cost_per_sms,\n" +
                    "       cost_per_sms,\n" +
                    "       ifnull(MAX(`sms_group`), 0) + 1 AS grp,\n" +
                    "       s.SMSKEY,\n" +
                    "       s.SMSUSERNAME,\n" +
                    "       s.SMSID,\n" +
                    "       s.sms_account\n" +
                    "from farm s\n" +
                    "         left join sms_history h on h.farm = s.id\n" +
                    "where s.id = '" + farm().getId() + "'").executeQuery();
            while (rt1.next()) {
                costpersms = rt1.getDouble("cost_per_sms");
            }


            String insertQuery = "INSERT INTO `sms_history`(Recipients, `Message`, `Type`, "
                    + " `Date_sent`, `phones`, `sms_id`, `status`, sent_by, sms_group, farm, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String updateQuery = "UPDATE farm SET sms_balance = ROUND(sms_balance - ?) WHERE id = ?";

            try (PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
                 PreparedStatement updateStatement = conn.prepareStatement(updateQuery)) {

                for (Object statusObject : reportStatusList) {

                    JSONObject status = (JSONObject) statusObject;
                    JSONObject individualStatus = (JSONObject) status.get("status");
                    String statusValue = (String) individualStatus.get("Status");

                    if (statusValue.equals("SUBMITTED") || statusValue.equals("DELIVERED")) {

                        insertStatement.setString(1, "[ALL MEMBERS]");
                        insertStatement.setString(2, message);
                        insertStatement.setString(3, "RECEIPT");
                        insertStatement.setString(4, stringdate);
                        insertStatement.setString(5, individualStatus.get("mobileNo").toString());
                        insertStatement.setString(6, individualStatus.get("msgId").toString());
                        insertStatement.setString(7, individualStatus.get("Status").toString());
                        insertStatement.setLong(8, loggedInUser().getId());
                        insertStatement.setInt(9, grp);
                        insertStatement.setInt(10, farm().getId());
                        insertStatement.setString(11, individualStatus.get("uuId").toString());

                        alredysent++;

                    } else {
                        insertStatement.setString(1, "[ALL MEMBERS]");
                        insertStatement.setString(2, message);
                        insertStatement.setString(3, "RECEIPT");
                        insertStatement.setString(4, stringdate);
                        insertStatement.setString(5, individualStatus.get("mobileNo").toString());
                        insertStatement.setString(6, individualStatus.get("msgId").toString());
                        insertStatement.setString(7, individualStatus.get("Status").toString());
                        insertStatement.setLong(8, loggedInUser().getId());
                        insertStatement.setInt(9, grp);
                        insertStatement.setInt(10, farm().getId());
                        insertStatement.setString(11, individualStatus.get("uuId").toString());
                    }

                    insertStatement.addBatch();
                }

                conn.setAutoCommit(false);
                insertStatement.executeBatch();

                updateStatement.setDouble(1, totalcost);
                updateStatement.setInt(2, farm().getId());
                updateStatement.executeUpdate();
                conn.commit();

                respotext = "Messages sent successfully";
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception as needed
                respotext = e.getLocalizedMessage();
            }

            return "[{\"querystatus\" : \"" + respotext + "\",\"alredysent\" : \"" + alredysent + "\"}]";

        } catch (Exception ex) {
            ex.printStackTrace();
            respotext = "Messages couldnt be sent \n" + ex.getLocalizedMessage();
            return "[{\"querystatus\" : \"" + respotext + "\",\"alredysent\" : \"" + alredysent + "\"}]";
        } finally {
            conn.close();
        }

    }

    @GetMapping("/recipients/{id}")
    public ResponseEntity<?> getRecipients(@PathVariable("id") Integer id) {

        try {

            String sql = "SELECT h.id, m.name, m.phone, h.date_sent, h.status\n" +
                    "FROM sms_history h\n" +
                    "         INNER JOIN member m ON RIGHT(m.phone, 9) = RIGHT(h.phones, 9)\n" +
                    "WHERE sms_group = "+id+" ";

            JSONObject response = AccModel.dbaction(sql, 1, 0, 0, 0);

            return ResponseEntity.ok(response.get("jsondata").toString().replace("\n", "").replace("/", ""));


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/analytics/{id}")
    public ResponseEntity<?> handleAnalytics(@PathVariable("id") Integer id) {
        List<NameValuePair> params;
        JSONObject response = null;
        JSONParser parser = new JSONParser();

        try (Connection connection = DbConnector.getConnection()){
            
            String uuid = "";
            
            String sql = "SELECT `uuid` FROM sms_history WHERE sms_group = "+id+" ";
            ResultSet rs = connection.prepareStatement(sql).executeQuery();
            while (rs.next()) {
                uuid = rs.getString("uuid");
            }
            
            String ServerDomain = "https://smsportal.hostpinnacle.co.ke/SMSApi/report/status";
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(ServerDomain);
            params = new ArrayList<>(2);
            params.add(new BasicNameValuePair("userid", "kenmutwiri"));
            params.add(new BasicNameValuePair("password", "nuC1tAk3"));
            params.add(new BasicNameValuePair("uuid", uuid));
            params.add(new BasicNameValuePair("output", "json"));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse respo = httpclient.execute(httppost);
            HttpEntity entity = respo.getEntity();

            if (entity != null) {
                try (InputStream instream = entity.getContent()) {
                    String jsonString = EntityUtils.toString(entity, "utf-8");
                    response = (JSONObject) parser.parse(jsonString);
                }
            }

            if (response != null) {
                return ResponseEntity.ok()
                        .body(response); // Return the JSON response as a string
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getLocalizedMessage());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{}"); // Return empty JSON object if there's an error or no response
    }


}
