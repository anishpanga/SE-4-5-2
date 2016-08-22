package manage;

import screens.HomePage;
import entityClasses.Employee;
import entityClasses.Project;
import javax.persistence.EntityManager;
import javax.swing.DefaultComboBoxModel;
import entityClasses.WorkedHours;
import importData.SystemData;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import importData.ConnectionManager;
import java.util.List;
import java.util.ArrayList;
import java.sql.Date;
import java.text.SimpleDateFormat;
import javax.persistence.Query;
import javax.swing.table.DefaultTableModel;

public class ApproveWorkedHours extends javax.swing.JPanel {    
    List<Project> projectList;
    List<Date> dates;
    List<String> developersList;JFrame  panelHolder;
    SystemData systemData;
    DefaultComboBoxModel model;
    int selectedProjectId;
    DefaultTableModel tableModel;
    Calendar selectedWeekStartDate;
    
    public ApproveWorkedHours(JFrame  panelHolder, SystemData systemData) {
        this.panelHolder = panelHolder;
        this.systemData = systemData;  
        initComponents();   
        welcome.setText("Welcome "+ systemData.getCurrentUser().getEmployee().getName());
        selectedWeekStartDate = Calendar.getInstance();
        selectedWeekStartDate.set(Calendar.DAY_OF_WEEK, 2);
        
        Calendar calendar = (Calendar)selectedWeekStartDate.clone();
	calendar.set(Calendar.DAY_OF_WEEK, 2);
        Date weekStartDate = new Date(calendar.getTimeInMillis());
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndDate = new Date(calendar.getTimeInMillis());
        jLabel10.setText("Approve Worked Hours: From "+new SimpleDateFormat("MM-dd-yyyy").format(weekStartDate)
                +" To "+new SimpleDateFormat("MM-dd-yyyy").format(weekEndDate));
        
        ConnectionManager cm = new ConnectionManager();
        EntityManager em = cm.getEntityManager();
        Query query = em.createQuery("Select p from Project p "
                + "where p.managerName= '" +systemData.getCurrentUser().getEmployee().getName()+"' "
                + " and p.status = 'In Progress'");              
        projectList = query.getResultList();
        model = new DefaultComboBoxModel();
        for(Project project: projectList){
            model.addElement(project.getName()+" ("+project.getId()+")");
        }
        projectSelection.setModel(model);
        model.setSelectedItem(projectList.get(0).getName()+" ("+projectList.get(0).getId()+")");
        selectedProjectId = projectList.get(model.getIndexOf(model.getSelectedItem())).getId();
                
        Object[] columnNames = new Object[10];
        columnNames[0] ="Developer Name";
        dates = new ArrayList<>();
        for (int i = 7; i >0; ) {
            Date date = new Date(calendar.getTimeInMillis());
            columnNames[i] = new SimpleDateFormat("MM-dd-yyyy").format(date);
            dates.add(0, date);
            --i;
            calendar.add(Calendar.DAY_OF_WEEK, -1);           
        }
        columnNames[8] = "Total Hours";
        columnNames[9] = "Select";
        
        query = em.createQuery("Select pp.personName  from ProjectPerson pp "
                + "where pp.projectID='"+ selectedProjectId +"' and pp.isActivated = 'Yes'");
        developersList = query.getResultList();
        
        Object[][] rowData = new Object[developersList.size()][10];        
        boolean[] isApproved = new boolean[developersList.size()];
        int i =0;
        List<WorkedHours> hoursList;
        for(String developer: developersList){
            Date startDate = new Date(weekStartDate.getTime());
            Date endDate = new Date(weekEndDate.getTime());
            query = em.createQuery("Select wh from WorkedHours wh where wh.projectID= '"+selectedProjectId +"' "      
                    + " and wh.empName='"+developer+"'"  
                    + " and wh.date between '"+startDate+"' and '"+ endDate+"' order by wh.date");
            hoursList = query.getResultList();
            isApproved[i] = (hoursList==null || hoursList.isEmpty()? false: hoursList.get(0).isIsApproved());
            rowData[i][0] = developer;
            if(!hoursList.isEmpty()){
                rowData[i][1] = hoursList.get(0).getHoursWorked();
                rowData[i][2] = hoursList.get(1).getHoursWorked();
                rowData[i][3] = hoursList.get(2).getHoursWorked();
                rowData[i][4] = hoursList.get(3).getHoursWorked();
                rowData[i][5] = hoursList.get(4).getHoursWorked();
                rowData[i][6] = hoursList.get(5).getHoursWorked();
                rowData[i][7] = hoursList.get(6).getHoursWorked();
                int tot = hoursList.get(0).getHoursWorked()+  hoursList.get(1).getHoursWorked()
                                + hoursList.get(2).getHoursWorked()+ hoursList.get(3).getHoursWorked()
                                + hoursList.get(4).getHoursWorked()+ hoursList.get(5).getHoursWorked()
                                + hoursList.get(6).getHoursWorked();
                
                rowData[i][8] = tot;
                rowData[i][9] = true;
                if(!isApproved[i] && tot==0) rowData[i][9] = false;
            } else{
                rowData[i][1] = "0";
                rowData[i][2] = "0";
                rowData[i][3] = "0";
                rowData[i][4] = "0";
                rowData[i][5] = "0";
                rowData[i][6] = "0";
                rowData[i][7] = "0";
                rowData[i][8] = 0;
                rowData[i][9] = false;
            }          
                
            ++i;
        }
        
        tableModel = new DefaultTableModel(rowData, columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                if(column==8)
                    return false;
                return !isApproved[row];
            }
         };
        jTable1.setModel(tableModel);
        jTable1 = new JTable(tableModel){
            private static final long serialVersionUID = 1L;
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 9:
                        return Boolean.class;
                    default:
                        return String.class;
                }
            }
        }; 
        jScrollPane1.setViewportView(jTable1);
        jTable1.setRowHeight(25);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(130);
        jTable1.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = jTable1.getSelectedRow();
                if(!tableModel.isCellEditable(row, 0)){
                    JOptionPane.showMessageDialog(null, "Hours in this row is already Approved!");
                }
            }
        }); 
        cm.close();
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        uncheckAll = new javax.swing.JRadioButton();
        selectAll = new javax.swing.JRadioButton();
        welcome = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        approveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        projectSelection = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

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
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable1.setName(""); // NOI18N
        jTable1.setRowHeight(25);
        jScrollPane1.setViewportView(jTable1);

        buttonGroup1.add(uncheckAll);
        uncheckAll.setText("Deselect All");
        uncheckAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uncheckAllActionPerformed(evt);
            }
        });

        buttonGroup1.add(selectAll);
        selectAll.setText("Select All");
        selectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(uncheckAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(selectAll, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectAll)
                    .addComponent(uncheckAll))
                .addGap(5, 5, 5))
        );

        welcome.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        welcome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        welcome.setText("Welcome Message");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Approve Worked Hours:  From 08/07/2016 To 08/13/2016");

        approveButton.setText("Approve");
        approveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                approveButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Select Project");

        projectSelection.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        projectSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                projectSelectionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(projectSelection, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 7, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(projectSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jButton1.setText("Previous Week");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Next Week");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(welcome, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cancelButton)
                .addGap(18, 18, 18)
                .addComponent(approveButton)
                .addGap(23, 23, 23))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(welcome, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(approveButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void approveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_approveButtonActionPerformed
        ConnectionManager cm = new ConnectionManager();
        EntityManager em = cm.getEntityManager();
        boolean flag = true;
        for (int i = 0; i < developersList.size(); i++) {
            if(tableModel.getValueAt(i, 9).equals(new Boolean(false))){
                continue;
            }
            flag = false;
            for (int j = 1; j <8 ; j++) {
                int hours = 0;
                try{
                    if(tableModel.getValueAt(i, j).toString()==null ||
                            tableModel.getValueAt(i, j).toString().equals("")){
                        hours =0;                    
                    }
                    else{                        
                        hours = Integer.parseInt(tableModel.getValueAt(i, j).toString());  
                    }                  
                }catch(Exception e){
                    JOptionPane.showMessageDialog(null, "Please enter Integer value for hours");
                    return;
                }

                WorkedHours wh = em.find(WorkedHours.class, selectedProjectId
                        +developersList.get(i)+dates.get(j-1));
                if(wh==null){
                    wh = new WorkedHours(selectedProjectId+developersList.get(i)+dates.get(j-1), 
                        em.find(Employee.class, developersList.get(i)), 
                        developersList.get(i), em.find(Project.class, selectedProjectId),
                        selectedProjectId, hours, dates.get(j-1), true, false, null);
                    em.persist(wh); 
                }
                else{              
                    wh.setHoursWorked(hours);    
                    wh.setIsApproved(true);
                }
            }    
        }
        if(flag){
            JOptionPane.showMessageDialog(null, "You have not selected Hours to approve!");
            return;
        }
        cm.close();
        JOptionPane.showMessageDialog(null, "Hours Approved sucessfully");
        updateTable();
    }//GEN-LAST:event_approveButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        panelHolder.setTitle("Home Page");
        panelHolder.getContentPane().removeAll();
        panelHolder.getContentPane().add(new HomePage(panelHolder, systemData, false));
        panelHolder.getContentPane().revalidate();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void projectSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_projectSelectionActionPerformed
        updateTable();
    }//GEN-LAST:event_projectSelectionActionPerformed

    private void updateTable(){          
        welcome.setText("Welcome "+ systemData.getCurrentUser().getEmployee().getName());
        Calendar calendar = (Calendar)selectedWeekStartDate.clone();
	calendar.set(Calendar.DAY_OF_WEEK, 2);
        Date weekStartDate = new Date(calendar.getTimeInMillis());
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndDate = new Date(calendar.getTimeInMillis());
        jLabel10.setText("Approve Worked Hours: From "+new SimpleDateFormat("MM-dd-yyyy").format(weekStartDate)
                +" To "+new SimpleDateFormat("MM-dd-yyyy").format(weekEndDate));
        
        ConnectionManager cm = new ConnectionManager();
        EntityManager em = cm.getEntityManager();        
        selectedProjectId = projectList.get(model.getIndexOf(model.getSelectedItem())).getId();
                
        Object[] columnNames = new Object[10];
        columnNames[0] ="Developer Name";
        dates = new ArrayList<>();
        for (int i = 7; i >0; ) {
            Date date = new Date(calendar.getTimeInMillis());
            columnNames[i] = new SimpleDateFormat("MM-dd-yyyy").format(date);
            dates.add(0, date);
            --i;
            calendar.add(Calendar.DAY_OF_WEEK, -1);           
        }columnNames[8] = "Total Hours";
        columnNames[9] = "Select";
        
        Query query = em.createQuery("Select pp.personName  from ProjectPerson pp "
                + "where pp.projectID='"+ selectedProjectId +"' and pp.isActivated = 'Yes'");
        developersList = query.getResultList();
        
        Object[][] rowData = new Object[developersList.size()][10];        
        boolean[] isApproved = new boolean[developersList.size()];
        int i =0;
        List<WorkedHours> hoursList;
        for(String developer: developersList){
            Date startDate = new Date(weekStartDate.getTime());
            Date endDate = new Date(weekEndDate.getTime());
            query = em.createQuery("Select wh from WorkedHours wh where wh.projectID= '"+selectedProjectId +"' "   
                    + " and wh.empName='"+developer+"'"  
                    + " and wh.date between '"+startDate+"' and '"+ endDate+"' order by wh.date");
            hoursList = query.getResultList();
            System.out.println("vgvcgv"+hoursList);
            isApproved[i] = (hoursList==null || hoursList.isEmpty()? false: hoursList.get(0).isIsApproved());
            rowData[i][0] = developer;
            if(!hoursList.isEmpty()){
                rowData[i][1] = hoursList.get(0).getHoursWorked();
                rowData[i][2] = hoursList.get(1).getHoursWorked();
                rowData[i][3] = hoursList.get(2).getHoursWorked();
                rowData[i][4] = hoursList.get(3).getHoursWorked();
                rowData[i][5] = hoursList.get(4).getHoursWorked();
                rowData[i][6] = hoursList.get(5).getHoursWorked();
                rowData[i][7] = hoursList.get(6).getHoursWorked();
                int tot = hoursList.get(0).getHoursWorked()+  hoursList.get(1).getHoursWorked()
                                + hoursList.get(2).getHoursWorked()+ hoursList.get(3).getHoursWorked()
                                + hoursList.get(4).getHoursWorked()+ hoursList.get(5).getHoursWorked()
                                + hoursList.get(6).getHoursWorked();
                
                rowData[i][8] = tot;
                rowData[i][9] = true;
                if(!isApproved[i] && tot==0) rowData[i][9] = false;
            } else{
                rowData[i][1] = "0";
                rowData[i][2] = "0";
                rowData[i][3] = "0";
                rowData[i][4] = "0";
                rowData[i][5] = "0";
                rowData[i][6] = "0";
                rowData[i][7] = "0";
                rowData[i][8] = 0;
                rowData[i][9] = false;
            }    
            ++i;
        }
        
        tableModel = new DefaultTableModel(rowData, columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                if(column==8)
                    return false;
                return !isApproved[row];
            }
         };
        jTable1.setModel(tableModel);
        jTable1 = new JTable(tableModel){
            private static final long serialVersionUID = 1L;
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 9:
                        return Boolean.class;
                    default:
                        return String.class;
                }
            }
        }; 
        jScrollPane1.setViewportView(jTable1);
        jTable1.setRowHeight(25);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(130);
        jTable1.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = jTable1.getSelectedRow();
                if(!tableModel.isCellEditable(row, 0)){
                    JOptionPane.showMessageDialog(null, "Hours in this row is already Approved!");
                }
            }
        });        
        cm.close();
    }
    
    private void selectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllActionPerformed
        int i=0;
        for(String Dev: developersList){
            tableModel.setValueAt(true, i, 9);
            tableModel.fireTableDataChanged();
            ++i;
        }
    }//GEN-LAST:event_selectAllActionPerformed

    private void uncheckAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uncheckAllActionPerformed
        int i=0;
        for(String Dev: developersList){
            if(tableModel.isCellEditable(i, 9)){
                tableModel.setValueAt(false, i, 9);                
            }
            tableModel.fireTableDataChanged();
            ++i;
        }
    }//GEN-LAST:event_uncheckAllActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        selectedWeekStartDate.add(Calendar.DAY_OF_WEEK, -7);
        buttonGroup1.clearSelection();
        updateTable();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        selectedWeekStartDate.add(Calendar.DAY_OF_WEEK, 7);
        buttonGroup1.clearSelection();
        updateTable();
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton approveButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox<String> projectSelection;
    private javax.swing.JRadioButton selectAll;
    private javax.swing.JRadioButton uncheckAll;
    private javax.swing.JLabel welcome;
    // End of variables declaration//GEN-END:variables
}
