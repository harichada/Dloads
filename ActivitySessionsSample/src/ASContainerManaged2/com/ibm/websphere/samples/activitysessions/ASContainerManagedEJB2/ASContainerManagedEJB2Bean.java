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
 * The ASContainerManagedEJB2 EJB implements the SessionBean interface. It is a Stateful Session
 * Bean that uses JDBC to access and update data stored in a non-transactional one phase datasource.  
 * <p>Points to note about it's deployment are as follows</p>
 * <ul>
 * <li>ActivitySession type of Container Managed</li>
 * <li>ActivitySession attribute Mandatory</li>
 * <li>Activation policy set to ActivitySession</li>
 * <li>Local Transaction Containment set to ActivitySession</li>
 * <li>Local Transaction resolution control set to Application</li>
 * <li>Local Transaction unresolved action set to rollback</li>
 * <li>Transaction attribute of NotSupported</li>
 * <li>The Datasource used by the bean has a resource sharing scope of Shareable</li>
 * </ul>
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ASContainerManagedEJB2Bean implements javax.ejb.SessionBean 
{
    private javax.ejb.SessionContext    mySessionCtx;
    private transient DataSource  ds    = null;
    private transient Connection  conn  = null;
    private int         initial_value   = 0;
    private int         final_value     = 0; 

    /**
     * This remote method is the first method the client calls on the EJB.
     * It looks up the datasource, stores it and opens a connection.
     * @exception Exception Signals that an exception of some sort has occurred.
     */
    public void open() throws Exception
    {
        InitialContext  ctx = null;

        try
        {
            ctx = new InitialContext();
            if (ds!=null)
            {
                System.out.println("Datasource NOT null at start of open");
            }
            // lookup the datasource and get a connection
            ds = (DataSource) ctx.lookup("java:comp/env/database/ASSAMPLE");

            if (ds==null)
            {
                System.out.println("Datasource lookup resulted in null value");
                throw new Exception("Datasource lookup resulted in null value");
            }

            conn = ds.getConnection();
            if (conn==null)
            {
                System.out.println("getConnection from datasource resulted in null value");
                throw new Exception("getConnection from datasource resulted in null value");
            }
            conn.setAutoCommit(false);
            conn.close();
        }
        catch (Exception e)
        {
            throw new Exception("open Failed with Exception "+e);
        }
    }

    /**
     * This method uses the ActivitySession that is created in startActivitySession.
     * It performs some JDBC to update data stored in a non-transactional one phase datasource.
     * @exception Exception indicates an unexpected error performing the method
     */
    public void performUpdates() throws Exception
    {
    	Statement stmt = null;

        try
        {
            conn = ds.getConnection();
            if (conn==null)
            {
                System.out.println("getConnection from datasource resulted in null value");
                throw new Exception("getConnection from datasource resulted in null value");
            }
            conn.setAutoCommit(false);
            // get the initial value and perform update
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT VALUE FROM ASSAMPLE.ASCONTAINERMANAGEDEJB WHERE INDEX=100");
            if (rs.next())
            {
                initial_value = rs.getInt("VALUE");
                stmt.executeUpdate("UPDATE ASSAMPLE.ASCONTAINERMANAGEDEJB SET VALUE = VALUE + 1 WHERE INDEX=100");
            }
            else
            {
                //initial_value defaults to 0
                stmt.executeUpdate("INSERT INTO ASSAMPLE.ASCONTAINERMANAGEDEJB VALUES(100, 1)");
            }

            // Cleanup RDBC artefacts we no longer need
            rs.close();
            stmt.close();
            // Close the connection - LTC will outlive this
            conn.close();
        }
        catch (Exception e)
        {
            throw new Exception("performUpdates Failed with Exception "+e);
        }
    }

    /**
     * This method will end the LTC in the direction being indicated by the 
     * value of the completionDirection parameter.
     * @param completionDirection boolean - LTC completion direction is determined
     * by the value of this parameter. true indicates commit, false indicates rollback.
     * @exception Exception indicates an unexpected error performing the method
     */
    public void endLTC(boolean completionDirection) throws Exception
    {
        // Complete the LTC in the desired direction
        try
        {
            conn = ds.getConnection();
            if (conn==null)
            {
                System.out.println("getConnection from datasource resulted in null value");
                throw new Exception("getConnection from datasource resulted in null value");
            }
            conn.setAutoCommit(false);
            if (completionDirection)
            {
                conn.commit();
            }
            else
            {
                conn.rollback();
            }
            // Close the connection as we have completed the LTC
            conn.close();
        }
        catch (Exception e)
        {
            //conn.close();
            throw new Exception("endLTCandActivitySession Failed with Exception "+e);
        }
    }

    /**
     * This method checks that the values have the expected values at the end of the ActivitySession.
     * The completeLTC and completionDirection parameters indicate whether the LTC has been completed
     * by the application and if so in which direction.
     * @param completeLTC boolean - true indicates that the application explicitly ended the LTC.
     * @param completionDirection boolean - true indicates that the LTC was committed, false indicates
     *        that the LTC was rolled back.  This is only relevant when completeLTC is true.
     * @exception Exception indicates an unexpected error performing the method 
     * @return boolean indicates whether the values are those expected
     */
    public boolean checkValues(boolean completeLTC, boolean completionDirection) throws Exception
    {
        boolean test_passed = false;

        try
        {
            InitialContext  ctx = null;
            ctx = new InitialContext();
            // lookup the datasource and get a connection
            ds = (DataSource) ctx.lookup("java:comp/env/database/ASSAMPLE");
            if (ds==null)
            {
                System.out.println("Datasource lookup resulted in null value");
                throw new Exception("Datasource lookup resulted in null value");
            }
            // see whether updates have occured
            // We need a new connection since we are in a new LTC scope
            conn = ds.getConnection();
            if (conn==null)
            {
                System.out.println("getConnection from datasource resulted in null value");
                throw new Exception("getConnection from datasource resulted in null value");
            }
            conn.setAutoCommit(true);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT VALUE FROM ASSAMPLE.ASCONTAINERMANAGEDEJB WHERE INDEX=100");
            if (rs.next())
            {
                final_value = rs.getInt("VALUE");
            } // else final_value defaults to 0
            if (completeLTC)
            {
                // LTC updates should have been completed in the direction specified.
                // NOTE the ActivitySession end mode is irrelevant.
                if (completionDirection)
                {
                    if (final_value==initial_value+1)
                    {
                        test_passed = true;
                    }
                }
                else
                {
                    if (final_value==initial_value)
                    {
                        test_passed = true;
                    }
                }
            }
            else
            {
                // LTC should have been completed in the direction indicated by the Bean's
                // Local Transaction Unresolved Action setting.
                // This should be rollback for this Bean.
                if (final_value==initial_value)
                {
                    test_passed = true;
                }
            } // else test_passed deafults to false
            // Cleanup some JDBC artefacts
            rs.close();
            stmt.close();
        }
        catch (Exception e)
        {
            conn.close();
            throw new Exception("checkValues Failed with Exception "+e);
        }
        // Close the connection as we have completed the LTC
        conn.close();
        return test_passed;
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

