package Application;

import ProjectLibs.DatabaseAccess;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class App implements AutoCloseable {

    private DatabaseAccess da;

    public App() throws SQLException {
        da = new DatabaseAccess();
        da.opendb("ocps", "app", "megameow");  // open the database
    }

    // check if login credentials are correct
    public Boolean login(String userid, String pwd) throws Exception {
        ResultSet rs = da.getData(
                "user",
                "password",
                "user_id = '" + userid + "'"
        );

        if (!rs.next())  // if result set has 0 rows
            throw new Exception("no such user");

        if (!pwd.equals(rs.getString("password")))  // if password doesn't match
            throw new Exception("wrong password");

        return true;  // 帰りたい
    }

    // get all accounts a user can authorize
    public Vector<Integer> getAccounts(String userid) throws SQLException {
        // retrieve account numbers from db
        ResultSet rs = da.getData("authorizes", "acc_no", "user_id = '" + userid + "'");

        Vector<Integer> a = new Vector<>();   // arraylist to store acc_numbers
        while (rs.next()) {
            a.add(rs.getInt("acc_no")); // add acc_no to the arraylist
        }

        return a;   // 帰りたい
    }

    // authorize a user to make a payment
    public Boolean authorize(String userid, String auth_type, String value) throws Exception {
        ResultSet resultSet = da.getData(
                "auth_keys",
                "value",
                "user_id = " + userid + " and type = " + auth_type
        );

        if (!resultSet.next())
            throw new Exception("No such authorization key");   // if resultSet has no rows

        if (!resultSet.getString("value").equals(value))
            throw new Exception("Wrong authorization key");

        return resultSet.getString("value").equals(value);  // 帰りたい
    }

    // create a user
    public Boolean createUser(String userid, String pwd) throws SQLException {
        da.addRow(
                "user",
                "'" + userid + "', '" + pwd + "'"
        );

        return true;  // 帰りたい
    }

    @Override
    public void close() throws SQLException {
        da.closedb();
    }
}

class TestApp {
    public static void main(String[] args) {

        System.out.println();

        try (App app = new App()) {
            System.out.println(app.login("hope.kartik", "AWUUV@19DEA"));
            System.out.println("it works...");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}