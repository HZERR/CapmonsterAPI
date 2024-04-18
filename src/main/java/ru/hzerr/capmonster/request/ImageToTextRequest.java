package ru.hzerr.capmonster.request;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.StringUtils;

import java.util.Base64;

public class ImageToTextRequest {

    @SerializedName("type")
    private final String TYPE = "ImageToTextTask";

    @SerializedName("body")
    private String body;

    @SerializedName("CapMonsterModule")
    private String module;

    @SerializedName("recognizingThreshold")
    private Integer recognizingThreshold;
    @SerializedName("case")
    private Boolean caseSensitive;
    @SerializedName("numeric")
    private Boolean onlyNumeric;
    @SerializedName("math")
    private Boolean mathOperation;

    public ImageToTextRequest() {
    }

    public ImageToTextRequest(String captchaImageAsBase64String) {
        this.body = StringUtils.remove(captchaImageAsBase64String, "\n");
    }

    public ImageToTextRequest(byte[] captchaImageAsBytes) {
        this.body = Base64.getEncoder().encodeToString(captchaImageAsBytes);
    }

    public String getType() {
        return TYPE;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String captchaImageAsBase64String) {
        this.body = captchaImageAsBase64String;
    }

    public void setBody(byte[] captchaImageAsBytes) {
        this.body = Base64.getEncoder().encodeToString(captchaImageAsBytes);
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Integer getRecognizingThreshold() {
        return recognizingThreshold;
    }

    public void setRecognizingThreshold(Integer recognizingThreshold) {
        this.recognizingThreshold = recognizingThreshold;
    }

    public Boolean getCaseSensitive() {
        return caseSensitive;
    }

    public void enableCaseSensitive() {
        this.caseSensitive = Boolean.TRUE;
    }

    public Boolean getOnlyNumeric() {
        return onlyNumeric;
    }

    public void enableOnlyNumeric() {
        this.onlyNumeric = Boolean.TRUE;
    }

    public Boolean getMathOperation() {
        return mathOperation;
    }

    public void enableMathOperation() {
        this.mathOperation = Boolean.TRUE;
    }
}
