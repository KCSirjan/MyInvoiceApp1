package com.example.myinvoice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class IndividualInvoice extends Fragment {
    private static final String TAG = "Individual Invoice";

    private Button btnPickImage;
    private Button btnUpdate;
    private EditText txtTitle;
    private EditText txtDate;
    private EditText txtShopName,txtComment;
    private ImageView imageView;
    private Spinner spinner;
    Bitmap bitmap;
    SQLiteHandler db;
    Button btnDelete;
    Button btnShowMap;
    ArrayList<Invoice> invoices;
    Invoice invoice;
    private static int RESULT_LOAD_IMAGE=1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_invoice, container, false);
        db=new SQLiteHandler(getContext());
        invoices=db.getRecords();
        invoice=invoices.get(MainActivity.index);
        btnUpdate=(Button) view.findViewById(R.id.btnUpdate);
        btnDelete=(Button) view.findViewById(R.id.btnDelete);
        btnShowMap=(Button) view.findViewById(R.id.btnShowMap);
        btnPickImage=(Button) view.findViewById(R.id.btnPickImage);
        btnPickImage=(Button) view.findViewById(R.id.btnPickImage);
        txtTitle= (EditText) view.findViewById(R.id.txtTitle);
        txtTitle.setText(invoice.getTitle());
        txtDate= (EditText) view.findViewById(R.id.txtDate);
        txtDate.setText(invoice.getDate());
        txtShopName= (EditText) view.findViewById(R.id.txtShopName);
        txtShopName.setText(invoice.getShopName());
        txtComment= (EditText) view.findViewById(R.id.txtComment);
        txtComment.setText(invoice.getComment());
        imageView= (ImageView) view.findViewById(R.id.imageView);
        spinner=(Spinner)view.findViewById(R.id.spinner);
        String[] categories=new String[]{"Clothes","Home","Stationery","Electronics"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(invoice.getInvoiceType()));
        bitmap=invoice.getImage();
        imageView.setImageBitmap(invoice.getImage());
        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=txtTitle.getText().toString();
                String date=txtDate.getText().toString();
                String invoiceType=spinner.getSelectedItem().toString();
                String shopName=txtShopName.getText().toString();
                String comment=txtComment.getText().toString();
                if(TextUtils.isEmpty(title) || TextUtils.isEmpty(date) || TextUtils.isEmpty(invoiceType) || TextUtils.isEmpty(shopName)  || TextUtils.isEmpty(comment)){
                    Toast.makeText(getContext(),"All fields are required", Toast.LENGTH_LONG).show();
                }else{
                    Invoice invoice=new Invoice(title,date,invoiceType,shopName,null,comment,bitmap);
                    db.updateInvoice(invoice);
                    Toast.makeText(getContext(),"Invoice updated successfully", Toast.LENGTH_LONG).show();
                    ((MainActivity)getActivity()).setViewPager(0);


                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Are you sure? This will deelete the invoice")
                        .setMessage("Delete invoice")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                db.deleleInvoice(invoice.getInvoiceID());
                                Toast.makeText(getContext(),"Invoice deleted successfully", Toast.LENGTH_LONG).show();
                                ((MainActivity)getActivity()).setViewPager(0);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
        btnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] arr=invoice.getLocation().split(":");
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+arr[1]+","+arr[0]);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
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
}
