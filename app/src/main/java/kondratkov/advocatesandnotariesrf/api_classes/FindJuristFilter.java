package kondratkov.advocatesandnotariesrf.api_classes;

import kondratkov.advocatesandnotariesrf.account.Bup;

/**
 * Created by xaratyan on 03.12.2016.
 */

public class FindJuristFilter {
    public enum SortingType
    {
        Name,
        Rating
    }

    public SortingType SortingType;
    public String Surename;
    public int CityId;
    public String Specialization;
    public Bup Bups;
    public int JudicialAreaId;
    public double Longitude;
    public double Latitude;
    public int Radius;

}
