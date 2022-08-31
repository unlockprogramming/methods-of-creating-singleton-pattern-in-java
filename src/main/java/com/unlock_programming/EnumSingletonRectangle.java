package com.unlock_programming;

public enum EnumSingletonRectangle {
    INSTANCE;

    private Double length;
    private Double width;

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }
    public Double findArea() {
        return length * width;
    }

    public Double findPerimeter() {
        return 2*(length+ width);
    }
}
