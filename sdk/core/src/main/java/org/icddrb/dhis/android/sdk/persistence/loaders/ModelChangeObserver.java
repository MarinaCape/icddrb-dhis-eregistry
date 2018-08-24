package org.icddrb.dhis.android.sdk.persistence.loaders;

import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.runtime.FlowContentObserver.OnModelStateChangedListener;
import com.raizlabs.android.dbflow.structure.BaseModel.Action;
import com.raizlabs.android.dbflow.structure.Model;
import org.icddrb.dhis.android.sdk.utils.Preconditions;

public class ModelChangeObserver<ModelClass extends Model> implements OnModelStateChangedListener {
    private static final String TAG = ModelChangeObserver.class.getSimpleName();
    private final DbLoader<?> mLoader;
    private final Class<ModelClass> mModelClass;
    private final FlowContentObserver mObserver = new FlowContentObserver();

    public ModelChangeObserver(Class<ModelClass> modelClass, DbLoader<?> loader) {
        this.mModelClass = (Class) Preconditions.isNull(modelClass, "Class<ModelClass> object must not be null");
        this.mLoader = (DbLoader) Preconditions.isNull(loader, "DbLoader must not be null");
    }

    public void registerObserver() {
        this.mObserver.registerForContentChanges(this.mLoader.getContext(), this.mModelClass);
        this.mObserver.addModelChangeListener(this);
    }

    public void unregisterObserver() {
        this.mObserver.unregisterForContentChanges(this.mLoader.getContext());
        this.mObserver.removeModelChangeListener(this);
    }

    public void onModelStateChanged(Class<? extends Model> cls, Action action) {
        if (!this.mLoader.isLoading()) {
            this.mLoader.onContentChanged();
            this.mLoader.loadInBackground();
        }
    }
}
