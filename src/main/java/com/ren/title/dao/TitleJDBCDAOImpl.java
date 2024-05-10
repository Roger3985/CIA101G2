package com.ren.title.dao;

import com.Entity.ServicePicture;
import com.Entity.ServiceRobot;
import com.Entity.Title;
import com.Entity.TitleAdmVO;

import java.util.List;

import java.sql.*;
import java.util.ArrayList;

public class TitleJDBCDAOImpl implements TitleDAO_interface {
    // 預計寫入功能
    // 1. 可以新增職位
    // 2. 查詢功能
    //    (1) 可以在下拉式選單中選取編號(或名稱)，將顯示職位編號、名稱與有哪些人，並在表單底下顯示有哪些人
    //    (2) 顯示全部職位名稱與多少人
    //    (3) 點擊人數時顯示共有哪些人
    // 3. 可以修改職位名稱
    // 4. 可以刪除職位(後台管理員參照)
    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/fallelove?serverTimezone=Asia/Taipei";
    String userid = "root";
    String passwd = "SaiKou97607";

    private static final String INSERT_STMT =
            "INSERT INTO Title (titleName) VALUES (?)";
    private static final String GET_ONE_STMT =
            "SELECT titleNo,titleName FROM Title WHERE titleNo= ?";
    private static final String GET_ALL_STMT =
            "SELECT titleNo,titleName FROM Title order by titleNo";
    private static final String GET_ADMS_BytitleNo_STMT =
            "SELECT t.titleNo, titleName, admName, admHireDate FROM Title t JOIN Administrator a ON t.titleNo = a.titleNo WHERE t.titleNo IN ('?')";
    private static final String GET_ADMS_BytitleName_STMT =
            "SELECT t.titleNo, titleName, admName, admHireDate FROM Title t JOIN Administrator a ON t.titleNo = a.titleNo WHERE t.titleName IN ('?')";
    private static final String GET_ADMS_ALL_STMT =
            "SELECT t.titleNo, titleName, admName, admHireDate FROM Title t JOIN Administrator a ON t.titleNo = a.titleNo ORDER BY t.titleNo";
    private static final String UPDATE_STMT =
            "UPDATE Title set titleName=? where titleNo = ?";
    private static final String DELETE_STMT =
            "DELETE FROM Title where titleNo = ?";

    @Override
    public void insert(Title title) {

        try (Connection con = DriverManager.getConnection(url, userid, passwd);
             PreparedStatement ps = con.prepareStatement(INSERT_STMT)) {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            // 從request的VO取值放入PreparedStatement
            ps.setString(1, title.getTitleName());
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
    public Title findByPrimaryKey(Integer titleNo) {
        // 宣告VO並指定空值，若查詢無結果會出現空值，後續於Controller作錯誤處理
        Title title = null;
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
                title.setTitleNo(rs.getInt("titleNo"));
                title.setTitleName(rs.getString("titleName"));
            }
            // Handle any driver errors
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
            // Handle any SQL errors
        } catch (SQLException se) {
            throw new RuntimeException("A database error occured. " + se.getMessage());
        }
        // 回傳VO，待後續Controller導至View呈現
        return title;
    }

    @Override
    public List<Title> getAll() {
        // 宣告ArrayList作為放入搜尋結果的列表
        List<Title> list = new ArrayList<>();
        // 宣告VO並指定空值，若查詢無結果會出現空值，後續於Controller作錯誤處理
        Title title = null;
        try (Connection con = DriverManager.getConnection(url, userid, passwd);
             PreparedStatement ps = con.prepareStatement(GET_ALL_STMT)) {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            // 執行SQL查詢並得到ResultSet物件
            ResultSet rs = ps.executeQuery();
            // 新增VO物件，取出ResultSet內資料放入VO
            while (rs.next()) {
                title = new Title();
                title.setTitleNo(rs.getInt("titleNo"));
                title.setTitleName(rs.getString("titleName"));
                list.add(title); // 將資料新增至列表內之後作為搜尋結果返回給View
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
    public List<TitleAdmVO> getAdms(Integer titleNo) {
        // 宣告ArrayList作為放入搜尋結果的列表
        List<TitleAdmVO> list = new ArrayList<>();
        // 宣告VO並指定空值，若查詢無結果會出現空值，後續於Controller作錯誤處理
        TitleAdmVO titleAdmVO = null;
        try (Connection con = DriverManager.getConnection(url, userid, passwd);
             PreparedStatement ps = con.prepareStatement(GET_ADMS_BytitleNo_STMT)) {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            // 將request的資料放入SQL
            ps.setInt(1, titleNo);
            // 執行SQL查詢並得到ResultSet物件
            ResultSet rs = ps.executeQuery();
            // 新增VO物件，取出ResultSet內資料放入VO
            while (rs.next()) {
                titleAdmVO = new TitleAdmVO();
                titleAdmVO.setTitleNo(rs.getInt("titleNo"));
                titleAdmVO.setTitleName(rs.getString("titleName"));
                titleAdmVO.setAdmName(rs.getString("admName"));
                titleAdmVO.setAdmHireDate(rs.getDate("admHireDate"));
                list.add(titleAdmVO); // 將資料新增至列表內之後作為搜尋結果返回給View
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
    public List<TitleAdmVO> getAdms(String titleName) {
        // 宣告ArrayList作為放入搜尋結果的列表
        List<TitleAdmVO> list = new ArrayList<>();
        // 宣告VO並指定空值，若查詢無結果會出現空值，後續於Controller作錯誤處理
        TitleAdmVO titleAdmVO = null;
        try (Connection con = DriverManager.getConnection(url, userid, passwd);
             PreparedStatement ps = con.prepareStatement(GET_ADMS_BytitleName_STMT)) {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            // 將request的資料放入SQL
            ps.setString(1, titleName);
            // 執行SQL查詢並得到ResultSet物件
            ResultSet rs = ps.executeQuery();
            // 新增VO物件，取出ResultSet內資料放入VO
            while (rs.next()) {
                titleAdmVO = new TitleAdmVO();
                titleAdmVO.setTitleNo(rs.getInt("titleNo"));
                titleAdmVO.setTitleName(rs.getString("titleName"));
                titleAdmVO.setAdmName(rs.getString("admName"));
                titleAdmVO.setAdmHireDate(rs.getDate("admHireDate"));
                list.add(titleAdmVO); // 將資料新增至列表內之後作為搜尋結果返回給View
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
    public List<TitleAdmVO> getAdmsAll() {
        // 宣告ArrayList作為放入搜尋結果的列表
        List<TitleAdmVO> list = new ArrayList<>();
        // 宣告VO並指定空值，若查詢無結果會出現空值，後續於Controller作錯誤處理
        TitleAdmVO titleAdmVO = null;
        try (Connection con = DriverManager.getConnection(url, userid, passwd);
             PreparedStatement ps = con.prepareStatement(GET_ADMS_ALL_STMT)) {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            // 執行SQL查詢並得到ResultSet物件
            ResultSet rs = ps.executeQuery();
            // 新增VO物件，取出ResultSet內資料放入VO
            while (rs.next()) {
                titleAdmVO = new TitleAdmVO();
                titleAdmVO.setTitleNo(rs.getInt("titleNo"));
                titleAdmVO.setTitleName(rs.getString("titleName"));
                titleAdmVO.setAdmName(rs.getString("admName"));
                titleAdmVO.setAdmHireDate(rs.getDate("admHireDate"));
                list.add(titleAdmVO); // 將資料新增至列表內之後作為搜尋結果返回給View
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
    public void update(Title title) {

        try (Connection con = DriverManager.getConnection(url, userid, passwd);
             PreparedStatement ps = con.prepareStatement(UPDATE_STMT)) {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            // 從request的VO取值放入PreparedStatement
            ps.setInt(1, title.getTitleNo());
            ps.setString(2, title.getTitleName());
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
        try (Connection con = DriverManager.getConnection(url, userid, passwd);
             PreparedStatement ps = con.prepareStatement(DELETE_STMT)) {
            // 載入Driver介面的實作類別.class檔來註冊JDBC
            Class.forName(driver);
            // 將選取商品編號放入PreparedStatement
            ps.setInt(1, titleNo);
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
}