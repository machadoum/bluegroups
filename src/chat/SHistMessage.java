/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chat;

import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

/**
 *
 * @author Thyago Salvá
 */
public class SHistMessage extends Form implements CommandListener {

    //Atributos
    private Command btCriar, btDesconectar;
    private Vector hist;
    private Chat1 chat;

    public SHistMessage(Chat1 chat) {
        super("Histórico");
        this.chat = chat;
        this.hist = new Vector();
        initHistMessage();
    }

    private void initHistMessage() {

        //Command
        btCriar = new Command("Criar", Command.OK, 1);
        btDesconectar = new Command("Desconectar", Command.EXIT, 2);

        addCommand(btCriar);
        addCommand(btDesconectar);
        setCommandListener(this);
    }

    public void addSI(StringItem si) {
        this.hist.addElement(si);
        this.deleteAll();
        int length = this.hist.size();
        int i = (length-5>=0)?length-5:0;
        for (; i < length; i++) {
            append((StringItem)this.hist.elementAt(i));
        }
    }

    public void commandAction(Command c, Displayable d) {
//        if (c == btCriar) {
//            chat.setCurrent(chat.getBoxMessage());
//        }
//        if (c == btDesconectar) {
//            if (chat.cs != null)
//                chat.cs.desconectar();
//            else
//                chat.cc.desconectar();
//        }
    }
}
