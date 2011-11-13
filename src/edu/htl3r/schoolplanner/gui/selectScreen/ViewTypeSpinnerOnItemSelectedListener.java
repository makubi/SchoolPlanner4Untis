/* SchoolPlanner4Untis - Android app to manage your Untis timetable
    Copyright (C) 2011  Mathias Kub <mail@makubi.at>
			Sebastian Chlan <sebastian@schoolplanner.at>
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.htl3r.schoolplanner.gui.selectScreen;

import java.util.List;

import org.springframework.util.Assert;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.gui.BundleConstants;

public class ViewTypeSpinnerOnItemSelectedListener implements OnItemSelectedListener {

	private Activity parent;
	private Intent intent;
	private List<? extends ViewType> list;
	private SpinnerMemory spinnerMemory;
	
	/** Wird benoetigt, damit beim Laden der Activity nicht automatisch die Action ausgefuehrt wird, da die Klasse {@link Spinner} bzw. der {@link OnItemSelectedListener} schon zu diesem Zeitpunkt gecalled werden. */
	private int lastSelectedPosition = -1;

	public ViewTypeSpinnerOnItemSelectedListener(Activity parent, Intent intent, List<? extends ViewType> list, SpinnerMemory spinnerMemory) {
		this.parent = parent;
		this.intent = intent;
		this.list = list;
		this.spinnerMemory = spinnerMemory;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
		// Hack, damit beim Initialisieren des Spinners nicht automatisch eine Action ausgefuehrt wird		
		if (lastSelectedPosition > -1 && lastSelectedPosition != position) {			
			fireEvent(position);
		}
		
		lastSelectedPosition = position;
	}

	public ViewType getViewType(int position) {
		return list.get(position);
	}
	
	public void fireEvent(int position) {
		ViewType item = getViewType(position);
		// Falls kein SpinnerMemory verwendet wird
		if(spinnerMemory != null) spinnerMemory.setSelectedViewType(item);
		
		Bundle bundle = new Bundle();
		bundle.putSerializable(BundleConstants.SELECTED_VIEW_TYPE, item);
		intent.putExtras(bundle);
		this.parent.startActivity(intent);
	}
	
	/**
	 * Fires event but does not remember position.
	 * @param viewTypeId ID of the ViewType, retrieved via {@link ViewType#getId()}.
	 */
	public void fireEventByIdAndDontRemember(int viewTypeId) {
		ViewType item = null;
		for(ViewType viewType : list) {
			if(viewType.getId() == viewTypeId) {
				item = viewType;
				break;
			}
		}
		Assert.notNull(item, "ViewType for ID "+viewTypeId+" not found. Unable to continue.");
		
		Bundle bundle = new Bundle();
		bundle.putSerializable(BundleConstants.SELECTED_VIEW_TYPE, item);
		intent.putExtras(bundle);
		this.parent.startActivity(intent);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {}
	
}
