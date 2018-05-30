package kondratkov.advocatesandnotariesrf.account;

/**
 * Created by xaratyan on 11.12.2016.
 */

public class GetProfileClient {
    public int Id;
    public AccountTypes AccounteType;
    public State State;

    public String Login;
    public String Name;
    public String Patronymic;
    public String Surename;
    public String Fio;
    public String ImageUrl;
    public String Email;
    public String Phone;
    public String LastActivityTime;
    public String LastPushUpdateTime;
    public Address Address;
    public double Longitude;
    public double Latitude;
    public double CurrentLatitude;
    public double CurrentLongitude;
    public String Password = "";
    public String ConfirmPassword = "";
    public boolean IsOnline;


    public enum State{
        RegisterNotConfirmed,
        Confirmed,
        Blacklist
    }

    public enum AccountTypes{
        Client,
        Jurist,
        DutyJurist,
        Moderator
    }
}
