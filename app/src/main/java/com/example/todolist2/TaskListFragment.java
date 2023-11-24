package com.example.todolist2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.CheckBox;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskListFragment extends Fragment {
    private RecyclerView recyclerView; // Pole przechowujące widok listy zadań
    private TaskAdapter adapter; // Pole przechowujące adapter dla listy zadań
    public static final String KEY_EXTRA_TASK_ID = "tasklistfragment.task_id"; // Stała przechowująca klucz do przekazywania identyfikatora zadania jako dodatkowych danych
    private static final String KEY_SUBTITLE_VISIBLE = "subtitleVisible";
    private boolean subtitleVisible;
    public TaskListFragment() {} // Konstruktor domyślny

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false); // Inflacja widoku fragmentu
        recyclerView = view.findViewById(R.id.task_recycler_view); // Znalezienie i przypisanie widoku do zmiennej
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // Ustawienie menedżera układu listy
        return view; // Zwrócenie widoku fragmentu
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView(); // Aktualizacja widoku listy zadań podczas wznowienia fragmentu
    }

    private void updateView() { // Metoda do aktualizacji widoku listy zadań
        TaskStorage taskStorage = TaskStorage.getInstance(); // Pobranie instancji magazynu zadań
        List<Task> tasks = taskStorage.getTasks(); // Pobranie listy zadań z magazynu

        if (adapter == null) { // Jeśli adapter nie został jeszcze utworzony
            adapter = new TaskAdapter(tasks); // Utworzenie adaptera na podstawie listy zadań
            recyclerView.setAdapter(adapter); // Ustawienie adaptera
        } else {
            adapter.notifyDataSetChanged(); // Jeśli adapter już istnieje, zaktualizuj go
        }
        updateSubtitle();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_menu, menu);
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (subtitleVisible)
            subtitleItem.setTitle(R.string.hide_subtitle);
        else
            subtitleItem.setTitle(R.string.show_subtitle);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            subtitleVisible = savedInstanceState.getBoolean(KEY_SUBTITLE_VISIBLE);
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.new_task) {
            Task task = new Task();
            TaskStorage.getInstance().addTask(task);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(KEY_EXTRA_TASK_ID, task.getId());
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.show_subtitle) {
            subtitleVisible = !subtitleVisible;
            getActivity().invalidateOptionsMenu();
            updateSubtitle();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    public void updateSubtitle() {
        String subtitle = null;
        if (subtitleVisible) {
            TaskStorage taskStorage = TaskStorage.getInstance();
            List<Task> tasks = taskStorage.getTasks();
            int todoTasksCount = 0;
            for (Task task : tasks) {
                if (!task.isDone())
                    ++todoTasksCount;
            }
            subtitle = getString(R.string.subtitle_format, todoTasksCount);
            if(!subtitleVisible)
                subtitle = null;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }


    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener { // Przechowywanie widoków elementu listy zadań w adapterze
        private CheckBox taskCheckbox; // Dodanie pola CheckBox
        private ImageView iconImageView;
        private ImageView otherIconImageView;
        private TextView nameTextView, dateTextView;
        private Task task;
        public TaskHolder(LayoutInflater inflater, ViewGroup parent) { // LayoutInflater - nadmuchiwanie (inflating) widoku elementu listy z pliku XML, ViewGroup - określenia, gdzie ten widok będzie dołączony w hierarchii widoku
            super(inflater.inflate(R.layout.list_item_task, parent, false));
            itemView.setOnClickListener(this); // Dodanie nasłuchiwania na kliknięcie na element listy

            taskCheckbox = itemView.findViewById(R.id.taskCheckbox); // Inicjalizacja CheckBox
            iconImageView = itemView.findViewById(R.id.imageView2);// Zaktualizowana kontrolka ImageView
            otherIconImageView = itemView.findViewById(R.id.imageView3); // Dodaj inicjalizację drugiego ImageView
            nameTextView = itemView.findViewById(R.id.task_item_name); // Znalezienie i przypisanie pola tekstowego do zmiennej
            dateTextView = itemView.findViewById(R.id.task_item_date); // Znalezienie i przypisanie pola tekstowego do zmiennej
        }
        public CheckBox getCheckBox() {
            return taskCheckbox;
        }

        public void bind(Task task) {
            this.task = task;

            // Ustawienie wartości CheckBox na podstawie pola task.done
            taskCheckbox.setChecked(task.isDone());

            // Dostosowanie ikony na podstawie kategorii zadania
            if (task.getCategory() == Category.STUDIES) {
                iconImageView.setImageResource(R.drawable.ic_studies);
                otherIconImageView.setVisibility(View.GONE);
            } else if (task.getCategory() == Category.HOME) {
                iconImageView.setImageResource(R.drawable.ic_house);
                otherIconImageView.setVisibility(View.GONE);
            }

            // Sprawdzenie i formatowanie nazwy zadania
            String taskName = task.getName();
            if (task.isDone()) {
                nameTextView.setText(getStrikethroughText(taskName));
            } else {
                // Ucięcie i dodanie trzech kropek dla długich nazw zadań
                nameTextView.setText(getEllipsizedText(taskName, 50)); // 50 to maksymalna długość nazwy, którą możesz dostosować
            }

            dateTextView.setText(task.getDate().toString());
        }

        private CharSequence getStrikethroughText(String text) {
            SpannableString spannable = new SpannableString(text);
            spannable.setSpan(new StrikethroughSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannable;
        }

        private CharSequence getEllipsizedText(String text, int maxLength) {
            if (text.length() > maxLength) {
                return text.substring(0, maxLength) + "...";
            } else {
                return text;
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(KEY_EXTRA_TASK_ID, task.getId());
            startActivity(intent);
        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> { // Dostarczanie danych i zarządzanie widokami elementów listy
        private List<Task> tasks; // Lista zadań

        public TaskAdapter(List<Task> tasks) {
            this.tasks = tasks;
        } // Przekazywanie listy zadań

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // Tworzenie nowych instancji TaskHolder, czyli widoków elementów listy, gdy są one potrzebne do wyświetlenia na liście
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());  // Uzyskanie obiektu
            return new TaskHolder(layoutInflater, parent); // Utworzenie nowego holdera zadania
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            Task task = tasks.get(position); // Pobranie zadania na podstawie pozycji
            holder.bind(task); // Przekazanie zadania do holdera do wyświetlenia
            CheckBox checkBox = holder.getCheckBox();
            checkBox.setChecked(task.isDone());
            checkBox.setOnCheckedChangeListener(((buttonView, isChecked) ->
                    tasks.get(holder.getBindingAdapterPosition()).setDone(isChecked)));
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        } // Zwrócenie liczby zadań w liście
    }
}