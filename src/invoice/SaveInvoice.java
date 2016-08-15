package invoice;

import entityClasses.Client;
import entityClasses.Employee;
import entityClasses.Invoice;
import entityClasses.InvoiceLineItem;
import entityClasses.Project;
import importData.SystemData;
import javax.swing.JFrame;

import importData.ConnectionManager;
import entityClasses.ProjectPerson;
import entityClasses.WorkedHours;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Scanner;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import javax.swing.table.DefaultTableModel;
//import org.apache.poi.xwpf.usermodel.XWPFDocument; 


public class SaveInvoice extends javax.swing.JPanel {
    JFrame  panelHolder;
    SystemData systemData;
        
    DefaultComboBoxModel comboModel1;
    DefaultComboBoxModel comboModel2;
    DefaultTableModel tableModel;
    Object[][] rowData;
    List<Date> dateList;
    Date fromDate;
    Date toDate;
    List<Invoice> invoiceList;
        
    public SaveInvoice(JFrame  panelHolder, SystemData systemData) {
        this.panelHolder = panelHolder;
        this.systemData = systemData;  
        initComponents();
                        
        ConnectionManager cm = new ConnectionManager();
        EntityManager em = cm.getEntityManager();
        
        comboModel1 = new DefaultComboBoxModel();
        comboModel2 = new DefaultComboBoxModel();
        Query query = em.createQuery("Select Distinct inv.invoiceDate from Invoice inv order by inv.invoiceDate");
        dateList = query.getResultList();
        for (Date date : dateList) {            
            comboModel1.addElement(new SimpleDateFormat("MM-dd-yyyy").format(date));
            comboModel2.addElement(new SimpleDateFormat("MM-dd-yyyy").format(date));
        }
        fromComboBox.setModel(comboModel1);
        toComboBox.setModel(comboModel2);        
        
        query = em.createQuery("Select Max(inv.invoiceDate) from Invoice inv");
        Date maxDate = (Date)query.getResultList().get(0);
        fromComboBox.setSelectedItem(new SimpleDateFormat("MM-dd-yyyy").format(maxDate));
        toComboBox.setSelectedItem(new SimpleDateFormat("MM-dd-yyyy").format(maxDate));
        
        fromDate = dateList.get(fromComboBox.getSelectedIndex());
        toDate = dateList.get(toComboBox.getSelectedIndex());                  
        cm.close();               
        refreshTable();        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        saveButton = new javax.swing.JButton();
        viewButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        fromComboBox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        toComboBox = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane1.setViewportView(jTable1);

        saveButton.setText("Save as PDF");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        viewButton.setText("View Invoice");
        viewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("List of Invoices in the selected timespan");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Show Invoice From");

        fromComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("To");

        toComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton1.setText("Show");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(viewButton)
                        .addGap(18, 18, 18)
                        .addComponent(saveButton)
                        .addGap(11, 11, 11))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fromComboBox, 0, 87, Short.MAX_VALUE)
                    .addComponent(toComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(55, 55, 55)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(fromComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(toComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton)
                    .addComponent(viewButton))
                .addGap(6, 6, 6))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void refreshTable(){
        ConnectionManager cm = new ConnectionManager();
        EntityManager em = cm.getEntityManager();
        
        Query query = em.createQuery("Select inv from Invoice inv"
        +" where inv.invoiceDate between '"+ fromDate+"' and '"+toDate+"'");
        invoiceList = query.getResultList();
//        System.out.println(invoiceList);
        
        Object[] columnNames = {"Project", "Client", "Invoice Number", "Invoice Date", "Invoice Amount"};
        Object[][] rowData = new Object[invoiceList.size()][5]; 
        int i =0; 
        for (Invoice invoice : invoiceList) {
            rowData[i][0] =  invoice.getProject().getName()+" ("+invoice.getProject().getId()+")";
            rowData[i][1] =  invoice.getClient().getName();
            rowData[i][2] =  invoice.getId();
            rowData[i][3] =  invoice.getInvoiceDate();
            rowData[i][4] =  invoice.getTotalAmountDue();
            ++i;                    
        }          
        tableModel = new DefaultTableModel(rowData, columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
         };
        
        jTable1 = new JTable(tableModel);        
        jScrollPane1.setViewportView(jTable1);
        jTable1.setRowHeight(25);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(130);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(150);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(100);  
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(60);  
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(80);  
        cm.close();
    }
    
    private void createDocx() throws FileNotFoundException{
//        try{
//            XWPFDocument doc = new XWPFDocument(OPCPackage.open("data/invoice.docx"));
//            for (XWPFParagraph p : doc.getParagraphs()) {
//                List<XWPFRun> runs = p.getRuns();
//                if (runs != null) {
//                    for (XWPFRun r : runs) {
//                        String text = r.getText(0);
//                        if (text != null && text.contains("needle")) {
//                            text = text.replace("needle", "haystack");
//                            r.setText(text, 0);
//                        }
//                    }
//                }
//            }
//            for (XWPFTable tbl : doc.getTables()) {
//               for (XWPFTableRow row : tbl.getRows()) {
//                  for (XWPFTableCell cell : row.getTableCells()) {
//                     for (XWPFParagraph p : cell.getParagraphs()) {
//                        for (XWPFRun r : p.getRuns()) {
//                          String text = r.getText(0);
//                          if (text.contains("needle")) {
//                            text = text.replace("needle", "haystack");
//                            r.setText(text);
//                          }
//                        }
//                     }
//                  }
//               }
//            }
//            doc.write(new FileOutputStream("data/output.docx"));
//        }catch(Exception e){
//            
//        }
    }
    
    private void viewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewButtonActionPerformed
        if(jTable1.getSelectedRow()==-1){
            JOptionPane.showMessageDialog(null, "Please select one Invoice.");
            return;            
        }
        Invoice selectedInvoice = invoiceList.get(jTable1.getSelectedRow());
        panelHolder.setTitle("View Invoice");
        panelHolder.getContentPane().removeAll();
        panelHolder.getContentPane().add(new ViewInvoice(panelHolder, systemData, selectedInvoice ));
        panelHolder.getContentPane().revalidate();
    }//GEN-LAST:event_viewButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        
    }//GEN-LAST:event_saveButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        fromDate = dateList.get(fromComboBox.getSelectedIndex());
        toDate = dateList.get(toComboBox.getSelectedIndex());        
        System.out.println(fromDate +" :"+toDate);
        if(fromDate.after(toDate)){
            JOptionPane.showMessageDialog(null, "From Date should be before To Date");
        }
        refreshTable();
    }//GEN-LAST:event_jButton1ActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> fromComboBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton saveButton;
    private javax.swing.JComboBox<String> toComboBox;
    private javax.swing.JButton viewButton;
    // End of variables declaration//GEN-END:variables
}
