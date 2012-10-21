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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import bluegroups.util.Serializable;

/**
 *
 * @author Thyago Salvá
 */
public class View implements Serializable {

    // Attributes
    private Hashtable members;

    // Constructors
    public View() {
        this(new Hashtable());
    }

    public View(Hashtable members) {
        this.members = members;
    }

    // Sets/Gets
    public void setMembers(Hashtable members) {
        this.members.clear();
        Object key;
        for(Enumeration en = members.keys(); en.hasMoreElements();){
            key = en.nextElement();
            addMember((String)key, (Member)members.get(key));
        }
    }

    public Hashtable getMembers() {
        return this.members;
    }

    public Member get(Object key) {
        return (Member)this.members.get(key);
    }

    public String getMaster(Object key) {
        return ((Member)this.members.get(key)).getMaster();
    }

    // Methods
    public void addMember(Member member) {
        this.members.put(member.getAddress(), member);
    }

    public void addMember(String address, Member member) {
        this.members.put(address, member);
    }

    public void addMember(String address, String name, String master) {
        Member aux = new Member(address, name, master, 0);
        this.members.put(address, aux);
    }

    public void removeMember(String address) {
        if (members.containsKey(address)) {
            members.remove(address);
            testeRecursivo(address);
        }
    }

    public void testeRecursivo(String address) {
        String key;
        for (Enumeration en = members.keys(); en.hasMoreElements();) {
            key = (String)en.nextElement();
            if (((Member)members.get(key)).getMaster().equals(address)) {
                members.remove(key);
                testeRecursivo(key);
            }
        }
    }

    public boolean isMaster(String address) {
        if (((Member)members.get(address)).getMaster().equals(address)) return true;
        return false;
    }

    public Vector getView() {
        Vector ht = new Vector();
        Object key;
        for (Enumeration en = members.keys(); en.hasMoreElements();) {
            key = en.nextElement();
            ht.addElement(members.get(key));
        }
        return ht;
    }

    public String toString() {
        Object key;
        String aux = "";
        for (Enumeration en = members.keys(); en.hasMoreElements();) {
            key = en.nextElement();
            aux += key + "*" + ((Member)members.get(key)).getMaster() + ";";
        }
        return aux;
    }

    // Serialize
    public byte[] toByteStream() throws IOException {
        
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(bout);

        Object key;
        byte[] aux = null;
        for (Enumeration en = members.keys(); en.hasMoreElements();) {
            key = en.nextElement();
            dout.writeUTF(key.toString());
            aux = ((Member)members.get(key)).toByteStream();
            dout.writeInt(aux.length);
            dout.write(aux);
        }
        dout.flush();
        return bout.toByteArray();
    }

    public void fromByteStream(byte[] data) throws IOException {
        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        DataInputStream din = new DataInputStream(bin);
        String key;
        int i = 0;

        members.clear();
        while(din.available() > 0) {
            key = din.readUTF();
            i = din.readInt();
            byte[] aux = new byte[i];
            din.read(aux);
            members.put(key, new Member(aux));
        }
    }
}
