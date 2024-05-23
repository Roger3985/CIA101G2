package com.photoWrite;

import java.sql.*;
import java.io.*;

class PhotoWrite {

    public static void main(String argv[]) {
        Connection con = null;
        PreparedStatement pstmt = null;
        InputStream fin = null;
        String url = "jdbc:mysql://localhost:3306/fallelove?serverTimezone=Asia/Taipei";
        String userid = "root";
        String passwd = "CIA101G2fallelove";
        String photoAdministrator = "src/main/resources/static/photo/administrator";
        String photoProduct = "src/main/resources/static/photo/product";
        String updateAdministrator = "update Administrator set admPhoto =? where admNo=?";
        String updateProductPic = "update ProductPicture set productPic =? where productPicNo=?";

        int countAdms = 1;
        try {
            con = DriverManager.getConnection(url, userid, passwd);
            pstmt = con.prepareStatement(updateAdministrator);
            File[] photoFiles = new File(photoAdministrator).listFiles();
            for (File f : photoFiles) {
                fin = new FileInputStream(f);
                pstmt = con.prepareStatement(updateAdministrator);
                pstmt.setInt(2, countAdms);
                pstmt.setBinaryStream(1, fin);
                pstmt.executeUpdate();
                countAdms++;
                System.out.print(" update the database...");
                System.out.println(f.toString());
            }

            fin.close();
            pstmt.close();
            System.out.println("加入圖片-更新成功.........");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

//        int countProds = 1;
//        try {
//            con = DriverManager.getConnection(url, userid, passwd);
//            pstmt = con.prepareStatement(updateProductPic);
//            File[] photoFiles = new File(photoProduct).listFiles();
//            for (File f : photoFiles) {
//                fin = new FileInputStream(f);
//                pstmt = con.prepareStatement(updateProductPic);
//                pstmt.setInt(2, countProds);
//                pstmt.setBinaryStream(1, fin);
//                pstmt.executeUpdate();
//                countProds++;
//                System.out.print(" update the database...");
//                System.out.println(f.toString());
//            }
//
//            fin.close();
//            pstmt.close();
//            System.out.println("加入圖片-更新成功.........");
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                con.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
