package ProjectLibs;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Scanner;

public class TestAGS {

    public static void main (String[] args){
        try (ClientTools client = new ClientTools("localhost", 8000)){

            JSONObject jsonObject = new JSONObject();

            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter operation to perform : ");
            String operation = scanner.next();
            jsonObject.put("operation", operation);

            switch (operation){
                case "getAccounts":
                    System.out.print("Enter device ID: ");
                    jsonObject.put("deviceID", scanner.next());
                    break;

                case "findDevice":

                case "addDevice":

                case "removeDevice":
                    System.out.print("Enter Account Number: ");
                    jsonObject.put("acc_no", scanner.nextLong());
                    System.out.print("Enter device ID: ");
                    jsonObject.put("deviceID", scanner.next());
                    break;

                case "authenticate":
                    System.out.print("Enter Account Number: ");
                    jsonObject.put("acc_no", scanner.nextLong());
                    System.out.print("Enter Pin: ");
                    jsonObject.put("pin", scanner.nextInt());
                    break;

                case "getTransactions":
                    System.out.print("Enter Sender's Account Number: ");
                    jsonObject.put("send_acc", scanner.nextLong());
                    System.out.print("Enter Recipient's Account Number: ");
                    jsonObject.put("recv_acc", scanner.nextLong());
                    break;

                case "makeTransaction":
                    System.out.print("Enter Sender's Account Number: ");
                    jsonObject.put("send_acc", scanner.nextLong());
                    System.out.print("Enter Recipient's Account Number: ");
                    jsonObject.put("recv_acc", scanner.nextLong());
                    System.out.print("Enter Amount: ");
                    jsonObject.put("amount", scanner.nextDouble());
                    break;

                default:
                    System.out.println("\nbad operation");
            }

            client.out.writeUTF(jsonObject.toJSONString());

            String s = client.in.readUTF();
            jsonObject = (JSONObject) (new JSONParser()).parse(s);
            System.out.println("\nresult = " + jsonObject.get("result"));

        }
        catch (Exception e){
            System.out.println(e);
        }
    }

}
