package com.shashankbhat.library;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.shashankbhat.library.Constants.API_KEY;
import static com.shashankbhat.library.Constants.CITY_NAME;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    String url = "http://api.openweathermap.org/data/2.5/";
    long time = 0;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        textView = findViewById(R.id.text_view);

        Button get_data_volley = findViewById(R.id.get_data_volley);
        Button get_data_retrofit = findViewById(R.id.get_data_retrofit);

        Button clear = findViewById(R.id.clear);

        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .build();

        get_data_volley.setOnClickListener(view -> {
            time = System.currentTimeMillis();
            requestFromVolley();
        });

        get_data_retrofit.setOnClickListener(view -> {
            time = System.currentTimeMillis();
            requestFromRetrofit();
        });

        clear.setOnClickListener(view -> textView.setText(""));
    }

    public void requestFromVolley() {

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "weather?q=" + CITY_NAME + "&APPID=" + API_KEY,
                response -> {

                    try {
                        JSONObject json = new JSONObject(response);
                        String description = json.getJSONArray("weather").getJSONObject(0).getString("description");
                        JSONObject main = json.getJSONObject("main");
                        String temperature = main.getString("temp");
                        String pressure = json.getJSONObject("main").getString("pressure");
                        String humidity = json.getJSONObject("main").getString("humidity");

                        textView.setText("Volley");
                        textView.append("\nCity Name : " + "Bengaluru");
                        textView.append("\nDescription : " + description);
                        textView.append("\nTemperature  : " + temperature);
                        textView.append("\nPressure  : " + pressure);
                        textView.append("\nHumidity  : " + humidity);
                        textView.append("\n\nTime taken : " + (System.currentTimeMillis() - time));

                    } catch (Exception ex) {
                        textView.setText(ex.getMessage());
                    }

                }, error -> {
            textView.setText("Response: " + error.getMessage());
        });
        queue.add(stringRequest);
    }

    public void requestFromRetrofit() {

        API api = retrofit.create(API.class);

        Call<ResponseBody> repos = api.getStringResponse(CITY_NAME, API_KEY);
        repos.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        String description = json.getJSONArray("weather").getJSONObject(0).getString("description");
                        JSONObject main = json.getJSONObject("main");
                        String temperature = main.getString("temp");
                        String pressure = json.getJSONObject("main").getString("pressure");
                        String humidity = json.getJSONObject("main").getString("humidity");

                        textView.setText("Retrofit");
                        textView.append("\nCity Name : " + "Bengaluru");
                        textView.append("\nDescription : " + description);
                        textView.append("\nTemperature  : " + temperature);
                        textView.append("\nPressure  : " + pressure);
                        textView.append("\nHumidity  : " + humidity);
                        textView.append("\n\nTime taken : " + (System.currentTimeMillis() - time));

                    } catch (IOException e) {
                        textView.setText(e.getMessage());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
