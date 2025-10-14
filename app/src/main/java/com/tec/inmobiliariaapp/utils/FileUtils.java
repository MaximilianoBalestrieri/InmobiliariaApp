package com.tec.inmobiliariaapp.utils;



import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import java.io.File;

public class FileUtils {

    /**
     * Obtiene la ruta del archivo real a partir de un Uri de MediaStore.
     * Esto funciona para Uris simples de la galería.
     * Para Android 10+ y Storage Access Framework, se necesita una lógica más compleja (como copiar el stream).
     */
    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Log.e("FileUtils", "Error al obtener path", e);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
}