package com.xiaolian.amigo.util.crash.acra.sender;

import android.content.Context;
import android.support.annotation.NonNull;

import com.xiaolian.amigo.util.crash.acra.ACRA;
import com.xiaolian.amigo.util.crash.acra.collector.CrashReportData;

import static com.xiaolian.amigo.util.crash.acra.ACRA.LOG_TAG;

/**
 * Sends no report.
 */
final class NullSender implements ReportSender {
    @Override
    public void send(@NonNull Context context, @NonNull CrashReportData errorContent) throws ReportSenderException {
        ACRA.log.w(LOG_TAG, context.getPackageName() + " reports will NOT be sent - no valid ReportSender is configured. Try setting 'formUri' or 'mailTo'");
    }
}
