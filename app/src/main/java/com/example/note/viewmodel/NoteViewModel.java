package com.example.note.viewmodel;


import android.app.Application;
import androidx.lifecycle.LiveData;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.note.database.respository.NoteRepository;
import com.example.note.model.Note;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NoteViewModel extends ViewModel {

    private final NoteRepository noteRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public NoteViewModel(Application application) {
        noteRepository = new NoteRepository(application);
    }

    public void insertNote(Note note) {
        Disposable disposable = noteRepository.insertNote(note)
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {}, throwable -> throwable.printStackTrace());

        disposables.add(disposable);
    }

    public void updateNote(Note note) {
        Disposable disposable = noteRepository.updateNote(note)
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {}, throwable -> throwable.printStackTrace());

        disposables.add(disposable);
    }

    public void deleteNote(Note note) {
        Disposable disposable = noteRepository.deleteNote(note)
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {}, throwable -> throwable.printStackTrace());

        disposables.add(disposable);
    }

    public LiveData<List<Note>> getAllNote() {
        return LiveDataReactiveStreams.fromPublisher(
                noteRepository.getAllNote()
                        .subscribeOn(Schedulers.io())
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public static class NoteViewModelFactory implements ViewModelProvider.Factory {

        private final Application application;

        public NoteViewModelFactory(Application application) {
            this.application = application;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(NoteViewModel.class)) {
                return (T) new NoteViewModel(application);
            }
            throw new IllegalArgumentException("Unable to construct ViewModel");
        }
    }
}
