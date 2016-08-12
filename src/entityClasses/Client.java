package entityClasses;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer number;
    private String name;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String State;
    private int zip;
    private String email;
    private String contact;
    private String invoiceFrequency;   
    private String billingTerm;
    private String invoiceGrouping;

    public Client() {
    }

    public Client(Integer number, String name, String addressLine1, String addressLine2, String city, String State, int zip, String email, String contact, String invoiceFrequency, String billingTerm, String invoiceGrouping) {
        this.number = number;
        this.name = name;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.State = State;
        this.zip = zip;
        this.email = email;
        this.contact = contact;
        this.invoiceFrequency = invoiceFrequency;
        this.billingTerm = billingTerm;
        this.invoiceGrouping = invoiceGrouping;
    } 
    
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getInvoiceFrequency() {
        return invoiceFrequency;
    }

    public void setInvoiceFrequency(String invoiceFrequency) {
        this.invoiceFrequency = invoiceFrequency;
    }

    public String getBillingTerm() {
        return billingTerm;
    }

    public void setBillingTerm(String billingTerm) {
        this.billingTerm = billingTerm;
    }

    public String getInvoiceGrouping() {
        return invoiceGrouping;
    }

    public void setInvoiceGrouping(String invoiceGrouping) {
        this.invoiceGrouping = invoiceGrouping;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (number != null ? number.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the number fields are not set
        if (!(object instanceof Client)) {
            return false;
        }
        Client other = (Client) object;
        if ((this.number == null && other.number != null) || (this.number != null && !this.number.equals(other.number))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
