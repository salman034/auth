package com.auth.auth.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseBean {

    private String status;

    private String message;

    private Object data;

    private String mdcToken;

    public ResponseBean(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}
