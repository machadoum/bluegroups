/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluegroups.group;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import bluegroups.interfacelisteners.AcceptListener;

/**
 *
 * @author Thyago Salvá
 */
public class TConnection extends Thread {

    // Atributes
    private         String connectionURL;
    private AcceptListener acceptListener;

    // Constructors
    public TConnection(String connectionURL) { this.connectionURL = connectionURL; }

    // Listerners
    public void setAcceptLister(AcceptListener listener) { acceptListener = listener; }


    // Methods
    public void run() {
        StreamConnection connection_aux;
        try {
            connection_aux = (StreamConnection)Connector.open(connectionURL);
            acceptListener.request(connection_aux);
            
        } catch(Exception e) {}
    }
}
