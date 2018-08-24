package org.icddrb.dhis.client.sdk.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.client.sdk.ui.C0935R;
import org.icddrb.dhis.client.sdk.ui.fragments.FilterableDialogFragment;
import org.icddrb.dhis.client.sdk.ui.models.Picker;

public class PickerAdapter extends Adapter {
    private static final String PICKER_ADAPTER_STATE = "state:pickerAdapter";
    private final FragmentManager fragmentManager;
    private final LayoutInflater layoutInflater;
    private OnPickerListChangeListener onPickerListChangeListener;
    private Picker pickerTree;
    private final List<Picker> pickers = new ArrayList();

    private class OnClickListener implements android.view.View.OnClickListener {
        private final Picker picker;

        private OnClickListener(Picker picker) {
            this.picker = picker;
        }

        public void onClick(View view) {
            if (view.getId() == C0935R.id.textview_picker) {
                attachFragment();
            } else if (view.getId() == C0935R.id.imageview_cancel) {
                clearSelection();
            }
        }

        private void attachFragment() {
            OnPickerItemClickListener listener = new OnItemClickedListener();
            FilterableDialogFragment dialogFragment = FilterableDialogFragment.newInstance(this.picker);
            dialogFragment.setOnPickerItemClickListener(listener);
            dialogFragment.show(PickerAdapter.this.fragmentManager, FilterableDialogFragment.TAG);
        }

        private void clearSelection() {
            this.picker.setSelectedChild(null);
            PickerAdapter.this.swapData(this.picker);
        }
    }

    private class OnItemClickedListener implements OnPickerItemClickListener {
        private OnItemClickedListener() {
        }

        public void onPickerItemClickListener(Picker selectedPicker) {
            if (selectedPicker.getParent() != null) {
                selectedPicker.getParent().setSelectedChild(selectedPicker);
            }
            PickerAdapter.this.swapData(selectedPicker);
        }
    }

    public interface OnPickerListChangeListener {
        void onPickerListChanged(List<Picker> list);
    }

    private class PickerViewHolder extends ViewHolder {
        private final ImageView cancel;
        private final TextView pickerLabel;

        public PickerViewHolder(View itemView) {
            super(itemView);
            this.pickerLabel = (TextView) itemView.findViewById(C0935R.id.textview_picker);
            this.cancel = (ImageView) itemView.findViewById(C0935R.id.imageview_cancel);
        }

        public void update(Picker picker) {
            if (picker.getSelectedChild() != null) {
                this.pickerLabel.setText(picker.getSelectedChild().getName());
            } else {
                this.pickerLabel.setText(picker.getHint());
            }
            OnClickListener listener = new OnClickListener(picker);
            this.pickerLabel.setOnClickListener(listener);
            this.cancel.setOnClickListener(listener);
            attachListenerToExistingFragment(picker);
        }

        private void attachListenerToExistingFragment(Picker picker) {
            FilterableDialogFragment fragment = (FilterableDialogFragment) PickerAdapter.this.fragmentManager.findFragmentByTag(FilterableDialogFragment.TAG);
            if (fragment != null) {
                Bundle arguments = fragment.getArguments();
                if (arguments != null && arguments.containsKey(FilterableDialogFragment.ARGS_PICKER) && picker.equals((Picker) arguments.getSerializable(FilterableDialogFragment.ARGS_PICKER))) {
                    fragment.setOnPickerItemClickListener(new OnItemClickedListener());
                }
            }
        }
    }

    public PickerAdapter(FragmentManager fragmentManager, Context context) {
        this.fragmentManager = fragmentManager;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PickerViewHolder(this.layoutInflater.inflate(C0935R.layout.recyclerview_row_picker, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        ((PickerViewHolder) holder).update((Picker) this.pickers.get(position));
    }

    public int getItemCount() {
        int itemCount = this.pickers.size();
        if (itemCount <= 0 || !((Picker) this.pickers.get(itemCount - 1)).getChildren().isEmpty()) {
            return itemCount;
        }
        return itemCount - 1;
    }

    public void onSaveInstanceState(Bundle outState) {
        if (outState != null && this.pickerTree != null) {
            outState.putSerializable(PICKER_ADAPTER_STATE, this.pickerTree);
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            swapData((Picker) savedInstanceState.getSerializable(PICKER_ADAPTER_STATE));
        }
    }

    public void setOnPickerListChangeListener(OnPickerListChangeListener listener) {
        this.onPickerListChangeListener = listener;
    }

    public void swapData(Picker pickerTree) {
        this.pickerTree = pickerTree;
        this.pickers.clear();
        if (pickerTree != null) {
            Picker node = getRootNode(pickerTree);
            do {
                if (!node.isLeaf()) {
                    if (node.areChildrenRoots()) {
                        for (Picker childNode : node.getChildren()) {
                            this.pickers.add(childNode);
                        }
                    } else {
                        this.pickers.add(node);
                    }
                }
                node = node.getSelectedChild();
            } while (node != null);
        }
        if (this.onPickerListChangeListener != null) {
            this.onPickerListChangeListener.onPickerListChanged(new ArrayList(this.pickers));
        }
        notifyDataSetChanged();
    }

    public List<Picker> getData() {
        return new ArrayList(this.pickers);
    }

    private Picker getRootNode(Picker picker) {
        Picker node = picker;
        while (node.getParent() != null) {
            node = node.getParent();
        }
        return node;
    }
}
