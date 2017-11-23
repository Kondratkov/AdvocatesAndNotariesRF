package kondratkov.advocatesandnotariesrf.api_classes;

/**
 * Created by Kondratkov on 14.12.2016.
 */

public class NewPushMessage {
    public int Id ;//{ get; set; }
    public int AccountId;// { get; set; }
    public AccountTypes AccountType;// { get; set; }
    public String Date;// { get; set; }
    public int ServiceId ;//{ get; set; }
    public PushTypes PushType;// { get; set; }

    public enum PushTypes
    {
        /// <summary>
        /// Если пришел ответ или комментарий на вопрос присылаю Id вопроса
        /// </summary>
        NewQuestionEvent,
        /// <summary>
        /// Произошла смена статуса консультации
        /// </summary>
        ConsultationNewState,
        /// <summary>
        /// Если к консультации есть новый коммент
        /// </summary>
        ConsultationNewComment,
        /// <summary>
        /// Произошла смена статуса заказа документов
        /// </summary>
        DocumentOrderNewState,
        /// <summary>
        /// Если к консультации есть новый коммент
        /// </summary>
        DocumentOrderNewComment,
        /// <summary>
        /// Пакет документов отправлен вам на почту
        /// </summary>
        DocumantPacketsSended,
    }

    public int EnumPushTypes(){
        int izi=0;
        if(PushType == PushTypes.NewQuestionEvent){
            izi =1;
        }else
        if(PushType == PushTypes.ConsultationNewComment){
            izi =2;
        }else
        if(PushType == PushTypes.ConsultationNewState){
            izi =3;
        }else
        if(PushType == PushTypes.DocumentOrderNewComment){
            izi =4;
        }else
        if(PushType == PushTypes.DocumentOrderNewState){
            izi =5;
        }else{
            izi =6;
        }
        return izi;
    }
    public enum AccountTypes
    {
        Client,
        Jurist,
        DutyJurist
    }
}
