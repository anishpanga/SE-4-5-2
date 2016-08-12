package screens;

import manage.ApproveWorkedHours;
import manage.ManageDevelopers;
import maintenance.EmployeeMaintenance;
import maintenance.ClientMaintenance;
import maintenance.ProjectMaintenance;
import maintenance.CompanyMaintenance;
import entityClasses.Company;
import importData.ConnectionManager;
import importData.SystemData;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class HomePage extends javax.swing.JPanel {
    JFrame  panelHolder;
    SystemData systemData;
    
    public HomePage(JFrame  panelHolder, SystemData systemData, boolean isFirstLogin) {
        this.panelHolder = panelHolder;
        this.systemData = systemData;        
        initComponents();    
        ConnectionManager cm = new ConnectionManager();
        EntityManager em = cm.getEntityManager();
        Query query = em.createQuery("Select c  from Company c");
        List<Company> list = query.getResultList();
        Company comp = list.get(0);       
        
        //name.setText(comp.getName());
        addressLine.setText("<html>"+ "Address:"
                +"<br>"+comp.getAddressLine1()
                +(comp.getAddressLine2()==null || comp.getAddressLine2().trim().equals("") 
                        ? "":", "+ comp.getAddressLine2()    )          
                +"<br>"+comp.getCity()+", "+comp.getState() + " " + comp.getZip()
                + "</html>");
        
        cm.close();
        
                
        JMenuBar menuBar = new JMenuBar();
        panelHolder.setJMenuBar(menuBar);

        JMenu mnMaintain = new JMenu("Maintenance");
        menuBar.add(mnMaintain);        
        if(systemData.getCurrentUser().getEmployeeType().equals("Developer") ){
            mnMaintain.setEnabled(false);
        }
        
        if(systemData.getCurrentUser().getEmployeeType().equals("Accountant") ){
            JMenuItem mntmCompany = new JMenuItem("Company");
            mnMaintain.add(mntmCompany);        
            mntmCompany.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    panelHolder.setTitle("Company Maintenance");
                    panelHolder.getContentPane().removeAll();
                    panelHolder.getContentPane().add(new CompanyMaintenance(panelHolder, systemData));
                    panelHolder.getContentPane().revalidate();        
                }
            }); 
        }
        
        if(systemData.getCurrentUser().getEmployeeType().equals("Accountant") ){
            JMenuItem mntmClient = new JMenuItem("Client");
            mnMaintain.add(mntmClient);
            mntmClient.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    panelHolder.setTitle("Client Maintenance");
                    panelHolder.getContentPane().removeAll();
                    panelHolder.getContentPane().add(new ClientMaintenance(panelHolder, systemData));
                    panelHolder.getContentPane().revalidate();        
                }
            });
        }
          
        if(systemData.getCurrentUser().getEmployeeType().equals("Project Manager") ){
            JMenuItem mntmProject = new JMenuItem("Project");
            mnMaintain.add(mntmProject);
            mntmProject.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    panelHolder.setTitle("Project Maintenance");
                    panelHolder.getContentPane().removeAll();
                    panelHolder.getContentPane().add(new ProjectMaintenance(panelHolder, systemData));
                    panelHolder.getContentPane().revalidate();        
                }
            });             
        }
        
        if(systemData.getCurrentUser().getEmployeeType().equals("Accountant") ){
            JMenuItem mntmEmployee = new JMenuItem("Employee");
            mnMaintain.add(mntmEmployee);
            mntmEmployee.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    panelHolder.setTitle("Employee Maintenance");
                    panelHolder.getContentPane().removeAll();
                    panelHolder.getContentPane().add(new EmployeeMaintenance(panelHolder, systemData));
                    panelHolder.getContentPane().revalidate();        
                }
            });
        }
        
        JMenu mnInvoice = new JMenu("Invoice");
        menuBar.add(mnInvoice);
        JMenuItem mntmGenerateInvoice = new JMenuItem("Generate / Mail");
        mnInvoice.add(mntmGenerateInvoice);
        mntmGenerateInvoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                panelHolder.setTitle("Import Student");
//                panelHolder.getContentPane().removeAll();
//		panelHolder.getContentPane().add(new GenerateInvoice(panelHolder, systemData));
//		panelHolder.getContentPane().revalidate();        
            }
        });
        
        if(systemData.getCurrentUser().getEmployeeType().equals("Developer") ||
            systemData.getCurrentUser().getEmployeeType().equals("Project Manager")   ){
            mnInvoice.setEnabled(false);
        }
        
        JMenuItem mntmNewMenuItem_3 = new JMenuItem("View / Save as PDF");
        mnInvoice.add(mntmNewMenuItem_3);
        mntmNewMenuItem_3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                panelHolder.setTitle("Save as PDF");
//                panelHolder.getContentPane().removeAll();
//		panelHolder.getContentPane().add(new SaveInvoice(panelHolder, systemData));
//		panelHolder.getContentPane().revalidate();        
            }
        });                
        
        JMenu mnReport = new JMenu("Report");
        menuBar.add(mnReport);
        JMenuItem mntmGenerateSchedule = new JMenuItem("Available Employees");
        mnReport.add(mntmGenerateSchedule);
        mntmGenerateSchedule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                panelHolder.setTitle("Available Employees");
//                panelHolder.getContentPane().removeAll();
//		panelHolder.getContentPane().add(new AvailableEmployees(panelHolder, systemData));
//		panelHolder.getContentPane().revalidate();        
            }
        });
        JMenuItem mntmNewMenuItem = new JMenuItem("Worked Hours");
        mnReport.add(mntmNewMenuItem);
        mntmNewMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                panelHolder.setTitle("Hours Clocked");
//                panelHolder.getContentPane().removeAll();
//		panelHolder.getContentPane().add(new HoursClocked(panelHolder, systemData));
//		panelHolder.getContentPane().revalidate();        
            }
        });
        JMenuItem mntmProjectReport= new JMenuItem("Project Report");
        mnReport.add(mntmProjectReport);
        mntmProjectReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                panelHolder.setTitle("Project Report");
//                panelHolder.getContentPane().removeAll();
//		panelHolder.getContentPane().add(new ProjectReport(panelHolder, systemData));
//		panelHolder.getContentPane().revalidate();        
            }
        });

        JMenu mnManage = new JMenu("Manage");
        menuBar.add(mnManage);        
        if(!systemData.getCurrentUser().getEmployeeType().equals("Project Manager")   ){
            mnManage.setEnabled(false);
        }
        
        JMenuItem mntmNewMenuItem_1 = new JMenuItem("Manage Developers");
        mnManage.add(mntmNewMenuItem_1);
        mntmNewMenuItem_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
                panelHolder.setTitle("Manage Developers");
                panelHolder.getContentPane().removeAll();
		panelHolder.getContentPane().add(new ManageDevelopers(panelHolder, systemData));
		panelHolder.getContentPane().revalidate();        
            }
        });        
               
        JMenuItem mntmNewMenuItem_2 = new JMenuItem("Approve Worked Hours");
        mnManage.add(mntmNewMenuItem_2);
        mntmNewMenuItem_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
                panelHolder.setTitle("Approve Worked Hours");
                panelHolder.getContentPane().removeAll();
		panelHolder.getContentPane().add(new ApproveWorkedHours(panelHolder, systemData));
		panelHolder.getContentPane().revalidate();        
            }
        });
                      
                
        JMenu mnCurUser = new JMenu(systemData.getCurrentUser().getEmployee().getName());
        menuBar.add(mnCurUser);
        
        if(!systemData.getCurrentUser().getEmployeeType().equals("Accountant") ){//user is not accountant
            JMenuItem mntmClockHours = new JMenuItem("Clock Hours");
            mnCurUser.add(mntmClockHours);
            mntmClockHours.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    panelHolder.setTitle("Clock Hours");
                    panelHolder.getContentPane().removeAll();
                    panelHolder.getContentPane().add(new ClockHours(panelHolder, systemData));
                    panelHolder.getContentPane().revalidate();      
                }
            }); 
        }        
        
        JMenuItem mntmChangePassword = new JMenuItem("Change Password");
        mnCurUser.add(mntmChangePassword);
        mntmChangePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelHolder.setTitle("Change Password");
                panelHolder.getContentPane().removeAll();
		panelHolder.getContentPane().add(new ChangePassword(panelHolder, systemData));
		panelHolder.getContentPane().revalidate();      
            }
        });   
        
        JMenuItem mntmLogout = new JMenuItem("Logout");
        mnCurUser.add(mntmLogout);
        mntmLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelHolder.setTitle("Login Page");
                panelHolder.setJMenuBar(null);
                panelHolder.getContentPane().removeAll();                                
		panelHolder.getContentPane().add(new LogInPanel(panelHolder));
		panelHolder.getContentPane().revalidate();      
            }
        });     
      
                
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        name = new javax.swing.JLabel();
        addressLine = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("Calibri", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Welcome to Invoice Generation System");

        name.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        name.setText("Eagle Consulting Services");

        addressLine.setText("2501 E. Memorial Rd");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addressLine, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addressLine, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(40, 40, 40)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>                        


    // Variables declaration - do not modify                     
    private javax.swing.JLabel addressLine;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel name;
    // End of variables declaration                   
}
