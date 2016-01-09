package edu.htl3r.schoolplanner.gui.timetable;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.ViewPager.OnPageChangeListener;

public class ViewPagerEventDistributer implements OnPageChangeListener {

	private List<ViewPagerEventDistributerOnPageChangeListener> registerdListeners = new ArrayList<ViewPagerEventDistributerOnPageChangeListener>();
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		for(ViewPagerEventDistributerOnPageChangeListener v : registerdListeners){
			v.onPageScrollStateChanged(arg0);
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		for(ViewPagerEventDistributerOnPageChangeListener v : registerdListeners){
			v.onPageScrolled(arg0, arg1, arg2);
		}		
	}

	@Override
	public void onPageSelected(int arg0) {
		for(ViewPagerEventDistributerOnPageChangeListener v : registerdListeners){
			v.onPageSelected(arg0);
		}		
	}
	
	public void addViewPagerEventDistributer(ViewPagerEventDistributerOnPageChangeListener viewPagerEventDistributerOnPageChangeListener){
		registerdListeners.add(viewPagerEventDistributerOnPageChangeListener);
	}
	public void removeViewPagerEventDistributer(ViewPagerEventDistributerOnPageChangeListener viewPagerEventDistributerOnPageChangeListener){
		registerdListeners.remove(viewPagerEventDistributerOnPageChangeListener);
	}

}

interface ViewPagerEventDistributerOnPageChangeListener{
	public void onPageScrollStateChanged(int arg0);
	public void onPageScrolled(int arg0, float arg1, int arg2);
	public void onPageSelected(int arg0);
}
