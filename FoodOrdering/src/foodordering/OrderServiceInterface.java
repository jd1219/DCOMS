/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodordering;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author cleme
 */
interface OrderServiceInterface extends Remote{
     public byte[] getFoodImage(String imageName) throws RemoteException;
     public void createOrder(String orderID, String foodID, String userID , String quantity, String price, String date) throws RemoteException;
     
     public void add(int x, int y) throws RemoteException;
}
