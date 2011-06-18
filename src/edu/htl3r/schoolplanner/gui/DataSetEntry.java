package edu.htl3r.schoolplanner.gui;

import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DataSetEntry extends View{

	private Context context;
	
	private TableLayout tableLayout;
	private Map<String, String> dataEntry;
	
	public DataSetEntry(Context context) {
		super(context);
		this.context = context;
	}
	
	/**
	 * Zeichnet den Datensatz
	 * Vorher muessen das TableLayout und die Daten gesetzt werden.
	 */
	public void draw() {		
		TextView name = new TextView(context);
		name.setTextSize(18);
		name.setText(dataEntry.get("name"));
		name.setGravity(Gravity.LEFT);
		name.setPadding(1, 0, 0, 0);
		
		TextView url = new TextView(context);
		url.setTextSize(14);
		url.setText(dataEntry.get("url"));
		url.setGravity(Gravity.LEFT);
		url.setPadding(3, 0, 0, 0);	
		
		TextView user = new TextView(context);
		user.setTextSize(14);
		user.setText(dataEntry.get("school"));
		user.setGravity(Gravity.CENTER_HORIZONTAL);
		user.setPadding(0, 0, 0, 0);
		
		TextView password = new TextView(context);
		password.setTextSize(14);
		password.setText(dataEntry.get("user"));
		password.setGravity(Gravity.RIGHT);
		password.setPadding(0, 0, 3, 0);
		
		TableRow nameRow = new TableRow(context);
		nameRow.addView(name);
		
		TableRow dataRow = new TableRow(context);
		dataRow.addView(url);
		dataRow.addView(user);
		dataRow.addView(password);
		

		View ruler = new View(context);
		ruler.setBackgroundColor(Color.parseColor("#FF909090"));
		
		tableLayout.addView(ruler, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 2));
		tableLayout.addView(nameRow, new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
				TableRow.LayoutParams.WRAP_CONTENT));
		tableLayout.addView(dataRow, new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
				TableRow.LayoutParams.WRAP_CONTENT));
	}

	/**
	 * Setze das TableLayout, zu dem der Eintrag hinzugefuegt werden soll.
	 * @param tableLayout TableLayout, zu dem der Eintrag hinzugefuegt werden soll
	 */
	public void setTableLayout(TableLayout tableLayout) {
		this.tableLayout = tableLayout;
	}

	/**
	 * Daten, die Verwendet werden sollen
	 * 'name' = Name des Eintrags
	 * 'url' = URL des Servers
	 * 'school' = Verwendeter Schulenname
	 * 'user' = Benutzername
	 * @param dataEntry Daten als Map, die verwendet werden sollen
	 */
	public void setDataEntry(Map<String, String> dataEntry) {
		this.dataEntry = dataEntry;
	}
	
	

}
