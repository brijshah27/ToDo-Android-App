package com.sargent.mark.todolist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sargent.mark.todolist.data.Contract;

/**
 * Created by mark on 7/4/17.
 */

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ItemHolder> {

    private Cursor cursor;
    private ItemClickListener listener;
    private String TAG = "todolistadapter";
    private SQLiteDatabase db;

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item, parent, false);
        ItemHolder holder = new ItemHolder(view);
        return holder;
    }



    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(holder, position);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }



    public interface ItemClickListener {
        void onItemClick(int pos, String description, String duedate,String category, long id);
    }

    public ToDoListAdapter(Cursor cursor, ItemClickListener listener) {
        this.cursor = cursor;
        this.listener = listener;
    }

    public void swapCursor(Cursor newCursor){
        if (cursor != null) cursor.close();
        cursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }



    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView descr;
        TextView due;
        TextView debug;
        String duedate;
        String debug_data;
        String description;
        CheckBox status;
        String category;
        boolean undone;
        TextView caterogyTV;

        long id;


        ItemHolder(View view) {
            super(view);
            descr = (TextView) view.findViewById(R.id.description);
            due = (TextView) view.findViewById(R.id.dueDate);

            //[Brij:] Initialize category textview.
            caterogyTV = (TextView) view.findViewById(R.id.category);
            debug = (TextView) view.findViewById(R.id.debug);//[Brij:]just for debugging purpose.
            //[Brij:] Initialize checkbox
            status = (CheckBox) view.findViewById(R.id.status);
            view.setOnClickListener(this);
        }

        public void bind(ItemHolder holder, int pos) {
            cursor.moveToPosition(pos);
            id = cursor.getLong(cursor.getColumnIndex(Contract.TABLE_TODO._ID));
            Log.d(TAG, "deleting id: " + id);

            category = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_CATEGORY));
            duedate = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DUE_DATE));
            description = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DESCRIPTION));
            //debug_data = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_STATUS));


            //[Brij:]setting status to not done by default
            undone = cursor.getInt(
                    cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_STATUS)) ==
                    1;
            descr.setText("Todo: "+description);
            due.setText("Due by: "+duedate);
            //[Brij:]Set category text.
            caterogyTV.setText("Category: "+category);
            //debug.setText(debug_data);


            holder.itemView.setTag(id);

            updateUi();


            //[Brij:] Adding listener on checkbox to add mark as done/undone.

            status.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Log.d(TAG, "ITEM ID: " + id);
                    //[Brij:] Check for todos status and call helper method accordingly.

                    undone = !undone;
                    //updateUi();
                    if(!status.isChecked()){
                       undone = false;
                        //markDone();
                    }else{
                        undone = true;
                        //markAsUndone();
                    }

                    //[Brij:]update UI based on status value
                    updateUi();
                    Log.d(TAG, "Before call:" + undone);

                    //[Brij:]Make changes to Database.
                    MainActivity.updateTodoStatus(pos, id, undone);
                    Log.d(TAG, "After call:>>>>>>>>>>>>>" + undone);

                }
            });

        }



        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onItemClick(pos, description, duedate,category, id);
        }

        //[Brij:] Update Todo's UI based on status value.
        private void updateUi(){
            if(undone){
                descr.setPaintFlags(descr.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                due.setPaintFlags(due.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                caterogyTV.setPaintFlags(due.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            else{
                descr.setPaintFlags(descr.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                due.setPaintFlags(due.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                caterogyTV.setPaintFlags(due.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }

        }
        //[Brij:] Make text striketrough for done todos.
//        private void markDone() {
//            descr.setPaintFlags(descr.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            due.setPaintFlags(due.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            caterogyTV.setPaintFlags(due.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        }

        //[Brij:] Make text normal for not done todos.
//        private void markAsUndone() {
//            descr.setPaintFlags(descr.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
//            due.setPaintFlags(due.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
//            caterogyTV.setPaintFlags(due.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
//        }

    }

}
