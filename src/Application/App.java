package Application;

import ProjectLibs.DatabaseAccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class App implements AutoCloseable {

    private DatabaseAccess da;

    public App() throws SQLException {
        da = new DatabaseAccess();
        da.opendb("ocps", "app", "megameow");  // open the database
    }

    // check if login credentials are correct
    public boolean login(String userid, String pwd) throws Exception {
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
    public boolean authorize(String userid, String auth_type, String value) throws Exception {
        ResultSet resultSet = da.getData(
                "auth_keys",
                "value",
                "user_id = '" + userid + "' and type = '" + auth_type + "'"
        );

        if (!resultSet.next())
            throw new Exception("No such authorization key");   // if resultSet has no rows

        if (!resultSet.getString("value").equals(value))
            throw new Exception("Wrong authorization key");

        return resultSet.getString("value").equals(value);  // 帰りたい
    }

    // create a user
    public boolean createUser(String name, String userid, String pwd) throws SQLException {
        da.addRow(
                "user",
                "'" + userid + "', '" + name + "', '" + pwd + "'"
        );

        return true;  // 帰りたい
    }

    // link bank account to user
    public boolean linkAccount(String userid, int acc) throws SQLException {
        da.addRow(
                "authorizes",
                "'" + userid + "', " + acc
        );

        return true;  // 帰りたい
    }

    public boolean unlinkAccount(String userid, int acc) throws SQLException {
        da.deleteRow(
                "authorizes",
                "user_id = '" + userid + "' and acc_no = " + acc
        );

        return true;  // 帰りたい
    }

    // link bank account to user
    public boolean addBio(String userid, String type, String val) throws SQLException {
        da.addRow(
                "auth_keys",
                "'" + userid + "', '" + type + "', '" + val + "'"
        );

        return true;  // 帰りたい
    }

    public ArrayList getBios(String userid) throws Exception {
        ArrayList<String> bios = new ArrayList<>(); // to store all the types that have a value

        ResultSet resultSet = da.getData("auth_keys", "type", "user_id = '" + userid + "'");

        if (!resultSet.next())  // if no biometrics are linked
            throw new Exception("No biometrics linked :(");

        //  add bios to arraylist
        do {
            bios.add(resultSet.getString("type") + " recognition");
        } while (resultSet.next());

        return bios;  // 帰りたい
    }

    public boolean rmBio(String userid, String type) throws SQLException {
        da.deleteRow(
                "auth_keys",
                "user_id = '" + userid + "' and type = '" + type + "'"
        );

        return true;  // 帰りたい
    }

    public boolean chgName(String userid, String name) throws SQLException {
        da.update("user", "user_id = '" + userid + "'", "name", "'" + name + "'");
        return true;  // 帰りたい
    }

    public boolean chgPWD(String userid, String pwd) throws SQLException {
        da.update("user", "user_id = '" + userid + "'", "password", "'" + pwd + "'");
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