package importData;

import entityClasses.Client;
import entityClasses.Employee;
import entityClasses.Project;
import java.io.File;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;

public class LoadProject {
    
    public LoadProject(String filePath){
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
                Project project;
                Scanner line = new Scanner(sc.nextLine());
                line.useDelimiter(",");  
                Employee emp;
                project = new Project(em.find(Client.class, line.nextInt()), line.nextInt(), 
                    line.next(), line.next(), line.next(), line.next(), 
                    emp = em.find(Employee.class, line.next()), emp.getName(),
                    line.next(), line.nextInt());
                line.close();
                //Load a record
                em.persist(project);
            }
            sc.close();   
                        
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Something went wrong while Loading Project data from CSV file!");
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.getTransaction().commit();
            em.close();
            emf.close();
        }
    }
    
}