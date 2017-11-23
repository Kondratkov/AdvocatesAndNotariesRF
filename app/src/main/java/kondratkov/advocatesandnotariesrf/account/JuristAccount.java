package kondratkov.advocatesandnotariesrf.account;

import java.util.Date;

/**
 * Created by xaratyan on 20.11.2016.
 */

public class JuristAccount extends BaseAccount{

    /// Судебная территория
    /*public class JudicialArea
    {
        public int Id; //{ get; set; }
        public City City;// { get; set; }
        public String Name;
    }*/

    public int Id;// { get; set; }
    /// Номер в реестре АП
    public String NumberInApReestr = "";// { get; set; }
    /// Статус адвоката присвоен кем
    public String JuristStatusAssignedBy= "";// { get; set; }
    /// Стаж адвоката
    public String ExperienceLevel;// { get; set; }
    /// Организация адвоката
    public JuristOrganisation JuristOrganisation = null;// { get; set; }
    /// Квалификация
    public String Qualification= "";// { get; set; }
    /// Ученая степень
    public String PhdInfo= "";// { get; set; }
    /// Владение иностранными языками
    public String LanguagesInfo= "";// { get; set; }
    /// Срочный выезд
    public boolean CanFastComing;// { get; set; }
    /// Членство во ФСАР
    public boolean IsFsarMember;// { get; set; }

    public String FsarMamberDate;// { get; set; }
    /// региональное отделение ФСАР
    public String FsarRegionalOffice= "";// { get; set; }
    /// Доп.офиц.данные и характеристики: Награды и т.д.
    public String ExtendedInfo= "";// { get; set; }
    ///  Рейтинг
    public double Rating;// { get; set; }
    /// Работа в выходные дни
    public boolean WorkInOffDays ;//{ get; set; }
    /// БЮПы
    public Bup[] BupsTypes;// { get; set; }
    /// Наличие взысканий
    public boolean HasRecoveries;// { get; set; }
    /// Судебная территория
    public JudicialAreas JudicialArea;// { get; set; }
    /// Специализации адвоката
    public JuristSpecialization[] Specializations ;//{ get; set; }
    /// Наставничество (имеет ли помощников/стажеров) да/нет
    public boolean Tutorship;// { get; set; }
    public double RatingExt;
    /// сайт
    public String Site= "";// { get; set; }


}
