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
public class FileMessage implements Serializable {
    private String id;
    private String name;
    private String address;
    private int partNo;
    private long totalPartsNo;
    private long size;
    private byte[] data;

    public FileMessage(String id, String name, String address, int partNo, long totalPartsNo, long size, byte[] data) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.partNo = partNo;
        this.totalPartsNo = totalPartsNo;
        this.size = size;
        this.data = data;
    }

    public FileMessage() {
        this.id = "";
        this.name = "";
        this.address = "";
        this.partNo = 0;
        this.totalPartsNo = 0;
        this.data = null;
    }

    public String getAddress() {
        return address;
    }
    
    public byte[] getData() {
        return data;
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

    public long getSize() {
        return size;
    }

    public long getTotalPartsNo() {
        return totalPartsNo;
    }

    public byte[] toByteStream() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(bout);

        dout.writeUTF(this.address);
        dout.writeUTF(this.id);
        dout.writeUTF(this.name);
        dout.writeInt(this.partNo);
        dout.writeLong(this.totalPartsNo);
        dout.writeLong(size);
        if (this.data != null) dout.write(this.data);

        dout.flush();
        return bout.toByteArray();
    }

    public void fromByteStream(byte[] data) throws IOException {
        System.out.println("FROM BYTE STREAM FILE MESSAGE: " + new String(data));
        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        DataInputStream din = new DataInputStream(bin);

        this.address = din.readUTF();
        this.id = din.readUTF();
        this.name = din.readUTF();
        this.partNo = din.readInt();
        this.totalPartsNo = din.readLong();
        this.size = din.readLong();
        byte[] aux = new byte[din.available()];
        din.read(aux);
        this.data = aux;
    }
    
}
