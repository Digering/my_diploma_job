package com.example.english_4;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_PATH; // полный путь к базе данных
    private static String DB_NAME = "database.db";
    private static final int SCHEMA = 1; // версия базы данных
    private Context myContext;
    private Connection connection;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
        this.myContext=context;
        DB_PATH =context.getFilesDir().getPath() + DB_NAME;
        create_db();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS Word");
//        onCreate(db);
    }

    public void word_know(String name_en) {
        SQLiteDatabase db = open();

        try {
            db.beginTransaction();

            db.execSQL("INSERT INTO Log (datatime, id_word, repetition, status) \n" +
                    "SELECT datetime('now'), w.id, 0, 3 " +  // добавлен пробел перед FROM
                    "FROM Word w " +
                    "WHERE w.name = ?", new String[]{name_en});

            db.setTransactionSuccessful(); // Помечаем транзакцию как успешную
        } catch (Exception e) {
            // Обработка возможных ошибок
            e.printStackTrace();
        } finally {
            db.endTransaction(); // Завершаем транзакцию
            db.close(); // Закрываем базу данных
        }
    }

    public List<String[]> PoiscWord(String name) {
        List<String[]> getPoisc_word = new ArrayList<>();
        SQLiteDatabase db = open();
        Cursor cursor = null;

        if (name.matches(".*[A-Za-z].*")){

            String query = "SELECT * FROM Word WHERE name LIKE ?";
            cursor = db.rawQuery(query, new String[]{name + "%"});

        }else if(name.matches(".*[А-Яа-яЁё].*")){

            String query = "SELECT * FROM Word WHERE rus LIKE ?";
            cursor = db.rawQuery(query, new String[]{name + "%"});
        }
        if (cursor.moveToFirst()) {
            do {
                String[] categoryData = new String[4];
                categoryData[0] = cursor.getString(1); // name
                categoryData[1] = cursor.getString(2); // rus
                categoryData[3] = cursor.getString(3); // examples
                categoryData[2] = cursor.getString(5); // transcription

                // Добавление массива строк в список
                getPoisc_word.add(categoryData);

            } while (cursor.moveToNext());
        }
        cursor.close();

        db.close();

        return getPoisc_word;
    }

    public void onUpgradevibor(ArrayList<Object[]> listenButtomsUpdate) {
        SQLiteDatabase db = open();
        db.beginTransaction();
        for (Object[] entry : listenButtomsUpdate) {

            Boolean isSelected = (Boolean) entry[1];
            String nameRus = (String) entry[0];

            db.execSQL("UPDATE Category\n" +
                    "SET is_selected = ?\n" +
                    "WHERE name_rus = ?;", new Object[]{isSelected ? 1 : 0, nameRus});


        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void startLearn(String name_en) {
        SQLiteDatabase db = open();

        long currentTimeMillis = System.currentTimeMillis();
        long intervalMillis = 1 * 60 * 1000;  // Преобразуем дни в миллисекунды
//        setTimer(name_en, intervalMillis);
        long nextReviewTimeMillis = currentTimeMillis + intervalMillis;

        // Преобразуем время следующего повторения в строку для SQLite
        String nextReviewTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date(nextReviewTimeMillis));
        Log.e("SQL", nextReviewTime);

        // SQL-запрос для вставки
        String sql = "INSERT INTO Log (datatime, id_word, repetition, status) " +
                "SELECT ?, w.id, 1, ? " +
                "FROM Word w " +
                "WHERE w.name = ?;";

        // Подготовка запроса
        SQLiteStatement statement = db.compileStatement(sql);

        // Привязка значений параметров
        statement.bindString(1, nextReviewTime);  // Привязка repetition
        statement.bindLong(2, 1);      // Привязка status
        statement.bindString(3, name_en);    // Привязка name_en

        // Выполнение запроса
        statement.executeInsert();

        // Закрытие базы данных
        db.close();
    }

    // Метод для получения всех значений из столбца name_rus таблицы Category
    public ArrayList<String[]> getCategoryNames() {
        ArrayList<String[]> categoryNames = new ArrayList<>();

        SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery("SELECT \n" +
                "    c.id,\n" +
                "    c.name_rus, \n" +
                "    COUNT(wc.id_word) AS word_count, \n" +
                "    ROUND((SUM(CASE WHEN l.id_word IS NOT NULL THEN 1 ELSE 0 END) * 100.0) / COUNT(wc.id_word), 2) AS repetition_percentage, \n" +
                "    CASE WHEN c.is_selected = 1 THEN 'true' ELSE 'false' END AS is_selected\n" +
                "FROM \n" +
                "    Category c\n" +
                "LEFT JOIN \n" +
                "    Word_Category wc ON c.id = wc.id_category\n" +
                "LEFT JOIN \n" +
                "    Log l ON wc.id_word = l.id_word  -- Изменено с w.id на wc.id_word\n" +
                "GROUP BY \n" +
                "    c.id, c.name_rus, c.is_selected  -- Добавлено c.name_rus и c.is_selected для соответствия стандартам SQL\n" +
                "ORDER BY \n" +
                "    c.name_rus ASC;\n", null);
        if (cursor.moveToFirst()) {
            do {
                String[] categoryData = new String[5];
                categoryData[0] = cursor.getString(0); // 1 строка из курсора
                categoryData[1] = cursor.getString(1); // 2 строка из курсора
                categoryData[2] = cursor.getString(2); // 3 строка из курсора
                categoryData[3] = cursor.getString(3); // 4 строка из курсора
                categoryData[4] = cursor.getString(4); // 5 строка из курсора

                Log.e("SQL", categoryData[1]);

                // Добавление массива строк в список
                categoryNames.add(categoryData);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return categoryNames;
    }

    public String addCategory(String category){
        SQLiteDatabase db = open();

        // Подготавливаем запрос для проверки наличия категории, игнорируя регистр
        String query = "SELECT * FROM Category WHERE LOWER(name_rus) = LOWER(?)";
        Cursor cursor = db.rawQuery(query, new String[]{category});

        // Проверяем, существует ли запись
        if (cursor.getCount() > 0) {
            // Категория уже существует
            cursor.close();
            return "Error";
        } else {

            String sql = "INSERT INTO Category (name_en, name_rus, is_selected, is_custom) " +
                    "SELECT ?, ?, 0, 1 ";

            // Подготовка запроса
            SQLiteStatement statement = db.compileStatement(sql);

            // Привязка значений параметров
            statement.bindString(1, category);
            statement.bindString(2, category);

            // Выполнение запроса
            statement.executeInsert();

            // Закрытие базы данных
            db.close();
            return "Good";
        }
    }

    public ArrayList<String[]> getRandomWords(int limit) {
        ArrayList<String[]> cards = new ArrayList<>();
        SQLiteDatabase db = open();

        Cursor cursor = db.rawQuery(
                "SELECT DISTINCT Word.name, Word.rus , Word.transcription_br, Word.examples_rus " +
                        "FROM Word " +
                        "LEFT JOIN Word_Category wc ON Word.id = wc.id_word " +
                        "LEFT JOIN Category ON wc.id_category = Category.id " +
                        "LEFT JOIN Log ON Word.id = Log.id_word " +
                        "WHERE Category.is_selected = 0 " +
                        "AND Log.id_word IS NULL " +  // Условие для исключения слов, которых нет в Log
                        "ORDER BY RANDOM() LIMIT ?;",
                new String[]{String.valueOf(limit)}
        );

        if (cursor.moveToFirst()) {
            do {
                String[] words = new String[4];
                words[0] = cursor.getString(0);
                words[1] = cursor.getString(1);
                if(cursor.isNull(2)){
                    words[2] = "";
                }else{
                words[2] = cursor.getString(2);}
                if(cursor.isNull(3)){
                    words[3] = "";
                }else{
                    words[3] = cursor.getString(3);}
                cards.add(words);  // Добавляем этот список в список карт

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return cards;
    }

    void create_db(){

        File file = new File(DB_PATH);
        if (!file.exists()) {
            //получаем локальную бд как поток
            try(InputStream myInput = myContext.getAssets().open(DB_NAME);
                // Открываем пустую бд
                OutputStream myOutput = new FileOutputStream(DB_PATH)) {

                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
            }
            catch(IOException ex){
            }
        }
    }
    public SQLiteDatabase open()throws SQLException {
        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }
}