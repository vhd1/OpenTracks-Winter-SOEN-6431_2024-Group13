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

import de.dennisguse.opentracks.data.models.WeatherInformation;

public class WeatherFetchService {
    public static final String API_KEY = "19fb6cae02984e348e314715240804";
    public static final String API_URL = "https://api.weatherapi.com/v1/current.json";

    @Nullable
    public static WeatherInformation fetchWeatherData(double latitudeDouble, double longitudeDouble) {
        try {
            String latitude = String.valueOf(latitudeDouble);
            String longitude = String.valueOf(longitudeDouble);

            URL url = getURL(latitude, longitude);

            HttpURLConnection connection = getHttpURLConnection(url);

            StringBuilder result = getWeatherData(connection);

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

    private static URL getURL(String latitude, String longitude) throws MalformedURLException {
        return new URL(API_URL + "?q=" + latitude +
                "," + longitude + "&key=" + API_KEY);
    }
}