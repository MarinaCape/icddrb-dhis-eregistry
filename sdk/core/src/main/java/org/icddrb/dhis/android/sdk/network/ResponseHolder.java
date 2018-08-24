package org.icddrb.dhis.android.sdk.network;

import retrofit.client.Response;

public class ResponseHolder<T> {
    private APIException apiException;
    private T item;
    private Response response;

    public APIException getApiException() {
        return this.apiException;
    }

    public void setApiException(APIException apiException) {
        this.apiException = apiException;
    }

    public Response getResponse() {
        return this.response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public T getItem() {
        return this.item;
    }

    public void setItem(T item) {
        this.item = item;
    }
}
