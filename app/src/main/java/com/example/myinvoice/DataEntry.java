package com.example.myinvoice;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;


public class DataEntry extends Fragment implements LocationListener {
    private static final String TAG = "Data entry";

    private Button btnPickImage;
    private Button btnAddInvoice;
    private EditText txtTitle;
    private EditText txtDate;
    private EditText txtShopName;
    private EditText txtLocation,txtComment;
    private ImageView imageView;
    private Spinner spinner;
    Bitmap bitmap;
    SQLiteHandler db;
    private static int RESULT_LOAD_IMAGE=1;
    public  static final int RequestPermissionCode  = 1 ;
    Location location;
    LocationManager locationManager ;
    boolean GpsStatus = false ;
    Criteria criteria ;
    String Holder;
    double longitude;
    double latitude;
    public DataEntry() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.data_entry, container, false);
        EnableRuntimePermission();

        db=new SQLiteHandler(getContext());
        btnAddInvoice=(Button) view.findViewById(R.id.btnAddInvoice);
        btnPickImage=(Button) view.findViewById(R.id.btnPickImage);
        txtTitle= (EditText) view.findViewById(R.id.txtTitle);
        txtDate= (EditText) view.findViewById(R.id.txtDate);
        txtDate= (EditText) view.findViewById(R.id.txtDate);
        txtShopName= (EditText) view.findViewById(R.id.txtShopName);
        txtLocation= (EditText) view.findViewById(R.id.txtLocation);
        txtComment= (EditText) view.findViewById(R.id.txtComment);
        imageView= (ImageView) view.findViewById(R.id.imageView);
        spinner=(Spinner)view.findViewById(R.id.spinner);
        String[] categories=new String[]{"Clothes","Home","Stationery","Electronics"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //GPS
        locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();

        Holder = locationManager.getBestProvider(criteria, false);
        CheckGpsStatus();

        if(GpsStatus == true) {
            if (Holder != null) {
                if (ActivityCompat.checkSelfPermission(
                        getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        &&
                        ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {

                }
                location = locationManager.getLastKnownLocation(Holder);
                locationManager.requestLocationUpdates(Holder, 12000, 7,DataEntry.this );
            }
        }else {

            Toast.makeText(getContext(), "Please Enable GPS First", Toast.LENGTH_LONG).show();

        }


        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        btnAddInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=txtTitle.getText().toString();
                String date=txtDate.getText().toString();
                String invoiceType=spinner.getSelectedItem().toString();
                String shopName=txtShopName.getText().toString();
                String location=txtLocation.getText().toString();
                String comment=txtComment.getText().toString();
                if(TextUtils.isEmpty(title) || TextUtils.isEmpty(date) || TextUtils.isEmpty(invoiceType) || TextUtils.isEmpty(shopName) || TextUtils.isEmpty(location) || TextUtils.isEmpty(comment)){
                    Toast.makeText(getContext(),"All fields are required", Toast.LENGTH_LONG).show();
                }else if(bitmap==null){
                    Toast.makeText(getContext(),"Please select an image of the invoice", Toast.LENGTH_LONG).show();
                }else{
                    Invoice invoice=new Invoice(title,date,invoiceType,shopName,location,comment,bitmap);
                    db.addInvoice(invoice);
                    Toast.makeText(getContext(),"Invoice added successfully", Toast.LENGTH_LONG).show();
                    ((MainActivity)getActivity()).setViewPager(0);


                }
            }
        });
        txtLocation.setText(longitude+":"+latitude);
        txtLocation.setEnabled(false);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImageURI = data.getData();
            bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), selectedImageURI);
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageView.setImageBitmap(bitmap);

        }


    }


    @Override
    public void onLocationChanged(Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        txtLocation.setText(location.getLongitude()+":"+location.getLatitude());
        Toast.makeText(getContext(), location.getLongitude()+":"+location.getLatitude(), Toast.LENGTH_SHORT).show();

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

    public void CheckGpsStatus(){

        locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION))
        {

            Toast.makeText(getContext(),"ACCESS_FINE_LOCATION permission allows us to Access GPS in app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this.getActivity(),new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getContext(),"Permission Granted, Now your application can access GPS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getContext(),"Permission Canceled, Now your application cannot access GPS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

}
