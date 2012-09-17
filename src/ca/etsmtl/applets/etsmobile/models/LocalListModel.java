package ca.etsmtl.applets.etsmobile.models;

import java.util.Vector;

import android.util.Log;

public class LocalListModel {
	
	private Vector <String> listLocaux;
	private static LocalListModel instance ; 
	
	private LocalListModel(){
		listLocaux = new Vector<String>();
	}
	public static LocalListModel getInstance(){
		if(instance == null){
			instance = new LocalListModel();
		}
		return instance;
	}
	
	public String[] getListLocaux(){
		String[] tabLocaux = new String[listLocaux.size()];
		for(int i=0; i<listLocaux.size() ;i++){
			tabLocaux[i] = (String) listLocaux.get(i);
		}
		return tabLocaux;
	}
	
	public void addLocal(String local){
		listLocaux.add(local);
	}
	
	public boolean checkLocal(String local){
		for(int i=0; i< listLocaux.size(); i++){
			if(listLocaux.get(i).equals(local)){
				return true;
			}
		}
		return false;
	}
}
