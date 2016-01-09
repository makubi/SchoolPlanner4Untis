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
package edu.htl3r.schoolplanner.gui.welcomeScreen;

import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.tani.app.ui.IconContextMenu;

import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.gui.WelcomeScreen;

public class WelcomeScreenContextMenu extends IconContextMenu{

	private WelcomeScreen parentActivity;
	private int context_id;
	private ListView listView;
	
	private final int CONTEXT_MENU_EDIT = 1;
	private final int CONTEXT_MENU_REMOVE = 2;
	private final int CONTEXT_MENU_RESYNC_MASTERDATA = 3;
	
	public WelcomeScreenContextMenu(WelcomeScreen parent, int id) {
		super(parent, id);
		this.parentActivity = parent;
		this.context_id = id;
	}
	
	public void init() {
		setOnClickListener(new IconContextMenu.IconContextMenuOnClickListener() {

			@Override
			public void onClick(int menuId) {
				switch(menuId) {
				case CONTEXT_MENU_EDIT:
					parentActivity.editLoginSet((int) getSelectedItem());
					break;
				case CONTEXT_MENU_REMOVE:
					parentActivity.removeLoginSet((int) getSelectedItem());
					break;
				case CONTEXT_MENU_RESYNC_MASTERDATA:
					parentActivity.resyncMasterData((int) getSelectedItem());
					break;
				}
			}
			
		});
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				setSelectedItem(id);
				parentActivity.showDialog(context_id);
				return true;
			}
		});
		
		Resources resources = parentActivity.getResources();
		
		addItem(resources, parentActivity.getString(R.string.menu_edit_login_set), R.drawable.ic_menu_edit, CONTEXT_MENU_EDIT);
		addItem(resources, parentActivity.getString(R.string.menu_remove_login_set), R.drawable.ic_menu_delete,  CONTEXT_MENU_REMOVE);
		addItem(resources, parentActivity.getString(R.string.menu_resync_masterdata), R.drawable.ic_menu_refresh, CONTEXT_MENU_RESYNC_MASTERDATA);
	}
	
	public void setListView(ListView listView) {
		this.listView = listView;
	}

}
