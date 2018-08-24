package org.icddrb.dhis.android.sdk.persistence.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import com.raizlabs.android.dbflow.structure.Model;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.icddrb.dhis.android.sdk.utils.Preconditions;

public class DbLoader<T> extends AsyncTaskLoader<T> {
    private T mData;
    private AtomicBoolean mIsLoading = new AtomicBoolean(false);
    private final List<Class<? extends Model>> mModelClasses;
    private List<ModelChangeObserver<?>> mObservers;
    private final Query<T> mQuery;

    public DbLoader(Context context, List<Class<? extends Model>> modelClasses, Query<T> query) {
        super(context);
        this.mModelClasses = (List) Preconditions.isNull(modelClasses, "Model Class object must not be null");
        this.mQuery = (Query) Preconditions.isNull(query, "Query object must not be null");
    }

    private void registerObservers() {
        this.mObservers = new ArrayList();
        for (Class<? extends Model> modelClass : this.mModelClasses) {
            ModelChangeObserver<?> observer = new ModelChangeObserver(modelClass, this);
            observer.registerObserver();
            this.mObservers.add(observer);
        }
    }

    private void unregisterObservers() {
        if (this.mObservers != null) {
            for (ModelChangeObserver<?> observer : this.mObservers) {
                observer.unregisterObserver();
            }
            this.mObservers = null;
        }
    }

    public T loadInBackground() {
        this.mIsLoading.set(true);
        T data = this.mQuery.query(getContext());
        this.mIsLoading.set(false);
        return data;
    }

    public void deliverResult(T data) {
        if (isReset()) {
            releaseResources();
            return;
        }
        T oldData = this.mData;
        this.mData = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
        if (oldData != null && oldData != data) {
            releaseResources();
        }
    }

    protected void onStartLoading() {
        if (this.mData != null) {
            deliverResult(this.mData);
        }
        registerObservers();
        if (takeContentChanged() || this.mData == null) {
            forceLoad();
        }
    }

    public void onStopLoading() {
        cancelLoad();
    }

    protected void onReset() {
        onStopLoading();
        releaseResources();
        unregisterObservers();
    }

    public void onCanceled(T data) {
        super.onCanceled(data);
        releaseResources();
    }

    public boolean isLoading() {
        return this.mIsLoading.get();
    }

    private void releaseResources() {
        this.mData = null;
    }
}
