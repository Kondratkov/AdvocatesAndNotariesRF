package kondratkov.advocatesandnotariesrf.api_classes;

import kondratkov.advocatesandnotariesrf.account.Address;
import kondratkov.advocatesandnotariesrf.account.Region;

/**
 * Created by MODEST_PC on 02.12.2016.
 */


public class Notary {
    //1) Ф.И.О.
    public int Id ;//{ get; set; }
   // [DisplayName("Имя")]
    public String Name ;//{ get; set; }
 //   [DisplayName("Фамилия")]
    public String Surename;// { get; set; }
 //   [DisplayName("Отчество")]
    public String Patronymic;// { get; set; }

    public String Fio;// => $"{Surename} {Name} {Patronymic}";
    /// <summary>
    /// Населенный пункт, офис (название и адрес)
    /// </summary>
    public Address Address;//
    /// Населенный пункт, офис (название и адрес)
    /// </summary>
    public String Address2 ;//{ get; set; }
    public double Longitude ;// get; set; }
    public double Latitude ;//{ get; set; }
    /// <summary>
    /// Лицензия № (в формате: №014 от 11.10.2006г)
    /// </summary>
    public String LicenseNumber ;//{ get; set; }
    /// <summary>
    /// Ведение наследственных дел – «Да», «Нет»
    /// </summary>
    public boolean IsPleadtingHereditaryCases;// { get; set; }
    /// <summary>
    /// Заверение сайтов – «Да», «Нет»
    /// </summary>
    public boolean IsSitesCertification;// { get; set; }
    /// <summary>
    /// Прием депозитов – да\нет
    /// </summary>
    public boolean DepositsReception;//{ get; set; }
    /// <summary>
    /// Выезд – «Да», «Нет»
    /// </summary>
    public boolean Equipage;// { get; set; }
    /// <summary>
    ///  Рейтинг
    /// </summary>
    public double Rating ;//{ get; set; }
    /// <summary>
    /// Работа в выходные дни
    /// </summary>
    public boolean WorkInOffDays ;//{ get; set; }
    /// <summary>
    /// Дополнительные официальные данные и характеристики: Приказ № (в формате №22-н л/с от 18.10.2006), награды, ученые степени и т.д.
    /// </summary>
    public String ExtendedInfo ;//{ get; set; }
    //11) График работы
    //12) Контактные данные(телефон (или несколько номеров)\е-mail\ веб-сайт)
    public ContactsData Contacts ;//{ get; set; }
    ///Запись на прием
    public boolean Appointments;//{ get; set; }
   ///Запись на прием Email
   public boolean AppointmentsEmail;//{ get; set; }
    ///работа с юр. лицами
    public boolean WorkWithJur;//{ get; set; }
    ///сделки
    public boolean  Transactions;//{ get; set; }
    ///Запросы\дубликаты,
    public boolean RequestsAndDuplicate;//{ get; set; }
    ///фиксация документов
    public boolean  LockDocuments;//{ get; set; }
    ///предоставление консультаций
    public boolean consultation;//{ get; set; }
    public String Phone="";
    public String Site="";
    public String Email="";
    public boolean Consultation;

}
