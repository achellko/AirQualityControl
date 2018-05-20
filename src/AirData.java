import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AirData {
    private static String provinceURL = "http://api.gios.gov.pl/pjp-api/rest/station/findAll";
    private static String stationURL = "http://api.gios.gov.pl/pjp-api/rest/station/sensors/";
    private static String airDataURL = "http://api.gios.gov.pl/pjp-api/rest/data/getData/";

    /**
     * function gets jsonArray from url
     * @param url with json data
     * @return JSONArray of data
     */
    private static JSONArray getJSON(String url) {
        JSONParser parser = new JSONParser();
        JSONArray a = null;
        try {
            URL oracle = new URL(url); // URL to Parse
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
               a = (JSONArray) parser.parse(inputLine);
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return a;
    }

    /**
     * function gets jsonObject from url
     * @param url with json data
     * @return JSONObject of data
     */
    private static JSONObject getJSONObject(String url) {
        JSONParser parser = new JSONParser();
        JSONObject a = null;
        try {
            URL oracle = new URL(url); // URL to Parse
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                a = (JSONObject) parser.parse(inputLine);
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return a;
    }

    /**
     * function makes list of unique provinces and then puts each of them as a key into provinceMap
     * with list of stations as values
     * @param stationMap unsorted map of every station and it`s province
     * @return sorted map with key - "province" and value - list of stations
     */
    private static HashMap<String, List<String>> sortByProvince(HashMap<String,String> stationMap){
        Set<String> provinceSet = new HashSet<>(stationMap.values());
        Map<String, List<String>> provinceMap = new HashMap<>();

        for (String s : provinceSet) {
            List<String> IdList = new ArrayList<>();
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
     * function takes data from GIOS API with Poland provinces and list of their stations
     * @return map with key "province" and value with list of id`s from their stations
     */
    private static Map<String, List<String>> getProvinceMap() {
        HashMap<String, String> stationMap = new HashMap<>();
        JSONArray a = getJSON(provinceURL);
        // Loop through each item
        for (Object o : a) {
            JSONObject json = (JSONObject) o;
            JSONObject city = (JSONObject) json.get("city");
            JSONObject commune = (JSONObject) city.get("commune");

            stationMap.put(json.get("id").toString(), commune.get("provinceName").toString());
        }
        Map<String, List<String>> provinceMap = sortByProvince(stationMap);
        return provinceMap;
    }

    /**
     * function gets data for given station from GIOS API
     * @param stationNumber id of station
     * @return map with key - "element name" and value - id of this element
     */
    private static HashMap<String, String> getDataID(String stationNumber) {
        String airURL = stationURL+stationNumber;
        HashMap<String, String> stationAirData = new HashMap<>();
        JSONArray a = getJSON(airURL);
        // Loop through each item
        for (Object o : a) {
            JSONObject json = (JSONObject) o;
            JSONObject param = (JSONObject) json.get("param");
            stationAirData.put(param.get("paramCode").toString(), json.get("id").toString());
        }
        return stationAirData;
    }

    /**
     * function gets the latest value of element form GIOS API
     * @param index of element
     * @return value of element
     */
    private static String getValueFromURL(String index){
        index = airDataURL+index;
        String result = new String ();
        int i = 0;

        JSONObject a = getJSONObject(index);

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
     * function gets
     * @param index map of elements and their id`s from GIOS API
     * @return map where id of element is rewrote to it`s actual value (taken from API)
     */
    private static HashMap getData(HashMap index) {
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
    private static void getAirData(HashMap<String, ArrayList> provinceMap){
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
    private static  HashMap<String, ArrayList> getListOfDataFromProvinces(HashMap provinceMap){
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

    private static HashMap<String, String> countAvg(HashMap<String, ArrayList> map){
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

    private static  HashMap<String, HashMap> sortByElement(HashMap<String, ArrayList> listOfStations) {
        ArrayList a = new ArrayList<>(listOfStations.values());
        HashMap<String, HashMap> newMap = new HashMap<>();
        Set s = new HashSet();
        int count = 0;
        ArrayList province = new ArrayList(listOfStations.keySet());
        ArrayList l;
        HashMap<String, ArrayList> duplicates = new HashMap<>();
        ArrayList duplicateValues = new ArrayList();
        for (Object o : a) {
            l = new ArrayList((ArrayList)o);
            for (Object oo : l) {
                HashMap hh = (HashMap)oo;
                for (Object key : hh.keySet()) {
                    if (!s.add(key.toString())) {
                        if(hh.get(key)!="") {
                            ArrayList al = (ArrayList) duplicates.get(key.toString()).clone();
                            al.add(hh.get(key));
                            duplicates.put(key.toString(), al);
                        }
                    } else {
                        if(hh.get(key)!= "") {
                            ArrayList tmp = new ArrayList();
                            tmp.add(hh.get(key));
                            duplicates.put(key.toString(), tmp);
                        }
                    }
                }
            }
            newMap.put(province.get(count).toString(),countAvg(duplicates));
            count++;
            s.clear();
            duplicates.clear();
        }
        return newMap;
    }

    /**
     * loop that takes data from API every 6 hours (4 times a day)
     */
    private static void timeLoop(){
        try {
            while (true) {
                HashMap m = new HashMap (getProvinceMap());
                HashMap m3 = getListOfDataFromProvinces(m);
                System.out.println(m3.toString());
                getAirData(m3);
                HashMap newHM = sortByElement(m3);
                System.out.println(newHM);
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