package com.example.mynotepad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class NoteAdapter extends BaseAdapter {

    private List<Note> noteList;
    private Context context;

    public NoteAdapter(Context context, List<Note> allNotes) {
        this.context = context;
        this.noteList = allNotes;
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_note, null);
        }

        final Note note = noteList.get(position);

        TextView textViewContent = convertView.findViewById(R.id.textViewContent);
        TextView textViewTimestamp = convertView.findViewById(R.id.textViewTimestamp);
        Button buttonDelete = convertView.findViewById(R.id.buttonDelete);
        Button buttonLock = convertView.findViewById(R.id.buttonLock);

        String content = note.getContent();
        if (content != null && !content.isEmpty()) {
            textViewContent.setText(content);
        } else {
            textViewContent.setText("");
        }
        textViewTimestamp.setText(note.getTimestamp());

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (note.isLocked()) {
                    showPasswordDialog(note);
                } else {
                    deleteNoteAndRefreshList(note);
                }
            }
        });

        buttonLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (note.isLocked()) {
                    note.setLocked(false);
                    buttonLock.setText("Lock");
                } else {
                    note.setLocked(true);
                    buttonLock.setText("Unlock");
                }
            }
        });

        return convertView;
    }

    private void showPasswordDialog(final Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter Password");
        final EditText passwordInput = new EditText(context);
        builder.setView(passwordInput);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String enteredPassword = passwordInput.getText().toString();
                if (enteredPassword.equals(note.getPassword())) {
                    deleteNoteAndRefreshList(note);
                } else {
                    Toast.makeText(context, "Incorrect password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void deleteNoteAndRefreshList(Note note) {
        noteList.remove(note);
        notifyDataSetChanged();

        if (context instanceof NoteManagerActivity) {
            ((NoteManagerActivity) context).deleteNoteAndRefreshList(note);
        }
    }

    // Add a method to update the noteList when changes occur
    public void updateNoteList(List<Note> updatedList) {
        noteList = updatedList;
        notifyDataSetChanged();
    }

    public void setNotes(List<Note> notes) {
        this.noteList = notes;
        notifyDataSetChanged();
    }
}
