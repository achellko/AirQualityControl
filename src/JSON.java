import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class JSON {

    /**
     * function gets jsonArray from url
     * @param url with json data
     * @return JSONArray of data
     */
    public JSONArray getJSON(String url) {
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
    public JSONObject getJSONObject(String url) {
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

}
