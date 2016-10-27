package com.example.kiwi.tpprogresstracker.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kiwi.tpprogresstracker.R;
import com.example.kiwi.tpprogresstracker.database.DBManager;
import com.example.kiwi.tpprogresstracker.database.DBTableStructure;
import com.example.kiwi.tpprogresstracker.model.HeaderItem;
import com.example.kiwi.tpprogresstracker.model.InnerActionItems;
import com.example.kiwi.tpprogresstracker.model.ListItem;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.example.kiwi.tpprogresstracker.R.id.txtActionItem;

/**
 * Created by kiwi on 10/10/2016.
 */

public class ActionItemAdapter extends RecyclerView.Adapter<ActionItemAdapter.ActionItemViewHolder> {

    Context m_Context;
    ArrayList<ListItem> m_ActionItems;
    int totalCount = 0;
    ActionItemClickListener actionItemClickListener;

    public ActionItemAdapter(Context context, ArrayList<ListItem> actionItemInfoList) {
        super();
        this.m_Context = context;
        this.m_ActionItems = actionItemInfoList;
    }

    @Override
    public ActionItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("viewType position", "onCreateViewHolder: " + viewType);
        if (viewType == ListItem.TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_do_list_parent, parent, false);
            ParentItem parentItem = new ParentItem(view);
            return parentItem;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_do_list_child, parent, false);
            ChildItem childItem = new ChildItem(view, new MyCustomEditTextListener());
            return childItem;
        }
    }

    @Override
    public void onBindViewHolder(ActionItemViewHolder holder, final int position) {
        if (holder.getItemViewType() == ListItem.TYPE_HEADER) {
            ParentItem v = (ParentItem) holder;
            HeaderItem items = (HeaderItem) m_ActionItems.get(position);
            v.txtCurrentDay.setText("day " + String.valueOf(items.getDay()));
        } else if (holder.getItemViewType() == ListItem.TYPE_CHILD_ITEM) {
            ChildItem childView = (ChildItem) holder;
            final InnerActionItems items = (InnerActionItems) m_ActionItems.get(position);

            childView.txtActionItem.setTag(position);
            childView.txtActionItem.setTag(txtActionItem, m_ActionItems.get(position));
            if (!items.isCurrent()) {
                childView.txtActionItem.setEnabled(false);
                childView.viewSeperator.setVisibility(View.GONE);
                childView.imgMinus.setVisibility(View.GONE);
            } else {
                childView.txtActionItem.setEnabled(true);
                childView.viewSeperator.setVisibility(View.VISIBLE);
                childView.imgMinus.setVisibility(View.VISIBLE);
            }
            childView.imgMinus.setTag(m_ActionItems.get(position));
            childView.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());
            childView.txtActionItem.setText(items.getItem());
            Log.d(TAG, "onBindViewHolder: holder.getAdapterPosition() : " + holder.getAdapterPosition() + " position : " + position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return m_ActionItems.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return m_ActionItems.size();
    }

    public void setOnItemClickListener(ActionItemClickListener actionItemClickListener) {
        this.actionItemClickListener = actionItemClickListener;
    }

    private boolean isPositionFooter(int position) {
        return position == totalCount;
    }

    public class ActionItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ActionItemViewHolder(View itemView) {
            super(itemView);
            Log.d("ActionItemViewHolder", "ActionItemViewHolder: " + itemView);
        }

        @Override
        public void onClick(View view) {
            actionItemClickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public class ParentItem extends ActionItemViewHolder {

        TextView txtCurrentDay;

        public ParentItem(View itemView) {
            super(itemView);
            txtCurrentDay = (TextView) itemView.findViewById(R.id.txtCurrentDay);
        }
    }

    public class ChildItem extends ActionItemViewHolder {
        EditText txtActionItem;
        View viewSeperator;
        ImageView imgMinus;
        MyCustomEditTextListener myCustomEditTextListener;

        public ChildItem(final View itemView, MyCustomEditTextListener myCustomEditTextListener) {
            super(itemView);
            this.myCustomEditTextListener = myCustomEditTextListener;
            txtActionItem = (EditText) itemView.findViewById(R.id.txtActionItem);
            viewSeperator = (View) itemView.findViewById(R.id.viewSeperator);
            imgMinus = (ImageView) itemView.findViewById(R.id.imgMinus);
            imgMinus.setOnClickListener(this);
            txtActionItem.addTextChangedListener(myCustomEditTextListener);
        }
    }

    public interface ActionItemClickListener {
        public void onItemClick(int position, View v);
    }

    private class MyCustomEditTextListener implements TextWatcher {
        private int position;
        private boolean isUpdated;

        public void updatePosition(int position) {
            this.position = position;
            isUpdated = true;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!isUpdated) {
                return;
            }
            InnerActionItems item = (InnerActionItems) m_ActionItems.get(position);
            item.setItem(charSequence.toString());
            Log.d(TAG, "onTextChanged: " + position + "String value : " + charSequence.toString());
            String itemID = item.getId();
            String projectID = item.getProjectID();
            String day = String.valueOf(item.getDay());
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBTableStructure.ActionItemsTable.KEY_ITEM, item.getItem());
            String whereClause = DBTableStructure.ActionItemsTable.KEY_PROJECT_ID + "=? AND " + DBTableStructure.ActionItemsTable.KEY_ITEM_ID + "=? AND " + DBTableStructure.ActionItemsTable.KEY_DAY + "=?";
            long updated = DBManager.getInstance(m_Context).update(contentValues, whereClause, new String[]{projectID, itemID, day});
            Log.d(TAG, "onTextChanged: " + item);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
}
