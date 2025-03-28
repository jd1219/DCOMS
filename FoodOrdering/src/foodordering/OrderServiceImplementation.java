/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodordering;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cleme
 */
public class OrderServiceImplementation extends UnicastRemoteObject implements OrderServiceInterface {

    DBConnection dbConn;

    public OrderServiceImplementation(DBConnection con) throws RemoteException {
        super();
        this.dbConn = con;
    }

    @Override
    public byte[] getFoodImage(String imageName) throws RemoteException {
        File file = new File("images/" + imageName); // Make sure the images are stored here
        if (!file.exists()) {
            file = new File("images/5216909.png");
        }
        try {

            FileInputStream fis = new FileInputStream(file);
            byte[] imageData = new byte[(int) file.length()];
            fis.read(imageData);
            fis.close();
            return imageData;
        } catch (IOException e) {
            System.out.println("Error reading image: " + e.getMessage());
            return null;
        }
    }

    
    @Override
    public void add(int x, int y) throws RemoteException {
        
    }

    @Override
    public void createOrder(String orderID, String foodID, String userID, String quantity, String price, String date) throws RemoteException {
        try {
            dbConn.createNewOrders(orderID, foodID, userID, quantity, price, date);
        } catch (SQLException ex) {
            Logger.getLogger(OrderServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
