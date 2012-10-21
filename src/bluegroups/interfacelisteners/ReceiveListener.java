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
	 * M�todo que receber� um {@link Vector} com a view.
	 * @param view {@link Vector} com os membros do grupo.
	 */
    public void view(Vector view);
    
    /**
     * M�todo que recebe as mensagens enviadas atrav�s do m�todo {@link Group #send}.
     * @param data Vetor de bytes que comp�e a mensagem.
     */
    public void receive(byte[] data);

    /**
     * As mensagens de falha s�o recebidas na implementa��o desse m�todo.
     * @param data Vetor de bytes que comp�e a mensagem.
     */
    public void failure(byte[] data);
}