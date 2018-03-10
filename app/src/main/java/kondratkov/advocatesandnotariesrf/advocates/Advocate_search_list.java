package kondratkov.advocatesandnotariesrf.advocates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kondratkov.advocatesandnotariesrf.api_classes.JuristAccounClass;

/**
 * Created by kondratkov on 08.03.2018.
 */

public class Advocate_search_list implements Comparator {

    public static JuristAccounClass[] search_distance(ArrayList<JuristAccounClass>accounClasses, double Longitude, double Latitude){
        double[] distanseJurist = new double[accounClasses.size()];
        int [] juristId = new int[accounClasses.size()];

        HashMap<Integer, Double> map = new HashMap<Integer, Double>();

        for (int i = 0; i < distanseJurist.length; i++) {
            double mx = Math.abs(Latitude- accounClasses.get(i).Latitude);//51.714342);
            double my = Math.abs(Longitude - accounClasses.get(i).Longitude);//39.275005);
            Math.sqrt(Math.pow(mx, 2) + Math.pow(my,2));
        }

//        entriesSortedByValues(map)
//        for (Map.Entry<String, Integer> entry  : entriesSortedByValues(nonSortedMap)) {
//            System.out.println(entry.getKey()+":"+entry.getValue());
//        }

        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()){
            Map.Entry me = (Map.Entry)iterator.next();
        }
//        int[] reverse = Arrays.stream( ints ).boxed()
//                .sorted( Collections.reverseOrder() )
//                .mapToInt( Integer::intValue ).toArray();
        return null;
    }

    @Override
    public int compare(Object o, Object t1) {
        return 0;
    }
}
