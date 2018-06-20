package app;

import config.Config;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        AirData ad = context.getBean("airData",AirData.class);
        ad.timeLoop();

        /**
        CSVReaderExample csv = new CSVReaderExample();
        csv.readCSV(new File(args[0]));
         **/
    }
}
