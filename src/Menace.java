import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

public class Menace extends JFrame implements ActionListener, ChangeListener {

    public static int mode = 1;
    public static int state = 5;
    public static boolean myTurn = true;
    public static boolean whoStarts = true;
    public static JLabel turnLab = new JLabel();
    private static boolean switchPlayers = false;
    JPanel bins = new JPanel();
    int trials = 0;
    JSpinner slider = new JSpinner();
    ArrayList<Bin> binsList = new ArrayList<>();
    public static void main(String[] args)
    {
            for(String s : args){
                if(s.startsWith("f"))
                    Menace.myTurn = Menace.whoStarts = false;
                if(s.startsWith("s"))
                    Menace.switchPlayers = true;
            }
        new Menace();
    }

    public Menace()
    {
        super("Trial: ");

        Build();

    }

    private void Build() {
        UpdateTrials();

        Container contentPane = getContentPane();

        bins.setPreferredSize(new Dimension(750,400));
        bins.setLayout(new GridLayout(1,4));
        binsList.add(new Bin(this));
        binsList.add(new Bin(this));
        binsList.add(new Bin(this));
        binsList.add(new Bin(this));
        binsList.add(new Bin(this));
        for(Bin b : binsList)
        {
            bins.add(b);
        }
        contentPane.add(bins,BorderLayout.WEST);

        JPanel toolbar = new JPanel();
        JRadioButton take1 = new JRadioButton("Take 1",true);
        take1.addActionListener(this);
        toolbar.add(take1);

        JRadioButton take2 = new JRadioButton("Take 2");
        take2.addActionListener(this);
        toolbar.add(take2);
        JRadioButton take3 = new JRadioButton("Take 3");

        take3.addActionListener(this);
        toolbar.add(take3);

        ButtonGroup bg = new ButtonGroup();
        bg.add(take1);
        bg.add(take2);
        bg.add(take3);
        JButton sample = new JButton("Get Action");
        sample.addActionListener(this);
        toolbar.add(sample);

        toolbar.setPreferredSize(new Dimension(160,400));
        toolbar.setBackground(new Color(.7f,.4f,.15f,1f));

        toolbar.add(new JLabel("I take:"));
        slider.addChangeListener(this);
        slider.setValue(1);
        toolbar.add(slider);
        JButton playerTakes = new JButton("Take");
        playerTakes.addActionListener(this);
        toolbar.add(playerTakes);
        UpdateTurnLabel();
        toolbar.add(turnLab);

        JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Reset();

            }
        });
        toolbar.add(reset,BorderLayout.SOUTH);

        contentPane.add(toolbar,BorderLayout.EAST);

        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void Reset() {
        Bin.totalState = Bin.initState;
        state = Bin.initState;
        if(switchPlayers)
            Menace.whoStarts = !Menace.whoStarts;
        myTurn = Menace.whoStarts;
        trials = 0;
        UpdateTrials();
        while(binsList.size()>0)
        {
            Bin b = binsList.remove(0);
            bins.remove(b);
        }
        binsList.add(new Bin(this));
        binsList.add(new Bin(this));
        binsList.add(new Bin(this));
        binsList.add(new Bin(this));
        binsList.add(new Bin(this));
        for(Bin b : binsList)
            bins.add(b);
        validate();
        repaint();
    }

    @Override
    public void dispose() {
        super.dispose();
        System.exit(0);

    }

    private void UpdateTrials() {
        trials +=1;
        setTitle("Trial: "+trials);
    }

    private void UpdateTurnLabel() {
        turnLab.setText("It is "+(myTurn?"my turn.":"your turn."));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object X = actionEvent.getSource();

        if(X instanceof JButton)
        {
            JButton button = (JButton) X;
            String name = actionEvent.getActionCommand();
            if(state>0){
                if((name.equals("Take")))
                {
                    if(myTurn){
                        JOptionPane.showInternalMessageDialog(null,"It's my turn!!");
                    }else{
                        int x = (int)slider.getValue();
                        if(x>state)
                        {
                            JOptionPane.showInternalMessageDialog(null,"You can't take that many!!");
                            return;
                        }

                        state=state - x;

                        myTurn=!myTurn;
                    }
                }else if(name.equalsIgnoreCase("Get Action")){
                    if(!myTurn){
                        JOptionPane.showInternalMessageDialog(null,"It's your turn!!");
                    }else{
                        int x = Sample();
                        state=state - x;
                        myTurn=!myTurn;
                    }
                }
                validate();
                repaint();

                if(state<=0)
                {
                    JOptionPane.showMessageDialog(null,
                            "Game Over! "+(!myTurn?"I won!":"You won!"));//,"",JOptionPane.OK_OPTION);
                    System.out.println("POPUP");
                    state = Bin.initState;
                    System.out.println(state);
                    if(switchPlayers)
                        whoStarts = !whoStarts;
                    myTurn = whoStarts;
                    UpdateTurnLabel();
                    for(Bin b: binsList)
                    {
                        b.hasSeen = false;
                    }
                    UpdateTrials();
                    validate();
                    repaint();

                }
            }


        }else{
            JRadioButton rad= (JRadioButton) X;
            String name = rad.getText();
            switch(name.toLowerCase())
            {
                case "take 1":
                    mode = 1;
                    break;
                case "take 2":
                    mode = 2;
                    break;
                case "take 3":
                    mode =3;
                    break;
            }
        }
    }

    private int Sample() {
        for(Bin bin : binsList)
        {
            if(bin.state == state)
            {
                int o=0,b=0,p=0;
                for(Bead bead : bin.beads)
                {
                    if(bead instanceof OrangeBead)
                        o+=1;
                    else if(bead instanceof BlueBead)
                        b+=1;
                    else p+=1;
                }
                Random r = new Random();
                int s = r.nextInt(o+b+p);
                int select = 3;
                if(s < o)
                    select = 1;
                else if(s<o+b)
                    select = 2;

                System.out.println(s+ " : " + o + " : "+(o+b));
                bin.selected = select;
                return select;
            }
        }
        return 0;
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        JSpinner spinner = (JSpinner)changeEvent.getSource();
        int val = (int)spinner.getValue();
        if(val > 3)
            spinner.setValue(3);
        else if(val<1)
            spinner.setValue(1);
    }
}

class Bin extends JPanel implements MouseListener {

    public int selected = 0;
    boolean hasSeen = false;
    public static final int beadRad = 3;
    private final Menace parent;
    ArrayList<Bead> beads = new ArrayList<Bead>();
    public static int initState = 5;
    public static int totalState = initState;
    int state;
    public static final int initBeads = 5;
    public Bin(Menace parent)
    {
        this.parent = parent;
        setBackground(Color.lightGray);
        setBorder(new LineBorder(Color.black));
        addMouseListener(this);
        setSize(new Dimension(150,200));
        state = totalState;
        totalState--;

        for(int i = 0; i < Math.min(3,state); i++)
        {
            for(int j = 0 ; j < (state==5?initBeads*2: initBeads); j++)
            {
                Bead b;
                do {b=Bead.MakeBead(i + 1);
                }while(!checkCollision(b.p));
                beads.add(b);
            }
        }

    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        int beadDiam = 2*beadRad;
        for(Bead b : beads)
        {
            g2.setColor(b.color);
            int x = b.p.x;
            int y = b.p.y;
            g2.fillRect(x-beadRad,y-beadRad, beadDiam,beadDiam);
        }

        g2.setColor(new Color(.25f,.4f,.8f,1f));
        g2.fillRect(1,1,147,18);
        g2.setColor(new Color(.15f,.3f,.65f,1f));
        g2.drawRect(1,1,147,18);
        g2.setColor(Color.black);
        if(state>4)
            g2.fillRect(10-beadRad,10-beadRad,beadDiam,beadDiam);
        else
            g2.drawRect(10-beadRad,10-beadRad,beadDiam-1,beadDiam-1);
        if(state >3)
            g2.fillRect(20-beadRad,10-beadRad,beadDiam,beadDiam);
        else
            g2.drawRect(20-beadRad,10-beadRad,beadDiam-1,beadDiam-1);

        if(state >2)
            g2.fillRect(30-beadRad,10-beadRad,beadDiam,beadDiam);
        else
            g2.drawRect(30-beadRad,10-beadRad,beadDiam-1,beadDiam-1);

        if(state >1)
            g2.fillRect(40-beadRad,10-beadRad,beadDiam,beadDiam);
        else
            g2.drawRect(40-beadRad,10-beadRad,beadDiam-1,beadDiam-1);

        if(state >0)
            g2.fillRect(50-beadRad,10-beadRad,beadDiam,beadDiam);
        else
            g2.drawRect(50-beadRad,10-beadRad,beadDiam-1,beadDiam-1);
        if(selected>0)
        {
            Color c;
            if(selected==1)
                c=OrangeBead._color;
            else if(selected==2)
                c=BlueBead._color;
            else c=PinkBead._color;
            g2.setColor(c);
            g2.fillRect(50,175,50,50);
            g2.setColor(c.darker().darker());
            g2.drawRect(50,175,50,50);
            Font font = g2.getFont();
            g2.setFont(new Font("Cambria",1,48));
            g2.drawChars((selected + "").toCharArray(),0,1,60,220);
            g2.setFont(font);

        }

        if(parent.state == state){

            hasSeen = true;
            g2.setColor(new Color(.8f,.2f,.1f,1f));
            g2.drawRect(1,1,148,398);
            g2.drawRect(2,2,146,396);
        }
        else if(hasSeen){

            g2.setColor(new Color(.2f,.7f,.1f,1f));
            g2.drawRect(1,1,148,398);
            g2.drawRect(2,2,146,396);
        }
        int o=0,b=0,p=0;
        for(Bead bead : beads)
        {
            if(bead instanceof OrangeBead)
                o++;
            else if(bead instanceof BlueBead)
                b++;
            else p++;
        }
        g2.setColor(Color.white);
        String s = o+"/"+b+"/"+p;
        g2.drawChars(s.toCharArray(),0,s.length(),150-s.length()*8,15);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if(mouseEvent.getButton()==1){
        Point p = mouseEvent.getPoint();
        int x = p.x;
        int y = p.y;
        if(x>beadRad && x<150-beadRad && y>20+beadRad && y<400-beadRad && checkCollision(p)){
            Bead bead;
            if(Menace.mode == 1)
            {
                bead = new OrangeBead(p);
            }else if(Menace.mode == 2)
                bead = new BlueBead(p);
            else bead = new PinkBead(p);
            beads.add(bead);
        }}else if(mouseEvent.getButton()==3){
            Bead rem = null;
            for(int i = 0; i<beads.size(); i++)
            {
                Bead b = beads.get(i);
                if(parent.mode == 1)
                {
                    if(b instanceof OrangeBead)
                    {
                        beads.remove(i);
                        break;
                    }
                }else if(parent.mode == 2)
                {

                    if(b instanceof BlueBead)
                    {
                        beads.remove(i);
                        break;
                    }
                }else
                {
                    if(b instanceof PinkBead)
                    {
                        beads.remove(i);
                        break;
                    }
                }

            }
        }
        selected = 0;
        validate();
        this.repaint();
    }

    private boolean checkCollision(Point p) {
        int x = p.x;
        int y = p.y;

        for(Bead b : beads)
        {
            Point p2 = b.p;
            int x2 = p2.x;
            int y2 = p2.y;
            int xAbs = Math.abs(x-x2);
            int yAbs = Math.abs(y-y2);

            if(xAbs <= 2*beadRad && yAbs<=2*beadRad)
                return false;
        }
        return true;
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}

class Bead{
    public Color color; // orange blue pink
    public Point p;

    public Bead(Color color, Point p)
    {
        this.color = color;
        this.p=p;
    }

    public static Bead MakeBead(int j) {
        Random r = new Random();
        Point p = new Point(r.nextInt(135)+5,r.nextInt(370)+25);

        if(j==1)
        {
            return new OrangeBead(p);
        }else if (j==2)
        {
            return new BlueBead(p);
        }return new PinkBead(p);
    }
}

class BlueBead extends Bead{

    static final Color _color = new Color(.2f,.4f,.8f,1f);
    public BlueBead(Point p)
    {
        super(_color,p);
    }
}
class OrangeBead extends Bead{

    static final Color _color = new Color(.65f,.3f,.1f,1f);
    public OrangeBead(Point p)
    {
        super(_color,p);
    }
}
class PinkBead extends Bead{
    static final Color _color = new Color(.8f,.2f,.6f,1f);

    public PinkBead(Point p)
    {
        super(_color,p);
    }
}