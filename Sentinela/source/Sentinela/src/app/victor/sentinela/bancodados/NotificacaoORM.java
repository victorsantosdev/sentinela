
package app.victor.sentinela.bancodados;

import java.util.ArrayList;
import java.util.List;

import app.victor.sentinela.bdclasses.Notificacao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class NotificacaoORM {
    private static final String TAG = "NotificacaoORM";

    private static final String TABLE_NAME = "notificacao";

    private static final String COMMA_SEP = ", ";

    private static final String COLUMN_NOTIFICACAO_ID_TYPE = "INTEGER PRIMARY KEY";
    private static final String COLUMN_NOTIFICACAO_ID = "notificacao_id";

    private static final String COLUMN_AGENTE_ID_TYPE = "INTEGER NOT NULL";
    private static final String COLUMN_AGENTE_ID = "agente_id";

    private static final String COLUMN_TERRENO_ID_TYPE = "INTEGER NOT NULL";
    private static final String COLUMN_TERRENO_ID = "terreno_id";

    private static final String COLUMN_CODIGOLEI_ID_TYPE = "INTEGER NOT NULL";
    private static final String COLUMN_CODIGOLEI_ID = "codigolei_id";

    private static final String COLUMN_NOTIFICACAO_FOTOPATH_TYPE = "TEXT";
    private static final String COLUMN_NOTIFICACAO_FOTOPATH = "notificacao_fotopath";

    private static final String COLUMN_NOTIFICACAO_DATA_TYPE = "TEXT";
    private static final String COLUMN_NOTIFICACAO_DATA = "notificacao_data";


    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COLUMN_NOTIFICACAO_ID + " " + COLUMN_NOTIFICACAO_ID_TYPE + COMMA_SEP +
                    COLUMN_AGENTE_ID + " " + COLUMN_AGENTE_ID_TYPE + COMMA_SEP +
                    COLUMN_TERRENO_ID + " " + COLUMN_TERRENO_ID_TYPE + COMMA_SEP +
                    COLUMN_CODIGOLEI_ID  + " " + COLUMN_CODIGOLEI_ID_TYPE + COMMA_SEP +
                    COLUMN_NOTIFICACAO_FOTOPATH + " " + COLUMN_NOTIFICACAO_FOTOPATH_TYPE + COMMA_SEP + 
                    COLUMN_NOTIFICACAO_DATA + " " + COLUMN_NOTIFICACAO_DATA_TYPE + COMMA_SEP + 
                    "FOREIGN KEY("+COLUMN_AGENTE_ID+") REFERENCES usuario(usuario_id)"  + COMMA_SEP +
                    "FOREIGN KEY("+COLUMN_TERRENO_ID+") REFERENCES terreno(terreno_id)"  + COMMA_SEP +
                    "FOREIGN KEY("+COLUMN_CODIGOLEI_ID+") REFERENCES codigoslei(codigolei_id)"  +
                    ")";

    public static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static void insertNotificacao(Context context, Notificacao notificacao) {

        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getWritableDatabase();

        ContentValues values = postToContentValues(notificacao);
        long notificacaoID = database.insert(NotificacaoORM.TABLE_NAME, "null", values);
        Log.i(TAG, "Inserted new Notificacao with ID: " + notificacaoID);

        database.close(); //whenever you get a readable/writable db you should close it after using
    }

    /**
     * Packs a Post object into a ContentValues map for use with SQL inserts.
     */
    private static ContentValues postToContentValues(Notificacao notificacao) {
        ContentValues values = new ContentValues();
        values.put(NotificacaoORM.COLUMN_NOTIFICACAO_ID, notificacao.getNotificacaoID());
        values.put(NotificacaoORM.COLUMN_AGENTE_ID, notificacao.getAgenteID());
        values.put(NotificacaoORM.COLUMN_TERRENO_ID, notificacao.getTerrenoID());
        values.put(NotificacaoORM.COLUMN_CODIGOLEI_ID, notificacao.getCodigoLeiID());
        values.put(NotificacaoORM.COLUMN_NOTIFICACAO_FOTOPATH, notificacao.getNotificacaoFotoPath());
        values.put(NotificacaoORM.COLUMN_NOTIFICACAO_DATA, notificacao.getNotificacaoData());
        return values;
    }

    public static List<Notificacao> getNotificacoes(Context context) {
        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + NotificacaoORM.TABLE_NAME, null);

        Log.i(TAG, "Loaded " + cursor.getCount() + " Notificacao...");
        List<Notificacao> notificacoesList = new ArrayList<Notificacao>();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Notificacao notificacao = cursorToNotificacao(cursor);
                notificacoesList.add(notificacao);
                cursor.moveToNext();
            }
            Log.i(TAG, "Posts loaded successfully.");
        }

        cursor.close();
        database.close(); //whenever you get a readable/writable db you should close it after using

        return notificacoesList;
    }

    /**
     * Populates a Post object with data from a Cursor
     * @param cursor
     * @return
     */
    private static Notificacao cursorToNotificacao(Cursor cursor) {
        Notificacao notificacao = new Notificacao();
        notificacao.setNotificacaoID(cursor.getInt(cursor.getColumnIndex(COLUMN_NOTIFICACAO_ID)));
        notificacao.setAgenteID(cursor.getInt(cursor.getColumnIndex(COLUMN_AGENTE_ID)));
        notificacao.setTerrenoID(cursor.getInt(cursor.getColumnIndex(COLUMN_TERRENO_ID)));
        notificacao.setCodigoLeiID(cursor.getInt(cursor.getColumnIndex(COLUMN_CODIGOLEI_ID)));
        notificacao.setNotificacaoFotoPath(cursor.getString(cursor.getColumnIndex(COLUMN_NOTIFICACAO_FOTOPATH)));
        notificacao.setNotificacaoData(cursor.getString(cursor.getColumnIndex(COLUMN_NOTIFICACAO_DATA)));

        return notificacao;
    }
}