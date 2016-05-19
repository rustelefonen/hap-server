package no.rustelefonen.hapserver.controllers;

import no.rustelefonen.hapserver.ejb.InfoEjb;
import no.rustelefonen.hapserver.entities.Category;
import no.rustelefonen.hapserver.entities.Info;
import org.apache.commons.codec.binary.Base64;
import org.imgscalr.Scalr;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

import static no.rustelefonen.hapserver.controllers.InfoController.StatusCode.*;

/**
 * Created by simenfonnes on 14.04.2016.
 * Refacced by Fredrik Loberg
 */
@Named
@ViewScoped
public class InfoController implements Serializable {
    public enum StatusCode {ADDED, EDITED, DELETED}
    public static final int IMAGE_MAX_WIDTH = 500;

    private int infoId;
    private int categoryId;
    private Info info;

    private List<Category> categoryList;
    private List<Info> infoList;
    private StatusCode statusCode;

    @Inject
    private InfoEjb infoEjb;

    @PostConstruct
    public void construct() {
        info = new Info();
    }

    public void loadAllInfos() {
        infoList = infoEjb.getAllInfos();
    }

    public void loadAllCategories() {
        categoryList = infoEjb.getAllCateogires();
    }

    public void initInfoyById() {
        info = infoEjb.getInfoById(infoId);
    }

    public void setCurrentCategoryId(){
        if(info == null) return;

        Category category = info.getCategory();
        if (category != null) categoryId = category.getId();
    }

    public String addInfo() {
        FacesContext fc = FacesContext.getCurrentInstance();

        Category category = infoEjb.getCategoryById(categoryId);
        if(category == null){
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Du må velge en gyldig kategori.", ""));
            return null;
        }
        else if(infoEjb.infoTitleExists(info.getTitle())) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tittelen er allerede i bruk.", ""));
            return null;
        }

        infoEjb.persist(info, category);
        return "/info/show_infos.jsf?faces-redirect=true&statusCode=" + ADDED;

    }

    public String updateInfo() {
        Info infoInDb = infoEjb.getInfoById(info.getId());

        if(anotherInfoWithSameTitleExists(info, infoInDb)){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tittelen er allerede i bruk.", ""));
            return null;
        }
        else {
            infoEjb.update(info, categoryId);
            return "/info/show_infos.jsf?faces-redirect=true&statusCode=" + EDITED;
        }
    }

    public void deleteInfo(int id) {
        infoEjb.deleteInfoById(id);
        infoList.removeIf(i -> i.getId() == id);

        statusCode = DELETED;
        addFacesMessage();
    }

    public void uploadImage(FileUploadEvent event){
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            Map<String, String> requestMap = ec.getRequestParameterMap();
            String key = requestMap.keySet().stream().filter(s -> s.contains("editor_input")).findFirst().orElse("");
            String value = requestMap.get(key);

            String filepath = resizeImgAndEncodeBase64(event.getFile());
            info.setHtmlContent(value + "<br/><img src=\"data:image/png;base64," + filepath + "\" width=\"300\" />");
            RequestContext.getCurrentInstance().update("infoform:editor");
        } catch (IOException ignored) {}
    }

    private String resizeImgAndEncodeBase64(UploadedFile file) throws IOException {
        BufferedImage img = ImageIO.read(file.getInputstream());
        if(img.getWidth() > IMAGE_MAX_WIDTH) img = Scalr.resize(img, IMAGE_MAX_WIDTH, img.getHeight());

        String[] fileSplit = file.getFileName().split("\\.");
        String fileType = fileSplit.length > 0 ? fileSplit[fileSplit.length-1] : "jpg";

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ImageIO.write(img, fileType, buffer);

        return Base64.encodeBase64String(buffer.toByteArray());
    }

    private String saveFileToDisk(UploadedFile uploadedFile) throws IOException {
        String realPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/hap/uploads");
        Path folder = Paths.get(realPath);
        folder.toFile().mkdirs();

        String[] filenameParts = uploadedFile.getFileName().split("\\.");
        String filename = filenameParts.length > 0 ? filenameParts[0] : "NO-NAME";
        String fileExstension = filenameParts.length > 1 ? filenameParts[1] : ".jpg";

        Path file = Files.createTempFile(folder, filename, "." + fileExstension);

        try (InputStream input = uploadedFile.getInputstream()) {
            Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
        }

        return file.getFileName().toString();
    }

    public void addFacesMessage() {
        FacesContext fc = FacesContext.getCurrentInstance();
        String statusMsg = statusCode == ADDED ? "Informasjonssiden ble lagt til."
                : statusCode == EDITED ? "Informasjonssiden ble redigert."
                : statusCode == DELETED ? "Informasjonssiden ble slettet."
                : null;

        if (statusMsg != null) fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", statusMsg));
    }

    private boolean anotherInfoWithSameTitleExists(Info info, Info originalFromDb){
        return !originalFromDb.getTitle().equals(info.getTitle()) &&
                infoEjb.infoTitleExists(info.getTitle());
    }

//    GETTERS AND SETTERS
    public List<Info> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<Info> infoList) {
        this.infoList = infoList;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public int getInfoId() {
        return infoId;
    }

    public void setInfoId(int infoId) {
        this.infoId = infoId;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
