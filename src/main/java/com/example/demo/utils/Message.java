package com.example.demo.utils;

// 用于创建统一格式的 ResponseBody
// {
//   status: "",
//   message: ""
// }
public class Message {
    private String message;
    private String status;

    public Message(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
