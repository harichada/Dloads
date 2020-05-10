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

package com.ibm.websphere.samples.activitysessions.ASContainerManagedEJB2;
/**
 * Remote interface for Enterprise Bean: ASContainerManagedEJB2
 */
public interface ASContainerManagedEJB2 extends javax.ejb.EJBObject 
{
    /**
     * This remote method is the first method invoked on the EJB.
     * It opens a connection to the datasource..
     * @exception java.rmi.RemoteException Signals that a Remote exception of
     *            some sort has occurred.
     * @exception Exception Signals that an exception of some sort has occurred.
     */	
    void open() throws Exception, java.rmi.RemoteException;

    /**
     * This remote method is the second method invoked on the EJB.
     * It updates some data in a database.
     * @exception java.rmi.RemoteException Signals that a Remote exception of
     *            some sort has occurred.
     * @exception Exception Signals that an exception of some sort has occurred.
     */
    void performUpdates() throws Exception, java.rmi.RemoteException;

    /**
     * This remote method is the third method invoked on the EJB.
     * It completes the LTC in the direction specified.
     * @param completionDirection boolean specifies which direction to complete the LTC,
     *        with true indicating to commit, false indicating to rollback.
     * @exception java.rmi.RemoteException Signals that a Remote exception of
     *            some sort has occurred.
     * @exception Exception Signals that an exception of some sort has occurred.
     */
    void endLTC(boolean completionDirection) throws Exception, java.rmi.RemoteException;

    /**
     * This remote method is the Fourth and final method invoked on the EJB
     * @param completeLTC boolean tells the checker whether the LTC was completed
     * @param completionDirection boolean tells the checker the direction the LTC was completed.
     *        This is only relevant if completeLTC is true.
     * @exception java.rmi.RemoteException Signals that a Remote exception of
     *            some sort has occurred.
     * @exception Exception Signals that an exception of some sort has occurred.
     */
    boolean checkValues(boolean completeLTC, boolean completionDirection) throws Exception, java.rmi.RemoteException;
}

