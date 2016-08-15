package reports;

import invoice.*;
import entityClasses.Invoice;
import entityClasses.InvoiceLineItem;
import entityClasses.Project;
import importData.ConnectionManager;
import importData.SystemData;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import screens.HomePage;

public class ProjectReport extends javax.swing.JPanel {
    JFrame  panelHolder;
    SystemData systemData;
    
    public ProjectReport(JFrame  panelHolder, SystemData systemData) {
        this.panelHolder = panelHolder;
        this.systemData = systemData; 
        initComponents();
        
               
        ConnectionManager cm = new ConnectionManager();
        EntityManager em = cm.getEntityManager();
        Object[] columnNames = {"Project", "Client", "Manager", "Start Date", 
            "End Date", "Budget", "Utilized Budget"};
        Query query = em.createQuery("Select p  from Project p where p.status='In Progress'");
        List<Project> projects = query.getResultList();
        
        Object[][] rowData = new Object[projects.size()][7]; 
        int i =0; 
        for (Project project : projects) {
            rowData[i][0] =  project.getName();
            rowData[i][1] =  project.getClient().getName();
            rowData[i][2] =  project.getManagerName();
            rowData[i][3] =  project.getStartDate();
            rowData[i][4] =  project.getEndDate();
            rowData[i][5] =  project.getBudget();
            
            query = em.createQuery("Select Sum(inv.totalAmountDue) from Invoice inv"
                    + " where inv.projectId = '"+project.getId()+"'");
            List<Long> utilBudgetList = query.getResultList();
            long  utilizedBudget= (utilBudgetList==null || utilBudgetList.isEmpty() || utilBudgetList.get(0)==null)? 
                        0: utilBudgetList.get(0);
            rowData[i][6] =  utilizedBudget;
            ++i;                    
        }          
        DefaultTableModel tableModel = new DefaultTableModel(rowData, columnNames){
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

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        back = new javax.swing.JButton();

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Project Progress Report");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        back.setText("Back");
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(back)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(back)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        panelHolder.setTitle("Home Page");
        panelHolder.getContentPane().removeAll();
        panelHolder.getContentPane().add(new HomePage(panelHolder, systemData, false));
        panelHolder.getContentPane().revalidate();
    }//GEN-LAST:event_backActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton back;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
