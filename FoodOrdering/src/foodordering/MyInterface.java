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
interface MyInterface extends Remote{
     byte[] getFoodImage(String imageName) throws RemoteException;
}
