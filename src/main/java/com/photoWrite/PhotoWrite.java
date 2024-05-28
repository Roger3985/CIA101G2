package com.photoWrite;

import java.sql.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class PhotoWrite {

    public static void main(String argv[]) {
        Connection con = null;
        PreparedStatement pstmt = null;
        InputStream fin = null;
        String url = "jdbc:mysql://localhost:3306/fallelove?serverTimezone=Asia/Taipei";
        String userid = "root";
        String passwd = "CIA101G2fallelove";
        String photoAdministrator = "src/main/resources/static/photo/administrator";
        String photoProductPic = "src/main/resources/static/photo/productPic";
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

        int countProds = 1;
        try {
            con = DriverManager.getConnection(url, userid, passwd);
            pstmt = con.prepareStatement(updateProductPic);

            File photoDir = new File(photoProductPic);
            List<File> photoFiles = new ArrayList<>();

            getAllFiles(photoDir, photoFiles);

            for (File f : photoFiles) {
                fin = new FileInputStream(f);
                pstmt.setBinaryStream(1, fin);
                pstmt.setInt(2, countProds);
                pstmt.executeUpdate();
                fin.close();
                countProds++;
                System.out.print("Updating the database...");
                System.out.println(f.toString());
            }

            pstmt.close();
            con.close();
            System.out.println("Successfully updated images...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getAllFiles(File dir, List<File> fileList) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    getAllFiles(file, fileList);
                } else {
                    fileList.add(file);
                }
            }
        }
    }

}
