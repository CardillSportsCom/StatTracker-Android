package com.cardill.sports.stattracker.creator.offline.domain;

import com.cardill.sports.stattracker.common.data.GameData;

import io.reactivex.Completable;

public class SaveGameUseCase {
    private final LocalGameRepository localGameRepository;
    private final SyncGameUseCase syncGameUseCase;

    public SaveGameUseCase(LocalGameRepository localGameRepository, SyncGameUseCase syncGameUseCase) {
        this.localGameRepository = localGameRepository;
        this.syncGameUseCase = syncGameUseCase;
    }

    public Completable saveGame(GameData gameData) {
        return localGameRepository.add(gameData)
                .flatMapCompletable(syncGameUseCase::syncGame);
    }
}
