package pojo;

import java.io.Serializable;

/**
 * Created by gandy on 27.09.14.
 */
public class LogInData implements Serializable {

    private String login;
    private String pass;
    private String mac;

    public LogInData (LogInData obj){
        this.setLogin(obj.getLogin());
        this.setPass(obj.getPass());
        this.setMac(obj.getMac());
    }

    public LogInData() {

    }


    public LogInData(String login, String pass, String mac) {
        this.login = login;
        this.pass = pass;
        this.mac  = mac;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "login='" + login + '\'' +
                ", pass='" + pass + '\'';
    }
}
