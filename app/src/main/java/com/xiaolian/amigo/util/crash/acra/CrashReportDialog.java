package com.xiaolian.amigo.util.crash.acra;

import android.os.Bundle;

import static com.xiaolian.amigo.util.crash.acra.ACRA.LOG_TAG;


/**
 * Old crash report dialog.
 *
 * @deprecated since 4.8.0 use {@link com.xiaolian.amigo.util.crash.acra.dialog.CrashReportDialog} instead
 **/
public final class CrashReportDialog extends com.xiaolian.amigo.util.crash.acra.dialog.CrashReportDialog {

    @Override
    protected void buildAndShowDialog(Bundle savedInstanceState){
        ACRA.log.w(LOG_TAG, "org.acra.CrashReportDialog has been deprecated. Please use org.acra.dialog.CrashReportDialog instead");
        super.buildAndShowDialog(savedInstanceState);
    }
}