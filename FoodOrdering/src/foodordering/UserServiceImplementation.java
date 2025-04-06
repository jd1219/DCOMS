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
    public String[] authenticate(String Id, String password) throws RemoteException {
        String[] userData = null;
        try {
            userData = connection.authenticate(Id, password);
            
        } catch (SQLException ex) {
            Logger.getLogger(UserServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return userData;
    }
    
    public void register(String firstName, String lastName, String email, String ic, String Id, String password) throws RemoteException {
        try {
            connection.register(firstName, lastName, email, ic, Id, password);
        } catch (SQLException ex) {
            Logger.getLogger(UserServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new RemoteException("Error registering user.", ex);
        }
    }
    
    public void createAcc(String firstName, String lastName, String email, String ic, String Id, String password) throws RemoteException {
        try {
            connection.createAcc(firstName, lastName, email, ic, Id, password);
        } catch (SQLException ex) {
            Logger.getLogger(UserServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new RemoteException("Error creating account.", ex);
        }
    }
    
    public String[] retrieveCredentials(String userId) throws RemoteException {
        String[] userCredentials = null;
        try {
            userCredentials = connection.retrieveCredentials(userId);
            
        } catch (SQLException ex) {
            Logger.getLogger(UserServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return userCredentials;
    }
    
    public void editProfile(String userId, String firstName, String lastName, String email, String ic, String Id, String password) throws RemoteException {
        try {
            connection.editProfile(userId, firstName, lastName, email, ic, Id, password);
        } catch (SQLException ex) {
            Logger.getLogger(UserServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new RemoteException("Error editing profile.", ex);
        }
    }
}
