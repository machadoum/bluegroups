/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluegroups.messages;

import bluegroups.Member;
import bluegroups.View;
import bluegroups.filemanager.BGFile;
import bluegroups.filemanager.Repository;
import bluegroups.util.Serializable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Thyago Salvá
 */
public class Pacote implements Serializable {

    // Atributes
    private String source;
    private String sender;
    private String receiver;
    private String destination;
    private int code;
    private byte[] data;

    // Constructors
    public Pacote() {
        this("", "", 0, null);
    }

    public Pacote(String sender, String receiver, int code) {
        this(sender, receiver, code, null);
    }

    public Pacote(String sender, int code, Object o) {
        this(sender, "", code, o);
    }

    public Pacote(String sender, String receiver, int code, Object o) {
        this.source = sender;
        this.sender = sender;
        this.receiver = receiver;
        this.destination = receiver;
        this.code = code;
        setObject(o);
    }


    // Sets / Gets
    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return this.source;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return this.sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiver() {
        return this.receiver;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setObject(Object o) {
        if (o != null) {
            byte[] data = null;
            try {
                if (o instanceof View)
                    data = ((View)o).toByteStream();
                else if (o instanceof String)
                    data = ((String)o).getBytes();
                else if (o instanceof Member)
                    data = ((Member)o).toByteStream();
                else if (o instanceof Repository) {
                    System.out.println("aaa");
                    data = ((BGFile)o).toByteStream();
                    System.out.println("bb");
                }                    
                else
                    data = (byte[])o;
            } catch(IOException e) { System.out.println("SetObjeto: " + e.getMessage()); }
            this.data = data;
        }
    }

    // Methods
    public String toString() {
        String aux;
        aux = "Source: " + source;
        aux += "Sender: " + sender;
        aux += "\nReceiver: " + receiver;
        aux += "\nDestination: " + destination;
        aux += "\nCode: " + code;
        aux += "\nData: " + new String(data);
        return aux;
    }

    public byte[] toByteStream() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(bout);

        dout.writeUTF(source);
        dout.writeUTF(sender);
        dout.writeUTF(receiver);
        dout.writeUTF(destination);
        dout.writeInt(code);
        if (data != null) dout.write(data);

        dout.flush();
        return bout.toByteArray();
    }

    public void fromByteStream(byte[] data) throws IOException {
        System.out.println("FROM BYTE STREAM: " + new String(data));
        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        DataInputStream din = new DataInputStream(bin);

        setSource(din.readUTF());
        setSender(din.readUTF());
        setReceiver(din.readUTF());
        setDestination(din.readUTF());
        setCode(din.readInt());
        byte[] aux = new byte[din.available()];
        din.read(aux);
        setData(aux);
    }

}
