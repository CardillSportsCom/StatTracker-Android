package com.cardillsports.stattracker.details.businesslogic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.cardillsports.stattracker.R;
import com.cardillsports.stattracker.common.data.Player;
import com.cardillsports.stattracker.game.data.Stat;
import com.cardillsports.stattracker.game.data.StatType;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

public class StatsTableAdapter extends AbstractTableAdapter<StatType, Player, Stat> {

    private final Context context;

    public StatsTableAdapter(Context context) {
        super(context);

        this.context = context;
    }

    /**
     * This is sample CellViewHolder class
     * This viewHolder must be extended from AbstractViewHolder class instead of RecyclerView.ViewHolder.
     */
    class MyCellViewHolder extends AbstractViewHolder {

        public final ElegantNumberButton editText;

        public MyCellViewHolder(View itemView) {
            super(itemView);
            editText = itemView.findViewById(R.id.stat_button);
        }
    }

    /**
     * This is where you create your custom Cell ViewHolder. This method is called when Cell
     * RecyclerView of the TableView needs a new RecyclerView.ViewHolder of the given type to
     * represent an item.
     *
     * @param viewType : This value comes from #getCellItemViewType method to support different type
     *                 of viewHolder as a Cell item.
     * @see #getCellItemViewType(int);
     */
    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        // Get cell xml layout
        View layout = LayoutInflater.from(context).inflate(R.layout.stat_item,
                parent, false);
        // Create a Custom ViewHolder for a Cell item.
        return new MyCellViewHolder(layout);
    }

    /**
     * That is where you set Cell View Model data to your custom Cell ViewHolder. This method is
     * Called by Cell RecyclerView of the TableView to display the data at the specified position.
     * This method gives you everything you need about a cell item.
     *
     * @param holder         : This is one of your cell ViewHolders that was created on
     *                       ```onCreateCellViewHolder``` method. In this example we have created
     *                       "MyCellViewHolder" holder.
     * @param cellItemModel  : This is the cell view model located on this X and Y position. In this
     *                       example, the model class is "Cell".
     * @param columnPosition : This is the X (Column) position of the cell item.
     * @param rowPosition    : This is the Y (Row) position of the cell item.
     * @see #onCreateCellViewHolder(ViewGroup, int);
     */
    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object cellItemModel, int
            columnPosition, int rowPosition) {
        Stat cell = (Stat) cellItemModel;

        // Get the holder to update cell item text
        MyCellViewHolder viewHolder = (MyCellViewHolder) holder;

        viewHolder.editText.setNumber(String.valueOf(cell.getCount()));

        // If your TableView should have auto resize for cells & columns.
        // Then you should consider the below lines. Otherwise, you can ignore them.

        // It is necessary to remeasure itself.
        viewHolder.itemView.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        viewHolder.editText.requestLayout();
    }


    /**
     * This is sample CellViewHolder class.
     * This viewHolder must be extended from AbstractViewHolder class instead of RecyclerView.ViewHolder.
     */
    class MyColumnHeaderViewHolder extends AbstractViewHolder {

        public final TextView cell_textview;
        public final ViewGroup container;

        public MyColumnHeaderViewHolder(View itemView) {
            super(itemView);
            cell_textview = (TextView) itemView.findViewById(R.id.stat_label);
            container = itemView.findViewById(R.id.container);
        }
    }

    /**
     * This is where you create your custom Column Header ViewHolder. This method is called when
     * Column Header RecyclerView of the TableView needs a new RecyclerView.ViewHolder of the given
     * type to represent an item.
     *
     * @param viewType : This value comes from "getColumnHeaderItemViewType" method to support
     *                 different type of viewHolder as a Column Header item.
     * @see #getColumnHeaderItemViewType(int);
     */
    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {

        // Get Column Header xml Layout
        View layout = LayoutInflater.from(context).inflate(R.layout
                .table_view_column_header_layout, parent, false);

        // Create a ColumnHeader ViewHolder
        return new MyColumnHeaderViewHolder(layout);
    }

    /**
     * That is where you set Column Header View Model data to your custom Column Header ViewHolder.
     * This method is Called by ColumnHeader RecyclerView of the TableView to display the data at
     * the specified position. This method gives you everything you need about a column header
     * item.
     *
     * @param holder                : This is one of your column header ViewHolders that was created on
     *                              ```onCreateColumnHeaderViewHolder``` method. In this example we have created
     *                              "MyColumnHeaderViewHolder" holder.
     * @param columnHeaderItemModel : This is the column header view model located on this X position. In this
     *                              example, the model class is "ColumnHeader".
     * @param position              : This is the X (Column) position of the column header item.
     * @see #onCreateColumnHeaderViewHolder(ViewGroup, int) ;
     */
    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object columnHeaderItemModel, int
            position) {
        StatType columnHeader = (StatType) columnHeaderItemModel;

        // Get the holder to update cell item text
        MyColumnHeaderViewHolder columnHeaderViewHolder = (MyColumnHeaderViewHolder) holder;
        columnHeaderViewHolder.cell_textview.setText(columnHeader.name());

        // If your TableView should have auto resize for cells & columns.
        // Then you should consider the below lines. Otherwise, you can ignore them.

        // It is necessary to remeasure itself.
        columnHeaderViewHolder.container.getLayoutParams().width = LinearLayout
                .LayoutParams.WRAP_CONTENT;
        columnHeaderViewHolder.cell_textview.requestLayout();
    }

    /**
     * This is sample CellViewHolder class.
     * This viewHolder must be extended from AbstractViewHolder class instead of RecyclerView.ViewHolder.
     */
    class MyRowHeaderViewHolder extends AbstractViewHolder {

        public final TextView cell_textview;

        public MyRowHeaderViewHolder(View itemView) {
            super(itemView);
            cell_textview = (TextView) itemView.findViewById(R.id.player_label);
        }
    }


    /**
     * This is where you create your custom Row Header ViewHolder. This method is called when
     * Row Header RecyclerView of the TableView needs a new RecyclerView.ViewHolder of the given
     * type to represent an item.
     *
     * @param viewType : This value comes from "getRowHeaderItemViewType" method to support
     *                 different type of viewHolder as a row Header item.
     * @see #getRowHeaderItemViewType(int);
     */
    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {

        // Get Row Header xml Layout
        View layout = LayoutInflater.from(context).inflate(R.layout
                .table_view_row_header_layout, parent, false);

        // Create a Row Header ViewHolder
        return new MyRowHeaderViewHolder(layout);
    }


    /**
     * That is where you set Row Header View Model data to your custom Row Header ViewHolder. This
     * method is Called by RowHeader RecyclerView of the TableView to display the data at the
     * specified position. This method gives you everything you need about a row header item.
     *
     * @param holder             : This is one of your row header ViewHolders that was created on
     *                           ```onCreateRowHeaderViewHolder``` method. In this example we have created
     *                           "MyRowHeaderViewHolder" holder.
     * @param rowHeaderItemModel : This is the row header view model located on this Y position. In this
     *                           example, the model class is "RowHeader".
     * @param position           : This is the Y (row) position of the row header item.
     * @see #onCreateRowHeaderViewHolder(ViewGroup, int) ;
     */
    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object rowHeaderItemModel, int
            position) {
        Player rowHeader = (Player) rowHeaderItemModel;

        // Get the holder to update row header item text
        MyRowHeaderViewHolder rowHeaderViewHolder = (MyRowHeaderViewHolder) holder;
        rowHeaderViewHolder.cell_textview.setText(rowHeader.firstName());
    }


    @Override
    public View onCreateCornerView() {
        // Get Corner xml layout
        return LayoutInflater.from(context).inflate(R.layout.table_view_corner_layout, null);
    }

    @Override
    public int getColumnHeaderItemViewType(int columnPosition) {
        // The unique ID for this type of column header item
        // If you have different items for Cell View by X (Column) position,
        // then you should fill this method to be able create different
        // type of CellViewHolder on "onCreateCellViewHolder"
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int rowPosition) {
        // The unique ID for this type of row header item
        // If you have different items for Row Header View by Y (Row) position,
        // then you should fill this method to be able create different
        // type of RowHeaderViewHolder on "onCreateRowHeaderViewHolder"
        return 0;
    }

    @Override
    public int getCellItemViewType(int columnPosition) {
        // The unique ID for this type of cell item
        // If you have different items for Cell View by X (Column) position,
        // then you should fill this method to be able create different
        // type of CellViewHolder on "onCreateCellViewHolder"
        return 0;
    }
}
