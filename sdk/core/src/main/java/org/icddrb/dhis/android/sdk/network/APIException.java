package org.icddrb.dhis.android.sdk.network;

import java.io.IOException;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class APIException extends RuntimeException {
    private final Kind kind;
    private final Response response;
    private final String url;

    public enum Kind {
        NETWORK,
        CONVERSION,
        HTTP,
        UNEXPECTED
    }

    public static APIException fromRetrofitError(RetrofitError error) {
        switch (error.getKind()) {
            case NETWORK:
                return networkError(error.getUrl(), (IOException) error.getCause());
            case CONVERSION:
                return conversionError(error.getUrl(), error.getResponse(), error.getCause());
            case HTTP:
                return httpError(error.getUrl(), error.getResponse());
            default:
                return unexpectedError(error.getUrl(), error.getCause());
        }
    }

    public static APIException networkError(String url, IOException exception) {
        return new APIException(exception.getMessage(), url, null, Kind.NETWORK, exception);
    }

    public static APIException conversionError(String url, Response response, Throwable exception) {
        return new APIException(exception.getMessage(), url, response, Kind.CONVERSION, exception);
    }

    public static APIException httpError(String url, Response response) {
        return new APIException(response.getStatus() + " " + response.getReason(), url, response, Kind.HTTP, null);
    }

    public static APIException unexpectedError(String url, Throwable exception) {
        return new APIException(exception.getMessage(), url, null, Kind.UNEXPECTED, exception);
    }

    APIException(String message, String url, Response response, Kind kind, Throwable exception) {
        super(message, exception);
        this.url = url;
        this.response = response;
        this.kind = kind;
    }

    public String getUrl() {
        return this.url;
    }

    public Response getResponse() {
        return this.response;
    }

    public Kind getKind() {
        return this.kind;
    }
}
