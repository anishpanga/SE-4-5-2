package importData;

import entityClasses.Client;
import entityClasses.Employee;
import entityClasses.Project;
import entityClasses.WorkedHours;
import java.io.File;
import java.sql.Date;
import java.util.Calendar;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;

public class LoadHours {
    
    public LoadHours(String filePath){
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("InvoiceGenerationPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();       
         
        try {
            Scanner sc = new Scanner(new File(filePath));
            
            // Skip the header
            if (sc.hasNextLine()) {
                sc.nextLine();
            }
            
            //Read a single record
            while (sc.hasNextLine()) {
                WorkedHours wh;
                Scanner line = new Scanner(sc.nextLine());
                line.useDelimiter(",");  
                
                Calendar cal = Calendar.getInstance();
                cal.set(2016, 5, 27);
                
//                while(line.hasNext())
//                    System.out.print(line.next()+"-");
                
                int projectId = Integer.parseInt(line.next());
                String empName = line.next();
                while(line.hasNext()){
                    String s = line.next();
                    if(!s.equals("")){
                        Date date = new Date(cal.getTimeInMillis());
//                        System.out.println("date"+date);
                        int hours = Integer.parseInt(s);
                        wh =  new WorkedHours(projectId+ empName +date, em.find(Employee.class, empName), 
                        empName, em.find(Project.class, projectId),
                        projectId, hours, date, false, false, null);
                        
                        em.persist(wh);
                    }
                    cal.add(Calendar.DAY_OF_WEEK, 1); 
                }
                
                line.close();
            }
            sc.close();  
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Something went wrong while Loading Hours from CSV file!");
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.getTransaction().commit();
            em.close();
            emf.close();
        }
    }  
    
}