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
 * This is a Home interface for the ActivitySessions Sample ASContainerManagedEJB Bean
 */
public interface ASContainerManagedEJBHome extends javax.ejb.EJBHome
{

    /**
     * This Home Interface Method is used to create an ASContextManagedEJB
     * @return com.ibm.websphere.samples.activitysessions.ASContainerManagedEJB
     * @exception javax.ejb.CreateException. This exception is thrown to 
     *            indicate a failure to create the entity EJB.
     * @exception java.rmi.RemoteException Signals that a Remote exception of
     *            some sort has occurred.
     */
    com.ibm.websphere.samples.activitysessions.ASContainerManagedEJB.ASContainerManagedEJB create(int index) throws javax.ejb.CreateException, java.rmi.RemoteException;

    /**
     * This Home Interface Method is used to find an ASContextManagedEJB by its primary
     * key.
     * @return com.ibm.websphere.samples.activitysessions.ASContainerManagedEJB
     * @param key com.ibm.websphere.samples.activitysessions.ASContainerManagedEJBKey
     * @exception java.rmi.RemoteException.  This exception is thrown to 
                  indicate some sort of remote exception occurred.
     * @exception javax.ejb.FinderException. This exception is thrown to 
     *            indicate a failure to find the entity EJB.
     */
    com.ibm.websphere.samples.activitysessions.ASContainerManagedEJB.ASContainerManagedEJB findByPrimaryKey(com.ibm.websphere.samples.activitysessions.ASContainerManagedEJB.ASContainerManagedEJBKey key) throws java.rmi.RemoteException, javax.ejb.FinderException;
}
