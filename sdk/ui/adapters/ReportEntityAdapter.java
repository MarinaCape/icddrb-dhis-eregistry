package org.icddrb.dhis.client.sdk.ui.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView.BufferType;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.icddrb.dhis.client.sdk.ui.C0935R;
import org.icddrb.dhis.client.sdk.ui.models.ReportEntity;
import org.icddrb.dhis.client.sdk.ui.models.ReportEntityFilter;
import org.icddrb.dhis.client.sdk.ui.views.CircleView;
import org.icddrb.dhis.client.sdk.ui.views.FontTextView;
import org.icddrb.dhis.client.sdk.utils.Preconditions;

public class ReportEntityAdapter extends Adapter {
    public static final String REPORT_ENTITY_LIST_KEY = "REPORT_ENTITY_LIST_KEY";
    private ArrayList<ReportEntityFilter> ReportEntityFilters;
    private final LayoutInflater layoutInflater;
    private OnReportEntityInteractionListener onReportEntityInteractionListener;
    private ArrayList<ReportEntity> reportEntities = new ArrayList();

    private class OnRecyclerViewItemClickListener implements OnClickListener {
        private ReportEntity reportEntity;

        private OnRecyclerViewItemClickListener() {
        }

        public void setReportEntity(ReportEntity reportEntity) {
            this.reportEntity = reportEntity;
        }

        public void onClick(View view) {
            if (ReportEntityAdapter.this.onReportEntityInteractionListener != null) {
                ReportEntityAdapter.this.onReportEntityInteractionListener.onReportEntityClicked(this.reportEntity);
            }
        }
    }

    public interface OnReportEntityInteractionListener {
        void onDeleteReportEntity(ReportEntity reportEntity);

        void onReportEntityClicked(ReportEntity reportEntity);
    }

    private final class ReportEntityViewHolder extends ViewHolder {
        final int colorError;
        final int colorOffline;
        final int colorSent;
        private final ViewGroup dataElementLabelContainer;
        final View deleteButton;
        final Drawable drawableError;
        final Drawable drawableOffline;
        final Drawable drawableSent;
        final OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
        ReportEntity reportEntity;
        final CircleView statusBackground;
        final ImageView statusIcon;
        final View statusIconContainer;

        /* renamed from: org.icddrb.dhis.client.sdk.ui.adapters.ReportEntityAdapter$ReportEntityViewHolder$2 */
        class C09482 implements DialogInterface.OnClickListener {
            C09482() {
            }

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }

        /* renamed from: org.icddrb.dhis.client.sdk.ui.adapters.ReportEntityAdapter$ReportEntityViewHolder$3 */
        class C09493 implements DialogInterface.OnClickListener {
            C09493() {
            }

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }

        /* renamed from: org.icddrb.dhis.client.sdk.ui.adapters.ReportEntityAdapter$ReportEntityViewHolder$4 */
        class C09504 implements DialogInterface.OnClickListener {
            C09504() {
            }

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (ReportEntityAdapter.this.onReportEntityInteractionListener != null) {
                    int entityIndex = ReportEntityAdapter.this.reportEntities.indexOf(ReportEntityViewHolder.this.reportEntity);
                    ReportEntityAdapter.this.reportEntities.remove(ReportEntityViewHolder.this.reportEntity);
                    ReportEntityAdapter.this.notifyItemRemoved(entityIndex);
                    ReportEntityAdapter.this.onReportEntityInteractionListener.onDeleteReportEntity(ReportEntityViewHolder.this.reportEntity);
                    return;
                }
                Toast.makeText(ReportEntityViewHolder.this.deleteButton.getContext(), C0935R.string.report_entity_deletion_error, 0).show();
            }
        }

        private class OnDeleteButtonClickListener implements OnClickListener {
            private OnDeleteButtonClickListener() {
            }

            public void onClick(View v) {
                if (ReportEntityAdapter.this.onReportEntityInteractionListener != null) {
                    ReportEntityViewHolder.this.showEntityDeletionConfirmationDialog();
                } else {
                    Toast.makeText(v.getContext(), C0935R.string.report_entity_deletion_error, 0).show();
                }
            }
        }

        public ReportEntityViewHolder(View itemView) {
            super(itemView);
            this.statusIconContainer = itemView.findViewById(C0935R.id.status_icon_container);
            this.statusBackground = (CircleView) itemView.findViewById(C0935R.id.circleview_status_background);
            this.statusIcon = (ImageView) itemView.findViewById(C0935R.id.imageview_status_icon);
            this.dataElementLabelContainer = (ViewGroup) itemView.findViewById(C0935R.id.data_element_label_container);
            this.deleteButton = itemView.findViewById(C0935R.id.delete_button);
            this.onRecyclerViewItemClickListener = new OnRecyclerViewItemClickListener();
            itemView.setOnClickListener(this.onRecyclerViewItemClickListener);
            this.deleteButton.setOnClickListener(new OnDeleteButtonClickListener());
            this.statusIconContainer.setOnClickListener(new OnClickListener(ReportEntityAdapter.this) {
                public void onClick(View v) {
                    ReportEntityViewHolder.this.showStatusDialog(v.getContext());
                }
            });
            Context context = itemView.getContext();
            this.drawableSent = ContextCompat.getDrawable(context, C0935R.drawable.ic_tick);
            this.drawableOffline = ContextCompat.getDrawable(context, C0935R.drawable.ic_offline);
            this.drawableError = ContextCompat.getDrawable(context, C0935R.drawable.ic_error);
            this.colorSent = ContextCompat.getColor(context, C0935R.color.color_material_green_default);
            this.colorOffline = ContextCompat.getColor(context, C0935R.color.color_accent_default);
            this.colorError = ContextCompat.getColor(context, C0935R.color.color_material_red_default);
        }

        public void update(ReportEntity reportEntity) {
            this.reportEntity = reportEntity;
            this.onRecyclerViewItemClickListener.setReportEntity(reportEntity);
            switch (reportEntity.getStatus()) {
                case SENT:
                    this.deleteButton.setVisibility(4);
                    this.statusBackground.setFillColor(this.colorSent);
                    this.statusIcon.setImageDrawable(this.drawableSent);
                    break;
                case TO_POST:
                    this.deleteButton.setVisibility(0);
                    this.statusBackground.setFillColor(this.colorOffline);
                    this.statusIcon.setImageDrawable(this.drawableOffline);
                    break;
                case TO_UPDATE:
                    this.deleteButton.setVisibility(4);
                    this.statusBackground.setFillColor(this.colorOffline);
                    this.statusIcon.setImageDrawable(this.drawableOffline);
                    break;
                case ERROR:
                    this.deleteButton.setVisibility(4);
                    this.statusBackground.setFillColor(this.colorError);
                    this.statusIcon.setImageDrawable(this.drawableError);
                    break;
            }
            updateDataElements(reportEntity);
        }

        private void updateDataElements(ReportEntity reportEntity) {
            if (ReportEntityAdapter.this.ReportEntityFilters == null) {
                showPlaceholder();
            } else if (ReportEntityAdapter.this.noDataElementsToShow(ReportEntityAdapter.this.ReportEntityFilters)) {
                showDataElementsByDisplayInReports(reportEntity);
            } else {
                int i = 0;
                while (i < ReportEntityAdapter.this.ReportEntityFilters.size()) {
                    ReportEntityFilter filter = (ReportEntityFilter) ReportEntityAdapter.this.ReportEntityFilters.get(i);
                    View dataElementLabelView = getDataElementLabelContainerChild(i);
                    if (filter.show()) {
                        String dataElementString = String.format("%s: %s", new Object[]{filter.getDataElementLabel(), reportEntity.getValueForDataElement(filter.getDataElementId())});
                        SpannableString text = new SpannableString(dataElementString);
                        text.setSpan(new StyleSpan(1), dataElementString.length() - value.length(), dataElementString.length(), 33);
                        ((FontTextView) dataElementLabelView).setText(text, BufferType.SPANNABLE);
                        dataElementLabelView.setVisibility(0);
                    } else {
                        dataElementLabelView.setVisibility(8);
                    }
                    i++;
                }
                trimElementLabelViews(i);
            }
        }

        private void showDataElementsByDisplayInReports(ReportEntity reportEntity) {
            View dataElementLabelView = getDataElementLabelContainerChild(0);
            int filterShowCount = 0;
            for (int i = 0; i < ReportEntityAdapter.this.ReportEntityFilters.size(); i++) {
                ReportEntityFilter filter = (ReportEntityFilter) ReportEntityAdapter.this.ReportEntityFilters.get(i);
                if (filter.show()) {
                    filterShowCount++;
                    String dataElementString = String.format("%s: %s", new Object[]{filter.getDataElementLabel(), reportEntity.getValueForDataElement(filter.getDataElementId())});
                    SpannableString text = new SpannableString(dataElementString);
                    text.setSpan(new StyleSpan(1), dataElementString.length() - value.length(), dataElementString.length(), 33);
                    ((FontTextView) dataElementLabelView).setText(text, BufferType.SPANNABLE);
                    dataElementLabelView.setVisibility(0);
                }
            }
            if (filterShowCount > 0) {
                trimElementLabelViews(filterShowCount);
            } else {
                showThreeFirstDataElements(reportEntity);
            }
        }

        private void showThreeFirstDataElements(ReportEntity reportEntity) {
            int viewIndex = 0;
            int i = 0;
            while (i < ReportEntityAdapter.this.ReportEntityFilters.size() && i < 3) {
                int viewIndex2 = viewIndex + 1;
                View dataElementLabelView = getDataElementLabelContainerChild(viewIndex);
                String dataElementString = String.format("%s: %s", new Object[]{filter.getDataElementLabel(), reportEntity.getValueForDataElement(((ReportEntityFilter) ReportEntityAdapter.this.ReportEntityFilters.get(i)).getDataElementId())});
                SpannableString text = new SpannableString(dataElementString);
                text.setSpan(new StyleSpan(1), dataElementString.length() - value.length(), dataElementString.length(), 33);
                ((FontTextView) dataElementLabelView).setText(text, BufferType.SPANNABLE);
                dataElementLabelView.setVisibility(0);
                i++;
                viewIndex = viewIndex2;
            }
            trimElementLabelViews(viewIndex);
        }

        private void showPlaceholder() {
            View dataElementLabelView = getDataElementLabelContainerChild(0);
            trimElementLabelViews(1);
            ((FontTextView) dataElementLabelView).setText(this.dataElementLabelContainer.getContext().getString(C0935R.string.report_entity));
            trimElementLabelViews(1);
        }

        private View getDataElementLabelContainerChild(int childIndex) {
            View dataElementLabelView = this.dataElementLabelContainer.getChildAt(childIndex);
            if (dataElementLabelView != null) {
                return dataElementLabelView;
            }
            dataElementLabelView = ReportEntityAdapter.this.layoutInflater.inflate(C0935R.layout.data_element_label, this.dataElementLabelContainer, false);
            this.dataElementLabelContainer.addView(dataElementLabelView);
            return dataElementLabelView;
        }

        private void trimElementLabelViews(int trimSize) {
            while (this.dataElementLabelContainer.getChildCount() > trimSize) {
                this.dataElementLabelContainer.removeViewAt(this.dataElementLabelContainer.getChildCount() - 1);
            }
        }

        private void showStatusDialog(Context context) {
            Builder builder = new Builder(context);
            switch (this.reportEntity.getStatus()) {
                case SENT:
                    builder.setTitle(C0935R.string.sync_status_ok_title);
                    Drawable mutableSentIcon = ContextCompat.getDrawable(context, C0935R.drawable.ic_tick).mutate();
                    mutableSentIcon.setColorFilter(this.colorSent, Mode.MULTIPLY);
                    builder.setIcon(mutableSentIcon);
                    builder.setMessage(C0935R.string.sync_status_ok_message);
                    break;
                case TO_POST:
                case TO_UPDATE:
                    builder.setTitle(C0935R.string.sync_status_offline_title);
                    Drawable mutableOfflineIcon = ContextCompat.getDrawable(context, C0935R.drawable.ic_offline).mutate();
                    mutableOfflineIcon.setColorFilter(this.colorOffline, Mode.MULTIPLY);
                    builder.setIcon(mutableOfflineIcon);
                    builder.setMessage(C0935R.string.sync_status_offline_message);
                    break;
                case ERROR:
                    builder.setTitle(C0935R.string.sync_status_error_title);
                    Drawable mutableDrawableError = ContextCompat.getDrawable(context, C0935R.drawable.ic_error).mutate();
                    mutableDrawableError.setColorFilter(this.colorError, Mode.MULTIPLY);
                    builder.setIcon(mutableDrawableError);
                    builder.setMessage(C0935R.string.sync_status_error_message);
                    break;
            }
            builder.setPositiveButton(17039370, new C09482());
            builder.create().show();
        }

        private void showEntityDeletionConfirmationDialog() {
            new Builder(this.deleteButton.getContext()).setTitle(C0935R.string.delete_report_entity_dialog_title).setMessage(C0935R.string.delete_report_entity_dialog_message).setPositiveButton(C0935R.string.delete, new C09504()).setNegativeButton(17039360, new C09493()).create().show();
        }
    }

    public ReportEntityAdapter(Context context) {
        Preconditions.isNull(context, "context must not be null");
        this.layoutInflater = LayoutInflater.from(context);
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReportEntityViewHolder(this.layoutInflater.inflate(C0935R.layout.recyclerview_report_entity_item, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        ((ReportEntityViewHolder) holder).update((ReportEntity) this.reportEntities.get(position));
    }

    public int getItemCount() {
        return this.reportEntities.size();
    }

    public void setOnReportEntityInteractionListener(OnReportEntityInteractionListener onInteractionListener) {
        this.onReportEntityInteractionListener = onInteractionListener;
    }

    public void swapData(@Nullable List<ReportEntity> reportEntities) {
        this.reportEntities.clear();
        if (reportEntities != null) {
            this.reportEntities.addAll(reportEntities);
        }
        notifyDataSetChanged();
    }

    public void onRestoreInstanceState(Bundle bundle) {
        this.reportEntities = bundle.getParcelableArrayList(REPORT_ENTITY_LIST_KEY);
        notifyDataSetChanged();
    }

    public void notifyFiltersChanged(ArrayList<ReportEntityFilter> filters) {
        this.ReportEntityFilters = filters;
        notifyDataSetChanged();
    }

    private boolean noDataElementsToShow(ArrayList<ReportEntityFilter> ReportEntityFilters) {
        Iterator it = ReportEntityFilters.iterator();
        while (it.hasNext()) {
            if (((ReportEntityFilter) it.next()).show()) {
                return false;
            }
        }
        return true;
    }

    public void addItem(ReportEntity reportEntity) {
        this.reportEntities.add(reportEntity);
        notifyItemInserted(this.reportEntities.size() - 1);
    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(REPORT_ENTITY_LIST_KEY, this.reportEntities);
        return bundle;
    }

    public ArrayList<ReportEntityFilter> getReportEntityFilters() {
        return this.ReportEntityFilters;
    }
}
