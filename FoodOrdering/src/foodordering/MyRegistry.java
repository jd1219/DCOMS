/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodordering;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

/**
 *
 * @author cleme
 */
public class MyRegistry {

    public static void main(String[] args) throws RemoteException, SQLException, FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {

        //initiate DB Connection
        DBConnection myConn = DBConnection.getInstance();

        //DCOMS Code
        Registry reg = LocateRegistry.createRegistry(1099);
        reg.rebind("OrderService", new OrderServiceImplementation(myConn));
        reg.rebind("UserService", new UserServiceImplementation(myConn));
        System.out.println("Server running at port 1099");
    }

}
