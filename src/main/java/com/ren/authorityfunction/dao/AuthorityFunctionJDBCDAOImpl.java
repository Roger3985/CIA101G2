package com.ren.authorityfunction.dao;

import com.ren.authorityfunction.entity.AuthorityFunction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorityFunctionJDBCDAOImpl implements AuthorityFunctionDAO_interface {

    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/fallelove?serverTimezone=Asia/Taipei";
    String userid = "root";
    String passwd = "SaiKou97607";

    // 新增商品
    private static final String INSERT_STMT =
            "INSERT INTO AuthorityFunction (authFuncInfo) VALUES (?)";
    // 查詢單一品項
    private static final String GET_ONE_STMT =
            "SELECT authFuncNo,authFuncInfo FROM AuthorityFunction WHERE authFuncNo = ?";
    // 查詢全部
    private static final String GET_ALL_STMT =
            "SELECT authFuncNo,authFuncInfo FROM AuthorityFunction ORDER BY authFuncNo";
    // 修改商品資料
    private static final String UPDATE_STMT =
            "UPDATE AuthorityFunction SET authFuncInfo=? WHERE authFuncNo = ?";
    // 刪除會員
    private static final String DELETE_AUTHORITYFUNCTION_STMT =
            "DELETE FROM AuthorityFunction WHERE authFuncNo = ?";

    @Override
    public void insert(AuthorityFunction authorityFunction) {

        try (Connection con = DriverManager.getConnection(url, userid, passwd);
             PreparedStatement ps = con.prepareStatement(INSERT_STMT)) {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            // 從request的VO取值放入PreparedStatement
            ps.setString(1, authorityFunction.getAuthFuncInfo());
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
    public AuthorityFunction findByPrimaryKey(Integer authFuncNo) {
        // 宣告VO並指定空值，若查詢無結果會出現空值，後續於Controller作錯誤處理
        AuthorityFunction authorityFunction = null;
        // ResultSet在相關的Statement關閉時會自動關閉，因此不用另外寫在Auto-closable
        try (Connection con = DriverManager.getConnection(url, userid, passwd);
             PreparedStatement ps = con.prepareStatement(GET_ONE_STMT)) {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            // 將request的商品編號放入SQL
            ps.setInt(1, authFuncNo);
            // 執行SQL查詢並得到ResultSet物件
            ResultSet rs = ps.executeQuery();
            // 取出ResultSet內資料放入VO
            while (rs.next()) {
                authorityFunction = new AuthorityFunction();
                authorityFunction.setAuthFuncNo(rs.getInt("authFuncNo"));
                authorityFunction.setAuthFuncInfo(rs.getString("authFuncInfo"));
            }
            // Handle any driver errors
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
            // Handle any SQL errors
        } catch (SQLException se) {
            throw new RuntimeException("A database error occured. " + se.getMessage());
        }
        // 回傳VO，待後續Controller導至View呈現
        return authorityFunction;
    }

    @Override
    public List<AuthorityFunction> getAll() {
        // 宣告ArrayList作為放入搜尋結果的列表
        List<AuthorityFunction> list = new ArrayList<>();
        // 宣告VO並指定空值，若查詢無結果會出現空值，後續於Controller作錯誤處理
        AuthorityFunction authorityFunction = null;
        try (Connection con = DriverManager.getConnection(url, userid, passwd);
             PreparedStatement ps = con.prepareStatement(GET_ALL_STMT)) {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            // 執行SQL查詢並得到ResultSet物件
            ResultSet rs = ps.executeQuery();
            // 新增VO物件，取出ResultSet內資料放入VO
            while (rs.next()) {
                authorityFunction = new AuthorityFunction();
                authorityFunction.setAuthFuncNo(rs.getInt("authFuncNo"));
                authorityFunction.setAuthFuncInfo(rs.getString("authFuncInfo"));
                list.add(authorityFunction); // 將資料新增至列表內之後作為搜尋結果返回給View
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
    public void update(AuthorityFunction authorityFunction) {

        try (Connection con = DriverManager.getConnection(url, userid, passwd);
             PreparedStatement ps = con.prepareStatement(UPDATE_STMT)) {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            // 從request的VO取值放入PreparedStatement
            ps.setInt(1, authorityFunction.getAuthFuncNo());
            ps.setString(2, authorityFunction.getAuthFuncInfo());
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
    public void delete(Integer authFuncNo) {

        Connection con = null;
        PreparedStatement ps = null;

        try {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            con = DriverManager.getConnection(url, userid, passwd);
            // 刪除管理員
            ps = con.prepareStatement(DELETE_AUTHORITYFUNCTION_STMT);
            ps.setInt(1, authFuncNo);
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
