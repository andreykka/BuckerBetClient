package connection;

import listeners.DataListener;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pojo.FlagsEnum;
import pojo.OutputData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PortListener implements Runnable {

    private final Logger logger = Logger.getLogger(getClass());
    private static List<DataListener> listeners = new ArrayList<>(3);

    private Socket              socket;
    private ObjectInputStream   in;
    private ObjectOutputStream  out;

    private boolean isListening;

    public PortListener(Socket socket) {
        this.socket = socket;
        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            logger.error(e);
        }

        this.isListening = true;
    }

    public void dataObtained(){
        try {
            JSONParser parser = new JSONParser();
            JSONObject obj = null;
            String json;

            logger.info("read object from server");

            if (in != null) {
                json = in.readUTF();
            } else {
                logger.info("input stream = null");
                this.stopListen();
                return;
            }

            System.out.println(json);
            try {
                obj =  (JSONObject) parser.parse(json);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            List<OutputData> arr = new ArrayList<>();
            OutputData data;
            int i=0;
            while (true){
                JSONObject objectData = (JSONObject) obj.get(Integer.toString(i));

                if (objectData == null ){
                    logger.info("objectData == null");
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
            for(DataListener dl: listeners) {
                dl.dataObtained(arr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        while(isListening) try {

            Object object = in.readObject();

            if (!(object instanceof FlagsEnum)) {
                logger.info("object is not a FlagsEnum object");

            }

            FlagsEnum task = FlagsEnum.valueOf(((FlagsEnum) object).name());
            logger.info("read " + task.name() + "  from server");

            switch (task) {
                case GET_DATA:  { dataObtained();   break;  }
                case CHECK_CONNECTION:   { checkConnection();   break;  }
            }

        } catch (IOException | ClassNotFoundException e) {
            logger.error(e);
        }
    }

    private void checkConnection() {
        if (out == null)
            return;
        try {
            out.writeObject(Boolean.TRUE);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public void stopListen() {
        this.isListening = false;
        try {
            if (this.in != null)
                this.in.close();
            if (this.out != null)
                this.out.close();
            if (this.socket != null)
                this.socket.close();
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public static void addListener(DataListener listener) {
        listeners.add(listener);
    }

    public static void removeListener(DataListener listener) {
        listeners.remove(listener);
    }

}

