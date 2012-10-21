/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluegroups.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import bluegroups.util.Serializable;

/**
 *
 * @author Rafael
 */
public class FileRequestMessage implements Serializable {
    private String source;
    private String id;
    private String name;
    private String adress;
    private int partNo;

    public FileRequestMessage(String id, String name, String adress, int partNo) {
        this.id = id;
        this.name = name;
        this.adress = adress;
        this.partNo = partNo;
    }

    public FileRequestMessage() {
        this.source = "";
        this.id = "";
        this.name = "";
        this.adress = "";
        this.partNo = 0;
    }

    public String getSource() {
        return source;
    }
    
    public String getAddress() {
        return adress;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPartNo() {
        return partNo;
    }

    public byte[] toByteStream() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(bout);
        
        dout.writeUTF(this.source);
        dout.writeUTF(this.adress);
        dout.writeUTF(this.id);
        dout.writeUTF(this.name);
        dout.writeInt(this.partNo);

        dout.flush();
        return bout.toByteArray();
    }

    public void fromByteStream(byte[] data) throws IOException {
        System.out.println("FROM BYTE STREAM FILE MESSAGE: " + new String(data));
        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        DataInputStream din = new DataInputStream(bin);

        this.source = din.readUTF();
        this.adress = din.readUTF();
        this.id = din.readUTF();
        this.name = din.readUTF();
        this.partNo = din.readInt();
    }
    
}
