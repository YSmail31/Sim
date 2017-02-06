package sim;

public class Task1 extends Task
{
    private int deadl_abs,date_activ,rest;
    public Task1(int id, int c, int d, int t,int dA,int dAbs) 
    {
        super(id, c, d, t);
        deadl_abs=dA;
        date_activ=dAbs;
        rest=0;
    }
    public Task1(int id, int c, int d, int t,int dA,int dAbs,int r) 
    {
        super(id, c, d, t);
        deadl_abs=dA;
        date_activ=dAbs;
        rest=r;
    }

    public int getRest() {
        return rest;
    }

    public void setRest() 
    {
        this.rest --;
    }

    public void setDeadl_abs() {
        deadl_abs +=t;
    }

    public void setDate_activ() {
        date_activ +=t;
    }
    public int getDeadl_abs() {
        return deadl_abs;
    }
    public int getDate_activ() {
        return date_activ;
    }
    public void setNewRest()
    {
        rest=c;
    }
}
