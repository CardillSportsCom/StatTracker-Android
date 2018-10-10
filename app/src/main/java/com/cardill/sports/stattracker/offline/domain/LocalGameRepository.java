package com.cardill.sports.stattracker.offline.domain;

import com.cardill.sports.stattracker.game.data.GameData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

/**
 * Responsible for CRUD operations against local gameData repository
 */
public interface LocalGameRepository {
    Single<GameData> add(GameData gameData);

    Single<ArrayList<GameData>> get();

}
