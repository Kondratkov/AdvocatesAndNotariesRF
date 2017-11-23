package kondratkov.advocatesandnotariesrf.api_classes;

import kondratkov.advocatesandnotariesrf.account.Bup;
import kondratkov.advocatesandnotariesrf.account.JudicialAreas;
import kondratkov.advocatesandnotariesrf.account.JuristAccount;
import kondratkov.advocatesandnotariesrf.account.JuristOrganisation;
import kondratkov.advocatesandnotariesrf.account.JuristSpecialization;

/**
 * Created by xaratyan on 05.12.2016.
 */

public class BaseJuristAccount{
    /// <summary>
    /// Номер в реестре АП
    /// </summary>
    public String NumberInApReestr ;//{ get; set; }
    /// <summary>
    /// Статус адвоката присвоен кем
    /// </summary>
    public String JuristStatusAssignedBy;// { get; set; }
    /// <summary>
    /// Стаж адвоката
    /// </summary>
    public String ExperienceLevel;//{ get; set; }
    /// <summary>
    /// Организация адвоката
    /// </summary>
    public JuristOrganisation JuristOrganisation ;//{ get; set; }
    /// <summary>
    /// Квалификация
    /// </summary>
    public String Qualification ;//{ get; set; }
    /// <summary>
    /// Ученая степень
    /// </summary>
    public String PhdInfo ;//{ get; set; }
    /// <summary>
    /// Владение иностранными языками
    /// </summary>
    public String LanguagesInfo;// { get; set; }
    /// <summary>
    /// Срочный выезд
    /// </summary>
    public boolean CanFastComing;// { get; set; }
    /// <summary>
    /// Членство во ФСАР
    /// </summary>
    public boolean IsFsarMember;// { get; set; }

    public String FsarMamberDate;// { get; set; }
    /// <summary>
    /// региональное отделение ФСАР
    /// </summary>
    public String FsarRegionalOffice;// { get; set; }

    /// <summary>
    /// Доп.офиц.данные и характеристики: Награды и т.д.
    /// </summary>
    public String ExtendedInfo;//{ get; set; }
    /// <summary>
    ///  Рейтинг
    /// </summary>
    public double Rating ;//{ get; set; }
    /// <summary>
    /// Работа в выходные дни
    /// </summary>
    public boolean WorkInOffDays;// { get; set; }
    /// <summary>
    /// БЮПы
    /// </summary>
    public Bup Bups;// { get; set; }
    /// <summary>
    /// Наличие взысканий
    /// </summary>
    public boolean HasRecoveries ;//{ get; set; }
    /// <summary>
    /// Судебная территория
    /// </summary>
    public JudicialAreas judicialArea;// { get; set; }
    /// <summary>
    /// Специализации адвоката
    /// </summary>
    public JuristSpecialization[] Specializations ;//{ get; set; }
    /// <summary>
    /// Наставничество (имеет ли помощников/стажеров) да/нет
    /// </summary>
    public boolean Tutorship;// { get; set; }

    public String Fio;
}
