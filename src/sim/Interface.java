//cette interface gère l'affichage de l'application Serdouk Simulator
package sim;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;

public class Interface extends JFrame{        

    private final JPanel panneauBouton;
    private final JPanel panneauAjtSup;
    private final JPanel panneauPrincipal;
    private final JButton tester,executer,ajouter,suprimer;
    private final JScrollPane scrollTableau,scrollDessin;
    private final JComboBox algo=new JComboBox(new String[]{"DM","EDF"});
    private final JComboBox preamptif=new JComboBox(new String[]{"Preemptif","Non Preemptif"});
    private final JTable tableau;
    private final Model modele;
    private final Graph graph;
    protected File trace;
    protected FileWriter fw;
	public Interface() throws IOException
	{
            setTitle("Serdouk Simulator");
            setExtendedState(Frame.MAXIMIZED_BOTH);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            panneauAjtSup=new JPanel();
            panneauAjtSup.setOpaque(false);
            panneauBouton=new JPanel();
            panneauBouton.setOpaque(false);
            panneauPrincipal=new ArrierePlan();
            //panneau des boutons de contr�l de simulation
            tester=new JButton("");
            tester.setIcon(new ImageIcon(ImageIO.read(new File("info.png"))));
            tester.setPreferredSize(new Dimension(28,26));
            executer=new JButton("Simuler");
            ajouter=new  JButton("+");
            suprimer=new JButton("-");
            panneauBouton.add(tester);
            panneauBouton.add(executer);
            panneauBouton.add(algo);
            panneauBouton.add(preamptif);
            panneauBouton.add(ajouter);
            panneauBouton.add(suprimer);
            //mettre en place le tableau
            modele=new Model();
            tableau=new JTable(modele);
            scrollTableau=new JScrollPane(tableau);
            scrollTableau.setPreferredSize(new Dimension(1200, 150));
            //mettre en place le diagramme 
            graph=new Graph();
            scrollDessin=new JScrollPane(graph);
            scrollDessin.setPreferredSize(new Dimension(1200, 490));
            getContentPane().add(panneauPrincipal);
            panneauPrincipal.add(panneauBouton);
            panneauPrincipal.add(scrollTableau);
            panneauPrincipal.add(scrollDessin);
            definirLesAction();
            
	}
    @SuppressWarnings("empty-statement")
    private void definirLesAction() {
    	//lors d'un choix d'un algorithme
        algo.addActionListener((ActionEvent e) -> {
            tester.setEnabled(algo.getSelectedIndex()==0);
        });
        //ajouter une ligne dans le tableau lors de l'appui du boutton "+"
        ajouter.addActionListener((ActionEvent e) -> {
            modele.addRow();
            tableau.setModel(modele);
            modele.fireTableDataChanged();
        });
      //supprime une ligne dans le tableau lors de l'appui du boutton "-"
        suprimer.addActionListener((ActionEvent e) -> {
            int selected[]=tableau.getSelectedRows();
            for(int i=selected.length-1;i>=0;i--)
                modele.remRow(selected[i]);
            tableau.setModel(modele);
            modele.fireTableDataChanged();
        });
      //execute la simulation lors de l'appui du boutton "simuler"
        executer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean preamptive= preamptif.getSelectedIndex()==0;
                trace=new File("trace.tex");
                try {
                    fw = new FileWriter(trace);
                } catch (IOException ex) {
                    Logger.getLogger(Simul.class.getName()).log(Level.SEVERE, null, ex);
                }
                TaskSet ts=new TaskSet(modele.getList());
                int hp=ts.hyperPeriod();
                int nbTask=ts.getTaskSet().size();
                ArrayList<Forme>[] g=null;
                String type="";
                int h=ts.hyperPeriod();
                String trace[]=new String[ts.getTaskSet().size()];
                try
                {
                switch(algo.getSelectedIndex())
                {
                        case 0://si 'algorithme et le DM
                        {
                                DMSimul dm=new DMSimul(ts);
                                if(preamptive)//s'il est preemptif
                                        dm.simulPreampt(h);
                                else//sinon
                                        dm.simulNonPreampt(h);
                                g=dm.getGraph();
                                type=dm.type;
                                trace=dm.getTrace();
                        }break;
                        case 1://si 'algorithme et le EDF
                        {
                                EDFSimul edf=new EDFSimul(ts);
                                if(preamptive)//s'il est preemptif
                                        edf.simulPreampt(h);
                                else//sinon
                                        edf.simulNonPreampt(h);
                                g=edf.getGraph();
                                type=edf.type;
                                trace=edf.getTrace();
                        }break;
                }
                }
                catch(IOException exc){}
                dessiner(g, h, type,ts.getTaskSet());
                genTrace(hp, nbTask,trace);
            }

            private void dessiner(ArrayList<Forme>[] g, int h, String type, ArrayList<Task> ts) {
                graph.setForme(g,h,ts,type);
		scrollDessin.setViewportView(graph);
            }
        });
        tester.addActionListener((ActionEvent e) -> {
            //JOptionPane jop1 = new JOptionPane();
            String Message="L'ensemble de taches est ";
            if(new DMSimul(new TaskSet(modele.getList())).exactTest())
                Message+="ordonnançable";
            else
                Message+="non ordonnançable";
            JOptionPane.showMessageDialog(null, Message, "Test d'ordonnançabilité", JOptionPane.INFORMATION_MESSAGE);
        });
        scrollDessin.getViewport().addChangeListener((ChangeEvent e) -> {
            scrollDessin.repaint();
        });;
    }
    private void header(int hp, int nbTask) {
        try {
            int width=(int)(hp*0.2);
            fw.write("\\documentclass[12pt]{article}\n" + "\\usepackage{rtsched}\n" + "\\usepackage{lscape}\n" + "\\begin{document}\n"+ "\\begin{landscape}\n"+ "\\begin{RTGrid}[height=3cm,width="+width+"cm,labelsize=5pt,numbersize=4,symbol=.]{"+nbTask+"}{"+(1+hp)+"}\n");
        } catch (IOException ex) {
            Logger.getLogger(Simul.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void genTrace(int hp,int nbTask,String[] trace)
    {
        header(hp,nbTask);
        for(String info:trace)
            try {
                fw.write(info);
            } catch (IOException ex) {
                Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
            }
        footer();
        //convert();
    }

    private void footer() {
        try {
            fw.write("\\end{RTGrid} \n" + "\\end{landscape}\n" +"\\end{document}");
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(Simul.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String[] args) throws IOException {
		new Interface().setVisible(true);
	}

    private void convert() {
        try {
            Process p = Runtime.getRuntime().exec("latex trace.tex" );
            p.waitFor();
            p = Runtime.getRuntime().exec("dvips trace.ps trace" );
            p.waitFor();
            p = Runtime.getRuntime().exec("ps2pdf trace.ps" );
            p.waitFor();
        } catch (IOException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

