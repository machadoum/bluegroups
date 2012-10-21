/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 
package bluegroups.filemanager.deprecated;

import java.io.*;
import javax.microedition.io.*;
import javax.microedition.io.file.*;

import bluegroups.filemanager.BGFile;
import bluegroups.filemanager.Diretorio;

*//**
 * 
 * @author Rafael
 *//*
public class VersionManager {

    private String FileInfoXMLName;
    private int versioningType; // criar enum
    private final static String metadadosFile = "_metadata";
    private DistributedRepository distributedR;
    
    public VersionManager() {
        //createRepository();
        createDistributedRepository();
        //createFile();
    }
    
    //Criar reposistório.
    //add
    //remove
    //update      

    private void createDistributedRepository() {
        distributedR = new DistributedRepository();
    }
    
    public DistributedRepository getDistributedRepository(){
        return this.distributedR;
    }
    
    public int createRepository() {
        ReadOnlyRepository repository = new ReadOnlyRepository(
                new Diretorio("repTeste"), 
                "RepPrototipo");
        return 0;
    }
    
    public int createRepositoryLocal() {
        LocalRepository localRepository = new LocalRepository(
                new Diretorio("repTeste"), 
                "Repositorio 1",
                new Diretorio("workTeste"));
        return 0;
    }
    
    public void createFile() {
        BGFile arquivo = new BGFile("", "teste.txt", false);
        try {
            arquivo.create();
        } catch(Exception e) { 
            System.out.println(e.getMessage());
        }
    }
       
}   
*/