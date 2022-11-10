package com.example.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.models.NoteRequest
import com.example.diary.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository): ViewModel() {

    val notesLiveData get() = noteRepository.notesLiveData
    val statusLiveData get() = noteRepository.statusLiveData

    fun getAllNotes(){
        viewModelScope.launch {
            noteRepository.getNotes()
        }
    }

    fun createNote(noteRequest: NoteRequest){
        viewModelScope.launch {
            noteRepository.createNote(noteRequest)
        }
    }

    fun deleteNote(noteId : String){
        viewModelScope.launch {
            noteRepository.deleteNote(noteId)
        }
    }

    fun updateNote(noteId : String, noteRequest: NoteRequest){
        viewModelScope.launch {
            noteRepository.updateNote(noteId, noteRequest)
        }
    }

}