/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chat;

import java.util.Vector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.*;

import bluegroups.*;

/**
 * @author Thyago Salvá
 */
public class Chat1 extends MIDlet implements CommandListener {

    private Display display;
    private Form fChat;

    private Command btCriar, btBuscar, btSair, btServicos, btVoltar, btConectar;
    private Image im;
//    public SChat cs = null;
//    public SChat cc = null;
    private SFormAlerta sfAlerta;
    private SHistMessage hm;
    private SBoxMessage bm;
    private Vector vecDevices, vecServices;
    private List lDevices, lServices;

    public Chat1() {
        
        //Command
        btCriar    = new Command("Criar", Command.OK, 1);
        btBuscar   = new Command("Buscar", Command.OK, 2);
        btSair     = new Command("Sair", Command.EXIT, 3);
        btVoltar   = new Command("Voltar", Command.BACK, 4);
        btServicos = new Command("Serviços", Command.SCREEN, 3);
        btConectar = new Command("Conectar", Command.OK, 3);

        //FormChat
        fChat = new Form("FormChat");

        //List
        lDevices = new List("Devices", List.IMPLICIT);
        lDevices.addCommand(btServicos);
        lDevices.addCommand(btVoltar);

        lServices = new List("Services", List.IMPLICIT);
        lServices.addCommand(btConectar);
        lServices.addCommand(btVoltar);

        //SBoxMessage
        hm = new SHistMessage(this);
        bm = new SBoxMessage(this);

        //Alerta
        sfAlerta = new SFormAlerta("",this);


        //Criando Imagem
        try {

            im = Image.createImage("/img/blue.png");
            
        } catch(Exception e) { }
    }

    public void setCurrent(Displayable d) {
        display.setCurrent(d);
    }

    public SHistMessage getHistMessage() {
        return this.hm;
    }

    public SBoxMessage getBoxMessage() {
        return this.bm;
    }

    public Form getPrincipal() {
        return this.fChat;
    }

    public void startApp() {

        //Configurando um receptor de eventos para o form
        //Adicionando os eventos ao form
        fChat.addCommand(btCriar);
        fChat.addCommand(btBuscar);
        fChat.addCommand(btSair);
        fChat.setCommandListener(this);

        //Adicionando imagem
        fChat.append(new ImageItem(null, im, ImageItem.LAYOUT_CENTER, null));

        lDevices.setCommandListener(this);
        lServices.setCommandListener(this);

        //Display
        display = Display.getDisplay(this);
        display.setCurrent(fChat);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable d) {
//        if (c == btSair) {
//            destroyApp(true);
//            notifyDestroyed();
//        } else if (c == btCriar) {
//            if (cs != null)
//                cs = null;
//            cs = new SChat(this);
//            cs.start();
//        } else if (c == btBuscar) {
//            buscarDevices();
//        }
//
//        if (d == lDevices) {
//
//            if (c == btServicos) {
//                buscarServices(lDevices.getSelectedIndex());
//            } else if(c == btVoltar)  {
//                display.setCurrent(fChat);
//            }
//        }
//
//        if (d == lServices) {
//
//            if (c == btConectar) {
//                conectarService(lServices.getSelectedIndex());
//            } else if(c == btVoltar)  {
//                display.setCurrent(lDevices);
//            }
//        }
    }

//     public void conectarService(int k) {
//        cc.start();
//        cc.conectarService(k);
//        display.setCurrent(bm);
//    }
//
//    public void buscarDevices() {
//        cc = new SChat(this);
//        cc.buscarDevices();
//        vecDevices = cc.getVecDevices();
//        lDevices.deleteAll();
//        String aux;
//        if (vecDevices.size() > 0) {
//            for (int i = 0; i < vecDevices.size(); i++) {
//                aux = (String)vecDevices.elementAt(i);
//                lDevices.append(aux, null);
//            }
//            display.setCurrent(lDevices);
//        } else {
//            display.setCurrent(new Alert("Resultado", "Nenhum dispositivo encontrado",null,null), lDevices);
//        }
//    }
//
//    private void buscarServices(int k) {
//        cc.buscarServices(k);
//        vecServices = cc.getVecServices();
//        lServices.deleteAll();
//        String aux;
//        if (vecServices.size() > 0) {
//            for (int i = 0; i < vecServices.size(); i++) {
//                aux = (String)vecServices.elementAt(i);
//                lServices.append(aux, null);
//            }
//            display.setCurrent(lServices);
//        } else {
//            display.setCurrent(new Alert("Resultado", "Nenhum serviço encontrado",null,null), lServices);
//        }
//    }
}
