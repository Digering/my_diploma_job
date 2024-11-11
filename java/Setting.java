package com.example.english_4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Setting extends AppCompatActivity {

    private LinearLayout dataLayout;
    private LinearLayout authorizedLayout;
    private Button loginButton;
    private Button Exit_Login;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dataLayout = findViewById(R.id.data);
        authorizedLayout = findViewById(R.id.linearLayoutAuthorized);
        loginButton = findViewById(R.id.btnLogin);
        Exit_Login = findViewById(R.id.Exit_Login);


        // Получаем SharedPreferences для проверки авторизации
        sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        boolean isAuthorized = sharedPreferences.getBoolean("isAuthorized", false);

        updateUI(isAuthorized);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Здесь можно вызвать активность для авторизации
                performLogin();
            }
        });

        Exit_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Здесь можно вызвать активность для авторизации
                exit_performLogin();
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new MenuMain())
                    .commit();
        }
    }

    private void updateUI(boolean isAuthorized) {
        if (isAuthorized) {
            dataLayout.setVisibility(View.VISIBLE);
            authorizedLayout.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
        } else {
            dataLayout.setVisibility(View.GONE);
            authorizedLayout.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }

    private void exit_performLogin() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("isAuthorized");
        editor.apply();

        // Обновляем интерфейс
        updateUI(false);
    }

    private void performLogin() {
        // Логика авторизации пользователя
        // После успешной авторизации:
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isAuthorized", true);
        editor.apply();

        // Обновляем интерфейс
        updateUI(true);
    }

    public void Setting(View view){

        startActivity(new Intent(this, MenuSetting.class));

    }

}
