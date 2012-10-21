/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 
package chat;

import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import bluegroups.filemanager.Diretorio;
import bluegroups.filemanager.deprecated.VersionManager;

*//**
 * @author Rafael
 *//*
public class Tela extends MIDlet implements CommandListener {
    
    private boolean midletPaused = false;
    private List lista;
    private Display display;
    private Command exit, update, checkout;
    private VersionManager versionM;
    //<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    //</editor-fold>//GEN-END:|fields|0|
    *//**
     * The Tela constructor.
     *//*
    public Tela() {
        display = this.getDisplay();
        exit = new Command("Exit", Command.EXIT, 0);
        update = new Command("Update", Command.OK, 0);
        checkout = new Command("CheckOut", Command.OK, 0);
    }

    //<editor-fold defaultstate="collapsed" desc=" Generated Methods ">//GEN-BEGIN:|methods|0|
    //</editor-fold>//GEN-END:|methods|0|
    //<editor-fold defaultstate="collapsed" desc=" Generated Method: initialize ">//GEN-BEGIN:|0-initialize|0|0-preInitialize
    *//**
     * Initilizes the application.
     * It is called only once when the MIDlet is started. The method is called before the <code>startMIDlet</code> method.
     *//*
    private void initialize() {//GEN-END:|0-initialize|0|0-preInitialize
        versionM = new VersionManager();
        lista = new List("Teste", List.IMPLICIT);
        lista.addCommand(exit);
        lista.addCommand(checkout);
        lista.addCommand(update);
        lista.setCommandListener(this);
        display.setCurrent(lista);
        // write pre-initialize user code here
//GEN-LINE:|0-initialize|1|0-postInitialize
        // write post-initialize user code here
    }//GEN-BEGIN:|0-initialize|2|
    //</editor-fold>//GEN-END:|0-initialize|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: startMIDlet ">//GEN-BEGIN:|3-startMIDlet|0|3-preAction
    *//**
     * Performs an action assigned to the Mobile Device - MIDlet Started point.
     *//*
    public void startMIDlet() {//GEN-END:|3-startMIDlet|0|3-preAction
        // write pre-action user code here
//GEN-LINE:|3-startMIDlet|1|3-postAction
        // write post-action user code here
    }//GEN-BEGIN:|3-startMIDlet|2|
    //</editor-fold>//GEN-END:|3-startMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: resumeMIDlet ">//GEN-BEGIN:|4-resumeMIDlet|0|4-preAction
    *//**
     * Performs an action assigned to the Mobile Device - MIDlet Resumed point.
     *//*
    public void resumeMIDlet() {//GEN-END:|4-resumeMIDlet|0|4-preAction
        // write pre-action user code here
//GEN-LINE:|4-resumeMIDlet|1|4-postAction
        // write post-action user code here
    }//GEN-BEGIN:|4-resumeMIDlet|2|
    //</editor-fold>//GEN-END:|4-resumeMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: switchDisplayable ">//GEN-BEGIN:|5-switchDisplayable|0|5-preSwitch
    *//**
     * Switches a current displayable in a display. The <code>display</code> instance is taken from <code>getDisplay</code> method. This method is used by all actions in the design for switching displayable.
     * @param alert the Alert which is temporarily set to the display; if <code>null</code>, then <code>nextDisplayable</code> is set immediately
     * @param nextDisplayable the Displayable to be set
     *//*
    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {//GEN-END:|5-switchDisplayable|0|5-preSwitch
        // write pre-switch user code here
        Display display = getDisplay();//GEN-BEGIN:|5-switchDisplayable|1|5-postSwitch
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }//GEN-END:|5-switchDisplayable|1|5-postSwitch
        // write post-switch user code here
    }//GEN-BEGIN:|5-switchDisplayable|2|
    //</editor-fold>//GEN-END:|5-switchDisplayable|2|

    *//**
     * Returns a display instance.
     * @return the display instance.
     *//*
    public Display getDisplay() {
        return Display.getDisplay(this);
    }

    *//**
     * Exits MIDlet.
     *//*
    public void exitMIDlet() {
        switchDisplayable(null, null);
        destroyApp(true);
        notifyDestroyed();
    }

    *//**
     * Called when MIDlet is started.
     * Checks whether the MIDlet have been already started and initialize/starts or resumes the MIDlet.
     *//*
    public void startApp() {
        if (midletPaused) {
            resumeMIDlet();
        } else {
            initialize();
            startMIDlet();
        }
        midletPaused = false;
    }

    *//**
     * Called when MIDlet is paused.
     *//*
    public void pauseApp() {
        midletPaused = true;
    }

    *//**
     * Called to signal the MIDlet to terminate.
     * @param unconditional if true, then the MIDlet has to be unconditionally terminated and all resources has to be released.
     *//*
    public void destroyApp(boolean unconditional) {
    }
    
    
    public void commandAction(Command c, Displayable s) {
        if (c == exit) {
            destroyApp(false);
            notifyDestroyed();
        }
        
        if (c == update) {
            try {
                //Arquivo que será recebido com informações sobre o repositório                
                FileConnection file = (FileConnection)Connector.open("file:///root1/repTeste/RepPrototipo.xml", Connector.READ);
                versionM.getDistributedRepository().update(file);
            }
            catch (Exception e) {
                System.out.println("Erro no update: "+e.getMessage());
            }
        }   
        
        if (c == checkout) {
            try {
               FileConnection file = (FileConnection)Connector.open("file:///root1/repTeste/RepPrototipo.xml", Connector.READ);
               versionM.getDistributedRepository().checkout(new Diretorio("pastaLocal"), file);
            }
            catch(IOException e) {
                System.out.println("ErroIO: "+e.getMessage());
            }

            catch(Exception e) {
                System.out.println("Erro: "+e.getMessage());
            }
        }
        
    }
}
*/