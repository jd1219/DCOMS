/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodordering;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author ianwd
 */
public class SslRmiSocketFactory extends RMISocketFactory {

    private final SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
    private final SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        return sslSocketFactory.createSocket(host, port);
    }

    @Override
    public ServerSocket createServerSocket(int port) throws IOException {
        return sslServerSocketFactory.createServerSocket(port);
    }
}
