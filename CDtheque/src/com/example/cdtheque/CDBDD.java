package com.example.cdtheque;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CDBDD {

	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = "CD.db";
 
	private static final String TABLE_CD = "table_cd";
	private static final String COL_ID = "ID";
	private static final int NUM_COL_ID = 0;
	private static final String ARTIST = "ARTIST";
	private static final int NUM_ARTIST = 1;
	private static final String ALBUM = "ALBUM";
	private static final int NUM_ALBUM = 2;
	private static final String YEAR = "YEAR";
	private static final int NUM_YEAR = 3;
	private static final String CONTACT = "CONTACT";
	private static final int NUM_CONTACT = 4;
	private static final String RATE = "RATE";
	private static final int NUM_RATE = 5;
 
	private SQLiteDatabase bdd;
 
	private MyBaseSQLite maBaseSQLite;
 
	public CDBDD(Context context){
		//On créer la BDD et sa table
		maBaseSQLite = new MyBaseSQLite(context, NOM_BDD, null, VERSION_BDD);
	}
 
	public void open(){
		//on ouvre la BDD en écriture
		bdd = maBaseSQLite.getWritableDatabase();
	}
 
	public void close(){
		//on ferme l'accès à la BDD
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}
 
	public long insertCD(CD cd){
		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(ARTIST, cd.getArtist());
		values.put(ALBUM, cd.getAlbum());
		values.put(YEAR, cd.getYear());
		values.put(RATE, cd.getRate());
		Log.d("RATE", String.valueOf(cd.getRate()));
		if(cd.getContact()!=null)values.put(CONTACT, cd.getContact());
		else values.put(CONTACT, "FREE");
		//if(cd.getRate()!=0)values.put(RATE, cd.getRate());
		//else values.put(RATE, 0.0);
		//on insère l'objet dans la BDD via le ContentValues
		return bdd.insert(TABLE_CD, null, values);
	}
 
	public int updateCD(int id, CD cd){
		//La mise à jour d'un livre dans la BDD fonctionne plus ou moins comme une insertion
		//il faut simple préciser quelle livre on doit mettre à jour grâce à l'ID
		ContentValues values = new ContentValues();	
		values.put(ARTIST, cd.getArtist());
		values.put(ALBUM, cd.getAlbum());
		values.put(YEAR, cd.getYear());
		values.put(CONTACT, cd.getContact());
		values.put(RATE, cd.getRate());
		return bdd.update(TABLE_CD, values, COL_ID + " = " +id, null);
	}
 
	public int removeCDWithID(int id){
		//Suppression d'un livre de la BDD grâce à l'ID
		return bdd.delete(TABLE_CD, COL_ID + " = " +id, null);
	}
 
	public CD getCDWithAlbum(String Album){
		//Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
		Cursor c = bdd.query(TABLE_CD, new String[] {COL_ID, ARTIST, ALBUM , YEAR , CONTACT, RATE}, ALBUM + " LIKE \"" + Album +"\"", null, null, null, null);
		return cursorToCD(c);
	}
	
	public CD[] getAllAlbum(){

		Cursor c = bdd.query(TABLE_CD, null,null, null, null, null, null);
		if (c.getCount() == 0)
			return null;
		CD[] CDs= new CD[c.getCount()];
		int i = 0;
		c.moveToFirst();
		while(!c.isAfterLast()){
			CDs[i].setId(c.getInt(NUM_COL_ID));
			CDs[i].setArtist(c.getString(NUM_ARTIST));
			CDs[i].setAlbum(c.getString(NUM_ALBUM));
			CDs[i].setYear(c.getString(NUM_YEAR));
			CDs[i].setContact(c.getString(NUM_CONTACT));
			CDs[i].setRate(c.getFloat(NUM_RATE));
			Log.d("CD name",CDs[i].getAlbum());
			i++;
			
			c.moveToNext();
		}
		c.close();
		return CDs;
	}
	
	public Cursor getListOfAlbum(){
		//Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
		Cursor c = bdd.query(TABLE_CD, new String[] {COL_ID, ARTIST, ALBUM , YEAR , CONTACT, RATE}, null, null, null, null, null);
		return c;
	}
 
	
	
	
	
	//Cette méthode permet de convertir un cursor en un livre
	private CD cursorToCD(Cursor c){
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		if (c.getCount() == 0)
			return null;
 
		//Sinon on se place sur le premier élément
		c.moveToFirst();
		//On créé un livre
		CD cd = new CD();
		//on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
		cd.setId(c.getInt(NUM_COL_ID));
		cd.setArtist(c.getString(NUM_ARTIST));
		cd.setAlbum(c.getString(NUM_ALBUM));
		cd.setYear(c.getString(NUM_YEAR));
		cd.setContact(c.getString(NUM_CONTACT));
		cd.setRate(c.getFloat(NUM_RATE));
		//On ferme le cursor
		c.close();
 
		//On retourne le livre
		return cd;
	}
	
}
