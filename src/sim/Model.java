package sim;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

public class Model extends AbstractTableModel{
	private int rowCount;
	private String columnName[]=new String[]{"ID","Temps de calcul","Deadline","Pèriode"};
	private ArrayList<Task> list;
	public Model() {
            list = new ArrayList<>();
            try {
                Scanner sc=new Scanner(new File("test.txt"));
                ArrayList<Task> tasks=new ArrayList();
                int i=1;
                while(sc.hasNext())
                {
                    String info[]=sc.nextLine().split(" ");
                    list.add(new Task(i, info[0], info[1], info[2]));
                    i++;
                }
                rowCount=3;
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
	public void addRow()
	{
		rowCount++;
		list.add(new Task(rowCount,0,0,0));
	}
	public void remRow(int i)
	{
		if(rowCount >0)
		{
			rowCount--;
			list.remove(i);
		}
	}
	@Override
	public int getColumnCount() {
		return 4;
	}
	@Override
	public String getColumnName(int arg0) {
		return columnName[arg0];
	}
	@Override
	public int getRowCount() {
		return list.size();
	}
	@Override
	public Object getValueAt(int r, int c) {
		switch (c) 
		{
			case 0:return list.get(r).getId();
			case 1:return list.get(r).getC();
			case 2:return list.get(r).getD();
			case 3:return list.get(r).getT();
		}
		return null;
	}
	@Override
	public void setValueAt(Object val, int arg0, int arg1) {
		int v=Integer.valueOf((String)val);
		switch (arg1) {
		case 0:list.get(arg0).setId(v);break;
		case 1:list.get(arg0).setC(v);break;
		case 2:list.get(arg0).setD(v);break;
		case 3:list.get(arg0).setT(v);break;
		}
		fireTableDataChanged();
	}
	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		return true;
	}
	public ArrayList<Task> getList() {
		return list;
	}
	public void setList(ArrayList<Task> list) {
		this.list = list;
	}
	public ArrayList<Task> getTasks(){
		return list;
	}
}

