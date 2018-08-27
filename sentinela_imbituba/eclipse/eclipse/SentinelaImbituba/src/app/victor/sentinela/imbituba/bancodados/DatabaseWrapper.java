package app.victor.sentinela.imbituba.bancodados;


import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseWrapper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseWrapper";

    private static final String DATABASE_NAME = "sentinela.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseWrapper instance;
    public Context context;

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private DatabaseWrapper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static boolean doesDatabaseExist(Context context) {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }

    public static DatabaseWrapper getInstance(Context context) {

        // Use the application context, which will ensure that you 
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (instance == null) {
            instance = new DatabaseWrapper(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Called if the database named DATABASE_NAME doesn't exist in order to create it.
     */

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "Creating database [" + DATABASE_NAME + " v." + DATABASE_VERSION + "]...");
        sqLiteDatabase.execSQL(TerrenoORM.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(CodigoLeiORM.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(UsuarioORM.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(InfracaoORM.SQL_CREATE_TABLE);
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase sqLiteDatabase = super.getWritableDatabase();
        return sqLiteDatabase;
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    /**
     * Called when the DATABASE_VERSION is increased.
     */

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.i(TAG, "Upgrading database ["+DATABASE_NAME+" v." + oldVersion+"] to ["+DATABASE_NAME+" v." + newVersion+"]...");
        //sqLiteDatabase.execSQL(UsuarioORM.SQL_DROP_TABLE);  
        onCreate(sqLiteDatabase);
    }

    public void closeDB() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
            sqLiteDatabase.close();
        Log.v("Debug BD", "database "+DATABASE_NAME+" fechado!");

    }

}
