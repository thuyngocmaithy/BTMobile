package com.example.appnotes.listener;

import com.example.appnotes.entitities.Note;

public interface NoteListener {
    void onNoteClicked(Note note, int position);
}
