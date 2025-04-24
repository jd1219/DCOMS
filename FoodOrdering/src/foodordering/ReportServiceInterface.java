/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodordering;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author User
 */
interface ReportServiceInterface extends Remote {

    public ArrayList<String[]> getSalesReport(String daterange) throws RemoteException;

    public ArrayList<String[]> getCustomersReport(String daterange) throws RemoteException;

}
