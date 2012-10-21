/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluegroups.interfacelisteners;

/**
 *
 * @author Thyago Salvá
 */
public interface FailureListener {

    // Method
    public void failureNotification(byte[] data);

    public void failureDetected(String addressRemote);

}
