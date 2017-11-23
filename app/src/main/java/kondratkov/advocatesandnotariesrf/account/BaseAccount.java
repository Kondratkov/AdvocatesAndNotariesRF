package kondratkov.advocatesandnotariesrf.account;

import java.util.Date;

/**
 * Created by xaratyan on 20.11.2016.
 */

public abstract class BaseAccount {

    public int id;
    public AccountType AccountType;
    public String Name;
    public String Surename;
    public String Patronymic;
    public String Email;
    public String Phone;
    public boolean IsOnline;
    public byte[] Image;
    public Date LastActivityTime ;
    public Address Address;
    public double Longitude;
    public double Latitude;

    public enum AccountType
    {
        client,
        jurist,
        dutyJurist
    }

}

