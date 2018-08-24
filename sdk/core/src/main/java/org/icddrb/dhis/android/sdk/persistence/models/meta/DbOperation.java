package org.icddrb.dhis.android.sdk.persistence.models.meta;

import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.BaseModel.Action;
import org.icddrb.dhis.android.sdk.utils.Preconditions;

public final class DbOperation {
    private final Action mAction;
    private final BaseModel mModel;

    private DbOperation(Action action, BaseModel model) {
        this.mModel = (BaseModel) Preconditions.isNull(model, "BaseModel object must nto be null,");
        this.mAction = (Action) Preconditions.isNull(action, "BaseModel.Action object must not be null");
    }

    public static <T extends BaseModel> DbOperation insert(T model) {
        return new DbOperation(Action.INSERT, model);
    }

    public static <T extends BaseModel> DbOperation update(T model) {
        return new DbOperation(Action.UPDATE, model);
    }

    public static <T extends BaseModel> DbOperation save(T model) {
        return new DbOperation(Action.SAVE, model);
    }

    public static <T extends BaseModel> DbOperation delete(T model) {
        return new DbOperation(Action.DELETE, model);
    }

    public BaseModel getModel() {
        return this.mModel;
    }

    public Action getAction() {
        return this.mAction;
    }
}
