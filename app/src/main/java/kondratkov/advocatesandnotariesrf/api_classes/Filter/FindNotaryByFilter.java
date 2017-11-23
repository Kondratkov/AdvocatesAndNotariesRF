package kondratkov.advocatesandnotariesrf.api_classes.Filter;

import kondratkov.advocatesandnotariesrf.account.City;

/**
 * Created by xaratyan on 10.12.2016.
 */

public class FindNotaryByFilter {

    public sortingType SortingType;// { get; set; }

    public City City;
    public boolean WorkInOffDays;
    public boolean Equipage;
    public boolean Appointments;
    public boolean AppointmentsEmail;
    public boolean WorkWithJur;
    public boolean IsPleadtingHereditaryCases;
    public boolean IsSitesCertification;
    public boolean LockDocuments;
    public boolean DepositsReception;
    public boolean RequestsAndDuplicate;
    public boolean Transactions;

    public boolean Consultation;

    public enum sortingType
    {
        Name,
        Rating
    }
}
