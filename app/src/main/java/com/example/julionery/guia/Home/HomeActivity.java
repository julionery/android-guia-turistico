package com.example.julionery.guia.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.julionery.guia.Data.SessionHandler;
import com.example.julionery.guia.Login.LoginActivity;
import com.example.julionery.guia.R;

public class HomeActivity extends AppCompatActivity {

    private static String TAG = "Guia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "ON CREATE");
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "ON START");
        verifySession();
    }

    public void verifySession(){
        String token = SessionHandler.getToken(this);
        if("".equals(token)){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, "ON RESUME");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(TAG, "ON PAUSE");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d(TAG, "ON STOP");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "ON DESTROY");
    }
}
