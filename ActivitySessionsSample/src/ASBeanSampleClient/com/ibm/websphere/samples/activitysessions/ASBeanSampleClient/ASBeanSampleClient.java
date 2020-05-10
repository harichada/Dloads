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

package com.ibm.websphere.samples.activitysessions.ASBeanSampleClient;

import com.ibm.websphere.samples.activitysessions.ASBeanManagedEJB.*;  
import com.ibm.websphere.ActivitySession.*;
import java.util.*;
import javax.naming.InitialContext;

/**
 * An ActivitySessions Sample.  
 * <p>The client creates an instance of the ASBeanManagedEJB bean and
 * invokes a method on it, displaying the results.  This is really just
 * way to start the sample.
 * </p>
 */
public class ASBeanSampleClient
{

    /**
     *
     * ASBeanSampleClient constructor.
     */
    public ASBeanSampleClient() 
    {
        super();
    }

    /**
     * The function method performs the all the work.
     */
    public void function(String[] args)
    {
        InitialContext ctx = null;
        UserActivitySession uAS = null;
        ASBeanManagedEJB theBean = null;
        ASBeanManagedEJBHome Home = null;

        System.out.println("Starting ActivitySessions Sample");
        
        try
        {
            ctx = new InitialContext();

            //--------------------------------------------------------------------------------------
            // Set up the Home
            //--------------------------------------------------------------------------------------
            System.out.println("Retrieving an ASBeanManagedEJBHome from JNDI");
            Home = (ASBeanManagedEJBHome) ctx.lookup("java:comp/env/ASBeanManagedEJB");
            if(Home == null)
            {
                System.out.println("ASBeanManagedEJBHome is NULL");
                throw new Exception("ASBeanManagedEJBHome is NULL");
            }
            //--------------------------------------------------------------------------------------
            // Create EJB instance
            //--------------------------------------------------------------------------------------
            System.out.println("Creating EJB instance");
            theBean = Home.create();

            //--------------------------------------------------------------------------------------
            // Run the sample - call the Bean and show output 
            //--------------------------------------------------------------------------------------
            System.out.println("Calling ASBeanManagedEJB to perform tasks");
            String output = theBean.performTasks();
            System.out.println(output);
        }
        catch(Exception e)
        {
            System.out.println("Sample has failed with unexpected exception");
            System.out.println("Exception is " + e);
            System.out.println("Stack Trace Follows");
            e.printStackTrace();
        }
    }
    /**
     * main method for this client.  It does not use any parameters passed to it.
     * It constructs an instance of the class and calls the function method.
     */
    public static void main(String[] args) 
    {
        ASBeanSampleClient client = new ASBeanSampleClient();
        client.function(args);
    }
}
