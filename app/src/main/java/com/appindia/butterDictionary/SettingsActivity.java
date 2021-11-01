package com.appindia.butterDictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CompoundButton;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    SwitchMaterial switchMaterial;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    MaterialButton textStyleButton1;
    MaterialButton textStyleButton2;
    MaterialButton textStyleButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = settings.edit();

        switchMaterial = findViewById(R.id.dark_mode_switcher);
        textStyleButton1 = findViewById(R.id.text_style_1);
        textStyleButton2 = findViewById(R.id.text_style_2);
        textStyleButton3 = findViewById(R.id.text_style_3);

        if (settings.getBoolean("MODE_NIGHT", false) || AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            switchMaterial.setActivated(true);
            switchMaterial.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.DarkTheme);
        }

        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("MODE_NIGHT", true);
                    editor.apply();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("MODE_NIGHT", false);
                    editor.apply();
                }
            }
        });

        textStyleButton1.setTextColor(getResources().getColor(R.color.colorPrimary));
        textStyleButton1.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

        textStyleButton2.setTextColor(getResources().getColor(R.color.colorPrimary));
        textStyleButton2.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

        textStyleButton3.setTextColor(getResources().getColor(R.color.colorPrimary));
        textStyleButton3.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

        int selectedButton = settings.getInt("BUTTONSELECTED", 2);

        switch (selectedButton) {

            case 1 :
                textStyleButton1.setTextColor(getResources().getColor(R.color.colorAccent));
                textStyleButton1.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                break;

            case 2 :
                textStyleButton2.setTextColor(getResources().getColor(R.color.colorAccent));
                textStyleButton2.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                break;

            case 3 :
                textStyleButton3.setTextColor(getResources().getColor(R.color.colorAccent));
                textStyleButton3.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                break;

        }

        textStyleButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textStyleButton1.setTextColor(getResources().getColor(R.color.colorAccent));
                textStyleButton1.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));

                textStyleButton2.setTextColor(getResources().getColor(R.color.colorPrimary));
                textStyleButton2.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

                textStyleButton3.setTextColor(getResources().getColor(R.color.colorPrimary));
                textStyleButton3.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

                editor.putInt("BUTTONSELECTED", 1);
                editor.apply();

                editor.putInt("TEXTSIZE", 22);
                editor.apply();

            }
        });

        textStyleButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textStyleButton2.setTextColor(getResources().getColor(R.color.colorAccent));
                textStyleButton2.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));

                textStyleButton1.setTextColor(getResources().getColor(R.color.colorPrimary));
                textStyleButton1.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

                textStyleButton3.setTextColor(getResources().getColor(R.color.colorPrimary));
                textStyleButton3.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

                editor.putInt("BUTTONSELECTED", 2);
                editor.apply();

                editor.putInt("TEXTSIZE", 20);
                editor.apply();

            }
        });

        textStyleButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textStyleButton3.setTextColor(getResources().getColor(R.color.colorAccent));
                textStyleButton3.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));

                textStyleButton1.setTextColor(getResources().getColor(R.color.colorPrimary));
                textStyleButton1.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

                textStyleButton2.setTextColor(getResources().getColor(R.color.colorPrimary));
                textStyleButton2.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

                editor.putInt("BUTTONSELECTED", 3);
                editor.apply();

                editor.putInt("TEXTSIZE", 18);
                editor.apply();

            }
        });

    }

}