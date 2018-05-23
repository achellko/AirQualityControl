import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class AirData {
    private static String provinceURL = "http://api.gios.gov.pl/pjp-api/rest/station/findAll";

    private static JSON json = new JSON();
    private static Sort sort = new Sort();
    private static Quality quality = new Quality();
    private static Data data = new Data();

    /**
     * function takes data from GIOS API with Poland provinces and list of their stations
     * @return map with key "province" and value with list of id`s from their stations
     */
    private static Map<String, List<String>> getProvinceMap() {
        HashMap<String, String> stationMap = new HashMap<>();
        JSONArray a = json.getJSON(provinceURL);

        // Loop through each item
        for (Object o : a) {
            JSONObject json = (JSONObject) o;
            JSONObject city = (JSONObject) json.get("city");
            JSONObject commune = (JSONObject) city.get("commune");

            stationMap.put(json.get("id").toString(), commune.get("provinceName").toString());
        }
        Map<String, List<String>> provinceMap = sort.sortByProvince(stationMap);
        return provinceMap;
    }

    /**
     * loop that takes data from API every 6 hours (4 times a day)
     */
    private static void timeLoop(){
        try {
            while (true) {
                HashMap m = new HashMap (getProvinceMap());
                m = data.getListOfDataFromProvinces(m);
                System.out.println(m.toString());

                data.getAirData(m);
                System.out.println(m.toString());

                HashMap newHM = sort.sortByElement(m);
                System.out.println(newHM);
                //output: ŚWIĘTOKRZYSKIE={NO2=4.2, O3=114.78, PM2.5=3.78, SO2=3.84, PM10=10.0, CO=216.67, C6H6=0.08}
                // PROVINCE name + list of element values

                newHM = quality.getAirQuality(newHM);
                System.out.println(newHM);
                //output: ŚWIĘTOKRZYSKIE=Good, PODKARPACKIE=Good,

                //timer for every 6 hrs
                Thread.sleep(60 * 60 * 6 * 1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        timeLoop();
    }

}