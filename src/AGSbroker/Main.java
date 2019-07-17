package AGSbroker;

import AGSlibs.Client;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {

    public static void main(String[] args) {

        try (Broker broker = new Broker()) {

            JSONObject input, result;

            do {

                // first read from javascript

                // read json object here, replace "{}" with json string you get from javascript
                input = (JSONObject) (new JSONParser()).parse("{}");

                // get what operation to make
                String operation = input.getOrDefault("operation", "").toString();

                //  perform operation and get result
                result = new JSONObject();
                switch (operation) {
                    case "findDevice":
                        result.put("result", broker.findDevice((long) input.get("acc_no"),
                                input.get("deviceID").toString()));
                        break;

                    case "addDevice":
                        broker.addDevice((long) input.get("acc_no"), input.get("deviceID").toString());
                        result.put("result", "device added");
                        break;

                    case "removeDevice":
                        broker.removeDevice((long) input.get("acc_no"), input.get("deviceID").toString());
                        result.put("result", "device added");
                        break;

                    case "authenticate":    // bank ka kaam

                    case "getTransactions": // bank ka kaam

                    case "makeTransaction": // bank ka kaam
                        try (Client client = new Client("localhost", 5000)) {   // change localhost to bank ip
                            client.out.writeUTF(input.toJSONString());  // write to bank
                            result = (JSONObject) (new JSONParser()).parse(client.in.readUTF());   // read from bank
                        }
                        break;

                    default:
                        System.out.println("bad operation");
                        result.put("result", null);
                }

                // write result to java script

            } while (true);

        } catch (Exception e) {
            //System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

}
