package app.victor.sentinela.bancodados;

import java.util.ArrayList;
import java.util.List;

import app.victor.sentinela.bdclasses.Infracao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class InfracaoORM {
    private static final String TAG = "InfracaoORM";

    private static final String TABLE_NAME = "infracao";

    private static final String COMMA_SEP = ", ";

    private static final String COLUMN_INFRACAO_ID_TYPE = "INTEGER PRIMARY KEY";
    private static final String COLUMN_INFRACAO_ID = "infracao_id";

    private static final String COLUMN_AGENTE_ID_TYPE = "INTEGER NOT NULL";
    private static final String COLUMN_AGENTE_ID = "agente_id";
    
    private static final String COLUMN_TERRENO_ID_TYPE = "INTEGER NOT NULL";
    private static final String COLUMN_TERRENO_ID = "terreno_id";
    
    private static final String COLUMN_CODIGOLEI_ID_TYPE = "INTEGER NOT NULL";
    private static final String COLUMN_CODIGOLEI_ID = "codigolei_id";
    
    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COLUMN_INFRACAO_ID + " " + COLUMN_INFRACAO_ID_TYPE + COMMA_SEP +
                    COLUMN_AGENTE_ID + " " + COLUMN_AGENTE_ID_TYPE + COMMA_SEP +
                    COLUMN_TERRENO_ID + " " + COLUMN_TERRENO_ID_TYPE + COMMA_SEP +
                    COLUMN_CODIGOLEI_ID  + " " + COLUMN_CODIGOLEI_ID_TYPE + COMMA_SEP +
                    "FOREIGN KEY("+COLUMN_AGENTE_ID+") REFERENCES usuario(usuario_id)"  + COMMA_SEP +
                    "FOREIGN KEY("+COLUMN_TERRENO_ID+") REFERENCES terreno(terreno_id)"  + COMMA_SEP +
                    "FOREIGN KEY("+COLUMN_CODIGOLEI_ID+") REFERENCES codigoslei(codigolei_id)"  +
                    ")";

    public static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static void insertInfracao(Context context, Infracao infracao) {

        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getWritableDatabase();

        ContentValues values = postToContentValues(infracao);
        long infracaoID = database.insert(InfracaoORM.TABLE_NAME, "null", values);
        Log.i(TAG, "Inserted new Infracao with ID: " + infracaoID);

        database.close(); //whenever you get a readable/writable db you should close it after using
    }

    /**
     * Packs a Post object into a ContentValues map for use with SQL inserts.
     */
    private static ContentValues postToContentValues(Infracao infracao) {
        ContentValues values = new ContentValues();
        values.put(InfracaoORM.COLUMN_INFRACAO_ID, infracao.getInfracaoID());
        values.put(InfracaoORM.COLUMN_AGENTE_ID, infracao.getAgenteID());
        values.put(InfracaoORM.COLUMN_TERRENO_ID, infracao.getTerrenoID());
        values.put(InfracaoORM.COLUMN_CODIGOLEI_ID, infracao.getCodigoLeiID());
        return values;
    }

    public static List<Infracao> getInfracoes(Context context) {
        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + InfracaoORM.TABLE_NAME, null);

        Log.i(TAG, "Loaded " + cursor.getCount() + " Infracao...");
        List<Infracao> infracoesList = new ArrayList<Infracao>();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Infracao infracao = cursorToInfracao(cursor);
                infracoesList.add(infracao);
                cursor.moveToNext();
            }
            Log.i(TAG, "Posts loaded successfully.");
        }

        cursor.close();
        database.close(); //whenever you get a readable/writable db you should close it after using

        return infracoesList;
    }

    /**
     * Populates a Post object with data from a Cursor
     * @param cursor
     * @return
     */
    private static Infracao cursorToInfracao(Cursor cursor) {
        Infracao infracao = new Infracao();
        infracao.setInfracaoID(cursor.getInt(cursor.getColumnIndex(COLUMN_INFRACAO_ID)));
        infracao.setAgenteID(cursor.getInt(cursor.getColumnIndex(COLUMN_AGENTE_ID)));
        infracao.setTerrenoID(cursor.getInt(cursor.getColumnIndex(COLUMN_TERRENO_ID)));
        infracao.setCodigoLeiID(cursor.getInt(cursor.getColumnIndex(COLUMN_CODIGOLEI_ID)));


        return infracao;
    }
}
