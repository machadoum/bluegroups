/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluegroups.filemanager.deprecated;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import org.kxml2.io.KXmlSerializer;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

import bluegroups.filemanager.Diretorio;
import bluegroups.filemanager.Utils;

/**
 * Classe que manipula um arquivo.
 * @author Rafael
 */
public final class Arquivo {

    private String path;
    private String name;
    private String id;
    private String md5;
    private String revisao;
    private FileConnection file;
    private FileConnection dataFile;
    private String url;
    private String metadataUrl;
    private boolean versionable;
        
    public Arquivo(String path, String name, boolean versionable) {
        this.path = path;
        this.name = name;
        this.url = "file:///root1/"+path+"/"+name; 
        this.versionable = versionable;
        create();
        initAttributes();
    }
	
    /**
     * Não cria o arquivo, e recebe os dados por parametro.
     */
    public Arquivo(String path, String name, String id, String md5, String revisao, boolean versionable) {
        this.path = path;
        this.name = name;
        this.url = "file:///root1/"+path+"/"+name; 
        this.versionable = versionable;
        this.id = id;
        this.md5 = md5;
        this.revisao = revisao;
    }
    
    /**
     * Verifica se há necessidade de criar metadados, lê o md5 e cria um id.
	* Caso seja versionavel, cria o arquivo de metadados.
     */
    public void initAttributes() {
        if (versionable) {
            this.metadataUrl = "file:///root1/"+this.path+"/"+this.name+".info";
            if (!readMetaData()) {
                getMd5();
                getId();
                getRevision();
                generateMetaData();
            }
        } else {
            getMd5();
            getId();
        }
    }

    public void connect(int readWriteMode) {
        try {
            file = (FileConnection)Connector.open(url, readWriteMode);
        } catch (IOException ex) {
            System.out.println("Erro ao connectar no arquivo");
        }
    }
    
    void close() {
        try {
            file.close();
        } catch (IOException ex) {
            System.out.println("Erro ao fechar arquivo");
        }
    }
    
    public void connectMetadata(int readWriteMode) {
        try {
            dataFile = (FileConnection)Connector.open(metadataUrl, readWriteMode);
        } catch (IOException ex) {
            System.out.println("Erro ao connectar no arquivo de metadados.");
        }
    }  
    
    void closeMetadata() {
        try {
            dataFile.close();
        } catch (IOException ex) {
            System.out.println("Erro ao fechar arquivo de metadados.");
        }
    }
    
    public void create() {        
        Diretorio dir = new Diretorio(this.path);
        connect(Connector.READ_WRITE);
        System.out.println("Existe? "+file.exists());
        if (!file.exists()) {            
            try {
                file.create();
                System.out.println("Criando arquivo: "+file.getPath()+file.getName());
                System.out.println("Criado? "+file.exists());
            } catch (IOException ex) {
                System.out.println("Erro ao criar o arquivo.");
            }
        }
        close();
    }
    
    public void copy(FileConnection fromFile, FileConnection toFile) throws IOException {        
        OutputStream os = toFile.openOutputStream();      
        InputStream is = fromFile.openInputStream(); 
        
        if (os != null)
        { // download bytes to buffer and write buffer to file system.
            int byteCnt = 0;
            byte[] buffer = new byte[2048];
            while ((byteCnt = is.read(buffer,0,buffer.length)) >= 0) {
                os.write(buffer,0,byteCnt);
                os.flush();
            }
        }
        else { 
            System.out.println("No outputStream"); 
        }
    }
  
    public void addLine(String line) {
        try {
            connect(Connector.READ_WRITE);
            OutputStream out = this.file.openOutputStream(file.totalSize());
            out.write((line+"\n").getBytes());
            out.close();
        }
        catch (IOException e) {
            System.out.println("IO Error: "+e.getMessage());
        }
    }
    
    public OutputStream getOutputStream() throws IOException {
        return this.file.openOutputStream();
    }
        
    public boolean isOlder(){
        return false;
    }

    public void insertMetadata(String metadadosFile) {
     
    }

    protected void generateMetaData() {
        System.out.println("Tentando gerar metadados.");
        connectMetadata(Connector.READ_WRITE); 
        OutputStream os = null;
        try {
            if (dataFile.exists()) {
                dataFile.delete();
            }
            dataFile.create();            
            os = dataFile.openOutputStream();
        } catch (IOException ex) {
            System.out.println("Erro ao criar o arquivo: "+ex.getMessage());
        }
        
        Document doc = new Document();
        Element root = doc.createElement("", "parent");
        root.setName("File");
        root.setAttribute(null, "id", getId());
        
        Element info = doc.createElement("", "child");
        info.setName("info");
        
        Element revision = doc.createElement("", "child");
        revision.setName("revision");        
        revision.addChild(0, Node.TEXT, getRevision());
        Element md5 = doc.createElement("", "child");
        md5.setName("md5");
        md5.addChild(0, Node.TEXT, getMd5());
        
        info.addChild(0, Node.ELEMENT, revision);
        info.addChild(1, Node.ELEMENT, md5);
        
        root.addChild(0, Node.ELEMENT, info);
        
        doc.addChild(0, Node.ELEMENT, root);
        
        KXmlSerializer serializer = new KXmlSerializer();
        try {
            serializer.setOutput(os, "UTF-8");
            doc.write(serializer);
            os.close();
        } catch (IOException ex) {
            System.out.println("Erro ao serializar metadados.");
        }
        closeMetadata();        
    }
    
    /**
     * Verifica se os dados do arquivo estão disponiveis no arquivo de metadata.
     * E atualiza os atributos do {@link Arquivo} com os dados lidos.
     * @return Retorna verdadeiro caso os metadados sejam lidos com sucesso e 
     * falso caso contrário.
     */
    private boolean readMetaData() {
        return false;
    }
    
    /**
     * Retorna o id do arquivo que é o md5 da primeira versão.
     * @return 
     */
    public String getId() {
        if (this.id == null) {
            this.id = this.getMd5();
        }
        return this.id;
    }
    
    /**
     * Retorna a revisão do arquivo, que é o timeStamp de quando foi gerado.
     * @return {@link String}
     */
    public String getRevision() {
        if (this.revisao == null) {
            generateNewRevision();
        }
        return this.revisao;
    }
    
     /**
     * Retorna a revisão do arquivo, que é o timeStamp de quando foi gerado.
     * @return {@link String}
     */
    public void generateNewRevision() {
        this.revisao = Utils.getTimeStamp();
    }
    
    /**
     * Retorna o ultimo md5 lido no arquivo. 
     * Caso ainda não tenha sido calculado, calcula.
     * @return {@link String}
     */
    public String getMd5() {
        if (this.md5 == null) {
            refreshMd5();
        }
        return this.md5;
    }
    
    /**
     * Atualiza o md5 dos bytes do arquivo.
     */
    private void refreshMd5() {
        byte[] fileBuffer = null;        
        byte[] hashMd5 = null;
        MessageDigest algoritmo;
        StringBuffer hexString = null;
        try {
            connect(Connector.READ);
            fileBuffer = new byte[(int)file.fileSize()]; 
            hashMd5 = new byte[16];

            InputStream is = file.openInputStream(); 
            is.read(fileBuffer); 
            is.close();
            close();

            algoritmo = MessageDigest.getInstance("MD5");
            algoritmo.reset();                
            algoritmo.update(fileBuffer, 0, fileBuffer.length);
            algoritmo.digest(hashMd5, 0, hashMd5.length);

            hexString = new StringBuffer();
            for (int i=0; i<hashMd5.length; i++) {
                hexString.append(Integer.toHexString(0xFF & hashMd5[i]));
            }
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("O algoritmo Md5 não foi encontrado.");
        } catch (DigestException ex) {
            System.out.println("Erro ao gerar número md5.");
        } catch (IOException ex) {
            System.out.println("Erro ao ler arquivo.");
        }
        this.md5 = hexString.toString(); 
    }    
    
    protected String getPath() {
        return this.path;
    }

    protected String getName() {
        return this.name;
    }
    
    public FileConnection getFile(){
        return this.getFile(Connector.READ_WRITE);
    }


    void setRevision(String aux) {
        this.revisao = aux;
    }

    void setId(String aux) {
        this.id = aux;
    }

    void printData() {
        System.out.println("Adress: " + this.path);
        System.out.println("Name: " + this.name);
        System.out.println("Id: " + this.id);
        System.out.println("Md5: " + this.md5);
        System.out.println("Revision: " + this.revisao);
    }
    
    public FileConnection getFile(int readMode){
        connect(readMode);        
        if (!this.file.exists()) {
            create();
        }
        connect(readMode);
        return this.file;
    }
    
    protected void deleteFile() {
    	FileConnection fconn = null;
    	try {            
            fconn = (FileConnection) Connector.open(url,
    				Connector.READ_WRITE);
    	} catch (IOException e) {
            System.out.println("Error opening file");
    	}

    	if (!fconn.exists()) {
            System.out.println("File not exists!");
    	} else {
    		try {
                    fconn.delete();
                    System.out.println("Arquivo "+ this.name + " deletado.");
    		} catch (IOException e1) {
                    System.out.println("Error deleting file: " + this.name + " : " + e1.getMessage());
    		}

    		try {
                    fconn.close();
    		} catch (IOException e) {
                    System.out.println("Error closing file connection");
    		}
    	}
    }
    
    protected String readLine(int lineCount) {        
            StringBuffer line = new StringBuffer();
            InputStream is = null;
            int bufferPosition = 0;
            int counter = 0;
            int c = 0;
            char charRead;
        try {
            byte[] buffer = new byte[1];
            is = getFile(Connector.READ).openInputStream();
            do {
                c = is.read(buffer, 0, 1);
                charRead = (char)buffer[0];
                if (charRead == '\n') {
                    if (line.length() == 0) {
                        return null;
                    }
                    if (counter != lineCount) {                    
                        line.delete(0, line.length());
                    }
                    counter++;
                    System.out.println("Counter: "+counter);
                } else {                
                    line.append(charRead);
                }
            } while(c != -1 && counter <= lineCount);
            System.out.println("Commit Lidos:" + bufferPosition);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return line.toString();
    } 
}
