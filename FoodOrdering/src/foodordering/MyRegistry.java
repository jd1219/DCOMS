/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodordering;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMISocketFactory;
import java.security.KeyManagementException;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.SQLException;

/**
 *
 * @author cleme
 */
public class MyRegistry {

    public static void main(String[] args) throws RemoteException, SQLException, FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {

        // Load SSL Keystore
        String keyStorePath = "serverkeystore.jks";
        String keyStorePassword = "nyahalo";

        System.setProperty("javax.net.ssl.keyStore", keyStorePath);
        System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);
 

        // Set custom SSL RMI socket factory
        RMISocketFactory sslFactory = new SslRmiSocketFactory();
        RMISocketFactory.setSocketFactory(sslFactory);

        //initiate DB Connection
        DBConnection myConn = DBConnection.getInstance();

        //DCOMS Code
        Registry reg = LocateRegistry.createRegistry(1099);
        reg.rebind("OrderService", new OrderServiceImplementation(myConn));
        reg.rebind("UserService", new UserServiceImplementation(myConn));
        System.out.println("Server running at port 1099");
    }

}
