package org.icddrb.dhis.android.sdk.network;

import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.facebook.stetho.server.http.HttpStatus;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Interceptor.Chain;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.DateUtils;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.utils.StringConverter;
import retrofit.ErrorHandler;
import retrofit.RestAdapter.Builder;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.converter.JacksonConverter;

public final class RepoManager {
    static final int DEFAULT_CONNECT_TIMEOUT_MILLIS = 60000;
    static final int DEFAULT_READ_TIMEOUT_MILLIS = 60000;
    static final int DEFAULT_WRITE_TIMEOUT_MILLIS = 60000;

    private static class AuthInterceptor implements Interceptor {
        private final String mPassword;
        private final String mUsername;

        public AuthInterceptor(String username, String password) {
            this.mUsername = username;
            this.mPassword = password;
        }

        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder().addHeader(HttpRequest.HEADER_AUTHORIZATION, Credentials.basic(this.mUsername, this.mPassword)).build();
            Log.d("RepoManager", request.toString());
            Response response = chain.proceed(request);
            if (response.code() == 401) {
                DhisController.getInstance();
                if (DhisController.isUserLoggedIn()) {
                    DhisController.getInstance();
                    DhisController.invalidateSession();
                }
            }
            return response;
        }
    }

    private static class RetrofitErrorHandler implements ErrorHandler {
        private RetrofitErrorHandler() {
        }

        public Throwable handleError(RetrofitError cause) {
            cause.printStackTrace();
            Log.d("RepoManager", "there was an error.." + cause.getKind().name());
            try {
                Log.e("RepoManager", new StringConverter().fromBody(cause.getResponse().getBody(), (Type) String.class));
            } catch (ConversionException e) {
                e.printStackTrace();
            } catch (NullPointerException e2) {
                e2.printStackTrace();
            }
            APIException apiException = APIException.fromRetrofitError(cause);
            switch (apiException.getKind()) {
                case CONVERSION:
                case UNEXPECTED:
                    RepoManager.logResponseBody(cause);
                    Crashlytics.logException(cause.getCause());
                    break;
                case HTTP:
                    if (apiException.getResponse().getStatus() < HttpStatus.HTTP_INTERNAL_SERVER_ERROR) {
                        if (apiException.getResponse().getStatus() == 409) {
                            RepoManager.logResponseBody(cause);
                            Crashlytics.logException(cause.getCause());
                            break;
                        }
                    }
                    RepoManager.logResponseBody(cause);
                    Crashlytics.logException(cause.getCause());
                    break;
                    break;
            }
            return apiException;
        }
    }

    private RepoManager() {
    }

    public static DhisApi createService(HttpUrl serverUrl, Credentials credentials) {
        return (DhisApi) new Builder().setEndpoint(provideServerUrl(serverUrl)).setConverter(provideJacksonConverter()).setClient(provideOkClient(credentials)).setErrorHandler(new RetrofitErrorHandler()).setLogLevel(LogLevel.HEADERS).build().create(DhisApi.class);
    }

    private static String provideServerUrl(HttpUrl httpUrl) {
        return httpUrl.newBuilder().addPathSegment("api").build().toString();
    }

    private static Converter provideJacksonConverter() {
        return new JacksonConverter(DhisController.getInstance().getObjectMapper());
    }

    private static OkClient provideOkClient(Credentials credentials) {
        return new OkClient(provideOkHttpClient(credentials));
    }

    public static OkHttpClient provideOkHttpClient(Credentials credentials) {
        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(new StethoInterceptor());
        client.interceptors().add(provideInterceptor(credentials));
        client.setConnectTimeout(DateUtils.MILLIS_PER_MINUTE, TimeUnit.MILLISECONDS);
        client.setReadTimeout(DateUtils.MILLIS_PER_MINUTE, TimeUnit.MILLISECONDS);
        client.setWriteTimeout(DateUtils.MILLIS_PER_MINUTE, TimeUnit.MILLISECONDS);
        return client;
    }

    private static Interceptor provideInterceptor(Credentials credentials) {
        return new AuthInterceptor(credentials.getUsername(), credentials.getPassword());
    }

    private static void logResponseBody(RetrofitError cause) {
        if (cause.getResponse() != null && cause.getResponse().getBody() != null) {
            Crashlytics.log(cause.getResponse().getBody().toString());
        }
    }
}
