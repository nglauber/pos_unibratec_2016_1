package br.com.maps;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION = 0;
    private GoogleMap mMap;
    private LatLng mOrigin;
    private LatLng mDest;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        exibirLocalAtual(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exibirLocalAtual(false);
            }
        }
    }

    private void exibirLocalAtual(boolean requestPermission) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            Location lastKnownLocation =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastKnownLocation != null) {
                LatLng nossaPosition = null;

                Double latitude = lastKnownLocation.getLatitude();
                Double longitude = lastKnownLocation.getLongitude();
                nossaPosition = new LatLng(latitude, longitude);

                mMap.setMyLocationEnabled(true);
                mMap.addMarker(new MarkerOptions().position(nossaPosition)
                        .title("Meu local atual")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(nossaPosition));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nossaPosition, 18));
            }

        } else if (requestPermission) {

            ActivityCompat.requestPermissions(this,
                    new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION },
                    REQUEST_LOCATION);
        }
    }



    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * Método responsável para realizar o evendo do Click na tela de busca da Origem e Destino
     * @param view
     */
    public void onSearch(View view) {

        TextView mTextOrigem = (TextView) findViewById(R.id.textOrigem);
        TextView mTextDestino = (TextView) findViewById(R.id.textDestino);
        List<Address> locationOrigin = null;
        List<Address> locationDest = null;
        Geocoder geocoder = new Geocoder(this);

        String desOrigin = mTextOrigem.getText().toString();
        String desDestino = mTextDestino.getText().toString();

        try {
            locationOrigin = geocoder.getFromLocationName(desOrigin, 1);
            locationDest = geocoder.getFromLocationName(desDestino, 2);

            if (locationOrigin == null || locationOrigin.isEmpty()
                    || locationDest == null || locationDest.isEmpty() ){
                Toast.makeText(getBaseContext(),"Origem/Destino não encontrado.",Toast.LENGTH_LONG).show();
                return;
            }

            mOrigin = new LatLng(locationOrigin.get(0).getLatitude(), locationOrigin.get(0).getLongitude());
            mDest = new LatLng(locationDest.get(0).getLatitude(), locationDest.get(0).getLongitude());

            mMap.addMarker(new MarkerOptions().position(mOrigin).title("Origem"));
            mMap.addMarker(new MarkerOptions().position(mDest).title("Destino"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mOrigin, 18));

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(mOrigin, mDest);
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Classe Task responsável em realizar a busca dos dados da origem e destino através da URL da API.
     * Depois executa a Task com o retorno dos dados
     */
    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * Método chamado da Task DownloadTask responsável em realizar a busca dos dados da origem e destino através da URL da API.
     * @param strUrl
     * @return
     * @throws IOException
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /**
     * Classe Task chamado da Task DownloadTask, responsável em carregar os dados contendo todos os pontos entre a origem e destino para realizar a marcação no mapa.
     */
    private class ParserTask extends AsyncTask<String, Integer, PolylineOptions> {

        @Override
        protected PolylineOptions doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            PolylineOptions lineOptions = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                // Starts parsing data
                routes = parser.parse(jObject);

                ArrayList<LatLng> points = null;

                MarkerOptions markerOptions = new MarkerOptions();

                if(routes.size() > 0){
                    // Traversing through all the routes
                    for(int i = 0; i < routes.size(); i++){
                        points = new ArrayList<LatLng>();
                        lineOptions = new PolylineOptions();

                        // Fetching i-th route
                        List<HashMap<String, String>> path = routes.get(i);

                        // Fetching all the points in i-th route
                        for(int j=0;j<path.size();j++){
                            HashMap<String,String> point = path.get(j);

                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);

                            points.add(position);
                        }

                        // Adding all the points in the route to LineOptions
                        lineOptions.addAll(points);
                        lineOptions.width(10);
                        lineOptions.color(Color.RED);
                    }
                    // Drawing polyline in the Google Map for the i-th route
                    mMap.addPolyline(lineOptions);
                }

            }catch(Exception e){
                e.printStackTrace();
            }
            return lineOptions;
        }

        @Override
        protected void onPostExecute(PolylineOptions result) {
            if (result != null){
                mMap.addPolyline(result);
                LatLngBounds area = new LatLngBounds.Builder()
                        .include(mOrigin)
                        .include(mDest)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(area, 50)); // 50 padding

            } else {
                Toast.makeText(getBaseContext(),"Origem/Destino não encontrado.",Toast.LENGTH_LONG).show();
            }
        }
    }
}
