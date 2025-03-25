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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cleme
 */
public class Server extends UnicastRemoteObject implements MyInterface{
    public Server() throws RemoteException
    {
        super();
    }

    @Override
    public byte[] getFoodImage(String imageName) throws RemoteException {
         File file = new File("images/" + imageName); // Make sure the images are stored here
         if(!file.exists()){
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
    
}
