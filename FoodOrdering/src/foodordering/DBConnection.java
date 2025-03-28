/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodordering;

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
