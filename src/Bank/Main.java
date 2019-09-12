package Bank;

import ProjectLibs.ServerTools;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {

    public static void main(String[] args) {

        try (Bank bank = new Bank()) {

            JSONObject input;
            Object result;

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

                            case "getTransactions":
                                result = bank.getTransactions((int) input.get("acc"));
                                break;

                            case "makeTransaction":
                                // if the pin is right
                                if (bank.authenticate((int) input.get("send_acc"), (int) input.get("pin"))) {
                                    // make the transaction
                                    bank.makeTransaction((int) input.get("send_acc"), (int) input.get("recv_acc"),
                                            (double) input.get("amount"));
                                    result = true;
                                } else {  //wrong pin, fuck off
                                    result = false;
                                }
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

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
