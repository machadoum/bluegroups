/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluegroups.interfacelisteners;

import java.util.Vector;

import bluegroups.group.Group;

/**
 *	Interface que deve ser implementada para receber as mensagens enviadas pelos dispositivos de um grupo.
 */
public interface ReceiveListener {

    // Methods
	/**
	 * Método que receberá um {@link Vector} com a view.
	 * @param view {@link Vector} com os membros do grupo.
	 */
    public void view(Vector view);
    
    /**
     * Método que recebe as mensagens enviadas através do método {@link Group #send}.
     * @param data Vetor de bytes que compõe a mensagem.
     */
    public void receive(byte[] data);

    /**
     * As mensagens de falha são recebidas na implementação desse método.
     * @param data Vetor de bytes que compõe a mensagem.
     */
    public void failure(byte[] data);
}