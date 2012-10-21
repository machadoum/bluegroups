/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluegroups.interfacelisteners;

/**
 *
 * @author Rafael
 */
public interface RepositoryListener {
    
    public void checkout_detected(String source);
    
    public void repository_received(byte[] data, String source);    
    
}
