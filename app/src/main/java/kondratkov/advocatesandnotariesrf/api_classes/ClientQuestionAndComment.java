package kondratkov.advocatesandnotariesrf.api_classes;

import kondratkov.advocatesandnotariesrf.account.ClientAccount;

/**
 * Created by xaratyan on 05.12.2016.
 */

public class ClientQuestionAndComment {
    public int Id=0;
    /// <summary>
    /// Заголовок/тема вопроса
    /// </summary>
    public String Header="" ;//{ get; set; }
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
    public Comment[] Comments = null;
}
