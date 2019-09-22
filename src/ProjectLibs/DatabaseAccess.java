package ProjectLibs;

import java.sql.*;

public class DatabaseAccess {
    private Connection connect = null;    // is the connection to the db
    private Statement statement = null;   // use this to fire queries etc.

    public void opendb(String dbName, String uname, String upass) throws SQLException {
        //if any other db is open, close it
        if (connect != null) {
            connect.close();
        }
        // Setup the connection with the DB
        connect = DriverManager.getConnection("jdbc:mysql://localhost/" + dbName + "?user=" + uname +
                "&password=" + upass);

        // Statements allow to issue SQL queries to the database
        statement = connect.createStatement();
    }

    public void closedb() throws SQLException {
        connect.close();    //close the connection
        connect = null;     //set it back to null
    }

    //fire a query and get result
    public ResultSet getData(String tablename, String columns) throws SQLException {
        ResultSet resultSet;     //will return null in case of error

        System.out.println("Select " + columns + " from " + tablename);   // for monitoring
        //fire query
        resultSet = statement.executeQuery(
                "Select " + columns + " from " + tablename
        );

        return resultSet;   //returning the result of query
    }

    // same as above, but with conditions - overloaded
    public ResultSet getData(String tablename, String columns, String conditions) throws SQLException {
        ResultSet resultSet;     //will return null in case of error

        System.out.println("Select " + columns + " from " + tablename + " where " + conditions);  // for monitoring
        //fire query
        resultSet = statement.executeQuery(
                "Select " + columns + " from " + tablename + " where " + conditions
        );

        return resultSet;   //returning the result of query
    }

    // same as above, but with join - overloaded
    public ResultSet getData(String tables, String joinOn, String columns, String conditions) throws SQLException {
        ResultSet resultSet;     //will return null in case of error
        // formulate query
        String query = "Select " + columns
                + " from " + tables.replace(", ", " join ")
                + " on " + joinOn
                + " where " + conditions;

        System.out.println(query);  // for monitoring
        //fire query
        resultSet = statement.executeQuery(
                query
        );

        return resultSet;   //returning the result of query
    }

    // insert a row into a table
    public void addRow(String tablename, String values) throws SQLException {
        System.out.println("insert into " + tablename + " values (" + values + ")");    // for monitoring
        // run insert stmt
        statement.executeUpdate(
                "insert into " + tablename + " values (" + values + ")"
        );
    }

    // delete a row from a table
    public void deleteRow(String tablename, String conditions) throws SQLException {
        System.out.println();
        // run delete stmt
        statement.executeUpdate(
                "delete from " + tablename + " where " + conditions
        );
    }

    // update value in table
    public void update(String tablename, String conditions, String field, String newValue) throws SQLException {
        // run update stmt
        statement.executeUpdate(
                "update " + tablename + " set " + field + " = " + newValue + " where " + conditions
        );
    }
}
