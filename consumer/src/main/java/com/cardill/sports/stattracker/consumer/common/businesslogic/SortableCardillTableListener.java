package com.cardill.sports.stattracker.consumer.common.businesslogic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.listener.ITableViewListener;
import com.evrencoskun.tableview.sort.SortState;

import androidx.navigation.Navigation;

import com.cardill.sports.stattracker.common.R;

public class SortableCardillTableListener implements ITableViewListener {

    public static final String PLAYER_ID_KEY = "player-id-key";

    TableView tableView; //Be careful of the listener and table holding references to each other

    public SortableCardillTableListener(TableView tableView) {
        this.tableView = tableView;
    }

    @Override
    public void onCellClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {

    }

    @Override
    public void onCellLongPressed(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {

    }

    @Override
    public void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder columnHeaderView, int column) {
        SortState sortingStatus = tableView.getSortingStatus(column);
        if (sortingStatus != SortState.DESCENDING) {
            tableView.sortColumn(column, SortState.DESCENDING);
        } else {
            tableView.sortColumn(column, SortState.ASCENDING);
        }
        tableView.scrollToRowPosition(0);
    }

    @Override
    public void onColumnHeaderLongPressed(@NonNull RecyclerView.ViewHolder columnHeaderView, int column) {

    }

    @Override
    public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {
        if (rowHeaderView instanceof ConsumerPlayerStatsTableAdapter.MyRowHeaderViewHolder) {
            ConsumerPlayerStatsTableAdapter.MyRowHeaderViewHolder holder =
                    (ConsumerPlayerStatsTableAdapter.MyRowHeaderViewHolder) rowHeaderView;
            Bundle params = new Bundle();
            params.putString(PLAYER_ID_KEY, holder.getPlayer().getPlayer().id());
            //TODO navigate to profile once profile is in a consumer module
            Navigation.findNavController(tableView).navigate(R.id.profileActivity, params);
        }
    }

    @Override
    public void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {

    }
}
