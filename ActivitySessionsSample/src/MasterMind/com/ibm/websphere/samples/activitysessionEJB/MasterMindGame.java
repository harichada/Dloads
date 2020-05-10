//  COPYRIGHT LICENSE: This information contains sample code provided in source 
//  code form. You may copy, modify, and distribute these sample programs in any
//  form without payment to IBM for the purposes of developing, using, marketing
//  or distributing application programs conforming to the application programming
//  interface for the operating platform for which the sample code is written. 
//  Notwithstanding anything to the contrary, IBM PROVIDES THE SAMPLE SOURCE CODE
//  ON AN "AS IS" BASIS AND IBM DISCLAIMS ALL WARRANTIES, EXPRESS OR IMPLIED,
//  INCLUDING, BUT NOT LIMITED TO, ANY IMPLIED WARRANTIES OR CONDITIONS OF
//  MERCHANTABILITY, SATISFACTORY QUALITY, FITNESS FOR A PARTICULAR PURPOSE, TITLE,
//  AND ANY WARRANTY OR CONDITION OF NON-INFRINGEMENT. IBM SHALL NOT BE LIABLE FOR
//  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL OR CONSEQUENTIAL DAMAGES ARISING OUT
//  OF THE USE OR OPERATION OF THE SAMPLE SOURCE CODE. IBM HAS NO OBLIGATION TO
//  PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS OR MODIFICATIONS TO THE
//  SAMPLE SOURCE CODE.

package com.ibm.websphere.samples.activitysessionEJB;

/**
 * The Remote Interface for the ActivitySessions Sample - MasterMind
 */
public interface MasterMindGame extends javax.ejb.EJBObject 
{

    /**
     * This remote method is used to calculate the results of the latest guess.
     * @return boolean Indicates if the guess is correct
     * @param guess int [] Represents the most recent guess
     * @exception java.rmi.RemoteException Signals that a RemoteException has occured.
     */
    boolean calculate(int [] guess) throws java.rmi.RemoteException;

    /**
     * This remote method is used to return the array of guesses made so far.
     * @return int [][] Representing an array of all the guesses made this game.
     * @exception java.rmi.RemoteException Signals that a RemoteException has occured.
     */
    int [][] getGuessArray() throws java.rmi.RemoteException;
    
    /**
     * This remote method is used to return the number of guesses made so far.
     * @return int
     * @exception java.rmi.RemoteException Signals that a RemoteException has occured.
     */
    int getGuessNumber() throws java.rmi.RemoteException;

    /**
     * This remote method is used to return the array of Results. There is one results for each guess.
     * @return int [][]
     * @exception java.rmi.RemoteException Signals that a RemoteException has occured.
     */
    int [][] getResultsArray() throws java.rmi.RemoteException;

    /**
     * This remote method is used to return the target array.
     * @return int []
     * @exception java.rmi.RemoteException Signals that a RemoteException has occured.
     */
    int [] getTarget() throws java.rmi.RemoteException;

    /**
     * This remote method is used to inform the bean that a new game has been requested.
     * @exception java.rmi.RemoteException Signals that a RemoteException has occured.
     */
    void newGame() throws java.rmi.RemoteException;
}
