/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluegroups.group;

import javax.bluetooth.UUID;

/**
 *
 * @author Thyago Salvá
 */
public class Service {

    // Atributes
    private String protocol;
    private String name;
    private String server;
    private UUID uuid;
    private boolean authenticate;
    private boolean encrypt;
    private boolean authorize;

    // Constructors
    public Service(String protocol, String server, UUID uuid, String name, boolean authenticate, boolean encrypt, boolean authorize) {
        this.protocol = protocol;
        this.server = server;
        this.uuid = uuid;
        this.name = name;
        this.authenticate = authenticate;
        this.encrypt = encrypt;
        this.authorize = authorize;
    }

    public Service(String name) {
        this("btspp","localhost",new UUID(0x0003),name,false,false,false);
    }

    // Sets/Gets
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getServer() {
        return this.server;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setAuthenticate(boolean authenticate) {
        this.authenticate = authenticate;
    }

    public boolean getAuthenticate() {
        return this.authenticate;
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    public boolean getEncrypt() {
        return this.encrypt;
    }

    public void setAuthorize(boolean authorize) {
        this.authorize = authorize;
    }

    public boolean getAuthorize() {
        return this.authorize;
    }
    
    // Methods
    public String toString() {
        return getProtocol() + "://" + getServer() + ":" + getUUID() + ";name=" + getName() + ";authenticate=" + getAuthenticate() + ";encrypt=" + getEncrypt() + ";authorize=" + getAuthorize();
    }
}
