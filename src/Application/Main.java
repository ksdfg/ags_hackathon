package Application;

import ProjectLibs.ClientTools;
import ProjectLibs.ServerTools;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {

    public static void main(String[] args) {

        try (App app = new App()) {

            JSONObject input, result;
            try (ServerTools server = new ServerTools(8000)) {

                do {

                    // Connect to app gui
                    server.accept();

                    // read json object sent by app gui
                    input = (JSONObject) (new JSONParser()).parse(server.in.readUTF());

                    // display received json object
                    System.out.println((input.toJSONString()));

                    // get what operation to make
                    String operation = input.getOrDefault("operation", "").toString();

                    //  perform operation and get result
                    result = new JSONObject();
                    try {
                        switch (operation) {
                            case "login":
                                result.put("result",
                                        app.login(input.get("userid").toString(), input.get("password").toString())
                                );
                                break;

                            case "getAccounts":
                                result.put(
                                        "accounts", app.getAccounts(input.get("userid").toString())
                                );
                                result.put("result", true);
                                break;

                            case "authorize":
                                result.put("result",
                                        app.authorize(input.get("userid").toString(), input.get("type").toString(),
                                        input.get("value").toString())
                                );

                            case "getTransactions": // bank ka kaam

                            case "makeTransaction": // bank ka kaam
                                try (ClientTools client = new ClientTools("localhost", 5000)) {   // change localhost to bank ip
                                    client.out.writeUTF(input.toJSONString());  // write to bank
                                    result = (JSONObject) (new JSONParser()).parse(client.in.readUTF());   // read from bank
                                }
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

                    // write result to app gui
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
