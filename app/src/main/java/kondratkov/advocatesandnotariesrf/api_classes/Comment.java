package kondratkov.advocatesandnotariesrf.api_classes;

/**
 * Created by xaratyan on 05.12.2016.
 */

public class Comment {
    public String Id="";
    public CommentType1 CommentType = CommentType1.Consultation;
    public int Serviceld=0;
    public String Date="";
    public String From="";
    public  String Message="";
    public int AccountId = -1;
    public ClientQuestion.AccountTypes AccountType;// = AccountTypes.Client;

    public enum CommentType1{
        Question,
        Consultation,
        DocumentOrder
    }
    public enum AccountTypes{
        Client,
        Jurist,
        DutyJurist
    }
//    public int GetAccountTypes(){
//        int i =0;
//        if(AccountType==AccountTypes.Client){
//            i=0;
//        }else i=1;
//        return i;
//    }
}
