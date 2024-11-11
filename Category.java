package com.example.english_4;

import static android.os.SystemClock.sleep;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Category extends AppCompatActivity {

    private RecyclerView ButtonList;
    private RecyclerView ButtonListUsers;
    private RecyclerView RecyclerView_poisk_word;
    private List<ListenButtom> listenButtoms = new ArrayList<>();
    private List<ListenButtom> listenButtomsUsers = new ArrayList<>();
    private List<Listen_card_word> listenPoiscWord = new ArrayList<>();
    private ListenButtomAdapter listenButtonAdapter;
    private ListenButtomAdapter listenButtonAdapterUsers;
    private Poisc_word_Adapter PoiscWordAdapter;
    private DatabaseHelper databaseHelper;
    private List<String[]> getCategoryNames = new ArrayList<>();
    private List<String[]> getPoisc_word = new ArrayList<>();
    private ExecutorService executorService;
    private ProgressBar progressBar;
    private FrameLayout fonprogressBar;
    private FrameLayout editcartegory;
    private EditText addcategory;
    private EditText poisk_word;
    private TextView ErrorText;
    private CardView systemcat;
    private CardView usercat;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new MenuMain())
                    .commit();
        }

        // UI настройка
        editcartegory = findViewById(R.id.editcartegory);

        editcartegory.setVisibility(View.GONE);

        systemcat = findViewById(R.id.systemcat);
        usercat = findViewById(R.id.usercat);
        ErrorText = findViewById(R.id.errortext);
        poisk_word = findViewById(R.id.poisk_word);
        addcategory = findViewById(R.id.addcategory);
        ButtonList = findViewById(R.id.ButtonList);
        ButtonListUsers = findViewById(R.id.ButtonListUsers);
        fonprogressBar = findViewById(R.id.fonprogressBar);
        progressBar = findViewById(R.id.progressBar);

        RecyclerView_poisk_word = findViewById(R.id.poisc_word);
        RecyclerView_poisk_word.setLayoutManager(new LinearLayoutManager(this));

        // Создание адаптеров
        listenButtonAdapter = new ListenButtomAdapter(this, listenButtoms, false);
        listenButtonAdapterUsers = new ListenButtomAdapter(this, listenButtomsUsers, false);

        Log.e("Name", "const55");
        PoiscWordAdapter = new Poisc_word_Adapter(this, listenPoiscWord);

        // Установка менеджера компоновки и адаптеров
        ButtonList.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        ButtonListUsers.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        ButtonList.setAdapter(listenButtonAdapter);
        ButtonListUsers.setAdapter(listenButtonAdapterUsers);
        RecyclerView_poisk_word.setAdapter(PoiscWordAdapter);

        // Инициализация пула потоков
        executorService = Executors.newSingleThreadExecutor();

        // Загрузка данных
        loadDataInBackground();

        poisk_word.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Этот метод вызывается перед изменением текста
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (TextUtils.isEmpty(poisk_word.getText())) {
                    RecyclerView_poisk_word.setVisibility(View.GONE);

                    if (systemcat != null && usercat != null) {
                        systemcat.setVisibility(View.VISIBLE);
                        usercat.setVisibility(View.VISIBLE);
                    }

                } else {
                    RecyclerView_poisk_word.setVisibility(View.VISIBLE);

                    if (systemcat != null && usercat != null) {
                        systemcat.setVisibility(View.GONE);
                        usercat.setVisibility(View.GONE);
                    }

                    String inputText = s.toString();
                    PoiscWordDatabase(inputText);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Этот метод вызывается после изменения текста
            }
        });

    }
    private void PoiscWordDatabase(String name) {
        executorService.execute(() -> {
            databaseHelper = new DatabaseHelper(this);
            getPoisc_word = databaseHelper.PoiscWord(name);

            // Очищаем список перед добавлением новых данных
            listenPoiscWord.clear();

            for (int i = 0; i < getPoisc_word.size(); i++) {
                String[] category = getPoisc_word.get(i);
                Listen_card_word Listen_card_word = new Listen_card_word(category[0], category[1], category[2], category[3]);
                listenPoiscWord.add(Listen_card_word);
                Log.e("NON", category[0]+" "+category[1]+" "+category[2]+" "+category[3]);
            }

            // Обновляем RecyclerView в основном потоке
            runOnUiThread(() -> {
                PoiscWordAdapter.notifyDataSetChanged();
            });

            Log.e("NON", "Готово");
        });
    }

    private void loadDataInBackground() {
        runOnUiThread(() -> {
            progressBar.setVisibility(View.VISIBLE);
            fonprogressBar.setVisibility(View.VISIBLE);
        });
        executorService.execute(() -> {
            databaseHelper = new DatabaseHelper(this);
            getCategoryNames = databaseHelper.getCategoryNames();

            // Загрузка элементов пользователей
            for (int i = 0; i < getCategoryNames.size(); i++) {
                String[] category = getCategoryNames.get(i);
                if (Integer.parseInt(category[0]) >= 60) {
                    ListenButtom listenButtom = new ListenButtom(category[1], category[2], category[3], Boolean.parseBoolean(category[4]));
                    listenButtomsUsers.add(listenButtom);
                }
            }

            // Обновляем UI после загрузки всех элементов пользователей
            runOnUiThread(() -> {
                listenButtonAdapterUsers.notifyDataSetChanged();
            });

            // Загрузка остальных элементов
            for (int i = 0; i < getCategoryNames.size(); i++) {
                String[] category = getCategoryNames.get(i);
                if (Integer.parseInt(category[0]) < 60) {
                    ListenButtom listenButtom = new ListenButtom(category[1], category[2], category[3], Boolean.parseBoolean(category[4]));
                    listenButtoms.add(listenButtom);

                    // Обновляем UI для группы из 8 элементов
                    if ((i + 1) % 8 == 0 || i == getCategoryNames.size() - 1) {
                        runOnUiThread(() -> {
                            listenButtonAdapter.notifyItemRangeInserted(listenButtoms.size() - 8, 8);
                            listenButtonAdapter.notifyDataSetChanged();
                        });

                        // Задержка для демонстрации загрузки
                        try {
                            Thread.sleep(200); // Задержка 300 миллисекунд перед загрузкой следующей группы
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                fonprogressBar.setVisibility(View.GONE);
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown(); // Остановка ExecutorService при уничтожении активности
    }

    public void add_category(View view){

        FrameLayout editCategoryLayout = findViewById(R.id.editcartegory);

        if (editCategoryLayout.getVisibility() == View.VISIBLE) {
            editcartegory.setVisibility(View.GONE);
            ErrorText.setVisibility(View.VISIBLE);
        }else{
            editcartegory.setVisibility(View.VISIBLE);
            ErrorText.setVisibility(View.GONE);
        }
    }
    public void Close(View view){

        editcartegory.setVisibility(View.GONE);
    }
    public void Good(View view){

        if(TextUtils.isEmpty(addcategory.getText())){
            ErrorText.setVisibility(View.VISIBLE);
        }else{
                DatabaseHelper data = new DatabaseHelper(this);
                String category = data.addCategory(String.valueOf(addcategory.getText()));
                if(category =="Error"){
                    ErrorText.setVisibility(View.VISIBLE);
                }else{
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    editcartegory.setVisibility(View.GONE);
                }
        }

    }
}