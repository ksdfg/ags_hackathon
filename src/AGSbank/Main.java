package AGSbank;

import AGSlibs.ServerTools;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {

    public static void main(String[] args) {

        try (Bank bank = new Bank()) {

            JSONObject input;
            Object result;
            ServerTools server = new ServerTools(5000);

            do {

                // get info sent to bank i.e. read from broker
                server.accept();
                input = (JSONObject) (new JSONParser()).parse(server.in.readUTF());

                // perform some action

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
                            bank.makeTransaction((long) input.get("send_acc"), (long) input.get("recv_acc"),
                                    123, (double) input.get("amount"));
                            result = "transaction successful";
                            break;

                        default:
                            System.out.println("bad operation");
                            result = null;
                    }
                }
                catch (Exception e){
                    result = "unsuccessful";
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
    }

}
