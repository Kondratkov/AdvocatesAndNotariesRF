package kondratkov.advocatesandnotariesrf.account;

/**
 * Created by xaratyan on 20.11.2016.
 */

public class UserModel {

    public String UserName = "";
    public String Password = "";
    public String ConfirmPassword = "";
    public AccountTypes AccountType = null;
    public int AccountId;
    public Object AccountProfile;

    public enum AccountTypes
    {
        Client,
        Jurist,
        DutyJurist
    }
}
