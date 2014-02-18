package com.JMJ.commonTools;

import java.io.File;
import android.annotation.SuppressLint;
import android.os.Environment;

@SuppressLint("DefaultLocale")
public class CommonTools {
	
	public static File getExternalSDCardDirectory(){
		
	    File innerDir = Environment.getExternalStorageDirectory();
	    File rootDir = innerDir.getParentFile();
	    File firstExtSdCard = innerDir;
	    File[] files = rootDir.getParentFile().listFiles();
	    for (File file : files) {
	    	if( file.isDirectory()){
		       if ((file.getAbsolutePath().toString().toLowerCase().contains("ext") && file.getAbsolutePath().toString().toLowerCase().contains("sd")) || file.getAbsolutePath().toString().toLowerCase().contains("sdcard2") ){
		            firstExtSdCard = file;
		            break;
		        }else{
		        	File[] files2 = file.listFiles();
		        	if(files2!=null){
			        	 for (File file2 : files2) {
						    	if( file2.isDirectory()){
							       if ((file2.getAbsolutePath().toString().toLowerCase().contains("ext") && file2.getAbsolutePath().toString().toLowerCase().contains("sd")) || file2.getAbsolutePath().toString().toLowerCase().contains("sdcard2") || (file2.getAbsolutePath().toString().toLowerCase().contains("mnt") && file2.getAbsolutePath().toString().toLowerCase().contains("sdcard")) ) {
							            firstExtSdCard = file2;
							            break;
							        }else{
							        	File[] files3 = file2.listFiles();
							        	if(files3!=null){
								        	 for (File file3 : files3) {
											    	if( file2.isDirectory()){
												       if ((file3.getAbsolutePath().toString().toLowerCase().contains("ext") && file3.getAbsolutePath().toString().toLowerCase().contains("sd")) || file3.getAbsolutePath().toString().toLowerCase().contains("sdcard2")) {
												            firstExtSdCard = file3;
												            break;
												        }
											    	}
								        	 }
							        	}
							        }
						    	}
			        	 }
		        	}
		        }
	    	}
	    }
	    return firstExtSdCard;
	}

	@SuppressLint("DefaultLocale")
	public static File getDownloadSDCardDirectory(){
		
	    File innerDir = Environment.getExternalStorageDirectory();
	    File rootDir = innerDir.getParentFile();
	    File firstExtSdCard = null ;
	    File[] files = rootDir.getParentFile().listFiles();
	    for (File file : files) {
	    	if( file.isDirectory()){
		       if ((file.getAbsolutePath().toString().toLowerCase().contains("download") && file.getAbsolutePath().toString().toLowerCase().contains("sd")) || (file.getAbsolutePath().toString().toLowerCase().contains("download") && file.getAbsolutePath().toString().toLowerCase().contains("storage")) ){
		            firstExtSdCard = file;
		            break;
		        }else{
		        	File[] files2 = file.listFiles();
		        	if(files2!=null){
			        	 for (File file2 : files2) {
						    	if( file2.isDirectory()){
							       if ((file2.getAbsolutePath().toString().toLowerCase().contains("download") && file2.getAbsolutePath().toString().toLowerCase().contains("sd")) || (file2.getAbsolutePath().toString().toLowerCase().contains("download") && file2.getAbsolutePath().toString().toLowerCase().contains("storage"))) {
							            firstExtSdCard = file2;
							            break;
							        }else{
							        	File[] files3 = file2.listFiles();
							        	if(files3!=null){
								        	 for (File file3 : files3) {
											    	if( file3.isDirectory()){
												       if ((file3.getAbsolutePath().toString().toLowerCase().contains("download") && file3.getAbsolutePath().toString().toLowerCase().contains("sd")) || (file3.getAbsolutePath().toString().toLowerCase().contains("download") && file3.getAbsolutePath().toString().toLowerCase().contains("storage"))) {
												            firstExtSdCard = file3;
												            break;
												        }
											    	}
								        	 }
							        	}
							        }
						    	}
			        	 }
		        	}
		        }
	    	}
	    }
	    return firstExtSdCard;
	}			
	
	public static File getBlueToothSDCardDirectory(){
		
	    File innerDir = Environment.getExternalStorageDirectory();
	    File rootDir = innerDir.getParentFile();
	    File firstExtSdCard=null;
	    File[] files = rootDir.getParentFile().listFiles();
	    for (File file : files) {
	    	if( file.isDirectory()){
		       if ((file.getAbsolutePath().toString().toLowerCase().contains("bluetooth") && file.getAbsolutePath().toString().toLowerCase().contains("sd")) || (file.getAbsolutePath().toString().toLowerCase().contains("bluetooth") && file.getAbsolutePath().toString().toLowerCase().contains("storage")) ){
		            firstExtSdCard = file;
		            break;
		        }else{
		        	File[] files2 = file.listFiles();
		        	if(files2!=null){
			        	 for (File file2 : files2) {
						    	if( file2.isDirectory()){
							       if ((file2.getAbsolutePath().toString().toLowerCase().contains("bluetooth") && file2.getAbsolutePath().toString().toLowerCase().contains("sd")) || (file2.getAbsolutePath().toString().toLowerCase().contains("bluetooth") && file2.getAbsolutePath().toString().toLowerCase().contains("storage"))) {
							            firstExtSdCard = file2;
							            break;
							        }else{
							        	File[] files3 = file2.listFiles();
							        	if(files3!=null){
								        	 for (File file3 : files3) {
											    	if( file3.isDirectory()){
												       if ((file3.getAbsolutePath().toString().toLowerCase().contains("bluetooth") && file3.getAbsolutePath().toString().toLowerCase().contains("sd")) || (file3.getAbsolutePath().toString().toLowerCase().contains("bluetooth") && file3.getAbsolutePath().toString().toLowerCase().contains("storage"))) {
												            firstExtSdCard = file3;
												            break;
												        }
											    	}
								        	 }
							        	}
							        }
						    	}
			        	 }
		        	}
		        }
	    	}
	    }
	    return firstExtSdCard;
	}

}
