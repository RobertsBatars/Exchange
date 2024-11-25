package com.example.exchange;

import android.os.AsyncTask;
import android.util.Log;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DataLoader extends AsyncTask<Void, Void, List<CurrencyRate>> {
    private static final String TAG = "DataLoader";
    private static final String RATES_URL = "http://www.floatrates.com/daily/usd.xml";
    private final DataLoadCallback callback;
    private String errorMessage;

    public interface DataLoadCallback {
        void onDataLoaded(List<CurrencyRate> rates);
        void onError(String error);
    }

    public DataLoader(DataLoadCallback callback) {
        this.callback = callback;
    }

    @Override
    protected List<CurrencyRate> doInBackground(Void... voids) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(RATES_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                errorMessage = "Server returned code: " + responseCode;
                Log.e(TAG, errorMessage);
                return null;
            }

            InputStream stream = connection.getInputStream();
            List<CurrencyRate> rates = Parser.parseXML(stream);

            stream.close();

            if (rates == null || rates.isEmpty()) {
                errorMessage = "No data received from server";
                Log.e(TAG, errorMessage);
                return null;
            }

            return rates;

        } catch (Exception e) {
            errorMessage = "Error: " + e.getMessage();
            Log.e(TAG, "Error loading data", e);
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(List<CurrencyRate> rates) {
        if (rates != null) {
            callback.onDataLoaded(rates);
        } else {
            callback.onError(errorMessage != null ? errorMessage : "Failed to load currency rates");
        }
    }
}