package in.kanishkkumar.programmingtest.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import in.kanishkkumar.programmingtest.R;
import in.kanishkkumar.programmingtest.model.ResponseItem;

public class DetailActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ResponseItem responseItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            finish();

        if (bundle.containsKey("data")) {
            responseItem = (ResponseItem) bundle.get("data");

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            ImageView imvProfileImage = findViewById(R.id.imvProfile);
            TextView txtTitle = findViewById(R.id.txtTitle);

            txtTitle.setText(responseItem.getName());
            Glide.with(this).load(responseItem.getPicture())
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imvProfileImage);
        } else finish();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng person = new LatLng(responseItem.getLocation().getLatitude(),responseItem.getLocation().getLongitude());
        Marker marker = mMap.addMarker(new MarkerOptions().position(person).title(responseItem.getName()+" <"+responseItem.getEmail()+">"));mMap.moveCamera(CameraUpdateFactory.newLatLng(person));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(person, 11.0f));
        marker.showInfoWindow();


    }
}
