package com.cardill.sports.stattracker.creator.offline.domain.services;

import com.cardill.sports.stattracker.common.data.GameData;

public class SyncGameResponse {
    public final SyncResponseEventType eventType;
    public final GameData gameData;

    public SyncGameResponse(SyncResponseEventType eventType, GameData gameData) {
        this.eventType = eventType;
        this.gameData = gameData;
    }
}
