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
    private final String msg;
    @Expose
    private final Object data;

    public static BaseResponseBuilder builder() {
        return new BaseResponseBuilder();
    }

    private MyResponse(BaseResponseBuilder builder) {
        this.code = builder.code;
        this.msg= builder.msg;
        this.data = builder.data;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", msg='" + msg+ '\'' +
                ", data=" + data +
                '}';
    }

    public static class BaseResponseBuilder {

        private int code = -1;
        private String msg = "default message";
        private Object data = null;

        public BaseResponseBuilder buildCode(int code) {
            this.code = code;
            return this;
        }

        public BaseResponseBuilder buildMessage(String message) {
            this.msg = message;
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
