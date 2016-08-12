package importData;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ConnectionManager {
    private EntityManagerFactory emf;
    private EntityManager entityManager;

    public ConnectionManager() {
        emf = javax.persistence.Persistence.createEntityManagerFactory("InvoiceGenerationPU");
        entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
      
    public void close(){
      entityManager.getTransaction( ).commit( );
      entityManager.close( );
      emf.close( );
    }
        
}
