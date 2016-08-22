package jUnit;

import entityClasses.Company;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

class TestRunnerForCompany{
    public static void main(String[] args) {
      Result result = JUnitCore.runClasses(CompanyDetailsTest.class);
		
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
		
      System.out.println(result.wasSuccessful());
   }
    
}

public class CompanyDetailsTest {
    
    @Test
    public void testCompany(){
        Company company = new Company();     
        String name = "Eagle Consulting Services";
        String addressLine1 = "2501 E. Memorial Rd";
        String city = "Edmond";
        String state = "OK";
        int zip = 73013;
        company.setName(name);
        company.setAddressLine1(addressLine1);
        company.setCity(city);
        company.setState(state);
        company.setZip(zip);
        
        
        Assert.assertEquals(name, company.getName());
        Assert.assertEquals(addressLine1, company.getAddressLine1());
        Assert.assertEquals(city, company.getCity());
        Assert.assertEquals(state, company.getState());
        Assert.assertEquals(zip, company.getZip());
    }
}


