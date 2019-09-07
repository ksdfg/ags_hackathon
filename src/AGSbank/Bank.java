package AGSbank;

import AGSlibs.DatabaseAccess;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Bank implements AutoCloseable {

    private DatabaseAccess da;

    public Bank() throws SQLException {
        da = new DatabaseAccess();
        da.opendb("ags_bank", "bank", "meow");  // open the database
    }

    // authenticate if credentials of user are correct
    public boolean authenticate(long acc_no, int pin) throws Exception {
        // get result of pin
        ResultSet resultSet = da.getData(
                "customers",
                "pin",
                "acc_no = " + acc_no
        );

        if (!resultSet.next()) {   // if no account
            throw new Exception("Account number " + acc_no + " not in bank");
        }

        int acc_pin = resultSet.getInt("pin");  // retrieve the pin
        return acc_pin == pin;  // check if pin is correct
    }

    public long getAccount(String name) throws Exception{
        ResultSet resultSet = da.getData(
                "customers",
                "acc_no",
                "name = '"+name+"'"
        );

        if(!resultSet.next()){
            throw new Exception("No Account Associated with this name");
        }

        return resultSet.getLong("acc_no");
    }

    public String getName(long acc_no) throws Exception{
        ResultSet resultSet = da.getData(
                "customers",
                "name",
                "acc_no = '"+acc_no+"'"
        );

        if(!resultSet.next()){
            throw new Exception("No Account Associated with this name");
        }

        return resultSet.getString("name");
    }

    public String getTransactions(long send_acc, long recv_acc) throws Exception {
        // get trans_table
        ResultSet resultSet = da.getData(
                "customers", "trans_table as tt",
                "acc_no = " + send_acc
        );

        if (!resultSet.next()) {    // if no such account
            throw new Exception("Account number " + send_acc + " not in bank");
        }

        // get all transactions with recv_acc
        resultSet = da.getData(
                "" + resultSet.getString("tt"),
                "*",
                "acc_no = " + recv_acc
        );

        JSONArray transactions = new JSONArray();
        JSONObject row;
        int i = 0;

        while (resultSet.next() && i++ < 10) {
            row = new JSONObject();
            row.put("trans_id", resultSet.getLong("trans_id"));
            row.put("name", resultSet.getString("name"));
            row.put("amount", resultSet.getDouble("amount"));
            transactions.add(row);
        }

        return transactions.toJSONString();
    }

    // add a transaction to the user
    public void makeTransaction(long send_acc, long recv_acc, long transID, double amount) throws Exception {
        // get current balance and trans_table of acc_no - send
        ResultSet sender = da.getData(
                "customers", "trans_table, balance, name", "acc_no = " + send_acc
        );
        // check if transaction is valid
        if (!sender.next()) {      // if no account
            throw new Exception("Account number not in bank\n");
        }
        else if (sender.getDouble("balance") < amount) {  // banda bohot gareeb
            throw new Exception("Account number " + send_acc + " has insufficient funds\n");
        }

        String send_table = sender.getString("trans_table");
        double send_balance = sender.getDouble("balance");
        String send_name = sender.getString("name");

        // get current balance and trans_table of acc_no - recv
        ResultSet recipient = da.getData(
                "customers", "trans_table, balance, name", "acc_no =" + recv_acc
        );
        // check if transaction is valid
        if (!recipient.next()) {      // if no account
            throw new Exception("Account number not in bank\n");
        }

        String recv_table = recipient.getString("trans_table");
        double recv_balance = recipient.getDouble("balance");
        String recv_name = recipient.getString("name");

        // add transaction to table - send
        da.addRow(
                "" + send_table,
                transID + ", '" + recv_name + "', " + recv_acc + ", " + (-amount)
        );
        // update balance - send
        da.update(
                "customers",
                "acc_no = " + send_acc,
                "balance",
                "" + (send_balance - amount)
        );

        // add transaction to table - recv
        da.addRow(
                "" + recv_table,
                transID + ", '" + send_name + "', " + send_acc +", " + (amount)
        );
        // update balance - recv
        da.update(
                "customers",
                "acc_no = " + recv_acc,
                "balance",
                "" + (recv_balance + amount)
        );
    }

    @Override
    public void close() throws SQLException {
        da.closedb();
    }
}

class TestBank {
    public static void main(String[] args) {

        System.out.println();

        try (Bank bank = new Bank()) {
            System.out.println(bank.authenticate(123456789, 1234));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
