package kondratkov.advocatesandnotariesrf.api_classes.Filter;

import kondratkov.advocatesandnotariesrf.account.Bup;

/**
 * Created by xaratyan on 10.12.2016.
 */

public class FindByCoordinatesFilter {
    public double Longitude;// { get; set; }
    public double Latitude;// { get; set; }
    public sortingType SortingType ;//{ get; set; }
    public String Surename;// { get; set; }
    public int CityId;// { get; set; }
    public String Specialization;// { get; set; }
    public Bup Bups;// { get; set; }
    public int JudicialAreaId;// { get; set; }
    public int Radius;// { get; set; }
    public boolean IsOnline;// { get; set; }
    public boolean WorkInOffDays;// { get; set; }
    public boolean CanFastComing;// { get; set; }

    public enum sortingType
    {
        Name,
        Rating
    }
}
