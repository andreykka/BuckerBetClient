package pojo;

import java.io.Serializable;

/**
 * Created by gandy on 27.09.14.
 *
 */
public class RegistrationData implements Serializable{

    private String name;
    private String surname;
    private String login;
    private String password;
    private String tel;
    private String eMail;
    private String mac;

    public RegistrationData() {
    }

    public RegistrationData(RegistrationData regData) {
        this.name       = regData.getName();
        this.surname    = regData.getSurname();
        this.login      = regData.getLogin();
        this.password   = regData.getPassword();
        this.tel        = regData.getTel();
        this.eMail      = regData.getEMail();
        this.mac        = regData.getMac();
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEMail() {
        return eMail;
    }

    public void setEMail(String eMail) {
        this.eMail = eMail;
    }
}
