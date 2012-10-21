/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chat;

import java.util.Vector;
import javax.microedition.lcdui.Form;

/**
 *
 * @author Thyago Salvá
 */
public class FormMessage extends Form {

    // Atributes
    private Vector historico;

    // Constructor
    public FormMessage() {
        super("Histórico");
        historico = new Vector();
    }

    public void add(String texto) {
        System.out.println("FUNCIONOU");
        append(texto);
    }
}
