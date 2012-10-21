/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chat;

import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;

/**
 *
 * @author Thyago Salvá
 */
public class BoxMessage extends TextBox {

    // Atributes
    private String receiver;

    // Constructor
    public BoxMessage() {
        super("Caixa de Texto", "", 1024, TextField.ANY);
        receiver = null;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiver() {
        return receiver;
    }
}

