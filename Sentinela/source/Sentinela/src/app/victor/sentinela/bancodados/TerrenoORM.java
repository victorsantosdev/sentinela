package app.victor.sentinela.bancodados;

import java.util.ArrayList;
import java.util.List;

import app.victor.sentinela.bdclasses.Terreno;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TerrenoORM {

    private static final String TAG = "TerrenoORM";

    private static final String TABLE_NAME = "terreno";

    private static final String COMMA_SEP = ", ";

    private static final String COLUMN_TERRENO_ID_TYPE = "INTEGER PRIMARY KEY";
    private static final String COLUMN_TERRENO_ID = "terreno_id";

    private static final String COLUMN_PROPRIETARIO_TYPE = "TEXT NOT NULL";
    private static final String COLUMN_PROPRIETARIO = "proprietario";

    private static final String COLUMN_ENDERECO_TYPE = "TEXT NOT NULL";
    private static final String COLUMN_ENDERECO = "endereco";

    private static final String COLUMN_NUMERO_TYPE = "INTEGER NOT NULL";
    private static final String COLUMN_NUMERO = "numero";

    private static final String COLUMN_BAIRRO_TYPE = "TEXT NOT NULL";
    private static final String COLUMN_BAIRRO = "bairro";

    private static final String COLUMN_CIDADE_TYPE = "TEXT NOT NULL";
    private static final String COLUMN_CIDADE = "cidade";

    private static final String COLUMN_ESTADO_TYPE = "TEXT NOT NULL";
    private static final String COLUMN_ESTADO = "estado";

    private static final String COLUMN_TOPOGRAFIA_TYPE = "TEXT NOT NULL";
    private static final String COLUMN_TOPOGRAFIA = "topografia";

    private static final String COLUMN_AREA_TYPE = "REAL NOT NULL";
    private static final String COLUMN_AREA = "area";

    private static final String COLUMN_CONFIGURACAO_TYPE = "TEXT NOT NULL";
    private static final String COLUMN_CONFIGURACAO = "configuracao";

    private static final String COLUMN_SITUACAO_CADASTRAL_TYPE = "TEXT NOT NULL";
    private static final String COLUMN_SITUACAO_CADASTRAL = "situacao_cadastral";
    
    private static final String COLUMN_FOTOPATH_TYPE = "TEXT NOT NULL";
    private static final String COLUMN_FOTOPATH = "foto_path";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_TERRENO_ID + " " + COLUMN_TERRENO_ID_TYPE + COMMA_SEP
            + COLUMN_PROPRIETARIO + " " + COLUMN_PROPRIETARIO_TYPE + COMMA_SEP + COLUMN_ENDERECO + " " + COLUMN_ENDERECO_TYPE + COMMA_SEP + COLUMN_NUMERO
            + " " + COLUMN_NUMERO_TYPE + COMMA_SEP + COLUMN_BAIRRO + " " + COLUMN_BAIRRO_TYPE + COMMA_SEP + COLUMN_CIDADE + " " + COLUMN_CIDADE_TYPE + COMMA_SEP + COLUMN_ESTADO + " "
            + COLUMN_ESTADO_TYPE + COMMA_SEP + COLUMN_TOPOGRAFIA + " " + COLUMN_TOPOGRAFIA_TYPE + COMMA_SEP + COLUMN_AREA + " " + COLUMN_AREA_TYPE + COMMA_SEP
            + COLUMN_CONFIGURACAO + " " + COLUMN_CONFIGURACAO_TYPE + COMMA_SEP + COLUMN_SITUACAO_CADASTRAL + " " + COLUMN_SITUACAO_CADASTRAL_TYPE + COMMA_SEP + COLUMN_FOTOPATH + " " + COLUMN_FOTOPATH_TYPE + ")";

    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static void insertTerreno(Context context, Terreno terreno) {

        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getWritableDatabase();

        ContentValues values = postToContentValues(terreno);
        long terrenoID = database.insert(TerrenoORM.TABLE_NAME, "null", values);
        Log.i(TAG, "Inserted new Terreno with ID: " + terrenoID);

        database.close(); // whenever you get a readable/writable db you should
        // close it after using
    }

    /**
     * Packs a Post object into a ContentValues map for use with SQL inserts.
     */
    private static ContentValues postToContentValues(Terreno terreno) {
        ContentValues values = new ContentValues();
        values.put(TerrenoORM.COLUMN_TERRENO_ID, terreno.getTerreno_id());
        values.put(TerrenoORM.COLUMN_PROPRIETARIO, terreno.getProprietario());
        values.put(TerrenoORM.COLUMN_ENDERECO, terreno.getEndereco());
        values.put(TerrenoORM.COLUMN_NUMERO, terreno.getNumero());
        values.put(TerrenoORM.COLUMN_BAIRRO, terreno.getBairro());
        values.put(TerrenoORM.COLUMN_CIDADE, terreno.getCidade());
        values.put(TerrenoORM.COLUMN_ESTADO, terreno.getEstado());
        values.put(TerrenoORM.COLUMN_TOPOGRAFIA, terreno.getTopografia());
        values.put(TerrenoORM.COLUMN_AREA, terreno.getArea());
        values.put(TerrenoORM.COLUMN_CONFIGURACAO, terreno.getConfiguracao());
        values.put(TerrenoORM.COLUMN_SITUACAO_CADASTRAL, terreno.getSituacao_cadastral());
        values.put(TerrenoORM.COLUMN_FOTOPATH, terreno.getFotoPath());

        return values;
    }

    public static List<Terreno> getTerrenos(Context context) {
        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TerrenoORM.TABLE_NAME, null);

        Log.i(TAG, "Loaded " + cursor.getCount() + " Terrenos...");
        List<Terreno> terrenosList = new ArrayList<Terreno>();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Terreno terreno = cursorToTerreno(cursor);
                terrenosList.add(terreno);
                cursor.moveToNext();
            }
            Log.i(TAG, "Terrenos loaded successfully.");
        }

        cursor.close();
        database.close(); // whenever you get a readable/writable db you should
        // close it after using

        return terrenosList;
    }

    public static List<Terreno> getTerrenosfromCidade(Context context, String cidade) {
        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TerrenoORM.TABLE_NAME + " WHERE " + COLUMN_CIDADE + "=?", new String[] { cidade });

        Log.i(TAG, "Loaded " + cursor.getCount() + " Terrenos...");
        List<Terreno> terrenosList = new ArrayList<Terreno>();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Terreno terreno = cursorToTerreno(cursor);
                terrenosList.add(terreno);
                cursor.moveToNext();
            }
            Log.i(TAG, "Terrenos loaded successfully.");
        }

        cursor.close();
        database.close(); // whenever you get a readable/writable db you should
        // close it after using

        return terrenosList;
    }

    public static Terreno getTerrenofromID(Context context, int terreno_id) {
        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TerrenoORM.TABLE_NAME + " WHERE " + COLUMN_TERRENO_ID + "=?", new String[] { Integer.toString(terreno_id) });
        Terreno terreno = null;

        if (cursor != null && cursor.getCount() > 0) {
            // só tem uma pergunta porque o campo é UNIQUE no BD
            cursor.moveToFirst();
            terreno = cursorToTerreno(cursor);
            Log.v("Debug terreno existente BD", "terreno encontrado == : " + "terreno_endereco: " + terreno.getEndereco() + "  terreno_cidade: " + terreno.getCidade() );
            cursor.close();
            database.close();
        } 
        return terreno;
    }
    
    
    public static List<String> getCidadeTerrenos(Context context) throws SQLException {
        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT DISTINCT cidade FROM " + TerrenoORM.TABLE_NAME, null);
        Log.i(TAG, "Loaded " + cursor.getCount() + " Terrenos:Cidade...");
        List<String> terrenosCidade = new ArrayList<String>();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Log.i(TAG, "Cidade encontrada: " + cursor.getString(cursor.getColumnIndex(COLUMN_CIDADE)));
                terrenosCidade.add(cursor.getString(cursor.getColumnIndex(COLUMN_CIDADE)));
                cursor.moveToNext();
            }
            Log.i(TAG, "Cidades loaded successfully.");
        }

        cursor.close();
        database.close(); // whenever you get a readable/writable db you should
        // close it after using

        for (int i = 0; i < terrenosCidade.size(); i++) {
            Log.i(TAG, "Cidade na lista: " + terrenosCidade.get(i));
        }

        return terrenosCidade;

    }

    /**
     * Populates a Post object with data from a Cursor
     * 
     * @param cursor
     * @return
     */
    private static Terreno cursorToTerreno(Cursor cursor) {
        Terreno terreno = new Terreno();
        terreno.setTerreno_id(cursor.getInt(cursor.getColumnIndex(COLUMN_TERRENO_ID)));
        terreno.setProprietario(cursor.getString(cursor.getColumnIndex(COLUMN_PROPRIETARIO)));
        terreno.setEndereco(cursor.getString(cursor.getColumnIndex(COLUMN_ENDERECO)));
        terreno.setNumero(cursor.getInt(cursor.getColumnIndex(COLUMN_NUMERO)));
        terreno.setBairro(cursor.getString(cursor.getColumnIndex(COLUMN_BAIRRO)));
        terreno.setCidade(cursor.getString(cursor.getColumnIndex(COLUMN_CIDADE)));
        terreno.setEstado(cursor.getString(cursor.getColumnIndex(COLUMN_ESTADO)));
        terreno.setTopografia(cursor.getString(cursor.getColumnIndex(COLUMN_TOPOGRAFIA)));
        terreno.setArea(cursor.getFloat(cursor.getColumnIndex(COLUMN_AREA)));
        terreno.setConfiguracao(cursor.getString(cursor.getColumnIndex(COLUMN_CONFIGURACAO)));
        terreno.setSituacao_cadastral(cursor.getString(cursor.getColumnIndex(COLUMN_SITUACAO_CADASTRAL)));
        terreno.setFotoPath(cursor.getString(cursor.getColumnIndex(COLUMN_FOTOPATH)));

        return terreno;
    }

}