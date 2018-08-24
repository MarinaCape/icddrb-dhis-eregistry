package org.icddrb.dhis.client.sdk.ui.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.client.sdk.ui.C0935R;
import org.icddrb.dhis.client.sdk.ui.models.ReportEntity;
import org.icddrb.dhis.client.sdk.ui.views.CircleView;
import org.icddrb.dhis.client.sdk.utils.Preconditions;

public abstract class EndlessReportEntityAdapter extends Adapter {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final EndlessScrollListener endlessScrollListener;
    private final LayoutInflater layoutInflater;
    private boolean loading;
    private OnReportEntityInteractionListener onReportEntityInteractionListener;
    private final RecyclerView recyclerView;
    private final List<ReportEntity> reportEntities;

    private class EndlessScrollListener extends OnScrollListener {
        public static final int BOTTOM_ITEM_TRESHOLD = 3;

        private EndlessScrollListener() {
        }

        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (!EndlessReportEntityAdapter.this.loading && bottomTresholdReached(recyclerView)) {
                EndlessReportEntityAdapter.this.loading = true;
                EndlessReportEntityAdapter.this.showLoadingView();
                EndlessReportEntityAdapter.this.onLoadData();
            }
        }

        private boolean bottomTresholdReached(RecyclerView recyclerView) {
            return recyclerView.getLayoutManager().getItemCount() - recyclerView.getChildCount() < ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition() + 3;
        }
    }

    private final class LoadingViewHolder extends ViewHolder {
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class OnRecyclerViewItemClickListener implements OnClickListener {
        private ReportEntity reportEntity;

        private OnRecyclerViewItemClickListener() {
        }

        public void setReportEntity(ReportEntity reportEntity) {
            this.reportEntity = reportEntity;
        }

        public void onClick(View view) {
            if (EndlessReportEntityAdapter.this.onReportEntityInteractionListener != null) {
                EndlessReportEntityAdapter.this.onReportEntityInteractionListener.onReportEntityClicked(this.reportEntity);
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
        final View deleteButton;
        final Drawable drawableError;
        final Drawable drawableOffline;
        final Drawable drawableSent;
        final OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
        ReportEntity reportEntity;
        final CircleView statusBackground;
        final ImageView statusIcon;
        final View statusIconContainer;

        /* renamed from: org.icddrb.dhis.client.sdk.ui.adapters.EndlessReportEntityAdapter$ReportEntityViewHolder$1 */
        class C09401 implements DialogInterface.OnClickListener {
            C09401() {
            }

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }

        /* renamed from: org.icddrb.dhis.client.sdk.ui.adapters.EndlessReportEntityAdapter$ReportEntityViewHolder$2 */
        class C09412 implements DialogInterface.OnClickListener {
            C09412() {
            }

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (EndlessReportEntityAdapter.this.onReportEntityInteractionListener != null) {
                    int entityIndex = EndlessReportEntityAdapter.this.reportEntities.indexOf(ReportEntityViewHolder.this.reportEntity);
                    EndlessReportEntityAdapter.this.reportEntities.remove(ReportEntityViewHolder.this.reportEntity);
                    EndlessReportEntityAdapter.this.notifyItemRemoved(entityIndex);
                    EndlessReportEntityAdapter.this.onReportEntityInteractionListener.onDeleteReportEntity(ReportEntityViewHolder.this.reportEntity);
                    return;
                }
                Toast.makeText(ReportEntityViewHolder.this.deleteButton.getContext(), C0935R.string.report_entity_deletion_error, 0).show();
            }
        }

        /* renamed from: org.icddrb.dhis.client.sdk.ui.adapters.EndlessReportEntityAdapter$ReportEntityViewHolder$3 */
        class C09423 implements DialogInterface.OnClickListener {
            C09423() {
            }

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }

        /* renamed from: org.icddrb.dhis.client.sdk.ui.adapters.EndlessReportEntityAdapter$ReportEntityViewHolder$4 */
        class C09434 implements DialogInterface.OnClickListener {
            C09434() {
            }

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }

        private class OnDeleteButtonClickListener implements OnClickListener {
            private OnDeleteButtonClickListener() {
            }

            public void onClick(View v) {
                if (EndlessReportEntityAdapter.this.onReportEntityInteractionListener != null) {
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
            this.deleteButton = itemView.findViewById(C0935R.id.delete_button);
            this.onRecyclerViewItemClickListener = new OnRecyclerViewItemClickListener();
            itemView.setOnClickListener(this.onRecyclerViewItemClickListener);
            this.deleteButton.setOnClickListener(new OnDeleteButtonClickListener());
            Context context = itemView.getContext();
            this.drawableSent = ContextCompat.getDrawable(context, C0935R.drawable.ic_tick);
            this.drawableOffline = ContextCompat.getDrawable(context, C0935R.drawable.ic_offline);
            this.drawableError = ContextCompat.getDrawable(context, C0935R.drawable.ic_error);
            this.colorSent = ContextCompat.getColor(context, C0935R.color.color_material_green_default);
            this.colorOffline = ContextCompat.getColor(context, C0935R.color.color_accent_default);
            this.colorError = ContextCompat.getColor(context, C0935R.color.color_material_red_default);
        }

        private void showEntityDeletionConfirmationDialog() {
            new Builder(this.deleteButton.getContext()).setTitle(C0935R.string.delete_report_entity_dialog_title).setMessage(C0935R.string.delete_report_entity_dialog_message).setPositiveButton(C0935R.string.delete, new C09412()).setNegativeButton(17039360, new C09401()).create().show();
        }

        public void update(ReportEntity reportEntity) {
            this.reportEntity = reportEntity;
            this.onRecyclerViewItemClickListener.setReportEntity(reportEntity);
            switch (reportEntity.getStatus()) {
                case SENT:
                    this.deleteButton.setVisibility(8);
                    this.statusBackground.setFillColor(this.colorSent);
                    this.statusIcon.setImageDrawable(this.drawableSent);
                    return;
                case TO_POST:
                    this.deleteButton.setVisibility(0);
                    this.statusBackground.setFillColor(this.colorOffline);
                    this.statusIcon.setImageDrawable(this.drawableOffline);
                    return;
                case TO_UPDATE:
                    this.deleteButton.setVisibility(8);
                    this.statusBackground.setFillColor(this.colorOffline);
                    this.statusIcon.setImageDrawable(this.drawableOffline);
                    return;
                case ERROR:
                    this.deleteButton.setVisibility(8);
                    this.statusBackground.setFillColor(this.colorError);
                    this.statusIcon.setImageDrawable(this.drawableError);
                    return;
                default:
                    return;
            }
        }

        private void showStatusDialog(Context context) {
            Builder builder = new Builder(context);
            builder.setTitle(C0935R.string.drawer_item_status);
            switch (this.reportEntity.getStatus()) {
                case SENT:
                    Drawable mutableSentIcon = ContextCompat.getDrawable(context, C0935R.drawable.ic_tick).mutate();
                    mutableSentIcon.setColorFilter(this.colorSent, Mode.MULTIPLY);
                    builder.setIcon(mutableSentIcon);
                    builder.setMessage(C0935R.string.sync_status_ok_message);
                    break;
                case TO_POST:
                case TO_UPDATE:
                    Drawable mutableOfflineIcon = ContextCompat.getDrawable(context, C0935R.drawable.ic_offline).mutate();
                    mutableOfflineIcon.setColorFilter(this.colorOffline, Mode.MULTIPLY);
                    builder.setIcon(mutableOfflineIcon);
                    builder.setMessage(C0935R.string.sync_status_offline_message);
                    break;
                case ERROR:
                    Drawable mutableDrawableError = ContextCompat.getDrawable(context, C0935R.drawable.ic_error).mutate();
                    mutableDrawableError.setColorFilter(this.colorError, Mode.MULTIPLY);
                    builder.setIcon(mutableDrawableError);
                    builder.setMessage(C0935R.string.sync_status_error_message);
                    break;
            }
            builder.setPositiveButton(C0935R.string.sync_now, new C09434()).setNegativeButton(17039360, new C09423());
            builder.create().show();
        }
    }

    public abstract void onLoadData();

    public EndlessReportEntityAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        Preconditions.isNull(recyclerView, "RecyclerView must not be null");
        Context context = recyclerView.getContext();
        Preconditions.isNull(context, "RecyclerView must have valid Context");
        this.layoutInflater = LayoutInflater.from(context);
        this.reportEntities = new ArrayList();
        this.endlessScrollListener = new EndlessScrollListener();
        recyclerView.addOnScrollListener(this.endlessScrollListener);
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ReportEntityViewHolder(this.layoutInflater.inflate(C0935R.layout.recyclerview_report_entity_item, parent, false));
        }
        if (viewType == 1) {
            return new LoadingViewHolder(this.layoutInflater.inflate(C0935R.layout.loading_view, parent, false));
        }
        return null;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof ReportEntityViewHolder) {
            ((ReportEntityViewHolder) holder).update((ReportEntity) this.reportEntities.get(position));
        }
    }

    public int getItemCount() {
        return this.reportEntities.size();
    }

    public int getItemViewType(int position) {
        return isLoadingView(position) ? 1 : 0;
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

    public void addItem(ReportEntity reportEntity) {
        this.reportEntities.add(reportEntity);
        notifyItemInserted(this.reportEntities.size() - 1);
    }

    private void showLoadingView() {
        if (!loadingViewIsShowing()) {
            this.reportEntities.add(null);
            notifyItemInserted(this.reportEntities.size() - 1);
        }
    }

    public void addLoadedItems(List<ReportEntity> newItems) {
        hideLoadingView();
        this.reportEntities.addAll(newItems);
        notifyDataSetChanged();
        this.loading = false;
    }

    private void hideLoadingView() {
        if (!this.reportEntities.isEmpty() && loadingViewIsShowing()) {
            this.reportEntities.remove(this.reportEntities.size() - 1);
            notifyItemRemoved(this.reportEntities.size());
        }
    }

    private boolean loadingViewIsShowing() {
        return isLoadingView(this.reportEntities.size() - 1);
    }

    private boolean isLoadingView(int position) {
        return this.reportEntities.get(position) == null;
    }

    public void onLoadFinished() {
        this.recyclerView.removeOnScrollListener(this.endlessScrollListener);
        hideLoadingView();
    }
}
