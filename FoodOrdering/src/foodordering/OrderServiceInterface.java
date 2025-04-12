/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodordering;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author cleme
 */
interface OrderServiceInterface extends Remote{
    public ArrayList<String[]> getAllFood() throws RemoteException;
    
    public ArrayList<String[]> getAllFoodCategory() throws RemoteException;
    
    public byte[] getFoodImage(String imageName) throws RemoteException;
    
     public void createOrder(ArrayList<String []> orderList) throws RemoteException;

}
