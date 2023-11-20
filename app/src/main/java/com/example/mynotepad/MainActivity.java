package com.example.mynotepad;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button buttonSave; // Declare the button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttonSave = findViewById(R.id.buttonSave); // Initialize the button

//        buttonSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle the "Save" button click
//                String noteContent = "Your note content"; // Get the note content (you can retrieve it from an EditText or any input field)
//                Intent intent = new Intent(MainActivity.this, NoteManagerActivity.class);
//                intent.putExtra("note_content", noteContent);
//                startActivity(intent);
//            }
//        }
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.editTextNote);
                String noteContent = editText.getText().toString();
                Intent intent = new Intent(MainActivity.this, NoteManagerActivity.class);
                intent.putExtra("action", "add_note"); // Change the key to "action"
                intent.putExtra("note_content", noteContent);
                startActivity(intent);
            }
        });


    }
}