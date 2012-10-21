/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluegroups.filemanager;

import java.util.Calendar;

/**
 *
 * @author Rafael
 */
public class Utils {  
    
    /**
     * Método que pega o timestamp atual.
     * @return Timestamp em milisegundos
     */
    public static String getTimeStamp() {
        Calendar calendar = Calendar.getInstance();        
        Long time;    
        
        time = new Long(calendar.getTime().getTime());        
        return time.toString(); 
    }
}
