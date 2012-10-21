/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chat;

import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

import bluegroups.group.Group;
import bluegroups.interfacelisteners.ReceiveListener;

/**
 * @author Thyago Salvá
 */
public class Chat extends MIDlet {

    // Atributes
    private Group group;
    private BoxMessage box;
    private FormMessage form;
    private ListView list;
    private Display display;
    private Alert alerta;
    private Command btCriar, btDesconectar, btEnviar, btVoltar, btView, btCheckout;

    private ReceiveListener receiveListener;
    private CommandListener commandListener;

    // Constructor
    public Chat() { }

    public void startApp() {
    	display = Display.getDisplay(this);

        receiveListener = new ReceiveListener() {

            public void view(Vector view) {
                list.setMembers(view);
            }

            public void receive(byte[] data) {
                form.add(new String(data));
            }

            public void failure(byte[] data) {
                alerta.setString(new String(data));
                alerta.setType(AlertType.ALARM);
                display.setCurrent(alerta);
            }
        };

        commandListener = new CommandListener() {

            public void commandAction(Command c, Displayable d) {
                if (d == form && c == btCriar) {
                    box.setReceiver(null);
                    display.setCurrent(box);
                }

                if (d == form && c == btDesconectar) {
                    group.leave();
                }

                if (d == box && c == btEnviar) {
                    group.send(box.getString(), box.getReceiver());
                    box.setString("");
                    display.setCurrent(form);
                }

                if ((d == box || d == list) && c == btVoltar) {
                    display.setCurrent(form);
                }

                if (d == form && c == btView) {
                    display.setCurrent(list);
                }
                
                if (d == form && c == btCheckout) {
                    group.checkout();
                }

                if (d == list && c == btCriar) {
                    box.setReceiver(list.get(list.getSelectedIndex()).getAddress());
                    display.setCurrent(box);
                }
            }
        };
        alerta = new Alert("Aviso");

        group = new Group();
        group.setReceiveListener(receiveListener);
        group.join("chat");



              btCriar = new Command("Criar", Command.OK, 1);
        btDesconectar = new Command("Desconectar", Command.EXIT, 2);
             btEnviar = new Command("Enviar", Command.OK, 1);
               btView = new Command("View", Command.OK, 2);
             btVoltar = new Command("Voltar", Command.BACK, 1);
           btCheckout = new Command("Checkout", Command.OK, 3);


        list = new ListView("Usuários");
        list.addCommand(btCriar);
        list.addCommand(btVoltar);
        list.setCommandListener(commandListener);

        box = new BoxMessage();
        box.addCommand(btVoltar);
        box.addCommand(btEnviar);
        box.setCommandListener(commandListener);

        form = new FormMessage();
        form.addCommand(btCriar);
        form.addCommand(btView);
        form.addCommand(btCheckout);
        form.addCommand(btDesconectar);
        form.setCommandListener(commandListener);
        display.setCurrent(form);
    }

    public void pauseApp() { }

    public void destroyApp(boolean unconditional) { }
}

/*
 Hashtable teste = new Hashtable();
        teste.put("A", "A");
        teste.put("B", "A");
        teste.put("C", "A");
        teste.put("D", "B");
        teste.put("E", "B");
        teste.put("G", "E");
        teste.put("H", "E");
        teste.put("I", "G");
        teste.put("J", "G");

        Object key;
        String aux = "";
        for (Enumeration en = teste.keys(); en.hasMoreElements();) {
            key = en.nextElement();
            aux += key + "*" + teste.get(key) + ";";
        }
        System.out.println("VIEW: " + aux);

        teste.remove("B");
        testeRecursivo(teste, "B");

        aux = "";
        for (Enumeration en = teste.keys(); en.hasMoreElements();) {
            key = en.nextElement();
            aux += key + "*" + teste.get(key) + ";";
        }
        System.out.println("VIEW: " + aux);

        public void testeRecursivo(Hashtable t, String address) {
            String key;
            for (Enumeration en = t.keys(); en.hasMoreElements();) {
                key = (String)en.nextElement();
                if (address == t.get(key)) {
                    t.remove(key);
                    testeRecursivo(t, key);
                }

            }
        }

 */