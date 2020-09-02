package com.example.mybatis.mybatisdemo.util;

import lombok.Data;

/**
 * @author wangrenzhe
 * @date 2020/8/19 18:12
 */
@Data
public class ResultDTO<T> {
    private T result;
    private Boolean success;
    private String errorCode;
    private String errorMessage;

    public ResultDTO build(String errorCode, String errorMessage) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setErrorCode(errorCode);
        resultDTO.setErrorMessage(errorMessage);
        return resultDTO;
    }
}
