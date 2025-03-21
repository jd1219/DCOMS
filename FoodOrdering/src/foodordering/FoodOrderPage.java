/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foodordering;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author cleme
 */
public class FoodOrderPage extends javax.swing.JFrame {

    ArrayList<Food> foodList = new ArrayList<>();
    ArrayList<Food> viewList;
    ArrayList<FoodCategory> categoryList = new ArrayList<>();
    Map<String, String> categoryMap = new HashMap<>();

    /**
     * Creates new form FoodOrderPage
     */
    public FoodOrderPage() {
        initComponents();

        //get all food category from database 
        //get all food from database 
        //dummy data for food category
        FoodCategory category1 = new FoodCategory("C1", "Fruits");
        FoodCategory category2 = new FoodCategory("C2", "Vegetables");
        FoodCategory category3 = new FoodCategory("C3", "Dairy");
        FoodCategory category4 = new FoodCategory("C4", "Meat");
        FoodCategory category5 = new FoodCategory("C5", "Drinks");
        categoryList.add(category1);
        categoryList.add(category2);
        categoryList.add(category3);
        categoryList.add(category4);
        categoryList.add(category5);
        //end of dummy data for food category

        FoodCategory allCategory = new FoodCategory("ALL", "All Categories");
        // Add all selection
        categoryComboBox.addItem(allCategory);

        //populate combobox + mapping for hashmap
        for (FoodCategory cat : categoryList) {
            categoryMap.put(cat.getCatID(), cat.getCatDesc());
            categoryComboBox.addItem(cat);
        }

        //dummy data for food 
        foodList.add(new Food("FOOD000001", "Apple", "A sweet red fruit", "C1", "1.20", "https://upload.wikimedia.org/wikipedia/commons/1/15/Red_Apple.jpg"));
        foodList.add(new Food("FOOD000002", "Banana", "A yellow elongated fruit", "C1", "0.80", "https://upload.wikimedia.org/wikipedia/commons/8/8a/Banana-Single.jpg"));
        foodList.add(new Food("FOOD000003", "Carrot", "An orange root vegetable", "C2", "1.00", "https://picsum.photos/id/1/5000/3333"));
        foodList.add(new Food("FOOD000004", "Broccoli", "A green cruciferous vegetable", "C2", "1.50", "https://upload.wikimedia.org/wikipedia/commons/0/03/Broccoli_and_cross_section_edit.jpg"));
        foodList.add(new Food("FOOD000005", "Chicken Breast", "Lean white meat", "C3", "5.00", "https://picsum.photos/id/5/367/267"));
        foodList.add(new Food("FOOD000006", "Salmon", "Fresh Atlantic salmon", "C3", "7.00", "https://picsum.photos/id/29/367/267"));
        foodList.add(new Food("FOOD000007", "Rice", "White long-grain rice", "C4", "2.00", "https://picsum.photos/id/21/367/267"));
        foodList.add(new Food("FOOD000008", "Pasta", "Italian-style wheat pasta", "C4", "1.80", "https://picsum.photos/id/12/367/267"));
        foodList.add(new Food("FOOD000009", "Milk", "Full cream dairy milk", "C5", "3.50", "https://picsum.photos/id/14/367/267"));
        foodList.add(new Food("FOOD000010", "Cheese", "Cheddar cheese block", "C5", "4.00", "https://picsum.photos/id/13/367/267"));
        //end of dummy data for food

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

        setLocationRelativeTo(null);
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

        MenubarPlaceholder = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout MenubarPlaceholderLayout = new javax.swing.GroupLayout(MenubarPlaceholder);
        MenubarPlaceholder.setLayout(MenubarPlaceholderLayout);
        MenubarPlaceholderLayout.setHorizontalGroup(
            MenubarPlaceholderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 805, Short.MAX_VALUE)
        );
        MenubarPlaceholderLayout.setVerticalGroup(
            MenubarPlaceholderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        getContentPane().add(MenubarPlaceholder, java.awt.BorderLayout.PAGE_START);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 368, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.WEST);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 368, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.EAST);

        MainContent.setLayout(new java.awt.BorderLayout(20, 0));

        CheckoutList.setLayout(new java.awt.GridBagLayout());

        orderListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Food Name", "Food Category", "Quantity", "Subtotal Price", "Remarks(HIDDEN)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        orderListTable.setColumnSelectionAllowed(true);
        orderListTable.getTableHeader().setReorderingAllowed(false);
        OrderedItemsTable.setViewportView(orderListTable);
        orderListTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (orderListTable.getColumnModel().getColumnCount() > 0) {
            orderListTable.getColumnModel().getColumn(4).setResizable(false);
        }

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        CheckoutList.add(OrderedItemsTable, gridBagConstraints);

        CheckoutBtn.setText("Checkout");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        CheckoutList.add(CheckoutBtn, gridBagConstraints);

        jLabel2.setText("Ordered Items");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        CheckoutList.add(jLabel2, gridBagConstraints);

        jLabel3.setText("Total Price:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        CheckoutList.add(jLabel3, gridBagConstraints);

        jTextField1.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        CheckoutList.add(jTextField1, gridBagConstraints);

        jButton1.setText("Delete");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        CheckoutList.add(jButton1, gridBagConstraints);

        jButton2.setText("Edit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        CheckoutList.add(jButton2, gridBagConstraints);

        MainContent.add(CheckoutList, java.awt.BorderLayout.LINE_END);

        FoodListView.setLayout(new java.awt.BorderLayout());

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
        FoodListTable.setColumnSelectionAllowed(true);
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

        categoryComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                categoryComboBoxItemStateChanged(evt);
            }
        });
        RightSearchBar.add(categoryComboBox);

        FilterBar.add(RightSearchBar, java.awt.BorderLayout.LINE_END);

        LeftSearchBar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jLabel1.setText("Search");
        LeftSearchBar.add(jLabel1);

        filterFoodTF.setColumns(20);
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

        MainContent.add(FoodListView, java.awt.BorderLayout.CENTER);

        getContentPane().add(MainContent, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void filterFoodTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterFoodTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filterFoodTFActionPerformed

    private void FoodListTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FoodListTableMouseClicked
        // TODO add your handling code here:
        this.setEnabled(false);
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
            FoodDetailsPage x = new FoodDetailsPage(viewTarget, foodCat, this);
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
         int selectedrow = orderListTable.getSelectedRow();
        if (selectedrow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a row for deletion");
        } else {
            DefaultTableModel model = (DefaultTableModel) orderListTable.getModel();
            String pricelabel = jTextField1.getText();
            jTextField1.setText(String.valueOf(Double.parseDouble(pricelabel) - Double.parseDouble(orderListTable.getValueAt(selectedrow, 3).toString())));
            model.removeRow(selectedrow);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FoodOrderPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FoodOrderPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FoodOrderPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FoodOrderPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        FoodOrderPage x = new FoodOrderPage();
        x.setVisible(true);

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CheckoutBtn;
    private javax.swing.JPanel CheckoutList;
    private javax.swing.JPanel FilterBar;
    private javax.swing.JTable FoodListTable;
    private javax.swing.JScrollPane FoodListTableSP;
    private javax.swing.JPanel FoodListView;
    private javax.swing.JPanel LeftSearchBar;
    private javax.swing.JPanel MainContent;
    private javax.swing.JPanel MenubarPlaceholder;
    private javax.swing.JScrollPane OrderedItemsTable;
    private javax.swing.JPanel RightSearchBar;
    private javax.swing.JComboBox<FoodCategory> categoryComboBox;
    private javax.swing.JTextField filterFoodTF;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTable orderListTable;
    // End of variables declaration//GEN-END:variables
}
