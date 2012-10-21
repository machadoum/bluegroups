/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluegroups.group;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import bluegroups.interfacelisteners.AcceptListener;

/**
 *
 * @author Thyago Salvá
 */
public class TAccept extends Thread {

    // Atributes
    private                  Service service;
    private StreamConnectionNotifier connection;
    private           AcceptListener acceptListener;

    // Constructors
    public TAccept(String nameGroup) { service = new Service(nameGroup); }

    // Listerners
    public void setAcceptLister(AcceptListener listener) { acceptListener = listener; }

    // Methods
    public void run() {
        StreamConnection connection_aux;

        try {
            connection = (StreamConnectionNotifier)Connector.open(service.toString());
            while (true) {
                connection_aux = connection.acceptAndOpen();
                acceptListener.requestDetected(connection_aux);
            }
        } catch(Exception e) { System.out.println("TAccept: " + e.getMessage()); }
    }
}
