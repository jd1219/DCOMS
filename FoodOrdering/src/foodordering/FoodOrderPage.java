/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodordering;

import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author cleme
 */
public class FoodOrderPage extends javax.swing.JFrame {

    ArrayList<Food> foodList = new ArrayList<>();
    ArrayList<Food> viewList;
    ArrayList<FoodCategory> categoryList = new ArrayList<>();
    Map<String, String> categoryMap = new HashMap<>();
    int editingTarget;
    String currentUser;
    OrderServiceInterface connection;

    /**
     * Creates new form FoodOrderPage
     */
    public FoodOrderPage(String userID) throws NotBoundException, MalformedURLException, RemoteException {
        initComponents();

        this.currentUser = userID;

        OrderServiceInterface Obj = (OrderServiceInterface) Naming.lookup("rmi://localhost:1099/OrderService");

        this.connection = Obj;
        //get all food category from database 
        //get all food from database 
        
        ArrayList <String []> fetchedFoodList = connection.getAllFood();
        ArrayList <String []> fetchedCategoryList = connection.getAllFoodCategory();
        
        for(int i = 0 ; i<fetchedCategoryList.size(); i++){
            FoodCategory temp = new FoodCategory(fetchedCategoryList.get(i)[0], fetchedCategoryList.get(i)[1]);
            categoryList.add(temp);
        }

        FoodCategory allCategory = new FoodCategory("ALL", "All Categories");
        // Add all selection
        categoryComboBox.addItem(allCategory);

        //populate combobox + mapping for hashmap
        for (FoodCategory cat : categoryList) {
            categoryMap.put(cat.getCatID(), cat.getCatDesc());
            categoryComboBox.addItem(cat);
        }

     
        for(int i = 0 ; i<fetchedFoodList.size(); i++){
            Food temp = new Food(fetchedFoodList.get(i)[0], fetchedFoodList.get(i)[1], fetchedFoodList.get(i)[2], fetchedFoodList.get(i)[3], fetchedFoodList.get(i)[4], fetchedFoodList.get(i)[5]);
            foodList.add(temp);
        }

        viewList = new ArrayList<>(foodList);
        updateFoodListTable(viewList);

        //hide foodID from all list table
        FoodListTable.getColumnModel().getColumn(0).setMinWidth(0); // Hide ID column
        FoodListTable.getColumnModel().getColumn(0).setMaxWidth(0);
        FoodListTable.getColumnModel().getColumn(0).setWidth(0);

        //hide remarks from order list table
        orderListTable.getColumnModel().getColumn(4).setMinWidth(0); // Hide ID column
        orderListTable.getColumnModel().getColumn(4).setMaxWidth(0);
        orderListTable.getColumnModel().getColumn(4).setWidth(0);

        //hide foodid from order list table
        orderListTable.getColumnModel().getColumn(5).setMinWidth(0); // Hide ID column
        orderListTable.getColumnModel().getColumn(5).setMaxWidth(0);
        orderListTable.getColumnModel().getColumn(5).setWidth(0);

        jTextField1.setText("0.00");

        //Register change listener for combobox
        categoryComboBox.addActionListener(e -> {
            filterFoodTF.setText("");
            FoodCategory selectedCategory = (FoodCategory) categoryComboBox.getSelectedItem();
            viewList.clear();
            if (selectedCategory != null && selectedCategory.getCatID().equals("ALL")) {
                viewList.addAll(foodList);
                updateFoodListTable(viewList);
            } else {

                for (int i = 0; i < foodList.size(); i++) {
                    if (foodList.get(i).getFoodCatID().equals(selectedCategory.getCatID())) {
                        viewList.add(foodList.get(i));
                    }
                }
                updateFoodListTable(viewList);
            }
        });

        //formating of table headers
        JTableHeader header = orderListTable.getTableHeader();
        header.setFont(new Font("Tahoma", Font.BOLD, 18));
        header = FoodListTable.getTableHeader();
        header.setFont(new Font("Tahoma", Font.BOLD, 18));

        this.setLocationRelativeTo(null);
        this.setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void removeEditingRow() {
        deleteOrder(editingTarget);
    }

    public void OrderItems(Food orderedFood, String quantity, String remarks) {
        DefaultTableModel model = (DefaultTableModel) orderListTable.getModel();
        String tempFoodArray[] = new String[model.getColumnCount()];
        tempFoodArray[0] = orderedFood.getFoodName();
        tempFoodArray[1] = categoryMap.getOrDefault(orderedFood.getFoodCatID(), "Unknown Category");
        tempFoodArray[2] = quantity;
        double subtotal = Math.round(Integer.parseInt(quantity) * Double.parseDouble(orderedFood.getFoodPrice()) * 100.0) / 100.0;
        tempFoodArray[3] = String.format("%.2f", subtotal);

        tempFoodArray[4] = remarks;
        tempFoodArray[5] = orderedFood.getFoodID();
        model.addRow(tempFoodArray);
        jTextField1.setText(String.format("%.2f", Double.parseDouble(jTextField1.getText()) + subtotal));
    }

    private void updateFoodListTable(ArrayList<Food> x) {
        DefaultTableModel model = (DefaultTableModel) FoodListTable.getModel();
        model.setRowCount(0);
        String tempFoodArray[] = new String[model.getColumnCount()];
        for (int i = 0; i < x.size(); i++) {
            Food currentFood = x.get(i);
            tempFoodArray[0] = currentFood.getFoodID();
            tempFoodArray[1] = currentFood.getFoodName();
            tempFoodArray[2] = categoryMap.getOrDefault(currentFood.getFoodCatID(), "Unknown Category");
            tempFoodArray[3] = currentFood.getFoodPrice();
            model.addRow(tempFoodArray);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        MainContent = new javax.swing.JPanel();
        CheckoutList = new javax.swing.JPanel();
        OrderedItemsTable = new javax.swing.JScrollPane();
        orderListTable = new javax.swing.JTable();
        CheckoutBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        FoodListView = new javax.swing.JPanel();
        FoodListTableSP = new javax.swing.JScrollPane();
        FoodListTable = new javax.swing.JTable();
        FilterBar = new javax.swing.JPanel();
        RightSearchBar = new javax.swing.JPanel();
        categoryComboBox = new javax.swing.JComboBox<>();
        LeftSearchBar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        filterFoodTF = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1500, 700));
        setPreferredSize(new java.awt.Dimension(1500, 700));
        setSize(new java.awt.Dimension(1500, 700));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        MainContent.setMinimumSize(new java.awt.Dimension(1400, 550));
        MainContent.setPreferredSize(new java.awt.Dimension(1400, 550));
        MainContent.setLayout(new java.awt.GridBagLayout());

        CheckoutList.setMinimumSize(new java.awt.Dimension(660, 462));
        CheckoutList.setPreferredSize(new java.awt.Dimension(660, 462));
        CheckoutList.setLayout(new java.awt.GridBagLayout());

        orderListTable.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        orderListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Food Name", "Food Category", "Quantity", "Subtotal Price", "Remarks(HIDDEN)", "FoodID"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        orderListTable.setColumnSelectionAllowed(true);
        orderListTable.setRowHeight(30);
        orderListTable.getTableHeader().setReorderingAllowed(false);
        OrderedItemsTable.setViewportView(orderListTable);
        orderListTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (orderListTable.getColumnModel().getColumnCount() > 0) {
            orderListTable.getColumnModel().getColumn(4).setResizable(false);
            orderListTable.getColumnModel().getColumn(5).setResizable(false);
        }

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        CheckoutList.add(OrderedItemsTable, gridBagConstraints);

        CheckoutBtn.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        CheckoutBtn.setText("Checkout");
        CheckoutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckoutBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        CheckoutList.add(CheckoutBtn, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setText("Cart");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        CheckoutList.add(jLabel2, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel3.setText("Total Price:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        CheckoutList.add(jLabel3, gridBagConstraints);

        jTextField1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jTextField1.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        CheckoutList.add(jTextField1, gridBagConstraints);

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton1.setText("Delete");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        CheckoutList.add(jButton1, gridBagConstraints);

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton2.setText("Edit");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        CheckoutList.add(jButton2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 25, 0, 0);
        MainContent.add(CheckoutList, gridBagConstraints);

        FoodListView.setLayout(new java.awt.BorderLayout());

        FoodListTable.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        FoodListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "FoodID_HIDDEN", "Food Name", "Food Category", "Food Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        FoodListTable.setRowHeight(30);
        FoodListTable.getTableHeader().setReorderingAllowed(false);
        FoodListTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                FoodListTableMouseClicked(evt);
            }
        });
        FoodListTableSP.setViewportView(FoodListTable);
        FoodListTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (FoodListTable.getColumnModel().getColumnCount() > 0) {
            FoodListTable.getColumnModel().getColumn(0).setResizable(false);
        }

        FoodListView.add(FoodListTableSP, java.awt.BorderLayout.CENTER);

        FilterBar.setLayout(new java.awt.BorderLayout());

        RightSearchBar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        categoryComboBox.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        categoryComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                categoryComboBoxItemStateChanged(evt);
            }
        });
        RightSearchBar.add(categoryComboBox);

        FilterBar.add(RightSearchBar, java.awt.BorderLayout.LINE_END);

        LeftSearchBar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Search");
        LeftSearchBar.add(jLabel1);

        filterFoodTF.setColumns(20);
        filterFoodTF.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        filterFoodTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterFoodTFActionPerformed(evt);
            }
        });
        filterFoodTF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                filterFoodTFKeyPressed(evt);
            }
        });
        LeftSearchBar.add(filterFoodTF);

        FilterBar.add(LeftSearchBar, java.awt.BorderLayout.CENTER);

        FoodListView.add(FilterBar, java.awt.BorderLayout.PAGE_START);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 581, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        FoodListView.add(jPanel3, java.awt.BorderLayout.SOUTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        MainContent.add(FoodListView, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(50, 50, 50, 50);
        getContentPane().add(MainContent, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void filterFoodTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterFoodTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filterFoodTFActionPerformed

    private void FoodListTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FoodListTableMouseClicked
        // TODO add your handling code here:
        this.setEnabled(false);
        int ordered = -1;
        int selectedRow = FoodListTable.getSelectedRow();
        Food viewTarget = null;
        if (selectedRow != -1) {
            Object foodID = FoodListTable.getValueAt(selectedRow, 0); // Get ID from column 0 (HIDDEN)
            for (int i = 0; i < foodList.size(); i++) {
                if (foodID.toString().equals(foodList.get(i).getFoodID())) {
                    viewTarget = foodList.get(i);
                }
            }
            String foodCat = FoodListTable.getValueAt(selectedRow, 2).toString();
            String subtotal;
            FoodDetailsPage x = null;
            for (int i = 0; i < orderListTable.getRowCount(); i++) {
                if (orderListTable.getValueAt(i, 5).toString().equals(foodID.toString())) {
                    ordered = i;
                    break;
                }
            }

            if (ordered == -1) {
                try {
                    subtotal = FoodListTable.getValueAt(selectedRow, 3).toString();
                    x = new FoodDetailsPage(viewTarget, foodCat, this, "", 1, 'C', subtotal, this.connection);
                } catch (RemoteException ex) {
                    Logger.getLogger(FoodOrderPage.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                subtotal = orderListTable.getValueAt(ordered, 3).toString();
                String remarks = orderListTable.getValueAt(ordered, 4).toString();
                int quantity = Integer.parseInt(orderListTable.getValueAt(ordered, 2).toString());
                try {
                    x = new FoodDetailsPage(viewTarget, foodCat, this, remarks, quantity, 'E', subtotal, this.connection);
                } catch (RemoteException ex) {
                    Logger.getLogger(FoodOrderPage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            x.setVisible(true);
        }
    }//GEN-LAST:event_FoodListTableMouseClicked

    private void categoryComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_categoryComboBoxItemStateChanged

    }//GEN-LAST:event_categoryComboBoxItemStateChanged

    private void filterFoodTFKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filterFoodTFKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            String keyword = filterFoodTF.getText();
            ArrayList<Food> temp = new ArrayList<>();
            for (int i = 0; i < viewList.size(); i++) {
                if (viewList.get(i).getFoodName().toLowerCase().contains(keyword.toLowerCase())) {
                    temp.add(viewList.get(i));

                }
            }
            updateFoodListTable(temp);
        }
    }//GEN-LAST:event_filterFoodTFKeyPressed

    private void deleteOrder(int row) {
        DefaultTableModel model = (DefaultTableModel) orderListTable.getModel();
        String pricelabel = jTextField1.getText();

        jTextField1.setText(String.format("%.2f", Double.parseDouble(pricelabel) - Double.parseDouble(orderListTable.getValueAt(row, 3).toString())));
        model.removeRow(row);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int selectedrow = orderListTable.getSelectedRow();
        if (selectedrow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a row for deletion");
        } else {
            deleteOrder(selectedrow);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        editingTarget = orderListTable.getSelectedRow();
        if (editingTarget == -1) {
            JOptionPane.showMessageDialog(null, "Please select a row for modification");
        } else {
            this.setEnabled(false);
            Food viewTarget = null;
            Object foodID = orderListTable.getValueAt(editingTarget, 5); // Get ID from column 0 (HIDDEN)
            String remarks = orderListTable.getValueAt(editingTarget, 4).toString();
            String subtotal = orderListTable.getValueAt(editingTarget, 3).toString();
            System.out.println(subtotal);
            for (int i = 0; i < foodList.size(); i++) {
                if (foodID.toString().equals(foodList.get(i).getFoodID())) {
                    viewTarget = foodList.get(i);
                }
            }
            String foodCat = orderListTable.getValueAt(editingTarget, 1).toString();
            int quantity = Integer.parseInt(orderListTable.getValueAt(editingTarget, 2).toString());
            FoodDetailsPage x = null;
            try {
                x = new FoodDetailsPage(viewTarget, foodCat, this, remarks, quantity, 'E', subtotal, this.connection);
            } catch (RemoteException ex) {
                Logger.getLogger(FoodOrderPage.class.getName()).log(Level.SEVERE, null, ex);
            }
            x.setVisible(true);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void CheckoutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckoutBtnActionPerformed
        int rowCount = orderListTable.getRowCount();
        String message = "";
        if (rowCount == 0) {
            JOptionPane.showMessageDialog(null, "You have not ordered anything");
        } else {
            for (int i = 0; i < rowCount; i++) {
                message = message + "Item : " + orderListTable.getValueAt(i, 0) + "\nQuantity: " + orderListTable.getValueAt(i, 2) + "\nPrice: " + orderListTable.getValueAt(i, 3) + "\nRemarks: " + orderListTable.getValueAt(i, 4) + "\n===============================\n";
            }
            String totalprice = jTextField1.getText();
            message = message + "Total Price : " + totalprice + "\n";
        }
        int finalchoice = JOptionPane.showConfirmDialog(null, message);
        if (finalchoice == JOptionPane.YES_OPTION) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);
            ArrayList<String[]> orderList = new ArrayList<>();
            for (int i = 0; i < rowCount; i++) {
                String[] temp = {orderListTable.getValueAt(i, 5).toString(), this.currentUser, orderListTable.getValueAt(i, 2).toString(), orderListTable.getValueAt(i, 3).toString(), formattedDateTime, orderListTable.getValueAt(i, 4).toString()};
                orderList.add(temp);
            }
            CreateOrderThread createOrderRunnable = new CreateOrderThread(orderList, connection);
            Thread createOrderThread = new Thread(createOrderRunnable);
            createOrderThread.start();
            JOptionPane.showMessageDialog(null, "Ordered Successful");
            this.dispose();
        }
    }//GEN-LAST:event_CheckoutBtnActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CheckoutBtn;
    private javax.swing.JPanel CheckoutList;
    private javax.swing.JPanel FilterBar;
    private javax.swing.JTable FoodListTable;
    private javax.swing.JScrollPane FoodListTableSP;
    private javax.swing.JPanel FoodListView;
    private javax.swing.JPanel LeftSearchBar;
    private javax.swing.JPanel MainContent;
    private javax.swing.JScrollPane OrderedItemsTable;
    private javax.swing.JPanel RightSearchBar;
    private javax.swing.JComboBox<FoodCategory> categoryComboBox;
    private javax.swing.JTextField filterFoodTF;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTable orderListTable;
    // End of variables declaration//GEN-END:variables
}
