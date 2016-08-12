package entityClasses;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class WorkedHours implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    @ManyToOne
    Employee employee;
    String empName;
    @ManyToOne
    Project project;
    Integer projectID;
    int hoursWorked;
    Date date;
    boolean isApproved;
    boolean isInvoiced;
    Integer includedInInvoiceNumber;

    public WorkedHours() {
    }

    public WorkedHours(String id, Employee employee, String empName, Project project, Integer projectID, 
            int hoursWorked, Date date, boolean isApproved, boolean isInvoiced, Integer includedInInvoiceNumber) {
        this.id = id;
        this.employee = employee;
        this.empName = empName;
        this.project = project;
        this.projectID = projectID;
        this.hoursWorked = hoursWorked;
        this.date = date;
        this.isApproved = isApproved;
        this.isInvoiced = isInvoiced;
        this.includedInInvoiceNumber = includedInInvoiceNumber;
    }

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isIsApproved() {
        return isApproved;
    }

    public void setIsApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public boolean isIsInvoiced() {
        return isInvoiced;
    }

    public void setIsInvoiced(boolean isInvoiced) {
        this.isInvoiced = isInvoiced;
    }

    public Integer getIncludedInInvoiceNumber() {
        return includedInInvoiceNumber;
    }

    public void setIncludedInInvoiceNumber(Integer includedInInvoiceNumber) {
        this.includedInInvoiceNumber = includedInInvoiceNumber;
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
        if (!(object instanceof WorkedHours)) {
            return false;
        }
        WorkedHours other = (WorkedHours) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entityClasses.WorkedHours[ id=" + id + " ]";
    }
    
}
