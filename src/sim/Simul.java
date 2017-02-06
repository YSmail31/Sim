package sim;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

public class Simul 
{
    protected TaskSet taskSet;//l'ensemble de tâches
    protected ArrayList<Task> ts;//liste de tâches
    protected ArrayList<Task1> scheduler;//la file d'attente de job
    protected String type;//type d'algorithme
    protected int nbr;//le nombre de tâches
    protected ArrayList<Forme> graph[];
    protected String trace[];
    protected int last[];
    public Simul(TaskSet ts) {
        taskSet=ts;
	this.ts=ts.getTaskSet();
	nbr=this.ts.size();
	scheduler=new ArrayList<>();
	graph=new ArrayList[nbr] ;
        trace=new String[nbr];
        last=new int[nbr];
        for(int i=0;i<nbr;i++)
        {
            trace[i]="\\multido{\\n=0+"+ts.getTask(i).getT()+"}{"+(ts.hyperPeriod()/ts.getTask(i).getT())+"}{\n"+"\\TaskArrDead{"+(i+1)+"}{\\n}{"+ts.getTask(i).getD()+"}}\n";
            last[i]=-1;
        }
    }
    //insérer l'information pour la représentation graphique
    public void represent(int ind,int horloge) throws IOException{
	for(int i=0;i<ts.size();i++)
	{
                
		if(i==ind)
                {
                    int t=horloge;
                    int t1=horloge+1;
                    graph[i].add(new Forme(26,6,Color.BLACK));
                    if(last[i]==horloge)
                    {
                        int indice=trace[i].lastIndexOf("{");
                        trace[i]=trace[i].substring(0,indice);
                        trace[i] += "{"+t1+"}\n";
                    }
                    else
                        trace[i] += "\\TaskExecution{"+(i+1)+"}{"+t+"}{"+t1+"}\n";
                    last[i]=t1;
                }
		else
                    graph[i].add(new Forme(26,1,Color.BLACK));
		int deadl_abs=(horloge+1)/ts.get(i).getT();
		deadl_abs*=ts.get(i).getT();
		deadl_abs+=ts.get(i).getD();
		if(deadl_abs==(horloge+1))
			graph[i].add(new Forme(3,1,Color.BLACK,Forme.DEADLINE));
		else if((horloge+1)%ts.get(i).getT()==0)
			graph[i].add(new Forme(3,1,Color.BLACK,Forme.PERIODE));
		else
			graph[i].add(new Forme(3,6,Color.BLACK));
	}
    }

    public String[] getTrace() {
        return trace;
    }
    //initialiser les variables de travail
    public void init(){
        scheduler=new ArrayList<>();
        for(int i=0;i<nbr;i++)
        {
            graph[i]=new ArrayList<>();
            graph[i].add(new Forme(3,16,Color.GREEN));
        } 
    }
    public void setGraph(ArrayList<Forme>[] graph) {
		this.graph = graph;
	}
    public String getType() {
		return type;
	}
    public void setType(String type) {
		this.type = type;
	}
    public ArrayList<Forme>[] getGraph() {
		return graph;
	}
}
