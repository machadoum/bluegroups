/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chat;

import bluegroups.Member;
import java.util.Vector;
import javax.microedition.lcdui.List;

/**
 *
 * @author Thyago Salvá
 */
public class ListView extends List {

    // Atributes
    private Vector members;

    // Construct
    public ListView(String title) {
        super(title, List.IMPLICIT);
    }

    // Methods
    public void setMembers(Vector members) {
        this.members = members;
        deleteAll();
        Member m;
        for (int i = 0, length = members.size(); i < length; i++) {
            m = (Member)members.elementAt(i);
            append(m.getAddress(), null);
        }
    }

    public Vector getMembers() {
        return this.members;
    }

    public Member get(int i) {
        return (Member)members.elementAt(i);
    }
}
