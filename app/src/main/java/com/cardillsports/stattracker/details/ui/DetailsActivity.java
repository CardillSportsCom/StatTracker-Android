package com.cardillsports.stattracker.details.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.cardillsports.stattracker.R;
import com.cardillsports.stattracker.common.data.Player;
import com.cardillsports.stattracker.common.ui.TableUtils;
import com.cardillsports.stattracker.details.businesslogic.StatsTableAdapter;
import com.cardillsports.stattracker.game.data.GameData;
import com.cardillsports.stattracker.game.data.GameRepository;
import com.cardillsports.stattracker.game.data.Stat;
import com.cardillsports.stattracker.game.data.StatType;
import com.evrencoskun.tableview.TableView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.disposables.Disposable;

public class DetailsActivity extends AppCompatActivity {

    @Inject GameRepository gameRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        GameData gameData = gameRepository.getGameStats();
        List<Player> players = new ArrayList<>();
        players.addAll(gameData.getTeamOnePlayers());
        players.addAll(gameData.getTeamTwoPlayers());

        TableView tableView = findViewById(R.id.team_1_table_view);


        initTableView(tableView, players, StatsTableAdapter.EDITABLE);
    }

    private void initTableView(TableView tableView, List<Player> players, int viewType) {
        tableView.getCellRecyclerView().setMotionEventSplittingEnabled(true);
        StatsTableAdapter adapter = new StatsTableAdapter(this, viewType);

        tableView.setAdapter(adapter);

        List<StatType> columnHeaderItems = Arrays.asList(StatType.values()).subList(0,7);
        List<List<Stat>> mCellList = TableUtils.generateTableCellList(players);

        adapter.setAllItems(columnHeaderItems, players, mCellList);

        int numOfTeamOnePlayers = gameRepository.getGameStats().getTeamOnePlayers().size();

        Disposable disposable = adapter.getChangeEvents()
                .subscribe(detailsChangedEvent -> {
                    int rowPosition = detailsChangedEvent.getRowPosition();
                    Player player;
                    if (rowPosition >= numOfTeamOnePlayers) {
                        rowPosition -= numOfTeamOnePlayers;
                        player = gameRepository.getGameStats().getTeamTwoPlayers().get(rowPosition);
                    } else {
                        player = gameRepository.getGameStats().getTeamOnePlayers().get(rowPosition);
                    }

                    switch (detailsChangedEvent.getColumnPosition()) {
                        case 0:
                            gameRepository.updateStats(player.id(), StatType.FGM, detailsChangedEvent.getNewValue());
                            break;
                        case 1:
                            gameRepository.updateStats(player.id(), StatType.MISSES, detailsChangedEvent.getNewValue());
                            break;
                        case 2:
                            gameRepository.updateStats(player.id(), StatType.AST, detailsChangedEvent.getNewValue());
                            break;
                        case 3:
                            gameRepository.updateStats(player.id(), StatType.REB, detailsChangedEvent.getNewValue());
                            break;
                        case 4:
                            gameRepository.updateStats(player.id(), StatType.STL, detailsChangedEvent.getNewValue());
                            break;
                        case 5:
                            gameRepository.updateStats(player.id(), StatType.BLK, detailsChangedEvent.getNewValue());
                            break;
                        case 6:
                            gameRepository.updateStats(player.id(), StatType.TO, detailsChangedEvent.getNewValue());
                            break;
                        default:
                            throw new UnsupportedOperationException("Invalid column number");
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_done) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
