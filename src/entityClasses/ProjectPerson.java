package entityClasses;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class ProjectPerson implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Project projectNumber;
    private Integer projectID;
    @ManyToOne
    private Employee projectPersonName;
    private String personName;
    private String  isActivated;
    private String manager;

    public ProjectPerson() {
    }

    public ProjectPerson(Project projectNumber, Integer projectID, Employee projectPersonName,
            String personName, String  isActivated, String manager) {
        this.projectNumber = projectNumber;
        this.projectID = projectID;
        this.projectPersonName = projectPersonName;
        this.personName = personName;        
        this.isActivated = isActivated;
        this.manager = manager;
    }

    public Project getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(Project projectNumber) {
        this.projectNumber = projectNumber;
    }

    public Employee getProjectPersonName() {
        return projectPersonName;
    }

    public void setProjectPersonName(Employee projectPersonName) {
        this.projectPersonName = projectPersonName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String isIsActivated() {
        return isActivated;
    }

    public void setIsActivated(String  isActivated) {
        this.isActivated = isActivated;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof ProjectPerson)) {
            return false;
        }
        ProjectPerson other = (ProjectPerson) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return personName;
    }
    
}
