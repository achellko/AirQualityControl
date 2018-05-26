import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.*;

public class Data {
    private JSON json = new JSON();
    private Sort sort = new Sort();
    private Quality quality = new Quality();

    private String provinceURL = "http://api.gios.gov.pl/pjp-api/rest/station/findAll";
    private String stationURL = "http://api.gios.gov.pl/pjp-api/rest/station/sensors/";
    private String airDataURL = "http://api.gios.gov.pl/pjp-api/rest/data/getData/";

    /**
     * function takes data from GIOS API with Poland provinces and list of their stations
     * @return map with key "province" and value with list of id`s from their stations
     */
    public HashMap<String, List<String>> getProvinceMap() {
        HashMap<String, String> stationMap = new HashMap<>();
        JSONArray a = json.getJSON(provinceURL);

        // Loop through each item
        for (Object o : a) {
            JSONObject json = (JSONObject) o;
            JSONObject city = (JSONObject) json.get("city");
            JSONObject commune = (JSONObject) city.get("commune");

            stationMap.put(json.get("id").toString(), commune.get("provinceName").toString());
        }
        HashMap<String, List<String>> provinceMap = sort.sortByProvince(stationMap);
        return provinceMap;
    }

    /**
     * function gets the latest value of element form GIOS API
     * @param index of element
     * @return value of element
     */
    private String getValueFromURL(String index){
        index = airDataURL+index;
        String result = new String ();
        int i = 0;

        JSONObject a = json.getJSONObject(index);

        HashMap b = (HashMap) ((HashMap)a);
        String key = (String) b.get("key");

        JSONArray l = (JSONArray) b.get("values");
        HashMap hm = new HashMap();

        for (Object o : l) {
            if (i == 1)
                break;
            else if (((HashMap)o).get("value") == null) {
                continue;
            }
            result = (String) ((HashMap)o).get("value").toString();
            hm.put( key, ((HashMap)o).get("value"));
            i++;
        }
        return result;
    }

    /**
     * function gets data for given station from GIOS API
     * @param stationNumber id of station
     * @return map with key - "element name" and value - id of this element
     */
    private HashMap<String, String> getDataID(String stationNumber) {
        String airURL = stationURL+stationNumber;
        HashMap<String, String> stationAirData = new HashMap<>();
        JSONArray a = json.getJSON(airURL);

        // Loop through each item
        for (Object o : a) {
            JSONObject json = (JSONObject) o;
            JSONObject param = (JSONObject) json.get("param");
            stationAirData.put(param.get("paramCode").toString(), json.get("id").toString());
        }
        return stationAirData;
    }

    /**
     * function rewrote element index to id
     * @param index map of elements and their id`s from GIOS API
     * @return map where id of element is rewrote to it`s actual value (taken from API)
     */
    private HashMap getData(HashMap index) {
        HashMap data = new HashMap();
        for (Object key : index.keySet()) {
            String keyStr = index.get(key).toString();

            data.put(key.toString(), getValueFromURL(keyStr) );
        }
        return data;
    }

    /**
     * function rewrites element id`s into their latest value from GIOS API
     * @param provinceMap map of provinces with list of their parameters from every station
     */
    public void getAirData(HashMap<String, ArrayList> provinceMap){
        ArrayList<HashMap> listOfMaps = new ArrayList<>();
        for (Object key : provinceMap.keySet()){
            Object listOfData = provinceMap.get(key);
            for (int i = 0; i < ((ArrayList) listOfData).size(); i++) {
                Object listOfIndex = ((ArrayList)listOfData).get(i);
                listOfMaps.add(getData((HashMap) listOfIndex));
            }
            ArrayList<HashMap> tmp = (ArrayList)listOfMaps.clone();
            provinceMap.put(key.toString(),tmp);
            listOfMaps.clear();
        }
    }

    /**
     * function rewrites for every province in provinceMap list of station id`s into list of data from each of this stations
     * @param provinceMap this is map of provinces with list of their stations
     * @return new map, in which list of id`s is changed to list of data from every station
     */
    public  HashMap<String, ArrayList> getListOfDataFromProvinces(HashMap provinceMap){
        ArrayList<HashMap> listOfMaps = new ArrayList<>();
        HashMap<String, ArrayList> provinceStations = new HashMap<>();
        for (Object key : provinceMap.keySet()) {
            Object listOfStations = provinceMap.get(key);
            for (int i = 0; i < ((ArrayList) listOfStations).size(); i++) {
                listOfMaps.add(getDataID(((ArrayList) listOfStations).get(i).toString()));
            }
            ArrayList<HashMap> tmp = (ArrayList)listOfMaps.clone();
            provinceStations.put(key.toString(), tmp);
            listOfMaps.clear();
        }
        return provinceStations;
    }

    /**
     * function gets air information for province on demand
     * @param province name
     * @return air wuality status
     */
    public String getProvinceAirQualityOnDemand(String province) {
        province = province.toUpperCase();
        HashMap provinceMap = getProvinceMap();
        HashMap provinceOnDemand = new HashMap();
        provinceOnDemand.put(province,provinceMap.get(province));
        provinceOnDemand = getListOfDataFromProvinces(provinceOnDemand);
        getAirData(provinceOnDemand);
        provinceOnDemand = sort.sortByElement(provinceOnDemand);
        provinceOnDemand = quality.getAirQuality(provinceOnDemand);

        return provinceOnDemand.get(province).toString();
    }
}
