package org.icddrb.dhis.android.eregistry.fragments.programoverview;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

class ProgramOverviewFragmentState implements Parcelable {
    public static final Creator<ProgramOverviewFragmentState> CREATOR = new C07861();
    private static final String TAG = ProgramOverviewFragmentState.class.getName();
    private String orgUnitId;
    private String orgUnitLabel;
    private String programId;
    private String programName;
    private boolean syncInProcess;
    private long trackedEntityInstanceId;

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.programoverview.ProgramOverviewFragmentState$1 */
    static class C07861 implements Creator<ProgramOverviewFragmentState> {
        C07861() {
        }

        public ProgramOverviewFragmentState createFromParcel(Parcel in) {
            return new ProgramOverviewFragmentState(in);
        }

        public ProgramOverviewFragmentState[] newArray(int size) {
            return new ProgramOverviewFragmentState[size];
        }
    }

    public ProgramOverviewFragmentState(ProgramOverviewFragmentState state) {
        if (state != null) {
            setSyncInProcess(state.isSyncInProcess());
            setOrgUnit(state.getOrgUnitId(), state.getOrgUnitLabel());
            setProgram(state.getProgramId(), state.getProgramName());
            setTrackedEntityInstance(state.getTrackedEntityInstanceId());
        }
    }

    private ProgramOverviewFragmentState(Parcel in) {
        boolean z = true;
        if (in.readInt() != 1) {
            z = false;
        }
        this.syncInProcess = z;
        this.orgUnitLabel = in.readString();
        this.orgUnitId = in.readString();
        this.programName = in.readString();
        this.programId = in.readString();
        this.trackedEntityInstanceId = in.readLong();
    }

    public int describeContents() {
        return TAG.length();
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.syncInProcess ? 1 : 0);
        parcel.writeString(this.orgUnitLabel);
        parcel.writeString(this.orgUnitId);
        parcel.writeString(this.programName);
        parcel.writeString(this.programId);
        parcel.writeLong(this.trackedEntityInstanceId);
    }

    public boolean isSyncInProcess() {
        return this.syncInProcess;
    }

    public void setSyncInProcess(boolean syncInProcess) {
        this.syncInProcess = syncInProcess;
    }

    public void setOrgUnit(String orgUnitId, String orgUnitLabel) {
        this.orgUnitId = orgUnitId;
        this.orgUnitLabel = orgUnitLabel;
    }

    public boolean isElementTest() {
        return this.programId.equals("AYpOmMHjDPK");
    }

    public boolean isClientRegister() {
        return this.programId.equals("ZBIqxwVixn8");
    }

    public boolean isMCH() {
        return this.programId.equals("WSGAb5XwJ3Y") || this.programId.equals("JrW4FqXO6Mr");
    }

    public String getOrgUnitLabel() {
        return this.orgUnitLabel;
    }

    public String getOrgUnitId() {
        return this.orgUnitId;
    }

    public void setProgram(String programId, String programLabel) {
        this.programId = programId;
        this.programName = programLabel;
    }

    public void resetProgram() {
        this.programId = null;
        this.programName = null;
    }

    public void setTrackedEntityInstance(long trackedEntityInstanceId) {
        this.trackedEntityInstanceId = trackedEntityInstanceId;
    }

    public long getTrackedEntityInstanceId() {
        return this.trackedEntityInstanceId;
    }

    public boolean isProgramEmpty() {
        return this.programId == null || this.programName == null;
    }

    public String getProgramName() {
        return this.programName;
    }

    public String getProgramId() {
        return this.programId;
    }
}
