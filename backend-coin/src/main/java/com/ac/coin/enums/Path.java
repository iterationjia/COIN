package com.ac.coin.enums;

public enum Path {
//    pythonPath("F:\\Anaconda3\\python.exe"),
//    JoinQuaintPath("C:\\Users\\ASUS\\Desktop\\SE3\\data\\joinQuant\\"),
//    StockPath("C:\\Users\\ASUS\\Desktop\\SE3\\Stock");
    pythonPath("D:\\anaconda3\\python.exe"),
    JoinQuaintPath("E:\\课程\\软件工程与计算III\\迭代三\\JoinQuant\\joinQuant\\"),
    StockPath("E:\\课程\\软件工程与计算III\\Stock\\Stock");

    private String value;

    Path(String value) {this.value = value;}

    public String getValue() {
        return value;
    }
}
