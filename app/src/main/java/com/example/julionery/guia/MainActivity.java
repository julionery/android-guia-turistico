package com.example.julionery.guia;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.julionery.guia.BD.BancoDAO;
import com.example.julionery.guia.Data.SessionHandler;
import com.example.julionery.guia.Login.LoginActivity;
import com.example.julionery.guia.Models.Locais;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_LOCATION = 1;
    Context context;
    BancoDAO bd;
    EditText mTxtNome, mTxtDescricao, mTxtValor, mTxtEndereco, mTxtGuia, mTxtLatitude, mTxtLongitude;
    Button mBtnAdd, mBtnList, mBtnMarcarLocalizacao;
    ImageView mImageView;
    LocationManager locationManager;
    Boolean camera = false;

    final int REQUEST_CODE_GALERY = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        bd = new BancoDAO(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Novo Item");

        mTxtNome = findViewById(R.id.edtTitulo);
        mTxtDescricao = findViewById(R.id.edtDescricao);
        mTxtValor = findViewById(R.id.edtValor);
        mTxtEndereco = findViewById(R.id.edtLocalizacao);
        mTxtGuia = findViewById(R.id.edtGuia);
        mBtnAdd = findViewById(R.id.btnAdd);
        mBtnList = findViewById(R.id.btnList);
        mImageView = findViewById(R.id.imageView);
        mBtnMarcarLocalizacao = findViewById(R.id.btnGetLocation);

        mTxtLatitude = findViewById(R.id.txtLatitude);
        mTxtLongitude = findViewById(R.id.txtLongitude);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final CharSequence[] items = {"Galeria", "Câmera"};

                android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);

                dialog.setTitle("Escolha uma ação:");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0){
                            ActivityCompat.requestPermissions(
                                    MainActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    REQUEST_CODE_GALERY
                            );
                        }

                        if(i==1){
                            camera = true;
                          Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                          startActivityForResult(intent, 0);
                        }
                    }
                });

                dialog.show();
            }
        });

        mBtnMarcarLocalizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    buildAlertMessageNoGps();
                }else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    getLocation();
                }
            }
        });

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if (mTxtNome.getText().toString().trim().equals("")) {
                        this.ShowMessage("Informe o Título");
                        return;
                    }
                    if (mTxtDescricao.getText().toString().trim().equals("")) {
                        this.ShowMessage("Informe a Descrição");
                        return;
                    }

                    Locais local = new Locais();
                    local.setTitulo(mTxtNome.getText().toString().trim());
                    local.setDescricao(mTxtDescricao.getText().toString().trim());
                    local.setLocalizacao(mTxtEndereco.getText().toString().trim());
                    local.setFoto(imageViewToByte(mImageView));

                    if (!mTxtLatitude.getText().toString().trim().isEmpty()) {
                        local.setLatitude(Double.parseDouble(mTxtLatitude.getText().toString()));
                    }

                    if (!mTxtLongitude.getText().toString().trim().isEmpty()) {
                        local.setLongitude(Double.parseDouble(mTxtLongitude.getText().toString()));
                    }

                    if (!mTxtValor.getText().toString().trim().isEmpty()) {
                        local.setValor(Double.parseDouble(mTxtValor.getText().toString()));
                    }
                    bd.createLocais(local);

                    Toast.makeText(MainActivity.this, "Adicionado com Sucesso!", Toast.LENGTH_SHORT).show();

                    mTxtNome.setText("");
                    mTxtDescricao.setText("");
                    mTxtValor.setText("");
                    mTxtEndereco.setText("");
                    mTxtGuia.setText("");
                    mTxtLongitude.setText("");
                    mTxtLatitude.setText("");
                    mImageView.setImageResource(R.drawable.addphoto);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            private void ShowMessage(String msg) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage(msg);
                builder1.setTitle("Oooops!");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        mBtnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RecordListActivity.class));
            }
        });
    }

    private void getLocation() {
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
           ActivityCompat.checkSelfPermission (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
           ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(location != null){
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                mTxtLatitude.setText(String.valueOf(lat));
                mTxtLongitude.setText(String.valueOf(lng));
            }else {
                Toast.makeText(this, "Falha ao buscar sua localização!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Por favor ATIVE o seu GPS!").setCancelable(false)
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        final  AlertDialog alert = builder.create();
        alert.show();
    }


    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("images/*");
                startActivityForResult(galleryIntent, REQUEST_CODE_GALERY);
            } else {
                Toast.makeText(this, "Você não possui permissao de acesso aos arquivos!", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(camera) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            mImageView.setImageBitmap(bitmap);
            camera = false;
        } else  {
            if (requestCode == REQUEST_CODE_GALERY && requestCode == RESULT_OK) {
                Uri imageUri = data.getData();
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);
            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (requestCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    mImageView.setImageURI(resultUri);
                } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SessionHandler.saveToken("", this);
            verifySession();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        verifySession();
    }

    public void verifySession() {
        String token = SessionHandler.getToken(this);
        if ("".equals(token)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_locais) {
            startActivity(new Intent(MainActivity.this, RecordListActivity.class));
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_terms) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
