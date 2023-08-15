package com.zybooks.weighttracker.helpers;

import android.database.sqlite.SQLiteDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserHelper {
    public UserHelper() {}

    public static String hash(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.reset();
        md.update(password.getBytes());
        byte[] mdArray = md.digest();
        StringBuilder builder = new StringBuilder(mdArray.length * 2);
        for (byte b : mdArray) {
            int v = b & 0xfff;
            if (v < 16) {
                builder.append('0');
            }

            builder.append(Integer.toHexString(v));
        }

        return builder.toString();
    }

}
