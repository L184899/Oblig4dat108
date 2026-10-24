package no.hvl.dat108.oblig4.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import no.hvl.dat108.oblig4.model.Deltager;


public class LogginnService {


    public static boolean erBrukerLoggetInn(HttpServletRequest request) {

        HttpSession sesjon = request.getSession(false);   // false = ikke lag ny
        return sesjon != null && sesjon.getAttribute("innlogget") != null;
    }


    public static void loggUtBruker(HttpServletRequest request) {

        HttpSession sesjon = request.getSession(false);
        if (sesjon != null) {
            sesjon.invalidate();
        }
    }


    public static void loggInnBruker(HttpServletRequest request, Deltager deltager) {

        loggUtBruker(request);                            // ny sesjon ved innlogging
        HttpSession sesjon = request.getSession(true);
        sesjon.setAttribute("innlogget", deltager);
    }


    public static Deltager hentInnloggetBruker(HttpServletRequest request) {

        HttpSession sesjon = request.getSession(false);
        return (sesjon == null) ? null : (Deltager) sesjon.getAttribute("innlogget");
    }
}
