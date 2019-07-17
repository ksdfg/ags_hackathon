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

                // read json object here, replace "" with json string you get from javascript
                input = (JSONObject) (new JSONParser()).parse("");

                // perform some action

                try (Client client = new Client("localhost", 5000)) {   // change localhost to bank ip
                    client.out.writeUTF(input.toJSONString());  // write to bank
                    result = (JSONObject) (new JSONParser()).parse(client.in.readUTF());   // read from bank
                }

                // perform some action

                // write to java script

            } while (true);

        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
