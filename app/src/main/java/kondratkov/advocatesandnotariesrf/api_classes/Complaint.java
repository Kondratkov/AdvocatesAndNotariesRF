package kondratkov.advocatesandnotariesrf.api_classes;

/**
 * Created by xaratyan on 23.12.2016.
 */

public class Complaint {
    public int ID;
    public String Date;
    public int JuristId;
    public int AccountId;
    public AccountTypes AccountType;
    public String From;
    public String Message;

    public enum AccountTypes{
        Client,
        Jurist,
        DutyJurist
    }
}
