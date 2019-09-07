package Bank;

import ProjectLibs.ServerTools;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Random;

import static java.lang.Math.abs;

public class Main {

    public static void main(String[] args) {

        try (Bank bank = new Bank()) {

            JSONObject input;
            Object result;
            Random r = new Random();

            try (ServerTools server = new ServerTools(5000)) {

                do {

                    // get info sent to bank i.e. read from broker
                    server.accept();
                    input = (JSONObject) (new JSONParser()).parse(server.in.readUTF());

                    // display received json object
                    System.out.println((input.toJSONString()));

                    // get what operation to make
                    String operation = (String) input.getOrDefault("operation", "");

                    // perform operation and get result
                    try {
                        switch (operation) {
                            case "authenticate":
                                result = bank.authenticate((long) input.get("acc_no"),
                                        Integer.parseInt(input.get("pin").toString()));
                                break;

                            case "getTransactions":
                                result = bank.getTransactions((long) input.get("send_acc"), (long) input.get("recv_acc"));
                                break;

                            case "makeTransaction":
                                while (true) {
                                    try {
                                        Long transID = abs(r.nextLong());
                                        bank.makeTransaction((long) input.get("send_acc"), (long) input.get("recv_acc"),
                                                transID, (double) input.get("amount"));
                                        break;
                                    }
                                    catch (SQLIntegrityConstraintViolationException se){
                                        System.out.println("duplicate key");
                                    }
                                }
                                result = true;
                                break;

                            default:
                                System.out.println("bad operation");
                                result = "unsuccessful";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        result = "error";
                    }

                    // create a json object with the result
                    JSONObject toWrite = new JSONObject();
                    toWrite.put("result", result);

                    // write to broker
                    server.out.writeUTF(toWrite.toJSONString());

                    // close socket
                    server.closeSocket();
                    System.out.println();

                } while (true);

            }
            catch (Exception e){
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
