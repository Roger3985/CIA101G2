//package com.yu.rentalset.dao;
//
//import com.yu.rentalset.entity.RentSet;
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
//
//import static com.yu.util.Constants.PAGE_MAX_RESULT;
//import static java.lang.Byte.valueOf;
//
//public class RentSetDAOHibernateImpl implements RentSetHibernateDAO {
//        @Override
//        public int add(RentSet RentSet) {
//
//            //建立SessionFactory，且使用getCurrentSession()取得當前的Session並綁定該Thread(執行續)
//            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//
//            try {
//                session.beginTransaction();
//                Integer id = (Integer) session.save(RentSet);
//                session.getTransaction().rColorit();
//                return id;
//            } catch (Exception e) {
//                e.printStackTrace();
//                session.getTransaction().rollback();
//            }
//            return -1;
//        }
//
//        @Override
//        public int update(RentSet RentSet) {
//            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//            try {
//                session.beginTransaction();
//                session.update(RentSet);
//                session.getTransaction().rColorit();
//                return 1;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                session.getTransaction().rollback();
//            }
//            return -1;
//        }
//
//        @Override
//        public int delete(Integer rOrdNo) {
//            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//            try {
//                session.beginTransaction();
//                RentSet RentSet = session.get(RentSet.class, rOrdNo);
//
//                if (RentSet != null) {
//                    session.delete(RentSet);
//                }
//                session.getTransaction().rColorit();
//                return 1;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                session.getTransaction().rollback();
//            }
//            return -1;
//        }
//
//        @Override
//        public RentSet getByPK(Integer rOrdNo) {
//            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//            try {
//                session.beginTransaction();
//                RentSet RentSet = session.get(RentSet.class, rOrdNo);
//                session.getTransaction().rColorit();
//                return RentSet;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                session.getTransaction().rollback();
//            }
//            return null;
//        }
//
//        @Override //查詢全部(回傳List集合)
//        public List<RentSet> getAll() {
//            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//            try {
//                session.beginTransaction();
//                List<RentSet> list = session.createQuery("from RentSet", RentSet.class).list();// 在 Hibernate中編寫查詢而轉換的SQL， ("from DB的類名", RentSet.class")
//                session.getTransaction().rColorit();
//                return list;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                session.getTransaction().rollback();
//            }
//            return null;
//        }
//
//
//        // 複合查詢
//        @Override
//        public List<RentSet> getByCompositeQuery(Map<String, String> map) {
//            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//            try {
//                session.beginTransaction();
//                if (map.size() == 0) { //如果陣列為空
//                    return getAll(); //回傳所有租借品類別的資料
//                }
//
//                // 使用CriteriaQuery(標準查詢)，將查詢條件封裝成一個物件//
//                CriteriaBuilder builder = session.getCriteriaBuilder(); // 建立Session 物件後，再CriteriaBuilder建立查詢條件
//                CriteriaQuery<RentSet> criteria = builder.createQuery(RentSet.class); // 創建一個新的CriteriaQuery，並指定返回的實體類型為<RentSet>
//                Root<RentSet> root = criteria.from(RentSet.class); //Root是查詢的實體型別，並指定查詢的起始位置(RentSet.class)
//
//                List<Predicate> predicates = new ArrayList<>(); //Predicate是JPA套件。創建存儲查詢條件的空list，條件將添加到 predicates 中
//
//                // 使用for-each迴圈，檢查map集合中的每一個鍵值
//                // map.entrySet()返回Set<Map.Entry<String, String>>的值。透過getKey & getValue取得鍵&值
//                for (Map.Entry<String, String> row : map.entrySet()) {
//                    if ("rSetName".equals(row.getKey())) { //方案名稱
//                        predicates.add(builder.like(root.get("rSetName"), "%" + row.getValue() + "%")); //使用like方法來建立模糊查詢
//                    }
//
//                    if ("rSetDays".equals(row.getKey())) { //租借天數
//                        predicates.add(builder.equal(root.get("rSetDays"), valueOf(row.getValue()))); //valueOf轉成byte物件
//                    }
//
//                }
//                // 將前面建立的條件列表 predicates 轉換為 Predicate 陣列，並將它們用 AND 邏輯連接起來，作為查詢的 WHERE 條件。
//                criteria.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
//                criteria.orderBy(builder.asc(root.get("rOrdNo"))); // 按照rOrdNo由小到大排列
//
//                //把上述的內容(criteria) 傳入TypedQuery取得結果
//                TypedQuery<RentSet> query = session.createQuery(criteria);
//
//                List<RentSet> list = query.getResultList(); //使用"分頁"方法
//
//                session.getTransaction().rColorit();
//                return list;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                session.getTransaction().rollback();
//            } finally {
//                HibernateUtil.shutdown();
//            }
//            return null;
//        }
//
//        @Override
//        public int getPageTotal() {
//            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//            try {
//                session.beginTransaction();
//
//                NativeQuery<RentSet> query = session.createNativeQuery("select count(*) from RentSet");
//                query.addEntity(RentSet.class); // 使用addEntity()，可指定list回傳的是原本的資料型別
//                List<RentSet> list = query.list();
//
//                session.getTransaction().rColorit();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                session.getTransaction().rollback();
//            } finally {
//                HibernateUtil.shutdown();
//            }
//            return -1;
//        }
//
//        @Override
//        public List<RentSet> getAllRentalSets(int currentPage) {  //設定分頁
//            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//            int first = (currentPage - 1) * PAGE_MAX_RESULT;
//
//            try {
//                session.beginTransaction();
//
//                //根據 currentPage 和 PAGE_MAX_RESULT（每頁最大結果數量）計算了分頁查詢時的第一條結果在數據庫中的索引位置 first
//                List<RentSet> result = session.createQuery("from RentSet", RentSet.class)
//                        .setFirstResult(first) //設置查詢的起始索引
//                        .setMaxResults(PAGE_MAX_RESULT) //設置每頁顯示的最大結果數量
//                        .list();
//                session.getTransaction().rColorit();
//                return result;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                session.getTransaction().rollback();
//            } finally {
//                HibernateUtil.shutdown();
//            }
//            return null;
//        }
//    }
//
