package com.example.english_4;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class MenuMain extends Fragment implements View.OnClickListener{

    private MenuMainViewModel mViewModel;

    ImageButton imageButtonLearn;
    ImageButton imageButtonList;
    ImageButton imageButtonStats;
    ImageButton imageButtonSetting;

    public static MenuMain newInstance() {
        return new MenuMain();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_main, container, false);

        imageButtonLearn = view.findViewById(R.id.imageButtonLearn);
        imageButtonList = view.findViewById(R.id.Category);
        imageButtonStats = view.findViewById(R.id.imageButtonStats);
        imageButtonSetting = view.findViewById(R.id.imageButtonSetting);

        imageButtonLearn.setOnClickListener(this);
        imageButtonList.setOnClickListener(this);
        imageButtonStats.setOnClickListener(this);
        imageButtonSetting.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.imageButtonLearn && !(getActivity() instanceof  MainActivity)){
            startActivity(new Intent(v.getContext(),MainActivity.class));

        }else if(v.getId() == R.id.Category && !(getActivity() instanceof  Category)){

            startActivity(new Intent(v.getContext(),Category.class));

        }else if(v.getId() == R.id.imageButtonStats && !(getActivity() instanceof  Statistic)){

            startActivity(new Intent(v.getContext(),Statistic.class));

        }else if(v.getId() == R.id.imageButtonSetting && !(getActivity() instanceof  Setting)){

            startActivity(new Intent(v.getContext(),Setting.class));

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        // Восстанавливаем состояние кнопки, если необходимо
        // Например, если вы хотите изменить кнопку в зависимости от состояния активности
        if(getActivity() instanceof  MainActivity){
            Log.println(Log.DEBUG, "SQL", "tru");

            updateUI(getView().findViewById(R.id.TextLearn), imageButtonLearn, R.drawable.learn_open);

        }else if(getActivity() instanceof  Category){

            updateUI(getView().findViewById(R.id.TextList), imageButtonList, R.drawable.list_open);

        }else if(getActivity() instanceof  Statistic){

            updateUI(getView().findViewById(R.id.TextStas), imageButtonStats, R.drawable.statistic_open);

        }else if(getActivity() instanceof  Setting){

            updateUI(getView().findViewById(R.id.TextSetting), imageButtonSetting, R.drawable.setting_open);

        }
    }

    private void updateUI(TextView textView, ImageButton imageButton, int imageResId) {
        // Меняем изображение и цвет текста
        Log.d("MenuMain", "Button clicked: " + textView);
        textView.setTextColor(getResources().getColor(R.color.activity_Buttom));


        imageButton.setImageResource(imageResId);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MenuMainViewModel.class);
        // TODO: Use the ViewModel
    }

}