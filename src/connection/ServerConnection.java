package connection;

import config.Config;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import pojo.FlagsEnum;
import pojo.LogInData;
import pojo.OutputData;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.*;
import org.json.simple.parser.*;
import pojo.RegistrationData;

import javax.swing.*;

/**
 * Created by gandy on 26.09.14.
 *
 */

public class ServerConnection {

    private volatile Socket                 socket  = null;
    private volatile ObjectInputStream      in      = null;
    private volatile ObjectOutputStream     out     = null;

    public static final Logger              LOGGER = Logger.getLogger(ServerConnection.class);

    public ServerConnection(Socket socket) throws IOException {
        try {
            this.socket = socket;
            this.out = new ObjectOutputStream(socket.getOutputStream());
//            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    private boolean isActive(){
        if (socket != null && !socket.isClosed()){
            return true;
        }
        LOGGER.info("Disconnect from server");
        return false;
    }

    /**
     * function "login" connected user to server
     * @param login     name of the user that will send to server
     * @param password  password of the user will send to server
     * @return true if user is register in DB, false if didn't
     *
     * */
    public Boolean login (String login, String password) throws NullPointerException{

        try {
            if (this.in == null)
                this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        LOGGER.info("start login function");

        LogInData data = new LogInData(login, password, Config.getMac());
        if (!isActive()) {
            JOptionPane.showMessageDialog(null, "Нет соединения с сервером");
            return null;
        }

        try {
            out.writeObject(FlagsEnum.LOG_IN);
            out.flush();
            LOGGER.info("write " + FlagsEnum.LOG_IN);

            out.writeObject(data);
            out.flush();
            LOGGER.info("write Login data");

            LOGGER.info("try to read request ...");
            Boolean result = in.readBoolean();

            if (result != null && !result) {
                String message = in.readUTF();
                JOptionPane.showMessageDialog(null, message);
                return false;
            }
            return true;
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return false;
    }

    public Boolean registerUser(RegistrationData regData){
        LOGGER.info("start registerUser function");
        try {
            if (this.in == null)
                this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!isActive()) {
            JOptionPane.showMessageDialog(null, "Нет соединения с сервером");
            return null;
        }

        try {
            out.writeObject(FlagsEnum.REG_USER);
            out.flush();
            LOGGER.info("write " + FlagsEnum.REG_USER);

            JSONObject object = new JSONObject();
            object.put("surname",   regData.getSurname());
            object.put("name",      regData.getName());
            object.put("email",     regData.getEMail());
            object.put("tel",       regData.getTel());
            object.put("login",     regData.getLogin());
            object.put("password",  regData.getPassword());
            object.put("mac",       Config.getMac());

            out.writeObject(object);
            out.flush();
            LOGGER.info("write RegistrationData data");

            LOGGER.info("try to read request ...");

            Boolean result = in.readBoolean();
            LOGGER.info("server request are: " + result);

            String message;
            message = in.readUTF();

            LOGGER.info(message);

            JOptionPane.showMessageDialog(null, message, "Ошибка",JOptionPane.WARNING_MESSAGE);

            return  result;

        } catch (IOException e) {
            LOGGER.error(e);
        }
        return false;
    }

    /**
     * get data from server
     * @return array of the OutputData from server
     * */
    public List<OutputData> getContentData(){
        List<OutputData> arr = new ArrayList<>();
        OutputData data;

        if (!isActive()) {
            throw new NullPointerException();
        }

        try {
            LOGGER.info("in getContentData");

            out.writeObject(FlagsEnum.GET_DATA);
            out.flush();

            LOGGER.info("try to read data");
            String json = in.readUTF();
            LOGGER.info("read data from server ");

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(json);
            JSONObject jsonObject = (JSONObject) obj;

            int i=0;
            while (true){
                JSONObject objectData = (JSONObject) jsonObject.get(Integer.toString(i));

                if (objectData == null ){
                    LOGGER.info("objectData == null");
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

        } catch (IOException | ParseException e){
            LOGGER.error(e);
        }
        return arr;
    }

    private void logOut(){
        LOGGER.info("Log OUT");
        try {
            if (out != null) {
                out.writeObject(FlagsEnum.LOG_OUT);
                out.flush();
            }
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    public void closeConnection() {
        logOut();
        try{
            if (this.in != null)
                this.in.close();
            this.in = null;

            if (this.out != null)
                this.out.close();
            this.out = null;

            if (socket != null)
                this.socket.close();
            this.socket = null;
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }
}
