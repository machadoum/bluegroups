/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluegroups.filemanager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import org.kxml2.io.KXmlSerializer;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

import bluegroups.util.Serializable;

/**
 * Classe que representa um arquivo no sistema de arquivos do dispositivo.
 */
public final class BGFile implements Serializable {

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
    private Vector fileParts;
    private Vector filePartsReceived;
    private long partsNo;
        
    public BGFile(String path, String name, boolean versionable) {
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
    public BGFile(String path, String name, String id, String md5, String revisao) {
        this.path = path;
        this.name = name;
        this.url = "file:///root1/"+path+"/"+name; 
        this.versionable = false;
        this.id = id;
        this.md5 = md5;
        this.revisao = revisao;
        this.fileParts = new Vector();
    }
    
    private void initAttributes() {
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

    /**
     * Inicia uma conexão com o arquivo para que seja possível realizar operações de leitura e escrita.
     * @param readWriteMode Modo de acesso ao arquivo. Modos disponiveis estaticamente na classe {@link Connector} 
     */
    public void connect(int readWriteMode) {
        try {
            file = (FileConnection)Connector.open(url, readWriteMode);
        } catch (IOException ex) {
            System.out.println("Erro ao connectar no arquivo");
        }
    }
    
	/**
	 * Encerra a conexão com o arquivo.
	 */
    public void close() {
        try {
            file.close();
        } catch (IOException ex) {
            System.out.println("Erro ao fechar arquivo");
        }
    }
    
    private void connectMetadata(int readWriteMode) {
        try {
            dataFile = (FileConnection)Connector.open(metadataUrl, readWriteMode);
        } catch (IOException ex) {
            System.out.println("Erro ao connectar no arquivo de metadados.");
        }
    }  
    
    private void closeMetadata() {
        try {
            dataFile.close();
        } catch (IOException ex) {
            System.out.println("Erro ao fechar arquivo de metadados.");
        }
    }
    
    /**
     * Cria o arquivo com os dados específicados no momento da construção do objeto.
     */
    public void create() {        
        new Diretorio(this.path);
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
    
    /**
     * Realiza uma cópia byte a byte de um arquivo.
     * @param fromFile {@link FileConnection} com a conexão do arquivo de origem.
     * @param toFile {@link FileConnection} com a conexão do arquivo destino.
     * @throws IOException
     */
    public void copy(FileConnection fromFile, FileConnection toFile) throws IOException {      
    	//FIXME Mover este método para uma classe utilitária.
        OutputStream os = toFile.openOutputStream();      
        InputStream is = fromFile.openInputStream(); 
        
        if (os != null)
        {
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
  
    /**
     * Adiciona ao final do arquivo de texto uma string.
     * @param line {@link String} com o texto a ser adicionado.
     */
    public void addLine(String line) {
        try {
            connect(Connector.READ_WRITE);
            OutputStream out = this.file.openOutputStream(file.totalSize());
            out.write((line+"\n").getBytes());
            out.close();
            close();
        }
        catch (IOException e) {
            System.out.println("IO Error: "+e.getMessage());
        }
    }
    
    /**
     * Retorna uma conexão de saída para o arquivo.
     * @return {@link OutputStream}
     * @throws IOException
     */
    public OutputStream getOutputStream() throws IOException {
        return this.file.openOutputStream();
    }
        
    private void generateMetaData() {
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
    
    private boolean readMetaData() {
        return false;
    }
    
    /**
     * Retorna o id do arquivo. Que é representado pelo MD5 da primeira versão.
     * @return {@link String}
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
     * Gera o identificador de uma revisão do arquivo.
     * Esse identificado é o timestamp do momento em que o método for executado.
     * @return {@link String}
     */
    public void generateNewRevision() {
        this.revisao = Utils.getTimeStamp();
    }
    
    /**
     * Retorna o ultimo md5 lido no arquivo. Caso ainda não tenha sido calculado, calcula.
     * Para atualizar o md5 deve-se utilizar o método refreshMd5().
     * @return {@link String}
     */
    public String getMd5() {
        if (this.md5 == null) {
            refreshMd5();
        }
        return this.md5;
    }
    
    /**
     * Calcula o md5 dos bytes do arquivo. E salva o valor em memória.
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
            System.out.println("O algoritmo Md5 nÃ£o foi encontrado.");
        } catch (DigestException ex) {
            System.out.println("Erro ao gerar nÃºmero md5.");
        } catch (IOException ex) {
            System.out.println("Erro ao ler arquivo.");
        }
        this.md5 = hexString.toString();
    }    
    
    /**
     * Retorna o caminho do arquivo no sistema.
     * @return {@link String}
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Retorna o nome do arquivo.
     * @return {@link String}
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Retorna uma conexão em modo leitura do arquivo.
     * @return {@link FileConnection}
     */
    public FileConnection getFile(){
        return this.getFile(Connector.READ_WRITE);
    }

    protected void setRevision(String aux) {
        this.revisao = aux;
    }
    
    protected void setId(String aux) {
        this.id = aux;
    }

    protected void printData() {
        System.out.println("Address: " + this.path);
        System.out.println("Name: " + this.name);
        System.out.println("Id: " + this.id);
        System.out.println("Md5: " + this.md5);
        System.out.println("Revision: " + this.revisao);
    }
    
    /**
     * Retorna uma conexão com o arquivo, especificando que tipo de conexão deve ser feita.
     * @param readMode {@link Connector}
     * @return {@link FileConnection}
     */
    public FileConnection getFile(int readMode){
        connect(readMode);        
        if (!this.file.exists()) {
            create();
        }
        connect(readMode);
        return this.file;
    }
    
    /**
     * Deleta o arquivo.
     */
    public void deleteFile() {
//    	FileConnection fconn = null;
//            fconn = (FileConnection) Connector.open(url,
//    				Connector.READ_WRITE);
    	connect(Connector.READ_WRITE);
    	if (!file.exists()) {
    		System.out.println("File not exists!");
    	} else {
    		try {
    			file.delete();
    			System.out.println("Arquivo "+ this.name + " deletado.");
    		} catch (IOException e1) {
    			System.out.println("Error deleting file: " + this.name + " : " + e1.getMessage());
    		}
    	}
    	try {
    		file.close();
    	} catch (IOException e) {
    		System.out.println("Error closing file connection");
    	}
    }
    
    /**
     * Lê e retorna uma linha específica do arquivo de texto.
     * @param lineCount Número da linha que se deseja ler.
     * @return {@link String} da linha lida.
     */
    public String readLine(int lineCount) {        
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
        close();
        return line.toString();
    }
    
    /**
     * Lê a proxima linha do arquivo e salva o valor em memória.
     * @return
     */
    public boolean readLine() {
        return true;
    }
    
    /**
     * Retorna a última leitura feita no arquivo.
     * @return {@link String}
     */
    public String getLine() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    /**
     * Retorna os bytes do arquivo.
     */
    public byte[] toByteStream() throws IOException {        
        System.out.println("oi1");
        InputStream is = this.getFile().openInputStream();
        System.out.println("DISPONIVEL::: ");
        byte[] data = new byte[(int)this.getFile().fileSize()];
        System.out.println("oi2");
        is.read(data);
        System.out.println("TESTE: "+new String(data));
        return data;
    }

    /**
     * Grava os bytes no arquivo.
     */
    public void fromByteStream(byte[] data) throws IOException {
        connect(Connector.READ_WRITE);
        InputStream is = file.openInputStream();
        is.read(data);
        is.close();
        close();
    }

    /**
     * Retorna o número total de partes que o arquivo será dividido.
     */
    public long getTotalParts() {
        if (this.partsNo == 0) {
            this.partsNo = this.totalSize() / 4096;
        } 
        return this.partsNo;
    }

    /**
     * Retorna o tamanho total do arquivo, em bytes.
     */
    public long totalSize() {
        return this.getFile().totalSize();
    }
    
    /**
     * Retorna qual a próxima parte pendente a ser recebida.
     */
    public int getNextPartNo() {
        int partsCount = 0;
        FileConnection fileTemp = null;
        FileConnection fileBitMap = null;
        InputStream isTemp = null;
        InputStream isBitMap = null;
        try {
            String urlTemp = "fiemple:///root1/"+this.getPath()+"/"+this.getName()+".tmp"; 
            String urlBitMap = "fiemple:///root1/"+this.getPath()+"/"+this.getId()+".tmp"; 
            
            fileTemp = (FileConnection)Connector.open(urlTemp, Connector.READ_WRITE);
            fileBitMap = (FileConnection)Connector.open(urlBitMap, Connector.READ_WRITE);
            if (fileBitMap.exists()) {
                isBitMap = fileBitMap.openInputStream();
                isTemp = fileTemp.openInputStream();
                int nextPart;
                byte[] nextBytes = new byte[4096];
                for (int i = 0; i < this.getTotalParts(); i++) {
                    nextPart = isBitMap.read();
                    isTemp.read(nextBytes, i * 4096, 4096);
                    this.filePartsReceived.insertElementAt(Integer.valueOf(String.valueOf(nextPart)), i);
                    this.fileParts.insertElementAt(nextBytes, i);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
        	try {
				fileTemp.close();
				fileBitMap.close();
				isTemp.close();
				isBitMap.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return partsCount;
    }

    protected void createTemp() {
    	FileConnection fileTemp = null;
    	OutputStream os = null;
        try {
            String urlTemp = "fiemple:///root1/"+this.getPath()+"/"+this.getName()+".tmp"; 
            fileTemp = (FileConnection)Connector.open(urlTemp, Connector.READ_WRITE);
            if (!fileTemp.exists()) {
                fileTemp.create();
            }
            os = fileTemp.openOutputStream();
            int totalSize = Integer.valueOf(String.valueOf(this.totalSize())).intValue();
            byte[] data = new byte[(totalSize)];
            os.write(data);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
        	try {
				fileTemp.close();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }

    protected Vector getFileParts() {
        return fileParts;
    }

    protected Vector getFilePartsReceived() {
        return filePartsReceived;
    }
    
}
