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
import java.sql.Date;
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

public class GenerateInvoice extends javax.swing.JPanel {
    JFrame  panelHolder;
    SystemData systemData;
    
    List<Project> projectList;
    List<ProjectPerson> projectPersonList;
    List<Long> projectPersonWorkedHoursList;
    DefaultTableModel tableModel;
    Object[][] rowData;
        
    public GenerateInvoice(JFrame  panelHolder, SystemData systemData) {
        this.panelHolder = panelHolder;
        this.systemData = systemData;  
        initComponents();
        
        ConnectionManager cm = new ConnectionManager();
        EntityManager em = cm.getEntityManager();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, 4);
        Date invoiceDate = new Date(cal.getTimeInMillis());
        System.out.println("inv"+invoiceDate);
        Query query = em.createQuery("Select p from Project p "
                + "where p.nextInvoiceDate = '" +invoiceDate.toString()+"'");
        projectList = query.getResultList();
        System.out.println(projectList);
        int i =0; 
        rowData = new Object[projectList.size()][5]; 
        for(Project project: projectList){
            Client client = project.getClient();
            System.out.println(project.getName()+" ("+project.getId()+")");
            System.out.println(project.getClient().getNumber());
            System.out.println(project.getClient().getInvoiceFrequency()); 
            
            Date projectStartDate = new Date(new java.util.Date(project.getStartDate()).getTime());
            Date projectEndDate = new Date(new java.util.Date(project.getEndDate()).getTime());
            
            Calendar invoiceStart = (Calendar) cal.clone(); 
            Calendar invoiceEnd = (Calendar) cal.clone();  
            if(!client.getInvoiceFrequency().equals("Monthly-Cal")){
                invoiceStart.set(Calendar.DAY_OF_WEEK, 1);
                invoiceEnd.set(Calendar.DAY_OF_WEEK, 1);
                invoiceEnd.add(Calendar.DAY_OF_WEEK, -1);
            }
            if(client.getInvoiceFrequency().equals("Weekly")){
                invoiceStart.add(Calendar.DAY_OF_WEEK, -7);
            }
            else if(client.getInvoiceFrequency().equals("BiWeekly")){
                invoiceStart.add(Calendar.DAY_OF_WEEK, -14);
            }
            else if(client.getInvoiceFrequency().equals("Monthly")){
                invoiceStart.add(Calendar.DAY_OF_WEEK, -28);
            }
            else if(client.getInvoiceFrequency().equals("Monthly-Cal")){
                invoiceStart.add(Calendar.MONTH, -1);
                invoiceStart.set(Calendar.DATE, 1);
                invoiceEnd.set(Calendar.DATE, 1);                
                invoiceEnd.add(Calendar.DAY_OF_MONTH, -1);             
            }            
            Date invoiceStartDate = new Date(invoiceStart.getTimeInMillis());
            Date invoiceEndDate = new Date(invoiceEnd.getTimeInMillis());
            if(invoiceStartDate.before(projectStartDate)){
                invoiceStartDate = projectStartDate;
            }
            if(invoiceEndDate.after(projectEndDate)){
                invoiceEndDate = projectEndDate;
            }
            System.out.print(invoiceStartDate+"----");
            System.out.println(invoiceEndDate+"----");
            
            query = em.createQuery("Select pp  from ProjectPerson pp "
                + "where pp.projectID= '" + project.getId() +"' and pp.isActivated = 'Yes'");
            projectPersonList = query.getResultList();
//            System.out.println(projectPersonList);
            projectPersonWorkedHoursList = new ArrayList<>(projectPersonList.size());
            

            boolean isApprovalPending = false;
            boolean isInvoiceGenerated = true;
            for (ProjectPerson projectPerson : projectPersonList) {
                System.out.println(projectPerson.getPersonName());
                List<Long> hoursList = null;
                query = em.createQuery("Select Sum(wh.hoursWorked) from WorkedHours wh "
                + "where wh.isApproved= true and wh.date between '"+ invoiceStartDate+"' and '"+invoiceEndDate
                + "' and wh.empName='"+projectPerson.getPersonName()+"'");
                hoursList = query.getResultList();
                long  approvedHours= (hoursList==null || hoursList.isEmpty() || hoursList.get(0)==null)? 
                        0: hoursList.get(0);
                
                query = em.createQuery("Select Sum(wh.hoursWorked) from WorkedHours wh "
                + "where wh.date between '"+ invoiceStartDate+"' and '"+invoiceEndDate
                + "' and wh.empName='"+projectPerson.getPersonName()+"'");
                hoursList = query.getResultList(); 
                long totalHours=  (hoursList==null || hoursList.isEmpty() || hoursList.get(0)==null)? 
                        0: hoursList.get(0);
                
                query = em.createQuery("Select Count(wh.hoursWorked) from WorkedHours wh "
                + "where wh.date between '"+ invoiceStartDate+"' and '"+invoiceEndDate
                + "' and wh.empName='"+projectPerson.getPersonName()+"'");
                hoursList = query.getResultList(); 
                long rowCountWorkedHours=  (hoursList==null || hoursList.isEmpty() || hoursList.get(0)==null)? 
                        0: hoursList.get(0);
                
                query = em.createQuery("Select count(wh.hoursWorked) from WorkedHours wh "
                + "where wh.isInvoiced= true and wh.date between '"+ invoiceStartDate+"' and '"+invoiceEndDate
                + "' and wh.empName='"+projectPerson.getPersonName()+"'");
                hoursList = query.getResultList();
                long rowCountInvoicedHours=  (hoursList==null || hoursList.isEmpty() || hoursList.get(0)==null)? 
                        0: hoursList.get(0);
                
                System.out.println(approvedHours + ": "+ totalHours+": "+ rowCountInvoicedHours);
                
                if(totalHours> approvedHours || rowCountWorkedHours==0){
                    isApprovalPending = true;
                }
                if(rowCountInvoicedHours==0){
                    isInvoiceGenerated = false;
                }
                projectPersonWorkedHoursList.add(totalHours);                
            }
            System.out.println(projectPersonList);
            System.out.println(projectPersonWorkedHoursList);
                        
             
            rowData[i][0] =  project.getName()+" ("+project.getId()+")";
            rowData[i][1] =  client.getName();
            int status = ( isApprovalPending ? 1: (isInvoiceGenerated? 3: 2));
            rowData[i][2] =  (isApprovalPending?"Hours Approval Pending":
                                (isInvoiceGenerated?"Invoice Generated": "Hours Approved"));
            rowData[i][3] =  status==2;
            rowData[i][4] =  status==3;
            ++i;          
        }    
        Object[] columnNames = {"Project", "Client", "Invoice Status", "Select to Genrate", "Select to Mail"};        
        tableModel = new DefaultTableModel(rowData, columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                if(column==3)
                    return (Boolean)rowData[row][3];
                if(column==4)
                    return (Boolean)rowData[row][4];
                return false;
            }
         };
        
        jTable1 = new JTable(tableModel){
            private static final long serialVersionUID = 1L;
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 3:
                        return Boolean.class;
                    case 4:
                        return Boolean.class;
                    default:
                        return String.class;
                }
            }
        };        
        jScrollPane1.setViewportView(jTable1);
        jTable1.setRowHeight(25);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(110);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(110);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(100);        
                
        cm.close();       
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        mailButton = new javax.swing.JButton();
        generateButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane1.setViewportView(jTable1);

        mailButton.setText("Mail Invoice");
        mailButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mailButtonActionPerformed(evt);
            }
        });

        generateButton.setText("Generate Invoice");
        generateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("List of Invoices for the current week");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(generateButton)
                        .addGap(18, 18, 18)
                        .addComponent(mailButton)
                        .addGap(11, 11, 11))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mailButton)
                    .addComponent(generateButton))
                .addGap(6, 6, 6))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 240;
        gridBagConstraints.ipady = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(29, 10, 0, 10);
        add(jPanel2, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void generateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateButtonActionPerformed
        System.out.println("---In Generate---");
        ConnectionManager cm = new ConnectionManager();
        EntityManager em = cm.getEntityManager();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, 4);
        Date invoiceDate = new Date(cal.getTimeInMillis());
        System.out.println("inv"+invoiceDate);
                
        System.out.println(projectList);
        
        int i = 0;
        boolean isAtLeastOneSelected = false;
        for(Project project: projectList){
            if(!(boolean)tableModel.getValueAt(i, 3)){         
                ++i;
                continue;
            }
            ++i;
            isAtLeastOneSelected =true;
            Client client = project.getClient();
            System.out.println(project.getName()+" ("+project.getId()+")");
            System.out.println(project.getClient().getNumber());
            System.out.println(project.getClient().getInvoiceFrequency()); 
            
            Date projectStartDate = new Date(new java.util.Date(project.getStartDate()).getTime());
            Date projectEndDate = new Date(new java.util.Date(project.getEndDate()).getTime());
            
            Calendar invoiceStart = (Calendar) cal.clone(); 
            Calendar invoiceEnd = (Calendar) cal.clone();  
            if(!client.getInvoiceFrequency().equals("Monthly-Cal")){
                invoiceStart.set(Calendar.DAY_OF_WEEK, 1);
                invoiceEnd.set(Calendar.DAY_OF_WEEK, 1);
                invoiceEnd.add(Calendar.DAY_OF_WEEK, -1);
            }
            if(client.getInvoiceFrequency().equals("Weekly")){
                invoiceStart.add(Calendar.DAY_OF_WEEK, -7);
            }
            else if(client.getInvoiceFrequency().equals("BiWeekly")){
                invoiceStart.add(Calendar.DAY_OF_WEEK, -14);
            }
            else if(client.getInvoiceFrequency().equals("Monthly")){
                invoiceStart.add(Calendar.DAY_OF_WEEK, -28);
            }
            else if(client.getInvoiceFrequency().equals("Monthly-Cal")){
                invoiceStart.add(Calendar.MONTH, -1);
                invoiceStart.set(Calendar.DATE, 1);
                invoiceEnd.set(Calendar.DATE, 1);                
                invoiceEnd.add(Calendar.DAY_OF_MONTH, -1);             
            }            
            Date invoiceStartDate = new Date(invoiceStart.getTimeInMillis());
            Date invoiceEndDate = new Date(invoiceEnd.getTimeInMillis());
            if(invoiceStartDate.before(projectStartDate)){
                invoiceStartDate = projectStartDate;
            }
            if(invoiceEndDate.after(projectEndDate)){
                invoiceEndDate = projectEndDate;
            }
            System.out.print(invoiceStartDate+"----");
            System.out.println(invoiceEndDate+"----");
            
            Query query = em.createQuery("Select pp  from ProjectPerson pp "
                + "where pp.projectID= '" + project.getId() +"' and pp.isActivated = 'Yes'");
            projectPersonList = query.getResultList();
//            System.out.println(projectPersonList);
            projectPersonWorkedHoursList = new ArrayList<>(projectPersonList.size());
            
            
            long totalAmountDue = 0;
            for (ProjectPerson projectPerson : projectPersonList) {
                System.out.println(projectPerson.getPersonName());
                List<Long> hoursList = null;
                                
                query = em.createQuery("Select Sum(wh.hoursWorked) from WorkedHours wh "
                + "where wh.date between '"+ invoiceStartDate+"' and '"+invoiceEndDate
                + "' and wh.empName='"+projectPerson.getPersonName()+"'");
                hoursList = query.getResultList(); 
                long totalHours=  (hoursList==null || hoursList.isEmpty() || hoursList.get(0)==null)? 
                        0: hoursList.get(0);
                
                totalAmountDue = totalAmountDue + (totalHours * projectPerson.getProjectPersonName().getBillRate());
                projectPersonWorkedHoursList.add(totalHours);                
            }
            System.out.println(projectPersonList);
            System.out.println(projectPersonWorkedHoursList);
            
            Invoice invoice = new Invoice(invoiceDate, invoiceStartDate, invoiceEndDate, 
                    totalAmountDue, client, client.getNumber(), project, project.getId());
            em.persist(invoice);
            int j = 0;
            for (ProjectPerson projectPerson : projectPersonList) {
                InvoiceLineItem invoiceLineItem = new InvoiceLineItem(invoice.getId(), 
                        projectPerson.getPersonName(), projectPerson.getProjectPersonName().getBillRate(),
                        projectPersonWorkedHoursList.get(j), 
                        projectPersonWorkedHoursList.get(j)*projectPerson.getProjectPersonName().getBillRate());
                em.persist(invoiceLineItem);
                ++j;
                
                query = em.createQuery("Select wh from WorkedHours wh "
                + "where wh.date between '"+ invoiceStartDate+"' and '"+invoiceEndDate
                + "' and wh.empName='"+projectPerson.getPersonName()+"'");
                List<WorkedHours> workedHoursList = query.getResultList();
                for (WorkedHours workedHours : workedHoursList) {
                    workedHours.setIsInvoiced(true);                    
                }               
                
            }           
                   
        }
        if(!isAtLeastOneSelected){            
            JOptionPane.showMessageDialog(null, "No Project selected to Generate Invoice!");
            return;
        }
        cm.close(); 
        
                                                  
        JOptionPane.showMessageDialog(null, "Invoice(s) for Selected project generated sucessfully.");
        
        panelHolder.setTitle("Generate Invoice");
        panelHolder.getContentPane().removeAll();
        panelHolder.getContentPane().add(new GenerateInvoice(panelHolder, systemData));
        panelHolder.getContentPane().revalidate();         
    }//GEN-LAST:event_generateButtonActionPerformed

    private void mailButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mailButtonActionPerformed
        JOptionPane.showMessageDialog(null, "Selected Invoice(s) Mailed.");
    }//GEN-LAST:event_mailButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton generateButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton mailButton;
    // End of variables declaration//GEN-END:variables
}
