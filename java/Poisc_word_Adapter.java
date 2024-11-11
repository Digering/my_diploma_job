package com.example.english_4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.speech.tts.TextToSpeech;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

public class Poisc_word_Adapter extends RecyclerView.Adapter<Poisc_word_Adapter.ViewHolder> {

    public LayoutInflater inflater;
    public List<Listen_card_word> Listen_card_words;
    public Poisc_word_Adapter.ViewHolder holder;
    private TextToSpeech tts;

    Poisc_word_Adapter(Context context, List<Listen_card_word> Listen_card_words){
        this.Listen_card_words = Listen_card_words;
        this.inflater = LayoutInflater.from(context);

        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    // Установка языка синтеза
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Язык не поддерживается");
                    }
                } else {
                    Log.e("TTS", "Инициализация синтезатора речи не удалась");
                }
            }
        });

    }

    @Override//возвращает обьект ViewHolder
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e("Name", "const17");

        View view = inflater.inflate(R.layout.poisk_vvod_word, parent, false);
        Log.e("Name", "const");

        return new ViewHolder(view);
    }

    @Override//наполняет инициализированное в ViewHolder
    public void onBindViewHolder(@NonNull Poisc_word_Adapter.ViewHolder holder, int position) {

        this.holder = holder;

        Listen_card_word Listen_card_word = Listen_card_words.get(position);
        holder.name_en.setText(Listen_card_word.getName_en());
        holder.name_rus.setText(Listen_card_word.getName_rus());
        Log.e("Name", String.valueOf(holder.name_en));
        Log.e("Name", "hell");

        if(Listen_card_word.getExample() != ""){
            Gson gson = new Gson();
            Type translationListType = new TypeToken<List<Translation>>() {}.getType();
            List<Translation> translations = gson.fromJson(Listen_card_word.getExample(), translationListType);

            if (translations != null){
                for (Translation translation : translations) {
                    LinearLayout linearLayout = new LinearLayout(holder.itemView.getContext());
                    linearLayout.setOrientation(LinearLayout.VERTICAL);

                    LinearLayout linearLayout2 = new LinearLayout(holder.itemView.getContext());
                    linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout2.setGravity(Gravity.CENTER_VERTICAL);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.gravity = Gravity.START;
                    linearLayout2.setLayoutParams(layoutParams);

                    ImageButton button = new ImageButton(holder.itemView.getContext());
                    button.setImageResource(R.drawable.scrit);
                    button.setScaleType(ImageView.ScaleType.CENTER_INSIDE);  // Подгоняет изображение по центру без обрезки
                    button.setAdjustViewBounds(true);  // Подгоняет границы кнопки под изображение
                    button.setBackgroundColor(Color.TRANSPARENT);
                    int width = 80;  // Например, 100 пикселей
                    int height = 80;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                    button.setLayoutParams(params);

                    ImageButton button2 = new ImageButton(holder.itemView.getContext());
                    button2.setImageResource(R.drawable.player);
                    button2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);  // Подгоняет изображение по центру без обрезки
                    button2.setAdjustViewBounds(true);  // Подгоняет границы кнопки под изображение
                    button2.setBackgroundColor(Color.TRANSPARENT);
                    width = 130;  // Например, 100 пикселей
                    height = 130;
                    params = new LinearLayout.LayoutParams(width, height);
                    button2.setLayoutParams(params);

                    SpannableString text_eng = word_itog(translation.getO());

                    // Установка текста в TextView
                    TextView engl_examples = new TextView(holder.itemView.getContext());
                    engl_examples.setMaxWidth(700);
                    engl_examples.setText(text_eng);
                    engl_examples.setTextSize(14);
                    engl_examples.setPadding(0, 0, 0, 16);

                    button2.setOnClickListener(new View.OnClickListener() {
                        private boolean isTranslationVisible = false; // Состояние видимости перевода

                        @Override
                        public void onClick(View v) {
                            speak(String.valueOf(text_eng));
                        }
                    });

                    linearLayout2.addView(button);
                    linearLayout2.addView(engl_examples);
                    linearLayout2.addView(button2);

                    SpannableString text_rus = word_itog(translation.getT());

                    TextView rus_examples = new TextView(holder.itemView.getContext());
                    rus_examples.setText(text_rus);
                    rus_examples.setTextSize(14);
                    rus_examples.setPadding(90, 0, 0, 20);
                    rus_examples.setVisibility(View.GONE);

                    button.setOnClickListener(new View.OnClickListener() {
                        private boolean isTranslationVisible = false; // Состояние видимости перевода

                        @Override
                        public void onClick(View v) {
                            isTranslationVisible = !isTranslationVisible; // Переключаем состояние
                            rus_examples.setVisibility(isTranslationVisible ? View.VISIBLE : View.GONE); // Показываем или скрываем

                            // Изменение кнопки (переворот)
                            if (isTranslationVisible) {
                                button.setRotation(180); // Поворачиваем кнопку
                            } else {
                                button.setRotation(0); // Возвращаем кнопку в исходное положение
                            }
                        }
                    });

                    linearLayout.addView(linearLayout2);
                    linearLayout.addView(rus_examples);

                    holder.examples_list.addView(linearLayout);
                }
                holder.examples_list.setVisibility(View.GONE);

                Log.e("Log", String.valueOf(holder.examples_list));

                holder.examples_list_prim.setOnClickListener(new View.OnClickListener() {
                    private boolean isExamplesVisible = false; // Состояние видимости примеров

                    @Override
                    public void onClick(View v) {
                        isExamplesVisible = !isExamplesVisible; // Переключаем состояние видимости

                        // Изменение видимости примеров
                        holder.examples_list.setVisibility(isExamplesVisible ? View.VISIBLE : View.GONE);

                        // Поворачиваем кнопку
                        holder.examples_list_prim.animate()
                                .rotation(isExamplesVisible ? 180 : 0)
                                .setDuration(300) // Анимация поворота
                                .start();
                    }
                });}else {
                holder.examples_list_prim.setVisibility(View.GONE);
                holder.examples.setVisibility(View.GONE);
            }
            holder.imageButton_word.setOnClickListener(new View.OnClickListener() {
                private boolean isTranslationVisible = false; // Состояние видимости перевода

                @Override
                public void onClick(View v) {
                    speak(String.valueOf(Listen_card_word.getName_en()));
                }
            });
        }

        holder.bind(Listen_card_words.get(position));
        if(Listen_card_word.getTranscription() != null){
            holder.transcription.setText(Listen_card_word.getTranscription());
        }
        else
            holder.transcription.setVisibility(View.GONE);
    }

    public SpannableString word_itog(String text){
        int start = text.indexOf("#");
        int end = text.indexOf("#", start + 1);

        if (start != -1 && end != -1) {
            // Удаление символов #
            String word = text.substring(start + 1, end);
            String finalText = text.replace("#" + word + "#", word);

            // Создание SpannableString для форматирования
            SpannableString spannableString = new SpannableString(finalText);

            // Применение стиля
            spannableString.setSpan(new StyleSpan(Typeface.ITALIC), start, start + word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new UnderlineSpan(), start, start + word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            return spannableString;
        } else {
            return SpannableString.valueOf(text);

        }
    }

    // Метод для синтеза речи
    private void speak(String text) {
        if (tts != null) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override//возвращаем колличество item
    public int getItemCount() {
        return Listen_card_words.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name_en, transcription, name_rus, examples;
        LinearLayout examples_list;
        ImageButton examples_list_prim, imageButton_word;

        public  ViewHolder(View itemView){
            super(itemView);

            name_en = itemView.findViewById(R.id.poisc_name_en);
            name_rus = itemView.findViewById(R.id.poisc_name_rus);
            transcription = itemView.findViewById(R.id.poisc_transcription);
            examples = itemView.findViewById(R.id.poisc_examples);

            examples_list = itemView.findViewById(R.id.poisc_examples_list);

            examples_list_prim = itemView.findViewById(R.id.examples_list_prim);
            imageButton_word = itemView.findViewById(R.id.imageButton_word);
        }
        // Метод для привязки данных к элементам ViewHolder
        public void bind(Listen_card_word listenCardWord) {
            name_en.setText(listenCardWord.getName_en());
            if (listenCardWord.getTranscription() != null) {
                transcription.setText(listenCardWord.getTranscription());
                transcription.setVisibility(View.VISIBLE);
            } else {
                transcription.setVisibility(View.GONE);
            }
        }
    }
}