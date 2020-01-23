package com.example.julionery.guia.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.julionery.guia.Models.User;
import com.example.julionery.guia.Models.Locais;

import java.util.ArrayList;
import java.util.List;

public class BancoDAO extends SQLiteOpenHelper {

    private static final String DB_NAME = "guia.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_USERS = "users";
    private static final String TABLE_LOCAIS = "locais";

    //COLUMN_NAMES_USER
    private static final String ROW_ID = "id";
    private static final String ROW_NAME = "name";
    private static final String ROW_USERNAME = "username";
    private static final String ROW_TOKEN = "token";
    private static final String ROW_EMAIL = "email";
    private static final String ROW_PHOTOURL = "photoURL";
    private static final String ROW_SUBTITULO = "subtitulo";

    //COLUMN_NAMES_LOCAIS
    private static final String ROW_TITULO = "titulo";
    private static final String ROW_DESCRICAO = "descricao";
    private static final String ROW_FOTO = "foto";
    private static final String ROW_LOCALIZACAO = "localizacao";
    private static final String ROW_VALOR = "valor";
    private static final String ROW_TEMPO_ESTIMADO = "tempo_estimado";
    private static final String ROW_GUIA = "guia";
    private static final String ROW_LATITUDE = "latitude";
    private static final String ROW_LONGITUDE = "longitude";

    public BancoDAO(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USERS_TABLE = "CREATE TABLE "
                + TABLE_USERS + "("
                + ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ROW_NAME + " TEXT,"
                + ROW_USERNAME + " TEXT,"
                + ROW_TOKEN + " TEXT,"
                + ROW_EMAIL + " TEXT,"
                + ROW_PHOTOURL + " TEXT,"
                + ROW_SUBTITULO + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_USERS_TABLE);

        String CREATE_LOCAIS_TABLE = "CREATE TABLE "
                + TABLE_LOCAIS + "("
                + ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ROW_TITULO + " TEXT,"
                + ROW_DESCRICAO + " TEXT,"
                + ROW_FOTO + " BLOB,"
                + ROW_LOCALIZACAO + " TEXT,"
                + ROW_VALOR + " DOUBLE,"
                + ROW_TEMPO_ESTIMADO + " TEXT,"
                + ROW_GUIA + " TEXT,"
                + ROW_LATITUDE + " DOUBLE,"
                + ROW_LONGITUDE + " DOUBLE)";
        sqLiteDatabase.execSQL(CREATE_LOCAIS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCAIS);
        onCreate(db);
    }

    public void createUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ROW_NAME, user.getName());
        values.put(ROW_USERNAME, user.getUsername());
        values.put(ROW_TOKEN, user.getToken());
        values.put(ROW_EMAIL, user.getEmail());
        values.put(ROW_PHOTOURL, user.getPhotoURL());
        values.put(ROW_SUBTITULO, user.getSubtitulo());

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public Cursor queryData(String sql){
        SQLiteDatabase database = this.getReadableDatabase();
        database.rawQuery(sql, null);
        return null;
    }

    public void createLocais(Locais local) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ROW_TITULO, local.getTitulo());
        values.put(ROW_DESCRICAO, local.getDescricao());
        values.put(ROW_FOTO, local.getFoto());
        values.put(ROW_LOCALIZACAO, local.getLocalizacao());
        values.put(ROW_VALOR, local.getValor());
        values.put(ROW_TEMPO_ESTIMADO, local.getTempo_estimado());
        values.put(ROW_GUIA, local.getGuia());
        values.put(ROW_LATITUDE, local.getLatitude());
        values.put(ROW_LONGITUDE, local.getLongitude());

        db.insert(TABLE_LOCAIS, null, values);
        db.close();
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<User>();

        String selectQuery = "SELECT * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(
                        0)));
                user.setName(cursor.getString(1));
                user.setUsername(cursor.getString(2));
                user.setToken(cursor.getString(3));
                user.setEmail(cursor.getString(4));
                user.setPhotoURL(cursor.getString(5));
                user.setSubtitulo(cursor.getString(6));
                // Adding contact to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        return userList;
    }

    public ArrayList<Locais> getAllLocais() {
        ArrayList<Locais> locaisList = new ArrayList<Locais>();

        String selectQuery = "SELECT * FROM " + TABLE_LOCAIS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Locais local = new Locais();
                local.setId(Integer.parseInt(cursor.getString(0)));
                local.setTitulo(cursor.getString(1));
                local.setDescricao(cursor.getString(2));
                local.setFoto(cursor.getBlob(3));
                local.setLocalizacao(cursor.getString(4));
                local.setValor(cursor.getDouble(5));
                local.setTempo_estimado(cursor.getString(6));
                local.setGuia(cursor.getString(7));
                local.setLatitude(cursor.getDouble(8));
                local.setLongitude(cursor.getDouble(9));
                locaisList.add(local);
            } while (cursor.moveToNext());
        }

        return locaisList;
    }


    public User retrieveUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[] {
                                ROW_ID,
                                ROW_NAME,
                                ROW_USERNAME,
                                ROW_TOKEN,
                                ROW_EMAIL,
                                ROW_PHOTOURL,
                                ROW_SUBTITULO
                },
                ROW_ID + "=?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null);
        User user = null;
        if (cursor != null) {
            cursor.moveToFirst();
            user = new User();
            user.setId(Integer.parseInt(cursor.getString(
                    0)));
            user.setName(cursor.getString(1));
            user.setUsername(cursor.getString(2));
            user.setToken(cursor.getString(3));
            user.setEmail(cursor.getString(4));
            user.setPhotoURL(cursor.getString(5));
            user.setSubtitulo(cursor.getString(6));
            return user;
        } else{
            throw new RuntimeException("Não existe esse Usuário!");
        }
    }

    public User retrieveUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[] {
                        ROW_ID,
                        ROW_NAME,
                        ROW_USERNAME,
                        ROW_TOKEN,
                        ROW_EMAIL,
                        ROW_PHOTOURL,
                        ROW_SUBTITULO
                },
                ROW_EMAIL + "=?",
                new String[] { String.valueOf(email) },
                null,
                null,
                null,
                null);
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            user = new User();
            user.setId(Integer.parseInt(cursor.getString(
                    0)));
            user.setName(cursor.getString(1));
            user.setUsername(cursor.getString(2));
            user.setToken(cursor.getString(3));
            user.setEmail(cursor.getString(4));
            user.setPhotoURL(cursor.getString(5));
            user.setSubtitulo(cursor.getString(6));
            return user;
        } else{
            return null;
        }
    }

    public Locais buscarLocalByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LOCAIS, new String[] { ROW_ID, ROW_TITULO, ROW_DESCRICAO, ROW_FOTO, ROW_LOCALIZACAO, ROW_VALOR, ROW_TEMPO_ESTIMADO, ROW_GUIA, ROW_LATITUDE, ROW_LONGITUDE  },ROW_ID + "=?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null);
        Locais local = null;
        if (cursor != null) {
            cursor.moveToFirst();
            local = new Locais();
            local.setId(Integer.parseInt(cursor.getString(0)));
            local.setTitulo(cursor.getString(1));
            local.setDescricao(cursor.getString(2));
            local.setFoto(cursor.getBlob(3));
            local.setLocalizacao(cursor.getString(4));
            local.setValor(cursor.getDouble(5));
            local.setTempo_estimado(cursor.getString(6));
            local.setGuia(cursor.getString(7));
            local.setLatitude(cursor.getDouble(8));
            local.setLongitude(cursor.getDouble(9));
            return local;
        } else{
            throw new RuntimeException("Não existe esse Locais!");
        }
    }


    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ROW_NAME, user.getName());
        values.put(ROW_USERNAME, user.getUsername());
        values.put(ROW_TOKEN, user.getToken());
        values.put(ROW_EMAIL, user.getEmail());
        values.put(ROW_PHOTOURL, user.getPhotoURL());
        values.put(ROW_SUBTITULO, user.getSubtitulo());

        // updating row
        return db.update(TABLE_USERS, values,
                ROW_ID + " = ?",
                new String[] { String.valueOf(user.getId()) });
    }

    public int updateLocais(Locais local) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ROW_TITULO, local.getTitulo());
        values.put(ROW_DESCRICAO, local.getTitulo());
        values.put(ROW_FOTO, local.getTitulo());
        values.put(ROW_LOCALIZACAO, local.getTitulo());
        values.put(ROW_VALOR, local.getTitulo());
        values.put(ROW_TEMPO_ESTIMADO, local.getTitulo());
        values.put(ROW_GUIA, local.getTitulo());
        values.put(ROW_LOCALIZACAO, local.getTitulo());
        values.put(ROW_LATITUDE, local.getTitulo());
        values.put(ROW_LONGITUDE, local.getTitulo());

        // updating row
        return db.update(TABLE_LOCAIS, values,
                ROW_ID + " = ?",
                new String[] { String.valueOf(local.getId()) });
    }


    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, ROW_ID + " = ?",
                new String[] { String.valueOf(user.getId()) });
        db.close();
    }

    public void deleteLocal(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOCAIS, ROW_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }
}