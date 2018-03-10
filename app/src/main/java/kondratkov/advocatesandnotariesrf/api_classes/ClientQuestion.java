package kondratkov.advocatesandnotariesrf.api_classes;

import java.util.Date;

import kondratkov.advocatesandnotariesrf.account.ClientAccount;

/**
 * Created by xaratyan on 03.12.2016.
 */

public class ClientQuestion {
    public int Id=0;
    /// <summary>
    /// Заголовок/тема вопроса
    /// </summary>
    public String Header="" ;//{ get; set; }
    public boolean IsReaded= false;
    /// <summary>
    /// Тело вопроса
    /// </summary>
    public String Body ="";//{ get; set; }
    /// <summary>
    /// Id клиента задавшего вопрос
    /// </summary>
    public int ClientId =0;//{ get; set; }
    /// <summary>
    /// Информация по аккаунту клиента
    /// </summary>
    public ClientAccount Account = null;//{ get; set; }
    /// <summary>
    /// Id адвкоката которому был адресован вопрос, если 0 то отсылается в общий чат
    /// </summary>
    public int JuristId =0;//{ get; set; }
    public BaseJuristAccount JuristAccount;
    public String Date ="";//{ get; set; }

    /// <summary>
    /// Всего ответов
    /// </summary>
    public int AnswersCount=0 ;//{ get; set; }
    /// <summary>
    /// Количество непрочтенных ответов
    /// </summary>
    public int IsNotReadedAnswers=0;// { get; set; }
    public String ParkingTime="" ;//{ get; set; }
    /// <summary>
    /// Ответы
    /// </summary>
    public QuestionAnswer[] Answers = null;//{ get; set; }
   //public Comment[] Comments = null;

    public AccountTypes AccountType;

    public QuestionStatusRead  StatusRead;

    public enum AccountTypes
    {
        Client,
        Jurist,
        DutyJurist
    }

    public enum QuestionStatusRead{
        ClientSent,
        Read,
        JuristSent
    }

}
