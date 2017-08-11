
package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;//DBCP provides our Connection Pool!


public final class DataBase {
	//The actual DB connection!
    private static DataSource dataSource;

    //Class is implemented as singleton,is instantiated only in context (first call)!!!
    private static DataBase  instance = null;//Instance of itself!
    private DataBase(String dbURL, String user, String pwd) {//class controls its instantiation.
    	try {
            //Check if driver exists.
			Class.forName("com.mysql.cj.jdbc.Driver");
			Class.forName("org.apache.commons.dbcp.BasicDataSource");
        } catch (ClassNotFoundException ex) {
            System.out.println("Problem with SQL connection. Drivers do not exist!");
            return;//Abort creation!
        }
    	//Create DataSource(connection with DB!)
    	BasicDataSource ds = new BasicDataSource();
    	ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
    	ds.setUrl(dbURL);
    	ds.setUsername(user);
    	ds.setPassword(pwd);
    	dataSource = (DataSource) ds;
    	System.out.println("DB is instantiated successfully!!!");
    }
    public static DataBase getInstance() {//For simple calls...
        if (DataBase.instance == null) {
            System.out.println("Connection with DB not instantiated!!!Server context wasn't accessed!");
        }
        return DataBase.instance;
    }
    public static DataBase getInstance(String dbURL, String user, String pwd){//For creation only...
        if (DataBase.instance == null) {
            DataBase.instance = new DataBase(dbURL, user,  pwd);
        }else{
        	System.out.println("Connection with DB already exists!");
        }
        return DataBase.instance;
    }

    //With each call to DB, a connection is used from the Connection Pool!!!
    // Returns the results for an SQL SELECT query.
    public ArrayList<HashMap<String, Object>> fetch(String sql) {
    	//For easy Iteration and Selection...
        ArrayList<HashMap<String, Object>> results = new ArrayList<HashMap<String, Object>>();
        
        Connection c = null ;
        try {
        	c = this.dataSource.getConnection();//Get connection for each call to DB!
        	if(c!=null) System.out.println("Connection estabilished!");
        	//TESTING EXTRA PARAMETERS...
            PreparedStatement stmt = c.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
            ResultSet rs = stmt.executeQuery();//Start query execution...
            this.doFetch(rs, results);
            stmt.close();
        } catch (SQLException e) {
        	handleException(e,sql);
        }finally {
        	closeConnection(c);
        }
        return results;
    }
    // Fetches the results from the ResultSet into the given ArrayList.
    private void doFetch(ResultSet rs, ArrayList<HashMap<String, Object>> results) throws SQLException {
    	//GET COLUMNS
        ResultSetMetaData rsmd = rs.getMetaData();//MetaData needed to get column name.
        ArrayList<String> cols = new ArrayList<String>();//Temp
        int numCols = rsmd.getColumnCount();//Number of columns.
        for (int i=1; i<=numCols; i++) {//For each column...
            cols.add(rsmd.getColumnName(i));//add name from metadata to Temp.
        }
        //PROCESS RESULTS
        while (rs.next()) {//For each row...
            HashMap<String, Object> result = new HashMap<String, Object>();
            //map each column name to its Value,through HashMap.
            for (int i=1; i<=numCols; i++) {
                result.put(cols.get(i-1), rs.getObject(i));
            }
            results.add(result);
        }
        rs.close();
    } 

    //Simple execution of statements, returns success or failure code!
    public int execute(String sql) {
        int result = 0;//Failure code.
        Connection c = null ;
        try {
        	c = this.dataSource.getConnection();//Get connection for each call to DB!
        	if(c!=null) System.out.println("Connection estabilished!");
            Statement stmt = c.createStatement();
            result = stmt.executeUpdate(sql);//Execute query...
            stmt.close();
        } catch (SQLException e) {
        	handleException(e,sql);
        }finally {
            closeConnection(c);
        }
        return result;
    }

	/*//Close connection safely!
	private void closeConnection(Connection connection){
		if(connection!=null) try {
			connection.close();//It shouldn't be done on this level! LOGGING ON AND OFF TO DB IS EXPENSIVE!!!
			System.out.println("DB Connection closed successfully!");
		} catch (SQLException e) {
			System.out.println("Problem while closing connection!");
			e.printStackTrace();
		}
	}
	*/
	//Handle exceptions during queries.
	private void handleException(SQLException e, String sql) {
		System.out.println("Problem with SQL Query!");
		System.out.println("SQLException " + e.getErrorCode() + ": " + e.getMessage());
        System.out.println("Statement: " + sql);
    }
}
