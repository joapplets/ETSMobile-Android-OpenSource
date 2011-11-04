package com.applets;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;

import com.applets.baseactivity.BaseExpendableListActivity;
import com.applets.models.DirectoryGroup;
import com.applets.models.Person;

public class DirectoryActivity extends BaseExpendableListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	// We'll define a custom screen layout here (the one shown above), but
	// typically, you could just use the standard ListActivity layout.
	setContentView(R.layout.bottin_list);
	createActionBar("Bottin", R.id.bottin_actionbar);

	DirectoryGroup directory = null;
	try {

	    System.out.println(getEventsFromAnXML(this));

	    directory = fetchDataFromXML(this);

	} catch (XmlPullParserException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	/*
	 * // Query for all people contacts using the Contacts.People
	 * convenience class. // Put a managed wrapper around the retrieved
	 * cursor so we don't have to worry about // requerying or closing it as
	 * the activity changes state. mCursor =
	 * this.getContentResolver().query(People.CONTENT_URI, null, null, null,
	 * null); startManagingCursor(mCursor);
	 * 
	 * // Now create a new list adapter bound to the cursor. //
	 * SimpleListAdapter is designed for binding to a Cursor. ListAdapter
	 * adapter = new SimpleCursorAdapter( this, // Context.
	 * android.R.layout.two_line_list_item, // Specify the row template to
	 * use (here, two columns bound to the two retrieved cursor rows).
	 * mCursor, // Pass in the cursor to bind to. new String[] {People.NAME,
	 * People.COMPANY}, // Array of cursor columns to bind to. new int[]
	 * {android.R.id.text1, android.R.id.text2}); // Parallel array of which
	 * template objects to bind to those columns.
	 * 
	 * // Bind to our new adapter. setListAdapter(adapter);
	 */

    }

    private String getEventsFromAnXML(Activity activity)
	    throws XmlPullParserException, IOException {

	DirectoryGroup root, department, team;

	StringBuffer stringBuffer = new StringBuffer();
	Resources res = activity.getResources();
	XmlResourceParser xpp = res.getXml(R.xml.directoryentries);
	xpp.next();
	int eventType = xpp.getEventType();

	root = new DirectoryGroup("root", null, null, null, null);
	while (eventType != XmlPullParser.END_DOCUMENT) {

	    if (eventType == XmlPullParser.START_DOCUMENT) {
		stringBuffer.append("--- Start XML ---");
	    } else if (eventType == XmlPullParser.START_TAG) {
		// System.out.println(xpp.getProperty("name"));
		stringBuffer.append("\nSTART_TAG: " + xpp.getName());
	    } else if (eventType == XmlPullParser.END_TAG) {
		stringBuffer.append("\nEND_TAG: " + xpp.getName());
	    } else if (eventType == XmlPullParser.TEXT) {
		stringBuffer.append("\nTEXT: " + xpp.getText());
	    }

	    if (eventType == XmlPullParser.START_TAG
		    && xpp.getName().equals("department")) {

		// eventType = xpp.next();
		// System.out.println(xpp.nextText());
		// eventType = xpp.next();

		// department = new
		// DirectoryGroup(xpp.nextText(),null,null,null,null);
		/*
		 * while(!(eventType == XmlPullParser.END_TAG &&
		 * xpp.getName().equals("department"))) {
		 * 
		 * team= new DirectoryGroup(xpp.nextText(),null,null,null,null);
		 * 
		 * while(!(eventType == XmlPullParser.END_TAG &&
		 * xpp.getName().equals("team"))) {
		 * 
		 * 
		 * 
		 * // team.add(new
		 * Person(xpp.nextText(),xpp.nextText(),xpp.nextText
		 * (),xpp.nextText
		 * (),xpp.nextText(),xpp.nextText(),xpp.nextText()
		 * ,xpp.nextText()));
		 * 
		 * eventType = xpp.next(); } department.add(team);
		 * 
		 * eventType = xpp.next(); }
		 */
		// root.add(department);
	    }

	    eventType = xpp.next();
	}
	stringBuffer.append("\n--- End XML ---");

	System.out.println(stringBuffer.toString());

	return stringBuffer.toString();
    }

    public DirectoryGroup fetchDataFromXML(Activity activity)
	    throws XmlPullParserException, IOException {

	DirectoryGroup root = null, department = null, team = null;

	Person p = null;
	Resources res = activity.getResources();
	XmlResourceParser xpp = res.getXml(R.xml.directoryentries);
	xpp.next();
	int eventType = xpp.getEventType();

	while (eventType != XmlPullParser.END_DOCUMENT) {
	    String name = null;
	    switch (eventType) {
	    case XmlPullParser.START_DOCUMENT:

		root = new DirectoryGroup("root", null, null, null, null);
		break;
	    case XmlPullParser.START_TAG:
		name = xpp.getName();
		if (name.equalsIgnoreCase("DEPARTMENT"))
		    department = new DirectoryGroup(xpp.getAttributeValue(null,
			    "name"), null, null, null, null);
		else if (name.equalsIgnoreCase("TEAM"))
		    team = new DirectoryGroup(xpp.getAttributeValue(null,
			    "name"), null, null, null, null);
		else if (name.equalsIgnoreCase("PERSON"))
		    p = new Person();
		else {
		    if (name.equalsIgnoreCase("LASTNAME")) {
			p.setLastName(xpp.nextText());

		    } else if (name.equalsIgnoreCase("FIRSTNAME")) {
			p.setFirstName(xpp.nextText());
		    } else if (name.equalsIgnoreCase("PHONENUMBER")) {
			p.setPhoneNumber(xpp.nextText());
		    } else if (name.equalsIgnoreCase("FAX")) {
			p.setFax(xpp.nextText());
		    } else if (name.equalsIgnoreCase("EMAIL")) {
			p.setEmail(xpp.nextText());
		    } else if (name.equalsIgnoreCase("TITLE")) {
			p.setTitle(xpp.nextText());
		    } else if (name.equalsIgnoreCase("WORK_DEPARTMENT")) {
			p.setDepartment(xpp.nextText());
		    } else if (name.equalsIgnoreCase("ROOM")) {
			p.setRoom(xpp.nextText());
		    }

		}
		break;
	    case XmlPullParser.END_TAG:
		name = xpp.getName();
		if (name.equalsIgnoreCase("PERSON"))
		    team.add(p);
		else if (name.equalsIgnoreCase("TEAM"))
		    department.add(team);
		else if (name.equalsIgnoreCase("DEPARTMENT"))
		    root.add(department);

		break;
	    }
	    eventType = xpp.next();
	}

	System.out.println(root.toString());

	return root;
    }
}
