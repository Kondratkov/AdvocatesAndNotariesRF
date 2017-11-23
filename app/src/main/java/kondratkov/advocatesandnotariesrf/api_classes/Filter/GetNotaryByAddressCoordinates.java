package kondratkov.advocatesandnotariesrf.api_classes.Filter;

/**
 * Created by xaratyan on 27.02.2017.
 */

public class GetNotaryByAddressCoordinates {
    public sortingType SortingType;// { get; set; }
    public double Longitude;// { get; set; }
    public double Latitude ;//{ get; set; }
    public int Radius ;//{ get; set; }

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
