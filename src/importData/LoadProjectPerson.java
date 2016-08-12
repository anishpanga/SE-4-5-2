package importData;

import entityClasses.Employee;
import entityClasses.Project;
import entityClasses.ProjectPerson;
import java.io.File;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;

public class LoadProjectPerson {
    
    public LoadProjectPerson(String filePath){
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
                ProjectPerson projectPerson;
                Scanner line = new Scanner(sc.nextLine());
                line.useDelimiter(",");  
                Project project = em.find(Project.class, line.nextInt());
                String empName = line.next();
                projectPerson = new ProjectPerson(project, project.getId(),  em.find(Employee.class, empName), 
                        empName, "Yes", project.getProjectManager().getName());
                line.close();
                //Load a record
                em.persist(projectPerson);
            }
            sc.close();   
                        
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Something went wrong while Loading ProjectPerson data from CSV file!");
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.getTransaction().commit();
            em.close();
            emf.close();
        }
    }
    
}