package kondratkov.advocatesandnotariesrf.api_classes;

import kondratkov.advocatesandnotariesrf.account.ClientAccount;
import kondratkov.advocatesandnotariesrf.account.DutyJuristAccount;
import kondratkov.advocatesandnotariesrf.account.JuristSpecializationSector;

/**
 * Created by xaratyan on 04.12.2016.
 */

public class DocumentOrder {
    public int Id;// { get; set; }
    /// <summary>
    /// Id заказа
    /// </summary>
    public String Uin;// => $"DHK-{Id:00000000}";
    /// <summary>
    /// Специализация юриста
    /// </summary>
    public JuristSpecializationSector JuristSpecialization;// { get; set; }
    /// <summary>
    /// Предпочтительный способ связи
    /// </summary>
    public PreferredContactMethod ContactMethod;// { get; set; }
    /// <summary>
    /// Тип документа
    /// </summary>
    public DocumentTyps DocumentType;// { get; set; }
    /// <summary>
    /// Кратко суть вопроса по заказу доумента
    /// </summary>
    public String Description;// { get; set; }
    /// <summary>
    /// Причение к заказу документа
    /// </summary>
    public String DocumentNote;// { get; set; }
    /// <summary>
    /// Id клиента
    /// </summary>
    public int ClientId;// { get; set; }
    /// <summary>
    /// Аккаунт клиента (можно не заполнять)
    /// </summary>
    public ClientAccount ClientAccount;// { get; set; }
    /// <summary>
    /// Документы прикрепленные к заказу клиентом
    /// </summary>
    public DocumentOrderFile[] ClientDocuments;// { get; set; }
    /// <summary>
    /// Документы прикрепленные к заказу юристом после оплаты
    /// </summary>
    public DocumentOrderFile[] JuristDocuments;// { get; set; }
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
    public Comment[] Comments;

    /// <summary>
    /// Виды документов
    /// </summary>
    public enum DocumentTyps {
        // Правовой кейс - тематический шаблон в виде архивного файла.Необходимо организовать систему направления клиенту самого архива или ссылки для скачивания!
        LegalCase,
        // Проверка добросовестности контрагента (дью дилидженс- лайт)
        CheckingIntegrityCounterparty,
        // Претензия
        Pretension,
        // Жалоба на действия должностных лиц
        ComplaintActionsOfficials,
        // Обращение в госорганы РФ
        AppealToStateBodies,
        // Заявление
        Statement,
        // Исковое заявление
        Plaint,
        // Возражение на иск
        ObjectionToClaim,
        // Обжалование решения суда
        AppealCourtDecision,
        // Иной документ
        AnotherDocument,
        // Затрудняюсь выбрать
        DifficultToChoose
    }

    public String GetDocumentTyp(){
        String doc_type = "";
        if(DocumentType == DocumentTyps.CheckingIntegrityCounterparty){
            doc_type = "Проверка добросовестности контрагента (дью дилидженс- лайт)";
        }else
        if(DocumentType == DocumentTyps.Pretension){
            doc_type = "Претензия";
        }else
        if(DocumentType == DocumentTyps.ComplaintActionsOfficials){
            doc_type = "Жалоба на действия должностных лиц";
        }else
        if(DocumentType == DocumentTyps.AppealToStateBodies){
            doc_type = "Обращение в госорганы РФ";
        }else
        if(DocumentType == DocumentTyps.Statement){
            doc_type = "Заявление";
        }else
        if(DocumentType == DocumentTyps.Plaint){
            doc_type = "Исковое заявление";
        }else
        if(DocumentType == DocumentTyps.ObjectionToClaim){
            doc_type = "Возражение на иск";
        }else
        if(DocumentType == DocumentTyps.AppealCourtDecision){
            doc_type = "Обжалование решения суда";
        }else
        if(DocumentType == DocumentTyps.AnotherDocument){
            doc_type = "Иной документ";
        }else{
            doc_type = "Затрудняюсь выбрать";
        }
        return doc_type;
    }

    public DocumentTyps SetDocumentTyp(int position){
        DocumentTyps dd = null;
        switch (position){
            case 0:dd = DocumentTyps.CheckingIntegrityCounterparty;
                break;
            case 1:dd = DocumentTyps.Pretension;
                break;
            case 2:dd = DocumentTyps.ComplaintActionsOfficials;
                break;
            case 3:dd = DocumentTyps.AppealToStateBodies;
                break;
            case 4:dd = DocumentTyps.Statement;
                break;
            case 5:dd = DocumentTyps.Plaint;
                break;
            case 6:dd = DocumentTyps.ObjectionToClaim;
                break;
            case 7:dd = DocumentTyps.AppealCourtDecision;
                break;
            case 8:dd = DocumentTyps.AnotherDocument;
                break;
            case 9:dd = DocumentTyps.DifficultToChoose;
                break;
        }
        return dd;
    }

    public enum PreferredContactMethod
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
