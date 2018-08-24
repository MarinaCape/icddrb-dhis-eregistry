package org.icddrb.dhis.android.sdk.export;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.ref.WeakReference;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRuleAction.Table;

public abstract class ExportService<T extends ExportResponse> extends Service {
    final Messenger messenger = new Messenger(new MessageHandler(this));

    private static class MessageHandler<T extends ExportResponse> extends Handler {
        private final WeakReference<ExportService<T>> exportServiceWeakReference;

        public MessageHandler(ExportService<T> exportServiceInstance) {
            this.exportServiceWeakReference = new WeakReference(exportServiceInstance);
        }

        public void handleMessage(Message msg) {
            if (msg.replyTo != null && this.exportServiceWeakReference.get() != null) {
                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString(Table.DATA, ((ExportService) this.exportServiceWeakReference.get()).marshallToString(((ExportService) this.exportServiceWeakReference.get()).getResponseObject()));
                message.setData(bundle);
                try {
                    msg.replyTo.send(message);
                } catch (RemoteException e) {
                    Log.e("EXPORT", "Error sending message to client", e);
                }
            }
        }
    }

    public abstract T getResponseObject();

    @Nullable
    public IBinder onBind(Intent intent) {
        Log.d("Service", "onBind. Intent: " + intent);
        return this.messenger.getBinder();
    }

    @NonNull
    private String marshallToString(T responseObject) {
        String responseString;
        ObjectMapper om = new ObjectMapper();
        try {
            return om.writeValueAsString(responseObject);
        } catch (JsonProcessingException e) {
            try {
                ExportResponse errorResponse = new ExportResponse();
                errorResponse.setError(e);
                responseString = om.writeValueAsString(errorResponse);
                Log.e("EXPORT", "Unable to marshall object to String: " + responseObject.getClass().toString(), e);
                return responseString;
            } catch (JsonProcessingException e1) {
                responseString = "Unable to marshall object to String\n" + e1.toString();
                Log.e("EXPORT", "Unable to marshall object to String", e1);
                return responseString;
            }
        }
    }
}
