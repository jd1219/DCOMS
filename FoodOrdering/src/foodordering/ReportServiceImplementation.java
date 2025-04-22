/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodordering;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class ReportServiceImplementation extends UnicastRemoteObject implements ReportServiceInterface {

    public DBConnection connection;

    public ReportServiceImplementation(DBConnection connection) throws RemoteException {
        super();
        this.connection = connection;

    }

    @Override
    public ArrayList<String[]> getSalesReport(String daterange) throws RemoteException {
        ArrayList<String[]> salesReport = new ArrayList<>();

        try {
            salesReport = connection.getSalesReport(daterange);

        } catch (SQLException ex) {
            Logger.getLogger(UserServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return salesReport;
    }

    @Override
    public ArrayList<String[]> getOrdersReport(String daterange) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
