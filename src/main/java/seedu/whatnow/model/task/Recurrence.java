//@@author A0126240W
package seedu.whatnow.model.task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class Recurrence {
    private static final String DATE_WITH_SLASH_FORMAT= "dd/MM/yyyy";
    private static final String EMPTY_STRING = "";
    private static final String FORWARD_SLASH = "/";
    private static final String SINGLE_DIGIT = ("^(\\d)$");
    private static final String LEAP_YEAR_DATE = "29/02/";
    private static final String DAILY = "day";
    private static final String WEEKLY = "week";
    private static final String MONTHLY = "month";
    private static final String YEARLY = "year";
    
    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final int DATE_DAY = 0;
    private static final int DATE_MONTH = 1;
    private static final int DATE_YEAR = 2;   

    private String period = null;
    private String taskDate = null;
    private String startDate = null;
    private String endDate = null;
    private String endPeriod = null;
    
    private HashMap<Integer, Integer> daysInMonths = new HashMap<Integer, Integer>();
    
    public Recurrence(String period, String taskDate, String startDate, String endDate, String endPeriod) {
        this.period = period;
        this.taskDate = taskDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.endPeriod = endPeriod;
        daysInMonths = storeDaysInMonths(daysInMonths);
    }
    
    public String getPeriod() {
        return this.period;
    }
    
    public void setPeriod(String period) {
        this.period = period;
    }
    
    public String getTaskDate() {
        return this.taskDate;
    }
    
    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }
    
    public String getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    
    public String getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    
    public String getEndPeriod() {
        return this.endPeriod;
    }
    
    public void setEndPeriod(String endPeriod) {
        this.endPeriod = endPeriod;
    }

    private static HashMap<Integer, Integer> storeDaysInMonths(HashMap<Integer, Integer> daysInMonths) {
        daysInMonths.put(1, 31);
        daysInMonths.put(2, 29);
        daysInMonths.put(3, 31);
        daysInMonths.put(4, 30);
        daysInMonths.put(5, 31);
        daysInMonths.put(6, 30);
        daysInMonths.put(7, 31);
        daysInMonths.put(8, 31);
        daysInMonths.put(9, 30);
        daysInMonths.put(10, 31);
        daysInMonths.put(11, 30);
        daysInMonths.put(12, 31);
        return daysInMonths;
    }
    
    public boolean hasRecurring() {
        return this.period != null;
    }
    
    public boolean hasNextTask(String date) {
        Date nextDate = null;
        try {
            DateFormat df = new SimpleDateFormat(DATE_WITH_SLASH_FORMAT);
            df.setLenient(false);
            nextDate = df.parse(date);
        } catch(ParseException ex) {
            return false;
        }
        
        Calendar d = new GregorianCalendar();
        d.setTime(nextDate);
        d.set(Calendar.HOUR_OF_DAY, 00);
        d.set(Calendar.MINUTE, 00);
        d.set(Calendar.SECOND, 00);
        nextDate = d.getTime();
        
        Date recurringEndDate = null;
        try {
            DateFormat df = new SimpleDateFormat(DATE_WITH_SLASH_FORMAT);
            df.setLenient(false);
            recurringEndDate = df.parse(endPeriod);
        } catch(ParseException ex) {
            return false;
        }
        
        //Following checks if the user input date is invalid i.e before today's date
        Calendar r = new GregorianCalendar();
        r.setTime(recurringEndDate);
        r.set(Calendar.HOUR_OF_DAY, 23);
        r.set(Calendar.MINUTE, 59);
        r.set(Calendar.SECOND, 59);
        Date recurEndDate = r.getTime();
        
        if(recurEndDate.compareTo(nextDate) < 0) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Formats the input date to the DD/MM/YYYY format
     * @param date The date to be formatted in DD/MM/YYYY format but DD and MM may be single digit
     * @return the formatted date with a zero padded in front of single digits in DD and MM
     */
    private static String formatDate(String[] splitDate) {
        String date = EMPTY_STRING;
        
        for (int i = 0; i < splitDate.length; i++) {
            date += splitDate[i].replaceAll(SINGLE_DIGIT, ZERO + splitDate[i]);
            if (i < splitDate.length - ONE) {
                date += FORWARD_SLASH;
            }
        }
        
        return date;
    }
    
    public String getNextDay(String date) {
        String[] splitDate = date.split(FORWARD_SLASH);      
        int day = Integer.parseInt(splitDate[DATE_DAY]) + 1;
        int month = Integer.parseInt(splitDate[DATE_MONTH]);
        int year = Integer.parseInt(splitDate[DATE_YEAR]);
        
        while (daysInMonths.get(month) == null || day > daysInMonths.get(month)) {
            if (month > 12) {
                year++;
                month = 1;
                break;
            }
            
            if (day > daysInMonths.get(month)) {
                month++;
                day = 1;
            }  
        }

        //February
        if (month == 2 && day == 29) {
            try {
                DateFormat df = new SimpleDateFormat(DATE_WITH_SLASH_FORMAT);
                df.setLenient(false);
                df.parse(LEAP_YEAR_DATE + year);
            } catch(ParseException ex) {
                day = 1;
                month++;
            }
        }
        
        splitDate[DATE_DAY] = (day < 10) ? "0" + day : EMPTY_STRING + day;
        splitDate[DATE_MONTH] = (month < 10) ? "0" + month : EMPTY_STRING + month;
        splitDate[DATE_YEAR] = EMPTY_STRING + year;
        
        return formatDate(splitDate);
    }
    
    public String getNextWeek(String date) {
        String formattedDate;
        String[] splitDate = date.split(FORWARD_SLASH);      
        int day = Integer.parseInt(splitDate[DATE_DAY]) + 7;
        int month = Integer.parseInt(splitDate[DATE_MONTH]);
        int year = Integer.parseInt(splitDate[DATE_YEAR]);
        
        while (daysInMonths.get(month) == null || day > daysInMonths.get(month)) {
            if (month > 12) {
                year++;
                month = 1;
                break;
            }
            
            if (day > daysInMonths.get(month)) {
                day -= daysInMonths.get(month);
                month++;
            } 
        }
        
        //February
        if (month == 2 && day == 29) {
            try {
                DateFormat df = new SimpleDateFormat(DATE_WITH_SLASH_FORMAT);
                df.setLenient(false);
                df.parse(date);
            } catch(ParseException ex) {
                day = 1;
                month++;
            }
        } else if (month == 3 && day < 7) {
            try {
                DateFormat df = new SimpleDateFormat(DATE_WITH_SLASH_FORMAT);
                df.setLenient(false);
                df.parse(LEAP_YEAR_DATE + year);
            } catch(ParseException ex) {
                day++;             
            }
        }
        
        splitDate[DATE_DAY] = (day < 10) ? "0" + day : EMPTY_STRING + day;
        splitDate[DATE_MONTH] = (month < 10) ? "0" + month : EMPTY_STRING + month;
        splitDate[DATE_YEAR] = EMPTY_STRING + year;
        formattedDate = formatDate(splitDate);
        
        return formattedDate;
    }
    
    public String getNextMonth(String date) {
        String[] splitDate = date.split(FORWARD_SLASH);      
        int day = Integer.parseInt(splitDate[DATE_DAY]);
        int month = Integer.parseInt(splitDate[DATE_MONTH]) + 1;
        int year = Integer.parseInt(splitDate[DATE_YEAR]);
        
        while (daysInMonths.get(month) == null || day > daysInMonths.get(month)) {
            if (month > 12) {
                year++;
                month = 1;
                break;
            }
            
            if (day > daysInMonths.get(month)) {
                day = daysInMonths.get(month);
            }
        }
        
        //February
        if (month == 2 && day == 29) {
            try {
                DateFormat df = new SimpleDateFormat(DATE_WITH_SLASH_FORMAT);
                df.setLenient(false);
                df.parse(LEAP_YEAR_DATE + year);
            } catch(ParseException ex) {
                day = 28;
            }
        }
        
        splitDate[DATE_DAY] = (day < 10) ? "0" + day : EMPTY_STRING + day;
        splitDate[DATE_MONTH] = (month < 10) ? "0" + month : EMPTY_STRING + month;
        splitDate[DATE_YEAR] = EMPTY_STRING + year;

        return formatDate(splitDate);
    }
    
    public String getNextYear(String date) {
        String[] splitDate = date.split(FORWARD_SLASH);      
        int day = Integer.parseInt(splitDate[DATE_DAY]);
        int month = Integer.parseInt(splitDate[DATE_MONTH]);
        int year = Integer.parseInt(splitDate[DATE_YEAR]) + 1;
        
        //February
        if (month == 2 && day == 29) {
            try {
                DateFormat df = new SimpleDateFormat(DATE_WITH_SLASH_FORMAT);
                df.setLenient(false);
                df.parse(LEAP_YEAR_DATE + year);
            } catch(ParseException ex) {
                day = 28;
            }
        }
        
        splitDate[DATE_DAY] = (day < 10) ? "0" + day : EMPTY_STRING + day;
        splitDate[DATE_MONTH] = (month < 10) ? "0" + month : EMPTY_STRING + month;
        splitDate[DATE_YEAR] = EMPTY_STRING + year;
        
        return formatDate(splitDate);
    }
    
    public Task getNextTask(Task currentTask) {
        if (this.period.equals(DAILY)) {
            if (this.taskDate != null) {
                this.taskDate = getNextDay(this.taskDate);
            }
        } else if (this.period.equals(WEEKLY)) {
            if (this.taskDate != null) {
                this.taskDate = getNextWeek(this.taskDate);
            }
        } else if (this.period.equals(MONTHLY)) {
            if (this.taskDate != null) {
                this.taskDate = getNextMonth(this.taskDate);
            } else if (this.startDate != null) {
                this.startDate = getNextMonth(this.startDate);
                this.endDate = getNextMonth(this.endDate);
            }
        } else if (this.period.equals(YEARLY)) {
            if (this.taskDate != null) {
                this.taskDate = getNextYear(this.taskDate);
            } else if (this.startDate != null) {
                this.startDate = getNextYear(this.startDate);
                this.endDate = getNextYear(this.endDate);
            }
        }
        
        return new Task(currentTask.getName(), this.taskDate, this.startDate, this.endDate, currentTask.getTaskTime(), currentTask.getStartTime(), currentTask.getEndTime(), currentTask.getPeriod(), currentTask.getEndPeriod(), currentTask.getTags(), currentTask.getStatus(), currentTask.getTaskType());
    }
    
	@Override
	public String toString() {
	    String str = EMPTY_STRING;
	    if (this.taskDate != null) {
	        str = this.taskDate;
	    }
	    
	    if (this.startDate != null) {
	        str = this.startDate;
	    }
	    
	    if (this.endDate != null) {
	        str += " to " + this.endDate;
	    }
	    
	    if (this.period != null) {
	        str += " every " + this.period;
	    }
	    
	    if (this.endPeriod != null) {
	        str += " till " + endPeriod;
	    }
	    
	    return str;
	}
	
	@Override
	public boolean equals(Object other) {
	    return true;
	}
}