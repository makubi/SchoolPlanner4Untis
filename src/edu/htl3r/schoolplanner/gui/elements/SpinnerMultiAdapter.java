/* SchoolPlanner4Untis - Android app to manage your Untis timetable
    Copyright (C) 2011  Mathias Kub <mail@makubi.at>
						Gerald Schreiber <mail@gerald-schreiber.at>
						Philip Woelfel <philip@woelfel.at>
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

package edu.htl3r.schoolplanner.gui.elements;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import edu.htl3r.schoolplanner.R;

public class SpinnerMultiAdapter extends BaseAdapter implements OnCheckedChangeListener {

	private ArrayList<String> data;
	private boolean[] check;
	private Context cont;
	private CheckBox[] cbs;

	public SpinnerMultiAdapter(Context cont, ArrayList<String> data) {
		if (data == null || cont == null) {
			throw new IllegalArgumentException("The arguments MUST NOT be null!");
		}
		this.data = data;
		this.cont = cont;
		this.check = new boolean[data.size()];
		this.cbs = new CheckBox[data.size()];
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return (data.size() > position ? data.get(position) : null);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tv = new TextView(cont);
		tv.setTextColor(Color.BLACK);
		tv.setTextSize(cont.getResources().getDimension(R.dimen.multiadapter_fontsize));
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setText(data.get(position));
		return tv;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		RelativeLayout rl = new RelativeLayout(cont);
		

		TextView tv = new TextView(cont);
		tv.setTextColor(Color.BLACK);
		//tv.setBackgroundColor(Color.RED);
		tv.setTextSize(cont.getResources().getDimension(R.dimen.multiadapter_fontsize));
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setText(data.get(position));
		

		cbs[position] = new CheckBox(cont);
		cbs[position].setChecked(check[position]);
		cbs[position].setOnCheckedChangeListener(this);

		RelativeLayout.LayoutParams lptv = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
		lptv.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		lptv.addRule(RelativeLayout.CENTER_VERTICAL);

		RelativeLayout.LayoutParams lpcb = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		lpcb.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		rl.addView(tv, lptv);
		rl.addView(cbs[position], lpcb);
		return rl;
	}


	public void setChecked(int pos, boolean checked) {
		check[pos] = checked;
		if (cbs[pos] != null) {
			cbs[pos].setChecked(checked);
		}
	}

	public void setChecked(String value, boolean checked) {
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).equals(value)) {
				setChecked(i, checked);
			}
		}
	}

	public boolean isChecked(int pos) {
		return (check.length > pos) ? check[pos] : false;
	}

	public boolean isChecked(String value) {
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).equals(value)) {
				return isChecked(i);
			}
		}
		return false;
	}

	public ArrayList<Integer> getCheckedIndexes() {
		ArrayList<Integer> out = new ArrayList<Integer>();
		for (int i = 0; i < check.length; i++) {
			if (check[i]) {
				out.add(i);
			}
		}
		return out;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		for (int i = 0; i < cbs.length; i++) {
			if (cbs[i] == buttonView) {
				check[i] = isChecked;
			}
		}
	}

	
}
