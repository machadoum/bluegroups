package velha;

import bluegroups.group.Group;
import bluegroups.interfacelisteners.ReceiveListener;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;
import java.util.Random;
import java.util.Vector;

class Jogo extends Canvas
{   private VelhaMidlet midlet;
    private static final int
        TECLA_0 = 0,
        TECLA_1 = 1,
        TECLA_2 = 2,
        TECLA_3 = 3,
        TECLA_4 = 4,
        TECLA_5 = 5,
        TECLA_6 = 6,
        TECLA_7 = 7,
        TECLA_8 = 8,
        TECLA_9 = 9,
        TECLA_DIREITA = 10,
        TECLA_ESQUERDA = 11,
        TECLA_CIMA = 12,
        TECLA_BAIXO = 13;
    private static boolean tecla[] = {false,false,false,false,false,false,false,false,false,false,false,false,false,false};
    private int corfundo[] = {255,255,255};
    private int corgrade[] = {0,0,0};
    private int corx[] = {0,0,255};
    private int coro[] = {255,0,0};
    public int tabuleiro[] = {0,0,0,0,0,0,0,0,0};
    private int jogou = 0;
    private int acabou = 0;
    private Random rnd = new Random();

    private int n;


    private Group group;
    public ReceiveListener receiveListener;

    public Jogo(VelhaMidlet m)
    {   midlet = m;
        n = 0;
        receiveListener = new ReceiveListener() {

            public void view(Vector view) { }

            public void receive(byte[] data) {
                tabuleiro[Integer.parseInt(new String(data))-1] = (n==1)?2:1;
                repaint();
            }

            public void failure(byte[] data) { }
        };
    }

    public void executa() throws Exception { while(true) Thread.sleep(100); }

    public int getWidth() { return(240); }

    public int getHeight() { return(290); }

    protected void paint(Graphics g)
    {   int i,x,y,d = 5;

        g.setColor(corfundo[0],corfundo[1],corfundo[2]);
        g.fillRect(0,0,getWidth(),getHeight());
        g.setColor(corgrade[0],corgrade[1],corgrade[2]);
        g.drawLine(0,(int)(getHeight()/3),getWidth(),(int)(getHeight()/3));
        g.drawLine(0,(int)(2*getHeight()/3),getWidth(),(int)(2*getHeight()/3));
        g.drawLine((int)(getWidth()/3),0,(int)(getWidth()/3),getHeight());
        g.drawLine((int)(2*getWidth()/3),0,(int)(2*getWidth()/3),getHeight());
        x = d;
        y = d;
        for (i = 0; (i < 9); i++)
        {   g.setColor(corgrade[0],corgrade[1],corgrade[2]);
            g.drawString(Integer.toString(i+1),x,y,Graphics.TOP|Graphics.LEFT);
            if (tabuleiro[i] == 1)
            {   g.setColor(corx[0],corx[1],corx[2]);
                g.drawLine(x,y,x+(int)(getWidth()/3)-2*d,y+(int)(getHeight()/3)-2*d);
                g.drawLine(x,y+(int)(getHeight()/3)-2*d,x+(int)(getWidth()/3)-2*d,y);
            }
            if (tabuleiro[i] == 2)
            {   g.setColor(coro[0],coro[1],coro[2]);
                g.drawArc(x,y,(int)(getWidth()/3)-2*d,(int)(getHeight()/3)-2*d,0,360);
            }
            x += (int)(getWidth()/3);
            if (x > getWidth())
            {   x = d;
                y += (int)(getHeight()/3);
            }
        }
    }

    public void keyPressed(int k) {
        super.keyPressed(k);
        int k1 = k - 48;
        if ((k-48) > 0 && (k-48) <= 9) {
            tabuleiro[k1-1] = n;
            repaint();
            midlet.group.send(""+(k-48), null);
        } else if (k-48 == 0) {
            midlet.destroyApp(true);
            midlet.notifyDestroyed();
        }
        if (k == getKeyCode(RIGHT)) { n = 1; }
        if (k == getKeyCode(LEFT))  { n = 2; }
    }

    public void keyReleased(int k) { super.keyReleased(k); }
}

public final class VelhaMidlet extends MIDlet
{   public Jogo jogo = new Jogo(this);
    public Group group;


    public VelhaMidlet()
    {   super();
    }

    protected void startApp()
    {   Display.getDisplay(this).setCurrent(jogo);

        group = new Group();
        group.join("velha");
        group.setReceiveListener(jogo.receiveListener);
        try {
            jogo.executa();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void pauseApp() {}

    protected void destroyApp(boolean b) {}
}

