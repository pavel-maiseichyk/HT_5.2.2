package com.example.ht_522;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private EditText loginET;
    private EditText passwordET;
    private Button loginButton;
    private Button registrationButton;
    private CheckBox dataLocationCheckbox;
    private static final String FILE_NAME = "EnterData.txt";
    private static final String SHARED_PREF = "SharedPrefs";
    private static final String SHARED_PREF_CHECKBOX = "Checkbox";
    private static final int PERMISSION = 8;
    private boolean check;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStatus == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION);

        initViews();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check) {
                    try {
                        checkDataFromExternal();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        checkDataFromInternal();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check) {
                    try {
                        saveDataFromExternal();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        saveDataFromInternal();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        dataLocationCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SHARED_PREF_CHECKBOX, check);
                editor.apply();
            }
        });
    }

    private void saveDataFromExternal() throws IOException {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), FILE_NAME);
        if (!file.exists()) Toast.makeText(this, "Создай файл", Toast.LENGTH_SHORT).show();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        String loginInput = loginET.getText().toString();
        String passwordInput = passwordET.getText().toString();
        bufferedWriter.append(loginInput).append(";").append(passwordInput);
        bufferedWriter.close();
        if (loginInput.equals("") || passwordInput.equals(""))
            Toast.makeText(this, "Неполные данные :(", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Данные сохранены)", Toast.LENGTH_SHORT).show();
    }

    private void checkDataFromExternal() throws IOException {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), FILE_NAME);
        if (!file.exists()) Toast.makeText(this, "Создай файл", Toast.LENGTH_SHORT).show();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String loginInput = loginET.getText().toString();
        String passwordInput = passwordET.getText().toString();
        String line = bufferedReader.readLine();
        if (line.equals(""))
            Toast.makeText(this, "Сначала зарегистируйтесь)", Toast.LENGTH_SHORT).show();
        else {
            if (loginInput.equals("") || passwordInput.equals(""))
                Toast.makeText(this, "Неполные данные :(", Toast.LENGTH_SHORT).show();
            else {
                String[] data = line.split(";");
                String savedLogin = data[0];
                String savedPassword = data[1];
                if (loginInput.equals(savedLogin) && passwordInput.equals(savedPassword)) {
                    Toast.makeText(this, "Вход успешен)", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, NotReallyAnEasterEgg.class));
                } else Toast.makeText(this, "Данные не совпадают :(", Toast.LENGTH_SHORT).show();
            }
        }
        bufferedReader.close();
    }

    private void saveDataFromInternal() throws IOException {
        String loginInput = loginET.getText().toString();
        String passwordInput = passwordET.getText().toString();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(openFileOutput(FILE_NAME, MODE_PRIVATE)));
        bufferedWriter.append(loginInput).append(";").append(passwordInput);
        bufferedWriter.close();
        if (loginInput.equals("") || passwordInput.equals(""))
            Toast.makeText(this, "Неполные данные :(", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Данные сохранены)", Toast.LENGTH_SHORT).show();
    }

    private void checkDataFromInternal() throws IOException {
        String loginInput = loginET.getText().toString();
        String passwordInput = passwordET.getText().toString();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openFileInput(FILE_NAME)));
        String line = bufferedReader.readLine();
        if (line.equals(""))
            Toast.makeText(this, "Сначала зарегистируйтесь)", Toast.LENGTH_SHORT).show();
        else {
            if (loginInput.equals("") || passwordInput.equals(""))
                Toast.makeText(this, "Неполные данные :(", Toast.LENGTH_SHORT).show();
            else {
                String[] data = line.split(";");
                String savedLogin = data[0];
                String savedPassword = data[1];
                if (loginInput.equals(savedLogin) && passwordInput.equals(savedPassword)) {
                    Toast.makeText(this, "Вход успешен)", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, NotReallyAnEasterEgg.class));
                } else Toast.makeText(this, "Данные не совпадают :(", Toast.LENGTH_SHORT).show();
            }
        }
        bufferedReader.close();
    }

    public void initViews() {
        loginET = findViewById(R.id.loginET);
        passwordET = findViewById(R.id.passwordET);
        loginButton = findViewById(R.id.loginButton);
        registrationButton = findViewById(R.id.registrationButton);
        dataLocationCheckbox = findViewById(R.id.dataLocationCheckbox);
        check = getSharedPreferences(SHARED_PREF, MODE_PRIVATE).getBoolean(SHARED_PREF_CHECKBOX, false);
        dataLocationCheckbox.setChecked(check);
        sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
    }
}