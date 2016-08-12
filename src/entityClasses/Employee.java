
package entityClasses;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String name;
    private String title;
    private int billRate;
    private String EmpRole;

    public Employee(String name, String title, int billRate, String role) {
        this.name = name;
        this.title = title;
        this.billRate = billRate;
        this.EmpRole = role;
    }

    public Employee() {
    }
       
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBillRate() {
        return billRate;
    }

    public void setBillRate(int billRate) {
        this.billRate = billRate;
    }

    public String getEmpRole() {
        return EmpRole;
    }

    public void setEmpRole(String EmpRole) {
        this.EmpRole = EmpRole;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
