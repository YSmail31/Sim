package sim;

import java.io.IOException;
import java.util.ArrayList;

public class DMSimul extends Simul
{
    public DMSimul(TaskSet ts){
        super(ts);
    }
    public void simulNonPreampt(int t) throws IOException{
    type="DEADLINE_MONOTONIC NON_PREEMPTIF MONO CONSTRAINED";
    ArrayList<Task> ts=taskSet.getTaskSet();
    int horloge=0;
    init();
    for(Task tache:ts)
    {
        int deadl=tache.getD();
        int per=tache.getT();
        int c=tache.getC();
        int id=tache.getId();
        int deadl_abs=tache.getD();
        int date_activ=0;
        Task1 tacheEnAttante=new Task1(id, c, deadl , per, deadl_abs, date_activ);
        scheduler.add(tacheEnAttante);
    }
    //boucle de simulation de 0 a HyperPeriode
    while(horloge<t)
    {
        //le processus prêt le plus prioritaire
        int ind=0;
        boolean test=true;
        while(test && horloge<t)
        {
            ind=exist(scheduler, horloge);
            if(ind==0 && scheduler.get(0).getDate_activ()>horloge)
            {
                represent(-1, horloge);
                horloge++;
            }
            else
                test=false;
        }
        //simulation
        if(horloge<t)
        {
            //récupérer la tâche prête et prioritaire
            Task1 tacheEnExec=scheduler.get(ind);
            int id=tacheEnExec.getId();
            for(int i=0;i<tacheEnExec.getC();i++)
                    represent(id-1,horloge+i);
            horloge+=tacheEnExec.getC();
            tacheEnExec.setDate_activ();
            tacheEnExec.setDeadl_abs();
            scheduler.remove(ind);
            int r=0;
            while(r<scheduler.size() && tacheEnExec.getDate_activ()>scheduler.get(r).getDate_activ())
                r++;
            scheduler.add(r,tacheEnExec);
        }
    }
}	
    public void simulPreampt(int t) throws IOException{
        type="DEADLINE_MONOTONIC PREEMPTIF MONO CONSTRAINED";
        ArrayList<Task> ts=taskSet.getTaskSet();
        int horloge=0;
        init();
        for(Task tache:ts)
        {
            int deadl=tache.getD();
            int per=tache.getT();
            int c=tache.getC();
            int id=tache.getId();
            int deadl_abs=tache.getD();
            int date_activ=0;
            int rest=tache.getC();
            Task1 tacheEnAttante=new Task1(id, c, deadl , per, deadl_abs, date_activ,rest);
            scheduler.add(tacheEnAttante);
        }
        while(horloge<t)
        {
            boolean test=true;
            int ind=0;
            while(test && horloge<t)
            {
                ind=exist(scheduler, horloge);
                if(ind==0 && scheduler.get(0).getDate_activ()>horloge)
                {
                    represent(-1, horloge);
                    horloge++;
                }
                else
                    test=false;
            }
            if(!test)
            {
                Task1 tacheEnExec=scheduler.get(ind);
                int id=tacheEnExec.getId();
                represent(id-1,horloge);
                horloge+=1;
                tacheEnExec.setRest();
                if(tacheEnExec.getRest()==0)
                {
                    tacheEnExec.setDeadl_abs();
                    tacheEnExec.setNewRest();
                    tacheEnExec.setDate_activ();
                }
                scheduler.remove(ind);
                int r=0;
                while(r<scheduler.size() && tacheEnExec.getDate_activ()>scheduler.get(r).getDate_activ())
                    r++;
                scheduler.add(r,tacheEnExec);
            }
        }
    }
    private int exist(ArrayList<Task1> scheduler2, int horloge) {
		int i=0;
		int m=0;
		while(i<scheduler2.size() && scheduler2.get(i).getDate_activ()<=horloge)
		{
			if(scheduler2.get(i).getD()<scheduler2.get(m).getD() || (scheduler2.get(i).getD()==scheduler2.get(m).getD() && scheduler2.get(i).getId()>scheduler2.get(m).getId()))
				m=i;
			i++;
		}
		return m;
	}
    public boolean exactTest() {
		taskSet.sort(TaskSet.SORT_D);
		for(Task t:taskSet.getTaskSet())
		{	
			if(responseTimeTest(t, taskSet)>t.getD())
				return false;
		}
		return true;
	}
    public int responseTimeTest(Task ts, TaskSet sortedTS) {
		int r=ts.getC();
		int x;
		int i;
		do
		{
			x=r;
			i=0;
			int s=0;
			while(sortedTS.getTask(i).getId()!=ts.getId())
			{
				s+=Math.ceil(r/(double)sortedTS.getTask(i).getT())*sortedTS.getTask(i).getC();
				i++;
			}
			r=ts.getC()+s;
		}while(r!=x);
		return r;
	}
}
