
package app.victor.sentinela.imbituba.bancodados;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import app.victor.sentinela.imbituba.bdclasses.CodigoLei;

public class CodigoLeiORM {
    private static final String TAG = "CodigoLeiORM";

    private static final String TABLE_NAME = "codigoslei";

    private static final String COMMA_SEP = ", ";

    private static final String COLUMN_CODIGOLEI_ID_TYPE = "INTEGER PRIMARY KEY";
    private static final String COLUMN_CODIGOLEI_ID = "codigolei_id";

    private static final String COLUMN_CODIGO_TYPE = "TEXT";
    private static final String COLUMN_CODIGO = "codigo";
    
    private static final String COLUMN_DESCRICAO_TYPE = "TEXT";
    private static final String COLUMN_DESCRICAO = "descricao";

    private static final String COLUMN_VALORINFRACAO_TYPE = "REAL";
    private static final String COLUMN_VALORINFRACAO = "valorinfracao";


    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COLUMN_CODIGOLEI_ID + " " + COLUMN_CODIGOLEI_ID_TYPE + COMMA_SEP +
                    COLUMN_CODIGO + " " + COLUMN_CODIGO_TYPE + COMMA_SEP +
                    COLUMN_DESCRICAO  + " " + COLUMN_DESCRICAO_TYPE + COMMA_SEP +
                    COLUMN_VALORINFRACAO + " " + COLUMN_VALORINFRACAO_TYPE +              
                    ")";

    public static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static void insertCodigoLei(Context context, CodigoLei codigoLei) {

        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getWritableDatabase();

        ContentValues values = postToContentValues(codigoLei);
        long codigoLeiID = database.insert(CodigoLeiORM.TABLE_NAME, "null", values);
        Log.i(TAG, "Inserted new codigoLei with ID: " + codigoLeiID);

        database.close(); //whenever you get a readable/writable db you should close it after using
    }

    /**
     * Packs a Post object into a ContentValues map for use with SQL inserts.
     */
    private static ContentValues postToContentValues(CodigoLei codigoLei) {
        ContentValues values = new ContentValues();
        values.put(CodigoLeiORM.COLUMN_CODIGOLEI_ID, codigoLei.getCodigoLeiID());
        values.put(CodigoLeiORM.COLUMN_CODIGO, codigoLei.getCodigoLei());
        values.put(CodigoLeiORM.COLUMN_DESCRICAO, codigoLei.getDescricao());
        values.put(CodigoLeiORM.COLUMN_VALORINFRACAO, codigoLei.getValorInfracao());
        return values;
    }

    public static List<CodigoLei> getCodigosLei(Context context) {
        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + CodigoLeiORM.TABLE_NAME, null);

        Log.i(TAG, "Loaded " + cursor.getCount() + " CodigosLei...");
        List<CodigoLei> ciList = new ArrayList<CodigoLei>();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                CodigoLei codigoLei = cursorToCodigoLei(cursor);
                ciList.add(codigoLei);
                cursor.moveToNext();
            }
            Log.i(TAG, "Posts loaded successfully.");
        }

        cursor.close();
        database.close(); //whenever you get a readable/writable db you should close it after using

        return ciList;
    }

    /**
     * Populates a Post object with data from a Cursor
     * @param cursor
     * @return
     */
    private static CodigoLei cursorToCodigoLei(Cursor cursor) {
        CodigoLei codigoLei = new CodigoLei();
        codigoLei.setCodigoLeiID(cursor.getInt(cursor.getColumnIndex(COLUMN_CODIGOLEI_ID)));
        codigoLei.setCodigoLei(cursor.getString(cursor.getColumnIndex(COLUMN_CODIGO)));
        codigoLei.setDescricao(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRICAO)));
        codigoLei.setValorInfracao(cursor.getFloat(cursor.getColumnIndex(COLUMN_VALORINFRACAO)));

        return codigoLei;
    }

}
