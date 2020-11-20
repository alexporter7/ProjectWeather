package alexporter7.github.projectweather;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class JSONTask extends AsyncTask<URL, String, String> {

    public static HttpsURLConnection connection;
    public static ArrayList<String> weatherData = new ArrayList<String>();

    @Override
    protected String doInBackground(URL... urls) {

        //======= Init our Line && Buffered Reader =======
        String line = "";
        String jsonResult = "";
        BufferedReader reader = null;

        try {
            //======= Create Connection to API =======
            URL url = urls[0];
            connection = (HttpsURLConnection) url.openConnection();
            connection.connect();
            //======= Create Input Stream && Buffered Reader =======
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer stringBuffer = new StringBuffer();
            //======= Append our String with the Buffered Reader =======
            while((line = reader.readLine()) != null) {
                stringBuffer.append(line);
            }
            jsonResult = stringBuffer.toString();
            return parseJson(jsonResult);
        }
        catch (MalformedURLException e) {
            System.out.println("Improperly formatted URL");
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(connection != null)
                connection.disconnect();
            try {
                if(reader != null)
                    reader.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s.equals("successful")) {
            MainActivity.cityTextView.setText("City: " + weatherData.get(0));
            MainActivity.highTextView.setText("High: " + weatherData.get(1) + " F");
            MainActivity.lowTextView.setText("Low: " + weatherData.get(2) + " F");
            MainActivity.tempTextView.setText("Currently: " + weatherData.get(3) + " F");
        }

        weatherData.clear();

    }

    public static double convertTemperature(double kelvinTemp) {
        return Math.round((((kelvinTemp - 273.15) * 9)/5) + 32);
    }

    public static String parseJson(String json) {

        String outputString = "";

        try {
            //======= Create the Parent Arrays =======
            JSONObject jsonParentObject = new JSONObject(json);
            System.out.println(jsonParentObject);
            //======= Check if Link Returned Valid Result =======
            int returnCode = jsonParentObject.getInt("cod");
            if (returnCode != 200) {
                return null;
            }
            //======= Get the Weather Data =======
            weatherData.add(jsonParentObject.getString("name")); //City Name

            JSONObject weatherDetails = jsonParentObject.getJSONObject("main");
            weatherData.add( //Daily High
                    String.valueOf(convertTemperature(weatherDetails.getDouble("temp_max"))));
            weatherData.add( //Daily Low
                    String.valueOf(convertTemperature(weatherDetails.getDouble("temp_min"))));
            weatherData.add( //Current Temperature
                    String.valueOf(convertTemperature(weatherDetails.getDouble("temp"))));

            outputString = "successful";
            return outputString;

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

}
