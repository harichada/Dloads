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

import java.rmi.RemoteException;
import java.util.Properties;
import javax.ejb.*;

/**
 * This is the MasterMindBean Class, part of the ActivitySessions Samples.
 * It is a Stateful Session Bean. The bean will be deployed with an activation
 * policy of ActivitySession.  It contains private instance data which is transient.
 * 
 */
public class MasterMindGameBean implements SessionBean 
{
    private javax.ejb.SessionContext mySessionCtx = null;

    private boolean trace = false;            // Can be set true to see Activation/Passivation in System.out

    private transient int guessNumber = 0;    // Number of guesses made
    private transient int[][] guessArray;     // Array of guesses made
    private transient int[] target;           // target code generated at beginning of game
    private transient int[] targetByColour;   // target expressed as number of each of an array of colours
    private transient int[][] resultsArray;   // Array of results

    public static final int NUMBER_OF_ELEMENTS = 4;
    public static final int NUMBER_OF_COLORS   = 6;
    public static final int MAX_GUESSES        = 10;
    /**
     * Calculates the results of the latest guess.
     * @return void
     * @param guess char [] Represents the most recent guess
     */
    public boolean calculate(int[] guess) 
    {
        // correctElements represents the number of elements of the guess that are
        // matched to the target in both colour and position.
        // matchingElements represents the total number of elements in the guess
        // that also exist in the target, but not necessarily in the correct 
        // position.
        int correctElements = 0, matchingElements = 0;
        int[] guessByColour = new int[NUMBER_OF_COLORS];
        int[] return_value = new int[2];
        boolean success = false;

        // Work out correctElements and update the guess array
        for (int i=0; i<NUMBER_OF_ELEMENTS; i++)
        {
            if (guess[i]==target[i])
            {
                correctElements++;
            }
            
            guessByColour[guess[i]]++;
            guessArray[i][guessNumber] = guess[i];
        }
        for (int i=0; i<NUMBER_OF_COLORS; i++)
        {
            matchingElements = matchingElements + Math.min(guessByColour[i], targetByColour[i]);
        }
        resultsArray[0][guessNumber] = matchingElements;
        resultsArray[1][guessNumber] = correctElements;
        if (correctElements==NUMBER_OF_ELEMENTS)
        {
            success = true;
        }
        guessNumber++;
        return success;
    }
    /**
     * Public accessor for the array of guesses
     * @return int[][] 
     */
    public int[][] getGuessArray() 
    {
        return guessArray;
    }

    /**
     * Public accessor for the number of guesses made.
     * @return int The number of guesses made.
     */
    public int getGuessNumber() 
    {
        return guessNumber;
    }

    /**
     * Public accessor for the array of results.
     * @return int[][]
     */
    public int[][] getResultsArray() 
    {
        return resultsArray;
    }

    /**
     * Public accessor for the target code.
     * @return int[] - an array of integers that represnts the target code
     */
    public int[] getTarget() 
    {
        return target;
    }


    /**
     * This method resets the instance variables of the bean to default values and
     * generates a new target code to be guessed.    
     */
    public void newGame() 
    {
        // Set initial values and generate code
        guessNumber = 0;
        target = new int[NUMBER_OF_ELEMENTS];
        targetByColour = new int[NUMBER_OF_COLORS];
        guessArray = new int[NUMBER_OF_ELEMENTS][MAX_GUESSES];
        resultsArray = new int[2][MAX_GUESSES];

        for (int i=0; i<NUMBER_OF_COLORS; i++)
        {
            targetByColour[i]=0;
        }
        // Set the target and targetByColour arrays
        for (int i=0; i<NUMBER_OF_ELEMENTS; i++)
        {
            int random = (int)(Math.random()*NUMBER_OF_COLORS) % NUMBER_OF_COLORS;
            target[i] = random;
            targetByColour[random]++;
        }
    }
    // STANDARD METHODS FOR SESSIONBEAN

    /**
     * This required callback method is user for lifecycle notification.
     * No actions need to be taken for the MasterMindBean.
     */
    public void ejbActivate() 
    {
        if (trace) System.out.println("<==> ejbActivate");
    }

    /**
     * This required callback method is user for lifecycle notification.
     * No actions need to be taken for the MasterMindBean.
     */
    public void ejbCreate() {}

    /**
     * This required callback method is user for lifecycle notification.
     * No actions need to be taken for the MasterMindBean.
     */
    public void ejbPassivate() 
    {
        if (trace) System.out.println("<==> ejbPassivate");
    }

    /**
     * This required callback method is user for lifecycle notification.
     * No actions need to be taken for the MasterMindBean.
     */
    public void ejbRemove() {}

    /**
     * This required callback method sets the session context attribute for this SessionBean
     * @param ctx javax.ejb.SessionContext
     */
    public void setSessionContext(javax.ejb.SessionContext ctx) 
    {
        mySessionCtx = ctx;
    }
}
