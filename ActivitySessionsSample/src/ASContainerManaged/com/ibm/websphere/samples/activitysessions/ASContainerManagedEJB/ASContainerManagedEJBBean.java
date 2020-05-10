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

import java.rmi.RemoteException;
import java.util.Properties;
import javax.ejb.*;

/**
 * The ASContainerManagedEJB CMP EJB Bean implements the EntityBean interface. 
 * It contains two instance variables that are both of type <code>int</code>.  These are <code>index</code>, which
 * is the key value for the EJB, and <code>value</code>.  
 * <p>Points to note about it's deployment are as follows</p>
 * <ul>
 * <li>The bean will be deployed such that it's data is backed into a one phase datastore</li>  
 * <li>Activation policy set to ActivitySession</li>
 * <li>Local Transaction Containment set to ActivitySession</li>
 * <li>Local Transaction resolution control set to ContainerAtBoundary</li>
 * <li>Transaction attribute is NotSupported</li>
 * <li>ActivitySession type of Container Managed</li>
 * <li>ActivitySession attribute set to Mandatory</li>
 * </ul>
 */
public abstract class ASContainerManagedEJBBean implements javax.ejb.EntityBean
{
    private javax.ejb.EntityContext entityContext = null;
    private int hitCount = 0;

     /**
     * Getter method for index
     * @return int
     */
    public abstract int getIndex();
    
    /**
     * Setter method for value
     * @param newIndex int
     */
    public abstract void setIndex(int newIndex); 
    
    /**
     * Getter method for value
     * @return int
     */
    public abstract int getValue();

    /**
     * Setter method for value
     * @param newValue int
     */
    public abstract void setValue(int newValue); 

    /**
     * Getter method for hitCount
     * @return int
     */
    public int getHitCount() 
    {
        return hitCount;
    }

    /**
     * Increment hit count
     */
    public void incrementHitCount() 
    {
        hitCount++;
    }

    // 
    // Home Interface Methods
    //

    /**
     * This Home Interface Method is responsible for creation.
     * It sets the index attribute to the value passed in.
     * @param argIndex int
     * @exception javax.ejb.CreateException The exception description.
     */
    public ASContainerManagedEJBKey ejbCreate(int argIndex) throws javax.ejb.CreateException 
    {
        // CMP fields should be initialized here. value has default already.
        setIndex(argIndex);
        // Set hit count to 0
        hitCount = 0;
        return null;
    }
	
    /**
     * ejbPostCreate
     */
    public void ejbPostCreate(int index) throws javax.ejb.CreateException 
    {
    }
	
    //
    // Required callback methods
    //
    
    /**
     * Required callback method for Contaimer Managed Persistence.
     */ 
    public void ejbLoad() {}

    /**
     * Required callback method for Contaimer Managed Persistence.
     */ 
    public void ejbRemove() {}

    /**
     * Required callback method for Contaimer Managed Persistence.
     */ 
    public void ejbStore() {}

    /**
     * getEntityContext
     */
    public javax.ejb.EntityContext getEntityContext() 
    {
    	return entityContext;
    }
	
    /**
     * This required callback method sets the entity context attribute for this EntityBean
     * @param ctx javax.ejb.EntityContext
     */
    public void setEntityContext(javax.ejb.EntityContext ctx) 
    {
        entityContext = ctx;
    }

    /**
     * This is a required callback method.
     */
    public void unsetEntityContext() 
    {
        entityContext = null;
    }

    /**
     * This required callback method is used for lifecycle notification. No 
     * actions need to be taken for ASContextManagedEJB.
     */ 
    public void ejbActivate () {}
    /**
     * This required callback method is used for lifecycle notification. No 
     * actions need to be taken for ASContextManagedEJB.
     */ 
    public void ejbPassivate () {}     
}
