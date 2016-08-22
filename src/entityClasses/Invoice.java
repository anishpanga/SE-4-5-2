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
    private double totalAmountDue;
    @ManyToOne
    private Client client;
    private Integer clientNumber;

    public Invoice() {
    }

    public Invoice( Date invoiceDate, Date invoiceStartDate, Date invoiceEndDate, double totalAmountDue, 
            Client client, Integer clientNumber) {        
        this.invoiceDate = invoiceDate;
        this.invoiceStartDate = invoiceStartDate;
        this.invoiceEndDate = invoiceEndDate;
        this.totalAmountDue = totalAmountDue;
        this.client = client;
        this.clientNumber = clientNumber;  
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

    public double getTotalAmountDue() {
        return totalAmountDue;
    }

    public void setTotalAmountDue(double totalAmountDue) {
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
