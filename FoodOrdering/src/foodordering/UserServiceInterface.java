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
    String[] authenticate(String Id, String password) throws RemoteException;
    void register(String firstName, String lastName, String email, String ic, String Id, String password) throws RemoteException;
    void createAcc(String firstName, String lastName, String email, String ic, String Id, String password, String accountType) throws RemoteException;
    public String[] retrieveCredentials(String userId) throws RemoteException;
    void editProfile(String userId, String firstName, String lastName, String email, String ic, String Id, String password) throws RemoteException;
}
