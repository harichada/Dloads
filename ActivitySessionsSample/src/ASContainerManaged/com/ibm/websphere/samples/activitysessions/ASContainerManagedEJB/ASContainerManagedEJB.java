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

package com.ibm.websphere.samples.activitysessions.ASContainerManagedEJB;

/**
 * The Remote Interface Buisness Methods for the ActivitySessions
 * Container Managed CMP Sample.
 */
public interface ASContainerManagedEJB extends javax.ejb.EJBObject
{
    /**
     * Getter method for index
     * @return int
     * @exception java.rmi.RemoteException Signals that a Remote exception of
     *            some sort has occurred.
     */
    int getIndex() throws java.rmi.RemoteException;


    /**
     * Getter method for value
     * @return int
     * @exception java.rmi.RemoteException Signals that a Remote exception of
     *            some sort has occurred.
     */
    int getValue() throws java.rmi.RemoteException;

    /**
     * Setter method for value
     * @param newValue int
     * @exception java.rmi.RemoteException Signals that a Remote exception of
     *            some sort has occurred.
     */
    void setValue(int newValue) throws java.rmi.RemoteException;

    /**
     * Getter method for hitCount
     * @return int
     * @exception java.rmi.RemoteException Signals that a Remote exception of
     *            some sort has occurred.
     */
    int getHitCount() throws java.rmi.RemoteException;

    /**
     * Increments the variable hitCount
     * @exception java.rmi.RemoteException Signals that a Remote exception of
     *            some sort has occurred.
     */
    void incrementHitCount() throws java.rmi.RemoteException;

}
