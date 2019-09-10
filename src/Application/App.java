package Application;

import ProjectLibs.DatabaseAccess;
import org.json.simple.JSONArray;

import java.sql.ResultSet;
import java.sql.SQLException;

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
                "user_id = "+userid
        );

        if(!rs.next())
            throw new Exception("no such user");

        return pwd.equals(rs.getString("password"));
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
            System.out.println("it works...");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}