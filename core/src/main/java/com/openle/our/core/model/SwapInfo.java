package com.openle.our.core.model;

//  new SwapInfo.Builder(200, "text");
public class SwapInfo {

    private final int code;
    private final String text;

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    private SwapInfo(Builder builder) {
        this.code = builder.code;
        this.text = builder.text;
    }

    public static class Builder {

        private int code;
        private String text;

        public Builder(int code) {
            this.code = code;
        }

        public Builder(int code, String text) {
            this.code = code;
            this.text = text;
        }

        public SwapInfo build() {
            return new SwapInfo(this);
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
