package Bank;

import ProjectLibs.ServerTools;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {

    public static void main(String[] args) {

        try (Bank bank = new Bank()) {

            JSONObject input;
            JSONObject result;

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
                    result = new JSONObject();
                    try {
                        switch (operation) {

                            case "get transactions":
                                result.put("result", bank.getTransactions((int) input.get("acc")));
                                break;

                            case "make transaction":
                                // if the pin is right
                                if (bank.authenticate((int) input.get("send_acc"), (int) input.get("pin")))
                                    // make the transaction
                                    bank.makeTransaction((int) input.get("send_acc"), (int) input.get("recv_acc"),
                                            (double) input.get("amount"));
                                break;

                            default:
                                System.out.println("bad operation");
                                result.put("result", false);
                                result.put("msg", "bad operation");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        result.put("result", false);
                        result.put("msg", e.getMessage());
                    }

                    // write to broker
                    server.out.writeUTF(result.toJSONString());

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
