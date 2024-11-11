package com.example.english_4;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuSetting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void Notification(View view){

        // Создаем диалог с предупреждением
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Переход в настройки приложения")
                .setMessage("Для изменения уведомлений вы перейдете в настройки приложения на телефоне.")
                .setPositiveButton("Перейти", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Открываем настройки приложения
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", view.getContext().getPackageName(), null);
                        intent.setData(uri);
                        view.getContext().startActivity(intent);
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Закрываем диалог
                        dialog.dismiss();
                    }
                });

        // Показываем диалог
        AlertDialog dialog = builder.create();
        dialog.show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        if (positiveButton != null) {
            positiveButton.setTextColor(Color.WHITE); // Меняем цвет текста кнопки "Перейти" на белый
        }
        if (negativeButton != null) {
            negativeButton.setTextColor(Color.WHITE); // Меняем цвет текста кнопки "Отмена" на белый
        }

    }

    public void backbottom(View view) {
        finish();}
}
