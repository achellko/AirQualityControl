package app;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CSVReaderExample {

    public ArrayList readCSV(File file) {
        File csvFile = file;
        List<Measurement> mList = new ArrayList<Measurement>();

        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(csvFile));
            String[] line;
            while ((line = reader.readNext()) != null) {
                try {
                    mList.add(new Measurement(Component.valueOf((line[1].replace(".","")).toUpperCase()), District.valueOf(line[0].toUpperCase()),
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(line[2]), new Double(line[3])));
                } catch (ParseException e) {
                    System.out.println("Error while creating date");
                }
            }
            System.out.println(mList.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (ArrayList) mList;
    }

}