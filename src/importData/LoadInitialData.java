package importData;

import importData.LoadClient;
import importData.LoadCompany;
import importData.LoadEmployee;
import importData.LoadProject;


public class LoadInitialData {
    public static void main(String args[]){        
        new LoadCompany("data/company_data.csv");
        new LoadClient("data/client_data.csv");
        new LoadEmployee("data/people_data.csv");
        new LoadProject("data/project_data.csv");   
        new LoadProjectPerson("data/project_person.csv");  
        new ComputeNextInvoiceDate();
    }
    
}
