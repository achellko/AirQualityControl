import java.util.*;

public class AirData {

    private static Sort sort = new Sort();
    private static Quality quality = new Quality();
    private static Data data = new Data();

    /**
     * loop that takes data from API every 6 hours (4 times a day)
     */
    private static void timeLoop(){
        HashMap m = new HashMap (data.getProvinceMap());
        m = data.getListOfDataFromProvinces(m);
        System.out.println(m.toString());
        //output: {ŚWIĘTOKRZYSKIE=[{NO2=5083, O3=14878, SO2=5087, PM10=5085}
        // PROVINCE name + lists of elements and their id`s

        try {
            while (true) {
                data.getAirData(m);
                System.out.println(m.toString());
                //output: {ŚWIĘTOKRZYSKIE=[{NO2=7.0, O3=91.84, SO2=9.73, PM10=29.25}
                // PROVINCE name + lists of element values from all stations

                HashMap newHM = sort.sortByElement(m);
                System.out.println(newHM);
                //output: ŚWIĘTOKRZYSKIE={NO2=4.2, O3=114.78, PM2.5=3.78, SO2=3.84, PM10=10.0, CO=216.67, C6H6=0.08}
                // PROVINCE name + list of element values

                newHM = quality.getAirQuality(newHM);
                System.out.println(newHM);
                //output: ŚWIĘTOKRZYSKIE=Good, PODKARPACKIE=Good

                System.out.println("Air quality for province "+"świętokrzyskie "+"is: "+data.getProvinceAirQualityOnDemand("ŚWIĘTOKRZYSKIE"));

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