/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluegroups.interfacelisteners;

import bluegroups.messages.FileRequestMessage;

/**
 *
 * @author Rafael
 */
public interface FileUpdateListener {
    
    public void request_file(String receiver, FileRequestMessage message);
    
}
