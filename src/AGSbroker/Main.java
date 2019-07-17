package AGSbroker;

import AGSlibs.Client;
import AGSlibs.Server;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {

    public static void main(String[] args){
        try (Broker broker = new Broker()){

            String line;
            JSONObject input = null;

            while (true){

                try (Server server = new Server(5000)) {
                    line = server.in.readUTF();
                    input = (JSONObject) (new JSONParser()).parse(line);
                }

                String operation = (String) input.getOrDefault("operation", "");

                switch (operation){
                    default:
                        System.out.println("bad operation");
                }

                try (Client client = new Client("", 5000)){
                    client.out.writeUTF("");
                }

            }

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
