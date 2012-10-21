/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluegroups.filemanager;

import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
/**
 * Classe auxiliar para manipular diretórios.
 */
public class Diretorio {
    
    private String address;
    
    public Diretorio(String adress) {                   
        this.address = adress;
        createDir();                
    }

    /**
     * Cria o diretório, caso não exista, setado no construtor.
     */
    private void createDir(){
    	FileConnection fc = null;
        try {
            fc = (FileConnection) Connector.open("file:///root1/"+getAddress(), Connector.READ_WRITE);
            System.out.println("Já tinha DIRETORIO? "+fc.exists());
            if (!fc.exists()) {
                fc.mkdir();
                System.out.println("Criou DIRETORIO? "+fc.exists());     
            }
            System.out.println("? "+fc.getPath());
        } catch (IOException e) {
            System.out.println("Erro na createDir: "+e.getMessage());
        } finally {
    		try {
				fc.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    public String getAddress(){
        return this.address;
    }
    
    public void setAddress(String address) {
    	this.address = address;
    }
    
}