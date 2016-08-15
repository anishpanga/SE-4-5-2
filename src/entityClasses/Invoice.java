package entityClasses;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Date invoiceDate;    
    private Date invoiceStartDate;
    private Date invoiceEndDate;
    private long totalAmountDue;
    @ManyToOne
    private Client client;
    private Integer clientNumber;
    @ManyToOne
    private Project project;
    private Integer projectId;

    public Invoice() {
    }

    public Invoice( Date invoiceDate, Date invoiceStartDate, Date invoiceEndDate, long totalAmountDue, 
            Client client, Integer clientNumber, Project project, Integer projectId) {        
        this.invoiceDate = invoiceDate;
        this.invoiceStartDate = invoiceStartDate;
        this.invoiceEndDate = invoiceEndDate;
        this.totalAmountDue = totalAmountDue;
        this.client = client;
        this.clientNumber = clientNumber;
        this.project = project;
        this.projectId = projectId;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public long getTotalAmountDue() {
        return totalAmountDue;
    }

    public void setTotalAmountDue(long totalAmountDue) {
        this.totalAmountDue = totalAmountDue;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Integer getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(Integer clientNumber) {
        this.clientNumber = clientNumber;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Date getInvoiceStartDate() {
        return invoiceStartDate;
    }

    public void setInvoiceStartDate(Date invoiceStartDate) {
        this.invoiceStartDate = invoiceStartDate;
    }

    public Date getInvoiceEndDate() {
        return invoiceEndDate;
    }

    public void setInvoiceEndDate(Date invoiceEndDate) {
        this.invoiceEndDate = invoiceEndDate;
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
        if (!(object instanceof Invoice)) {
            return false;
        }
        Invoice other = (Invoice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entityClasses.Invoice[ id=" + id + " ]";
    }
    
}
