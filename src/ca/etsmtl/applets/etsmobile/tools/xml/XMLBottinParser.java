package ca.etsmtl.applets.etsmobile.tools.xml;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import ca.etsmtl.applets.etsmobile.models.BottinEntry;

public class XMLBottinParser extends XMLAppletsHandler {

	public XMLBottinParser(final Context context) {
		super(context);
	}

	@Override
	public Object getData() {
		final List<BottinEntry> list = new ArrayList<BottinEntry>();

		return list;
	}

}
