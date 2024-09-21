package com.app.cv.common.classes;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import com.app.cv.model.SuccessResponse;

public class Common {
 
    private Common() {}

    public static LocalDateTime  getCurrentTime() {
        return  LocalDateTime.now();
    }

    public static SuccessResponse getSuccessResponse(String message, Object  data) {
        SuccessResponse response = new SuccessResponse();
        response.setMessage(message);
        response.setData(data);
        response.setTimestamp(OffsetDateTime.now());

        return response;
    }
}
