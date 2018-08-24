package org.icddrb.dhis.android.sdk.utils;

import com.raizlabs.android.dbflow.runtime.TransactionManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Database;
import org.icddrb.dhis.android.sdk.persistence.models.BaseIdentifiableObject;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.persistence.models.meta.DbOperation;
import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;

public final class DbUtils {
    private DbUtils() {
    }

    public static void applyBatch(final Collection<DbOperation> operations) {
        Preconditions.isNull(operations, "List<DbOperation> object must not be null");
        if (!operations.isEmpty()) {
            TransactionManager.transact(Dhis2Database.NAME, new Runnable() {
                public void run() {
                    for (DbOperation operation : operations) {
                        switch (operation.getAction()) {
                            case INSERT:
                                operation.getModel().insert();
                                break;
                            case UPDATE:
                                operation.getModel().update();
                                break;
                            case SAVE:
                                operation.getModel().save();
                                break;
                            case DELETE:
                                operation.getModel().delete();
                                break;
                            default:
                                break;
                        }
                    }
                }
            });
        }
    }

    public static <T extends BaseIdentifiableObject> List<DbOperation> createOperations(List<T> oldModels, List<T> newModels, boolean keepOldValues) {
        List<DbOperation> ops = new ArrayList();
        Map<String, T> newModelsMap = BaseIdentifiableObject.toMap(newModels);
        Map<String, T> oldModelsMap = BaseIdentifiableObject.toMap(oldModels);
        for (String oldModelKey : oldModelsMap.keySet()) {
            BaseIdentifiableObject newModel = (BaseIdentifiableObject) newModelsMap.get(oldModelKey);
            BaseIdentifiableObject oldModel = (BaseIdentifiableObject) oldModelsMap.get(oldModelKey);
            if (newModel != null) {
                DateTime oldTime = null;
                if (oldModel.getLastUpdated() != null) {
                    try {
                        oldTime = DateTime.parse(oldModel.getLastUpdated());
                    } catch (Exception e) {
                    }
                }
                DateTime newTime = null;
                if (newModel.getLastUpdated() != null) {
                    try {
                        newTime = DateTime.parse(newModel.getLastUpdated());
                    } catch (Exception e2) {
                    }
                }
                if (oldTime == null || newTime == null || newTime.isAfter((ReadableInstant) oldTime)) {
                    newModel.setUid(oldModel.getUid());
                    ops.add(DbOperation.update(newModel));
                }
                newModelsMap.remove(oldModelKey);
            } else if (!keepOldValues) {
                ops.add(DbOperation.delete(oldModel));
            }
        }
        for (String newModelKey : newModelsMap.keySet()) {
            ops.add(DbOperation.save((BaseIdentifiableObject) newModelsMap.get(newModelKey)));
        }
        return ops;
    }

    public static <T extends BaseValue> List<DbOperation> createBaseValueOperations(List<T> oldModels, List<T> newModels, boolean keepOldValues) {
        List<DbOperation> ops = new ArrayList();
        Map<String, T> newModelsMap = BaseValue.toMap(newModels);
        Map<String, T> oldModelsMap = BaseValue.toMap(oldModels);
        for (String oldModelKey : oldModelsMap.keySet()) {
            BaseValue newModel = (BaseValue) newModelsMap.get(oldModelKey);
            BaseValue oldModel = (BaseValue) oldModelsMap.get(oldModelKey);
            if (newModel != null) {
                if (oldModel == null || newModel == null || oldModel.getValue().equals(newModel.getValue())) {
                    newModel.setValue(oldModel.getValue());
                    ops.add(DbOperation.update(newModel));
                }
                newModelsMap.remove(oldModelKey);
            } else if (!keepOldValues) {
                ops.add(DbOperation.delete(oldModel));
            }
        }
        for (String newModelKey : newModelsMap.keySet()) {
            ops.add(DbOperation.save((BaseValue) newModelsMap.get(newModelKey)));
        }
        return ops;
    }

    public static <T extends BaseIdentifiableObject> List<DbOperation> removeResources(List<T> list) {
        List<DbOperation> ops = new ArrayList();
        Map<String, T> listToBeRemoved = BaseIdentifiableObject.toMap(list);
        for (String newModelKey : listToBeRemoved.keySet()) {
            ops.add(DbOperation.delete((BaseIdentifiableObject) listToBeRemoved.get(newModelKey)));
        }
        return ops;
    }
}
