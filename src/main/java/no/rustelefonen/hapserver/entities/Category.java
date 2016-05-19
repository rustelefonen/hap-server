package no.rustelefonen.hapserver.entities;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static no.rustelefonen.hapserver.entities.Category.SEQUENCE_NAME;

/**
 * Created by lon on 05/04/16.
 */
@SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, initialValue = 50)
@NamedQueries({
        @NamedQuery(name = Category.GET_ALL, query = "select c from Category c ORDER BY c.orderNumber ASC"),
        @NamedQuery(name = Category.GET_BY_TITLE, query = "select c from Category c where c.title = :"+Category.TITLE_PARAM),
        @NamedQuery(name = Category.GET_BY_ORDER_NUMBER, query = "select c from Category c where c.orderNumber = :"+Category.ORDER_PARAM)
})
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Category {
    public static final String GET_ALL = "Category.getAll";
    public static final String GET_BY_TITLE = "Category.getByTitle";
    public static final String TITLE_PARAM = "Category_titleParam";
    public static final String GET_BY_ORDER_NUMBER = "Category.getByOrder";
    public static final String ORDER_PARAM = "Category_orderParam";
    public static final String SEQUENCE_NAME = "Category_Sequence";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private int id;

    @NotNull
    @Size(min = 1, max = 256, message = "Tittel må være mellom ett og 256 tegn")
    private String title;

    @NotNull(message = "Sorteringsnummer kan ikke være tom")
    @Min(value = 1, message = "Sorteringsnummer kan ikke være mindre enn 1")
    private int orderNumber;

    @NotNull
    @Min(value = 1)
    private int versionNumber;

    @XmlElementWrapper(name = "infoList")
    @XmlElement(name = "info")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "category", cascade = CascadeType.ALL)
    private List<Info> infoList;

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

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int order) {
        this.orderNumber = order;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public List<Info> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<Info> infoList) {
        this.infoList = infoList;
    }

    @XmlRootElement
    @XmlSeeAlso(Category.class)
    public static class CategoryList extends ArrayList<Category> {

        public CategoryList() {
            super();
        }

        public CategoryList(Collection<? extends Category> c) {
            super(c);
        }

        @XmlElement(name = "Category")
        public List<Category> getCategories() {
            return this;
        }

        public void setCategories(List<Category> categories) {
            this.addAll(categories);
        }
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Summary {
        int id;
        int versionNumber;

        public Summary() {}

        public Summary(int id, int versionNumber){
            this.id = id;
            this.versionNumber = versionNumber;
        }
    }

    @XmlRootElement(name = "CategorySummaries")
    @XmlSeeAlso(Summary.class)
    public static class SummaryList extends ArrayList<Summary> {

        public SummaryList() {
            super();
        }

        public SummaryList(Collection<? extends Summary> c) {
            super(c);
        }

        @XmlElement(name = "CategorySummary")
        public List<Summary> getSummaries() {
            return this;
        }
        public void setSummaries(List<Summary> summaries) {
            this.addAll(summaries);
        }
    }
}
