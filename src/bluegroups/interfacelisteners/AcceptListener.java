/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluegroups.interfacelisteners;

import javax.microedition.io.StreamConnection;

/**
 *
 * @author Thyago Salvá
 */
public interface AcceptListener {

    // Methods
    public void requestDetected(StreamConnection connection);

    public void acceptNotification(byte[] data);

    public void request(StreamConnection connection);

    public void leave(String address);

}
