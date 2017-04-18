package util;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
public class AppContextListener implements ServletContextListener {
	//Constructor
    public AppContextListener() {
    }

    //Happens when application is initialized
    public void contextInitialized(ServletContextEvent arg0)  { 
    	//Get values from context,and initialize the utility for DB connection.
    	ServletContext ctx = arg0.getServletContext();//Get context.
    	//Get init Parameters.
    	String URL = ctx.getInitParameter("dbURL");
        String user = ctx.getInitParameter("dbUser");
        String pwd = ctx.getInitParameter("dbPassword");
    	DataBase DB = DataBase.getInstance(URL,user,pwd);//Instantiate DataBase!
    	ctx.setAttribute("DataBase", DB);//Set DataBase as global Attribute  	
    }	
    
    //Happens when application is ended
    public void contextDestroyed(ServletContextEvent arg0)  { 
    }

}
