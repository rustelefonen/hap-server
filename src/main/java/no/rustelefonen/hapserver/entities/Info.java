package no.rustelefonen.hapserver.entities;

import no.rustelefonen.hapserver.entities.constraints.MaxByteSize;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by lon on 05/04/16.
 */
@NamedQueries({
        @NamedQuery(name = Info.GET_ALL, query = "SELECT i FROM Info i order by i.category.orderNumber, i.title"),
        @NamedQuery(name = Info.GET_BY_TITLE, query = "select i from Info i where i.title = :"+Info.TITLE_PARAM)
})
@SequenceGenerator(name = Info.SEQUENCE_NAME, sequenceName = Info.SEQUENCE_NAME, initialValue = 50)
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Info {
    public static final String GET_ALL = "Info.getAll";
    public static final String GET_BY_TITLE = "Info.getByTitle";
    public static final String TITLE_PARAM = "Info_titleParam";
    public static final String SEQUENCE_NAME = "Info_Sequence";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private int id;

    @NotNull
    @Size(min = 1, max = 256, message = "Tittel må være mellom ett og 256 tegn langt.")
    private String title;

    @NotNull
    @Lob
    @MaxByteSize(value = 2050000, message = "Innholdet er for stort! (Tips: bilder tar mye plass)")
    @Size(min = 1, message = "Innholdet må være minst ett tegn.")
    private String htmlContent;

    @XmlTransient
    @NotNull
    @ManyToOne
    private Category category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
