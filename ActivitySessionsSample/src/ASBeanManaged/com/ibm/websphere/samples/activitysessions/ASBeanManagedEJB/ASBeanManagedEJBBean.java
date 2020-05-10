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

package com.ibm.websphere.samples.activitysessions.ASBeanManagedEJB;

/**
 * The ASBeanManagedEJB CMP EJB Bean implements the Session interface. It is a Stateless Session
 * Bean that uses JDBC to access and update data stored in a non-transactional one phase datasource.  
 * <p>Points to note about it's deployment are as follows</p>
 * <ul>
 * <li>Activation policy set to TRANSACTION</li>
 * <li>Local Transaction Containment set to BeanMethod</li>
 * <li>Local Transaction resolution control set to Application</li>
 * <li>ActivitySession type of Bean Managed</li>
 * </ul>
 */

import java.sql.*;
import javax.sql.*;
import javax.naming.*;
import com.ibm.websphere.ActivitySession.*;
import com.ibm.websphere.samples.activitysessions.ASContainerManagedEJB2.*;  
import java.util.*;
import java.io.*;

public class ASBeanManagedEJBBean implements javax.ejb.SessionBean 
{
    private javax.ejb.SessionContext 	mySessionCtx;
    private int         initial_value   = 0;
    private int         final_value     = 0;
	
    /**
     * This remote method is called by the client to run the sample.
     * <p>The bean performs three variations of the same basic sequence</p>
     * <ol>
     * <li>starts an ActivitySession</li>
     * <li>calls a method on an instance of ASContainerManagedEJB2 that opens a connection to 
     * a non-transactional one phase datasource, thereby starting a Resource Manager Local Transaction (RMLT)</li>
     * <li>calls a method on an instance of ASContainerManagedEJB2 that updates data in the datasource</li>
     * <li>either calls a method on an instance of ASContainerManagedEJB2 that commits or rolls back the RMLT. 
     * Note if this isn't called the RMLT is left active (or dangling). </li> 
     * <li>ends the ActivitySession with an end mode of EndModeCheckPoint.</li>
     * <li>calls a method on an instance of ASContainerManagedEJB2 to obtain results and checks them.</li>
     * </ol>
     * <p>The first sequence uses the commit option, the second uses the rollback option and the third
     * leaves the local transaction active.</p>
     * @return String[] that the client can output to the user.
     */
    public String performTasks()
    {
    	InitialContext ctx = null;
        UserActivitySession uAS = null;
        ASContainerManagedEJB2 theBean = null;
        ASContainerManagedEJB2Home Home = null;
        ByteArrayOutputStream outStr = new ByteArrayOutputStream();
        PrintStream writer = new PrintStream(outStr, true);
                
        writer.println("Starting ActivitySessions Sample");
        Properties props = new Properties();
        props.put( javax.naming.Context.INITIAL_CONTEXT_FACTORY, "com.ibm.websphere.naming.WsnInitialContextFactory" );

        try
        {
            ctx = new InitialContext( props );
            uAS = (UserActivitySession) ctx.lookup("java:comp/websphere/UserActivitySession");
            if(uAS==null)
            {
                throw new Exception("UserActivitySession is NULL");
            }
            //--------------------------------------------------------------------------------------
            // Set up the Home
            //--------------------------------------------------------------------------------------
            writer.println("Retrieving an ASContainerManagedEJB2Home from JNDI");
            Home = (ASContainerManagedEJB2Home) ctx.lookup("java:comp/env/ejb/ASContainerManagedEJB2");
            if(Home == null)
            {
            	writer.println("ASContainerManagedEJB2Home is NULL");
                throw new Exception("ASContainerManagedEJB2 is NULL");
            }
            //--------------------------------------------------------------------------------------
            // Create EJB instance
            //--------------------------------------------------------------------------------------
            writer.println("Creating EJB instance");
            theBean = Home.create();

            //--------------------------------------------------------------------------------------
            // Run 1st Sequence - commit Local Transaction in bean before ending ActivitySession
            //--------------------------------------------------------------------------------------
            writer.println("Starting an ActivitySession");
            uAS.beginSession();
            writer.println("Calling open");
            theBean.open();
            writer.println("Calling performUpdates");
            theBean.performUpdates();
            writer.println("Calling endLTC to end LTC with commit");
            theBean.endLTC(true);
            writer.println("Ending an ActivitySession with EndModeCheckPoint");
            uAS.endSession(UserActivitySession.EndModeCheckPoint);
            writer.println("Checking values");
            uAS.beginSession();
            if(theBean.checkValues(true, true))
            {
                writer.println("Values correct");
            } else {
                writer.println("Values incorrect");
                throw new Exception("Values incorrect for commit local transaction scenario");
            }
            uAS.endSession(UserActivitySession.EndModeReset);
            
            //--------------------------------------------------------------------------------------
            // Run 2nd Sequence - rollback Local Transaction in bean before ending ActivitySession
            //--------------------------------------------------------------------------------------
            writer.println("Starting an ActivitySession");
            uAS.beginSession();
            writer.println("Calling open");
            theBean.open();
            writer.println("Calling performUpdates");
            theBean.performUpdates();
            writer.println("Calling endLTC to end LTC with rollback");
            theBean.endLTC(false);
            writer.println("Ending an ActivitySession with EndModeCheckPoint");
            uAS.endSession(UserActivitySession.EndModeCheckPoint);
            writer.println("Checking values");
            uAS.beginSession();
            if(theBean.checkValues(true, false))
            {
                writer.println("Values correct");
            } else {
                writer.println("Values incorrect");
                throw new Exception("Values incorrect for rollback local transaction scenario");
            }
            uAS.endSession(UserActivitySession.EndModeReset);
            
            //--------------------------------------------------------------------------------------
            // Run 3rd Sequence - leave Local Transaction dangling when ending ActivitySession  
            //--------------------------------------------------------------------------------------
            writer.println("Starting an ActivitySession");
            uAS.beginSession();
            writer.println("Calling open");
            theBean.open();
            writer.println("Calling performUpdates");
            theBean.performUpdates();
            writer.println("endLTC not called leaving LTC dangling");
            writer.println("Ending an ActivitySession with EndModeCheckPoint");
            uAS.endSession(UserActivitySession.EndModeCheckPoint);
            writer.println("Checking values");
            uAS.beginSession();
            if(theBean.checkValues(false, false))
            {
                writer.println("Values correct");
            } else {
                writer.println("Values incorrect");
                throw new Exception("Values incorrect for dangling local transaction scenario");
            }
            uAS.endSession(UserActivitySession.EndModeReset);
            writer.println("Sample has completed successfully");

        }
        catch(Exception e)
        {
            e.printStackTrace();
            writer.println("Sample has failed with unexpected exception");
            writer.println("Exception is " + e);
            
            //----------------------------------------------------------------------------------
            // End any ActivitySession that may still be active
            //----------------------------------------------------------------------------------
            try
            {
                uAS.endSession(UserActivitySession.EndModeReset);
            }
            catch(NoActivitySessionException exc)
            {
                // We don't care if there wan't an ActivitySession active
            }
            catch(Exception exc)
            {
                writer.println("Unexpected error ending ActivitySession after error in sample");
            }
        }
        return outStr.toString();
    }	
    
    /**
     * getSessionContext
     */
    public javax.ejb.SessionContext getSessionContext() 
    {
       	return mySessionCtx;
    }

    /**
     * setSessionContext
     */
    public void setSessionContext(javax.ejb.SessionContext ctx) 
    {
        mySessionCtx = ctx;
    }
    
    /**
     * ejbActivate
     */
    public void ejbActivate() 
    {
    }

    /**
     * ejbCreate
     */
    public void ejbCreate()  
    {
    }

    /**
     * ejbPassivate
     */
    public void ejbPassivate() 
    {
    }

    /**
     * ejbRemove
     */
    public void ejbRemove() 
    {
    }
}

