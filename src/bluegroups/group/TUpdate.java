/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluegroups.group;

import bluegroups.filemanager.BGFile;
import bluegroups.filemanager.Repository;
import bluegroups.interfacelisteners.FileUpdateListener;
import bluegroups.messages.FileRequestMessage;

/**
 *
 * @author Rafael
 */
public class TUpdate extends Thread {
    private FileUpdateListener fileUpdateListener;
    private Repository repository;
    private String source;
    
    public TUpdate(Repository repository, FileUpdateListener listener, String source) {
        this.repository = repository;                
        this.fileUpdateListener = listener;
        this.source = source;
    }   
    
    
    public synchronized void run() {
        
        while(!repository.getOutDatedFiles().isEmpty()) {
            BGFile file = (BGFile)repository.getOutDatedFiles().firstElement();
            FileRequestMessage message = new FileRequestMessage(
                    file.getId(), 
                    file.getName(), 
                    file.getPath(), 
                    file.getNextPartNo());
            fileUpdateListener.request_file(source, message);
            try {
                this.wait();
            } catch (InterruptedException ex) {
                System.out.println("Erro ao durmir TUPDATE: "+ex.getMessage());
            }
        }
        
    }
}
