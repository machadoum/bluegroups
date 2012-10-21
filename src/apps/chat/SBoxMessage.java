/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chat;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;

/**
 *
 * @author Thyago Salvá
 */
public class SBoxMessage extends TextBox implements CommandListener {

    //Atributos
    private Command btEnviar, btVoltar;
    private Chat1 chat;

    //Construtor
    public SBoxMessage(Chat1 chat) {
        super("Campo de Mensagem", "", 120, TextField.ANY);
        this.chat = chat;
        initBoxMessage();
    }

    private void initBoxMessage() {

        //Command
        btEnviar = new Command("Enviar", Command.OK, 1);
        btVoltar = new Command("Voltar", Command.BACK, 2);

        //Configurando receptor de eventos
        addCommand(btEnviar);
        addCommand(btVoltar);
        setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
//
//        if (c == btVoltar) {
//            chat.setCurrent(chat.getHistMessage());
//        } else if ( c == btEnviar) {
//
//            if (this.getString().length() > 0) {
//                chat.getHistMessage().addSI(new StringItem("me: ", this.getString()));
//
//                if (chat.cs != null) {
//                    chat.cs.setMensagem(this.getString());
//                } else {
//                    chat.cc.setMensagem(this.getString());
//                }
//
//                this.setString("");
//
//                chat.setCurrent(chat.getHistMessage());
//            }
//        }
    }
}
