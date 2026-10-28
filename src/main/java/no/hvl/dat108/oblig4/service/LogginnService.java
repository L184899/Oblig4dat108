package no.hvl.dat108.oblig4.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import no.hvl.dat108.oblig4.model.Deltager;
import org.springframework.stereotype.Service;

@Service
public class LogginnService {


    public boolean erBrukerLoggetInn(HttpServletRequest request) {

        HttpSession sesjon = request.getSession(false);
        return sesjon != null && sesjon.getAttribute("innlogget") != null;
    }

    public void loggUtBruker(HttpServletRequest request) {

        HttpSession sesjon = request.getSession(false);
        if (sesjon != null) {
            sesjon.invalidate();
        }
    }

    public void loggInnBruker(HttpServletRequest request, Deltager deltager) {

        loggUtBruker(request);
        HttpSession sesjon = request.getSession(true);
        sesjon.setAttribute("innlogget", deltager);
    }


    public Deltager hentInnloggetBruker(HttpServletRequest request) {

        HttpSession sesjon = request.getSession(false);
        return (sesjon == null) ? null : (Deltager) sesjon.getAttribute("innlogget");
    }
}
