package org.icddrb.dhis.android.sdk.job;

import org.icddrb.dhis.android.sdk.network.APIException;
import org.icddrb.dhis.android.sdk.network.ResponseHolder;
import org.icddrb.dhis.android.sdk.network.SessionManager;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.preferences.ResourceType;

public abstract class NetworkJob<T> extends Job<ResponseHolder<T>> {
    private final ResourceType mResourceType;

    public static class NetworkJobResult<Type> {
        private final ResourceType mResourceType;
        private final ResponseHolder<Type> mResponseHolder;

        public NetworkJobResult(ResourceType resourceType, ResponseHolder<Type> responseHolder) {
            this.mResourceType = resourceType;
            this.mResponseHolder = responseHolder;
        }

        public ResourceType getResourceType() {
            return this.mResourceType;
        }

        public ResponseHolder<Type> getResponseHolder() {
            return this.mResponseHolder;
        }
    }

    public abstract T execute() throws APIException;

    public NetworkJob(int jobId, ResourceType responseType) {
        super(jobId);
        this.mResourceType = responseType;
    }

    public final ResponseHolder<T> inBackground() {
        ResponseHolder<T> holder = new ResponseHolder();
        try {
            holder.setItem(execute());
        } catch (APIException exception) {
            holder.setApiException(exception);
        }
        return holder;
    }

    public final void onFinish(ResponseHolder<T> result) {
        SessionManager.getInstance().setResourceTypeSynced(this.mResourceType);
        Dhis2Application.getEventBus().post(new NetworkJobResult(this.mResourceType, result));
    }
}
