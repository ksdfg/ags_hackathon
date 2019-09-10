package Bank;

import ProjectLibs.DatabaseAccess;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Random;

import static java.lang.Math.abs;

public class Bank implements AutoCloseable {

    private DatabaseAccess da;

    public Bank() throws SQLException {
        da = new DatabaseAccess();
        da.opendb("ocps", "bank", "meow");  // open the database
    }

    // authenticate if credentials of user are correct
    public boolean authenticate(long acc_no, int pin) throws Exception {
        // get result of pin
        ResultSet resultSet = da.getData(
                "account",
                "PIN",
                "acc_no = " + acc_no
        );

        if (!resultSet.next()) {   // if no account
            throw new Exception("Account number " + acc_no + " not in bank");
        }

        int acc_pin = resultSet.getInt("PIN");  // retrieve the pin
        return acc_pin == pin;  // check if pin is correct
    }

    public String getTransactions(long acc) throws Exception {
        // get trans_table

        return "";
    }

    // add a transaction to the user
    public void makeTransaction(long send_acc, long recv_acc, double amount) throws Exception {
        // get current balance and trans_table of acc_no - send
        ResultSet sender = da.getData(
                "account", "balance, name", "acc_no = " + send_acc
        );
        // check if transaction is valid
        if (!sender.next()) {      // if no account
            throw new Exception("Account number not in bank\n");
        }
        else if (sender.getDouble("balance") < amount) {  // banda bohot gareeb
            throw new Exception("Account number " + send_acc + " has insufficient funds\n");
        }

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

        double recv_balance = recipient.getDouble("balance");
        String recv_name = recipient.getString("name");

        // update balance - send
        da.update(
                "customers",
                "acc_no = " + send_acc,
                "balance",
                "" + (send_balance - amount)
        );

        // update balance - recv
        da.update(
                "customers",
                "acc_no = " + recv_acc,
                "balance",
                "" + (recv_balance + amount)
        );

        // add transaction to table
        while (true) {
            try {
                da.addRow(
                        "transactions",
                        abs((new Random()).nextLong()) + ", " + recv_acc + ", " + send_acc + ", " + (amount)
                );
                break;
            }
            catch (SQLIntegrityConstraintViolationException e){
                System.out.println("duplicate trans id");
            }
        }

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
            System.out.println("it works...");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
