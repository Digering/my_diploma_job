package com.example.english_4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;

import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class Learn_Word extends AppCompatActivity {

    private LinearLayout linearLayout;
    private int currentFrameIndex = 0;
    private RecyclerView recyclerView;
    private CardView endLearn;
    private Listen_card_word_Adapter Listen_card_word_Adapter;
    private ArrayList<String[]> words;
    private List<Listen_card_word> Listen_card_words = new ArrayList<>();
    private List<Listen_card_word> Listen_card_words_learn = new ArrayList<>();
    private int number = 2;
    FrameLayout[] pocazatel = new FrameLayout[20];

    int numberLearn;

    private static final String PREFS_NAME = "MyAppPrefs";

    private static final String KEY_NUMBER = "number";
    private static final String KEY_DATE = "lastDate";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_learn_word);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        pocazatel[0] = findViewById(R.id.progress_1);
        pocazatel[1] = findViewById(R.id.progress_2);
        pocazatel[2] = findViewById(R.id.progress_3);
        pocazatel[3] = findViewById(R.id.progress_4);
        pocazatel[4] = findViewById(R.id.progress_5);
        pocazatel[5] = findViewById(R.id.progress_6);
        pocazatel[6] = findViewById(R.id.progress_7);
        pocazatel[7] = findViewById(R.id.progress_8);
        pocazatel[8] = findViewById(R.id.progress_9);
        pocazatel[9] = findViewById(R.id.progress_10);
        pocazatel[10] = findViewById(R.id.progress_11);
        pocazatel[11] = findViewById(R.id.progress_12);
        pocazatel[12] = findViewById(R.id.progress_13);
        pocazatel[13] = findViewById(R.id.progress_14);
        pocazatel[14] = findViewById(R.id.progress_15);
        pocazatel[15] = findViewById(R.id.progress_16);
        pocazatel[16] = findViewById(R.id.progress_17);
        pocazatel[17] = findViewById(R.id.progress_18);
        pocazatel[18] = findViewById(R.id.progress_19);
        pocazatel[19] = findViewById(R.id.progress_20);

        for(FrameLayout x: pocazatel){
            int color = ContextCompat.getColor(this, R.color.gray);
            x.setBackgroundTintList(ColorStateList.valueOf(color));
        }

        linearLayout = findViewById(R.id.schet);

        Create(1, null);

        recyclerView = findViewById(R.id.Cart_word);
        endLearn = findViewById(R.id.endLearn);
        endLearn.setVisibility(View.GONE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Listen_card_word_Adapter = new Listen_card_word_Adapter(this, Listen_card_words, 1);
        recyclerView.setAdapter(Listen_card_word_Adapter);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        numberLearn = prefs.getInt(KEY_NUMBER, MODE_PRIVATE); // по умолчанию 0
        long savedDateMillis = prefs.getLong(KEY_DATE, MODE_PRIVATE);

        Calendar currentCalendar = Calendar.getInstance();
        long currentDateMillis = currentCalendar.getTimeInMillis();

        Calendar savedCalendar = Calendar.getInstance();
        savedCalendar.setTimeInMillis(savedDateMillis);

        if (!isSameDay(savedCalendar, currentCalendar)) {
            // Обновляем число
            numberLearn = 0;
            // Сохраняем обновленное число и новую дату
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(KEY_NUMBER, numberLearn);
            editor.putLong(KEY_DATE, currentDateMillis);
            editor.apply();
        }
        int grayColor = getResources().getColor(R.color.gray);

        numberLearn = 0;

        for(int i=0; i<numberLearn; i++){
            ColorStateList backgroundTint = pocazatel[i].getBackgroundTintList();
            int currentColor = backgroundTint.getDefaultColor();
            if(currentColor == grayColor){
                int color = getResources().getColor(R.color.system_dictionary);
                pocazatel[i].setBackgroundTintList(ColorStateList.valueOf(color));
            }
        }

    }
    private boolean isSameDay(Calendar savedCalendar, Calendar currentCalendar) {
        return savedCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)
                && savedCalendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR);
    }

    public  void Create(int number, Listen_card_word Listen_card_word){
        if (Listen_card_word == null){
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            words = databaseHelper.getRandomWords(number);

            for(String[] category : words){

                Log.e("Trans", category[2]);
                Listen_card_word listen_card_word = new Listen_card_word(category[0], category[1], category[2], category[3]);
                Listen_card_words.add(listen_card_word);
            }

        }else{
            Listen_card_words.add(Listen_card_word);
        }


    }

    public void name_rus(View view){
        Listen_card_word_Adapter.pocazname_rus(Listen_card_word_Adapter.holder);
    }

    public void left(View view) {

        if (Listen_card_words.size() > 0) {
            final View cardView = recyclerView.findViewWithTag(Listen_card_words.get(0).getName_en());

            DatabaseHelper databaseHelper = new DatabaseHelper(this);

            databaseHelper.word_know(Listen_card_words.get(0).getName_en());

            // Удаляем элемент из списка и уведомляем адаптер
            Listen_card_words.remove(0);
            Listen_card_word_Adapter.notifyItemRemoved(0);

            // Запускаем анимацию после удаления
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
            cardView.startAnimation(animation);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Здесь можно делать дополнительные действия, если нужно
                    if (Listen_card_words.size() == 0) {

                        if(Listen_card_words_learn.size() != 0 && number == 0){
                            Create(1, Listen_card_words_learn.get(0)); // Загружаем новое слово
                            number = 2;
                        }else{
                            Create(1, null); // Загружаем новое слово
                            if (number == 0) number = 2;
                            else number -= 1;
                        }
                        Listen_card_word_Adapter.notifyDataSetChanged(); // Уведомляем об изменениях

                    }
                }
            }, 200); // время в миллисекундах
        }
    }

    public void right(View view) {

        int grayColor = getResources().getColor(R.color.gray);

        for(FrameLayout x:pocazatel){
            ColorStateList backgroundTint = x.getBackgroundTintList();
            int currentColor = backgroundTint.getDefaultColor();
            if(currentColor == grayColor){
                int color = getResources().getColor(R.color.system_dictionary);
                x.setBackgroundTintList(ColorStateList.valueOf(color));
                break;
            }
        }
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.startLearn(Listen_card_words.get(0).getName_en());

        if (Listen_card_words.size() > 0) {
            final View cardView = recyclerView.findViewWithTag(Listen_card_words.get(0).getName_en());
            Listen_card_words_learn.add(Listen_card_words.get(0));
            // Удаляем элемент из списка и уведомляем адаптер
            Listen_card_words.remove(0);
            Listen_card_word_Adapter.notifyItemRemoved(0);

            // Запускаем анимацию после удаления
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
            cardView.startAnimation(animation);

            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

            SharedPreferences.Editor editor = prefs.edit();

            numberLearn++;

            editor.putInt(KEY_NUMBER, numberLearn);
            editor.apply();

            Log.e("SQL", "numberLearn: "+String.valueOf(numberLearn));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Listen_card_words.size() == 0) {

                        if((Listen_card_words_learn.size() != 0 && number == 0)){
                            Create(1,  Listen_card_words_learn.get(0)); // Загружаем новое слово
                            Listen_card_word_Adapter.notifyItemInserted(Listen_card_words.size() - 1); // Уведомляем адаптер о добавлении новой карточки
                            Listen_card_words_learn.remove(0);
                            number = 2;
                        }else{
                            Create(1, null); // Загружаем новое слово
                            Listen_card_word_Adapter.notifyDataSetChanged(); // Уведомляем об изменениях
                            if (number == 0) number = 2;
                            else number -= 1;
                        }
                        if((numberLearn == 20)){
                            recyclerView.setVisibility(View.GONE);

                            endLearn.setAlpha(0f);
                            endLearn.setVisibility(View.VISIBLE);

                            endLearn.animate()
                                    .alpha(1f)
                                    .setDuration(300)  // Длительность анимации в миллисекундах
                                    .setListener(null); // Убираем слушатель, если не нужен
                        }

                    }
                }
            }, 200); // время в миллисекундах
        }
    }
    public void backbottom(View view) {
        finish();}
    public void endLearnbutton(View view) {
        finish();}

    public void Audio_word(View view){
        Listen_card_word_Adapter.Audio_word(Listen_card_word_Adapter.holder);
    }
}
