/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluegroups.interfacelisteners;

import bluegroups.messages.FileMessage;

/**
 *
 * @author Rafael
 */
public interface FileListener {
    
    public void file_received(FileMessage file);
    
    public void file_requested(byte[] data);            
    
}
