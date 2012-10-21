package bluegroups.filemanager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import org.kxml2.io.KXmlParser;
import org.kxml2.io.KXmlSerializer;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 *        
 * <repository id=xxxx name=xxxx>
 *    <revision id=yyyy>
 *       <file revision="">
 *           <nome></nome>
 *           <id></id>
 *           <address></address>
 *           <md5></md5>
 *       </file>
 *    </revision>
 * </repository>
 *
 */
public class Repository {

    private String id;
    private String name;
    private String revision;
    private String address;
    private BGFile controlFile;
    private BGFile commit;
    private Vector arquivos;
    private Vector outdatedFiles;
    private BGFile externalReposiroty;

    public Repository(String address, String name) {
        //Verifica se ja existe repositorio
        this.address = address;
        this.name = name;
        this.arquivos = new Vector();
        processControlFile();        
    }

    private void processControlFile() {
        controlFile = new BGFile(this.getAddress(), this.getName(), false);
        if (!controlFileIsValid()) {
            controlFile.deleteFile();
            this.id = createId(); //Hash
            fillControlFile(); //Arquivo contendo informaÃ§Ãµes sobre o repositorio
        } else {
            System.out.println("Tudo ok!");
        }
    }

    /**
     * Cria um Id para o repositório, com um valor aleatÃ³rio.
     * @return {@link String}
     */
    private String createId() {
        if (this.id == null) {
            this.id = Utils.getTimeStamp();
        }
        return this.id;
    }

    /**
     * Verifica se o controlFile estÃ¡ com os dados consistentes.
     * @return boolean
     */
    private boolean controlFileIsValid() {
        System.out.println("controlFileIsValid");
        String idTemp;
        String revisionTemp;
        Vector arquivosTemp = new Vector();
        InputStream is = null;
        try {
            is = controlFile.getFile().openInputStream();
            KXmlParser xmlParser = new KXmlParser();                        
            xmlParser.setInput(new InputStreamReader(is));
            
            xmlParser.nextTag();
            if (!xmlParser.getName().equals("repository")) {
                System.out.println("nao axou tag repository");
                controlFile.close();
                is.close();
                return false;
            }
            //verifica id
            if (xmlParser.getAttributeName(0).equals("id")) {
                idTemp = xmlParser.getAttributeValue(0);
            } else {
                System.out.println("nao axou id do repository");
                controlFile.close();
                is.close();
                return false;
            }            
            //verifica o nome
            if (!xmlParser.getAttributeName(1).equals("name") 
                    && !xmlParser.getAttributeValue(1).equals(this.name)) {
                System.out.println("nao axou name do repository");
                controlFile.close();
                is.close();
                return false;
            }               
            //verifica a tag revision
            xmlParser.nextTag();
            if (xmlParser.getName().equals("revision") 
                    && xmlParser.getAttributeName(0).equals("version")) {
                revisionTemp = xmlParser.getAttributeValue(0);
            } else {
                System.out.println("nao axou versao do repository");
                controlFile.close();
                is.close();
                return false;
            }            
            //verifica se a tag file existe
                //caso sim, chama mÃ©todo getFilesFromControlFile()
            while (xmlParser.nextTag() == XmlPullParser.START_TAG) {
                if (xmlParser.getName().equals("file") 
                        && xmlParser.getAttributeName(0).equals("revision")) {                   
                    String fileName = null;
                    String fileAddress = null;
                    String fileId = null;
                    String fileMd5 = null;
                    String fileRevision = xmlParser.getAttributeValue(0);
                    xmlParser.nextTag();                    
                    if (xmlParser.getName().equals("name")) {
                        fileName = xmlParser.nextText();
                    } else {
                        System.out.println("nao axou name do file");
                        controlFile.close();
                        is.close();
                        return false;
                    }
                    xmlParser.nextTag();
                    if (xmlParser.getName().equals("id")) {
                        fileId = xmlParser.nextText();
                    } else {
                        System.out.println("nao axou id do file");
                        controlFile.close();
                        is.close();
                        return false;
                    }
                    xmlParser.nextTag();                    
                    if (xmlParser.getName().equals("address")) {
                        fileAddress = xmlParser.nextText();
                    } else {
                        System.out.println("nao axou address do file");
                        controlFile.close();
                        is.close();
                        return false;
                    }
                    xmlParser.nextTag();                    
                    if (xmlParser.getName().equals("md5")) {
                        fileMd5 = xmlParser.nextText();
                    } else {
                        System.out.println("nao axou md5 do file");
                        controlFile.close();
                        is.close();
                        return false;
                    }
                    arquivosTemp.addElement(new BGFile(fileAddress, fileName, fileId, fileMd5, fileRevision));
                } else {
                    controlFile.close();
                    is.close();
                    return false;
                }
                xmlParser.nextTag();
            }
            if (!(xmlParser.getName().equals("revision") 
                    && xmlParser.getEventType() == XmlPullParser.END_TAG)) {
                controlFile.close();
                is.close();
                return false;
            }
            
            xmlParser.nextTag();
            if (!(xmlParser.getName().equals("repository") 
                    && xmlParser.getEventType() == XmlPullParser.END_TAG)) {
                controlFile.close();
                is.close();
                return false;
            }
            
            xmlParser.next();
            if (xmlParser.getEventType() != XmlPullParser.END_DOCUMENT) {
                controlFile.close();
                is.close();
                return false;
            }
            
            this.id = idTemp;
            this.revision = revisionTemp;
            this.arquivos = arquivosTemp;            
            
            controlFile.close();
            is.close();
        } catch (XmlPullParserException ex) {   
            System.out.println("Erro no parser");
            return false;
        } catch (IOException ex) {
            System.out.println("Erro de IO: " + ex.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException ex1) {
                ex1.printStackTrace();
            }
        }
        return true;
    }

    /**
     * Preenche o controlFile com os dados de repositÃ³rio.
     */
     private void fillControlFile() {
        OutputStream os = null;
        KXmlSerializer serializer = null;
        Document doc = null;
        try {
            os = controlFile.getFile().openOutputStream();
        } catch (IOException ex) {
            System.out.println("Erro ao abrir canal de saÃ­da: " + ex.getMessage());
        }
        doc = new Document();
        Element repositoryElement = doc.createElement("", "parent");
        repositoryElement.setName("repository");
        repositoryElement.setAttribute(null, "id", this.getId());
        repositoryElement.setAttribute(null, "name", this.getName());
        
        Element revisionElement = doc.createElement("", "revision");
        revisionElement.setName("revision");
        revisionElement.setAttribute(null, "version", this.getRevision());

        repositoryElement.addChild(0, Node.ELEMENT, revisionElement);
        doc.addChild(0, Node.ELEMENT, repositoryElement);
        
        serializer = new KXmlSerializer();
        try {
            serializer.setOutput(os, "UTF-8");
            doc.write(serializer);
            os.flush();  
        } catch (IOException ex) {
            System.out.println("Erro ao serializar metadados.");
        } finally {              
            try {
                os.close();
                controlFile.close();
            } catch (IOException ex) {
                System.out.println("Erro ao fechar control file");
            }
        }        
    }

    /**
     * Adiciona um arquivo a um repositório.
     * Recebe o caminho e nome do arquivo que serão adicionados e altera o arquivo
     * commit, com informaÃ§Ãµes sobre a adição.
     * @param path {@link String}
     * @param name {@link String}
     */
    public void add(String path, String name) {
        validateCommit();
        OutputStream os = null;
        InputStream is = null;
        KXmlSerializer serializer = null;
        KXmlParser xmlParser = null;
        Document doc = null;
        try {
            is = getCommit().getFile().openInputStream();
            os = getCommit().getFile().openOutputStream();        
        
            xmlParser = new KXmlParser();                        
            doc = new Document();
            xmlParser.setInput(new InputStreamReader(is));
            doc.parse(xmlParser); 
            
            Element addElement = doc.createElement("", "add");
            addElement.setName("add");
            addElement.setAttribute(null, "path", path);
            addElement.setAttribute(null, "name", name);

            doc.addChild(Node.ELEMENT, addElement);

            serializer = new KXmlSerializer();
            
            serializer.setOutput(os, "UTF-8");
            doc.write(serializer);
            os.flush(); 
        } catch (IOException ex) {
            System.out.println("Erro ao serializar metadados.");
        } catch (XmlPullParserException ex) {
            ex.printStackTrace();
        } finally {                       
            try {
                getCommit().close();
                os.close();
                is.close();
            } catch (IOException ex) {
                System.out.println("Erro ao fechar commit");
            }
        }       
    }
    
    /**
     * Grava as alterações do arquivo commit no controlFile do repositório,
     * atualizando a versão do repositório.
     */
    public void commit() {        
        BGFile temp = null;
        String command = "";
        String fileName = "";
        String filePath = "";
        InputStream is = null;
        FileConnection file = null;
        try {
            is = getCommit().getFile().openInputStream();
            KXmlParser xmlParser = new KXmlParser();                        
            xmlParser.setInput(new InputStreamReader(is));
            xmlParser.nextTag();
            int eventType = xmlParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                command = xmlParser.getName();
                if (command != null && command.equals("add") && eventType == XmlPullParser.START_TAG) {
                    fileName = xmlParser.getAttributeValue(1);
                    filePath = xmlParser.getAttributeValue(0);
                    String url = null;
                    url = "file:///root1/"+this.getAddress()+"/"+filePath+fileName; 
                    file = (FileConnection)Connector.open(url);
                    if (file.exists()) {
                        temp = new BGFile(filePath, fileName, true);
                        this.arquivos.addElement(temp);
                    }
                }
                eventType = xmlParser.next();
            }
            getControlFile().deleteFile();
            controlFileUpdate();                
        } catch (XmlPullParserException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
            	getCommit().close();
            	file.close();
                is.close();
                getCommit().deleteFile();
                System.out.println("Deletando commit.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }                
    }
    
    private void controlFileUpdate() {
        try {
            BGFile temp = null;
 
            OutputStream os = null;
            KXmlSerializer serializer = null;
            Document doc = null;

            os = controlFile.getFile().openOutputStream();

            serializer = new KXmlSerializer();        
            serializer.setOutput(os, "UTF-8");
            
            doc = new Document();

            Element repositoryElement = doc.createElement("", "repository");
            repositoryElement.setAttribute("", "id", getId());
            repositoryElement.setAttribute("", "name", getName());            
            
            setRevision(Utils.getTimeStamp());
            Element revisionElement = doc.createElement("", "revision");
            revisionElement.setName("revision");
            revisionElement.setAttribute("", "version", getRevision());
            
            int i = 0;
            while (i < arquivos.size()) {
                temp = (BGFile)arquivos.elementAt(i);            
                Element fileElement = doc.createElement("", "file");
                fileElement.setName("file");
                fileElement.setAttribute("", "revision", temp.getRevision());
                
                Element nameFileElement = doc.createElement("", "name");
                nameFileElement.addChild(0, Node.TEXT, temp.getName());
                
                Element idFileElement = doc.createElement("", "id");
                idFileElement.addChild(0, Node.TEXT, temp.getId());
                
                Element addressFileElement = doc.createElement("", "adress");
                addressFileElement.addChild(0, Node.TEXT, temp.getPath());
                
                Element md5FileElement = doc.createElement("", "md5");
                md5FileElement.addChild(0, Node.TEXT, temp.getMd5());
                
                fileElement.addChild(Node.ELEMENT, nameFileElement);
                fileElement.addChild(Node.ELEMENT, idFileElement);
                fileElement.addChild(Node.ELEMENT, addressFileElement);
                fileElement.addChild(Node.ELEMENT, md5FileElement);
                
                revisionElement.addChild(Node.ELEMENT, fileElement);
                i++;
            }
            
            repositoryElement.addChild(Node.ELEMENT, revisionElement);
            doc.addChild(Node.ELEMENT, repositoryElement);
            
            doc.write(serializer);
            os.flush();            
            os.close();
            controlFile.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Verifica se algum arquivo foi modificado e salva no arquivo commit
     */
    private void verifyChanges() {
        System.out.println("Verify changes");        
    }

    /**
     * Verifica se o repositorio tem algum arquivo adicionado.
     * @return boolean
     */
    public boolean hasFiles() {
        if (arquivos.size() > 0) {
            return true;                    
        }
        return false;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Retorna a revisão atual do repositório.
     * @return {@link String}
     */
    public String getRevision() {
        if (revision == null) {
            this.revision = Utils.getTimeStamp();
        }
        return this.revision;                
    }

    /**
     * Retorna o endereço do repositório.
     * @return {@link String}
     */
    public String getAddress() {
        return this.address;
    }
    
    /**
     * Retorna o arquivo onde ficam salva as alterações que serão feitas no commit.
     * @return {@link BGFile}
     */
    public BGFile getCommit() {
        if (this.commit == null) {
            this.commit = new BGFile(this.address, "commit", false);
        }
        return this.commit;
    }
    
    /**
     * Retorna o arquivo com os metadados do repositório.
     * @return {@link BGFile}
     */
    public BGFile getControlFile() {
        if (this.controlFile == null) {
            this.controlFile = new BGFile(this.address, this.name, false);
        }
        return this.controlFile;
    }


    private void setRevision(String revision) {
        this.revision = revision;
    }

    /**
     * Verifica se o arquivo commit estÃ¡ estruturado corretamente.
     * Se nÃ£o estiver, zera o arquivo.
     */
    private void validateCommit() {
        return;
    }

    private void checkout() {
        //solicita controlFile para o servidor;
        //substitui arquivo recebido pelo controlFile atual.        
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Compara o repositório atual com algum repositório recebido.
     * Atualiza o vetor dos arquivos disponíveis, que não estão no repositório.
     * Atualiza também o vetor, com os arquivos que possuem novas versões.
     */
    public void syncronize() {
        //verifica se controlFile eh valido
        //atualiza vetor de arquivos
        Vector arquivosNovos = readFilesFromControlFile(this.externalReposiroty);
        if (!this.hasFiles()) {
            outdatedFiles = arquivosNovos;
            return;
        } 
        for (int i = 0; i < arquivosNovos.size(); i++) {
            if(isNewer(arquivosNovos.elementAt(i))) {
                outdatedFiles.addElement(arquivosNovos.elementAt(i));
            }
        }
                
        //verifica quais desses arquivos estÃ£o desatualizados (verifica se existe, e se versÃ£o Ã© diferente).
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Retorna o vetor com os arquivos que não estão atualizados, ou não existem no repositório.
     * @return {@link Vector}
     */
    public Vector getOutDatedFiles() {
        return this.outdatedFiles;
    }
    
    private Vector readFilesFromControlFile(BGFile externalReposiroty) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private boolean isNewer(Object elementAt) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void update() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Seta um repositório externo, que será comparado através do syncronize.
     * @param controlFile
     */
    public void setExternalRepository(BGFile controlFile) {
        this.externalReposiroty = controlFile;
    }

    public BGFile getFileFromRepository(String fileId, String fileName, String fileAddress) throws Exception {
        for (int i = 0; i < this.arquivos.size(); i++) {
            BGFile file = (BGFile) arquivos.elementAt(i);
            if (file.getId().equals(fileId)
                    && file.getName().equals(fileName)
                    && file.getPath().equals(fileAddress)) {
                return file;
            }
        }
        throw new Exception("Arquivo nÃ£o encontrado no repositÃ³rio");
    }

    public void setFilePart(String id, String name, String address, int partNo, long size, byte[] data) {
        try {
            BGFile file = this.getFileFromRepository(id, name, address);
            if (partNo == 1) {
                file.createTemp();
            }
            file.getFileParts().addElement(data);
            OutputStream os = file.getFile().openOutputStream();
            
            for (int i = 0; i < file.getFileParts().size(); i++) {
                byte[] dados = new byte[4096];
                dados = (byte[])file.getFileParts().elementAt(i);
                os.write(dados, data.length * partNo, data.length);
            }
            os.close();
            file.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
