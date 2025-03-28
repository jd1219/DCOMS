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
import java.util.ArrayList;
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

    public String obtainNewOrderNumber() throws SQLException {
        return dbConn.fetchLatestOrderNumber(); // Call the method that queries D
    }

    @Override
    public void createOrder(ArrayList<String[]> orderList) throws RemoteException {
        try {
            String newOrderNumber = obtainNewOrderNumber();
            for (int i = 0; i < orderList.size(); i++) {
                dbConn.createNewOrders(newOrderNumber, orderList.get(i)[0], orderList.get(i)[1], orderList.get(i)[2], orderList.get(i)[3], orderList.get(i)[4], orderList.get(i)[5]);
                System.out.println("Record: [" + newOrderNumber + ", " + orderList.get(i)[0] + ", " + orderList.get(i)[1] + ", " + orderList.get(i)[2] + ", " + orderList.get(i)[3] + ", " + orderList.get(i)[4] +", " +orderList.get(i)[5]+ "] Added");
            }

        } catch (SQLException ex) {
            Logger.getLogger(OrderServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public ArrayList<String[]> getAllFood() throws RemoteException {
        ArrayList<String []> fetchedFoodItems = new ArrayList<>();
        try {
            fetchedFoodItems = dbConn.getAllFood();
        } catch (SQLException ex) {
            Logger.getLogger(OrderServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fetchedFoodItems;
    }

    @Override
    public ArrayList<String[]> getAllFoodCategory() throws RemoteException {
        ArrayList<String []> fetchedFoodCategory = new ArrayList<>();
        try {
            fetchedFoodCategory = dbConn.getAllFoodCategory();
        } catch (SQLException ex) {
            Logger.getLogger(OrderServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fetchedFoodCategory;
    }

}
