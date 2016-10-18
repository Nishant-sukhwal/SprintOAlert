package com.example.kiwi.tpprogresstracker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kiwi.tpprogresstracker.R;
import com.example.kiwi.tpprogresstracker.model.HeaderItem;
import com.example.kiwi.tpprogresstracker.model.InnerActionItems;
import com.example.kiwi.tpprogresstracker.model.ListItem;

import java.util.ArrayList;

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
        if (viewType == ListItem.TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_do_list_parent, parent, false);
            ParentItem parentItem = new ParentItem(view);
            return parentItem;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_do_list_child, parent, false);
            ChildItem childItem = new ChildItem(view);
            return childItem;
        }
    }

    @Override
    public void onBindViewHolder(ActionItemViewHolder holder, int position) {
        if (holder.getItemViewType() == ListItem.TYPE_HEADER) {
            ParentItem v = (ParentItem) holder;
            HeaderItem items = (HeaderItem) m_ActionItems.get(position);
            v.txtCurrentDay.setText("day " + String.valueOf(items.getDay()));
        } else if (holder.getItemViewType() == ListItem.TYPE_CHILD_ITEM) {
            ChildItem childView = (ChildItem) holder;
            final InnerActionItems items = (InnerActionItems) m_ActionItems.get(position);
            childView.txtActionItem.setText(items.getItem());
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
            childView.txtActionItem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b) {
                        final int position = (Integer) view.getTag();
                        final EditText Caption = (EditText) view;
                        items.setItem(Caption.getText().toString());
                        Log.d("After text changed", "afterTextChanged: " + position);
                    }
                }
            });
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

        public ChildItem(final View itemView) {
            super(itemView);
            txtActionItem = (EditText) itemView.findViewById(R.id.txtActionItem);
            viewSeperator = (View) itemView.findViewById(R.id.viewSeperator);
            imgMinus = (ImageView) itemView.findViewById(R.id.imgMinus);
            imgMinus.setOnClickListener(this);
        }
    }

    public interface ActionItemClickListener {
        public void onItemClick(int position, View v);
    }
}
