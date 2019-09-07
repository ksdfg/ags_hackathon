package AGSbroker;

import AGSlibs.ClientTools;
import AGSlibs.ServerTools;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {

    public static void main(String[] args) {

        try (Broker broker = new Broker()) {

            JSONObject input, result;
            try (ServerTools server = new ServerTools(8000)) {

                do {

                    // Connect to javascript
                    server.accept();

                    // read json object sent by javascript
                    input = (JSONObject) (new JSONParser()).parse(server.in.readUTF());

                    // display received json object
                    System.out.println((input.toJSONString()));

                    // get what operation to make
                    String operation = input.getOrDefault("operation", "").toString();

                    //  perform operation and get result
                    result = new JSONObject();
                    try {
                        switch (operation) {
                            case "getAccounts":
                                result.put("result", broker.getAccounts(input.get("deviceID").toString()));
                                break;

                            case "findDevice":
                                result.put("result", broker.findDevice((long) input.get("acc_no"),
                                        input.get("deviceID").toString()));
                                break;

                            case "addDevice":
                                broker.addDevice((long) input.get("acc_no"), input.get("deviceID").toString());
                                result.put("result", true);
                                break;

                            case "removeDevice":
                                broker.removeDevice((long) input.get("acc_no"), input.get("deviceID").toString());
                                result.put("result", true);
                                break;

                            case "authenticate":    // bank ka kaam

                            case "getTransactions": // bank ka kaam

                            case "makeTransaction": // bank ka kaam
                                try (ClientTools client = new ClientTools("localhost", 5000)) {   // change localhost to bank ip
                                    client.out.writeUTF(input.toJSONString());  // write to bank
                                    result = (JSONObject) (new JSONParser()).parse(client.in.readUTF());   // read from bank
                                }
                                break;

                            default:
                                System.out.println("bad operation");
                                result.put("result", "unsuccessful");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        result.put("result", "error");
                    }

                    // write result to javascript
                    server.out.writeUTF(result.toJSONString());

                    // close socket
                    server.closeSocket();
                    System.out.println();

                } while (true);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            //System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

}
