package no.rustelefonen.hapserver.controllers;

import no.rustelefonen.hapserver.ejb.UserEJB;
import no.rustelefonen.hapserver.entities.User;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Simen Fonnes on 31.03.2016.
 * Refacced by Fredrik Loberg
 */
@Named
@SessionScoped
public class UserController implements Serializable {

    @Inject
    private UserEJB userEJB;

    private User user;
    private String password;
    private boolean isLoggedIn;

    @PostConstruct
    public void init() {
        user = new User();
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void logOut(){
        isLoggedIn = false;
    }

    public String logIn() {
        isLoggedIn = userEJB.login(user, password);

        if (isLoggedIn) return "/category/show_categories.jsf?faces-redirect=true";

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Feil brukernavn/passord!", null));
        return null;
    }

    public void redirectIfNotLoggedIn() throws IOException {
        if (isLoggedIn) return;
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc.getViewRoot().getViewId().equals("/log_in.xhtml")) return; //excluding log in page from redirect

        ExternalContext ec = fc.getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/log_in.jsf");
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}