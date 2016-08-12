package test;

import entityClasses.Company;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class TestCompanyOld {
    
    public static void main(String args[]){
        Company company = new Company();
        company.setName("Eagle Consluting");
        company.setAddressLine1("2501 E. Memorial Rd");
        company.setAddressLine2("");
        company.setCity("Edmond");  
        company.setState("OK");  
        company.setZip(73013);  
        
        TestCompanyOld demo = new TestCompanyOld();
        demo.persist(company);
    }

    public void persist(Object object) {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("InvoiceGenerationPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
}