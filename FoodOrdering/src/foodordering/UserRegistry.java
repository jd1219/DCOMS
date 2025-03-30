/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodordering;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

/**
 *
 * @author cleme
 */
public class UserRegistry {

    public static void main(String[] args) throws RemoteException, SQLException {
        //initiate DB Connection
        DBConnection myConn = DBConnection.getInstance();
        
        //DCOMS Code
        Registry reg = LocateRegistry.createRegistry(1098);
        
        System.out.println("Server running at port 1098");
    }

}
