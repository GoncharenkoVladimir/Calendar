package calendar;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.Month;
import java.time.DayOfWeek;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.Year;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

public class Calendar extends JFrame {
 String[] years = { "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016" };

  JComboBox comboBox = new JComboBox(years);

  String[] months = { "January", "February", "March", "April", "May", "June", "July", "August",
      "September", "October", "November", "December" };

  JList list = new JList(months);

  JScrollPane scrollPane = new JScrollPane(list);

  CalendarModel model = new CalendarModel();
  
  LocalDateTime timePoint = LocalDateTime.now( );
  int day = timePoint.getDayOfMonth();
  int monthNum = timePoint.getMonth().getValue();
  
  String curentYear = timePoint.getYear()+"";

  JTable table = new JTable(model){
       public Component prepareRenderer(TableCellRenderer rendererr, int roww, int columnn)
                 {
                 Component componentt = super.prepareRenderer(rendererr,roww,columnn); 
                 if(columnn == 6 || columnn == 5){
                    componentt.setBackground(Color.green);
                 }
                 if(columnn < 5){
                    componentt.setBackground(Color.white);
                 }                 
                Object cell = getModel().getValueAt(roww, columnn);
                String daystr = new Integer(day).toString();
                if(cell.equals(daystr)){
                    componentt.setBackground(Color.orange);
                }                 
                return componentt;
             }
  };

  public Calendar() {
    super();
    
    getContentPane().setLayout(null);
    comboBox.setBounds(10, 10, 100, 30);
    for(int i = 0; i <= comboBox.getItemCount(); i++){
        
        if(curentYear.equals(comboBox.getItemAt(i))){
            comboBox.setSelectedIndex(i);
        }
    }
    //comboBox.setSelectedIndex(8);
    comboBox.addItemListener(new ComboHandler());
    scrollPane.setBounds(200, 10, 150, 100);
    list.setSelectedIndex(monthNum - 1);
    list.addListSelectionListener(new ListHandler());
    table.setBounds(10, 150, 550, 200);
    model.setMonth(comboBox.getSelectedIndex() + 2008, list.getSelectedIndex());
    getContentPane().add(comboBox);
    getContentPane().add(scrollPane);
    table.setGridColor(Color.black);
    table.setShowGrid(true);
    getContentPane().add(table);

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    setSize(800, 500);
    setVisible(true);
  }

  public static void main(String[] args) {
    Calendar app = new Calendar();
  }
  public class ComboHandler implements ItemListener {
    public void itemStateChanged(ItemEvent e) {
      model.setMonth(comboBox.getSelectedIndex() + 2008, list.getSelectedIndex());
      table.repaint();
    }
  }

  public class ListHandler implements ListSelectionListener {
    public void valueChanged(ListSelectionEvent e) {
      model.setMonth(comboBox.getSelectedIndex() + 2008, list.getSelectedIndex());
      table.repaint();
    }
  }
}
class CalendarModel extends AbstractTableModel {
  String[] days = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };

  int[] numDays = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

  String[][] calendar = new String[7][7];

  public CalendarModel() {
    for (int i = 0; i < days.length; ++i)
      calendar[0][i] = days[i];
    for (int i = 1; i < 7; ++i)
      for (int j = 0; j < 7; ++j)
        calendar[i][j] = " ";
  }

  public int getRowCount() {
    return 7;
  }

  public int getColumnCount() {
    return 7;
  }

  public Object getValueAt(int row, int column) {
    return calendar[row][column];
  }

  public void setValueAt(Object value, int row, int column) {
    calendar[row][column] = (String) value;
  }

  public void setMonth(int year, int month) {
    for (int i = 1; i < 7; ++i)
      for (int j = 0; j < 7; ++j)
        calendar[i][j] = " ";
    java.util.GregorianCalendar cal = new java.util.GregorianCalendar();    
    cal.set(year, month, 1);
    int offset = cal.get(java.util.GregorianCalendar.DAY_OF_WEEK) - 2;
    offset += 7;
    int num = daysInMonth(year, month);
    for (int i = 0; i < num; ++i) {
      calendar[offset / 7][offset % 7] = Integer.toString(i + 1);
      ++offset;
    }
  }

  public boolean isLeapYear(int year) {
    if (year % 4 == 0)
      return true;
    return false;
  }

  public int daysInMonth(int year, int month) {
    int days = numDays[month];
    if (month == 1 && isLeapYear(year))
      ++days;
    return days;
  }
}