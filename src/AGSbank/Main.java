package AGSbank;

import AGSlibs.Client;
import AGSlibs.Server;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {

    public static void main(String[] args){

        try (Bank bank = new Bank()){

            String line;
            JSONObject input;
            Object result;

            do {

                // get info sent to bank
                try (Server server = new Server(5000)) {
                    line = server.in.readUTF();
                    input = (JSONObject) (new JSONParser()).parse(line);
                }

                // get wht operation to make
                String operation = (String) input.getOrDefault("operation", "");

                // perform operation and get result
                switch (operation) {
                    case "authenticate":
                        result = bank.authenticate((long)input.get("acc_no"), (int)input.get("pin"));
                        break;

                    case "getTransactions":
                        result = bank.getTransactions((long)input.get("send_acc"), (long)input.get("recv_acc"));
                        break;

                    case "makeTransaction":
                        bank.makeTransaction((long)input.get("send_acc"), (long)input.get("recv_acc"),
                                123, (double)input.get("amount"));
                        result = "transaction successful";
                        break;

                    default:
                        System.out.println("bad operation");
                        result = "";
                }

                // create a json object with the result
                JSONObject toWrite = new JSONObject();
                toWrite.put("result", result);

                // write the json object to broker
                try (Client client = new Client("insert ip here", 5000)){
                    client.out.writeBytes(toWrite.toJSONString());
                }

            } while (true);

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
