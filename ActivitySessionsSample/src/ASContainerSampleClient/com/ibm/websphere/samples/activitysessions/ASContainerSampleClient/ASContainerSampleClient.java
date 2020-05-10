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

package com.ibm.websphere.samples.activitysessions.ASContainerSampleClient;

import com.ibm.websphere.samples.activitysessions.ASContainerManagedEJB.*;  
import com.ibm.websphere.ActivitySession.*;
import java.util.*;
import javax.naming.InitialContext;

/**
 * An ActivitySessions Sample.  This client uses the UserActivitySession interface to begin
 * and end ActivitySessions.  It updates instances of the ASContainerManagedEJB inside ActivitySessions
 * and verifies that the updates are committed when the ActivitySession is ended with EndModeCheckPoint
 * and that they are rolled back when the ActivitySession is ended with EndModeReset.
 */
public class ASContainerSampleClient
{

    /**
     *
     * ASContainerSampleClient constructor.
     */
    public ASContainerSampleClient() 
    {
        super();
    }

    /**
     * The function method performs the all the work.
     */
    public void function() 
    {
        InitialContext ctx = null;
        UserActivitySession uAS = null;
        ASContainerManagedEJB theBean = null, theBean2 = null;
        ASContainerManagedEJBHome Home = null;
        int key = 1, key2 = 100;
        int initial_value = 0, middle_value = 0, final_value = 0;
        int initial_value2 = 0, middle_value2 = 0, final_value2 = 0;
        int initial_hitCount = 0, initial_hitCount2 = 0;
        
        System.out.println("Starting ActivitySessions Sample");
                
        try
        {
            //--------------------------------------------------------------------------------------
            // Set up UserActivitySession
            //--------------------------------------------------------------------------------------
            System.out.println("Accessing UserActivitySession");
            ctx = new InitialContext();
            uAS = (UserActivitySession) ctx.lookup("java:comp/websphere/UserActivitySession");
            if(uAS == null)
            {
                System.out.println("UserActivitySession is NULL");
                throw new Exception("UserActivitySession is NULL");
            }

            //--------------------------------------------------------------------------------------
            // Set up the Home
            //--------------------------------------------------------------------------------------
            System.out.println("Retrieving an ASContainerManagedEJBHome from JNDI");
            Home = (ASContainerManagedEJBHome) ctx.lookup("java:comp/env/ASContainerManagedEJB");
            if(Home == null)
            {
                System.out.println("ASContainerManagedEJBHome is NULL");
                throw new Exception("ASContainerManagedEJBHome is NULL");
            }
            
            //--------------------------------------------------------------------------------------
            // Find or Create EJB instances
            //--------------------------------------------------------------------------------------
            System.out.println("Finding or Creating EJB instances");
            ASContainerManagedEJBKey theKey = new ASContainerManagedEJBKey(key);
            try
            {
                theBean = Home.findByPrimaryKey(theKey);
            } 
            catch(javax.ejb.ObjectNotFoundException e)
            {
                theBean = Home.create(key);
            } 
          
            ASContainerManagedEJBKey theKey2 = new ASContainerManagedEJBKey(key2);
            try
            {
                theBean2 = Home.findByPrimaryKey(theKey2);
            } 
            catch ( javax.ejb.ObjectNotFoundException e)
            {
                theBean2 = Home.create(key2);
            } 
         
            //--------------------------------------------------------------------------------------
            // Begin the First ActivitySession 
            //--------------------------------------------------------------------------------------
            System.out.println("Starting First ActivitySession");
            uAS.beginSession();
                
            //--------------------------------------------------------------------------------------
            // Update the objects 
            //--------------------------------------------------------------------------------------
            System.out.println("Updating EJBs");
            initial_hitCount = theBean.getHitCount();
            initial_hitCount2 = theBean2.getHitCount();
            initial_value = theBean.getValue();
            theBean.incrementHitCount();
            theBean.setValue(initial_value + 1);
            theBean.incrementHitCount();
            initial_value2 = theBean2.getValue();
            theBean2.incrementHitCount();
            theBean2.setValue(initial_value2 + 1);
            theBean2.incrementHitCount();
            //--------------------------------------------------------------------------------------
            // Check hitCount has increased by two to show same instance is used
            //--------------------------------------------------------------------------------------
            if((theBean.getHitCount() != (initial_hitCount+2)) || (theBean2.getHitCount() != (initial_hitCount2+2)))
            {
                  System.out.println("hitCount variable not set to expected value");
                  throw new Exception("Sample has failed, different bean instances used in same activity session");
            }
            //--------------------------------------------------------------------------------------
            // End the ActivitySession with a CheckPoint
            //--------------------------------------------------------------------------------------
            System.out.println("Ending ActivitySession with Checkpoint");
            uAS.endSession(UserActivitySession.EndModeCheckPoint);

            //--------------------------------------------------------------------------------------
            // Begin the Second ActivitySession 
            //--------------------------------------------------------------------------------------
            System.out.println("Starting Second ActivitySession");
            uAS.beginSession();

            //--------------------------------------------------------------------------------------
            // Update the objects 
            //--------------------------------------------------------------------------------------
            System.out.println("Updating EJBs");
            initial_hitCount = theBean.getHitCount();
            initial_hitCount2 = theBean2.getHitCount();
            middle_value = theBean.getValue();
            theBean.incrementHitCount();
            theBean.setValue(middle_value + 10);
            theBean.incrementHitCount();
            middle_value2 = theBean2.getValue();
            theBean2.incrementHitCount();
            theBean2.setValue(middle_value2 + 10);
            theBean2.incrementHitCount();
            //--------------------------------------------------------------------------------------
            // Check hitCount has increased by two to show same instance is used
            //--------------------------------------------------------------------------------------
            if((theBean.getHitCount() != (initial_hitCount+2)) || (theBean2.getHitCount() != (initial_hitCount2+2)))
            {
                  System.out.println("hitCount variable not set to expected value");
                  throw new Exception("Sample has failed, different bean instances used in same activity session");
            }
            //--------------------------------------------------------------------------------------
            // End the ActivitySession with a Reset
            //--------------------------------------------------------------------------------------
            System.out.println("Ending ActivitySession with Reset");
            uAS.endSession(UserActivitySession.EndModeReset);
       
            //--------------------------------------------------------------------------------------
            // Get final value and check results
            //--------------------------------------------------------------------------------------
            System.out.println("Checking values");
            uAS.beginSession();
            final_value = theBean.getValue();
            final_value2 = theBean2.getValue(); 
            uAS.endSession(UserActivitySession.EndModeReset);
            if((middle_value==initial_value+1)
            && (final_value==middle_value)
            && (middle_value2==initial_value2+1)
            && (final_value2==middle_value2))
            {
                System.out.println("Sample has completed successfully");
            } else {
                System.out.println("Sample Failed, values are not consistent with ActivitySession outcomes");
            }
        }
        catch(Exception e)
        {
            System.out.println("Sample has failed with unexpected exception");
            System.out.println("Exception is " + e);
            System.out.println("Stack Trace Follows");
            e.printStackTrace();
            //--------------------------------------------------------------------------------------
            // End any uncompleted activity sessions
            //--------------------------------------------------------------------------------------
            try
            {
                uAS.endSession(UserActivitySession.EndModeReset);
            }
            catch(Exception exc)
            {
                // Not interesed in any exceptions here.
            }
        }
    }
    /**
     * main method for this client.  It does not use any parameters passed to it.
     * It constructs an instance of the class and calls the function method.
     */
    public static void main(String[] args) 
    {
        ASContainerSampleClient client = new ASContainerSampleClient();
        client.function();
    }
}
