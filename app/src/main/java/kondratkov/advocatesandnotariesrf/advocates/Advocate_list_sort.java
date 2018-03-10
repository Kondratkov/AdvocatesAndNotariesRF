package kondratkov.advocatesandnotariesrf.advocates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import kondratkov.advocatesandnotariesrf.api_classes.JuristAccounClass;

/**
 * Created by kondratkov on 10.03.2018.
 */

public class Advocate_list_sort {

    public static ArrayList<JuristAccounClass> sortListBy(ArrayList<JuristAccounClass> list, By by) {

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

    private static final Comparator<JuristAccounClass> byPrice = new Comparator<JuristAccounClass>() {
        @Override
        public int compare(JuristAccounClass t1, JuristAccounClass t2) {
            double p1 = t1.dist;
            double p2 = t2.dist;
            return p1 == p2 ? 0 : p1 > p2 ? 1 : -1;
        }
    };

    private static final Comparator<JuristAccounClass> byReting = new Comparator<JuristAccounClass>() {
        @Override
        public int compare(JuristAccounClass t1, JuristAccounClass t2) {
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
