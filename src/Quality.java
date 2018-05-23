import java.util.HashMap;

public class Quality {

    /**
     * function counts air quality
     * @param index map of elements and their values from one province
     * @return air quality status
     */
    private String getQualityStatus(HashMap index){
        Double PM10 = (Double)index.get("PM10");
        Double PM2_5 = (Double)index.get("PM2.5");
        Double O3 = (Double)index.get("O3");
        Double NO2 = (Double)index.get("NO2");
        Double SO2 = (Double)index.get("SO2");
        Double C6H6 = (Double)index.get("C6H6");
        Double CO = (Double)index.get("CO")*0.001;

        if (PM10 < 21.0 && PM2_5 < 13.0 && O3 < 71.0 && NO2 < 41.0 && SO2 < 51.0 && C6H6 < 6.0 && CO <3.0) {
            return "Very good";
        } else if (PM10 < 61.0 && PM2_5 < 37.0 && O3 < 121.0 && NO2 < 101.0 && SO2 < 101.0 && C6H6 < 11.0 && CO <7.0) {
            return "Good";
        } else if (PM10 < 101.0 && PM2_5 < 61.0 && O3 < 151.0 && NO2 < 151.0 && SO2 < 201.0 && C6H6 < 16.0 && CO <11.0) {
            return "Tolerable";
        } else if (PM10 < 141.0 && PM2_5 < 85.0 && O3 < 181.0 && NO2 < 201.0 && SO2 < 351.0 && C6H6 < 21.0 && CO <15.0){
            return "Adequate";
        } else if(PM10 < 201.0 && PM2_5 < 121.0 && O3 < 241.0 && NO2 < 401.0 && SO2 < 501.0 && C6H6 < 51.0 && CO <21.0) {
            return "Bad";
        } else if(PM10 > 201.0 && PM2_5 > 121.0 && O3 > 241.0 && NO2 > 401.0 && SO2 > 501.0 && C6H6 > 51.0 && CO > 21.0) {
            return "Very bad";
        } else {
            return "No index";
        }
    }

    /**
     * function counts air quality for map of provinces
     * @param provinceMap map with key - "province" and value - list of elements and their values
     * @return map with key - "province" and value - "air quality status"
     */
    public HashMap getAirQuality(HashMap<String, HashMap> provinceMap){
        HashMap result = new HashMap();
        for (Object province : provinceMap.keySet()) {
            result.put(province.toString(),getQualityStatus(provinceMap.get(province.toString())));
        }
        return result;
    }
}
