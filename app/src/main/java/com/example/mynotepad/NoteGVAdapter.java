package com.example.mynotepad;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class NoteGVAdapter extends ArrayAdapter<Note> {
    public NoteGVAdapter(Context context, ArrayList<Note> noteList) {
        super(context, 0, noteList);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_note, parent, false);
        }


        Note note = getItem(position);
        ImageView noteIV = listItemView.findViewById(R.id.idIVcourse);
        if (note != null) {
            int imgResourceId = note.getImgResourceId();
            if (imgResourceId != -1) { // Assuming -1 indicates no image
                noteIV.setImageResource(imgResourceId);
            }
        }


        return listItemView;
    }


}
