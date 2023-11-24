package com.example.todolist2;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class TaskStorage {
    private static TaskStorage instance = new TaskStorage(); // Prywatne pole przechowujące instancję TaskStorage
    private List<Task> tasks; // Prywatne pole przechowujące listę zadań

    private TaskStorage() { // Prywatny konstruktor, inicjalizujący listę zadań z przykładowymi danymi
        final int tasksCount = 100; // Liczba zadań do utworzenia
        tasks = new ArrayList<Task>(tasksCount); // Inicjalizacja listy zadań jako ArrayList
        for (int i = 1; i <= tasksCount; ++i) {
            Task task = new Task(); // Utworzenie nowego zadania
            task.setName("Zadanie #" + i); // Ustawienie nazwy zadania
            task.setDone(i % 3 == 0); // Ustawienie stanu zakończenia zadania (co trzecie zadanie zakończone)

            if (i % 3 == 0) {
                task.setCategory(Category.STUDIES);
            } else {
                task.setCategory(Category.HOME);
            }
            tasks.add(task); // Dodanie zadania do listy
        }
    }

    public static TaskStorage getInstance() { // Metoda statyczna zwracająca instancję TaskStorage
        return instance; // Zwrócenie jednej instancji klasy TaskStorage (wzorzec Singleton)
    }

    public void addTask(Task task) { // Metoda dodająca nowe zadanie do listy
        tasks.add(task);
    } // Dodanie nowego zadania do listy

    public List<Task> getTasks() {
        return tasks;
    } // Metoda zwracająca listę zadań

    public Task getTask(UUID id) { // Metoda pobierająca zadanie na podstawie identyfikatora UUID
        for (Task task : tasks) { // Przeszukiwanie listy zadań
            if (task.getId().equals(id)) // Jeśli identyfikator zadania zgadza się z podanym identyfikatorem
                return task; // Zwrócenie znalezionego zadania
        }
        return null; // Jeśli zadanie o podanym identyfikatorze nie zostało znalezione, zwróć null
    }
}