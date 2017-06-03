package com.example.usuario.sodamovil.Fragmentos;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.example.usuario.sodamovil.AcercaDeActivity;
import com.example.usuario.sodamovil.AgregarRestauranteActivity;
import com.example.usuario.sodamovil.AgregarRestauranteNombre;
import com.example.usuario.sodamovil.BaseDeDatos.DataBase;
import com.example.usuario.sodamovil.Entidades.DataHelper;
import com.example.usuario.sodamovil.Entidades.Restaurante;
import com.example.usuario.sodamovil.Entidades.RestauranteSuggestion;
import com.example.usuario.sodamovil.LoginActivity;
import com.example.usuario.sodamovil.MisRestaurantes;
import com.example.usuario.sodamovil.R;
import com.example.usuario.sodamovil.RestauranteActivity;
import com.example.usuario.sodamovil.RestaurantesBusqueda;
import com.example.usuario.sodamovil.VariablesGlobales;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.example.usuario.sodamovil.Entidades.DataHelper.clearRestaurantes;
import static com.example.usuario.sodamovil.Entidades.DataHelper.inicializarRestaurantes;
import static com.example.usuario.sodamovil.Entidades.DataHelper.setRestauranteSuggestionHistory;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.twitter.sdk.android.Twitter.logOut;


public class GMFragmento extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnInfoWindowClickListener  {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private FloatingSearchView mSearchView;
    private ArrayList<Restaurante> restaurantes;
    private DrawerLayout drawerLayoutFromView;
    FirebaseAuth firebaseAuth;
    private String mLastQuery = "";
    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragmento_mapa, null, false);
        firebaseAuth= FirebaseAuth.getInstance();
        mSearchView= (FloatingSearchView) v.findViewById(R.id.floating_search_view);
        mSearchView.attachNavigationDrawerToMenuButton(drawerLayoutFromView);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();



        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                ((AppCompatActivity) getActivity()).onMenuItemSelected(item.getItemId(),item);
                 OnMenuItemClick(item,v);
            }

        });




        setupFloatingSearch();

        return v;
    }

    public void OnMenuItemClick(MenuItem item,View view){
        int id = item.getItemId();
        if (id == R.id.menuItemAgregarRestaurante) {
            VariablesGlobales.getInstance().restauranteAgregar = null;
            VariablesGlobales.getInstance().posicionAgregarRestaurante =null;
            Intent intento = new Intent(getApplicationContext(), AgregarRestauranteNombre.class);
            startActivity(intento);

        } else if (id == R.id.menuItemAcercaDe) {
            Intent intento = new Intent(getApplicationContext(), AcercaDeActivity.class);
            startActivity(intento);
        } else if (id == R.id.menuItemCerrarSesion) {
            DialogoSiNoLogOut(view);
        } else if (id == R.id.menuItemMisRestaurantes) {
            Intent intento = new Intent(getApplicationContext(), MisRestaurantes.class);
            startActivity(intento);
        }
    }

    public void DialogoSiNoLogOut(View view){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
        builder1.setMessage("Estas seguro de que quieres cerrar sesión");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Cerrar Sesión",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {logOut(); } });
        builder1.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { } });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    };

    public void logOut(){
        firebaseAuth.signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }


    public void setDrawerLayout(DrawerLayout drawerLayoutFromView){
        this.drawerLayoutFromView=drawerLayoutFromView;
    }

    private void pintarRestaurantes() {
        final DataBase db= DataBase.getInstance();
        db.getmDatabaseReference().child("Restaurantes_Todos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(restaurantes.size() > 0)
                    restaurantes.clear();
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Restaurante restaurante = postSnapshot.getValue(Restaurante.class);
                    restaurantes.add(restaurante);
                }
                for (Restaurante rest:restaurantes) {

                    LatLng latLng = new LatLng(rest.getLatitudesH(), rest.getLatitudesV());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(rest.getNombre());
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    mMap.addMarker(markerOptions);
                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MapFragment fragment= (  MapFragment ) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this.getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(16).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (LocationListener) this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this.getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        restaurantes = new ArrayList<Restaurante>();
        pintarRestaurantes();
        mMap.setOnInfoWindowClickListener(this);

        mMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        boolean ya=false;
        Restaurante actual = null;
        for(int i=0;i<restaurantes.size() && ya==false;i++){
            if( restaurantes.get(i).getLatitudesH().equals(marker.getPosition().latitude) && restaurantes.get(i).getLatitudesV().equals(marker.getPosition().longitude)){
                actual = restaurantes.get(i);
                VariablesGlobales.getInstance().setRestauranteActual(actual);
                ya=true;
                pasarActividadRestaurante();
            }
        }

    }

    public void pasarActividadRestaurante(){

        Intent intento = new Intent(getActivity(), RestauranteActivity.class);
        startActivity(intento);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this.getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(this.getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this.getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            //You can add here other case statements according to your requirement.
        }
    }



    private void setupFloatingSearch() {
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {

                    //this shows the top left circular progress
                    //you can call it where ever you want, but
                    //it makes sense to do it when loading something in
                    //the background.
                    mSearchView.showProgress();

                    //simulates a query call to a data source
                    //with a new query.
                    DataHelper.findSuggestions(getActivity(), newQuery, 5,
                            FIND_SUGGESTION_SIMULATED_DELAY, new DataHelper.OnFindSuggestionsListener() {

                                @Override
                                public void onResults(List<RestauranteSuggestion> results) {

                                    //this will swap the data and
                                    //render the collapse/expand animations as necessary
                                    mSearchView.swapSuggestions(results);

                                    //let the users know that the background
                                    //process has completed
                                    mSearchView.hideProgress();
                                }
                            });
                }

               // Log.d(TAG, "onSearchTextChanged()");
            }
        });


        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {

                RestauranteSuggestion restauranteSuggestion = (RestauranteSuggestion) searchSuggestion;
                setRestauranteSuggestionHistory(restauranteSuggestion);
                getRestauranteFromSearch(restauranteSuggestion);
            }
            @Override
            public void onSearchAction(String query) {
                DataHelper.findRestaurante(query,
                        new DataHelper.OnFindRestauranteListener() {
                            @Override
                            public void onResults(List<RestauranteSuggestion> results) {
                                   List<Restaurante> restaurantes= new LinkedList<Restaurante>();
                                   for (RestauranteSuggestion restauranteSuggestion: results) {
                                       Restaurante restaurante= new Restaurante();
                                       restaurante.setNombre(restauranteSuggestion.getBody());
                                       restaurante.setCodigo(restauranteSuggestion.getCodigoFireBase());
                                       restaurante.setDescripcion(restauranteSuggestion.getDescripcion());
                                       restaurantes.add(restaurante);
                                    }
                                VariablesGlobales.getInstance().setRestaurantesResultadoBuscar(restaurantes);
                                Intent intento = new Intent(getActivity(), RestaurantesBusqueda.class);
                                startActivity(intento);
                            }
                        });
            }
        });


        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {

                mSearchView.swapSuggestions(DataHelper.getHistory(getActivity(), 3));
            }

            @Override
            public void onFocusCleared() {

                //set the title of the bar so that when focus is returned a new query begins
                mSearchView.setSearchBarTitle("");

                //you can also set setSearchText(...) to make keep the query there when not focused and when focus returns
                //mSearchView.setSearchText(searchSuggestion.getBody());

                //Log.d(TAG, "onFocusCleared()");
            }
        });



        /*
         * Here you have access to the left icon and the text of a given suggestion
         * item after as it is bound to the suggestion list. You can utilize this
         * callback to change some properties of the left icon and the text. For example, you
         * can load the left icon images using your favorite image loading library, or change text color.
         *
         *
         * Important:
         * Keep in mind that the suggestion list is a RecyclerView, so views are reused for different
         * items in the list.
         */
        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item, int itemPosition) {
                RestauranteSuggestion restauranteSuggestion = (RestauranteSuggestion) item;

                String textColor =  "#000000";
                String textLight =  "#787878";

                if (restauranteSuggestion.getIsHistory()) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_history_black_24dp, null));

                    Util.setIconColor(leftIcon, Color.parseColor(textColor));
                    leftIcon.setAlpha(.36f);
                } else {
                    leftIcon.setAlpha(0.0f);
                    leftIcon.setImageDrawable(null);
                }

                textView.setTextColor(Color.parseColor(textColor));
                String text = restauranteSuggestion.getBody()
                        .replaceFirst(mSearchView.getQuery(),
                                "<font color=\"" + textLight + "\">" + mSearchView.getQuery() + "</font>");
                textView.setText(Html.fromHtml(text));
            }

        });



        /*
         * When the user types some text into the search field, a clear button (and 'x' to the
         * right) of the search text is shown.
         *
         * This listener provides a callback for when this button is clicked.
         */
        /*
        mSearchView.setOnClearSearchActionListener(new FloatingSearchView.OnClearSearchActionListener() {
            @Override
            public void onClearSearchClicked() {

                Log.d(TAG, "onClearSearchClicked()");
            }
        });*/
    }


    private void getRestauranteFromSearch(RestauranteSuggestion restauranteSuggestion){
        final DataBase db= DataBase.getInstance();
        db.getmDatabaseReference().child("Restaurantes_Todos").child(restauranteSuggestion.getCodigoFireBase()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Restaurante restaurante= dataSnapshot.getValue(Restaurante.class);
                VariablesGlobales.getInstance().setRestauranteActual(restaurante);
                pasarActividadRestaurante();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }





}
