package com.example.cdtheque;

public class CD {
	
	private int id;
	private String artist;
	private String album;
	private String year;
	private String contact;
	private float rate;
	
	public CD() {}
	
	public CD(String artist, String album, String year, String contact,float rate) {
		super();
		this.artist = artist;
		this.album = album;
		this.year = year;
		this.contact = contact;
		this.rate = rate;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public String getAlbum() {
		return album;
	}
	
	public void setAlbum(String album) {
		this.album = album;
	}
	
	public String getYear() {
		return year;
	}
	
	public void setYear(String year) {
		this.year = year;
	}
	
	public String getContact() {
		return contact;
	}
	
	public void setContact(String contact) {
		this.contact = contact;
	}
	
	public float getRate() {
		return rate;
	}
	
	public void setRate(float rate) {
		this.rate = rate;
	}
	
}
