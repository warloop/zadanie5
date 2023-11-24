package com.example.todolist2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Przesłonięta metoda onCreate, Bundle służy do przekazywania danych między aktywnościami
        super.onCreate(savedInstanceState); // Wywołanie metody bazowej onCreate -  prawidłowe przekazanie stanu do klasy bazowej
        setContentView(R.layout.activity_main); // Ustawienie widoku na activity_main

        FragmentManager fragmentManager = getSupportFragmentManager(); // Utworzenie menedżera fragmentów
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container); // Sprawdzenie, czy fragment jest już w kontenerze
        if (fragment == null) { // Jeśli fragment nie istnieje
            fragment = createFragment(); // Utworzenie nowego fragmentu, korzystając z metody createFragment()
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment) // Dodanie fragmentu do kontenera o identyfikatorze fragment_container
                    .commit(); // Potwierdzenie transakcji fragmentu
        }
    }
    protected abstract Fragment createFragment(); // Deklaracja abstrakcyjnej metody createFragment(), która musi być zaimplementowana w klasach dziedziczących
}