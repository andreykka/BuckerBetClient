package connection;

import config.Config;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import pojo.FlagsEnum;
import pojo.LogInData;
import pojo.OutputData;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDate;
import java.util.*;
import org.json.simple.parser.*;
import pojo.RegistrationData;
import sun.awt.ModalExclude;

import javax.swing.*;

/**
 * Created by gandy on 26.09.14.
 *
 */

public class ServerConnection extends Thread {

    private static ServerConnection         instance = new ServerConnection();

    private static final int                PORT    = Config.PORT();        // port server
    private static final String             ADDRESS = Config.IP_ADDRESS();  // ip address server
    private volatile ObjectInputStream      in      = null;
    private volatile ObjectOutputStream     out     = null;
    private volatile Socket                 socket  = null;

    public static final Logger              LOGGER = Logger.getLogger(ServerConnection.class);
    
//    private PortListener portListener;

    /**
     * create new THREAD and start it
     * */
    private ServerConnection(){
        super("ServerConnectionThread");
        this.start();
    }

    public static ServerConnection getInstance(){
        return instance;
    }

    @Override
    public void run() {
        try{
            InetAddress inetAddress = InetAddress.getByName(ADDRESS);
            LOGGER.info("try to connect to server on port " + PORT + " address " + inetAddress);
            this.socket = new Socket(inetAddress, PORT);
            LOGGER.info("Connected Successeful");
            this.out = new ObjectOutputStream(this.socket.getOutputStream());
            this.in = new ObjectInputStream(this.socket.getInputStream());

        } catch (IOException e ){
            //System.out.println("Unknown inet address ERROR");

            LOGGER.error(e);
            LOGGER.info("Unknown inet address ERROR");
            LOGGER.info( "не удалось соединиться с сервером. Попробуйте позже.");

            //e.printStackTrace();
            this.interrupt();
            JOptionPane.showMessageDialog(null, "не удалось соединиться с вервером.\r\nПопробуйте позже.","Внимание", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isActive(){
        if (socket != null &&  socket.isConnected()){
            return true;
        }
        //System.out.println("Disconnect from server");
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

        //System.out.println("start login function");
        LOGGER.info("start login function");

        LogInData data = new LogInData(login, password, Config.getMac());
        if (!isActive()) {
            JOptionPane.showMessageDialog(null, "Нет соединения с сервером");
            return null;
            //throw new NullPointerException();
        }

        try {
            out.writeObject(FlagsEnum.LOG_IN);
            out.flush();
            //System.out.println("write " + FlagsEnum.LOG_IN);
            LOGGER.info("write " + FlagsEnum.LOG_IN);

            out.writeObject(data);
            out.flush();
            //System.out.println("write Login data");
            LOGGER.info("write Login data");

            //System.out.println("try to read request ...");
            LOGGER.info("try to read request ...");
            Boolean result = in.readBoolean();

            if (result != null && !result) {
                String message = in.readUTF();
                JOptionPane.showMessageDialog(null, message);
                return false;
            }

//            this.portListener = new PortListener(in);
            return true;
        } catch (IOException e) {
            LOGGER.error(e);
            //e.printStackTrace();
        }
        return false;
    }

    public Boolean registerUser(RegistrationData regData){
        //System.out.println("start registerUser function");
        LOGGER.info("start registerUser function");
        if (!isActive()) {
            JOptionPane.showMessageDialog(null, "Нет соединения с сервером");
            //throw new NullPointerException();
            return null;
        }

        try {
            out.writeObject(FlagsEnum.REG_USER);
            out.flush();
            //System.out.println("write " + FlagsEnum.REG_USER);
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
            //System.out.println("write RegistrationData object\r\n" + object.toJSONString());
            LOGGER.info("write RegistrationData data");

            //System.out.println("try to read request ...");
            LOGGER.info("try to read request ...");

            Boolean result = in.readBoolean();
            //System.out.println("server request are: " + result);
            LOGGER.info("server request are: " + result);

            String message;
            message = in.readUTF();

            LOGGER.info(message);

            JOptionPane.showMessageDialog(null, message, "Ошибка",JOptionPane.WARNING_MESSAGE);

            return  result;

        } catch (IOException e) {
            LOGGER.error(e);
            //e.printStackTrace();
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
            //System.out.println("in getContentData");
            LOGGER.info("in getContentData");

            out.writeObject(FlagsEnum.GET_DATA);
            out.flush();

            //System.out.println("try to read data");
            LOGGER.info("try to read data");
            String json = in.readUTF();
            //System.out.println("read from server \r\n" +json);
            LOGGER.info("read data from server ");

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(json);
            JSONObject jsonObject = (JSONObject) obj;

            int i=0;
            while (true){
                JSONObject objectData = (JSONObject) jsonObject.get(Integer.toString(i));

                if (objectData == null ){
                    //System.out.println("objectData == null");
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
            //e.printStackTrace();
        }
        return arr;
    }

    private void logOut(){
        //System.out.println("Log OUT");
        LOGGER.info("Log OUT");
        try {
            if (out != null) {
                out.writeObject(FlagsEnum.LOG_OUT);
                out.flush();
            }
        } catch (IOException e) {
            LOGGER.error(e);
            //e.printStackTrace();
        }
    }

    @Override
    public void interrupt() {

//        if (portListener != null)
//            this.portListener.interrupt();
        logOut();
        this.in = null;
        this.out = null;
        try {
            if (socket != null && !socket.isClosed()) {

                this.socket.shutdownInput();
                this.socket.shutdownOutput();
                this.socket.close();
            }
        } catch (IOException e) {
            LOGGER.error(e);
            //e.printStackTrace();
        }

        super.interrupt();

    }
}
