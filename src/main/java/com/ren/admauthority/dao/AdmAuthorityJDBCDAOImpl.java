package com.ren.admauthority.dao;

import com.Entity.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdmAuthorityJDBCDAOImpl implements AdmAuthorityDAO_interface {

    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/fallelove?serverTimezone=Asia/Taipei";
    String userid = "root";
    String passwd = "SaiKou97607";

    // 新增商品
    private static final String INSERT_STMT =
            "INSERT INTO AdmAuthority (titleNo,authFuncNo) VALUES (?, ?)";
    // 查詢單一品項
    private static final String GET_ONE_STMT =
            "SELECT titleNo,authFuncNo FROM AdmAuthority WHERE titleNo = ?";
    // 查詢全部
    private static final String GET_ALL_STMT =
            "SELECT titleNo,authFuncNo FROM AdmAuthority ORDER BY titleNo";
    // 修改商品資料
    private static final String UPDATE_STMT =
            "UPDATE AdmAuthority SET authFuncNo=? WHERE titleNo = ?";
    // 刪除會員
    private static final String DELETE_ADMAUTHORITY_STMT =
            "DELETE FROM AdmAuthority WHERE titleNo = ?";

    @Override
    public void insert(AdmAuthority admAuthority) {
        Title title = admAuthority.getTitle();
        AuthorityFunction authorityFunction = admAuthority.getAuthorityFunction();

        try (Connection con = DriverManager.getConnection(url, userid, passwd);
             PreparedStatement ps = con.prepareStatement(INSERT_STMT)) {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            // 從request的VO取值放入PreparedStatement
            ps.setInt(1, title.getTitleNo());
            ps.setInt(2, authorityFunction.getAuthFuncNo());
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
    public AdmAuthority findByPrimaryKey(Integer titleNo) {
        // 宣告VO並指定空值，若查詢無結果會出現空值，後續於Controller作錯誤處理
        AdmAuthority admAuthority = null;
        Title title = admAuthority.getTitle();
        AuthorityFunction authorityFunction = admAuthority.getAuthorityFunction();
        // ResultSet在相關的Statement關閉時會自動關閉，因此不用另外寫在Auto-closable
        try (Connection con = DriverManager.getConnection(url, userid, passwd);
             PreparedStatement ps = con.prepareStatement(GET_ONE_STMT)) {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            // 將request的商品編號放入SQL
            ps.setInt(1, titleNo);
            // 執行SQL查詢並得到ResultSet物件
            ResultSet rs = ps.executeQuery();
            // 取出ResultSet內資料放入VO
            while (rs.next()) {
                admAuthority = new AdmAuthority();
                title.setTitleNo(rs.getInt("titleNo"));
                authorityFunction.setAuthFuncNo(rs.getInt("authFuncNo"));
            }
            // Handle any driver errors
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
            // Handle any SQL errors
        } catch (SQLException se) {
            throw new RuntimeException("A database error occured. " + se.getMessage());
        }
        // 回傳VO，待後續Controller導至View呈現
        return admAuthority;
    }

    @Override
    public List<AdmAuthority> getAll() {
        // 宣告ArrayList作為放入搜尋結果的列表
        List<AdmAuthority> list = new ArrayList<>();
        // 宣告VO並指定空值，若查詢無結果會出現空值，後續於Controller作錯誤處理
        AdmAuthority admAuthority = null;
        Title title = admAuthority.getTitle();
        AuthorityFunction authorityFunction = admAuthority.getAuthorityFunction();
        try (Connection con = DriverManager.getConnection(url, userid, passwd);
             PreparedStatement ps = con.prepareStatement(GET_ALL_STMT)) {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            // 執行SQL查詢並得到ResultSet物件
            ResultSet rs = ps.executeQuery();
            // 新增VO物件，取出ResultSet內資料放入VO
            while (rs.next()) {
                admAuthority = new AdmAuthority();
                title.setTitleNo(rs.getInt("titleNo"));
                authorityFunction.setAuthFuncNo(rs.getInt("authFuncNo"));
                list.add(admAuthority); // 將資料新增至列表內之後作為搜尋結果返回給View
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
    public void update(AdmAuthority admAuthority) {
        Title title = admAuthority.getTitle();
        AuthorityFunction authorityFunction = admAuthority.getAuthorityFunction();
        try (Connection con = DriverManager.getConnection(url, userid, passwd);
             PreparedStatement ps = con.prepareStatement(UPDATE_STMT)) {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            // 從request的VO取值放入PreparedStatement
            ps.setInt(1, authorityFunction.getAuthFuncNo());
            ps.setInt(2, title.getTitleNo());
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
    public void delete(Integer titleNo) {

        Connection con = null;
        PreparedStatement ps = null;

        try {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            con = DriverManager.getConnection(url, userid, passwd);
            // 刪除管理員
            ps = con.prepareStatement(DELETE_ADMAUTHORITY_STMT);
            ps.setInt(1, titleNo);
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
}
