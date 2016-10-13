package com.example.kiwi.tpprogresstracker.adapter;

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
import com.example.kiwi.tpprogresstracker.model.ActionItems;
import com.example.kiwi.tpprogresstracker.model.InnerActionItems;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kiwi on 10/10/2016.
 */

public class ActionItemAdapter extends RecyclerView.Adapter<ActionItemAdapter.ActionItemViewHolder> {

    Context m_Context;
    ArrayList<ActionItems> m_ActionItems;
    ArrayList<Integer> m_ParentItems = new ArrayList<>();
    HashMap<Integer, Integer> childMap = new HashMap<>();
    public static final int PARENT_ITEM = 0;
    public static final int CHILD_ITEM = 1;
    public static final int FOOTER_ITEM = 2;
    int totalCount = 0;

    public ActionItemAdapter(Context context, ArrayList<ActionItems> actionItemInfoList) {
        super();
        this.m_Context = context;
        this.m_ActionItems = actionItemInfoList;
    }

    @Override
    public ActionItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == PARENT_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_do_list_parent, parent, false);
            ParentItem parentItem = new ParentItem(view);
            return parentItem;
        } else if (viewType == FOOTER_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_do_footer, parent, false);
            FooterItem footerItem = new FooterItem(view);
            return footerItem;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_do_list_child, parent, false);
            ChildItem childItem = new ChildItem(view, new ChildItem.ITextWatcher() {
                @Override
                public void beforeTextChanged(int position, CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(int position, CharSequence s, int start, int before, int count) {
                    // m_ActionItems[position] = s.toString();
                }

                @Override
                public void afterTextChanged(int position, Editable s) {

                }
            });

            return childItem;
        }
    }

    @Override
    public void onBindViewHolder(ActionItemViewHolder holder, int position) {
        if (holder.getItemViewType() == PARENT_ITEM) {
            int index = m_ParentItems.indexOf(position);
            int childSize = m_ActionItems.get(index).getItems().size();
            for (int i = 1; i <= childSize; i++) {
                childMap.put(position + i, position);
            }
            ParentItem v = (ParentItem) holder;
            v.txtCurrentDay.setText("day " + String.valueOf(m_ActionItems.get(index).getDay()));
        } else if (holder.getItemViewType() == CHILD_ITEM) {
            final ChildItem childView = (ChildItem) holder;
            int value = childMap.get(position);
            final InnerActionItems item = m_ActionItems.get(m_ParentItems.indexOf(value)).getItems().get(position - value - 1);
            childView.txtActionItem.setText(item.getItem());
            childView.txtActionItem.setTag(item);
//            childView.txtActionItem.setEnabled(false);
//            childView.viewSeperator.setVisibility(View.GONE);
//            childView.imgMinus.setVisibility(View.GONE);
        } else {
            FooterItem footerItem = (FooterItem) holder;
            footerItem.imgPlus.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (m_ParentItems.contains(position)) {
            return PARENT_ITEM;
        } else if (isPositionFooter(position)) {
            return FOOTER_ITEM;
        } else {
            return CHILD_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        totalCount = 0;
        m_ParentItems.clear();
        for (ActionItems items : m_ActionItems) {
            m_ParentItems.add(totalCount);
            totalCount = totalCount + 1;
            totalCount = totalCount + items.getItems().size();
        }
        return totalCount + 1;
    }

    private boolean isPositionFooter(int position) {
        return position == totalCount;
    }

    public static class ActionItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //TextView txtCurrentDay;

        public ActionItemViewHolder(View itemView) {
            super(itemView);
            //txtCurrentDay = (TextView) itemView.findViewById(txtCurrentDay);
            Log.d("ActionItemViewHolder", "ActionItemViewHolder: " + itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public class ParentItem extends ActionItemViewHolder {

        TextView txtCurrentDay;

        public ParentItem(View itemView) {
            super(itemView);
            txtCurrentDay = (TextView) itemView.findViewById(R.id.txtCurrentDay);
        }
    }

    public static class ChildItem extends ActionItemViewHolder {
        EditText txtActionItem;
        View viewSeperator;
        ImageView imgMinus;
        private ITextWatcher mTextWatcher;

        public interface ITextWatcher {
            // you can add/remove methods as you please, maybe you dont need this much
            void beforeTextChanged(int position, CharSequence s, int start, int count, int after);

            void onTextChanged(int position, CharSequence s, int start, int before, int count);

            void afterTextChanged(int position, Editable s);
        }

        public ChildItem(View itemView, ITextWatcher textWatcher) {
            super(itemView);
            txtActionItem = (EditText) itemView.findViewById(R.id.txtActionItem);
            viewSeperator = (View) itemView.findViewById(R.id.viewSeperator);
            imgMinus = (ImageView) itemView.findViewById(R.id.imgMinus);
            txtActionItem.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    mTextWatcher.onTextChanged(getAdapterPosition(), charSequence, i, i1, i2);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }

    public class FooterItem extends ActionItemViewHolder {

        ImageView imgPlus;

        public FooterItem(View itemView) {
            super(itemView);
            imgPlus = (ImageView) itemView.findViewById(R.id.imgPlus);
        }
    }

}
