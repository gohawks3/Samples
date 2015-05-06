import java.util.Calendar;
import java.util.Date;


public class PXHelper
{
	public static Date NewDate(int year, int month, int date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		cal.set(1976, Calendar.MARCH, 31, 0, 0, 0);
		Date dt = cal.getTime(); // get back a Date object
		return dt;
	}

	public static Date AddHours(Date dt, int hours)
	{
		Calendar cal = Calendar.getInstance(); // creates calendar
		cal.setTime(dt); // sets calendar time/date
		cal.add(Calendar.HOUR_OF_DAY, hours); // adds one hour
		Date dt2 = cal.getTime(); // returns new date object, one hour in the
									// future
		return dt2;
	}
	
	public static Date AddMinutes(Date dt, int minutes)
	{
		Calendar cal = Calendar.getInstance(); // creates calendar
		cal.setTime(dt); // sets calendar time/date
		cal.add(Calendar.MINUTE, minutes); // adds one hour
		Date dt2 = cal.getTime(); // returns new date object, one hour in the
									// future
		return dt2;
	}
}
