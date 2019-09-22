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

                            case "get accounts":
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
                                break;

                            case "create user":
                                result.put(
                                        "result",
                                        app.createUser(input.get("name").toString(),
                                                input.get("userid").toString(), input.get("pwd").toString())
                                );
                                break;

                            case "link acc":
                                result.put("result", app.linkAccount(input.get("userid").toString(),
                                        Integer.parseInt(input.get("acc").toString())));
                                break;

                            case "unlink acc":
                                result.put("result", app.unlinkAccount(input.get("userid").toString(),
                                        Integer.parseInt(input.get("acc").toString())));
                                break;

                            case "add bio":
                                result.put("result", app.addBio(input.get("userid").toString(),
                                        input.get("type").toString(), input.get("value").toString()));
                                break;

                            case "get bios":
                                result.put("bios", app.getBios(input.get("userid").toString()));
                                result.put("result", true);
                                break;

                            case "rm bio":
                                result.put("result", app.rmBio(input.get("userid").toString(),
                                        input.get("type").toString()));
                                break;

                            case "change name":
                                result.put("result", app.chgName(input.get("userid").toString(),
                                        input.get("name").toString()));
                                break;

                            case "change pwd":
                                result.put("result", app.chgPWD(input.get("userid").toString(),
                                        input.get("pwd").toString()));
                                break;

                            case "get otp":

                            case "get details":      // bank ka kaam

                            case "get transactions": // bank ka kaam

                            case "make transaction": // bank ka kaam

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
