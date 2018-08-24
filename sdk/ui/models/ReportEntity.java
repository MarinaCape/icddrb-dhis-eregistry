package org.icddrb.dhis.client.sdk.ui.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.HashMap;
import java.util.Map;
import org.icddrb.dhis.client.sdk.utils.Preconditions;

public class ReportEntity implements Parcelable {
    public static final Creator<ReportEntity> CREATOR = new C09541();
    private final Map<String, String> dataElementMap;
    private final String id;
    private final Status status;

    /* renamed from: org.icddrb.dhis.client.sdk.ui.models.ReportEntity$1 */
    static class C09541 implements Creator<ReportEntity> {
        C09541() {
        }

        public ReportEntity createFromParcel(Parcel in) {
            return new ReportEntity(in);
        }

        public ReportEntity[] newArray(int size) {
            return new ReportEntity[size];
        }
    }

    public enum Status {
        SENT,
        TO_UPDATE,
        TO_POST,
        ERROR
    }

    public ReportEntity(String id, Status status, Map<String, String> dataElementMap) {
        this.id = (String) Preconditions.isNull(id, "id must not be null");
        this.status = (Status) Preconditions.isNull(status, "status must not be null");
        this.dataElementMap = dataElementMap;
    }

    public String getId() {
        return this.id;
    }

    public Status getStatus() {
        return this.status;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.status.name());
        dest.writeMap(this.dataElementMap);
    }

    private ReportEntity(Parcel in) {
        this.id = in.readString();
        this.status = Status.valueOf(in.readString());
        this.dataElementMap = new HashMap();
        in.readMap(this.dataElementMap, null);
    }

    public String getValueForDataElement(String uid) {
        if (this.dataElementMap.containsKey(uid)) {
            return (String) this.dataElementMap.get(uid);
        }
        return "none";
    }
}
