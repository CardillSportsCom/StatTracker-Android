package com.cardill.sports.stattracker.scores.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.cardill.sports.stattracker.R;
import com.cardill.sports.stattracker.common.data.CardillService;
import com.cardill.sports.stattracker.common.ui.BaseFragment;
import com.cardill.sports.stattracker.scores.businesslogic.GameDaysAdapter;
import com.cardill.sports.stattracker.scores.businesslogic.ScoreEvent;
import com.cardill.sports.stattracker.scores.businesslogic.ScoresPresenter;
import com.cardill.sports.stattracker.scores.model.GameDays;

import javax.inject.Inject;

import androidx.navigation.fragment.NavHostFragment;
import io.reactivex.disposables.Disposable;

import static com.cardill.sports.stattracker.scores.ui.GameDayFragment.GAME_DAY;

/**
 * Created by vithushan on 9/10/18.
 */

public class ScoresFragment extends BaseFragment implements ScoresViewBinder {

    private RecyclerView recycler;
    private ProgressBar mProgress;
    private ScoresPresenter mPresenter;

    @Inject CardillService cardillService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scores, container, false);

        recycler = view.findViewById(R.id.recycler_view);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        mProgress = view.findViewById(R.id.progress);

        mPresenter = new ScoresPresenter(this, cardillService);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadScores();
    }

    @Override
    public void loadGameDays(GameDays gameDays) {
        mProgress.setVisibility(View.GONE);

        GameDaysAdapter adapter = new GameDaysAdapter(gameDays.getGameDays());
        Disposable subscribe = adapter.getEventObservable()
                .subscribe(this::gotoGameDay);

        recycler.setAdapter(adapter);
    }

    private void gotoGameDay(ScoreEvent.DateSelected dateSelected) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(GAME_DAY, dateSelected.getGameDay());
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_scoresFragment_to_gameDayFragment,
                        bundle);

    }
}