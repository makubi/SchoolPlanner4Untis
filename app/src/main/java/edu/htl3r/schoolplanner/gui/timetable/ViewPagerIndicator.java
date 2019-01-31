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
package edu.htl3r.schoolplanner.gui.timetable;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * An small bar indicating the title of the previous,
 * current and next page to be shown in a ViewPager.
 * Made to resemble the indicator in the Google+ application
 * in function.
 * 
 * @author Mark Gjol @ Zylinc
 */
public class ViewPagerIndicator extends RelativeLayout implements ViewPagerEventDistributerOnPageChangeListener {
	private static final int PADDING = 5;
	
	TextView mPrevious;
	TextView mCurrent;
	TextView mNext;
	
	LinearLayout mPreviousGroup;
	LinearLayout mNextGroup;
	
	int mArrowPadding;
	int mSize;
	
	ImageView mCurrentIndicator;
	ImageView mPrevArrow;
	ImageView mNextArrow;
	
	int[] mFocusedTextColor;
	int[] mUnfocusedTextColor;
	
	public interface PageInfoProvider{
		String getTitle(int pos);
	}
	
	PageInfoProvider mPageInfoProvider;
	public void setPageInfoProvider(PageInfoProvider pageInfoProvider){
		this.mPageInfoProvider = pageInfoProvider;
	}
	
	public void setFocusedTextColor(int[] col){
		System.arraycopy(col, 0, mFocusedTextColor, 0, 3);
		updateColor(0);
	}
	
	public void setUnfocusedTextColor(int[] col){
		System.arraycopy(col, 0, mUnfocusedTextColor, 0, 3);
		mNext.setTextColor(Color.argb(255, col[0], col[1], col[2]));
		mPrevious.setTextColor(Color.argb(255, col[0], col[1], col[2]));
		updateColor(0);
	}
	
	/**
	 * Initialization
	 * 
	 * @param startPos The initially selected element in the ViewPager
	 * @param size Total amount of elements in the ViewPager
	 * @param pageInfoProvider Interface that returns page titles
	 */
	public void init(int startPos, int size, PageInfoProvider pageInfoProvider){
		setPageInfoProvider(pageInfoProvider);
		setText(startPos - 1);
		this.mSize = size;
	}
	
	public ViewPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		addContent();
	}
	
	public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		addContent();
	}
	
	public ViewPagerIndicator(Context context) {
		super(context);
		addContent();
	}
	
	/**
	 * Add drawables for arrows
	 * 
	 * @param prev Left pointing arrow
	 * @param next Right pointing arrow
	 */
	public void setArrows(Drawable prev, Drawable next){
		this.mPrevArrow = new ImageView(getContext());
		this.mPrevArrow.setImageDrawable(prev);
		
		this.mNextArrow = new ImageView(getContext());
		this.mNextArrow.setImageDrawable(next);
		
		LinearLayout.LayoutParams arrowLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		arrowLayoutParams.gravity = Gravity.CENTER;
		
		mPreviousGroup.removeAllViews();
		mPreviousGroup.addView(mPrevArrow, arrowLayoutParams);
		mPreviousGroup.addView(mPrevious, arrowLayoutParams);
		
		mPrevious.setPadding(PADDING, 0, 0, 0);
		mNext.setPadding(0, 0, PADDING, 0);
		
		mArrowPadding = PADDING + prev.getIntrinsicWidth();
		
		mNextGroup.addView(mNextArrow, arrowLayoutParams);
	}
	
	/**
	 * Create all views, build the layout
	 */
	private void addContent(){
		mFocusedTextColor = new int[]{0, 0, 0};
		mUnfocusedTextColor = new int[]{190, 190, 190};
		
		// Text views
		mPrevious = new TextView(getContext());
		mCurrent = new TextView(getContext());
		mNext = new TextView(getContext());
		
		RelativeLayout.LayoutParams previousParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		previousParams.addRule(RelativeLayout.ALIGN_LEFT);
		
		RelativeLayout.LayoutParams currentParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		currentParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		
		RelativeLayout.LayoutParams nextParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		nextParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		
		// Groups holding text and arrows
		mPreviousGroup = new LinearLayout(getContext());
		mPreviousGroup.setOrientation(LinearLayout.HORIZONTAL);
		mNextGroup = new LinearLayout(getContext());
		mNextGroup.setOrientation(LinearLayout.HORIZONTAL);
		
		mPreviousGroup.addView(mPrevious, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mNextGroup.addView(mNext, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		addView(mPreviousGroup, previousParams);
		addView(mCurrent, currentParams);
		addView(mNextGroup, nextParams);
		
		mPrevious.setSingleLine();
		mCurrent.setSingleLine();
		mNext.setSingleLine();
		
		mPrevious.setText("previous");
		mCurrent.setText("current");
		mNext.setText("next");
		
		// Set colors
		mNext.setTextColor(Color.argb(255, mUnfocusedTextColor[0], mUnfocusedTextColor[1], mUnfocusedTextColor[2]));
		mPrevious.setTextColor(Color.argb(255, mUnfocusedTextColor[0], mUnfocusedTextColor[1], mUnfocusedTextColor[2]));
		updateColor(0);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		positionOffsetPixels = adjustOffset(positionOffsetPixels);
		position = updatePosition(position, positionOffsetPixels);
		setText(position - 1);
		updateColor(positionOffsetPixels);
		updateArrows(position);
		updatePositions(positionOffsetPixels);
	}
	
	void updatePositions(int positionOffsetPixels){
		int textWidth = mCurrent.getWidth() - mCurrent.getPaddingLeft() - mCurrent.getPaddingRight();
		int maxOffset = this.getWidth() / 2 - textWidth / 2 - mArrowPadding;
		if(positionOffsetPixels > 0){
			maxOffset -= this.getPaddingLeft();
			int offset = Math.min(positionOffsetPixels, maxOffset - 1);
			mCurrent.setPadding(0, 0, 2 * offset, 0);
			
			// Move previous text out of the way. Slightly buggy.
			/*
			int overlapLeft = mPreviousGroup.getRight() - mCurrent.getLeft() + mArrowPadding;
			mPreviousGroup.setPadding(0, 0, Math.max(0, overlapLeft), 0);
			mNextGroup.setPadding(0, 0, 0, 0);
			*/
		}else{
			maxOffset -= this.getPaddingRight();
			int offset = Math.max(positionOffsetPixels, -maxOffset);
			mCurrent.setPadding(-2 * offset, 0, 0, 0);
			
			// Move next text out of the way. Slightly buggy.
			/*
			int overlapRight = mCurrent.getRight() - mNextGroup.getLeft() + mArrowPadding;
			mNextGroup.setPadding(Math.max(0, overlapRight), 0, 0, 0);
			mPreviousGroup.setPadding(0, 0, 0, 0);
			*/
		}
	}
	
	/**
	 * Hide arrows if we can't scroll further
	 * 
	 * @param position
	 */
	void updateArrows(int position){
		if(mPrevArrow != null){
			mPrevArrow.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
			mNextArrow.setVisibility(position == mSize - 1 ? View.INVISIBLE : View.VISIBLE);
		}
	}
	
	/**
	 * Adjust position to be the view that is showing the most.
	 * 
	 * @param givenPosition
	 * @param offset
	 * @return
	 */
	int updatePosition(int givenPosition, int offset){
		int pos;
		if(offset < 0){
			pos = givenPosition + 1;
		}else{
			pos = givenPosition;
		}
		return pos; 
	}
	
	/**
	 * Fade "currently showing" color depending on it's position
	 * 
	 * @param offset
	 */
	void updateColor(int offset){
		offset = Math.abs(offset);
		float fraction = offset / ((float)this.getWidth() / 4.0f);
		fraction = Math.min(1, fraction);
		int r = (int)(mUnfocusedTextColor[0] * fraction + mFocusedTextColor[0] * (1 - fraction));
		int g = (int)(mUnfocusedTextColor[1] * fraction + mFocusedTextColor[1] * (1 - fraction));
		int b = (int)(mUnfocusedTextColor[2] * fraction + mFocusedTextColor[2] * (1 - fraction));
		mCurrent.setTextColor(Color.argb(255, r, g, b));
	}
	
	/**
	 * Update text depending on it's position
	 * 
	 * @param prevPos
	 */
	void setText(int prevPos){
		if(prevPos < 0){
			mPrevious.setText("");
		}else{
			mPrevious.setText(mPageInfoProvider.getTitle(prevPos));
		}
		mCurrent.setText(mPageInfoProvider.getTitle(prevPos + 1));
		if(prevPos + 2 == this.mSize){
			mNext.setText("");
		}else{
			mNext.setText(mPageInfoProvider.getTitle(prevPos + 2));
		}
	}
	
	// Original:
	// 244, 245, 0, 1, 2
	// New:
	// -2, -1, 0, 1, 2
	int adjustOffset(int positionOffsetPixels){
		// Move offset half width
		positionOffsetPixels += this.getWidth() / 2;
		// Clamp to width
		positionOffsetPixels %= this.getWidth();
		// Center around zero
		positionOffsetPixels -= this.getWidth() / 2;
		return positionOffsetPixels;
	}
	
	@Override
	public void onPageSelected(int position) {
		// Reset padding when the page is finally selected (May not be necessary)
		mCurrent.setPadding(0, 0, 0, 0);
	}

}
