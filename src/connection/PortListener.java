/*
package connection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pojo.OutputData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

*/
/**
 * Created by gandy on 15.10.14.
 *
 *//*


public class PortListener extends  Thread{

    private ObjectInputStream in;

    private boolean isListening;

    private List<OutputData> arr;

    public PortListener(ObjectInputStream in) {
        super("PortListener Thread");
        this.in = in;
        this.isListening = true;

        this.start();
    }

    @Override
    public void run() {
        super.run();
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while(isListening){

            try {

                JSONParser parser = new JSONParser();
                JSONObject obj = null;
                String json;
                System.out.println("read object from server");
                Log.write("read object from server");

                if (in != null) {
                    json = in.readUTF();
                } else {
                    System.out.println("input stream = null");
                    Log.write("input stream = null");
                    this.interrupt();
                    return;
                }

                System.out.println(json);
                try {
                    obj =  (JSONObject) parser.parse(json);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                arr = new ArrayList<>();
                OutputData data;
                int i=0;
                while (true){
                    JSONObject objectData = (JSONObject) obj.get(Integer.toString(i));

                    if (objectData == null ){
                        System.out.println("objectData == null");
                        Log.write("objectData == null");
                        break;
                    }

                    data = new OutputData();
                    data.setDate((LocalDate.parse((String)objectData.get("date"))));
                    data.setResult((String) objectData.get("result"));
                    data.setEvent(((String) objectData.get("event")));
                    data.setTime((String)objectData.get("time"));

                    arr.add(data);
                    ++i;
                }
                //Controller.getInstance().setServerMessage(arr);
                //Controller.getFromServer(arr);
            } catch (IOException e) {
                e.printStackTrace();
                Log.write(e);
            }

        }

    }


    @Override
    public void interrupt() {
        super.interrupt();
        this.isListening = false;
    }
}

*/
