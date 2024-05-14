//package com.yu.rentalmyfavorite.service;
//
//import static com.yu.util.Constants.PAGE_MAX_RESULT;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import com.yu.rentalmyfavorite.dao.RentalMyFavoriteHibernateDAO;
//import com.yu.rentalmyfavorite.dao.RentalMyFavoriteDAOHibernateImpl;
//import com.yu.rentalmyfavorite.model.RentalCategory;
//import com.yu.rentalmyfavorite.service.RentalCategoryService_Interface;
//import com.yu.util.HibernateUtil;
//import com.yu.util.Constants;
//
//
//    // 搭配 JSP / Thymeleaf 後端渲染畫面，將交易動作至於 view filter
//    public class RentalMyFavoriteServiceImpl implements RentalCategoryService_Interface {
//
//        // 一個 service 實體對應一個 dao 實體
//        private RentalMyFavoriteHibernateDAO dao;
//
//        public RentalCategoryServiceImpl() {
//            dao = new RentalMyFavoriteDAOHibernateImpl();
//        }
//        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// addRentalCat
//        @Override
//        public RentalCategory addRentalCat(String rCatName, Integer rStockQty, Integer rRentedQty, BigDecimal rDesPrice) {
//
//            RentalCategory RentalCategory = new RentalCategory();
//            RentalCategory.setrCatName(rCatName);
//            RentalCategory.setrStockQty(rStockQty);
//            RentalCategory.setrRentedQty(rRentedQty);
//            RentalCategory.setrDesPrice(rDesPrice);
//            dao.add(RentalCategory);// 將VO放入DAO的方法內執行資料庫操作
//
//            return RentalCategory;
//        }
//        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// updateRentalCat
//        @Override
//        public RentalCategory updateRentalCat(Integer rCatNo,String rCatName, Integer rStockQty, Integer rRentedQty, BigDecimal rDesPrice) {
//
//            RentalCategory RentalCategory = new RentalCategory();
//            RentalCategory.setrCatNo(rCatNo);
//            RentalCategory.setrCatName(rCatName);
//            RentalCategory.setrStockQty(rStockQty);
//            RentalCategory.setrRentedQty(rRentedQty);
//            RentalCategory.setrDesPrice(rDesPrice);
//
//            dao.update(RentalCategory);
//            return RentalCategory;
//        }
//
//        @Override
//        public void deleteRentalCat(Integer rCatNo) {
//            dao.delete(rCatNo);
//        }
//
//
//        @Override //單筆查詢(PK)
//        public RentalCategory getOneRentalCat(Integer rCatNo) {
//            return dao.getByPK(rCatNo);
//        }
//
//        @Override   //萬用複合查詢
//        public List<RentalCategory> getAll() {
//            return dao.getAll();
//        }
//
//        @Override
//        public List<RentalCategory> getAllRentalCats(int currentPage) {
//            return dao.getAllRentalCats(currentPage);
//        }
//
//        @Override
//        public List<RentalCategory> getByCompositeQuery(Map<String, String[]> map) {
//            Map<String, String> query = new HashMap<>();
//            // Map.Entry即代表一組key-value
//            Set<Map.Entry<String, String[]>> entry = map.entrySet();
//
//            for (Map.Entry<String, String[]> row : entry) {
//                String key = row.getKey();
//                // 因為請求參數裡包含了action，做個去除動作
//                if ("action".equals(key)) {
//                    continue;
//                }
//                // 若是value為空即代表沒有查詢條件，做個去除動作
//                String value = row.getValue()[0]; // getValue拿到一個String陣列, 接著[0]取得第一個元素檢查
//                if (value == null || value.isEmpty()) {
//                    continue;
//                }
//                query.put(key, value);
//            }
//
//            System.out.println(query);
//
//            return dao.getByCompositeQuery(query);
//        }
//
//        @Override
//        public int getPageTotal() {
//            long total = dao.getPageTotal();
////            // 計算Rental數量每頁3筆的話總共有幾頁
//            int pageQty = (int)(total % PAGE_MAX_RESULT == 0 ? (total / PAGE_MAX_RESULT) : (total / PAGE_MAX_RESULT + 1));
//            return pageQty;
//        }
//
//    }
//
//
