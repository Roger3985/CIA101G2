package com.ren.administrator.dao;

import com.Entity.Administrator;
import com.Entity.Title;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdministratorJDBCDAOImpl implements AdministratorDAO_interface {

    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/fallelove?serverTimezone=Asia/Taipei";
    String userid = "root";
    String passwd = "SaiKou97607";

    // 新增管理員(不包括圖片)
    private static final String INSERT_STMT =
            "INSERT INTO Administrator (admPwd,admName,admStat,admEmail,titleNo,admHireDate) VALUES (?, ?, ?, ?, ?, ?)";
    // 查詢單一管理員
    // 因密碼不應顯示於前端，查詢功能不提供密碼查詢
    private static final String GET_ONE_STMT =
            "SELECT admNo,admName,admStat,admEmail,titleNo,admHireDate, admPhoto FROM Administrator WHERE admNo = ?";
    private static final String GET_NAME_STMT =
            "SELECT admNo,admName,admStat,admEmail,titleNo,admHireDate FROM Administrator WHERE admName = ?";
    private static final String GET_EMAIL_STMT =
            "SELECT admNo,admName,admStat,admEmail,titleNo,admHireDate FROM Administrator WHERE admEmail = ?";
    // 查詢全部
    // 因密碼不應顯示於前端，查詢功能不提供密碼查詢
    private static final String GET_ALL_STMT =
            "SELECT admNo,admName,admStat,admEmail,titleNo,admHireDate,admPhoto FROM Administrator ORDER BY admNo";
    // 修改資料
    private static final String UPDATE_STMT =
            "UPDATE Administrator SET admPwd=?, admName=?, admStat=?, admEmail=?, titleNo=?, admHireDate=?, admPhoto WHERE admNo = ?";
    // 刪除會員
    private static final String DELETE_ADMINISTRATOR_STMT =
            "DELETE FROM Administrator WHERE admNo = ?";
    // 上傳圖片
    private static final String UPLOAD_STMT =
            "UPDATE Administrator SET admPhoto=? WHERE admNo = ?";
    // 顯示大頭貼
    private static final String PHOTO_DISPLAY_STMT =
            "SELECT admPhoto FROM Administrator WHERE admNo = ?";
    // 修改圖片
    private static final String CHANGE_PHOTO_STMT =
            "UPDATE Administrator SET admPhoto=? WHERE admNo = ?";


    @Override
    public void insert(Administrator administrator) {
        Title title = administrator.getTitle();
        try (Connection con = DriverManager.getConnection(url, userid, passwd);
             PreparedStatement ps = con.prepareStatement(INSERT_STMT)) {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            // 從request的VO取值放入PreparedStatement
            ps.setString(1, administrator.getAdmPwd());
            ps.setString(2, administrator.getAdmName());
            ps.setByte(3, administrator.getAdmStat());
            ps.setString(4, administrator.getAdmEmail());
            ps.setInt(5, title.getTitleNo());
            ps.setDate(6, administrator.getAdmHireDate());
            // 執行SQL指令將VO資料新增進資料庫
            ps.executeUpdate();
            // Handle any driver errors
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
            // Handle any SQL errors
        } catch (SQLException se) {
            throw new RuntimeException("A database error occured. " + se.getMessage());
        }
    }

    @Override
    public Administrator findByPrimaryKey(Integer admNo) {
        // 宣告VO並指定空值，若查詢無結果會出現空值，後續於Controller作錯誤處理
        Administrator administrator = null;
        Title title = administrator.getTitle();
        // ResultSet在相關的Statement關閉時會自動關閉，因此不用另外寫在Auto-closable
        try (Connection con = DriverManager.getConnection(url, userid, passwd);
             PreparedStatement ps = con.prepareStatement(GET_ONE_STMT)) {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            // 將request的商品編號放入SQL
            ps.setInt(1, admNo);
            // 執行SQL查詢並得到ResultSet物件
            ResultSet rs = ps.executeQuery();
            // 取出ResultSet內資料放入VO
            while (rs.next()) {
                administrator = new Administrator();
                administrator.setAdmNo(rs.getInt("AdmNo"));
                administrator.setAdmName(rs.getString("AdmName"));
                administrator.setAdmStat(rs.getByte("AdmStat"));
                administrator.setAdmEmail(rs.getString("AdmEmail"));
                title.setTitleNo(rs.getInt("TitleNo"));
                administrator.setAdmHireDate(rs.getDate("AdmHireDate"));
                administrator.setAdmPhoto(rs.getBytes("admPhoto"));
            }
            // Handle any driver errors
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
            // Handle any SQL errors
        } catch (SQLException se) {
            throw new RuntimeException("A database error occured. " + se.getMessage());
        }
        // 回傳VO，待後續Controller導至View呈現
        return administrator;
    }

    @Override
    public List<Administrator> getAll() {
        // 宣告ArrayList作為放入搜尋結果的列表
        List<Administrator> list = new ArrayList<>();
        // 宣告VO並指定空值，若查詢無結果會出現空值，後續於Controller作錯誤處理
        Administrator administrator = null;
        Title title = administrator.getTitle();
        try (Connection con = DriverManager.getConnection(url, userid, passwd);
             PreparedStatement ps = con.prepareStatement(GET_ALL_STMT)) {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            // 執行SQL查詢並得到ResultSet物件
            ResultSet rs = ps.executeQuery();
            // 新增VO物件，取出ResultSet內資料放入VO
            while (rs.next()) {
                administrator = new Administrator();
                administrator.setAdmNo(rs.getInt("AdmNo"));
                administrator.setAdmName(rs.getString("AdmName"));
                administrator.setAdmStat(rs.getByte("AdmStat"));
                administrator.setAdmEmail(rs.getString("AdmEmail"));
                title.setTitleNo(rs.getInt("TitleNo"));
                administrator.setAdmHireDate(rs.getDate("AdmHireDate"));
                administrator.setAdmPhoto(rs.getBytes("admPhoto"));
                list.add(administrator); // 將資料新增至列表內之後作為搜尋結果返回給View
            }
            // Handle any driver errors
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
            // Handle any SQL errors
        } catch (SQLException se) {
            throw new RuntimeException("A database error occured. " + se.getMessage());
        }
        return list;
    }

    @Override
    public void update(Administrator administrator) {
        Title title = administrator.getTitle();
        try (Connection con = DriverManager.getConnection(url, userid, passwd);
             PreparedStatement ps = con.prepareStatement(UPDATE_STMT)) {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            // 從request的VO取值放入PreparedStatement
            ps.setString(1, administrator.getAdmPwd());
            ps.setString(2, administrator.getAdmName());
            ps.setByte(3, administrator.getAdmStat());
            ps.setString(4, administrator.getAdmEmail());
            ps.setInt(5, title.getTitleNo());
            ps.setDate(6, administrator.getAdmHireDate());
            ps.setBytes(7, administrator.getAdmPhoto());
            ps.setInt(8, administrator.getAdmNo());
            // 執行SQL指令將資料庫內對應的資料修改成VO的值
            ps.executeUpdate();
            // Handle any driver errors
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
            // Handle any SQL errors
        } catch (SQLException se) {
            throw new RuntimeException("A database error occured. " + se.getMessage());
        }
    }

    @Override
    public void delete(Integer admNo) {

        Connection con = null;
        PreparedStatement ps = null;

        try {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            con = DriverManager.getConnection(url, userid, passwd);
            // 刪除管理員
            ps = con.prepareStatement(DELETE_ADMINISTRATOR_STMT);
            ps.setInt(1, admNo);
            ps.executeUpdate();
            // Handle any driver errors
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load database driver. "
                    + e.getMessage());
            // Handle any SQL errors
        } catch (SQLException se) {
            throw new RuntimeException("A database error occured. "
                    + se.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException se) {
                    se.printStackTrace(System.err);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }

    @Override
    public void upload(Integer admNo, byte[] admPhoto) {
        try (Connection con = DriverManager.getConnection(url, userid, passwd);
             PreparedStatement ps = con.prepareStatement(UPLOAD_STMT)) {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            ps.setBytes(1, admPhoto);
            ps.setInt(2, admNo);
            // 執行SQL指令
            ps.executeUpdate();
            // Handle any driver errors
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
            // Handle any SQL errors
        } catch (SQLException se) {
            throw new RuntimeException("A database error occured. " + se.getMessage());
        }
    }

    @Override
    public byte[] photoSticker(Integer admNo) {
        // 宣告VO並指定空值，若查詢無結果會出現空值，後續於Controller作錯誤處理
        byte[] admPhoto = null;
        // ResultSet在相關的Statement關閉時會自動關閉，因此不用另外寫在Auto-closable
        try (Connection con = DriverManager.getConnection(url, userid, passwd);
             PreparedStatement ps = con.prepareStatement(PHOTO_DISPLAY_STMT)) {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            // 將request的商品編號放入SQL
            ps.setInt(1, admNo);
            // 執行SQL查詢並得到ResultSet物件
            ResultSet rs = ps.executeQuery();
            // 取出ResultSet內資料放入VO
            while (rs.next()) {
                admPhoto = rs.getBytes("admPhoto");
            }
            // Handle any driver errors
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
            // Handle any SQL errors
        } catch (SQLException se) {
            throw new RuntimeException("A database error occured. " + se.getMessage());
        }
        return admPhoto;
    }

    @Override
    public void ChangePhoto(Integer admNo, byte[] admPhoto) {
        try (Connection con = DriverManager.getConnection(url, userid, passwd);
             PreparedStatement ps = con.prepareStatement(CHANGE_PHOTO_STMT)) {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            // 從request的VO取值放入PreparedStatement
            ps.setBytes(1, admPhoto);
            ps.setInt(2, admNo);
            // 執行SQL指令將資料庫內對應的資料修改成VO的值
            ps.executeUpdate();
            // Handle any driver errors
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
            // Handle any SQL errors
        } catch (SQLException se) {
            throw new RuntimeException("A database error occured. " + se.getMessage());
        }
    }

    @Override
    public List findExistData(String admName, String admEmail) {

        List<String> existData = new LinkedList<>();

        Connection con = null;
        PreparedStatement ps = null;

        try {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            con = DriverManager.getConnection(url, userid, passwd);
            // 刪除管理員
            ps = con.prepareStatement(GET_NAME_STMT);
            ps.setString(1, admName);
            ResultSet rsName = ps.executeQuery();

            if (rsName.next()) {
                existData.add("已存在用戶名:" + admName);
            }

            ps = con.prepareStatement(GET_EMAIL_STMT);
            ps.setString(1, admEmail);
            ResultSet rsEmail = ps.executeQuery();

            if (rsEmail.next()) {
                existData.add("已使用信箱:" + admEmail);
            }
            // Handle any driver errors
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load database driver. "
                    + e.getMessage());
            // Handle any SQL errors
        } catch (SQLException se) {
            throw new RuntimeException("A database error occured. "
                    + se.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException se) {
                    se.printStackTrace(System.err);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }

        }
        return existData;
    }

}
