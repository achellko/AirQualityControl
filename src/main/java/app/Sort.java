package app;

import org.springframework.stereotype.Component;

import java.util.*;

@Component("sort")
public class Sort {

    /**
     * function makes list of unique provinces and then puts each of them as a key into provinceMap
     * with list of stations as values
     * @param stationMap unsorted map of every station and it`s province
     * @return sorted map with key - "province" and value - list of stations
     */
    public HashMap<String, List<String>> sortByProvince(HashMap<String,String> stationMap){
        Set<String> provinceSet = new HashSet<String>(stationMap.values());
        Map<String, List<String>> provinceMap = new HashMap<String, List<String>>();

        for (String s : provinceSet) {
            List<String> IdList = new ArrayList<String>();
            for (HashMap.Entry<String ,String> entry : stationMap.entrySet()) {
                if (entry.getValue().equals(s)){
                    IdList.add(entry.getKey());
                }
            }
            provinceMap.put(s,IdList);
        }
        return (HashMap<String, List<String>>) provinceMap;
    }

    /**
     * function sort list of air elements from different stations for every province
     * @param listOfStations map where key is province and value is list of elements from different stations
     *                       from this province
     * @return map with key - "province" and value - list of elements with actual values
     * from province
     */
    public  HashMap<String, HashMap> sortByElement(HashMap<String, ArrayList> listOfStations) {
        ArrayList listOfElements = new ArrayList(listOfStations.values());
        HashMap<String, HashMap> sortedMap = new HashMap<String, HashMap>();
        Set setOfValues = new HashSet();
        int count = 0;
        ArrayList province = new ArrayList(listOfStations.keySet());
        ArrayList listOfValues;
        HashMap<String, ArrayList> duplicates = new HashMap<String, ArrayList>();

        for (Object o : listOfElements) {
            listOfValues = new ArrayList((ArrayList)o);
            for (Object mapOfElements : listOfValues) {
                HashMap mapOfValues = (HashMap)mapOfElements;
                for (Object key : mapOfValues.keySet()) {
                    if (!setOfValues.add(key.toString())) {
                        if(mapOfValues.get(key)!="") {
                            ArrayList tmp2 = (ArrayList) duplicates.get(key.toString()).clone();
                            tmp2.add(mapOfValues.get(key));
                            duplicates.put(key.toString(), tmp2);
                        }
                    } else {
                        if(mapOfValues.get(key)!= "") {
                            ArrayList tmp1 = new ArrayList();
                            tmp1.add(mapOfValues.get(key));
                            duplicates.put(key.toString(), tmp1);
                        }
                    }
                }
            }
            sortedMap.put(province.get(count).toString(),countAvg(duplicates));
            count++;
            setOfValues.clear();
            duplicates.clear();
        }
        return sortedMap;
    }

    /**
     * function counts average value of element from list of values
     * @param map of element and list of it`s values
     * @return map with key - "element" and value - average value
     */
    private HashMap<String, String> countAvg(HashMap<String, ArrayList> map){
        ArrayList listOfValues;
        HashMap newMap = new HashMap();
        for (Object key : map.keySet()){
            listOfValues = (ArrayList)map.get(key.toString());
            double sum = 0;
            String empty = new String("");
            for (int i = 0; i < listOfValues.size(); i++){
                String str = (String) listOfValues.get(i);
                if (!str.equals(empty)) {
                    sum += Double.parseDouble((String) listOfValues.get(i));
                }
            }
            newMap.put(key.toString(),Math.round(sum/listOfValues.size()*100.0)/100.0);
        }
        return newMap;
    }
}
