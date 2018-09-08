package com.cardillsports.stattracker.common.data;

import com.cardillsports.stattracker.game.data.JSONGameStats;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class MockCardillService implements CardillService {


    @Override
    public Observable<LeaguePlayersResponse> getPlayersForLeague(String leagueId) {
        String respString = "{\"players\":[{\"player\":{\"_id\":\"5abeccd77e05fb641c956921\",\"firstName\":\"jason\",\"lastName\":\"rajasegaram\"}},{\"player\":{\"_id\":\"5b8235f58db19f000443df72\",\"firstName\":\"Vithushan\",\"lastName\":\"Namasivayasivam\"}},{\"player\":{\"_id\":\"5b8236128db19f000443df74\",\"firstName\":\"Sujee\",\"lastName\":\"Kalanadan\"}},{\"player\":{\"_id\":\"5b8236318db19f000443df75\",\"firstName\":\"Mith\",\"lastName\":\"Sivanasan\"}},{\"player\":{\"_id\":\"5b8236528db19f000443df77\",\"firstName\":\"Prageen\",\"lastName\":\"Sivabalan\"}},{\"player\":{\"_id\":\"5b8236778db19f000443df78\",\"firstName\":\"Prez\",\"lastName\":\"Karunagaran\"}},{\"player\":{\"_id\":\"5b82368a8db19f000443df79\",\"firstName\":\"Sukee\",\"lastName\":\"Krishnabagavan\"}},{\"player\":{\"_id\":\"5b8236438db19f000443df76\",\"firstName\":\"JV\",\"lastName\":\"Dave\"}}]}\n";
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        LeaguePlayersResponse response = gson.fromJson(respString, LeaguePlayersResponse.class);

        return Observable.just(response);
    }

    @Override
    public Observable<ResponseBody> saveGameStats(JSONGameStats jsonGameStats) {
        return Observable.empty();
    }
}
