package com.example.julionery.guia.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.julionery.guia.BD.BancoDAO;
import com.example.julionery.guia.Data.SessionHandler;
import com.example.julionery.guia.Data.WebTaskLogin;
import com.example.julionery.guia.Home.HomeActivity;
import com.example.julionery.guia.Models.User;
import com.example.julionery.guia.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class LoginActivity extends AppCompatActivity {

    private MaterialDialog dialog;
    BancoDAO bancoDAO;

    public LoginActivity(){
        bancoDAO = new BancoDAO(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button button = findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tentaLogin();
            }
        });

        EditText editTextEmail = findViewById(R.id.txtEmail);
        String SavedEmail = SessionHandler.getEmail(this);
        if(!"".equals(SavedEmail)){
            editTextEmail.setText(SavedEmail);
        }
    }

    public void tentaLogin(){

        hideSoftKeyBoard();

        EditText editTextEmail = findViewById(R.id.txtEmail);
        String inputEmail = editTextEmail.getText().toString();

        EditText editTextSenha = findViewById(R.id.txtSenha);
        String inputSenha = editTextSenha.getText().toString();

        if("".equals(inputEmail)){
            showError(getString(R.string.error_no_email));
            return;
        }

        if("".equals(inputSenha)){
            showError(getString(R.string.error_na_senha));
            return;
        }

        if(!isValidEmail(inputEmail)){
            showError(getString(R.string.error_invalid_email));
            return;
        }

        dialog = new MaterialDialog.Builder(this)
                .title(R.string.progressDialog)
                .progress(true, 0)
                .cancelable(false)
                .show();

        WebTaskLogin webTaskLogin = new WebTaskLogin(this, inputEmail, inputSenha);
        webTaskLogin.execute();
    }

    public void goToHome(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }


    public void hideSoftKeyBoard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showError(String error){
        Snackbar.make(
                findViewById(R.id.screen),
                error,
                Snackbar.LENGTH_LONG)
                .show();
    }

    public static boolean isValidEmail(String target)
    {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    protected void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void onEvent(Error error){
        dialog.dismiss();
        showError(error.getMessage());
    }

    @Subscribe
    public void onEvent(User user){
        dialog.dismiss();
        SessionHandler.saveEmail(user.getEmail(),this);
        SessionHandler.saveToken(user.getToken(),this);

        User usuario = bancoDAO.retrieveUserByEmail(user.getEmail());

        if(usuario == null) {
            bancoDAO.createUser(user);
        } else {
            usuario.setName(user.getName());
            usuario.setToken(user.getToken());
            usuario.setSubtitulo(user.getSubtitulo());
            usuario.setPhotoURL(user.getPhotoURL());
            bancoDAO.updateUser(usuario);
        }

        finish();
    }
}
