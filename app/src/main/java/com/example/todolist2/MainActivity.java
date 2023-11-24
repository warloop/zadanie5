package com.example.todolist2;

import static android.content.Intent.getIntent;

import java.util.UUID;
import androidx.fragment.app.Fragment;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() { // Przesłonięta metoda do tworzenia fragmentu
        UUID taskId = (UUID)getIntent().getSerializableExtra(TaskListFragment.KEY_EXTRA_TASK_ID); // Pobranie identyfikatora zadania z intencji, getSerializableExtra - przeysłanie danych między komponentami
        return TaskFragment.newInstance(taskId);  // Utworzenie i zwrócenie fragmentu TaskFragment z identyfikatorem zadania
    }
}