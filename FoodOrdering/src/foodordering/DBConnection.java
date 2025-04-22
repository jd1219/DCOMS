/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodordering;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cleme
 */
public class DBConnection {

    //SINGLETON CLASS
    static DBConnection instance;
    Connection conn;

    private DBConnection() throws SQLException {
        this.conn = DriverManager.getConnection("jdbc:derby://localhost:1527/FoodOrdering", "nyahalo", "nyahalo");
        conn.setAutoCommit(false);
    }

    public synchronized static DBConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }
    //SINGLETON CLASS END

    //USER FUNCTION
    public String[] authenticate(String Id, String password) throws RemoteException, SQLException {
        String[] userData = null;

        String query = "SELECT user_id, account_type FROM USERS WHERE id = ? AND pass = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, Id);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    userData = new String[]{rs.getString("user_id"), rs.getString("account_type")};
                }
            }
        }
        return userData; // Returns null if authentication fails
    }

    public String generateUserId() throws SQLException {
        String sql = "SELECT USER_ID FROM USERS ORDER BY CAST(SUBSTR(USER_ID, 5) AS INT) DESC FETCH FIRST 1 ROWS ONLY";
        PreparedStatement psmt = conn.prepareStatement(sql);
        ResultSet result = psmt.executeQuery();

        if (result.next()) {
            String latestUserId = result.getString("USER_ID");
            StringBuilder sb = new StringBuilder(latestUserId);
            int index = sb.length() - 1;

            // Traverse the numeric part of the USER_ID and increment it
            while (index >= 4 && Character.isDigit(sb.charAt(index))) {
                int digit = Character.getNumericValue(sb.charAt(index));
                digit++;
                if (digit > 9) {
                    sb.setCharAt(index, '0'); // Carry-over, set current digit to 0
                    index--; // Move left
                } else {
                    sb.setCharAt(index, Character.forDigit(digit, 10)); // Set incremented digit
                    System.out.println(sb.toString());
                    return sb.toString(); // Return updated USER_ID
                }
            }

            // If we reach here, it means we need to increment the leading part
            return "USER" + "1" + "000000".substring(1);
        } else {
            // If no users exist, start from USER000001
            return "USER000001";
        }
    }

    public void register(String firstName, String lastName, String email, String ic, String Id, String password) throws SQLException {
        String userId = generateUserId();

        String sql = "INSERT INTO USERS (USER_ID, ID, IC_PASSPORT, PASS, FIRST_NAME, LAST_NAME, EMAIL, ACCOUNT_TYPE) VALUES (?, ?, ?, ?, ?, ?, ?, 'CUSTOMER')";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, userId);   // Set the generated USER_ID
        pstmt.setString(2, Id);  // Set the custom user ID entered by the user (for login)
        pstmt.setString(3, ic);
        pstmt.setString(4, password);
        pstmt.setString(5, firstName);
        pstmt.setString(6, lastName);
        pstmt.setString(7, email);

        int rowsInserted = pstmt.executeUpdate();
        conn.commit();
    }

    public void createAcc(String firstName, String lastName, String email, String ic, String Id, String password, String accountType) throws SQLException {
        String userId = generateUserId();

        if (accountType.equalsIgnoreCase("Customer")) {
            accountType = "CUSTOMER";
        } else if (accountType.equalsIgnoreCase("Admin")) {
            accountType = "ADMIN";
        }

        String sql = "INSERT INTO USERS (USER_ID, ID, IC_PASSPORT, PASS, FIRST_NAME, LAST_NAME, EMAIL, ACCOUNT_TYPE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, userId);   // Set the generated USER_ID
        pstmt.setString(2, Id);  // Set the custom user ID entered by the user (for login)
        pstmt.setString(3, ic);
        pstmt.setString(4, password);
        pstmt.setString(5, firstName);
        pstmt.setString(6, lastName);
        pstmt.setString(7, email);
        pstmt.setString(8, accountType);

        int rowsInserted = pstmt.executeUpdate();
        conn.commit();
    }

    public String[] retrieveCredentials(String userId) throws RemoteException, SQLException {
        String[] userCredentials = null;

        String query = "SELECT id, ic_passport, pass, first_name, last_name, email FROM USERS WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    userCredentials = new String[]{rs.getString("id"), rs.getString("IC_PASSPORT"), rs.getString("pass"),
                        rs.getString("first_name"), rs.getString("last_name"), rs.getString("email")};
                }
            }
        }
        return userCredentials;
    }

    public boolean isIdTaken(String Id) throws RemoteException, SQLException {
        String query = "SELECT COUNT(*) FROM USERS WHERE ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, Id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;  // If count > 0, the ID exists
            }
        }
        return false;  // If no rows match, ID does not exist
    }

    public boolean isIdTakenEdit(String Id, String userId) throws RemoteException, SQLException {
        String getCurrentRecordIdQuery = "SELECT ID FROM USERS WHERE USER_ID = ?";
        String currentRecordId = null;

        try (PreparedStatement pstmt = conn.prepareStatement(getCurrentRecordIdQuery)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                currentRecordId = rs.getString("ID");
            } else {
                return false; // User not found
            }
        }

        // Check if any OTHER user has the same ID
        String checkIdQuery = "SELECT COUNT(*) FROM USERS WHERE ID = ? AND ID != ?";
        try (PreparedStatement pstmt = conn.prepareStatement(checkIdQuery)) {
            pstmt.setString(1, Id);
            pstmt.setString(2, currentRecordId); // Ignore self
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // true if taken by someone else
            }
        }

        return false;
    }

    public void editProfile(String userId, String firstName, String lastName, String email, String ic, String Id, String password) throws SQLException {
        String sql = "UPDATE USERS SET ID = ?, IC_PASSPORT = ?, PASS = ?, FIRST_NAME = ?, LAST_NAME = ?, EMAIL = ? WHERE USER_ID = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, Id);
        pstmt.setString(2, ic);
        pstmt.setString(3, password);
        pstmt.setString(4, firstName);
        pstmt.setString(5, lastName);
        pstmt.setString(6, email);
        pstmt.setString(7, userId);

        int rowsInserted = pstmt.executeUpdate();
        conn.commit();
    }

    //ORDER FUNCTION
    public void createNewOrders(String orderID, String foodID, String userID, String quantity, String price, String date, String remarks) throws SQLException {
        String sql = "INSERT INTO ORDERS VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, orderID);
        pstmt.setString(2, foodID);
        pstmt.setString(3, userID);
        pstmt.setString(4, quantity);  // Use setInt for numbers
        pstmt.setString(5, price);  // Use setDouble for price
        pstmt.setString(6, date);
        pstmt.setString(7, remarks);
        int rowsInserted = pstmt.executeUpdate();
        conn.commit();
    }

    public ArrayList<String[]> getAllFood() throws SQLException {
        String sql = "SELECT * FROM FOOD";
        PreparedStatement pstm = conn.prepareStatement(sql);
        ResultSet result = pstm.executeQuery();
        ArrayList<String[]> foodItemList = new ArrayList<>();
        while (result.next()) {
            String food_ID = result.getString("FOOD_ID");
            String food_Name = result.getString("FOOD_NAME");
            String food_Desc = result.getString("FOOD_DESCRIPTION");
            String cat_ID = result.getString("CATEGORY_ID");
            String food_price = result.getString("FOOD_PRICE");
            String food_pic = result.getString("FOOD_PICTURE");

            String[] temp = {food_ID, food_Name, food_Desc, cat_ID, food_price, food_pic};
            foodItemList.add(temp);
        }
        return foodItemList;
    }

    public ArrayList<String[]> getAllFoodCategory() throws SQLException {
        String sql = "SELECT * FROM CATEGORY";
        PreparedStatement pstm = conn.prepareStatement(sql);
        ResultSet result = pstm.executeQuery();
        ArrayList<String[]> foodCategoryList = new ArrayList<>();
        while (result.next()) {
            String cat_ID = result.getString("CATEGORY_ID");
            String cat_Desc = result.getString("CATEGORY_DESCRIPTION");

            String[] temp = {cat_ID, cat_Desc};
            foodCategoryList.add(temp);
        }
        return foodCategoryList;
    }

    public String fetchLatestOrderNumber() throws SQLException {
        String sql = "SELECT ORDER_ID FROM ORDERS ORDER BY CAST(SUBSTR(ORDER_ID, 4) AS INT) DESC FETCH FIRST 1 ROWS ONLY";
        PreparedStatement psmt = conn.prepareStatement(sql);
        ResultSet result = psmt.executeQuery();
        if (result.next()) {
            String latestOrderNo = result.getString("ORDER_ID");
            StringBuilder sb = new StringBuilder(latestOrderNo);
            int index = sb.length() - 1;
            while (index >= 3 && Character.isDigit(sb.charAt(index))) {
                int digit = Character.getNumericValue(sb.charAt(index));
                digit++;
                if (digit > 9) {
                    sb.setCharAt(index, '0'); // Carry-over, set current digit to 0
                    index--; // Move left
                } else {
                    sb.setCharAt(index, Character.forDigit(digit, 10)); // Set incremented digit
                    return sb.toString(); // Return updated order ID
                }
            }
            return "ORD" + "1" + "000000".substring(1);
        } else {
            return "ORD000001";
        }
    }

    //REPORT FUNCTION
    public ArrayList<String[]> getSalesReport(String daterange) throws SQLException {
        String sql;
        PreparedStatement psmt;

        if (daterange.equalsIgnoreCase("all time")) {
            sql = "SELECT FOOD_ID, "
                    + "SUM(CAST(QUANTITY AS INT)) AS TOTAL_QUANTITY, "
                    + "SUM(CAST(SUBTOTAL AS DECIMAL(10,2))) AS TOTAL_SUBTOTAL "
                    + "FROM ORDERS "
                    + "GROUP BY FOOD_ID";

            psmt = conn.prepareStatement(sql);
        } else {
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusMonths(1);

            sql = "SELECT FOOD_ID, "
                    + "SUM(CAST(QUANTITY AS INT)) AS TOTAL_QUANTITY, "
                    + "SUM(CAST(SUBTOTAL AS DECIMAL(10,2))) AS TOTAL_SUBTOTAL "
                    + "FROM ORDERS "
                    + "WHERE CAST(SUBSTR(DATE, 1, 10) AS DATE) BETWEEN ? AND ? "
                    + "GROUP BY FOOD_ID";

            psmt = conn.prepareStatement(sql);
            psmt.setDate(1, java.sql.Date.valueOf(startDate));
            psmt.setDate(2, java.sql.Date.valueOf(endDate));
        }

        ResultSet result = psmt.executeQuery();
        ArrayList<String[]> salesReport = new ArrayList<>();

        while (result.next()) {
            String foodId = result.getString("FOOD_ID");
            String foodName = getFoodName(result.getString("FOOD_ID"));
            String totalQty = result.getString("TOTAL_QUANTITY");
            String totalSubtotal = result.getString("TOTAL_SUBTOTAL");

            salesReport.add(new String[]{foodId, foodName, totalQty, totalSubtotal});
        }

        return salesReport;

    }

    public ArrayList<String[]> getOrdersReport(String daterange) {
        String sql = "SELECT * FROM ORDERS";

        return null;
    }

    public String getFoodName(String food_id) throws SQLException {
        String sql = "SELECT FOOD_NAME FROM FOOD WHERE FOOD_ID = ?";
        PreparedStatement psmt = conn.prepareStatement(sql);
        psmt.setString(1, food_id);
        ResultSet result = psmt.executeQuery();

        if (result.next()) {
            return result.getString("FOOD_NAME");
        } else {
            return null; // or throw an exception if not found
        }
    }
}
