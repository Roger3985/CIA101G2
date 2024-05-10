//package com.ren.product.dao;
//
//import com.Entity.Product;
//import com.Entity.ProductCategory;
//import com.Entity.ServicePicture;
//import com.Entity.ServiceRobot;
//
//import javax.naming.Context;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//import javax.sql.DataSource;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class ProductJNDIDAOImpl implements ProductDAO_interface {
//
//	// 一個應用程式中,針對一個資料庫 ,共用一個DataSource即可
//	private static DataSource ds = null;
//	static {
//		try {
//			System.out.println("1");
//			Context ctx = new InitialContext();
//			System.out.println("2");
//			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/fallelove");
//			System.out.println("DataSource: " + ds);
//		} catch (NamingException e) {
//			System.err.println("Failed to initialize DataSource: " + e.getMessage());
//			e.printStackTrace();
//		}
//	}
//
//	// 新增商品
//	private static final String INSERT_STMT = "INSERT INTO product (pCatNo,pName,pInfo,pSize,pColor,pPrice,pStat,pSalQty,pComPeople,pComScore) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//	// 查詢單一品項
//	private static final String GET_ONE_STMT = "SELECT pNo,pCatNo,pName,pInfo,pSize,pColor,pPrice,pStat,pSalQty,pComPeople,pComScore FROM product WHERE pNo = ?";
//	// 查詢全部
//	private static final String GET_ALL_STMT = "SELECT pNo,pCatNo,pName,pInfo,pSize,pColor,pPrice,pStat,pSalQty,pComPeople,pComScore FROM product ORDER BY pNo";
//	// 修改商品資料
//	private static final String UPDATE_STMT = "UPDATE product SET pName=?, pInfo=?, pSize=?, pColor=?, pPrice=?, pStat=?, pSalQty=?, pComPeople=?, pComScore=? WHERE pNo = ?";
//	// 刪除所有參照商品的表格
//	// 刪除商品訂單明細
//	private static final String DELETE_PRODUCTORDERDETAILS_STMT = "DELETE FROM ProductOrderDetail WHERE pNo = ?";
//	// 刪除商品我的最愛
//	private static final String DELETE_PRODUCTMYFAVORITE_STMT = "DELETE FROM ProductMyFavorite WHERE pNo = ?";
//	// 刪除商品購物車清單
//	private static final String DELETE_CART_STMT = "DELETE FROM cart WHERE pNo = ?";
//	// 刪除商品照片
//	private static final String DELETE_PRODUCTPICTURE_STMT = "DELETE FROM ProductPicture WHERE pNo = ?";
//	// 刪除商品
//	private static final String DELETE_PRODUCT_STMT = "DELETE FROM product WHERE pNo = ?";
//
//	@Override
//	public int insert(Product product) {
//		ProductCategory productCategory = product.getProductCategory();
//		int result = Integer.parseInt(null);
//		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(INSERT_STMT)) {
//			// 從request的VO取值放入PreparedStatement
//			ps.setInt(1, productCategory.getpCatNo());
//			ps.setString(2, product.getpName());
//			ps.setString(3, product.getpInfo());
//			ps.setInt(4, product.getpSize());
//			ps.setString(5, product.getpColor());
//			ps.setBigDecimal(6, product.getpPrice());
//			ps.setByte(7, product.getpStat());
//			ps.setInt(8, product.getpSalQty());
//			ps.setInt(9, product.getpComPeople());
//			ps.setInt(10, product.getpComScore());
//			// 執行SQL指令將VO資料新增進資料庫
//			result = ps.executeUpdate();
//			// Handle any SQL errors
//		} catch (SQLException se) {
//			throw new RuntimeException("A database error occured. " + se.getMessage());
//		}
//		return result;
//	}
//
//	@Override
//	public Product getById(Integer pNo) {
//		// 宣告VO並指定空值，若查詢無結果會出現空值，後續於Controller作錯誤處理
//		Product product = null;
//		ProductCategory productCategory = product.getProductCategory();
//		// ResultSet在相關的Statement關閉時會自動關閉，因此不用另外寫在Auto-closable
//		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(GET_ONE_STMT)) {
//			// 將request的商品編號放入SQL
//			ps.setInt(1, pNo);
//			// 執行SQL查詢並得到ResultSet物件
//			ResultSet rs = ps.executeQuery();
//			// 取出ResultSet內資料放入VO
//			while (rs.next()) {
//				product = new Product();
//				product.setpNo(rs.getInt("pNo"));
//				productCategory.setpCatNo(rs.getInt("pCatNo"));
//				product.setpName(rs.getString("pName"));
//				product.setpInfo(rs.getString("pInfo"));
//				product.setpSize(rs.getInt("pSize"));
//				product.setpColor(rs.getString("pColor"));
//				product.setpPrice(rs.getBigDecimal("pPrice"));
//				product.setpStat(rs.getByte("pStat"));
//				product.setpSalQty(rs.getInt("pSalQty"));
//				product.setpComPeople(rs.getInt("pComPeople"));
//				product.setpComScore(rs.getInt("pComScore"));
//			}
//			// Handle any SQL errors
//		} catch (SQLException se) {
//			throw new RuntimeException("A database error occured. " + se.getMessage());
//		}
//		// 回傳VO，待後續Controller導至View呈現
//		return product;
//	}
//
//	@Override
//	public List<Product> getAll() {
//		// 宣告ArrayList作為放入搜尋結果的列表
//		List<Product> list = new ArrayList<>();
//		// 宣告VO並指定空值，若查詢無結果會出現空值，後續於Controller作錯誤處理
//		Product product = null;
//		ProductCategory productCategory = product.getProductCategory();
//		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(GET_ALL_STMT)) {
//			// 執行SQL查詢並得到ResultSet物件
//			ResultSet rs = ps.executeQuery();
//			// 新增VO物件，取出ResultSet內資料放入VO
//			while (rs.next()) {
//				product = new Product();
//				product.setpNo(rs.getInt("pNo"));
//				productCategory.setpCatNo(rs.getInt("pCatNo"));
//				product.setpName(rs.getString("pName"));
//				product.setpInfo(rs.getString("pInfo"));
//				product.setpSize(rs.getInt("pSize"));
//				product.setpColor(rs.getString("pColor"));
//				product.setpPrice(rs.getBigDecimal("pPrice"));
//				product.setpStat(rs.getByte("pStat"));
//				product.setpSalQty(rs.getInt("pSalQty"));
//				product.setpComPeople(rs.getInt("pComPeople"));
//				product.setpComScore(rs.getInt("pComScore"));
//				list.add(product); // 將資料新增至列表內之後作為搜尋結果返回給View
//			}
//			// Handle any SQL errors
//		} catch (SQLException se) {
//			throw new RuntimeException("A database error occured. " + se.getMessage());
//		}
//		return list;
//	}
//
//	@Override
//	public List<Product> getByCompositeQuery(Map<String, String> map) {
//		return null;
//	}
//
//	@Override
//	public List<Product> getAll(int currentPage) {
//		return null;
//	}
//
//	@Override
//	public int update(Product product) {
//		ProductCategory productCategory = product.getProductCategory();
//		int result = Integer.parseInt(null);
//		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(UPDATE_STMT)) {
//			// 從request的VO取值放入PreparedStatement
//			ps.setString(1, product.getpName());
//			ps.setString(2, product.getpInfo());
//			ps.setInt(3, product.getpSize());
//			ps.setString(4, product.getpColor());
//			ps.setBigDecimal(5, product.getpPrice());
//			ps.setByte(6, product.getpStat());
//			ps.setInt(7, product.getpSalQty());
//			ps.setInt(8, product.getpComPeople());
//			ps.setInt(9, product.getpComScore());
//			ps.setInt(10, product.getpNo());
//			// 執行SQL指令將資料庫內對應的資料修改成VO的值
//			result = ps.executeUpdate();
//			// Handle any SQL errors
//		} catch (SQLException se) {
//			throw new RuntimeException("A database error occured. " + se.getMessage());
//		}
//		return result;
//	}
//
//	@Override
//	public int delete(Integer pNo) {
//
//		Connection con = null;
//		PreparedStatement ps = null;
//		int result = Integer.parseInt(null);
//		try {
//			// 取得連線池連線
//			con = ds.getConnection();
//			// 設定於 pstmt.executeUpdate()之前
//			con.setAutoCommit(false);
///******************************資料庫換成級聯版的話註解掉以下內容************************************/
//			ps = con.prepareStatement(DELETE_PRODUCTORDERDETAILS_STMT);
//			ps.setInt(1, pNo);
//			ps.executeUpdate();
//			// 刪除商品我的最愛
//			ps = con.prepareStatement(DELETE_PRODUCTMYFAVORITE_STMT);
//			ps.setInt(1, pNo);
//			ps.executeUpdate();
//			// 刪除商品購物車清單
//			ps = con.prepareStatement(DELETE_CART_STMT);
//			ps.setInt(1, pNo);
//			ps.executeUpdate();
//			// 刪除商品照片
//			ps = con.prepareStatement(DELETE_PRODUCTPICTURE_STMT);
//			ps.setInt(1, pNo);
//			ps.executeUpdate();
///********************************************************************************************/
//			// 終於可以刪商品了
//			ps = con.prepareStatement(DELETE_PRODUCT_STMT);
//			ps.setInt(1, pNo);
//			ps.executeUpdate();
//			// 設定於 pstmt.executeUpdate()之後
//			con.commit();
//			con.setAutoCommit(true);
//			// Handle any SQL errors
//		} catch (SQLException se) {
//			if (con != null) {
//				try {
//					// .rollback()設定於當有exception發生時之catch區塊內
//					con.rollback();
//				} catch (SQLException excep) {
//					throw new RuntimeException("rollback error occured. " + excep.getMessage());
//				}
//			}
//			throw new RuntimeException("A database error occured. " + se.getMessage());
//		} finally {
//			if (ps != null) {
//				try {
//					ps.close();
//				} catch (SQLException se) {
//					se.printStackTrace(System.err);
//				}
//			}
//			if (con != null) {
//				try {
//					con.close();
//				} catch (Exception e) {
//					e.printStackTrace(System.err);
//				}
//			}
//		}
//		return result;
//	}
//
//	@Override
//	public long getTotal() {
//		return 0;
//	}
//
//}
