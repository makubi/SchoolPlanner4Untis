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

package edu.htl3r.schoolplanner.backend.preferences;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.Button;

public class ValidatedEditTextPreference extends EditTextPreference
{
    public ValidatedEditTextPreference(Context ctx, AttributeSet attrs, int defStyle)
    {
        super(ctx, attrs, defStyle);        
    }

    public ValidatedEditTextPreference(Context ctx, AttributeSet attrs)
    {
        super(ctx, attrs);                
    }

    private class EditTextWatcher implements TextWatcher
    {    
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count){}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int before, int count){}

        @Override
        public void afterTextChanged(Editable s)
        {        
            onEditTextChanged();
        }
    }
    EditTextWatcher m_watcher = new EditTextWatcher();

    /**
     * Return true in order to enable positive button or false to disable it.
     */
    protected boolean onCheckValue(String value)
    {        
    	try {
			int newCacheLifeTimeInHours = Integer.parseInt(value);
			return isNumberBetween(newCacheLifeTimeInHours, Settings.MIN_CACHE_LIFE_TIME, Settings.MAX_CACHE_LIFE_TIME);
		}
		catch (NumberFormatException e) {
			return false;
		}
    }

    private boolean isNumberBetween(int number, int min, int max) {
		return number >= min && number <= max;
	}

	protected void onEditTextChanged()
    {
        boolean enable = onCheckValue(getEditText().getText().toString());
        Dialog dlg = getDialog();
        if(dlg instanceof AlertDialog)
        {
            AlertDialog alertDlg = (AlertDialog)dlg;
            Button btn = alertDlg.getButton(AlertDialog.BUTTON_POSITIVE);
            btn.setEnabled(enable);                
        }
    }

    @Override
    protected void showDialog(Bundle state)
    {
        super.showDialog(state);

        getEditText().removeTextChangedListener(m_watcher);
        getEditText().addTextChangedListener(m_watcher);
        onEditTextChanged();
    }
    
    @Override
    protected String getPersistedString(String defaultReturnValue) {
        return String.valueOf(getPersistedInt(-1));
    }

    @Override
    protected boolean persistString(String value) {
        return persistInt(Integer.valueOf(value));
    }
}