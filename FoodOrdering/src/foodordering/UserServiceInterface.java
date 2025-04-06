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
 * @author ianwd
 */
public interface UserServiceInterface extends Remote{
    String[] authenticate(String userId, String password) throws RemoteException;
    void register(String firstName, String lastName, String email, String ic, String userId2, String password) throws RemoteException;
    void createAcc(String firstName, String lastName, String email, String ic, String userId3, String password) throws RemoteException;
    public String[] retrieveCredentials(String userId) throws RemoteException;
}
