package config;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Scanner;

/**
 * Created by gandy on 12.10.14.
 *
 */
public class Config {

    private static  Config      INSTANCE = new Config();
    private final   String      IP_ADDRESS;
    private int                 PORT;
    public static final Logger  LOGGER = Logger.getLogger(Config.class);

    private Config (){
        JSONParser parser = new JSONParser();
        JSONObject object = new JSONObject();
        StringBuilder buffer = new StringBuilder();

        ClassLoader CLDR = this.getClass().getClassLoader();
        // jet resources from jar file as Stream
        InputStream in = CLDR.getResourceAsStream("config/config.json");
        Scanner scanner = new Scanner(in);

        while (scanner.hasNext()){
            buffer.append(scanner.next());
        }

        try {
            object = (JSONObject) parser.parse(buffer.toString());
        } catch (ParseException e) {
            //e.printStackTrace();
            LOGGER.error(e);
            System.out.println("ERROR parse config");
        }

        IP_ADDRESS = (String) object.get("IP_ADDRESS");
        PORT = Integer.parseInt(((String) object.get("PORT")));
    }

    public static String IP_ADDRESS() {
        return INSTANCE.IP_ADDRESS;
    }

    public static int PORT() {
        return INSTANCE.PORT;
    }

  /*  // return the MD5 hash from string msg
    private static String MD5(String msg){
        String digest = null;
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(msg.getBytes());

            for (byte b: hash ){
                sb.append(String.format("%02x", b&0xff));
            }
            digest = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return digest;
    }

    public static String getPasswordHash(String msg){
        String sol = "sdpaojf ;ldskjf;lkdsf j";
        return  MD5(MD5 (msg + MD5(sol)) );
    }*/

    public static String getMac() {
        StringBuilder result = new StringBuilder();
        Enumeration<NetworkInterface> en = null;
        try {
            en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface in = en.nextElement();
                byte[] mac = in.getHardwareAddress();
                if (mac != null) {
                    for (byte aMac : mac) {
                        result.append(aMac);
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        if (result.length() <= 0){
            result.append("NotAvailableMac");
        }
        return result.toString();
    }

}
