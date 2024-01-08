// UserResponse.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.example.pulopo.model.response;
import java.util.List;

public class UserResponse {
    private Data data;
    private boolean success;
    private String message;

    @Override
    public String toString() {
        return "UserResponse{" +
                "data=" + data +
                ", success=" + success +
                ", message='" + message + '\'' +
                '}';
    }

    public Data getData() { return data; }
    public void setData(Data value) { this.data = value; }

    public boolean getSuccess() { return success; }
    public void setSuccess(boolean value) { this.success = value; }

    public String getMessage() { return message; }
    public void setMessage(String value) { this.message = value; }
}

// Data.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

class Data {
    private String accessToken;
    private String refreshToken;


    @Override
    public String toString() {
        return "Data{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String value) { this.accessToken = value; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String value) { this.refreshToken = value; }
}
