package ru.makproductions.androidlevel2;

import android.content.Intent;
import android.telephony.SmsMessage;

public class SMSHelper {
    public static String receiveSMS(Intent intent) {
        if (intent != null && intent.getAction() != null) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            if (pdus.length > 1) return "";
            SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }
            StringBuilder body = new StringBuilder();
            for (int i = 0; i < messages.length; i++) {
                body.append(messages[i].getMessageBody());
            }
            return body.toString();
        }
        return "";
    }
}
