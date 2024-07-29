package com.example.kuntakatselu2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherFragment extends Fragment {

    private EditText etMunicipality;
    private TextView tvWeatherInfo;
    private ImageView ivWeatherIcon;
    private TextView tvForecast;
    private static final String API_KEY = "89fad8d8ae0fe15ec0b883b3b9e30fcf";
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        etMunicipality = view.findViewById(R.id.etMunicipality);
        tvWeatherInfo = view.findViewById(R.id.tvWeatherInfo);
        ivWeatherIcon = view.findViewById(R.id.ivWeatherIcon);
        tvForecast = view.findViewById(R.id.tvForecast);
        progressBar = view.findViewById(R.id.progressBar);
        Button btnGetWeather = view.findViewById(R.id.btnGetWeather);

        btnGetWeather.setOnClickListener(v -> fetchWeatherData());

        return view;
    }

    private void fetchWeatherData() {
        String municipality = etMunicipality.getText().toString();
        if (!municipality.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            tvWeatherInfo.setText("");
            tvForecast.setText("");
            ivWeatherIcon.setImageDrawable(null);

            String apiUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + municipality + ",fi&units=metric&appid=" + API_KEY;

            DataRetriever.fetchData(apiUrl, "", new DataRetriever.DataFetchListener() {
                @Override
                public void onDataFetched(JSONObject result) {
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONArray forecastList = result.getJSONArray("list");
                        JSONObject currentWeather = forecastList.getJSONObject(0);

                        JSONObject main = currentWeather.getJSONObject("main");
                        double temperature = main.getDouble("temp");
                        int humidity = main.getInt("humidity");

                        JSONArray weatherArray = currentWeather.getJSONArray("weather");
                        JSONObject weather = weatherArray.getJSONObject(0);
                        String description = weather.getString("description");
                        String iconCode = weather.getString("icon");

                        String weatherInfo = String.format(Locale.getDefault(),
                                "Current weather:\nTemperature: %.1f°C\nHumidity: %d%%\nDescription: %s",
                                temperature, humidity, description);

                        tvWeatherInfo.setText(weatherInfo);

                        // Load weather icon using Glide
                        String iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png";
                        loadWeatherIcon(iconUrl);

                        updateForecast(forecastList);
                    } catch (Exception e) {
                        tvWeatherInfo.setText("Error parsing weather data: " + e.getMessage());
                    }
                }

                @Override
                public void onError(String error) {
                    progressBar.setVisibility(View.GONE);
                    tvWeatherInfo.setText("Error fetching weather data: " + error);
                }
            });
        } else {
            tvWeatherInfo.setText("Please enter a municipality name.");
        }
    }
    private void loadWeatherIcon(String iconUrl) {
        if (getContext() != null) {
            Glide.with(getContext())
                    .load(iconUrl)
                    .into(ivWeatherIcon);
        }
    }

    private void updateForecast(JSONArray forecastList) throws Exception {
        StringBuilder forecastBuilder = new StringBuilder("Forecast:\n");
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM", Locale.getDefault());
        for (int i = 1; i < forecastList.length(); i += 8) { // Every 24 hours (3-hour steps)
            JSONObject forecast = forecastList.getJSONObject(i);
            String dateText = forecast.getString("dt_txt");
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dateText);
            JSONObject forecastMain = forecast.getJSONObject("main");
            double forecastTemp = forecastMain.getDouble("temp");
            String forecastDesc = forecast.getJSONArray("weather").getJSONObject(0).getString("description");

            forecastBuilder.append(String.format(Locale.getDefault(),
                    "%s: %.1f°C, %s\n",
                    sdf.format(date), forecastTemp, forecastDesc));
        }
        tvForecast.setText(forecastBuilder.toString());
    }
}