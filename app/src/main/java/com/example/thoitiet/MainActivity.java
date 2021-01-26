package com.example.thoitiet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.example.thoitiet.Adapter.AdapterThoiTiet;
import com.example.thoitiet.Contexts.ContectThoiTiet;
import com.example.thoitiet.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements LocationListener {
    String urlAPI,urlAPI1;
    ActivityMainBinding binding;
    LocationManager manager;
    Geocoder geocoder;
    Calendar cal;
    AdapterThoiTiet adapterThoiTiet ;
    List<ContectThoiTiet> contectThoiTiets;
    List<ContectThoiTiet> contectThoiTietsHienThi;
    boolean check =true;

Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        setSupportActionBar(binding.toolBar);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.requestLocationUpdates(manager.GPS_PROVIDER, 1000, 1, this);
        startTimer();
        binding.btnTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String diaDiem = binding.etTim.getText().toString();
                urlAPI="http://api.openweathermap.org/data/2.5/weather?q="+diaDiem+"&appid=298def9ce8d941741e4134b34b2685c3";
                urlAPI1="http://api.openweathermap.org/data/2.5/forecast?q="+diaDiem+"&appid=298def9ce8d941741e4134b34b2685c3";
                contectThoiTiets=new ArrayList<>();
                contectThoiTietsHienThi = new ArrayList<>();
                new DogetData(urlAPI).execute();
                new DogetData1(urlAPI1).execute();
                adapterThoiTiet= new AdapterThoiTiet(contectThoiTietsHienThi);
            }
        });


    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            cal = Calendar.getInstance();
            binding.tvTime.setText(dateFormat.format(cal.getTime()));
            dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            binding.tvDate.setText(dateFormat.format(cal.getTime()));
            startTimer();
        }
    };
    public void startTimer() {
        handler.postDelayed(runnable, 1000);
    }
    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        try {

            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addresses.size() >= 1) {
                urlAPI="http://api.openweathermap.org/data/2.5/weather?q="+addresses.get(0).getLocality()+"&appid=298def9ce8d941741e4134b34b2685c3";
                urlAPI1="http://api.openweathermap.org/data/2.5/forecast?q="+addresses.get(0).getLocality()+"&appid=298def9ce8d941741e4134b34b2685c3";
                contectThoiTiets=new ArrayList<>();
                contectThoiTietsHienThi = new ArrayList<>();
                new DogetData(urlAPI).execute();
                new DogetData1(urlAPI1).execute();
                adapterThoiTiet= new AdapterThoiTiet(contectThoiTietsHienThi);
            }
        } catch (IOException ex) {

            ex.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    public class DogetData1 extends AsyncTask<Void, Void, Void> {
        String result = "";
        String urlApi;

        public DogetData1(String url) {
            this.urlApi = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(urlApi);
                URLConnection connection = url.openConnection();
                InputStream is = connection.getInputStream();
                int byteCharacter;
                while ((byteCharacter = is.read()) != -1) {
                    result += (char) byteCharacter;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            try {
                JSONObject jsonObject = new JSONObject(result);

                JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                ContectThoiTiet contectThoiTiet ;
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    Date date = stringToDate(jsonObject1.getString("dt_txt"));
                    if(contectThoiTiets.size()>0){
                        for(int j=0;j<contectThoiTiets.size();j++){
                            if(date.compareTo(stringToDate1(contectThoiTiets.get(j).getDay()))==0){
                                check=false;
                                break;
                            }
                        }
                    }
                    if(check){
                        JSONArray jsonArray1 = jsonObject1.getJSONArray("weather");
                        JSONObject object2 = jsonArray1.getJSONObject(0);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("main");

                        DateFormat formatDate=new SimpleDateFormat("EEEE");


                        contectThoiTiet = new ContectThoiTiet(object2.getString("main"),nhietDo(jsonObject2.getString("temp_max")),nhietDo(jsonObject2.getString("temp_min")),formatDate.format(date));
                        contectThoiTietsHienThi.add(contectThoiTiet);
                        contectThoiTiet = new ContectThoiTiet(object2.getString("main"),nhietDo(jsonObject2.getString("temp_max")),nhietDo(jsonObject2.getString("temp_min")),dateToString(date));
                        contectThoiTiets.add(contectThoiTiet);

                    }
                    check=true;
                }

                RecyclerView.LayoutManager layoutManager =new GridLayoutManager(getBaseContext(),1,RecyclerView.VERTICAL,false);
                binding.rcv.setAdapter(adapterThoiTiet);
                binding.rcv.setLayoutManager(layoutManager);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public class DogetData extends AsyncTask<Void, Void, Void> {
        String result = "";
        String urlApi;

        public DogetData(String url) {
            this.urlApi = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(urlApi);
                URLConnection connection = url.openConnection();
                InputStream is = connection.getInputStream();
                int byteCharacter;
                while ((byteCharacter = is.read()) != -1) {
                    result += (char) byteCharacter;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            try {
                JSONObject jsonObject = new JSONObject(result);
                //nhiet do,do am
                JSONObject object = jsonObject.getJSONObject("main");
                binding.nhietDoHienTai.setText(nhietDo(object.getString("temp")));
                binding.nhietDoCaoNhat.setText(nhietDo(object.getString("temp_max")));
                binding.nhietDoThapNhat.setText(nhietDo(object.getString("temp_min")));
                binding.tvDoAm.setText(object.getString("humidity")+"%");
                //thanh pho
                binding.viTri.setText(jsonObject.getString("name"));
                //Nuoc
                JSONObject object1 = jsonObject.getJSONObject("sys");
                binding.diaDiem.setText(object1.getString("country"));
                //thoi tiet
                JSONArray jsonArray = jsonObject.getJSONArray("weather");
                JSONObject object2 = jsonArray.getJSONObject(0);
                binding.thoiTiet.setText(object2.getString("main"));
                //toc do gio
                JSONObject object3 = jsonObject.getJSONObject("wind");
                binding.tvWind.setText(object3.getString("speed")+"mph");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public String nhietDo(String nd){
        float doK = Float.parseFloat(nd);
        float doC = doK-273;
        String kq = String.format("%2.1f",doC);
        return kq;
    }
    public Date stringToDate(String d){
        Date da = null;
        try {
            String dd = d.substring(0,10);
            da = new SimpleDateFormat("yyyy-MM-dd").parse(dd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return da;
    }
    public Date stringToDate1(String d){
        Date da = null;
        try {
            da = new SimpleDateFormat("dd-MM-yyyy").parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return da;
    }
    public String dateToString(Date date){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.format(date);
    }
}