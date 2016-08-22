package entityClasses;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class InvoiceLineItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer invoiceId;
    private Date startDate;
    private Date endDate;
    private String description;
    private int billRate;
    private long hours;
    @ManyToOne
    private Project project;
    private Integer projectID;
    private double total;

    public InvoiceLineItem() {
    }

    public InvoiceLineItem(Integer invoiceId, Date startDate, Date endDate, String description, int billRate, long hours, Project project, Integer projectID, double total) {
        this.invoiceId = invoiceId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.billRate = billRate;
        this.hours = hours;
        this.project = project;
        this.projectID = projectID;
        this.total = total;
    }    

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBillRate() {
        return billRate;
    }

    public void setBillRate(int billRate) {
        this.billRate = billRate;
    }

    public long getHours() {
        return hours;
    }

    public void setHours(long hours) {
        this.hours = hours;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
        
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
        if (!(object instanceof InvoiceLineItem)) {
            return false;
        }
        InvoiceLineItem other = (InvoiceLineItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entityClasses.InvoiceLineItem[ id=" + id + " ]";
    }
    
}
