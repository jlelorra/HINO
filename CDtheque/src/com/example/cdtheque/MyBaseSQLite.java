package com.example.cdtheque;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyBaseSQLite  extends SQLiteOpenHelper{
	
	private static final String TABLE_CD = "table_cd";
	private static final String COL_ID = "ID";
	private static final String ARTIST = "ARTIST";
	static final String ALBUM = "ALBUM";
	private static final String YEAR = "YEAR";
	private static final String CONTACT = "CONTACT";
	private static final String RATE = "RATE";
	
	private static final String CREATE_BDD = "CREATE TABLE " + TABLE_CD + " ("
			+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ARTIST + " TEXT NOT NULL, "
			+ ALBUM + " TEXT NOT NULL, " + YEAR + " TEXT NOT NULL, " + CONTACT + " TEXT NOT NULL, " + RATE + " TEXT NOT NULL);";

	
	public MyBaseSQLite(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
		//on créé la table à partir de la requête écrite dans la variable CREATE_BDD
		db.execSQL(CREATE_BDD);
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//On peut fait ce qu'on veut ici moi j'ai décidé de supprimer la table et de la recréer
		//comme ça lorsque je change la version les id repartent de 0
		db.execSQL("DROP TABLE " + TABLE_CD + ";");
		onCreate(db);
	}
	
	
}
