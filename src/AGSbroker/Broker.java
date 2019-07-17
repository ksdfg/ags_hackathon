package AGSbroker;

import AGSlibs.DatabaseAccess;

import java.sql.ResultSet;
import java.lang.AutoCloseable;
import java.sql.SQLException;

public class Broker implements AutoCloseable {

    private DatabaseAccess da;

    public Broker() throws SQLException {
        da = new DatabaseAccess();
        da.opendb("ags_broker", "broker", "megameow");  // open the database
    }

    public boolean findDevice(long acc_no, String deviceID) throws SQLException {
        // get list of device_ids linked to given acc_no
        ResultSet resultSet = da.getData("linked_device_list",
                "device_id", "acc_no = " + acc_no);

        while (resultSet.next()) {
            if (resultSet.getString(1).equals(deviceID)) {
                return true;    // return true if device_id in result set
            }
        }

        return false;   // return false if not in result set
    }

    public void addDevice(long acc_no, String deviceID) throws SQLException {
        da.addRow("linked_device_list",
                acc_no + ", '" + deviceID + "'");
    }

    public void removeDevice(long acc_no, String deviceID) throws SQLException {
        da.deleteRow("linked_device_list",
                "acc_no = " + acc_no + " and device_id = '" + deviceID + "'");
    }

    @Override
    public void close() throws SQLException {
        da.closedb();
    }
}

class TestBroker {
    public static void main(String[] args) {

        System.out.println();

        try (Broker broker = new Broker()) {
            System.out.println(broker.findDevice(123456789, "vc:sd:qw:re:qw"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}