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
    
    public synchronized void register(String firstName, String lastName, String email, String ic, String Id, String password) throws RemoteException {
        try {
            connection.register(firstName, lastName, email, ic, Id, password);
        } catch (SQLException ex) {
            Logger.getLogger(UserServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new RemoteException("Error registering user.", ex);
        }
    }
    
    public synchronized void createAcc(String firstName, String lastName, String email, String ic, String Id, String password, String accountType) throws RemoteException {
        try {
            connection.createAcc(firstName, lastName, email, ic, Id, password, accountType);
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
    
    public boolean isIdTaken(String Id) throws RemoteException {
        try {
            // Assuming connection is a valid database connection object
            return connection.isIdTaken(Id);  // Call the method of the connection object to check if the ID exists
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Database error while checking ID.", e);  // Rethrow as RemoteException if there's an error
        }
    }
    
    public boolean isIdTakenEdit(String Id, String userId) throws RemoteException {
        try {
            // Assuming connection is a valid database connection object
            return connection.isIdTakenEdit(Id, userId);  // Call the method of the connection object to check if the ID exists
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Database error while checking ID.", e);  // Rethrow as RemoteException if there's an error
        }
    }
}
