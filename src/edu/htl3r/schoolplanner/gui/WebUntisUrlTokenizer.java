package edu.htl3r.schoolplanner.gui;

import android.widget.MultiAutoCompleteTextView;

public class WebUntisUrlTokenizer implements MultiAutoCompleteTextView.Tokenizer {

	private final String delimiter = "http://";
	private final int delimiterLength = delimiter.length();
	
	@Override
	public int findTokenStart(CharSequence text, int cursor) {
		String currentText = text.toString();
		if(currentText.startsWith(delimiter)) return delimiterLength;
		else return 0;
	}

	@Override
	public int findTokenEnd(CharSequence text, int cursor) {
		return text.length();
	}

	@Override
	public CharSequence terminateToken(CharSequence text) {
		return text;
	}
	
}