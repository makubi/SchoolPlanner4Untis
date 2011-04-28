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

package edu.htl3r.schoolplanner.backend.schoolObjects.lesson;

import java.io.Serializable;

public abstract class LessonCode implements Serializable,Cloneable {
	
	private static final long serialVersionUID = 126291868822043825L;
	
	protected int fgColor;
	protected int bgColor;
	
	public int getFgColor() {
		return fgColor;
	}
	public int getBgColor() {
		return bgColor;
	}
	
	protected void setFgColor(int fgColor) {
		this.fgColor = fgColor;
	}
	protected void setBgColor(int bgColor) {
		this.bgColor = bgColor;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
        return super.clone();
	}
}
