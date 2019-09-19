package Bank;

import ProjectLibs.DatabaseAccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.abs;

public class Bank implements AutoCloseable {

    private DatabaseAccess da;

    public Bank() throws SQLException {
        da = new DatabaseAccess();
        da.opendb("ocps", "bank", "meow");  // open the database
    }

    // authenticate if credentials of user are correct
    public boolean authenticate(int acc_no, int pin) throws Exception {
        // get result of pin
        ResultSet resultSet = da.getData(
                "account",
                "PIN",
                "acc_no = " + acc_no
        );

        if (!resultSet.next()) {   // if no account
            throw new Exception("Account number " + acc_no + " not in bank");
        }

        if (pin != resultSet.getInt("PIN")) // retrieve the pin
            throw new Exception("Incorrect PIN");

        return true;  // check if pin is correct
    }

    public ArrayList getTransactions(int acc) throws Exception {
        // get trans_table
        ResultSet resultSet = da.getData(
                "transactions t, account s, account r",
                "t.sender = s.acc_no and t.receiver = r.acc_no",
                "t.sender as sno, s.name as sname, t.receiver as rno, r.name as rname, t.amount as amount," +
                        " t.timestamp as timestamp",
                "t.sender = " + acc + " or t.receiver = " + acc + " order by t.timestamp"
        );

        ArrayList<ArrayList<Object>> a = new ArrayList<>();    // map to store all transactions
        ArrayList<Object> v;   // temp to add a transaction (other_party, amount) to arraylist
        while (resultSet.next()) {
            v = new ArrayList<>();

            // if acc is sender, then he's lost money
            if (resultSet.getInt("sno") == acc) {
                v.add(resultSet.getString("rname"));
                v.add(resultSet.getDouble("amount") * -1);
            }
            // if acc isn't sender, he's gained money
            else {
                v.add(resultSet.getString("sname"));
                v.add(resultSet.getDouble("amount"));
            }

            v.add(resultSet.getTimestamp("timestamp").toString());

            a.add(v);   // add transaction to arraylist
        }

        return a;   // 帰りたい
    }

    // add a transaction to the user
    public void makeTransaction(int send_acc, int recv_acc, double amount) throws Exception {
        // get current balance and trans_table of acc_no - send
        ResultSet sender = da.getData(
                "account", "balance", "acc_no = " + send_acc
        );
        // check if transaction is valid
        if (!sender.next()) {      // if no account
            throw new Exception("Account number not in bank\n");
        } else if (sender.getDouble("balance") < amount) {  // banda bohot gareeb
            throw new Exception("Account number " + send_acc + " has insufficient funds\n");
        }

        double send_balance = sender.getDouble("balance");

        // get current balance and trans_table of acc_no - recv
        ResultSet recipient = da.getData(
                "account", "balance", "acc_no =" + recv_acc
        );
        // check if transaction is valid
        if (!recipient.next()) {      // if no account
            throw new Exception("Account number not in bank\n");
        }

        double recv_balance = recipient.getDouble("balance");

        // update balance - send
        da.update(
                "account",
                "acc_no = " + send_acc,
                "balance",
                "" + (send_balance - amount)
        );

        // update balance - recv
        da.update(
                "account",
                "acc_no = " + recv_acc,
                "balance",
                "" + (recv_balance + amount)
        );

        // add transaction to table
        while (true) {
            try {
                da.addRow(
                        "transactions",
                        abs((new Random()).nextInt()) + ", " + send_acc + ", " + recv_acc + ", " + (amount) +
                                ", '" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()) + "'"
                );
                break;
            } catch (SQLIntegrityConstraintViolationException e) {
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
            System.out.println(bank.getTransactions(1));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }
}
