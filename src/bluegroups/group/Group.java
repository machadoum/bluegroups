/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluegroups.group;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.StreamConnection;

import bluegroups.Code;
import bluegroups.Member;
import bluegroups.View;
import bluegroups.filemanager.BGFile;
import bluegroups.filemanager.Repository;
import bluegroups.interfacelisteners.AcceptListener;
import bluegroups.interfacelisteners.FailureListener;
import bluegroups.interfacelisteners.FileListener;
import bluegroups.interfacelisteners.FileUpdateListener;
import bluegroups.interfacelisteners.MessageListener;
import bluegroups.interfacelisteners.ReceiveListener;
import bluegroups.interfacelisteners.RepositoryListener;
import bluegroups.interfacelisteners.ViewListener;
import bluegroups.messages.FileMessage;
import bluegroups.messages.FileRequestMessage;
import bluegroups.messages.Pacote;
import bluegroups.util.Log;

/**
 * Classe que cria um grupo, e realiza uma conex�o com todos os dispositivos que estejam 
 * na �rea de alcance. Para entrar em um grupo deve-se utilizar o m�todo {@link #join(String)}.
 * 
 * O m�todo {@link #send(String, String)} pode ser utilizado para enviar mensagens � outros dispositivos
 * pertencentes ao grupo.
 * 
 * A interface {@link ReceiveListener} deve ser implementada, e sua implementa��o deve ser informada
 * atrav�s do m�todo {@link #setReceiveListener(ReceiveListener)}. Essa implementa��o receber� as mensagens
 * enviadas atrav�s do comando {@link #send(String, String)}.
 *  
 */
public class Group {

	// Atributes
    //object used for waiting
    public static final Object lock = new Object();

    private    LocalDevice dispositivo;
    private DiscoveryAgent agente;
    private      Hashtable devices;
    private         String nameGroup;
    private         String myAddress;
    private         String myName;
    private         String connectionURL;
    private           View myView;
    private      Hashtable ins;
    private      Hashtable outs;
    private      Hashtable connections;

    // Listeners
    private     AcceptListener acceptListener;
    private       ViewListener viewListener;
    private    MessageListener messageListener;
    private    ReceiveListener receiveListener;
    private    FailureListener failureListener;
    private  DiscoveryListener discoveryListener;
    private RepositoryListener repositoryListener;
    private       FileListener fileListener;    
    private FileUpdateListener fileUpdateListener;
    private Repository repository;

    public Group() {
        connectionURL = null;
               myView = new View();
          connections = new Hashtable();
                  ins = new Hashtable();
                 outs = new Hashtable();
              devices = new Hashtable();


        
        acceptListener = new GroupAcceptListener();

        viewListener = new GroupViewListener();

        messageListener = new GroupMessageListener();

        failureListener =  new GroupFailureListener();  
        
        fileUpdateListener = new GroupFileUpdateListener();
        
        repositoryListener = new GroupRepositoryListener();
                
        fileListener = new GroupFileListener();

        discoveryListener = new GroupDiscoveryListener();

        try {
            dispositivo = LocalDevice.getLocalDevice();
            agente = dispositivo.getDiscoveryAgent();

        } catch(Exception e) { failure("Construct: " + e.getMessage()); }

        this.myAddress = dispositivo.getBluetoothAddress();
           this.myName = dispositivo.getFriendlyName();
    }

    // Listeners
    /**
     * M�todo utilizado para definir a implementa��o do {@link ReceiveListener} que ir�
     * receber as mensagens envidas pelos outros dispositivos.
     * @param listener
     */
    public void setReceiveListener(ReceiveListener listener) { receiveListener = listener; }

    private boolean contains(String address) {
        if (outs.containsKey(address)) return true;
        return false;
    }

    private void addConnection(String address, StreamConnection connection) { this.connections.put(address, connection); }

    private void addOutput(String address, OutputStream out) { this.outs.put(address, out); }

    private void addInput(String address, Thread in) { this.ins.put(address, in); }

    private void removeConnection(String address) {
        try {
            ((StreamConnection)this.connections.get(address)).close();
            this.outs.remove(address);
        } catch (Exception e) { System.out.println("RemoveConnection: " + e.getMessage()); }
        this.connections.remove(address);
    }

    private void removeInput(String address) {
        try {
            ((Thread)this.ins.get(address)).interrupt();
            this.ins.remove(address);
        } catch (Exception e) { failure("RemoveInput: " + e.getMessage()); }
        this.ins.remove(address);
    }

    private void removeOutput(String address) {
        try {
            ((OutputStream)this.outs.get(address)).close();
            this.outs.remove(address);
        } catch (Exception e) { failure("RemoveOutput: " + e.getMessage()); }
        this.outs.remove(address);
    }

    public void remove(String address) {
        removeInput(address);
        removeOutput(address);
        removeConnection(address);
    }

    /**
     * M�todo utilizado para sair do grupo.
     */
    public void leave() {
        String key;
        Pacote p = new Pacote(myAddress, myView.getMaster(myAddress), Code.LEAVE);
        send(p);

//        for (Enumeration en = connections.keys(); en.hasMoreElements();) {
//            key = (String)en.nextElement();
//            //remove(key);
//            try {
//                ((StreamConnection)connections.get(key)).close();
//                ((OutputStream)outs.get(key)).close();
//                ((Thread)ins.get(key)).interrupt();
//            } catch(Exception e) { System.out.println("ERRO: " + e.getMessage()); }
//        }
    }

    /**
     * M�todo utilizado para se juntar a um grupo.
     * @param nameGroup Nome do grupo ao qual se deseja juntar.
     */
    public void join(String nameGroup) {
        join(nameGroup, null);
    }
    
    /**
     * M�todo utilizado para se juntar a um grupo e criar um reposit�rio para receber os arquivos desse grupo.
     * @param nameGroup Nome do grupo ao qual se deseja juntar.
     * @param repositoryAddress Endere�o local onde o reposit�rio ser� criado.
     */
    public void join(String nameGroup, String repositoryAddress) {
        try {            
            this.nameGroup = nameGroup;
            if (repositoryAddress != null) {
                this.repository = new Repository(repositoryAddress, nameGroup);
            }
            System.out.println("passou_aki_00");
            searchDevices();
            System.out.println("pass�ou_aki_01");
            searchServices();
            System.out.println("passou_aki_02");

            if (connectionURL != null) {
                TConnection tConnection = new TConnection(connectionURL);
                tConnection.setAcceptLister(acceptListener);
                tConnection.start();
//                this.checkout();
            } else {
                myView.addMember(new Member(myAddress, myName, myAddress, 0));
            }

            TAccept tAccept = new TAccept(nameGroup);
            tAccept.setAcceptLister(acceptListener);
            tAccept.start();

        } catch(Exception e) { 
            failure("JOIN: " + e.getMessage()); 
        }
    }
    /**
    public void retrieveGroup(String address) {
        try {
            remove(address);
        } catch(Exception e) { failure("RETRIEVE: " + e.getMessage()); }
    }*/

    private void searchDevices() throws BluetoothStateException, InterruptedException {
        devices.clear();
        System.out.println("devices_00");
        
        synchronized (lock) {
            System.out.println("devices_01");
            agente.startInquiry(DiscoveryAgent.GIAC, discoveryListener);
            System.out.println("devices_02");
            lock.wait();
            System.out.println("devices_024");
        }
        System.out.println("devices_03");
        int devicesCount = devices.size();
        if (devicesCount == 0) { Log.log("Not devices found."); }
    }

    private void searchServices() throws BluetoothStateException, InterruptedException {
        UUID uuidSet[] = new UUID[1];
        uuidSet[0] = new UUID(0x0003);

        RemoteDevice rd;
        String aux;
        for (Enumeration en = devices.keys(); en.hasMoreElements();) {
            aux = (String)en.nextElement();
            searchServices(aux);
            if (connectionURL != null) break;
        }
    }

    private void searchServices(String address) throws BluetoothStateException, InterruptedException {
        UUID uuidSet[] = new UUID[1];
        uuidSet[0] = new UUID(0x0003);
        RemoteDevice rd = (RemoteDevice)devices.get(address);
        
        synchronized(lock) {
            agente.searchServices(null, uuidSet, rd, discoveryListener);
            lock.wait();
        }
    }

    // Notification Failure
    private void failure(byte[] data) {
        System.out.println("AAAA: " + new String(data));
        receiveListener.failure(data);
    }

    private void failure(String f) {
        failure(f.getBytes());
    }

    // Sends
    /**
     * Envia uma mensagem a um dispositivo espec�fico, ou para todo o grupo.
     * @param m {@link String} com a mensagem que ser� enviada.
     * @param receiver Endere�o do destinat�rio da mensagem. Envia para todos, caso esse seja null.
     */
    public void send(String m, String receiver) {
        Pacote p = new Pacote(myAddress,Code.MESSAGE, m.getBytes());
        if (receiver != null) p.setDestination(receiver);
        send(p);
    }

    private void send(Pacote p) {
        try {
            if (!p.getReceiver().equals("")) {
                unicast(p.getReceiver(), p.toByteStream());
            }
            else
                multicast(p);
        } catch(Exception e) { failure("SEND: " + e.getMessage()); }
    }

    private void multicast(Pacote p) throws Exception {
        String key, sender = p.getSender();
        p.setSender(myAddress);
        for (Enumeration en = outs.keys(); en.hasMoreElements();) {
            key = (String)en.nextElement();
            if (!sender.equals(key)) unicast(key, p.toByteStream());
        }
    }

    private void unicast(String address, byte[] data) { write((OutputStream)outs.get(address), data); }

    private void write(OutputStream out, byte[] data) {
        try {
            out.write(data.length);
            out.write(data);
        } catch (Exception e) { failure("WRITE: " + e.getMessage()); }
    }
    
    public void checkout() {
        send(new Pacote(myAddress, myView.getMaster(myAddress), Code.CHECKOUT));
    }

    /**
     * Realiza um commit no reposit�rio utilizado pelo grupo.
     */
    public void commit() {
        if (this.repository != null) {
            this.repository.commit();
        } else {
            System.out.println("Repositório não existente");
        }        
    }

    /**
     * Adiciona um arquivo no reposit�rio utilizado pelo grupo.
     * @param path {@link String} Caminho do arquivo
     * @param fileName {@link String} Nome do arquivo
     */
    public void add(String path, String fileName) {
        this.repository.add(path, fileName);
    }

    /**
     * Solicita aos membros do grupo seus arquivos de reposit�rio, para que possa ser feita compara��o.
     */
    public void sincronyze() {
        checkout();
    }
    
    private final class GroupDiscoveryListener implements DiscoveryListener {

        public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
            if (!devices.contains(btDevice))
                devices.put(btDevice.getBluetoothAddress(), btDevice);
        }

        public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
            if (servRecord != null && servRecord.length > 0) {
                connectionURL = servRecord[0].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
            }
        }

        public void serviceSearchCompleted(int transID, int respCode) {
            switch (respCode) {
                case DiscoveryListener.SERVICE_SEARCH_COMPLETED :            Log.log("SERVICE_SEARCH_COMPLETED");  break;
                case DiscoveryListener.SERVICE_SEARCH_DEVICE_NOT_REACHABLE : Log.log("SERVICE_TERMINATED");        break;
                case DiscoveryListener.SERVICE_SEARCH_ERROR :                Log.log("SERVICE_SEARCH_ERROR");      break;
                case DiscoveryListener.SERVICE_SEARCH_NO_RECORDS :           Log.log("SERVICE_SEARCH_NO_RECORDS"); break;
                case DiscoveryListener.SERVICE_SEARCH_TERMINATED :           Log.log("SERVICE_SEARCH_TERMINATED"); break;
                default : Log.log("UNKNOWN RESPONSE CODE");
            }
            synchronized(lock) { lock.notifyAll(); }
        }

        public void inquiryCompleted(int discType) {
            System.out.println("aaaaaaaaaaaaaaa");
            synchronized(lock) {
                System.out.println("tesssss01");
                lock.notifyAll();
                System.out.println("tesssss02");
            }

            switch (discType) {
                case DiscoveryListener.INQUIRY_COMPLETED  : Log.log("INQUIRY_COMPLETED");     break;
                case DiscoveryListener.INQUIRY_TERMINATED : Log.log("INQUIRY_TERMINATED");    break;
                case DiscoveryListener.INQUIRY_ERROR      : Log.log("INQUIRY_ERROR");         break;
                default :                                   Log.log("UNKNOWN RESPONSE CODE"); break;
            }
        }
    }

	private final class GroupFileListener implements FileListener {
		public void file_received(FileMessage file) {                
		    repository.setFilePart(
		            file.getId(), 
		            file.getName(), 
		            file.getAddress(), 
		            file.getPartNo(),
		            file.getSize(),
		            file.getData());
		    fileUpdateListener.notify();
		}

		public void file_requested(byte[] data) {
		    try {
		        FileRequestMessage message = new FileRequestMessage();
		        message.fromByteStream(data);
		        
		        String fileId = message.getId();
		        String fileName = message.getName();
		        String fileAddress = message.getAddress();
		                
		        BGFile file = repository.getFileFromRepository(fileId, fileName, fileAddress);
		        
		        int partNo = message.getPartNo();
		        long partTotal = file.getTotalParts();
		        long size = file.totalSize();
		                
		        FileMessage fileMessage = new FileMessage(fileId, fileName, fileAddress, partNo, partTotal, size, file.toByteStream());
		        Pacote p = new Pacote(myAddress, message.getSource(), Code.FILE, fileMessage);
		        
		        send(p);
		        
		    } catch (Exception ex) {
		        ex.printStackTrace();
		    }
		}
	}

	private final class GroupRepositoryListener implements RepositoryListener {
		public void checkout_detected(String source) {
		    try {
		        System.out.println("entrou checkout detected");
		        FileMessage file = new FileMessage(
		                repository.getId(), 
		                repository.getName(), 
		                "", 
		                1, 
		                1,
		                0,
		                repository.getControlFile().toByteStream());
		        Pacote p = new Pacote(myAddress, source, Code.FILE, file);
		        System.out.println(p.toString());
		        send(p);
		        System.out.println("passou checkout detected");
		    } catch (IOException ex) {
		        ex.printStackTrace();
		    }
		}

		public void repository_received(byte[] data, String source) {
		    try {
		        BGFile controlFile = repository.getControlFile();
		        controlFile.fromByteStream(data);
		        repository.setExternalRepository(controlFile);
		        repository.syncronize();
		        TUpdate updateFiles = new TUpdate(repository, fileUpdateListener, source);
		        updateFiles.start();
		        
		    } catch (IOException ex) {
		        System.out.println("Erro RepositoryFromByteStream" + ex.getMessage());
		    }
		}
	}

	private final class GroupFileUpdateListener implements FileUpdateListener {
		public void request_file(String receiver, FileRequestMessage message) {                
		    Pacote pacote;
		    try {
		        pacote = new Pacote(myAddress, receiver, Code.REQUEST_FILE, message.toByteStream());
		        send(pacote);
		    } catch (IOException ex) {
		        ex.printStackTrace();
		    }
		}
	}

	private final class GroupFailureListener implements FailureListener {
		private void failure(String addressRemote) {
		    Pacote p = new Pacote();
		    p.setSender(myAddress);
		    if (contains(addressRemote)) {
		        receiveListener.failure((addressRemote + " falhou...").getBytes());
		        remove(addressRemote);
		    }
		    if (myView.isMaster(myAddress)) {
		        myView.removeMember(addressRemote);
		        p.setCode(Code.VIEW);
		        p.setObject(myView);
		    } else {
		        p.setReceiver(myView.getMaster(myAddress));
		        p.setCode(Code.FAILURE_NOTIFICATION);
		        p.setObject(addressRemote);
		    }
		    send(p);
		}

		public void failureNotification(byte[] data) {
		    System.out.println("FUI NOTIFICADO DE FALHA");
		    failure(new String(data));
		}

		public void failureDetected(String addressRemote) {
		    System.out.println("Realmetne, " + addressRemote + " falhou...");
		    if (myView.getMaster(myAddress).equals(addressRemote)) {
		        receiveListener.failure((addressRemote + " falhou...").getBytes());
		        //retrieveGroup(addressRemote);
		    }
		    else  failure(addressRemote);
		}
	}

	private final class GroupMessageListener implements MessageListener {
		public void receive(Pacote p) {
		    if (p.getDestination().equals("") || p.getDestination().equals(myAddress)) {
		        receiveListener.receive(p.getData());
		    }

		    if (!p.getDestination().equals(myAddress))
		        send(p);
		}
	}

	private final class GroupAcceptListener implements AcceptListener {
		String addressRemote;
		String nameRemote;

		private void connect(StreamConnection connection) {
		    try {
		        RemoteDevice rd = RemoteDevice.getRemoteDevice(connection);
		        addressRemote = rd.getBluetoothAddress();
		           nameRemote = rd.getFriendlyName(true);

		        addConnection(addressRemote, connection);
		        addOutput(addressRemote, connection.openOutputStream());

		        TReceive receive = new TReceive(connection.openInputStream(), addressRemote);
		        receive.setViewListener(viewListener);
		        receive.setAcceptListener(acceptListener);
		        receive.setMessageListener(messageListener);
		        receive.setFailureListener(failureListener);
		        receive.setReposirotyListener(repositoryListener);
		        receive.setFileListener(fileListener);
		        receive.start();
		        addInput(addressRemote, receive);
		    } catch(Exception e) { failure(e.getMessage()); }
		}

		private void accept(Member member) {
		    
		    if (myView.isMaster(myAddress)) {
		        myView.addMember(member);
		        receiveListener.view(myView.getView());
		        System.out.println("VIEW ATUALIZADA: " + myView.toString());
		        send(new Pacote(myAddress, Code.VIEW, myView));
		    } else {
		        System.out.println("EU NAO SOU MASTER");
		        Pacote p = new Pacote(myAddress, myView.getMaster(myAddress), Code.ACCEPT_NOTIFICATION, member);
		        p.setSender(myAddress);
		        System.out.println("Pacote: " + p.toString());
		        send(p);
		    }
		}

		public void requestDetected(StreamConnection connection) {
		    connect(connection);
		    Member member = new Member();
		    member.setName(nameRemote);
		    member.setAddress(addressRemote);
		    member.setMaster(myAddress);
		    accept(member);
		}

		public void acceptNotification(byte[] data) {
		    System.out.println("NOTIFICATION");
		    accept(new Member(data));
		}

		public void request(StreamConnection connection) {
		    connect(connection);
		}

		public void leave(String address) {
		    System.out.println(address + "pediu pra sair...");
		    remove(address);
		}
	}

	private final class GroupViewListener implements ViewListener {
		public void viewAccept(Pacote p) {
		    System.out.println("Recebi uma view");
		    try {
		        myView.fromByteStream(p.getData());
		        receiveListener.view(myView.getView());
		        System.out.println("VIEW: " + myView.toString());
		        send(p);
		    } catch(Exception e) { failure(e.getMessage()); }
		}
	}
    
}