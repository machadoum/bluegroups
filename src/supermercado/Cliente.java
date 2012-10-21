package supermercado;

import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;

import bluegroups.filemanager.BGFile;
import bluegroups.group.Group;
import bluegroups.interfacelisteners.ReceiveListener;

/**
 * @author Rafael
 */
public final class Cliente extends MIDlet implements CommandListener {
    
    private boolean midletPaused = false;
    private List lista;
    private Display display;
    private Command exit, viewList;    
    private Group group;
    private ReceiveListener receiveListener;
    private static final String REPOSITORY_ADDRESS = "repSuperMercado";
    private static final String FILE_ADDRESS = "";
    private int membersCounter;
    BGFile promocaoFile;

    /**
     * The constructor.
     */
    public Cliente() {        
        display = this.getDisplay();
        exit = new Command("Exit", Command.EXIT, 0);
        viewList = new Command("Ver Promoções", Command.OK, 0);
        group = new Group();  
        membersCounter = 0;
    }

    /**
     * Initilizes the application.
     * It is called only once when the MIDlet is started. The method is called before the <code>startMIDlet</code> method.
     */
    private void initializeButtons() {
        lista = new List("Promoções", List.IMPLICIT);
        lista.addCommand(exit);
        
        lista.setCommandListener(this);
        display.setCurrent(lista);
                
        lista.addCommand(viewList);
    }

    private void setupGroup() {
        receiveListener = new ReceiveListener() {
            public void view(Vector view) {
                if (view.size() > membersCounter) {
                    group.sincronyze();
                } 
                membersCounter = view.size();
            }
            public void receive(byte[] data) {
                
            }
            public void failure(byte[] data) {
            }
        };
        group.join("promocoes", REPOSITORY_ADDRESS);
        group.setReceiveListener(receiveListener);
    }

    /**
     * Exits MIDlet.
     */
    public void exitMIDlet() {
        switchDisplayable(null, null);
        destroyApp(true);
        notifyDestroyed();
    }

    /**
     * Called when MIDlet is started.
     * Checks whether the MIDlet have been already started and initialize/starts or resumes the MIDlet.
     */
    public void startApp() {
        if (midletPaused) {
            resumeMIDlet();
        } else {
            initializeButtons();
            setupGroup();
            startMIDlet();
        }
        midletPaused = false;
    }
    
    public void commandAction(Command c, Displayable s) {
        if (c == exit) {
            destroyApp(false);
            notifyDestroyed();
        }
        if (c == viewList) {
            showList();
        }
    }
	
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Resumed point.
     */
    public void resumeMIDlet() {
        // write pre-action user code here
        // write post-action user code here
    }	

    /**
     * Called when MIDlet is paused.
     */
    public void pauseApp() {
        midletPaused = true;
    }  
	
    /**
     * Switches a current displayable in a display. The <code>display</code> instance is taken from <code>getDisplay</code> method. This method is used by all actions in the design for switching displayable.
     * @param alert the Alert which is temporarily set to the display; if <code>null</code>, then <code>nextDisplayable</code> is set immediately
     * @param nextDisplayable the Displayable to be set
     */
    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {        
        Display currentDisplay = getDisplay();
        if (alert == null) {
            currentDisplay.setCurrent(nextDisplayable);
        } else {
            currentDisplay.setCurrent(alert, nextDisplayable);
        }       
    }

    /**
     * Performs an action assigned to the Mobile Device - MIDlet Started point.
     */
    public void startMIDlet() {        
    }
    
    /**
     * Returns a display instance.
     * @return the display instance.
     */
    public Display getDisplay() {
        return Display.getDisplay(this);
    }
    
    /**
     * Called to signal the MIDlet to terminate.
     * @param unconditional if true, then the MIDlet has to be unconditionally terminated and all resources has to be released.
     */
    public void destroyApp(boolean unconditional) {
    }

    private void showList() {
        BGFile file = getPromocaoFile();
        
        while (file.readLine()) {
            lista.append(file.getLine(), null);
        }
    }

    private BGFile getPromocaoFile() {
        if (promocaoFile == null) {
            promocaoFile = new BGFile(REPOSITORY_ADDRESS, FILE_ADDRESS, false);
        }
        return this.promocaoFile;
    }
}
