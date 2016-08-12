package entityClasses;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    @ManyToOne
    private Client client;
    private String startDate;
    private String endDate;    
    private String status;  
    @ManyToOne
    private Employee projectManager;
    private String managerName;
    private String clientContact;
    private int budget;

    public Project() {
    }

    public Project(Client client, Integer id, String name, String startDate,
            String endDate, String status, Employee projectManager, String  managerName,
            String clientContact, int budget) {
        this.id = id;
        this.name = name;
        this.client = client;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.projectManager = projectManager;
        this.managerName = managerName;
        this.clientContact = clientContact;
        this.budget = budget;
    }

    public Integer getId() {
        return id;
    }   
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Employee getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(Employee projectManager) {
        this.projectManager = projectManager;
    }

    public String getClientContact() {
        return clientContact;
    }

    public void setClientContact(String clientContact) {
        this.clientContact = clientContact;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }   

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Project)) {
            return false;
        }
        Project other = (Project) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id +"";
    }
    
}
