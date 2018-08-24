package org.icddrb.dhis.client.sdk.ui.models;

public class ReportEntityFilter implements Comparable<ReportEntityFilter> {
    private String dataElementId;
    private String dataElementLabel;
    private boolean show;

    public ReportEntityFilter(String dataElementId, String dataElementLabel, boolean show) {
        this.dataElementId = dataElementId;
        this.dataElementLabel = dataElementLabel;
        this.show = show;
    }

    public String getDataElementId() {
        return this.dataElementId;
    }

    public void setDataElementId(String dataElementId) {
        this.dataElementId = dataElementId;
    }

    public String getDataElementLabel() {
        return this.dataElementLabel;
    }

    public void setDataElementLabel(String dataElementLabel) {
        this.dataElementLabel = dataElementLabel;
    }

    public boolean show() {
        return this.show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public int compareTo(ReportEntityFilter another) {
        if (this.show == another.show) {
            return this.dataElementLabel.compareTo(another.dataElementLabel);
        }
        if (this.show) {
            return -1;
        }
        if (another.show) {
            return 1;
        }
        return 0;
    }
}
