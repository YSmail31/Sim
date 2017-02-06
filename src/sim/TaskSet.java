package sim;
import java.util.ArrayList;
import java.util.Comparator;

public class TaskSet    { 
    private ArrayList<Task> taskSet;
    int size;
    public static int SORT_D=1,SORT_T=2,SORT_ID=3;
    private int CsleepMin = 2;
    public TaskSet() {
            this.taskSet = new ArrayList<>();
            size = 0;
    }
    public TaskSet(ArrayList<Task> t) {
        this.taskSet = t;
        size = t.size();
        System.out.println(processUtil());
        Task tsleep = getSleepTask();
        if(tsleep.getC() > CsleepMin)
            addTask(tsleep);
        sort(SORT_ID);
        System.out.println(processUtil());
    }
    public Task getSleepTask(){
        sort(TaskSet.SORT_T);
        int periode = taskSet.get(0).getT();
        int i=1;
        while(i<size && taskSet.get(i).getT() > 2*periode)
            i++;
            
        //if(i < size )
            //periode /= 2;
        
        double u=processUtil();
        
        int tempsExec = (int)((1 - u) * periode); 
        Task sleepTask = new Task(size+1, tempsExec, periode, periode);
        
        while(!worstCaseResp(sleepTask) && tempsExec > 0)
        {
            tempsExec -- ;
            sleepTask.setC(tempsExec);
        }
        
        return sleepTask;
    }
    public boolean worstCaseResp(Task tSleep){
        for(Task t:taskSet)
        {
            int w=t.getC(),w1=w-1;
            while(w != w1)
            {
                w1 = w;
                w = (int) (t.getC() + Math.ceil((double) w1/tSleep.getT())*tSleep.getC());
                for(int i=0; taskSet.get(i) != t; i++)
                    w += (int) (Math.ceil((double) w1/taskSet.get(i).getT())*taskSet.get(i).getC());
                
            }
            if( w > t.getD())
                return false;
            }
        return true;
    }
    private double processUtil() {
        double s=0;
        for(Task t:taskSet)
            s += (double)t.getC()/t.getT();
        return s;
    }
    public int hyperPeriod(){
		if(size>0)
		{
			int h=taskSet.get(0).getT();
			int i=1;
			while(i<size)
			{
				h=h*getTask(i).getT()/pgcd(h,getTask(i).getT());
				i++;
			}
			return h;
		}
		return 0;
	}
    public int pgcd(int h, int t) {
		if(t>h)
		{
			int c=t;
			t=h;
			h=c;
		}
		while(h%t!=0)
		{
			int c=h;
			h=t;
			t=c%t;
		}
		return t;
	}
    public void addTask(Task t){
            taskSet.add(t);
	}
    public void printTs(){
            for(Task t:taskSet)
            {
                    System.out.println(t);
            }
	}
    public void setTaskSet(ArrayList<Task> ts){
            taskSet=ts;
            size = ts.size();
	}
    public ArrayList<Task> getTaskSet() {
            return taskSet;
	}
    public Task getTask(int i){
            return taskSet.get(i);
	}
    void sort(int type){
        Comparator sorter = new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                if(type == 1 && t1.getD() != t2.getD())
                    return t1.getD() - t2.getD();
                
                if(type == 2 && t1.getT() != t2.getT())
                    return t1.getT() - t2.getT();
                
                return t1.getId() - t2.getId();
            }
        };
        taskSet.sort(sorter);
    }        
}
