package invoice;

import entityClasses.Client;
import entityClasses.Employee;
import entityClasses.Invoice;
import entityClasses.InvoiceLineItem;
import entityClasses.InvoiceToProject;
import entityClasses.Project;
import importData.SystemData;
import javax.swing.JFrame;

import importData.ConnectionManager;
import entityClasses.ProjectPerson;
import entityClasses.WorkedHours;
import java.awt.Component;
import java.io.File;
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

public class GenerateInvoice extends javax.swing.JPanel {
    JFrame  panelHolder;
    SystemData systemData;
    
    ArrayList projectListCached;
    List<String> clientNameList;
    List<ProjectPerson> projectPersonList;
    List<Long> projectPersonWorkedHoursList;
    DefaultTableModel tableModel;
    Object[][] rowData;
    Calendar selectedInvoiceDateCal;
        
    public GenerateInvoice(JFrame  panelHolder, SystemData systemData) {
        this.panelHolder = panelHolder;
        this.systemData = systemData;  
        initComponents();
                
        selectedInvoiceDateCal = Calendar.getInstance();
        selectedInvoiceDateCal.set(Calendar.DAY_OF_WEEK, 4);   

        populateTable();
    } 
    
    private void populateTable(){
        Calendar cal = (Calendar) selectedInvoiceDateCal.clone();
        Date invoiceDate = new Date( cal.getTimeInMillis());
//        System.out.println("inv"+invoiceDate);
        jLabel1.setText("List of Invoices on Invoice Date: "+ 
                new SimpleDateFormat("MM-dd-yyyy").format(invoiceDate));
        
        ConnectionManager cm = new ConnectionManager();
        EntityManager em = cm.getEntityManager();
        Query query = em.createQuery("Select pInvDate.project from ProjectInvoiceDate pInvDate "
                + "where pInvDate.invoiceDate = '" +invoiceDate+"'");
        List<Project> projectList = query.getResultList();
        System.out.println(projectList);
        int i =0; 
        rowData = new Object[projectList.size()][5]; 
        projectListCached = new ArrayList();
        
        query = em.createQuery("Select Distinct(pInvDate.clientName) from ProjectInvoiceDate pInvDate "
                + "where pInvDate.invoiceDate = '" +invoiceDate+"'");
        clientNameList = query.getResultList();
        System.out.println(clientNameList);        
                
        for (String clientName : clientNameList) {
            query = em.createQuery("Select pInvDate.project from ProjectInvoiceDate pInvDate "
                + "where pInvDate.invoiceDate = '" +invoiceDate
                    +"' and pInvDate.clientName = '"+ clientName+ "'");
            projectList = query.getResultList();
            System.out.print(clientName+": ");
            System.out.println(projectList);
            
            Client client = null;
            for(Project project: projectList){
                client = project.getClient();
                System.out.println(project.getName()+" ("+project.getId()+")");
                System.out.println(project.getClient().getNumber());
                System.out.println(project.getClient().getInvoiceFrequency()); 

                Date projectStartDate = new Date(new java.util.Date(project.getStartDate()).getTime());
                Date projectEndDate = new Date(new java.util.Date(project.getEndDate()).getTime());
                
                System.out.println("projectStartDate"+projectStartDate);
                System.out.println("projectEndDate"+projectEndDate);

                Calendar invoiceStart = (Calendar) cal.clone(); 
                Calendar invoiceEnd = (Calendar) cal.clone();  
                if(!client.getInvoiceFrequency().equals("Monthly-Cal")){
                    invoiceStart.set(Calendar.DAY_OF_WEEK, 2);
                    invoiceEnd.set(Calendar.DAY_OF_WEEK, 1);
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
                    + "where wh.isApproved= true and wh.projectID= '" + project.getId() +"'"
                    + " and wh.date between '"+ invoiceStartDate+"' and '"+invoiceEndDate
                    + "' and wh.empName='"+projectPerson.getPersonName()+"'");
                    hoursList = query.getResultList();
                    long  approvedHours= (hoursList==null || hoursList.isEmpty() || hoursList.get(0)==null)? 
                            0: hoursList.get(0);

                    query = em.createQuery("Select Sum(wh.hoursWorked) from WorkedHours wh "
                    + "where wh.date between '"+ invoiceStartDate+"' and '"+invoiceEndDate
                    + "' and wh.empName='"+projectPerson.getPersonName()
                    +"' and wh.projectID= '" + project.getId() +"'");
                    hoursList = query.getResultList(); 
                    long totalHours=  (hoursList==null || hoursList.isEmpty() || hoursList.get(0)==null)? 
                            0: hoursList.get(0);

                    query = em.createQuery("Select Count(wh.hoursWorked) from WorkedHours wh "
                    + "where wh.date between '"+ invoiceStartDate+"' and '"+invoiceEndDate
                    + "' and wh.empName='"+projectPerson.getPersonName()
                    + "' and wh.projectID= '" + project.getId() +"'");
                    hoursList = query.getResultList(); 
                    long rowCountWorkedHours=  (hoursList==null || hoursList.isEmpty() || hoursList.get(0)==null)? 
                            0: hoursList.get(0);

                    query = em.createQuery("Select count(wh.hoursWorked) from WorkedHours wh "
                    + "where wh.isInvoiced= true and wh.date between '"+ invoiceStartDate+"' and '"+invoiceEndDate
                    + "' and wh.empName='"+projectPerson.getPersonName()
                    + "' and wh.projectID= '" + project.getId() +"'");
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
                    
                if(client.getInvoiceGrouping().equals("Project")){
                    rowData[i][0] =  project.getName()+" ("+project.getId()+")";
                    rowData[i][1] =  client.getName()+ " ("+client.getInvoiceFrequency()
                            + "/" + client.getInvoiceGrouping()+ ")";
                    int status = ( isApprovalPending ? 1: (isInvoiceGenerated? 3: 2));
                    rowData[i][2] =  (isApprovalPending?"Hours Approval Pending":
                                        (isInvoiceGenerated?"Invoice Generated": "Hours Approved"));
                    rowData[i][3] =  status==2;
                    rowData[i][4] =  status==3;
                    ++i;  
                    ArrayList al = new ArrayList();
                    al.add(project);
                    projectListCached.add(al);
                }
                else{
                    rowData[i][0] =  (rowData[i][0]==null)?"<br>"+project.getName()+" ("+project.getId()+")"
                                        :rowData[i][0] + "<br>"+project.getName()+" ("+project.getId()+")";
                    rowData[i][1] =  client.getName()+ " ("+client.getInvoiceFrequency()
                            + "/" + client.getInvoiceGrouping()+ ")";
                    int status = ( isApprovalPending ? 1: (isInvoiceGenerated? 3: 2));
                    rowData[i][2] =  (rowData[i][2]==null)?"<br>"+(isApprovalPending?"Hours Approval Pending":
                                        (isInvoiceGenerated?"Invoice Generated": "Hours Approved"))
                                        :rowData[i][2]+ "<br>"+(isApprovalPending?"Hours Approval Pending":
                                        (isInvoiceGenerated?"Invoice Generated": "Hours Approved"));
                    rowData[i][3] =  (rowData[i][3]==null)?status==2:((Boolean)rowData[i][3] && status==2);
                    rowData[i][4] =  status==3;
                    
//                System.out.println("NJ"+rowData[i][0]);
                }
                        
            }
            if(!client.getInvoiceGrouping().equals("Project")){
//                System.out.println("NHV"+rowData[i][0]);
                rowData[i][0] =  "<html>"+rowData[i][0].toString().substring(4)+"</html>";
                rowData[i][2] =  "<html>"+rowData[i][2].toString().substring(4)+"</html>";
                ++i;
                projectListCached.add(projectList);
            }
        }
            
        Object[] columnNames = {"Project", "Client", "Invoice Status", "Select to Generate", "Select to Mail"};        
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


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        generateButton = new javax.swing.JButton();
        mailButton = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton2.setText("Next Week");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setText("Previous Week");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("List of Invoices on Invoice Date: 08/17/2016");

        generateButton.setText("Generate Invoice");
        generateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateButtonActionPerformed(evt);
            }
        });

        mailButton.setText("Mail Invoice");
        mailButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mailButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(generateButton)
                .addGap(27, 27, 27)
                .addComponent(mailButton)
                .addGap(24, 24, 24))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(generateButton)
                    .addComponent(mailButton)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void generateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateButtonActionPerformed
        System.out.println("---In Generate---");
        ConnectionManager cm = new ConnectionManager();
        EntityManager em = cm.getEntityManager();
        Calendar cal = (Calendar) selectedInvoiceDateCal.clone();
        cal.set(Calendar.DAY_OF_WEEK, 4);
        Date invoiceDate = new Date(cal.getTimeInMillis());
        System.out.println("inv"+invoiceDate);
                
        System.out.println(projectListCached);
        
        int i = 0;
        boolean isAtLeastOneSelected = false;
        for (Object object : projectListCached) {            
            if(!(boolean)tableModel.getValueAt(i, 3)){         
                ++i;
                continue;
            }
            ++i;
            isAtLeastOneSelected =true;
            
            List<Project> projectList = (List)object;  
                System.out.println("hdsbchbd"+projectList);
            Invoice invoice = new Invoice();
            double totalAmountDue = 0;
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
                    invoiceStart.set(Calendar.DAY_OF_WEEK, 2);
                    invoiceEnd.set(Calendar.DAY_OF_WEEK, 1);
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
                invoice.setInvoiceDate(invoiceDate);
                invoice.setInvoiceStartDate(invoiceStartDate);
                invoice.setInvoiceEndDate(invoiceEndDate);
                invoice.setClient(client);
                invoice.setClientNumber(client.getNumber());
                em.persist(invoice);
                InvoiceToProject itp = new InvoiceToProject(invoice.getId(), project.getId(), project);
                em.persist(itp);
                
                Calendar invoiceWeeklyEnd = (Calendar) invoiceStart.clone();
                invoiceWeeklyEnd.set(Calendar.DAY_OF_WEEK, 1);
                invoiceWeeklyEnd.add(Calendar.DAY_OF_WEEK, 7);
                Date invoiceWeeklyEndDate = new Date(invoiceWeeklyEnd.getTimeInMillis());
                System.out.println(invoiceWeeklyEndDate+"----");
                if(invoiceWeeklyEndDate.after(invoiceEndDate)){
                    invoiceWeeklyEndDate = invoiceEndDate;
                }
                while(invoiceWeeklyEndDate.compareTo(invoiceEndDate)<=0){
                    System.out.println("in side");
                    Query query = em.createQuery("Select pp  from ProjectPerson pp "
                        + "where pp.projectID= '" + project.getId() +"' and pp.isActivated = 'Yes'");
                    projectPersonList = query.getResultList();
        //            System.out.println(projectPersonList);
//                    projectPersonWorkedHoursList = new ArrayList<>(projectPersonList.size());

                    for (ProjectPerson projectPerson : projectPersonList) {
                        System.out.println(projectPerson.getPersonName());
                        List<Long> hoursList = null;

                        query = em.createQuery("Select Sum(wh.hoursWorked) from WorkedHours wh "
                        + "where wh.date between '"+ invoiceStartDate+"' and '"+invoiceWeeklyEndDate
                        + "' and wh.empName='"+projectPerson.getPersonName()+"'"
                        + " and wh.projectID= '" + project.getId() +"'");
                        hoursList = query.getResultList(); 
                        long totalHours=  (hoursList==null || hoursList.isEmpty() || hoursList.get(0)==null)? 
                                0: hoursList.get(0);
                        
                        double weeklyAmount = (totalHours>40)? (40 * projectPerson.getProjectPersonName().getBillRate())
                                                            +(totalHours-40)* projectPerson.getProjectPersonName().getBillRate() *1.5
                                            : (totalHours * projectPerson.getProjectPersonName().getBillRate());
                        totalAmountDue = totalAmountDue + weeklyAmount;
                        if(weeklyAmount>0){
                            InvoiceLineItem invoiceLineItem = new InvoiceLineItem(invoice.getId(), invoiceStartDate,                            
                                    invoiceWeeklyEndDate, projectPerson.getPersonName(),
                                    projectPerson.getProjectPersonName().getBillRate(), totalHours, 
                                    project, project.getId(), weeklyAmount);
                            em.persist(invoiceLineItem);
                        }
                        

                        query = em.createQuery("Select wh from WorkedHours wh "
                        + "where wh.date between '"+ invoiceStartDate+"' and '"+invoiceWeeklyEndDate
                        + "' and wh.empName='"+projectPerson.getPersonName()+"'"
                        + " and wh.projectID= '" + project.getId() +"'");
                        List<WorkedHours> workedHoursList = query.getResultList();
                        for (WorkedHours workedHours : workedHoursList) {
                            workedHours.setIsInvoiced(true);         
                            System.out.println("jsbdvdb");
                        }                              
                    }
                    
                    System.out.println(invoiceStartDate+"--"+invoiceWeeklyEndDate);
                    invoiceWeeklyEnd.add(Calendar.DAY_OF_WEEK, 1);
                    invoiceStartDate = new Date(invoiceWeeklyEnd.getTimeInMillis());
                    invoiceWeeklyEnd.add(Calendar.DAY_OF_WEEK, 6);
                    invoiceWeeklyEndDate = new Date(invoiceWeeklyEnd.getTimeInMillis());                    
                    if(invoiceStartDate.compareTo(invoiceEndDate)<=0 && invoiceWeeklyEndDate.after(invoiceEndDate)){
                        invoiceWeeklyEndDate = invoiceEndDate;
                    }
                    System.out.print(invoiceStartDate+"----");
                    System.out.println(invoiceWeeklyEndDate+"----");
                }               
            }
            invoice.setTotalAmountDue(totalAmountDue);
            em.persist(invoice);
        }
        if(!isAtLeastOneSelected){            
            JOptionPane.showMessageDialog(null, "No Project selected to Generate Invoice!");
            return;
        }
        cm.close();         
                                                  
        JOptionPane.showMessageDialog(null, "Invoice(s) for Selected project generated sucessfully.");        
        populateTable();
    }//GEN-LAST:event_generateButtonActionPerformed

    private void mailButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mailButtonActionPerformed
        JOptionPane.showMessageDialog(null, "Selected Invoice(s) Mailed.");                    
    }//GEN-LAST:event_mailButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        selectedInvoiceDateCal.add(Calendar.DAY_OF_WEEK, -7);
        populateTable();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        selectedInvoiceDateCal.add(Calendar.DAY_OF_WEEK, 7);
        populateTable();
    }//GEN-LAST:event_jButton2ActionPerformed
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton generateButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton mailButton;
    // End of variables declaration//GEN-END:variables
}
