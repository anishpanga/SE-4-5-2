package reports;

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
import invoice.ViewInvoice;
import java.awt.Component;
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
import org.apache.poi.openxml4j.opc.OPCPackage;
import screens.HomePage;
import java.io.FileOutputStream;
import java.util.List;
import javax.swing.JFileChooser;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;


public class InvoiceReport extends javax.swing.JPanel {
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
        
    public InvoiceReport(JFrame  panelHolder, SystemData systemData) {
        this.panelHolder = panelHolder;
        this.systemData = systemData;  
        initComponents();
                        
        ConnectionManager cm = new ConnectionManager();
        EntityManager em = cm.getEntityManager();
        
        comboModel1 = new DefaultComboBoxModel();
        comboModel2 = new DefaultComboBoxModel();
        Query query = em.createQuery("Select Distinct inv.invoiceDate from Invoice inv order by inv.invoiceDate");
        dateList = query.getResultList();
        if(dateList.isEmpty()){ 
            jButton1.setEnabled(false);
            viewButton.setEnabled(false);
            saveButton.setEnabled(false);
            JOptionPane.showMessageDialog(null, "No Invoice for the seleted week to show.");            
            return;           
        }
        for (Date date : dateList) {            
            comboModel1.addElement(new SimpleDateFormat("MM-dd-yyyy").format(date));
            comboModel2.addElement(new SimpleDateFormat("MM-dd-yyyy").format(date));
        }
        fromComboBox.setModel(comboModel1);
        toComboBox.setModel(comboModel2);        
        
        query = em.createQuery("Select Max(inv.invoiceDate) from Invoice inv");
        Date maxDate = (Date)query.getResultList().get(0);
        fromComboBox.setSelectedItem(new SimpleDateFormat("MM-dd-yyyy").format(dateList.get(0)));
        toComboBox.setSelectedItem(new SimpleDateFormat("MM-dd-yyyy").format(maxDate));
        
        fromDate = dateList.get(fromComboBox.getSelectedIndex());
        toDate = dateList.get(toComboBox.getSelectedIndex());                  
        cm.close();               
        refreshTable();        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
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
        jLabel4 = new javax.swing.JLabel();

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane1.setViewportView(jTable1);

        saveButton.setText("Save Invoice");
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

        fromComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {  }));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("To");

        toComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {  }));

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
                    .addComponent(jScrollPane1)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fromComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(toComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(235, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(fromComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(toComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Invoice Report");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );
    }// </editor-fold>                        

    private void refreshTable(){
        ConnectionManager cm = new ConnectionManager();
        EntityManager em = cm.getEntityManager();
        
        Query query = em.createQuery("Select inv from Invoice inv "
                +" where inv.invoiceDate between '"+ fromDate+"' and '"+toDate+"'"
                + " ORDER BY INV.clientNumber, inv.invoiceDate");
        invoiceList = query.getResultList();
        System.out.println("invoiceList"+invoiceList);
        if(invoiceList.isEmpty()){        
            jButton1.setEnabled(false);
            viewButton.setEnabled(false);
            saveButton.setEnabled(false);
            JOptionPane.showMessageDialog(null, "No Invoice in the seleted week to show.");
            return;              
        }
        
        Object[] columnNames = {"Client", "Project", "Invoice Number", "Invoice Date", "Invoice Amount"};
        Object[][] rowData = new Object[invoiceList.size()][5]; 
        int i =0; 
        for (Invoice invoice : invoiceList) {
            query = em.createQuery("Select itp.project from InvoiceToProject itp"
            +" where itp.invoiceID ="+ invoice.getId() +"");
            List<Project> projectList = query.getResultList();
            System.out.println("projectList"+projectList);            
            String projectNames = "";
            for (Project project : projectList) {
                projectNames = projectNames+"<br>"+project.getName()+" ("+project.getId()+")";
            }
            projectNames = "<html>"+projectNames.substring(4)+"</html>";
            System.out.println("projectNames"+projectNames);
            rowData[i][0] =  invoice.getClient().getName();
            rowData[i][1] =  projectNames;
            rowData[i][2] =  invoice.getId();
            rowData[i][3] =  invoice.getInvoiceStartDate()+ " To "+ invoice.getInvoiceEndDate();
            rowData[i][4] =  "$"+invoice.getTotalAmountDue();
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
        for (int row = 0; row < jTable1.getRowCount(); row++){
            int rowHeight = jTable1.getRowHeight();

            for (int column = 0; column < jTable1.getColumnCount(); column++){
                Component comp = jTable1.prepareRenderer(jTable1.getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }
            jTable1.setRowHeight(row, rowHeight);
        }
        
        cm.close();
    }
    
    private void viewButtonActionPerformed(java.awt.event.ActionEvent evt) {                                           
        if(jTable1.getSelectedRow()==-1){
            JOptionPane.showMessageDialog(null, "Please select one Invoice.");
            return;            
        }
        Invoice selectedInvoice = invoiceList.get(jTable1.getSelectedRow());
        panelHolder.setTitle("View Invoice");
        panelHolder.getContentPane().removeAll();
        panelHolder.getContentPane().add(new ViewInvoice(panelHolder, systemData, selectedInvoice, true ));
        panelHolder.getContentPane().revalidate();
    }                                          

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {                                           
        if(jTable1.getSelectedRow()==-1){
            JOptionPane.showMessageDialog(null, "Please select the Invoice to save.");
            return;            
        }        
        Invoice invoice = invoiceList.get(jTable1.getSelectedRow());
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Report");   
        fileChooser.setSelectedFile(new File("Invoice"+invoice.getId()+".docx"));
        int userSelection = fileChooser.showSaveDialog(panelHolder);
        File fileToSave = null;
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            fileToSave = fileChooser.getSelectedFile();
        }
        else{
            JOptionPane.showMessageDialog(null, "Report was not saved.");
            return;
        }
        try{
            XWPFDocument doc = new XWPFDocument(OPCPackage.open("data/invoiceSample.docx"));
            XWPFTable lineItemTable = null;
            for (XWPFTable tbl : doc.getTables()) {
               for (XWPFTableRow row : tbl.getRows()) {
                  for (XWPFTableCell cell : row.getTableCells()) {
                     for (XWPFParagraph p : cell.getParagraphs()) {
                        for (XWPFRun r : p.getRuns()) {
                            String text = r.getText(0);
                            if (text != null){
                                if(text.contains("clientName")){                                
                                    text = text.replace("clientName", invoice.getClient().getName());
                                    r.setText(text, 0);
                                }
                                if(text.contains("addressLine")){                                
                                    text = text.replace("addressLine", 
                                            invoice.getClient().getAddressLine1()
                                            + (invoice.getClient().getAddressLine2().equals("")?"":
                                                    ", "+ invoice.getClient().getAddressLine2()));
                                    r.setText(text, 0);
                                }
                                if(text.contains("cityStateZip")){                                
                                    text = text.replace("cityStateZip", invoice.getClient().getCity()
                                            + ", "+invoice.getClient().getState()
                                            + " "+invoice.getClient().getZip());
                                    r.setText(text, 0);
                                }
                                if(text.contains("clientId")){                                
                                    text = text.replace("clientId", invoice.getClient().getNumber()+"");
                                    r.setText(text, 0);
                                }

                                ConnectionManager cm = new ConnectionManager();
                                EntityManager em = cm.getEntityManager();
                                Query query = em.createQuery("Select itp.project from InvoiceToProject itp"
                                    +" where itp.invoiceID ='"+ invoice.getId() +"'");
                                List<Project> projectList = query.getResultList();
                                String projectNames = "";
                                for (Project project : projectList) {
                                    projectNames = projectNames+", "+project.getName()+" ("+project.getId()+")";
                                }
                                projectNames = projectNames.substring(1);
                                em.close();
                                if(text.contains("projectName")){                                
                                    text = text.replace("projectName", projectNames);
                                    r.setText(text, 0);
                                }if(text.contains("invoiceNumber")){                                
                                    text = text.replace("invoiceNumber", invoice.getId()+"");
                                    r.setText(text, 0);
                                }if(text.contains("invoiceDate")){                                
                                    text = text.replace("invoiceDate", new SimpleDateFormat("MM-dd-yyyy").
                                            format(invoice.getInvoiceDate()));
                                    r.setText(text, 0);
                                }if(text.contains("billingTerm")){                                
                                    text = text.replace("billingTerm", invoice.getClient().getBillingTerm());
                                    r.setText(text, 0);
                                }if(text.contains("invoiceFrequency")){                                
                                    text = text.replace("invoiceFrequency", invoice.getClient().getInvoiceFrequency());
                                    r.setText(text, 0);
                                }if(text.contains("totalAmountDue")){                                
                                    text = text.replace("totalAmountDue", "$"+invoice.getTotalAmountDue());
                                    r.setText(text, 0);
                                }                         
                                
                                if(text.contains("Description")){ 
                                    lineItemTable = tbl;
                                }
                            }
                        }
                     }
                  }
               }
            }
            
            for (XWPFParagraph p : doc.getParagraphs()) {
                List<XWPFRun> runs = p.getRuns();
                if (runs != null) {
                    for (XWPFRun r : runs) {
                        String text = r.getText(0);
                        if (text != null && text.contains("amnt")) {
                            text = text.replace("amnt", "$"+invoice.getTotalAmountDue());
                            r.setText(text, 0);
                        }
                    }
                }
            }
            
            ConnectionManager cm = new ConnectionManager();
            EntityManager em = cm.getEntityManager();
            Query query = em.createQuery("Select line from InvoiceLineItem line"
                    +" where line.invoiceId ='"+ invoice.getId()+"'");
            List<InvoiceLineItem> lineItems = query.getResultList();        
        
            for (InvoiceLineItem lineItem : lineItems) {
                XWPFTableRow tableRow= lineItemTable.createRow();
                tableRow.getCell(0).setText(lineItem.getProject().getName());
                tableRow.getCell(1).setText(new SimpleDateFormat("MM-dd-yyyy").format(lineItem.getStartDate())+" To "
                        +new SimpleDateFormat("MM-dd-yyyy").format(lineItem.getEndDate()));
                tableRow.getCell(2).setText(lineItem.getDescription());
                tableRow.getCell(3).setText("$"+lineItem.getBillRate());
                tableRow.getCell(4).setText(lineItem.getHours()+"");
                tableRow.getCell(5).setText("$"+lineItem.getTotal());
            }          
            cm.close();
            
            doc.write(new FileOutputStream(fileToSave));
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "Selected Invoice Saved Successfully.");
    }                                          
       
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        fromDate = dateList.get(fromComboBox.getSelectedIndex());
        toDate = dateList.get(toComboBox.getSelectedIndex());        
        System.out.println(fromDate +" :"+toDate);
        if(fromDate.after(toDate)){
            JOptionPane.showMessageDialog(null, "From Date should be before To Date");
            return;
        }
        refreshTable();
    }                                        

    
    // Variables declaration - do not modify                     
    private javax.swing.JComboBox<String> fromComboBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton saveButton;
    private javax.swing.JComboBox<String> toComboBox;
    private javax.swing.JButton viewButton;
    // End of variables declaration                   
}
