package com.app.cv.common;
import java.time.OffsetDateTime;

import com.app.cv.model.SuccessResponse;

public class Common {
 
    private Common() {}

    public static OffsetDateTime  getCurrentTime() {
        return  OffsetDateTime.now();
    }

    public static SuccessResponse getSuccessResponse(String message, Object  data) {
        SuccessResponse response = new SuccessResponse();
        response.setMessage(message);
        response.setData(data);
        response.setTimestamp(Common.getCurrentTime());

        return response;
    }
}
