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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ianwd
 */
public class UserServiceImplementation extends UnicastRemoteObject implements UserServiceInterface {


    public DBConnection connection;
    
    public UserServiceImplementation(DBConnection connection) throws RemoteException {
        super();
        this.connection = connection;
        
    }

    @Override
    public String[] authenticate(String userId, String password) throws RemoteException {
        String[] userData = null;
        try {
            userData = connection.authenticate(userId, password);
            
        } catch (SQLException ex) {
            Logger.getLogger(UserServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return userData;
    }

}
