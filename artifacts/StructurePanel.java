/*******************************************************************************
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the e-UCM
 *          research group.
 *   
 *    Copyright 2005-2012 e-UCM research group.
 *  
 *     e-UCM is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *  
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *  
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *  
 *  ****************************************************************************
 * This file is part of eAdventure, version 1.5.
 * 
 *   You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *  
 *  ****************************************************************************
 *       eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *  
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *  
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with Adventure.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

//http://sourceforge.net/projects/e-adventure

import java.util.List;

/**
 * This is the class of the StructurePanel, the panel at the right of the
 * editor.
 * <p>
 * 
 * @author Eugenio Marchiori
 */
public class StructurePanel {

protected List<String> structureElements;



//    private int getHeight(){
//    	return 20;
//    }

    private int calculateTranslateConstants( int newIndex, int oldIndex ) {
    	int cont = 0;

    	int INCREMENT = 5;

    	int UNSELECTED_BUTTON_HEIGHT = 35;
    	
    	int top;

       int bottom;
       int increment;
       
       int height = 20;

        if( newIndex > oldIndex ) {
            if( this.structureElements.get( oldIndex ).length() > 0 ) {
                increment = -INCREMENT;
                if( this.structureElements.get( newIndex ).length() > 0 ) {
                    bottom = height - UNSELECTED_BUTTON_HEIGHT * ( structureElements.size( ) - newIndex - 1 );
                    top = bottom - UNSELECTED_BUTTON_HEIGHT * ( newIndex - oldIndex );
                    int reach = UNSELECTED_BUTTON_HEIGHT * ( oldIndex + 1 );
                    cont = -( top - reach ) / increment;
                }
                else {
                    bottom = height;
                    top = bottom - UNSELECTED_BUTTON_HEIGHT * ( this.structureElements.size( ) - oldIndex - 1 );
                    int reach = UNSELECTED_BUTTON_HEIGHT * ( oldIndex + 1 );
                    cont = -( top - reach ) / increment;
                }
            }
            else {
                if( this.structureElements.get( newIndex ).length() > 0 ) {
                    increment = INCREMENT;
                    top = UNSELECTED_BUTTON_HEIGHT * newIndex + 40;
                    bottom = top + UNSELECTED_BUTTON_HEIGHT * ( this.structureElements.size( ) - newIndex - 1 );
                    int reach = height - UNSELECTED_BUTTON_HEIGHT * ( this.structureElements.size( ) - newIndex + 1 );
                    cont = -( top - reach ) / increment;
                }
                else {
                    cont = 0;
                }
            }
        }
        else {
            if( this.structureElements.get( oldIndex ).length() > 0 ) {
                if( this.structureElements.get( newIndex ).length() > 0 ) {
                    increment = INCREMENT;
                    top = UNSELECTED_BUTTON_HEIGHT * newIndex + UNSELECTED_BUTTON_HEIGHT;
                    bottom = top + UNSELECTED_BUTTON_HEIGHT * ( oldIndex - newIndex - 1 ) + 40;
                    int reach = height - UNSELECTED_BUTTON_HEIGHT * ( this.structureElements.size( ) - newIndex );
                    cont = -( top - reach ) / increment;
                }
                else {
                    increment = -INCREMENT;
                    top = height - UNSELECTED_BUTTON_HEIGHT * ( structureElements.size( ) - oldIndex - 1 );
                    bottom = height;
                    int reach = UNSELECTED_BUTTON_HEIGHT * ( oldIndex + 1 );
                    cont = -( top - reach ) / increment;
                }
            }
            else {
                if( this.structureElements.get( newIndex ).length() > 0 ) {
                    increment = INCREMENT;
                    top = UNSELECTED_BUTTON_HEIGHT * newIndex + UNSELECTED_BUTTON_HEIGHT;
                    bottom = UNSELECTED_BUTTON_HEIGHT * this.structureElements.size( ) - UNSELECTED_BUTTON_HEIGHT + 40;
                    int reach = height - UNSELECTED_BUTTON_HEIGHT * ( this.structureElements.size( ) - newIndex );
                    cont = -( top - reach ) / increment;
                }
                else {
                    cont = 0;
                }
            }
        }
        return cont;
    }
   

}
