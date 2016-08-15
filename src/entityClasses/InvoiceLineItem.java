package entityClasses;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class InvoiceLineItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer invoiceId;
    private String description;
    private int billRate;
    private long hours;
    private long total;

    public InvoiceLineItem() {
    }

    public InvoiceLineItem(Integer invoiceId, String description, int billRate, long hours, long total) {
        this.invoiceId = invoiceId;
        this.description = description;
        this.billRate = billRate;
        this.hours = hours;
        this.total = total;
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

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
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
