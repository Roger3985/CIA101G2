//package com.yu.rentalmyfavorite.dao;
//
//import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
//import com.yu.util.HibernateUtil;
//import org.hibernate.Session;
//import org.hibernate.query.NativeQuery;
//
//import javax.persistence.TypedQuery;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.sql.Date;
//
//import static com.yu.util.Constants.PAGE_MAX_RESULT;
//
//public class RentalMyFavoriteDAOHibernateImpl implements RentalMyFavoriteHibernateDAO {
//    @Override
//    public int add(RentalMyFavorite RentalMyFavorite) {
//
//        //建立SessionFactory，且使用getCurrentSession()取得當前的Session並綁定該Thread(執行續)
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//
//        try {
//            session.beginTransaction();
//            Integer id = (Integer) session.save(RentalMyFavorite);
//            session.getTransaction().rColorit();
//            return id;
//        } catch (Exception e) {
//            e.printStackTrace();
//            session.getTransaction().rollback();
//        }
//        return -1;
//    }
//
//    @Override
//    public int update(RentalMyFavorite RentalMyFavorite) {
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        try {
//            session.beginTransaction();
//            session.update(RentalMyFavorite);
//            session.getTransaction().rColorit();
//            return 1;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            session.getTransaction().rollback();
//        }
//        return -1;
//    }
//
//    @Override
//    public int delete(Integer rNo, Integer memNo) {
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        try {
//            session.beginTransaction();
//
//            // 因rNo & memNo為複合主鍵，故先設定至pk參數。根據rNo & memNo尋找匹對的RentalMyFavorite對象
//            RentalMyFavorite.CompositeDetail pk = new RentalMyFavorite.CompositeDetail();
//            pk.setRNo(rNo);
//            pk.setMemNo(memNo);
//            RentalMyFavorite RentalMyFavorite = session.get(RentalMyFavorite.class, pk);
//
//            if (RentalMyFavorite != null) {
//                session.delete(RentalMyFavorite);
//            }
//            session.getTransaction().rColorit();
//            return 1;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            session.getTransaction().rollback();
//        }
//        return -1;
//    }
//
//    @Override
//    public RentalMyFavorite getByPK(Integer rNo, Integer memNo) {
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        try {
//            session.beginTransaction();
//
//            // 因rNo & memNo為複合主鍵，故先設定至pk參數。根據rNo & memNo尋找匹對的RentalMyFavorite對象
//            RentalMyFavorite.CompositeDetail pk = new RentalMyFavorite.CompositeDetail();
//            pk.setRNo(rNo);
//            pk.setMemNo(memNo);
//            RentalMyFavorite RentalMyFavorite = session.get(RentalMyFavorite.class, pk);
//            session.getTransaction().rColorit();
//            return RentalMyFavorite;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            session.getTransaction().rollback();
//        }
//        return null;
//    }
//
//    @Override //查詢全部(回傳List集合)
//    public List<RentalMyFavorite> getAll() {
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        try {
//            session.beginTransaction();
//            List<RentalMyFavorite> list = session.createQuery("from RentalMyFavorite", RentalMyFavorite.class).list();// 在 Hibernate中編寫查詢而轉換的SQL， ("from DB的類名", RentalMyFavorite.class")
//            session.getTransaction().rColorit();
//            return list;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            session.getTransaction().rollback();
//        }
//        return null;
//    }
//
//
//    // 複合查詢
//    @Override
//    public List<RentalMyFavorite> getByCompositeQuery(Map<String, String> map) {
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        try {
//            session.beginTransaction();
//            if (map.size() == 0) { //如果陣列為空
//                return getAll(); //回傳所有租借品類別的資料
//            }
//
//            // 使用CriteriaQuery(標準查詢)，將查詢條件封裝成一個物件//
//            CriteriaBuilder builder = session.getCriteriaBuilder(); // 建立Session 物件後，再CriteriaBuilder建立查詢條件
//            CriteriaQuery<RentalMyFavorite> criteria = builder.createQuery(RentalMyFavorite.class); // 創建一個新的CriteriaQuery，並指定返回的實體類型為<RentalMyFavorite>
//            Root<RentalMyFavorite> root = criteria.from(RentalMyFavorite.class); //Root是查詢的實體型別，並指定查詢的起始位置(RentalMyFavorite.class)
//
//            List<Predicate> predicates = new ArrayList<>(); //Predicate是JPA套件。創建存儲查詢條件的空list，條件將添加到 predicates 中
//
//            // 使用for-each迴圈，檢查map集合中的每一個鍵值
//            // map.entrySet()返回Set<Map.Entry<String, String>>的值。透過getKey & getValue取得鍵&值
//            for (Map.Entry<String, String> row : map.entrySet()) {
//                if ("rNo".equals(row.getKey())) { //庫存數量
//                    predicates.add(builder.equal(root.get("rNo"), row.getValue()));
//                }
//
//                if ("memNo".equals(row.getKey())) { //已租借數量
//                    predicates.add(builder.equal(root.get("memNo"), row.getValue()));
//                }
//
//                if ("rFavTime".equals(row.getKey())) {
//                    if (!map.containsKey("rFavTime"))
//                        predicates.add(builder.greaterThanOrEqualTo(root.get("rFavTime"), Date.valueOf(row.getValue())));
//                }
//
//            }
//            // 將前面建立的條件列表 predicates 轉換為 Predicate 陣列，並將它們用 AND 邏輯連接起來，作為查詢的 WHERE 條件。
//            criteria.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
//            criteria.orderBy(builder.asc(root.get("rNo"))); // 按照rNo由小到大排列
//
//            //把上述的內容(criteria) 傳入TypedQuery取得結果
//            TypedQuery<RentalMyFavorite> query = session.createQuery(criteria);
//
//            List<RentalMyFavorite> list = query.getResultList(); //使用"分頁"方法
//
//            session.getTransaction().rColorit();
//            return list;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            session.getTransaction().rollback();
//        } finally {
//            HibernateUtil.shutdown();
//        }
//        return null;
//    }
//
//    @Override
//    public int getPageTotal() {
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        try {
//            session.beginTransaction();
//
//            NativeQuery<RentalMyFavorite> query = session.createNativeQuery("select count(*) from RentalMyFavorite");
//            query.addEntity(RentalMyFavorite.class); // 使用addEntity()，可指定list回傳的是原本的資料型別
//            List<RentalMyFavorite> list = query.list();
//
//            session.getTransaction().rColorit();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            session.getTransaction().rollback();
//        } finally {
//            HibernateUtil.shutdown();
//        }
//        return -1;
//    }
//
//    @Override
//    public List<RentalMyFavorite> getAllRentalFavs(int currentPage) {  //設定分頁
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        int first = (currentPage - 1) * PAGE_MAX_RESULT;
//
//        try {
//            session.beginTransaction();
//
//            //根據 currentPage 和 PAGE_MAX_RESULT（每頁最大結果數量）計算了分頁查詢時的第一條結果在數據庫中的索引位置 first
//            List<RentalMyFavorite> result = session.createQuery("from RentalMyFavorite", RentalMyFavorite.class)
//                    .setFirstResult(first) //設置查詢的起始索引
//                    .setMaxResults(PAGE_MAX_RESULT) //設置每頁顯示的最大結果數量
//                    .list();
//            session.getTransaction().rColorit();
//            return result;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            session.getTransaction().rollback();
//        } finally {
//            HibernateUtil.shutdown();
//        }
//        return null;
//    }
//}
