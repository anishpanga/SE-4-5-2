package importData;

import entityClasses.Client;
import entityClasses.Project;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class ComputeNextInvoiceDate {
    public ComputeNextInvoiceDate(){
        ConnectionManager cm = new ConnectionManager();
        EntityManager em = cm.getEntityManager();
        Query query = em.createQuery("Select p  from Project p where p.status = 'In Progress'");
        List<Project> projectList = query.getResultList();
//        System.out.println(projectList);
        
        Date today = new Date(Calendar.getInstance().getTimeInMillis());
//        System.out.println(today);
        for (Project project : projectList) {
            Date startDate = new Date(new java.util.Date(project.getStartDate()).getTime());
            Date endDate = new Date(new java.util.Date(project.getEndDate()).getTime());
                        
            Calendar firstDayOfStartWeek = Calendar.getInstance();
            firstDayOfStartWeek.setTime(startDate);
            firstDayOfStartWeek.set(Calendar.DAY_OF_WEEK, 1);
            Client client = project.getClient();
            Calendar nextInvoiceDate = (Calendar) firstDayOfStartWeek.clone();
            
            Date dateOfNextInvoice= null;
            while(dateOfNextInvoice== null || ( dateOfNextInvoice.before(today) && 
                    dateOfNextInvoice.before(endDate))  ){   
                if(!client.getInvoiceFrequency().equals("Monthly-Cal")){
                    nextInvoiceDate.set(Calendar.DAY_OF_WEEK, 1);
                }
            
                if(client.getInvoiceFrequency().equals("Weekly")){
                    nextInvoiceDate.add(Calendar.DAY_OF_WEEK, 7);
                }
                else if(client.getInvoiceFrequency().equals("BiWeekly")){
                    nextInvoiceDate.add(Calendar.DAY_OF_WEEK, 14);
                }
                else if(client.getInvoiceFrequency().equals("Monthly")){
                    nextInvoiceDate.add(Calendar.DAY_OF_WEEK, 28);
                }
                else if(client.getInvoiceFrequency().equals("Monthly-Cal")){
                    nextInvoiceDate.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                    nextInvoiceDate.add(Calendar.MONTH, 1);
                    nextInvoiceDate.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);               
                }
                
                if(!client.getInvoiceFrequency().equals("Monthly-Cal")){
                    nextInvoiceDate.add(Calendar.DAY_OF_WEEK, 3);
                }
                dateOfNextInvoice = new Date(nextInvoiceDate.getTimeInMillis());   
//                System.out.println(dateOfNextInvoice);
                
            } 
//            System.out.print(new Date(firstDayOfStartWeek.getTimeInMillis())+"----");
//            System.out.println(dateOfNextInvoice);
//            System.out.println("=================");
            project.setNextInvoiceDate(dateOfNextInvoice);
        }
        
        cm.close();
    }    
    public static void main(String[] args) {
        new ComputeNextInvoiceDate();
    }
}
