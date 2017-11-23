package kondratkov.advocatesandnotariesrf.account;

/**
 * Created by xaratyan on 11.12.2016.
 */

public class GetProfileClient {
    public int Id;
    public AccountTypes AccounteType;
    public AccountState State;
    public String Name;
    public String Patronymic;
    public String Surename;
    public String Fio;
    public String Email;
    public String Phone;
    public String LastActivityTime;
    public Address Address;
    public double Longitude;
    public double Latitude;
    public double CurrentLatitude;
    public double CurrentLongitude;
    public boolean IsOnline;

    public enum AccountState{
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
