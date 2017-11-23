package kondratkov.advocatesandnotariesrf.api_classes;

import java.util.Date;

import kondratkov.advocatesandnotariesrf.account.ClientAccount;
import kondratkov.advocatesandnotariesrf.account.DutyJuristAccount;
import kondratkov.advocatesandnotariesrf.account.JuristSpecializationSector;

/**
 * Created by xaratyan on 03.12.2016.
 */

public class PostConsultation {

    public int Id = 0;// { get; set; }
    /// <summary>
    /// Id заказа
    /// </summary>
    public String Uin;// => $"GHK-{Id:00000000}";
    /// <summary>
    /// Специализация адвоката
    /// </summary>
    public JuristSpecializationSector SpecializationSector;// { get; set; }
    /// <summary>
    /// Предпочтительный способ связи
    /// </summary>
    public preferredContactMethod ContactMethod ;//{ get; set; }
    /// <summary>
    /// Кратко суть вопроса
    /// </summary>
    public String ConsultationQuestion ;//{ get; set; }
    /// <summary>
    /// Желаемые даты и время консультации
    /// </summary>
    public String ClientPreferredConsultationDate;// { get; set; }
    /// <summary>
    /// Даты и время консультации назначенные адвокатом
    /// </summary>
    public String ConsultationDate ;//{ get; set; }
    /// <summary>
    /// Примечания к заказу
    /// </summary>
    public String ClientNote ;//{ get; set; }
    /// <summary>
    /// Id клиента
    /// </summary>
    public int ClientId ;//{ get; set; }
    /// <summary>
    /// Аккаунт клиента (можно не заполнять)
    /// </summary>
    public ClientAccount ClientAccount;// get; set; }
    /// <summary>
    /// Id юриста
    /// </summary>
    public int JuristId;// { get; set; }
    /// <summary>
    /// Назначенный адвокат/Взял в паркинг
    /// </summary>
    public DutyJuristAccount JuristAccount;// { get; set; }
    /// <summary>
    /// Текущий статус заказа
    /// </summary>

    public ServiceState State;// { get; set; }
    /// <summary>
    /// Счет на оплату
    /// </summary>
    public Order Order;// { get; set; }
    public Comment[]Comments;

    public enum preferredContactMethod
    {
        Email,
        Phone
    }
    public enum ServiceState
    {
        /// <summary>
        /// Стадия согласования объема и стоимости правовой помощи со специалистом
        /// </summary>
        MatchingStage,
        /// <summary>
        /// Согласовано, ожидается оплата. Означает, что система ожидает осуществления транзакции оплаты
        /// </summary>
        WaitingForPayment,
        /// <summary>
        /// Исполнение заказа. При этом статусе ведется подготовка заказанных услуг.
        /// </summary>
        InProgress,
        /// <summary>
        /// Ваш заказ готов. Означает, что услуга подготовлена(адвокат свяжется или документы будут отправлены на почту).
        /// </summary>
        Completed
    }
    public int GetState(){
        int num_state =0;
        if(State == ServiceState.MatchingStage){
            num_state = 0 ;
        }else
        if(State == ServiceState.WaitingForPayment){
            num_state = 1 ;
        }else
        if(State == ServiceState.InProgress){
            num_state = 2 ;
        }else{
            num_state = 3 ;
        }
        return num_state;
    }
}
