package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

//HTTP requests to Slack: HttpURLConnection, URL, OutputStream.
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//Specifies character encoding for the HTTP payload.
import java.nio.charset.StandardCharsets;

//JDBC operations: Connection, DriverManager, Statement.
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

//For password generation: Random
import java.util.*;

@Service
public class PasswordRotationService {

    //injecting the properties in variable
    @Value("${snowflake.url}")
    public String url;

    @Value("${snowflake.admin.username}")
    public String adminUsername;

    @Value("${snowflake.admin.password}")
    public String adminPassword;

    @Value("${snowflake.database}")
    public String database;

    @Value("${snowflake.schema}")
    public String schema;

    @Value("${snowflake.target.user}")
    public String targetUser;

    @Value("${slack.webhook.url}")
    public String slackWebhookUrl;

    public String generatePassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public boolean rotatePassword() {
        String newPassword = "Q3(0rah+uQ#57uK4";
        try (Connection conn = DriverManager.getConnection(url, adminUsername, adminPassword)) {
            Statement stmt = conn.createStatement();
            String sql = String.format("ALTER USER %s SET PASSWORD = '%s'", targetUser, newPassword);
            stmt.execute(sql);
            sendToSlack(targetUser, newPassword);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

//    public void addPwd(String newPassword){
//        ArrayList<String> str1= new ArrayList<>();
//
//        for(int i=0;i<3;i++){
//            str1.add(newPassword);
//        }
//    }

    private final Deque<String> lastThreePasswords = new LinkedList<>();
    private boolean isPasswordRepeated(String newPassword) {
        return lastThreePasswords.contains(newPassword);
    }

    private void updatePasswordHistory(String newPassword) {
        if (lastThreePasswords.size() == 3) {
            lastThreePasswords.removeFirst(); // Remove oldest
        }
        lastThreePasswords.addLast(newPassword); // Add new
    }

    public void sendToSlack(String user, String newPassword) throws Exception {
//        String message = String.format("Password for user *%s* has been rotated. New password: `%s`", user, newPassword);
//        String payload = String.format("{\"text\": \"%s\"}", message);
//
//
//        try {
//            URL url = new URL(slackWebhookUrl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//            conn.setRequestMethod("POST");
//            conn.setDoOutput(true);
//            conn.setRequestProperty("Content-Type", "application/json");
//
//            try (OutputStream os = conn.getOutputStream()) {
//                os.write(payload.getBytes(StandardCharsets.UTF_8));
//            }
//
//            int responseCode = conn.getResponseCode();
//            if (responseCode != 200) {
//                System.err.println("Slack notification failed. HTTP response code: " + responseCode);
//            } else {
//                System.out.println("Slack notification sent successfully");
//            }
//
//        } catch (Exception e) {
//            System.err.println("Exception while sending Slack notification: " + e.getMessage());
//            e.printStackTrace();
//        }
        if (isPasswordRepeated(newPassword)) {
            System.err.println("Duplicate password detected. Not sending to Slack.");
            return;
        }

        updatePasswordHistory(newPassword); // Only update if not duplicate

        String message = String.format("Password for user *%s* has been rotated. New password: `%s`", user, newPassword);
        String payload = String.format("{\"text\": \"%s\"}", message);

        try {
            URL url = new URL(slackWebhookUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                System.err.println("Slack notification failed. HTTP response code: " + responseCode);
            } else {
                System.out.println("Slack notification sent successfully");
            }

        } catch (Exception e) {
            System.err.println("Exception while sending Slack notification: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
