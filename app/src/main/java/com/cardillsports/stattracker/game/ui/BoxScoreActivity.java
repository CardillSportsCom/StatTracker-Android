package com.cardillsports.stattracker.game.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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

import static com.cardillsports.stattracker.details.businesslogic.StatsTableAdapter.NON_EDITABLE;

public class BoxScoreActivity extends AppCompatActivity {

    @Inject GameRepository gameRepository;
    private TextView scoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_score);

        GameData gameData = gameRepository.getGameStats();

        TableView tableView = findViewById(R.id.team_1_table_view);

        scoreView = findViewById(R.id.score_textview);

        initTableView(tableView, gameData.getTeamOnePlayers(), gameData.getTeamTwoPlayers());
    }

    private void initTableView(TableView tableView, List<Player> team1, List<Player> team2) {
        tableView.getCellRecyclerView().setMotionEventSplittingEnabled(true);

        StatsTableAdapter adapter = new StatsTableAdapter(this, NON_EDITABLE);
        tableView.setAdapter(adapter);

        List<StatType> columnHeaderItems = Arrays.asList(StatType.values()).subList(2,9);

        List<Player> players = new ArrayList<>();
        players.addAll(team1);
        players.addAll(team2);

        List<List<Stat>> cellList = TableUtils.generateTableCellList(players);

        adapter.setAllItems(columnHeaderItems, players, cellList);

        tableView.setColumnWidth(0, 200);
        tableView.setColumnWidth(1, 250);
        tableView.setColumnWidth(2, 200);
        tableView.setColumnWidth(3, 200);
        tableView.setColumnWidth(4, 200);
        tableView.setColumnWidth(5, 200);
        tableView.setColumnWidth(6, 200);
        tableView.setColumnWidth(7, 200);
        tableView.setColumnWidth(8, 200);
        tableView.setColumnWidth(9, 200);
        tableView.setColumnWidth(10,200);

        int team1Score = 0;
        int team2Score = 0;
        for (Player player : team1) {
            team1Score += player.fieldGoalMade;
        }
        for (Player player : team2) {
            team2Score += player.fieldGoalMade;
        }

        String scoreText = team1Score + " - " + team2Score;
        scoreView.setText(scoreText);
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

