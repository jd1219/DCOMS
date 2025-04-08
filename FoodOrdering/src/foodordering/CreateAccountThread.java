/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodordering;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ianwd
 */
public class CreateAccountThread implements Runnable {

    UserServiceInterface authService;
    String firstName;
    String lastName;
    String email;
    String ic;
    String Id;
    String password;
    String accountType;

    CreateAccountThread(UserServiceInterface authService, String firstName, String lastName, String email, String ic, String Id, String password, String accountType) {
        this.authService = authService;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.ic = ic;
        this.Id = Id;
        this.password = password;
    }

    @Override
    public void run() {
        try {
            authService.createAcc(firstName, lastName, email, ic, Id, password, accountType);
        } catch (RemoteException ex) {
            Logger.getLogger(CreateAccountThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
