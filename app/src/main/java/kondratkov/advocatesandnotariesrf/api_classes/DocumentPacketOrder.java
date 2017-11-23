package kondratkov.advocatesandnotariesrf.api_classes;

/**
 * Created by Kondratkov on 14.12.2016.
 */

public class DocumentPacketOrder {
    public int Id ;//{ get; set; }
    public String Uin;// => $"DPK-{Id:00000000}";

    public DocumentPacket DocumentPacket; //{ get; set; }
    public DocumentOrderFile[] Documents;// { get; set; }
    public Order Order;// { get; set; }
    public ServiceState State;

    public String Header ;//{ get; set; }
    public String Description ;//{ get; set; }
    public double Cost ;//{ get; set; }

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
