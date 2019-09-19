package ProjectLibs;

import java.sql.*;

public class DatabaseAccess {
    protected Connection connect = null;    // is the connection to the db
    protected Statement statement = null;   // use this to fire queries etc.

    public void opendb(String dbName, String uname, String upass) throws SQLException {
        try {
            //if any other db is open, close it
            if (connect != null){
                connect.close();
            }
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://localhost/" + dbName + "?user=" + uname +
                    "&password=" + upass);

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
        }
        catch (Exception e){
            throw e;
        }
    }

    public void closedb() throws SQLException {
        try {
            connect.close();    //close the connection
            connect = null;     //set it back to null
        }
        catch (Exception e){
            throw e;
        }
    }

    //fire a query and get result
    public ResultSet getData(String tablename, String columns) throws SQLException {
        ResultSet resultSet = null;     //will return null in case of error
        try {
            //fire query
            resultSet = statement.executeQuery(
                    "Select "+columns+" from "+tablename
                    );
        }
        catch (Exception e){
            throw e;
        }
        return resultSet;   //returning the result of query
    }

    // same as above, but with conditions - overloaded
    public ResultSet getData(String tablename, String columns, String conditions) throws SQLException {
        ResultSet resultSet = null;     //will return null in case of error
        try {
            //fire query
            System.out.println("Select "+columns+" from "+tablename+" where "+conditions);
            resultSet = statement.executeQuery(
                    "Select "+columns+" from "+tablename+" where "+conditions
            );
        }
        catch (Exception e){
            throw e;
        }
        return resultSet;   //returning the result of query
    }

    // same as above, but with join - overloaded
    public ResultSet getData(String tables, String joinOn, String columns, String conditions) throws SQLException {
        ResultSet resultSet = null;     //will return null in case of error
        try {
            //fire query
            resultSet = statement.executeQuery(
                    "Select "+columns
                            +" from "+tables.replace(", ", " join ")
                            +" on "+joinOn
                            +" where "+conditions
            );
        }
        catch (Exception e){
            throw e;
        }
        return resultSet;   //returning the result of query
    }

    // insert a row into a table
    public void addRow(String tablename, String values) throws SQLException {
        try {
            // run insert stmt
            statement.executeUpdate(
                    "insert into "+tablename+" values ("+values+")"
            );
        }
        catch (Exception e){
            throw e;
        }
    }

    // delete a row from a table
    public void deleteRow(String tablename, String conditions) throws SQLException {
        try {
            // run delete stmt
            statement.executeUpdate(
                    "delete from "+tablename+" where "+conditions
            );
        }
        catch (Exception e){
            throw e;
        }
    }

    // update value in table
    public void update(String tablename, String conditions, String field, String newValue) throws SQLException {
        try {
            // run update stmt
            statement.executeUpdate(
                    "update "+tablename+" set "+field+" = "+newValue+"where "+conditions
            );
        }
        catch (Exception e){
            throw e;
        }
    }
}
