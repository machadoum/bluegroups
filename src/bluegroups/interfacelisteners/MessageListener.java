/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluegroups.interfacelisteners;

import bluegroups.messages.Pacote;

/**
 *
 * @author Thyago Salvá
 */
public interface MessageListener {

    // Methods
    public void receive(Pacote m);
}
