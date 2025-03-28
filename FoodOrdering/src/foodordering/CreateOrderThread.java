/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodordering;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cleme
 */
public class CreateOrderThread implements Runnable{

    ArrayList<String []> orderList = new ArrayList<>();
    OrderServiceInterface connection;
    
    CreateOrderThread(ArrayList<String []> orderList, OrderServiceInterface connection){
        this.orderList = orderList;
        this.connection = connection;
    }
    
    @Override
    public void run() {
        try {
            connection.createOrder(orderList);
        } catch (RemoteException ex) {
            Logger.getLogger(CreateOrderThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
