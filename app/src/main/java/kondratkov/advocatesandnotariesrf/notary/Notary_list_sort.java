package kondratkov.advocatesandnotariesrf.notary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import kondratkov.advocatesandnotariesrf.api_classes.Notary;

/**
 * Created by kondratkov on 29.03.2018.
 */

public class Notary_list_sort {
    public static ArrayList<Notary> sortListBy(ArrayList<Notary> list, By by) {

        int ds = list.size();

        switch (by) {
            case PRICE:
                Collections.sort(list, byPrice);
                break;
            case RETING:
                Collections.sort(list, byReting);
                break;
            default:
                break;
        }
        return list;
    }

    private static final Comparator<Notary> byPrice = new Comparator<Notary>() {
        @Override
        public int compare(Notary t1, Notary t2) {
            double p1 = t1.Distance;
            double p2 = t2.Distance;
            return p1 == p2 ? 0 : p1 > p2 ? 1 : -1;
        }
    };

    private static final Comparator<Notary> byReting = new Comparator<Notary>() {
        @Override
        public int compare(Notary t1, Notary t2) {
            double p1 = t1.Rating;
            double p2 = t2.Rating;
            return p1 == p2 ? 0 : p1 > p2 ? 1 : -1;
        }
    };

    public enum By {
        PRICE,
        RETING
    }
}
