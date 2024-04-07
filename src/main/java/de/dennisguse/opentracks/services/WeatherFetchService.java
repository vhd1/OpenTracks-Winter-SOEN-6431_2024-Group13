package de.dennisguse.opentracks.services;

import androidx.annotation.Nullable;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherFetchService {
    public static final String API_KEY = "b95112d362764c318e8c58042ab6072f";
    public static final String API_URL = "http://api.weatherstack.com/current";

    @Nullable
    public static WeatherInfo fetchWeatherData(double latitude, double longitude) {
        try {

            String latitudeS = String.valueOf(latitude);
            String longitudeS = String.valueOf(longitude);

            URL url = getURL(latitudeS, longitudeS);

            HttpURLConnection connection = getHttpURLConnection(url);
            StringBuilder result = getWeatherData(connection);
            return null;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static StringBuilder getWeatherData(HttpURLConnection connection) throws IOException {
        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder result = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        return result;
    }

    private static HttpURLConnection getHttpURLConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        return connection;
    }

    @SuppressWarnings("deprecation")
    private static URL getURL(String latitude, String longitude) throws MalformedURLException {
        return new URL(API_URL + "?access_key=" + API_KEY +
                "&query=" + latitude + "," + longitude);
    }
}
