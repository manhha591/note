package com.example.note.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.example.note.database.respository.NoteRepository;
import com.example.note.model.Note;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

// NoteViewModel definition
public class NoteViewModel extends ViewModel {

    private final NoteRepository noteRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<List<Note>> allNotes = new MutableLiveData<>();

    public NoteViewModel(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
        loadAllNotes();
    }

    public void insertNote(Note note) {
        Disposable disposable = noteRepository.insertNote(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            // Handle success
                            System.out.println("Note inserted successfully");
                        },
                        throwable -> {
                            // Handle error
                            System.err.println("Error inserting note: " + throwable.getMessage());
                            throwable.printStackTrace();
                        }
                );

        disposables.add(disposable);
    }

    // Update note method
    public void updateNote(Note note) {
        Disposable disposable = noteRepository.updateNote(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, throwable -> throwable.printStackTrace());

        disposables.add(disposable);
    }

    // Delete note method
    public void deleteNote(Note note) {
        Disposable disposable = noteRepository.deleteNote(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, throwable -> throwable.printStackTrace());

        disposables.add(disposable);
    }

    // Get all notes method
    public LiveData<List<Note>> getAllNotes() {

        return allNotes;
    }
    public void loadAllNotes() {
       disposables.add( noteRepository.getAllNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        notes -> allNotes.setValue(notes)
                ));
    }
    public void loadNotesByState(Long startDate, Long endDate) {
        Disposable disposable = noteRepository.getByDate(startDate, endDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        notes -> allNotes.setValue(notes),
                        throwable -> Log.e("NoteViewModel", "Error loading notes", throwable)
                );

        disposables.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear(); // Dispose of all disposables when the ViewModel is cleared
    }

    // Static initializer for ViewModel
    public static class NoteViewModelFactory extends ViewModelProvider.NewInstanceFactory {

        private final NoteRepository noteRepository;


        public NoteViewModelFactory(Context context) {
            this.noteRepository = new NoteRepository(context);

        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(NoteViewModel.class)) {
                return (T) new NoteViewModel(noteRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
