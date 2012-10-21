/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluegroups.util;

import java.io.IOException;

/**
 *
 * @author Thyago Salvá
 */
public interface Serializable {

    // Methods
    public byte[] toByteStream() throws IOException;

    public void fromByteStream(byte[] data) throws IOException;
}
