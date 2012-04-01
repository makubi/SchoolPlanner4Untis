package edu.htl3r.schoolplanner.gui;

import android.widget.MultiAutoCompleteTextView;

/**
 * Tokenizer, um die WebUntis-Server-URLs zur Auto-Vervollstaendigung anzuzeigen.<br />
 * <b>http://</b> am Anfang wird ignoriert.
 */
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