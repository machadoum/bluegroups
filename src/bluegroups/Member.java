/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluegroups;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import bluegroups.util.Serializable;

/**
 *
 * @author Thyago Salvá
 */
public class Member implements Serializable {

    // Atributes
    private String address;
    private String name;
    private String master;
    private    int rank;

    // Constructors
    public Member() {
        this("", "", "", -1, null);
    }

    public Member(String address, String name, String master, int rank) {
        this(address, name, master, rank, null);
    }

    public Member(String address, String name, String master, int rank, byte[] data) {
        this.address = address;
           this.name = name;
         this.master = master;
           this.rank = rank;
        try {
            fromByteStream(data);
        } catch(Exception e) {}
    }

    public Member(byte[] data) {
        this("", "", "", -1, data);
    }

    // Sets/Gets
    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getMaster() {
        return this.master;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return this.rank;
    }

    // Methods
    public byte[] toByteStream() throws IOException {

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(bout);

        dout.writeUTF(this.address);
        dout.writeUTF(this.name);
        dout.writeUTF(this.master);
        dout.writeInt(this.rank);

        dout.flush();
        return bout.toByteArray();
    }

    public void fromByteStream(byte[] data) throws IOException {
        if (data != null) {
            ByteArrayInputStream bin = new ByteArrayInputStream(data);
            DataInputStream din = new DataInputStream(bin);

            this.address = din.readUTF();
               this.name = din.readUTF();
             this.master = din.readUTF();
               this.rank = din.readInt();
        }
    }
}
