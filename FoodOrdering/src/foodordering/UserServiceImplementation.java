/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodordering;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author ianwd
 */
public class UserServiceImplementation extends UnicastRemoteObject implements UserServiceInterface {
    private static final String DB_URL = "jdbc:derby://localhost:1527/FoodOrdering";
    private static final String DB_USER = "nyahalo";
    private static final String DB_PASSWORD = "nyahalo";

    public UserServiceImplementation() throws RemoteException {
        super();
    }

    @Override
    public boolean authenticate(String userId, String password) throws RemoteException {
        boolean isAuthenticated = false;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Staff WHERE staff_name = ? AND password = ?")) {

            pstmt.setString(1, userId);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                isAuthenticated = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isAuthenticated;
    }

    public static void main(String[] args) {
        try {
            UserServiceImplementation service = new UserServiceImplementation();
            java.rmi.Naming.rebind("rmi://localhost:1098/UserService", service);
            System.out.println("User Service is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
