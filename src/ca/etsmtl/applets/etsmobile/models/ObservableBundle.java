package ca.etsmtl.applets.etsmobile.models;

import java.util.Observable;

public class ObservableBundle extends Observable{

	private Object content;
	
	public ObservableBundle(){}
	
	public ObservableBundle(Object content){
		setContent(content);
	}
	
	public void setContent(Object content){
		this.content = content;
		setChanged();
		notifyObservers(this.content);
	}
}
