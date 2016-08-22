package jUnit;

import entityClasses.Project;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

class TestRunnerForProject{
    public static void main(String[] args) {
      Result result = JUnitCore.runClasses(ProjectTest.class);
		
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
		
      System.out.println(result.wasSuccessful());
   }
    
}


public class ProjectTest {
    
    @Test
    public void testProject(){
        Project project = new Project();
        String name = "E-Commerce";    
        project.setName(name);    
        String startDate = "3/7/16";
        String endDate = "5/2/16";
        String status = "Closed";  
        project.setStatus(status);                  
        String  managerName = "Amber Monarrez";
        project.setManagerName(managerName);
        String clientContact = "Lenna Paprocki";
        project.setClientContact(clientContact);
        int budget = 80000;
        project.setBudget(budget);
        
        
        
        Assert.assertEquals(name, project.getName());
        Assert.assertEquals(managerName, project.getManagerName());
        Assert.assertEquals(clientContact, project.getClientContact());
        Assert.assertEquals(status, project.getStatus());
        Assert.assertEquals(budget, project.getBudget());
    }
}


