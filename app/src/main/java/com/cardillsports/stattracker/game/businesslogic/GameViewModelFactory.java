package com.cardillsports.stattracker.game.businesslogic;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.cardillsports.stattracker.offline.domain.SaveGameUseCase;

public class GameViewModelFactory implements ViewModelProvider.Factory {

    private final SaveGameUseCase saveGameUseCase;

    public GameViewModelFactory(SaveGameUseCase saveGameUseCase1) {
        this.saveGameUseCase = saveGameUseCase1;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GameViewModel.class)) {
            return (T) new GameViewModel(saveGameUseCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}