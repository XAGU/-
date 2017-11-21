package com.xiaolian.amigo.util.crash.acra.sender;

import android.content.Context;
import android.support.annotation.NonNull;

import com.xiaolian.amigo.util.crash.acra.config.ACRAConfiguration;

/**
 * Constructs an {@link EmailIntentSender}.
 */
public final class EmailIntentSenderFactory implements ReportSenderFactory {

    @NonNull
    @Override
    public ReportSender create(@NonNull Context context, @NonNull ACRAConfiguration config) {
        return new EmailIntentSender(config);
    }
}
