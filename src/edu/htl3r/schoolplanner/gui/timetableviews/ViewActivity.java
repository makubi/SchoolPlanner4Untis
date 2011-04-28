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

package edu.htl3r.schoolplanner.gui.timetableviews;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import edu.htl3r.schoolplanner.CalendarUtils;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.TimegridDay;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.TimegridUnit;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;
import edu.htl3r.schoolplanner.gui.ExtrasStrings;
import edu.htl3r.schoolplanner.gui.SchoolplannerActivity;
import edu.htl3r.schoolplanner.gui.TestView;
import edu.htl3r.schoolplanner.gui.elements.ViewHeader;
import edu.htl3r.schoolplanner.gui.elements.ViewLesson;
import edu.htl3r.schoolplanner.gui.elements.ViewLessonContextInfo;

public abstract class ViewActivity extends SchoolplannerActivity implements Runnable, OnClickListener, android.view.View.OnClickListener, OnCancelListener {
	public int settingsId;

	protected String dateString = "heute";
	protected Calendar currentDate;

	private ViewType selectedViewType;

	protected GestureDetector gestureDetector;
	private ScrollView currentView;
	protected SeitenWechsler viewFlipper;

	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
	private Animation slideRightOut;

	private final int MENU_ADD_TEST = 10;
	private final int MENU_CHANGE_VIEW = 11;
	private final int MENU_TODAY = 12;
	private final int MENU_REFRESH = 14; // ja wir sind abergläubisch und nehmen keine 13 ;D
	public static final int RESULT_FAIL = 101;

	protected final int HANDLER_UPDATEVIEW = 0;
	protected final int HANDLER_SWAPVIEW = 1;
	protected final int HANDLER_NOLESSON = 2;
	protected final int HANDLER_ERRORLESSON = 3;

	private int thread_action = -1;
	private boolean handler_nextview = false;
	protected float scale;

	private int toasttime = 5;

	protected boolean refresh_lessons = false;

	protected Class<ViewActivity> prevClass = null;

	private AlertDialog selectViewTypeDialog = null;
	private List<? extends ViewType> selectViewTypeList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("Philip", getClass().getSimpleName() + ": oncreate");
		Bundle extras = getIntent().getExtras();
		if (getIntent().hasExtra(ExtrasStrings.VIEWTYPE)) {
			setSelectedViewType((ViewType) extras.get(ExtrasStrings.VIEWTYPE));
		}
		currentDate = Calendar.getInstance();
		if (getIntent().hasExtra(ExtrasStrings.DATE)) {
			// dateString = (String) extras.get("date");
			// try {
			currentDate = (Calendar) extras.get(ExtrasStrings.DATE);
			// } catch (ParseException e) {
			// e.printStackTrace();
			// }
		}
		if (getIntent().hasExtra("prevClass")) {
			prevClass = (Class<ViewActivity>) extras.get("prevClass");
		}

		scale = getResources().getDisplayMetrics().density;
		// Title und Statusbar ausblenden
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// setContentView(R.layout.newday);

		slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
		slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
		slideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
		slideRightOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);

		viewFlipper = new SeitenWechsler(this);
		viewFlipper.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		gestureDetector = new GestureDetector(this, new GestenDetector(this));

		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				// dismissDialog(SchoolplannerActivity.DIALOG_PROGRESS_CANCELABLE);

				switch (msg.what) {
					case HANDLER_UPDATEVIEW:
						viewFlipper.removeAllViews();
						viewFlipper.addView(currentView);
						setContentView(viewFlipper);
						break;

					case HANDLER_SWAPVIEW:
						viewFlipper.addView(currentView);
						if (handler_nextview) {
							viewFlipper.setInAnimation(slideLeftIn);
							viewFlipper.setOutAnimation(slideLeftOut);
						}
						else {
							viewFlipper.setInAnimation(slideRightIn);
							viewFlipper.setOutAnimation(slideRightOut);
						}
						viewFlipper.showNext();
						viewFlipper.removeViewAt(0);
						setContentView(viewFlipper);
						break;
					case HANDLER_NOLESSON:
						if (msg.getData().get(ExtrasStrings.DATE) != null) {
							bitteToasten(CalendarUtils.getDateString((Calendar) msg.getData().get(ExtrasStrings.DATE), false) + ": " + getString(R.string.no_lessons), toasttime);
						}
						else if (msg.getData().get(ExtrasStrings.DATELIST) != null) {
							bitteToasten(getString(R.string.no_lessons_part1) + " " + msg.getData().get(ExtrasStrings.DATELIST) + " " + getString(R.string.no_lessons_part2), toasttime);
						}
						else {
							bitteToasten(getString(R.string.no_lessons), toasttime);
						}
						break;
					case HANDLER_ERRORLESSON:
						if (msg.getData().get(ExtrasStrings.DATE) != null) {
							bitteToasten(CalendarUtils.getDateString((Calendar) msg.getData().get(ExtrasStrings.DATE), false) + ": " + getString(R.string.error_laoddata), toasttime);
						}
						else if (msg.getData().get(ExtrasStrings.DATELIST) != null) {
							bitteToasten(getString(R.string.error_laoddata) + " (" + msg.getData().get(ExtrasStrings.DATELIST) + ")", toasttime);
						}
						else {
							bitteToasten(getString(R.string.error_laoddata), toasttime);
						}
						break;
				}
			};
		};

		thread_action = HANDLER_UPDATEVIEW;
		startDialogAction(getString(R.string.progress_createLayout_title), getString(R.string.progress_createLayout_text), this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("Philip", "resume");
		checkCurrentView();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("Philip", "result");
		switch (requestCode) {
			case MENU_ADD_TEST:
				if (resultCode == RESULT_OK) {
					Log.d("Philip", "menuadd");
					bitteToasten(getString(R.string.addtest_success), 3);

				}
				else {
					Log.d("Philip", "error");
					bitteToasten(getString(R.string.addtest_failed), 3);
				}
				break;

			case MENU_SETTINGS:
				if (data != null && data.hasExtra(ExtrasStrings.UPDATEVIEW)) {
					Log.d("Philip", "updateview");
					thread_action = HANDLER_UPDATEVIEW;
					startDialogAction(getString(R.string.progress_createLayout_title), getString(R.string.progress_createLayout_text), this);
				}
				break;
		}

	}

	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (thisThread != null && thisThread.isAlive()) {
			// thread lauft grad --> wird erst gladen --> nix tun
			Log.d("Philip", getClass().getSimpleName() + ": runningthread");
		}
		else {
			// gui neu laden
			Log.d("Philip", getClass().getSimpleName() + ": startingthread");
			thread_action = HANDLER_UPDATEVIEW;
			startDialogAction(getString(R.string.progress_createLayout_title), getString(R.string.progress_createLayout_text), this);
		}
	}

	@Override
	public void onBackPressed() {
		if (prevClass != null) {
			app.setCurrentView(prevClass);
			Log.d("Philip", getClass().getSimpleName() + ": backpressed setcurrentview: " + prevClass);
		}
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.removeItem(MENU_CREDITS);
		menu.add(0, MENU_ADD_TEST, 4, R.string.menu_add_test).setIcon(R.drawable.ic_menu_add);
		menu.add(0, MENU_CHANGE_VIEW, 5, R.string.menu_change_view).setIcon(R.drawable.ic_menu_view);
		menu.add(0, MENU_TODAY, 6, R.string.menu_today).setIcon(R.drawable.ic_menu_today);
		menu.add(0, MENU_REFRESH, 6, R.string.menu_refresh).setIcon(R.drawable.ic_menu_refresh);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_ADD_TEST:
				Intent myIntent = new Intent(this, TestView.class);
				myIntent.putExtra(ExtrasStrings.VIEWTYPE, getSelectedViewType());
				myIntent.putExtra(ExtrasStrings.DATE, currentDate);
				startActivityForResult(myIntent, MENU_ADD_TEST);
				return true;
			case MENU_CHANGE_VIEW:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(R.string.change_view_title);
				builder.setItems(R.array.views, this);
				AlertDialog alert = builder.create();
				alert.show();
				break;

			case MENU_TODAY:
				Calendar tmp = currentDate;
				currentDate = Calendar.getInstance();
				if (currentDate.before(tmp)) {
					swapView(false);
				}
				else {
					swapView(true);
				}
				break;
			case MENU_REFRESH:
				refresh_lessons = true;
				thread_action = HANDLER_UPDATEVIEW;
				startDialogAction(getString(R.string.progress_createLayout_title), getString(R.string.progress_createLayout_text), this);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(R.string.newtest_title);
		menu.add(Menu.NONE, MENU_ADD_TEST, 0, getString(R.string.newtest));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ViewLessonContextInfo ci = (ViewLessonContextInfo) item.getMenuInfo();

		switch (item.getItemId()) {
			case MENU_ADD_TEST:
				Intent myIntent = new Intent(this, TestView.class);
				myIntent.putExtra(ExtrasStrings.VIEWTYPE, getSelectedViewType());
				myIntent.putExtra(ExtrasStrings.DATE, currentDate);
				myIntent.putExtra(ExtrasStrings.LESSON, ci.lesson);
				startActivityForResult(myIntent, MENU_ADD_TEST);
				return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// Log.d("Philip", "dispatch");
		if (!(this instanceof DateListView)) {
			if (gestureDetector.onTouchEvent(ev)) {
				return true;
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (dialog == selectViewTypeDialog) {
			if (selectViewTypeList != null) {
				setSelectedViewType(selectViewTypeList.get(which));
				thread_action = HANDLER_UPDATEVIEW;
				startDialogAction(getString(R.string.progress_createLayout_title), getString(R.string.progress_createLayout_text), this);
			}
		}
		else {
			switch (which) {
				case 0:
					app.setCurrentView(DayView.class);
					break;

				case 1:
					app.setCurrentView(WeekView.class);
					break;

				case 2:
					app.setCurrentView(MonthView.class);
					break;

				case 3:
					app.setCurrentView(DateListView.class);
					break;
			}
			bitteToasten(getString(R.string.view_changed), 3); // TODO nur anzeigen wenn wirklich geaendert
			checkCurrentView();
		}
	}

	// normales onclick auf die views
	@Override
	public void onClick(View v) {
		if (v instanceof ViewLesson) {
			Log.d("Philip", getClass().getSimpleName() + ": viewlesson click");
			Intent in = new Intent(this, LessonView.class);
			in.putExtra(ExtrasStrings.LESSON, ((ViewLesson) v).getLesson());
			startActivity(in);
		}
		else if (v instanceof ViewHeader) {
			Log.d("Philip", getClass().getSimpleName() + ": viewheader click");
			Intent in = new Intent(this, DayView.class);
			in.putExtra(ExtrasStrings.DATE, ((ViewHeader) v).getDate());
			in.putExtra(ExtrasStrings.VIEWTYPE, getSelectedViewType());
			in.putExtra(ExtrasStrings.PREVCLASS, getClass());
			startActivity(in);
			app.setCurrentView(DayView.class);
		}

	}

	// oncancel fuer progressdialog
	@Override
	public void onCancel(DialogInterface dialog) {
		finish();

	}

	@Override
	public void run() {
		Log.d("Philip", getClass().getSimpleName() + ": run");
		currentView = (ScrollView) createLayout();
		refresh_lessons = false;
		handler.sendEmptyMessage(thread_action);
		Log.d("Philip", getClass().getSimpleName() + ": run ende");
	}

	/**
	 * gibt den Abstand zum Beginn des Unterrichts (0te Stunde des {@link TimegridDay}) zurueck, damit er fuer das Layout verwendet werden kann
	 * @param now von wo aus der Abstand berechnet werden soll
	 * @param hour TODO
	 * @return der Abstand
	 */
	protected int getMarginSinceStart(Calendar now, int hour) {
		Calendar start = (Calendar) now.clone();
		try {
			List<TimegridUnit> tg = app.getData().getTimegrid().getTimegridForCalendarDay(now.get(Calendar.DAY_OF_WEEK));
			if (tg != null) {
				Calendar tmp = tg.get(hour).getBegin();
				start.set(Calendar.HOUR_OF_DAY, tmp.get(Calendar.HOUR_OF_DAY));
				start.set(Calendar.MINUTE, tmp.get(Calendar.MINUTE));
			}
			else {
				return 0;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (start == null) {
			start = (Calendar) now.clone();
			start.set(Calendar.HOUR_OF_DAY, 7);
			start.set(Calendar.MINUTE, 10);
		}
		long minutes = CalendarUtils.minBetween(start, now);
		int margin = (int) (minutes * scale);
		return margin;
	}

	/**
	 * gibt den Abstand zwischen zwei Daten zurueck, damit er fuer das Layout verwendet werden kann
	 * @param start das Datum von dem aus gerechnet werden soll
	 * @param end das Datum bis zu dem gerechnet werden soll
	 * @return der Abstand zwischen den zwei Daten
	 */
	protected int getMarginBetweenCalendars(Calendar start, Calendar end) {
		long minutes = CalendarUtils.minBetween(start, end);
		int margin = (int) (minutes * scale);
		return margin;
	}

	/**
	 * ueberprueft ob die angezeigte Ansicht (also diese Klasse) gleich der in {@link SchoolPlannerApp} gespeicherten Ansicht ist
	 * und wechselt auf diese sollte dies nicht so sei
	 */
	private void checkCurrentView() {
		if (this.getClass() != app.getCurrentView()) {
			Log.d("Philip", getClass().getSimpleName() + ": wrong class");
			Intent myIntent = new Intent(this, app.getCurrentView());
			myIntent.putExtra(ExtrasStrings.VIEWTYPE, getSelectedViewType());
			myIntent.putExtra(ExtrasStrings.DATE, currentDate);
			if (prevClass != null) {
				myIntent.putExtra("prevClass", prevClass);
			}
			// myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(myIntent);
			finish();
		}
	}

	/**
	 * wird in Unterklassen ueberschrieben darin wird das Layout erstellt
	 * 
	 * @return der View der angezeigt werden soll
	 */
	public View createLayout() {
		// wird in unterklassen ueberschrieben
		ScrollView sv = new ScrollView(this);
		RelativeLayout list = new RelativeLayout(this);
		TextView header = new TextView(this);
		String datum = currentDate.get(Calendar.DAY_OF_MONTH) + "." + (currentDate.get(Calendar.MONTH) + 1) + "." + currentDate.get(Calendar.YEAR);
		header.setText(datum + " - " + getSelectedViewType().getName());
		header.setId(1337);
		header.setTextSize(20);
		header.setGravity(Gravity.CENTER);
		Display display = getWindowManager().getDefaultDisplay();
		header.setWidth(display.getWidth());
		list.addView(header);
		sv.addView(list);
		// sv.setOnTouchListener(gestureListener);
		return sv;
	}

	/**
	 * holt die Stunden fuer die in selectedViewType gespeicherte Art fuer das mitgelieferte Datum
	 * 
	 * @param datum
	 *            das Datum von dem man die Liste will
	 * @return eine Liste von Lesson Objekten
	 */
	protected List<Lesson> getLessonList(Calendar datum) {
		try {
			Log.d("Philip", getClass().getSimpleName() + ": refresh_lessons: " + refresh_lessons);
			return app.getData().getMergedLessons(getSelectedViewType(), datum, refresh_lessons);
		} catch (IOException e) {
			// TODO: Fehlermeldung anzeigen, wenn Netzwerkproblem auftritt
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ruft createLayout auf und wechselt dann zu dem neuen view
	 * 
	 * @param next
	 *            ist der neue view vor oder nach dem jetzigen (bestimmt animationsrichtung)
	 */
	public void swapView(boolean next) {
		thread_action = HANDLER_SWAPVIEW;
		handler_nextview = next;
		startDialogAction(getString(R.string.progress_createLayout_title), getString(R.string.progress_createLayout_text), this);
	}

	/**
	 * gibt den aktuellen View der angezeigt wird zurueck
	 * @return der gerade angezeigte View
	 */
	public ScrollView getCurrentView() {
		return currentView;
	}

	/**
	 * setzt den aktuellen View neu
	 * @param currentView der neue View
	 */
	public void setCurrentView(ScrollView currentView) {
		this.currentView = currentView;
	}

	/**
	 * gibt das aktuelle Datum zurueck
	 * @return das Datum
	 */
	public Calendar getDate() {
		return currentDate;
	}

	/**
	 * setzt das aktuelle Datum neu
	 * @param date das neue Datum
	 */
	public void setDate(Calendar date) {
		this.currentDate = date;
	}

	/**
	 * @param selectedViewType the selectedViewType to set
	 */
	public void setSelectedViewType(ViewType selectedViewType) {
		this.selectedViewType = selectedViewType;
	}

	/**
	 * @return the selectedViewType
	 */
	public ViewType getSelectedViewType() {
		return selectedViewType;
	}

	/**
	 * Zeigt einen {@link ProgressDialog} an, waehrend im Hintergrund ein Thread aufgerufen wird der eine Aktion ausfuehrt
	 * dazu muss die {@link #run()}-Methode ueberschrieben werden und danach mittels des {@link #handler}s ein {@link #dismissDialog(int)}
	 * mit dem Parameter {@link #DIALOG_PROGRESS} aufgerufen werden.
	 * @param title der Titel des ProgressDialogs
	 * @param msg der Text des ProgressDialogs
	 */
	protected void startDialogAction(String title, String msg) {
		setContentView(R.layout.loading);
		TextView titletv = (TextView) findViewById(R.id.loading_title);
		titletv.setText(title);
		TextView msgtv = (TextView) findViewById(R.id.loading_msg);
		msgtv.setText(msg);

		// progressDialog = ProgressDialog.show(this, title, msg, false, false);
		thisThread = new Thread(this);
		thisThread.start();
	}

	/**
	 * Zeigt einen {@link ProgressDialog} an, waehrend im Hintergrund ein Thread aufgerufen wird der eine Aktion ausfuehrt
	 * dazu muss die {@link #run()}-Methode ueberschrieben werden und danach mittels des {@link #handler}s ein {@link #dismissDialog(int)}
	 * mit dem Parameter {@link #DIALOG_PROGRESS_CANCELABLE} aufgerufen werden.
	 * Dieser Dialog ist <i>cancelable</i>, dass heisst er kann mittels des Zurueck-Buttons des Geraetes abgebrochen werden, 
	 * dabei wird dann der mitgegebene {@link OnCancelListener} aufgerufen
	 * @param title der Titel des ProgressDialogs
	 * @param msg der Text des ProgressDialogs
	 * @param cancelListener ein {@link OnCancelListener} der beim abbrechen aufgerufen werden soll
	 */
	protected void startDialogAction(String title, String msg, OnCancelListener cancelListener) {
		setContentView(R.layout.loading);
		TextView titletv = (TextView) findViewById(R.id.loading_title);
		titletv.setText(title);
		TextView msgtv = (TextView) findViewById(R.id.loading_msg);
		msgtv.setText(msg);

		// progressDialog = ProgressDialog.show(this, title, msg, false, true);
		// progressDialog.setOnCancelListener(cancelListener);
		thisThread = new Thread(this);
		thisThread.start();
	}

	protected void showSelectViewTypePopup() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(R.string.newviewtype_popup);
		try {
			if (getSelectedViewType() instanceof SchoolClass) {
				selectViewTypeList = app.getData().getSchoolClassList();
			}
			else if (getSelectedViewType() instanceof SchoolTeacher) {
				selectViewTypeList = app.getData().getSchoolTeacherList();
			}
			else if (getSelectedViewType() instanceof SchoolRoom) {
				selectViewTypeList = app.getData().getSchoolRoomList();
			}
			else if (getSelectedViewType() instanceof SchoolSubject) {
				selectViewTypeList = app.getData().getSchoolSubjectList();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		dialog.setItems(getElementNamesArray(selectViewTypeList, false), this);
		selectViewTypeDialog = dialog.show();
	}
	
	public abstract void nextDate();
	
	public abstract void prevDate();
}
