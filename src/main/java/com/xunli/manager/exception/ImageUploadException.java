package com.xunli.manager.exception;

import com.xunli.manager.enumeration.ReturnCode;

/**
 * Created by Betty on 2017/9/16.
 */
public class ImageUploadException extends Throwable{
    private int code;
    private String msg;
    public ImageUploadException(int code,String msg)
    {
        this.code = code;
        this.msg = msg;
    }

    public ReturnCode getReturnCode()
    {
        switch (code)
        {
            case 1:
                return ReturnCode.PUBLIC_UPLOAD_IMAGE_TOO_LARGE;
            case 2:
                return ReturnCode.PUBLIC_UPLOAD_IMAGE_FULL;
            case 3:
                return ReturnCode.PUBLIC_UPLOAD_IMAGE_PATTERN_ERROR;
        }
        return ReturnCode.SYSTEM_SYSTEM_ERROR;
    }
}
