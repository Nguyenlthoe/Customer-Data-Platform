package bk.edu.data.response.base;


import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MyResponse {

    @Expose
    private final int code;
    @Expose
    private final UUID requestId = UUID.randomUUID();
    @Expose
    private final String message;
    @Expose
    private final Object data;

    public static BaseResponseBuilder builder() {
        return new BaseResponseBuilder();
    }

    private MyResponse(BaseResponseBuilder builder) {
        this.code = builder.code;
        this.message = builder.message;
        this.data = builder.data;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class BaseResponseBuilder {

        private int code = -1;
        private String message = "default message";
        private Object data = null;

        public BaseResponseBuilder buildCode(int code) {
            this.code = code;
            return this;
        }

        public BaseResponseBuilder buildMessage(String message) {
            this.message = message;
            return this;
        }

        public BaseResponseBuilder buildData(Object data) {
            this.data = data;
            return this;
        }

        public MyResponse get() {
            return new MyResponse(this);
        }

    }
}
