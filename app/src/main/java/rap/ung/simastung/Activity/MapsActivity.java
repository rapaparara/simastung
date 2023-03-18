package rap.ung.simastung.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import rap.ung.simastung.R;

public class MapsActivity extends Activity implements LocationListener {
    private MapView mapView;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the OpenStreetMap configuration
        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE));

        // Set the layout for the activity
        setContentView(R.layout.activity_maps);

        // Find the MapView in the layout
        mapView = (MapView) findViewById(R.id.mapView);
        // Set the tile source to OpenStreetMap
        if (mapView != null) {
            mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        }

        // Set the initial map view to the current location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                GeoPoint startPoint = new GeoPoint(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                mapView.getController().setCenter(startPoint);
                mapView.getController().setZoom(18);

                // Create a new marker and set its position and icon
                Marker currentMarker = new Marker(mapView);
                currentMarker.setPosition(startPoint);
                Drawable iconDrawable = getResources().getDrawable(R.drawable.ic_location);
                Bitmap iconBitmap = ((BitmapDrawable) iconDrawable).getBitmap();
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(iconBitmap, 45, 45, false);
                Drawable resizedIcon = new BitmapDrawable(getResources(), resizedBitmap);
                currentMarker.setIcon(resizedIcon);



                // Set the anchor to the center of the icon
                currentMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);


                // Add the marker to the map
                mapView.getOverlays().add(currentMarker);
                mapView.invalidate();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDetach();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Get the current location
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
        Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        GeoPoint currentPoint = new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());

        // Create a new marker and set its position
        Marker currentMarker = new Marker(mapView);
        currentMarker.setPosition(currentPoint);
        Drawable icon = getResources().getDrawable(R.drawable.ic_location);
        currentMarker.setIcon(icon);

        // Add the marker to the map
        mapView.getOverlays().add(currentMarker);
        mapView.invalidate();
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}