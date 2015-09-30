package com.gas.ui.calendar;

public interface DatePickerController {
	public abstract int getMaxYear();
	public abstract void onDayOfMonthSelected(int year, int month, int day,SimpleMonthView simpleMonthView);
    public abstract void onDateRangeSelected(final SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays);

}