package no.rustelefonen.hapserver.converters;

import no.rustelefonen.hapserver.entities.ResearchUser;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Created by simenfonnes on 11.05.2016.
 */
@FacesConverter("GenderConverter")
public class GenderConverter implements Converter {


    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return ResearchUser.Gender.valueOf(s);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        ResearchUser.Gender g = (ResearchUser.Gender) o;
        return g == ResearchUser.Gender.MALE ? "Mann"
                : g == ResearchUser.Gender.FEMALE ? "Kvinne"
                : g == ResearchUser.Gender.OTHER ? "Annet"
                : "Ikke spesifisert";
    }
}
