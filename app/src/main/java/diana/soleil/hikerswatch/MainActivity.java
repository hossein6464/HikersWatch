package diana.soleil.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;
    TextView textView1, textView2, textView3, textView4, textView5;
    double latitude, logtitude, atitude, accuracy;
    Geocoder geocoder;
    String address;
    List<Address> addressesList;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView6);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                logtitude = location.getLongitude();
                accuracy = location.getAccuracy();
                atitude = location.getAltitude();
                textView1.setText("Latitude: " + String.valueOf(latitude));
                textView2.setText("Longtitude: " + String.valueOf(logtitude));
                textView3.setText("Accuracy: " + String.valueOf(accuracy));
                textView4.setText("Altitude: " + String.valueOf(atitude));
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                address = "";
                try {
                    addressesList = geocoder.getFromLocation(latitude,logtitude,1);
                    Log.i("Address", addressesList.toString());
                    if (addressesList.get(0) != null && addressesList.size()>0) {
                        if (addressesList.get(0).getFeatureName() != null) {
                            address+= addressesList.get(0).getFeatureName() + " - ";
                        }
                        if (addressesList.get(0).getThoroughfare() != null) {
                            address+= addressesList.get(0).getThoroughfare() + ", ";
                        }
                        if (addressesList.get(0).getLocality() != null) {
                            address+= addressesList.get(0).getLocality() + " ";
                        }
                        if (addressesList.get(0).getPostalCode() != null) {
                            address+= addressesList.get(0).getPostalCode() + " ";
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                textView5.setText(address);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
            }
        }


    }
}