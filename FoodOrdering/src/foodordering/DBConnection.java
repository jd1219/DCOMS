/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodordering;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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

    public void createNewOrders(String orderID, String foodID, String userID , String quantity, String price, String date) throws SQLException {
        Statement stm = conn.createStatement();
        stm.executeUpdate("INSERT INTO FOOD VALUES ('"+orderID+"',"+foodID+"',"+userID+"',"+quantity+"',"+price+"',"+date+"')");
        conn.commit();
    }

}
