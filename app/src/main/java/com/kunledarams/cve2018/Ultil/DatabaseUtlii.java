package com.kunledarams.cve2018.Ultil;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentResolver.SCHEME_CONTENT;
import static android.content.ContentResolver.SCHEME_FILE;

/**
 * Created by ok on 10/24/2018.
 */

public class DatabaseUtlii {

    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }

    public String getPathFromUri(Uri uriPhoto){
        if(uriPhoto ==null)
            return null ;

        if(SCHEME_FILE.equals(uriPhoto.getScheme())) {
            return uriPhoto.getPath();
        }
        else if(SCHEME_CONTENT.equals(uriPhoto.getScheme())) {
            final String[] filePathColumn = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME};
            Cursor cursor = null;
            try {


               // cursor = RegistrationForm.getContentResolver().query(uriPhoto, filePathColumn, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int columnIdex = (uriPhoto.toString().startsWith("content://com.google.android.gallery3rd")) ? cursor
                            .getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME) : cursor.getColumnIndex(MediaStore.MediaColumns.DATA);


                    if (columnIdex != -1) {
                        String filePath = cursor.getString(columnIdex);
                        if (!TextUtils.isEmpty(filePath)) {
                            return filePath;
                        }
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException i) {
                i.printStackTrace();
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
        return null;

    }
}
