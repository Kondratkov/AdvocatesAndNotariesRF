package kondratkov.advocatesandnotariesrf.api_classes;

import java.util.Date;

import kondratkov.advocatesandnotariesrf.account.DutyJuristAccount;

/**
 * Created by xaratyan on 03.12.2016.
 */

public class QuestionAnswer {
    public int Id;// { get; set; }
    /// <summary>
    /// Id вопроса
    /// </summary>
    public ClientQuestion Question;// { get; set; }
    /// <summary>
    /// Тело ответа
    /// </summary>
    public String Body;// { get; set; }
    /// <summary>
    /// юрист ответивший на вопрос
    /// </summary>
    public BaseJuristAccount Account;// { get; set; }
    /// <summary>
    /// Дата ответа
    /// </summary>
    public String Date;// { get; set; }
    /// <summary>
    /// Признак того что прочтено клиентом
    /// </summary>
    public boolean IsReaded;// { get; set; }
}
