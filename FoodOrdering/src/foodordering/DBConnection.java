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
import java.util.ArrayList;

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
    public String[] authenticate(String userId, String password) throws RemoteException, SQLException {
        String[] userData = null;

        String query = "SELECT id, account_type FROM USERS WHERE id = ? AND pass = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    userData = new String[]{rs.getString("id"), rs.getString("account_type")};
                }
            }
        }
        return userData; // Returns null if authentication fails
    }
    
    public String generateUserId() throws SQLException {
        String sql = "SELECT USER_ID FROM USERS ORDER BY CAST(SUBSTRING(USER_ID, 5) AS INT) DESC FETCH FIRST 1 ROWS ONLY";
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

    public void register(String firstName, String lastName, String email, String ic, String userId2, String password) throws SQLException {
        System.out.println("Register method is called");

        String userId = generateUserId();
        if (userId == null || userId.isEmpty()) {
            throw new SQLException("Generated USER_ID is null or empty.");
        }
        System.out.println("Generated USER_ID: " + userId);


        String sql = "INSERT INTO USERS (USER_ID, ID, IC_PASSPORT, PASS, FIRST_NAME, LAST_NAME, EMAIL, ACCOUNT_TYPE) VALUES (?, ?, ?, ?, ?, ?, ?, 'CUSTOMER')";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, userId);   // Set the generated USER_ID
        pstmt.setString(2, userId2);  // Set the custom user ID entered by the user (for login)
        pstmt.setString(3, ic);
        pstmt.setString(4, password);
        pstmt.setString(5, firstName);
        pstmt.setString(6, lastName);
        pstmt.setString(7, email);

        int rowsInserted = pstmt.executeUpdate();
        conn.commit();
    }
    
    public void createAccount(String firstName, String lastName, String email, String ic, String userId2, String password) throws SQLException {
        String sql = "INSERT INTO USERS (ID, IC_PASSPORT, PASS, FIRST_NAME, LAST_NAME, EMAIL, ACCOUNT_TYPE) VALUES (?, ?, ?, ?, ?, ?, 'ADMIN')";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, userId2);
        pstmt.setString(2, ic);
        pstmt.setString(3, password);
        pstmt.setString(4, firstName);
        pstmt.setString(5, lastName);
        pstmt.setString(6, email);

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

}
