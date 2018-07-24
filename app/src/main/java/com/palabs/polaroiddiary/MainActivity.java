package com.palabs.polaroiddiary;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LocationListener {

    TextView date,locations;
    ImageButton cam, gps, save, share;

    LocationManager locationManager;
    ProgressDialog progressDialog;
    String strLoc = "";

    RelativeLayout polaroid;

    ImageView pic;
    Bitmap photo,scaledPhoto;


    private static int RESULT_LOAD_IMAGE = 1;

    private static final int CAMERA_REQUEST = 1888;

    String formattedDate;

    Typeface face;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        Intent r1 = getIntent();


        date = (TextView) findViewById(R.id.textViewDate);
        locations = (TextView) findViewById(R.id.textViewLocation);

        cam = (ImageButton) findViewById(R.id.imageButtonCamera);
        gps = (ImageButton) findViewById(R.id.imageButtonLocation);
        save = (ImageButton) findViewById(R.id.imageButtonDownload);
        share = (ImageButton) findViewById(R.id.imageButtonShare);

        pic = (ImageView) findViewById(R.id.imageViewPic);

        polaroid = (RelativeLayout) findViewById(R.id.relativeLayout);


        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        formattedDate = df.format(c.getTime());

        date.setText(formattedDate);


        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");



        cam.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Toast.makeText(getApplicationContext(),"Camera/Gallery",Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        gps.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Toast.makeText(getApplicationContext(),"Add location/text",Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        save.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Toast.makeText(getApplicationContext(),"Save Polaroid",Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        share.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Toast.makeText(getApplicationContext(),"Share Polaroid",Toast.LENGTH_SHORT).show();

                return false;
            }
        });



        face= Typeface.createFromAsset(getAssets(),"fonts/lietome.ttf");
        date.setTypeface(face);
        locations.setTypeface(face);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.help) {

            Intent i2=new Intent(MainActivity.this,ai.class);
            startActivity(i2);

        }

        return super.onOptionsItemSelected(item);
    }

    public String getOriginalImagePath() {
        String[] projection = {MediaStore.Images.Media.DATA };
        Cursor cursor = this.managedQuery( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        int column_index_data = cursor .getColumnIndexOrThrow(MediaStore.Images.Media.DATA); cursor.moveToLast();
        return cursor.getString(column_index_data);
    }


    public void cameraClick(View view) {

        String[] choices = {"Open Camera","Open Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                switch (i){
                    case 0:
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);

                        break;

                    case 1:
                        Intent galleryIntent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);

                        break;

                }

            }
        });
        builder.create().show();

        builder.setCancelable(true);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {


            photo = (Bitmap) data.getExtras().get("data");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            scaledPhoto = BitmapFactory.decodeFile(getOriginalImagePath());

            pic.setImageBitmap(scaledPhoto);


        }



        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            pic.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }

    }

    public void onGPSclicked(View view) {

        String[] choices = {"Auto-generate Location","Add personalized location/text"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                switch (i){
                    case 0:
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(MainActivity.this,"Please Grant Permissions",Toast.LENGTH_LONG).show();
                        }else {

                            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 5, MainActivity.this);
                                progressDialog.show();
                            }else{
                                Toast.makeText(MainActivity.this,"Please Enable GPS",Toast.LENGTH_LONG).show();

                                // Buil-In Intent
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                            }


                        }

                        break;

                    case 1:
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                        alertDialog.setTitle("Enter Location/Text");
                        alertDialog.setMessage("Tip : Do not exceed 2 lines");

                        final EditText input = new EditText(MainActivity.this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);
                        alertDialog.setView(input);

                        alertDialog.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        String personalizedText=input.getText().toString();
                                        locations.setText(personalizedText);

                                    }
                                });

                        alertDialog.setNegativeButton("CANCEL",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                        alertDialog.show();

                        break;

                }

            }
        });
        builder.create().show();

        builder.setCancelable(true);

    }


    @Override
    public void onLocationChanged(Location location) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        strLoc = "Location: "+latitude+" - "+longitude;

        try {
            // Reverse Geocoding : Fetch Address from Latitude and Longitude
            Geocoder geocoder = new Geocoder(this);
            List<Address> adrsList = geocoder.getFromLocation(latitude, longitude, 5);

            if(adrsList!=null && adrsList.size()>0){
                Address address = adrsList.get(0);
                StringBuffer buffer = new StringBuffer();

                for(int i=0;i<address.getMaxAddressLineIndex();i++){
                    buffer.append(address.getAddressLine(i)+"\n");
                }

                locations.setText(buffer.toString());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        progressDialog.dismiss();

        //location.getSpeed(); mps

        locationManager.removeUpdates(this);
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

    File file;

    public void downloadClick(View view) {

        View content = findViewById(R.id.relativeLayout);
        content.setDrawingCacheEnabled(true);
        Bitmap bitmap = content.getDrawingCache();
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();

        String pname = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss'.tsv'").format(new Date());

        File folder = new File(root+ "/PolaroidDiary");
        file = new File(root+ "/PolaroidDiary/polaroid " + pname + ".png");
        try {

            if (!folder.exists()) {
                folder.mkdir();
            }

            if (!file.exists()) {

                file.createNewFile();
            }

            FileOutputStream ostream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, ostream);
            ostream.close();
            content.invalidate();
            Toast.makeText(this, "Image Saved", Toast.LENGTH_LONG).show();
            getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        }

        catch (FileNotFoundException f) {
            Toast.makeText(this, "FileNotFoundException", Toast.LENGTH_LONG).show();
            Log.e("fileNotFound","reason");
            f.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(this, "IOException", Toast.LENGTH_LONG).show();
            Log.d("IO",e.getMessage());
            System.out.print(e);

        }catch (Exception e) {
            Toast.makeText(this, "Exception", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {

            content.setDrawingCacheEnabled(false);

        }

    }


    public void shareClicked(View view) {

        downloadClick(view);

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        startActivity(Intent.createChooser(share,"Share via"));

    }





}
