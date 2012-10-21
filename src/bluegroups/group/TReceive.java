/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluegroups.group;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.DataInputStream;

import bluegroups.Code;
import bluegroups.interfacelisteners.AcceptListener;
import bluegroups.interfacelisteners.FailureListener;
import bluegroups.interfacelisteners.FileListener;
import bluegroups.interfacelisteners.MessageListener;
import bluegroups.interfacelisteners.RepositoryListener;
import bluegroups.interfacelisteners.ViewListener;
import bluegroups.messages.FileMessage;
import bluegroups.messages.Pacote;

/**
 *
 * @author Thyago Salvá
 */
public class TReceive extends Thread {

    // Atributes
    private        InputStream in;
    private             String addressRemote;
    private             String groupName;
    private       ViewListener viewListener;
    private    MessageListener messageListener;
    private     AcceptListener acceptListener;
    private    FailureListener failureListener;
    private RepositoryListener repositoryListener;
    private       FileListener fileListener;

    // Constructors
    public TReceive(InputStream in, String addressRemote) { this.in = in; this.addressRemote = addressRemote; }

    // Listerners
    public void setViewListener(ViewListener listener) { viewListener = listener; }

    public void setMessageListener(MessageListener listener) { messageListener = listener; }

    public void setAcceptListener(AcceptListener listener) { acceptListener = listener; }

    public void setFailureListener(FailureListener listener) { failureListener = listener; }

    public void setReposirotyListener(RepositoryListener listener) { repositoryListener = listener; }
    
    public void setFileListener(FileListener listener) { fileListener = listener; }
    
    // Methods
    public byte[] receive() throws Exception {
        byte[] data = null;

        int length = in.read();
        if (length == -1) throw new Exception(Code.FAILURE + "");
        length = in.available();
        data = new byte[length];
        length = 0;

        while (length != data.length) {

            int ch = in.read(data, length, data.length - length);

            if (ch == -1) throw new Exception("Não pode ler data");

            length += ch;
        }
        return data;
    }
    
    public void run() {
        byte[] data;
        Pacote p;
        p = new Pacote();
        while (true) {
            try {
                data = receive();
                p.fromByteStream(data);
                switch (p.getCode()) {
                    case Code.VIEW:
                        viewListener.viewAccept(p);
                        break;
                    case Code.MESSAGE:
                        messageListener.receive(p);
                        break;
                    case Code.ACCEPT_NOTIFICATION:
                        System.out.println("ACCEPT_NOTIFICATION");
                        acceptListener.acceptNotification(p.getData());
                        break;
                    case Code.FAILURE_NOTIFICATION:
                        System.out.println("FAILURE_NOTIFICATION: " + p.toString());
                        failureListener.failureNotification(p.getData());
                        break;
                    case Code.LEAVE:
                        acceptListener.leave(p.getSender());
                        break;
                    case Code.CHECKOUT:
                        repositoryListener.checkout_detected(p.getSource());
                        break;
                    case Code.FILE:
                        System.out.println(">FILE: "+new String(p.getData()));
                        FileMessage message = new FileMessage();
                        message.fromByteStream(data);
                        if (message.getAddress().equals("") && message.getName().equals("promocoes")) {
                            repositoryListener.repository_received(message.getData(), p.getSource());
                        } else {
                            fileListener.file_received(message);
                        }
                        break;
                    case Code.FILE_REQUEST:
                        fileListener.file_requested(p.getData());
                        break;
                    default:
                        System.out.println("Código inválido: " + p.toString());
                }
            } catch(Exception e) {
                System.out.println(addressRemote + " falhou...." + e.getMessage());
                if (e.getMessage() != null)
                    failureListener.failureDetected(addressRemote);
                break;
            }
        }
    }
}
