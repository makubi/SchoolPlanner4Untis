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
package edu.htl3r.schoolplanner.backend.network;

public interface ErrorCodes {

	public static int JSON_EXCEPTION = 255;
	public static int IO_EXCEPTION = 256;
	public static int HTTP_HOST_CONNECTION_EXCEPTION = 1;
	public static int UNKNOWN_HOST_EXCEPTION = 2;
	public static int SSL_FORCED_BUT_UNAVAILABLE = 3;
	public static int WEBUNTIS_SERVICE_EXCEPTION = 4;
	public static int SOCKET_TIMEOUT_EXCEPTION = 5;
	public static int NETWORK_NEEDED_BUT_UNAVAILABLE = 6;
	public static int WRONG_PORT_NUMBER = 7;
	
}
