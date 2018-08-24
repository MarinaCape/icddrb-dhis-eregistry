package org.icddrb.dhis.android.sdk.ui.fragments.dataentry;

public class SaveThread extends AsyncHelperThread {
    private DataEntryFragment dataEntryFragment;

    public void init(DataEntryFragment dataEntryFragment) {
        setDataEntryFragment(dataEntryFragment);
    }

    public void setDataEntryFragment(DataEntryFragment dataEntryFragment) {
        this.dataEntryFragment = dataEntryFragment;
    }

    protected void work() {
        if (this.dataEntryFragment != null) {
            this.dataEntryFragment.save();
        }
    }

    public void kill() {
        super.kill();
        this.dataEntryFragment = null;
    }
}
