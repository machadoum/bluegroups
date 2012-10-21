/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chat;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

/**
 *
 * @author Thyago Salvá
 */
public class SFormAlerta extends Form implements CommandListener {

    //Atributos
    private StringItem si;
    private Command btDesconectar;
    private Chat1 chat;

    public SFormAlerta(Chat1 chat) {
        this("",chat);
    }

    public SFormAlerta(String msg, Chat1 chat) {
        super("Aviso");
        initForm(msg);
        this.chat = chat;
    }

    private void initForm(String msg) {
        si = new StringItem(null, msg);
        append(si);

        btDesconectar = new Command("Desconectar", Command.EXIT, 1);
        addCommand(btDesconectar);
        setCommandListener(this);
    }

    public void setText(String txt) {
        this.si.setText(txt);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == btDesconectar) {
//            chat.cs.desconectar();
        }
    }
}
