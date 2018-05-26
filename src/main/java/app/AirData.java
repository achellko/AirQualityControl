package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component("airData")
public class AirData {

    private Sort sort;
    private Quality quality;
    private Data data;

    @Autowired
    public AirData(Sort sort, Quality quality, Data data) {
        this.sort = sort;
        this.quality = quality;
        this.data = data;
    }

    /**
     * loop that takes data from API every 6 hours (4 times a day)
     */
    public void timeLoop(){
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

}