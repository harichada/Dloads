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
 * The Primary Key Class for the ASContainerManagedEJB.  The Priamary Key is
 * based on an int value.
 */

public class ASContainerManagedEJBKey implements java.io.Serializable
{
    static final long serialVersionUID = 3206093459760846163L;
    public int index;
    
    /**
     * Default constructor for ASContainerManagedKey
     */
    public ASContainerManagedEJBKey() 
    {
        
    }

    /**
     * Contructor for ASContainerManagedKey specifying the key value
     * @param argIndex int The key value
     */
    public ASContainerManagedEJBKey(int argIndex) 
    {
        index = argIndex;
    }

    /**
     * This equality method determines if the Object passed in matches this
     * Primary Key.
     * @return boolean Object representing the equality of the passed object.
     */
    public boolean equals(Object o) 
    {
        if (o instanceof ASContainerManagedEJBKey)
        {
            ASContainerManagedEJBKey otherKey = (ASContainerManagedEJBKey) o;
            return(((this.index == otherKey.index)));
        } else
            return false;
    }

    /**
     * This method returns the hash code of a Primary Key.
     * @return int Object representing the hash code of this Primary Key.
     */
    public int hashCode() 
    {
        return((new java.lang.Integer(index).hashCode()));
    }
}
