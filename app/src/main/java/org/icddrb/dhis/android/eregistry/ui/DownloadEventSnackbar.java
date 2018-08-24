package org.icddrb.dhis.android.eregistry.ui;

import android.support.design.widget.Snackbar;
import android.support.design.widget.Snackbar.Callback;
import android.view.View;
import android.view.View.OnClickListener;
import org.icddrb.dhis.android.eregistry.fragments.selectprogram.SelectProgramFragment;

public class DownloadEventSnackbar {
    private boolean errorHasOccured;
    private final SelectProgramFragment selectProgramFragment;
    private Snackbar snackbar;

    /* renamed from: org.icddrb.dhis.android.eregistry.ui.DownloadEventSnackbar$1 */
    class C08361 implements OnClickListener {
        C08361() {
        }

        public void onClick(View v) {
            DownloadEventSnackbar.this.selectProgramFragment.downloadAll();
        }
    }

    /* renamed from: org.icddrb.dhis.android.eregistry.ui.DownloadEventSnackbar$2 */
    class C08372 extends Callback {
        C08372() {
        }

        public void onDismissed(Snackbar snackbar, int event) {
            super.onDismissed(snackbar, event);
            DownloadEventSnackbar.this.snackbar = null;
        }
    }

    public DownloadEventSnackbar(SelectProgramFragment selectProgramFragment) {
        this.selectProgramFragment = selectProgramFragment;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void show(org.icddrb.dhis.android.sdk.events.OnTeiDownloadedEvent r5) {
        /*
        r4 = this;
        r3 = 1;
        r0 = org.icddrb.dhis.android.eregistry.ui.DownloadEventSnackbar.C08383.f38x3a3c559a;
        r1 = r5.getEventType();
        r1 = r1.ordinal();
        r0 = r0[r1];
        switch(r0) {
            case 1: goto L_0x0022;
            case 2: goto L_0x0031;
            case 3: goto L_0x004f;
            default: goto L_0x0010;
        };
    L_0x0010:
        r0 = r4.selectProgramFragment;
        r0 = r0.getContext();
        r0 = r5.getUserFriendlyMessage(r0);
        r1 = r5.getMessageDuration();
        r4.showSnackbar(r0, r1);
    L_0x0021:
        return;
    L_0x0022:
        r0 = r4.snackbar;
        if (r0 == 0) goto L_0x002b;
    L_0x0026:
        r0 = r4.snackbar;
        r0.dismiss();
    L_0x002b:
        r0 = 0;
        r4.snackbar = r0;
        r0 = 0;
        r4.errorHasOccured = r0;
    L_0x0031:
        r0 = java.lang.System.out;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Norway: ";
        r1 = r1.append(r2);
        r2 = r5.getEventType();
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.println(r1);
        r4.errorHasOccured = r3;
    L_0x004f:
        r0 = r4.errorHasOccured;
        if (r0 == 0) goto L_0x0010;
    L_0x0053:
        r5.setErrorHasOccured(r3);
        r0 = r4.selectProgramFragment;
        r0 = r0.getContext();
        r0 = r5.getUserFriendlyMessage(r0);
        r1 = r5.getMessageDuration();
        r4.showSnackbar(r0, r1);
        r0 = r4.snackbar;
        r1 = r4.selectProgramFragment;
        r1 = r1.getContext();
        r2 = 2131558679; // 0x7f0d0117 float:1.874268E38 double:1.0531299154E-314;
        r1 = r1.getString(r2);
        r2 = new org.icddrb.dhis.android.eregistry.ui.DownloadEventSnackbar$1;
        r2.<init>();
        r0.setAction(r1, r2);
        goto L_0x0021;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.icddrb.dhis.android.eregistry.ui.DownloadEventSnackbar.show(org.icddrb.dhis.android.sdk.events.OnTeiDownloadedEvent):void");
    }

    private void showSnackbar(String message, int duration) {
        if (this.selectProgramFragment.getView() != null) {
            if (this.snackbar == null) {
                this.snackbar = Snackbar.make(this.selectProgramFragment.getView(), (CharSequence) message, duration);
                this.snackbar.setCallback(new C08372());
                this.snackbar.show();
                return;
            }
            this.snackbar.setText((CharSequence) message);
            this.snackbar.setDuration(duration);
            if (!this.snackbar.isShown()) {
                this.snackbar.show();
            }
        }
    }
}
