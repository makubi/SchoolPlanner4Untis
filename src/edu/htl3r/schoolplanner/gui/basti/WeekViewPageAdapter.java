package edu.htl3r.schoolplanner.gui.basti;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ScrollView;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.gui.basti.GUIData.GUIWeek;
import edu.htl3r.schoolplanner.gui.basti.Week.WeekLayout;

public class WeekViewPageAdapter extends PagerAdapter implements ViewPagerIndicator.PageInfoProvider {

	public final static int NUM_SCREENS = 100;
	

	private DateTime date;
	private Context context;
	private int oldpos = NUM_SCREENS / 2;
	private WeekLayout view_cach[] = new WeekLayout[NUM_SCREENS];
	private BlockingDownloadQueue downloadschlange;
	
	public void setDate(DateTime dt) {
		date = dt;
	}

	public synchronized void setWeeData(GUIWeek data, int pos) {
		if (!view_cach[pos].isDataHere() && data != null) {
			view_cach[pos].setWeekData(data);
		}
	}

	public void setContext(Context c, BlockingDownloadQueue bd) {
		context = c;
		downloadschlange = bd;
		for (int i = 0; i < view_cach.length; i++) {
			view_cach[i] = new WeekLayout(context, i);
		}
	}

	@Override
	public void destroyItem(View collection, int arg1, Object view) {
		ScrollView s = (ScrollView) view;
		s.removeAllViews();
		((ViewPager) collection).removeView(s);
	}

	@Override
	public void finishUpdate(View arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getCount() {
		return NUM_SCREENS;
	}

	@Override
	public synchronized View instantiateItem(View collection, int position) {

		int di = position - 50;

		DateTime ad = new DateTime();
		ad.set(date.getDay() + (di * 7), date.getMonth(), date.getYear());

		InputTransferObject input = new InputTransferObject(ad, position);

		if (!view_cach[position].isDataHere() ){//&& !downloadschlange.contains(input)) {
			downloadschlange.add(input);
		}

		ViewPager tmp = (ViewPager) collection;

		tmp.removeView((ScrollView) view_cach[position].getParent());

		oldpos = position;

		ScrollView scr = new ScrollView(context);
		scr.addView(view_cach[position]);
		tmp.addView(scr);

		return scr;

	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == (View) object;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
		// TODO Auto-generated method stub
	}

	public int getItemPosition(Object object) {
		return oldpos - 1;
	}

	@Override
	public String getTitle(int pos) {
		int di = pos - 50;
		DateTime ad = new DateTime();
		ad.set(date.getDay() + (di * 7), date.getMonth(), date.getYear());
		return ad.getDay() + "." + ad.getMonth() + "." + ad.getYear();
	}

}