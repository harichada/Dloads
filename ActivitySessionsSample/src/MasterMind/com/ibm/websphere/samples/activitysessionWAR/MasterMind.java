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

package com.ibm.websphere.samples.activitysessionWAR;

import javax.naming.InitialContext;
import javax.servlet.http.*;
import java.io.*;
import com.ibm.websphere.samples.activitysessionEJB.*;
/**
 * The MasterMind Servlet.
 * <br>At the start of a game the Servlet creates a new HttPSession and
 * stores a reference to an instance of the MasterMindGame Entity EJB.
 * The servlet solicits guesses from the user and displays the results.
 * Once the game is over, either because the guess is corrrect, 
 * too many guesses have been attempted, or a new game has been requested, 
 * the session is invalidated and the EJB removed.
 * <br>Since the Servlet is deployed with an activity session control of
 * webcontainer, an ActivitySession is associated with the HttpSession.
 * This ties in with the activation policy of the EJB, which is ActivitySession,
 * and hence the EJB remains in active state for the duration of the HttpSession,
 * or until it is deleted.
 */
public class MasterMind extends javax.servlet.http.HttpServlet 
{
    private final static String[] colourArray = { "Red   ", "Green ", "Blue  ", "Yellow", "White ", "Pink  "};
    private final char[] imageMap = {'R', 'G', 'B', 'Y', 'W', 'P'};
    private final static String ejbRef = "ejbRef";

    /**
     * Process incoming HTTP GET requests by passing them to performTask 
     * 
     * @param request Object that encapsulates the request to the servlet 
     * @param response Object that encapsulates the response from the servlet
     */
    public void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException 
    {
        performTask(request, response);
    }

    /**
     * Process incoming HTTP POST requests by passing them to performTask 
     * 
     * @param request Object that encapsulates the request to the servlet 
     * @param response Object that encapsulates the response from the servlet
     */
    public void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException 
    {
        performTask(request, response);
    }

    /**
     * Returns the servlet info string.
     * @return String
     */
    public String getServletInfo() 
    {
        return super.getServletInfo();
    }

    /**
     * Initializes the servlet.
     */
    public void init() 
    {
    }

    /**
     * Process incoming requests for information
     * The method examines the action in the form and either processes a new game,
     * a guess, or a request for Instructions.
     * @param request Object that encapsulates the request to the servlet 
     * @param response Object that encapsulates the response from the servlet
     */
    public void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) 
    {
        try
        {
            PrintWriter out = null;
            MasterMindGame theBean = null;

            response.setContentType("text/html");
            out = response.getWriter();

            outputHeader(out);
            // Get parameters
            String values[];

            String action = "";
            values = request.getParameterValues("action");
            if (values != null)
            {
                action = values[0];
            }

            // IF action is New Game, reset the game
            if (action.equals("New Game"))
            {
                // if there is an HttpSession, invalidate it
                HttpSession session = request.getSession(false);
                if (session!=null)
                {
                    // Get our reference to the bean that we've stored in the HttpSession and Remove it
                    theBean = (MasterMindGame) session.getAttribute(ejbRef);
                    if(theBean != null)
                    { 
                        theBean.remove();
                    }
                    session.invalidate();
                }
                // create a new session
                session = request.getSession(true);
                // Create a new Bean for this HttpSession and store it in the HttpSession
                InitialContext ctx = new InitialContext();
                Object homeObject = ctx.lookup("java:comp/env/MasterMindGame");
                MasterMindGameHome mmgHome = (MasterMindGameHome)javax.rmi.PortableRemoteObject.narrow(homeObject, MasterMindGameHome.class);
                theBean = mmgHome.create();
                theBean.newGame();
                session.setAttribute(ejbRef, theBean);
                
                // Call outputGame to output input fields
                boolean graphics = (request.getParameterValues("graphics")!=null);
                outputGame(out, theBean, graphics, false, session);
                out.println("<input type=submit value=\"Instructions\" name=action>");
                if (graphics)
                {
                    out.println("<input type=\"checkbox\" checked value=\"on\" name=\"graphics\" id=\"graphics\"><label for=\"graphics\">Display game graphically</label>");
                } else
                {
                    out.println("<input type=\"checkbox\" value=\"on\" name=\"graphics\" id=\"graphics\"><label for=\"graphics\">Display game graphically</label>");
                }
                out.println("</form>");
            } else if (action.equals("Guess"))
            {
                // A guess has been made.  Get the guess from the HttpSession attributes
                // and pass it to the bean for processing.
                HttpSession session = request.getSession(false);
                boolean graphics = (request.getParameterValues("graphics") != null);
                if(session == null)
                {
                    out.println("ERROR - no HTTPSession.  HTTPSession timeout may have occurred.");
                    out.println("<form>");
                } else {
                    // Get our reference to the bean that we've stored in the HttpSession
                    theBean = (MasterMindGame) session.getAttribute(ejbRef);
                    if(theBean == null)
                    {
                        out.println("ERROR - no enterprise bean reference in HTTPSession.");
                        out.println("<form>");
                    } else {
                        // Get the guess
                        int guess[] = new int[MasterMindGameBean.NUMBER_OF_ELEMENTS];
                        for (int element=0; element < MasterMindGameBean.NUMBER_OF_ELEMENTS; element++)
                        {
                            values = request.getParameterValues("element"+element);
                            if (values != null)
                            {
                                guess[element] = Integer.parseInt(values[0]);
                            }
                        }

                        // Make the guess
                        boolean success = theBean.calculate(guess);

                        // Call outputGame to output previous guesses and results
                        // and, if relevant, declare success, failure or output input fields
                        outputGame(out, theBean, graphics, success, session);
                    }
                }
                out.println("<input type=submit value=\"New Game\" name=action>");
                out.println("<input type=submit value=\"Instructions\" name=action>");

                if (graphics)
                {
                    out.println("<input type=\"checkbox\" checked value=\"on\" name=\"graphics\" id=\"graphics\"><label for=\"graphics\">Display game graphically</label>");
                } else
                {
                    out.println("<input type=\"checkbox\" value=\"on\" name=\"graphics\" id=\"graphics\"><label for=\"graphics\">Display game graphically</label>");
                }
                out.println("</form>");
            } else
            // action is instructions or there is no action
            {
                // if there is an HttpSession, invalidate it
                HttpSession session = request.getSession(false);
                if (session!=null)
                {
                    // Get our reference to the bean that we've stored in the HttpSession and Remove it
                    theBean = (MasterMindGame) session.getAttribute(ejbRef);
                    if(theBean != null)
                    { 
                        theBean.remove();
                    }
                    session.invalidate();
                }
                outputRules(out);
            }

            // End of output
            out.println("</td>");
            out.println("</tr>");
            out.println("</table>");

            out.println("</body>");
            out.println("</html>");            
        } catch (Throwable theException)
        {
            // uncomment the following line when unexpected exceptions
            // are occuring to aid in debugging the problem.
            theException.printStackTrace();
        }
    }

    /**
     * Writes the previous guesses and input fields for the next guess.
     * 
     * @param out java.io.PrintWriter
     */
    private void outputGame(PrintWriter out, MasterMindGame theBean, boolean graphics, boolean success, HttpSession session) 
    {
        try
        {
            int guessNumber = theBean.getGuessNumber();
            int [][] guessArray = theBean.getGuessArray();
            int [][] resultsArray = theBean.getResultsArray();
            int [] target = theBean.getTarget();


            out.println("<table border cellpadding=5 cellspacing=0>");
            out.println("<caption>MasterMind Game</caption>");
            out.println("<th id=\"t1\" colspan=1>Guess</th>");
            out.println("<th id=\"t2\" colspan=1>A</th>");
            out.println("<th id=\"t3\" colspan=1>B</th>");
            out.println("<th id=\"t4\" colspan=1>C</th>");
            out.println("<th id=\"t5\" colspan=1>D</th>");
            out.println("<th id=\"t6\" colspan=1>Only Color Correct</th>");
            out.println("<th id=\"t7\" colspan=1>Color and Place Correct</th>");
            for ( int guess=0; guess < guessNumber; guess++)
            {
                out.println("<tr>");
                out.println("<td headers=\"t1\" >Guess number "+(guess+1)+"</td>");
                for ( int element=0; element<MasterMindGameBean.NUMBER_OF_ELEMENTS; element++)
                {
                    if (graphics)
                    {
                        out.println("<td headers=\"t"+(element+2)+"\" align=center width=\"10%\"><img src=\"images/"+imageMap[guessArray[element][guess]]+".gif\" alt=\""+colourArray[guessArray[element][guess]]+"\"></td>");
                    } else
                    {
                        out.println("<td headers=\"t"+(element+2)+"\" align=center width=\"10%\">"+colourArray[guessArray[element][guess]]+"</td>");
                    }
                }
                out.println("<td headers=\"t6\" align=center>"+(resultsArray[0][guess]-resultsArray[1][guess])+"</td>");
                out.println("<td headers=\"t7\" align=center>"+resultsArray[1][guess]+"</td>");
                out.println("</tr>");
            }

            out.println("<form>");
            if (success)
            {
                out.println("</table>");
                out.println("<br>");
                out.println("<h2>Congratulations, you have cracked the code</h2>");
                session.invalidate();
            } else if (guessNumber >= MasterMindGameBean.MAX_GUESSES)
            {
                out.println("<tr>");
                out.println("<td headers=\"t1\">The code was</td>");
                for ( int element=0; element < MasterMindGameBean.NUMBER_OF_ELEMENTS; element++)
                {
                    if (graphics)
                    {
                        out.println("<td headers=\"t"+(element+2)+"\" align=center width=\"10%\"><img src=\"images/"+imageMap[target[element]]+".gif\" alt=\""+colourArray[target[element]]+"\"></td>");
                    } else
                    {
                        out.println("<td headers=\"t"+(element+2)+"\" align=center width=\"10%\">"+colourArray[target[element]]+"</td>");
                    }
                }
                out.println("</tr>");
                out.println("</table>");
                out.println("<br>");
                out.println("<h2>Bad luck, you have run out of guesses</h2>");
                session.invalidate();
            } else
            {
                out.println("<tr>");
                out.println("<td headers=\"t1\">Next</td>");

                for (int element=0; element < MasterMindGameBean.NUMBER_OF_ELEMENTS; element++)
                {
                    out.println("<td headers=\"t"+(element+2)+"\">");
                    out.println("<label for=\"element"+element+"\"></label>"); 
                    out.println("<select id=\"element"+element+"\" name=\"element"+element+"\">");
                    for (int i=0; i < MasterMindGameBean.NUMBER_OF_COLORS; i++)
                    {
                        if (guessNumber > 0 && guessArray[element][guessNumber-1]==i)
                        {
                            out.println("<option selected value=\""+i+"\">"+colourArray[i]);
                        } else
                        {
                            out.println("<option value=\""+i+"\">"+colourArray[i]);
                        }
                    }
                    out.println("</select>");
                    out.println("</td>");
                }
                out.println("</tr>");
                out.println("</table>");
                out.println("<input type=submit value=\"Guess\" name=action>");
            }
        } catch ( java.rmi.RemoteException e )
        {
        }
    }

    /**
     * Outputs the standard header for the HTML.
     * 
     * @param out java.io.PrintWriter
     */
    private void outputHeader(PrintWriter out)
    {
        out.println("<html>");
        out.println("<HEAD>");
        out.println("<TITLE>ActivitySessions Sample - MasterMind Game</TITLE>");
        out.println("</HEAD>");

        out.println("<BODY leftmargin=0 topmargin=0 marginwidth=\"0\" marginheight=\"0\">");
        out.println("<table cellpadding=5 cellspacing=5>");
        out.println("<tr>");
        out.println("<td>");
        out.println("<BR>");
        out.println("<H1 class=\"sampjsp\">ActivitySessions Sample - MasterMind Game</H1>");
        out.println("</td>");
        out.println("</tr>");
        out.println("</table>");

        out.println("<HR width=100%>"); 

        out.println("<table cellpadding=5 cellspacing=5>");
        out.println("<tr>");
        out.println("<td>");
    }
    /**
     * Outputs the rules of the game.
     * 
     * @param out java.io.PrintWriter
     */
    private void outputRules(PrintWriter out) 
    {
        out.println("<h2>Welcome to MasterMind</h2>");
        out.println("<p>The object of the game is to guess the four element code generated at the start of the game.");
        out.println("Each time you make a guess the computer responds by telling you how many elements of the target code");
        out.println("your guess contained in the wrong place, and how many your guess contained in the right place - though not which elements these are. ");
        out.println("The game ends when you have successfully guessed the code, or you have made ten incorrect guesses.");  
        out.println("<form>");
        out.println("<br><br><input type=submit value=\"New Game\" name=action>");
        out.println("<input type=\"checkbox\" checked value=\"on\" name=\"graphics\" id=\"graphics\"><label for=\"graphics\">Display game graphically</label>");
        out.println("</form>");
    }
}
