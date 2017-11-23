package kondratkov.advocatesandnotariesrf.api_classes;

import kondratkov.advocatesandnotariesrf.account.ClientAccount;

/**
 * Created by xaratyan on 03.12.2016.
 */

public class Order {
    public enum OrderState
    {
        /// <summary>
        /// Ожидает оплаты
        /// </summary>
        WaitingForPayment,
        /// <summary>
        /// Оплачен
        /// </summary>
        Paid,
        /// <summary>
        /// Отказано
        /// </summary>
        Denied,
        /// <summary>
        /// Отменено
        /// </summary>
        Canceled
    }
    /// <summary>
    /// Тип услуги
    /// </summary>
    public enum ServiceType
    {
        /// <summary>
        /// Консультация
        /// </summary>
        Consultation,
        /// <summary>
        /// Заказ документа
        /// </summary>
        DocumentOrder,
        DocumentPacketOrder
    }
    public int Id;// { get; set; }
    /// <summary>
    /// Id счета в яндекс
    /// </summary>
    public String YandexId;// { get; set; }
    /// <summary>
    /// Id клиента
    /// </summary>
    //[DisplayName("Id клиента")]
    public int ClientId;// { get; set; }
    /// <summary>
    /// Аккаунт клиента
    /// </summary>
    public kondratkov.advocatesandnotariesrf.account.ClientAccount ClientAccount;// { get; set; }

    /// <summary>
    /// Номер счета на оплату
    /// </summary>
    //[DisplayName("Номер счета на оплату")]
    public String OrderNumber ;//=> $"{Id:00000000}";
    /// <summary>
    /// Cумма к оплате
    /// </summary>
    //[DisplayName("Cумма к оплате")]
    public double PaymentAmount;// { get; set; }
    /// <summary>
    /// Текущий статус оплаты
    /// </summary>
    //[DisplayName("Текущий статус оплаты")]
    public OrderState State ;//{ get; set; }
    //[DisplayName("Тип услуги")]
    public ServiceType ServiceType;// { get; set; }
}
