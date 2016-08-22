package jUnit;

import entityClasses.Employee;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


class TestRunnerForEmployee{
    public static void main(String[] args) {
      Result result = JUnitCore.runClasses(EmployeeTest.class);
		
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
		
      System.out.println(result.wasSuccessful());
   }
    
}

public class EmployeeTest {
    
    @Test
    public void testEmployee(){
        Employee emp = new Employee();     
        String name = "Andra Scheyer";
        String title = "Developer";
        int billRate =  90;
        String role = "Graphic Artist";
        emp.setName(name);
        emp.setTitle(title);
        emp.setBillRate(billRate);
        emp.setEmpRole(role);
                
        
        Assert.assertEquals(name, emp.getName());
        Assert.assertEquals(title, emp.getTitle());
        Assert.assertEquals(billRate, emp.getBillRate());
        Assert.assertEquals(role, emp.getEmpRole());
    }
}


