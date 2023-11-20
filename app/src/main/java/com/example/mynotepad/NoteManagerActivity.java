package com.example.mynotepad;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mynotepad.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import java.util.TimeZone;

public class NoteManagerActivity extends AppCompatActivity {

    private GridView gridView;
    private NoteAdapter noteAdapter;
    private NoteDataSource dataSource;
    private Button buttonLock;
    private Button buttonDelete;
    private Note selectedNote;

    Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_manager);
//        editTextNote = findViewById(R.id.editTextNote);
        gridView = findViewById(R.id.gridViewNotes);

        dataSource = new NoteDataSource(this);


//        buttonLock.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle locking the note (show password dialog)
//                if (selectedNote != null) {
//                    showPasswordDialog(selectedNote, "lock_note");
//                }
//            }
//        });
//
//        buttonDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle deleting the note
//                if (selectedNote != null) {
//                    showPasswordDialog(selectedNote, "delete_note");
//                }
//            }
//        });
        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getStringExtra("action");
            if ("add_note".equals(action)) {
                String noteContent = intent.getStringExtra("note_content");
            }
        }
        dataSource.open();
        Log.d("NoteManagerActivity", "onCreate called");
        if (savedInstanceState != null) {
            // Restore state from savedInstanceState
            String savedNoteContent = savedInstanceState.getString("noteContent");
//            editTextNote.setText(savedNoteContent);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize RecyclerView and its adapter
        noteAdapter = new NoteAdapter(this, dataSource.getAllNotes());
        gridView.setAdapter((ListAdapter) noteAdapter);
        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            String action = receivedIntent.getStringExtra("action");
            if ("add_note".equals(action)) {
                String noteContent = receivedIntent.getStringExtra("note_content");
            }
        }
























        FloatingActionButton fabAddNote = findViewById(R.id.fabAddNote);
        // Click listener for FAB to add a new note
        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle adding a new note
                String defaultPassword = "your_default_password_here";
                String noteContent = "Your note content"; // Replace with user input

                // Replace noteId with the actual ID of the note you want to modify
                long noteId = dataSource.createNote(noteContent, defaultPassword);
                Note note = dataSource.getNoteById(noteId); // Fetch the note from the data source

                if (note != null) {
                    if (note.isLocked()) {
                        // Prompt user for password and validate
                        showPasswordDialog(note, "delete_note");
                    } else {
                        // Note is not locked, directly add it
                        noteId = dataSource.createNote(noteContent, defaultPassword);
                        long startTimeMillis = System.currentTimeMillis();
                        long endTimeMillis = startTimeMillis + (5 * 60 * 1000);
                        addToCalendar("Note Reminder", startTimeMillis, endTimeMillis);
                        int rowsUpdated = dataSource.updateNote(noteId, "Updated content", defaultPassword);
                        if (rowsUpdated > 0) {
                            addToCalendar("Updated Note Reminder", startTimeMillis, endTimeMillis);
                        }
                        noteAdapter.setNotes(dataSource.getAllNotes());
                        noteAdapter.notifyDataSetChanged();
                    }
                }
            }

//            @Override
//            public boolean onCreateOptionsMenu(Menu menu) {
//                getMenuInflater().inflate(R.menu.note_manager_menu, menu);
//                return true;
//            }
//
//            @Override
//            public boolean onOptionsItemSelected(MenuItem item) {
//                if (item.getItemId() == R.id.action_add_note) {
//                    EditText editText = findViewById(R.id.editTextNote);
//                    String noteContent = editText.getText().toString();
//                    Intent intent = new Intent(NoteManagerActivity.this, NoteManagerActivity.class);
//                    intent.putExtra("action", "add_note");
//                    intent.putExtra("note_content", noteContent);
//                    startActivity(intent);
//                    return true;
//                }
//                return super.onOptionsItemSelected(item);
//            }
//
















            private void showPasswordDialog(final Note note, final String action) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NoteManagerActivity.this);
                builder.setTitle("Enter Password");
                final EditText passwordInput = new EditText(NoteManagerActivity.this);
                builder.setView(passwordInput);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String enteredPassword = passwordInput.getText().toString();
                        if (enteredPassword.equals(note.getPassword())) {
                            if ("lock_note".equals(action)) {
                                // Password is correct, lock the note (update isLocked property)
                                note.setLocked(true);
                                updateNoteInDatabase(note); // Update the note with the new lock state
                                Toast.makeText(getApplicationContext(), "Note is locked", Toast.LENGTH_SHORT).show();
                            } else if ("delete_note".equals(action)) {
                                deleteNoteAndRefreshList(note);
                            } else if ("edit_note".equals(action)) {
                                // Handle editing the note here
                            }
                        } else {
                            // Password is incorrect, show an error message
                            Toast.makeText(getApplicationContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    }

                    private void updateNoteInDatabase(Note note) {
                        // Implement the logic to update the note's lock state in the database
                        // For example, you can update the 'isLocked' property in the database
                    }

                    private void deleteNoteAndRefreshList(Note note) {
                        // Delete the note from the database
                        dataSource.open();
                        dataSource.deleteNote(note.getId());
                        dataSource.close();

                        // Refresh the RecyclerView to reflect the changes
                        noteAdapter.setNotes(dataSource.getAllNotes());
                        noteAdapter.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                buttonLock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle locking the note (show password dialog)
                        if (selectedNote != null) {
                            showPasswordDialog(selectedNote, "lock_note");
                        }
                    }
                });

                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle deleting the note
                        if (selectedNote != null) {
                            showPasswordDialog(selectedNote, "delete_note");
                        }
                    }
                });

            }

        });
    }

            private void addNoteToDatabase(String noteContent) {
        String defaultPassword = "your_default_password_here"; // Replace with your default password
        long noteId = dataSource.createNote(noteContent, defaultPassword);
        noteAdapter.setNotes(dataSource.getAllNotes());
        noteAdapter.notifyDataSetChanged();
    }

    public void addToCalendar(String eventTitle, long startTimeMillis, long endTimeMillis) {
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startTimeMillis);
        values.put(CalendarContract.Events.DTEND, endTimeMillis);
        values.put(CalendarContract.Events.TITLE, eventTitle);
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        long eventId = Long.parseLong(uri.getLastPathSegment());
    }

    // Handle other actions such as editing and deleting notes

    @Override
    protected void onDestroy() {
        dataSource.close();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        String noteContent = editTextNote.getText().toString();
//        outState.putString("noteContent", noteContent);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            String savedNoteContent = savedInstanceState.getString("noteContent");
//            editTextNote.setText(savedNoteContent);
        }
    }

    public void deleteNoteAndRefreshList(Note note) {
        dataSource.open();
        dataSource.deleteNote(note.getId());
        dataSource.close();
        noteAdapter.setNotes(dataSource.getAllNotes());
        noteAdapter.notifyDataSetChanged();
    }

}
