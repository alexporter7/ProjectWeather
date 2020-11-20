package alexporter7.github.projectweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static final String API_KEY = "853e66aa0d1362f88e694d4c85f22b33";
    public static String apiLink = "https://api.openweathermap.org/data/2.5/weather?q=CITY&appid=APIKEY";

    public static TextView cityTextView;
    public static TextView highTextView;
    public static TextView lowTextView;
    public static TextView tempTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //======= Setup the Text Views to be Accessed in JSONTask =======
        cityTextView = (TextView) findViewById(R.id.city);
        highTextView = (TextView) findViewById(R.id.high);
        lowTextView = (TextView) findViewById(R.id.low);
        tempTextView = (TextView) findViewById(R.id.temp);

    }

    public static String createApiLink(String city, String apiKey) {
        if (city != null && apiKey != null) {
            String formattedURL = apiLink
                    .replace("CITY", city)
                    .replace("APIKEY", apiKey);
            return formattedURL;
        }
        return null;
    }

    public void searchWeather(View view) throws MalformedURLException {

        EditText cityTextBox = (EditText) findViewById(R.id.editTextTextPersonName);
        String city = cityTextBox.getText().toString();
        URL newUrl = new URL(createApiLink(city, API_KEY));
        new JSONTask().execute(newUrl);

    }
}