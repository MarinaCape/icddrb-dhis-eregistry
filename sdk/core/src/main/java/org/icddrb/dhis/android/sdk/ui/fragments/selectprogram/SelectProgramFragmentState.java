package org.icddrb.dhis.android.sdk.ui.fragments.selectprogram;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SelectProgramFragmentState implements Parcelable {
    public static final Creator<SelectProgramFragmentState> CREATOR = new C09201();
    private static final String TAG = SelectProgramFragmentState.class.getName();
    private String filterId;
    private String filterLabel;
    private String orgUnitId;
    private String orgUnitLabel;
    private String programId;
    private String programName;
    private boolean syncInProcess;

    /* renamed from: org.icddrb.dhis.android.sdk.ui.fragments.selectprogram.SelectProgramFragmentState$1 */
    static class C09201 implements Creator<SelectProgramFragmentState> {
        C09201() {
        }

        public SelectProgramFragmentState createFromParcel(Parcel in) {
            return new SelectProgramFragmentState(in);
        }

        public SelectProgramFragmentState[] newArray(int size) {
            return new SelectProgramFragmentState[size];
        }
    }

    public SelectProgramFragmentState(){}

    public SelectProgramFragmentState(SelectProgramFragmentState state) {
        if (state != null) {
            setSyncInProcess(state.isSyncInProcess());
            setOrgUnit(state.getOrgUnitId(), state.getOrgUnitLabel());
            setProgram(state.getProgramId(), state.getProgramName());
            setFilter(state.getFilterId(), state.getFilterLabel());
        }
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

    private SelectProgramFragmentState(Parcel in) {
        boolean z = true;
        if (in.readInt() != 1) {
            z = false;
        }
        this.syncInProcess = z;
        this.orgUnitLabel = in.readString();
        this.orgUnitId = in.readString();
        this.programName = in.readString();
        this.programId = in.readString();
        this.filterId = in.readString();
        this.filterLabel = in.readString();
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
        parcel.writeString(this.filterId);
        parcel.writeString(this.filterLabel);
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

    public void resetOrgUnit() {
        this.orgUnitId = null;
        this.orgUnitLabel = null;
    }

    public boolean isOrgUnitEmpty() {
        return this.orgUnitId == null || this.orgUnitLabel == null;
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

    public void setFilter(String filterId, String filterLabel) {
        this.filterId = filterId;
        this.filterLabel = filterLabel;
    }

    public void resetProgram() {
        this.programId = null;
        this.programName = null;
    }

    public void resetFilter() {
        this.filterId = null;
        this.filterLabel = null;
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

    public String getFilterId() {
        return this.filterId;
    }

    public void setFilterId(String filterId) {
        this.filterId = filterId;
    }

    public String getFilterLabel() {
        return this.filterLabel;
    }

    public void setFilterLabel(String filterLabel) {
        this.filterLabel = filterLabel;
    }
}
