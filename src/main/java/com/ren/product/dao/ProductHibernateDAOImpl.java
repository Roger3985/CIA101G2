//package com.ren.product.dao;
//
//import com.Entity.Product;
//import javax.persistence.*;
//import javax.persistence.criteria.*;
//
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.sql.Date;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import static com.ren.util.Constants.PAGE_MAX_RESULT;
//
//@Repository
//public class ProductHibernateDAOImpl implements ProductDAO_interface {
//    // SessionFactory 為 thread-safe，可宣告為屬性讓請求執行緒們共用
//
//    private SessionFactory factory;
//
//    @Autowired
//    public ProductHibernateDAOImpl(SessionFactory sessionFactory) {
//        this.factory = sessionFactory;
//    }
//
//    // Session 為 not thread-safe，所以此方法在各個增刪改查方法裡呼叫
//    // 以避免請求執行緒共用了同個 Session
//    private Session getSession() {
//        return factory.getCurrentSession();
//    }
//
//    @Override
//    @Transactional
//    public int insert(Product entity) {
//        // 回傳給 service 剛新增成功的自增主鍵值
//        return (Integer) getSession().save(entity);
//    }
//
//    @Override
//    @Transactional
//    public Product getById(Integer id) {
//        System.out.println("DAO你有在嗎");
//        return getSession().get(Product.class, id);
//    }
//
//    @Override
//    @Transactional
//    public List<Product> getAll() {
//        return getSession().createQuery("from ProductVO", Product.class).list();
//    }
//
//    @Override
//    public List<Product> getByCompositeQuery(Map<String, String> map) {
//        if (map.size() == 0) {
//            return getAll();
//        }
//
//        CriteriaBuilder builder = getSession().getCriteriaBuilder();
//        CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
//        Root<Product> root = criteria.from(Product.class);
//
//        List<Predicate> predicates = new ArrayList<>();
//
//        if (map.containsKey("startpNo") && map.containsKey("endpNo")) {
//            predicates.add(builder.between(root.get("pNo"), Date.valueOf(map.get("startpNo")), Date.valueOf(map.get("endpNo"))));
//        }
//
//        if (map.containsKey("startpPrice") && map.containsKey("endpPrice")) {
//            predicates.add(builder.between(root.get("pPrice"), new BigDecimal(map.get("startpPrice")), new BigDecimal(map.get("endpPrice"))));
//        }
//
//        for (Map.Entry<String, String> row : map.entrySet()) {
//            if ("pName".equals(row.getKey())) {
//                predicates.add(builder.like(root.get("pName"), "%" + row.getValue() + "%"));
//            }
//
//            if ("job".equals(row.getKey())) {
//                predicates.add(builder.equal(root.get("job"), row.getValue()));
//            }
//
//            if ("startpNo".equals(row.getKey())) {
//                if (!map.containsKey("endpNo"))
//                    predicates.add(builder.greaterThanOrEqualTo(root.get("pNo"), Date.valueOf(row.getValue())));
//            }
//
//            if ("endpNo".equals(row.getKey())) {
//                if (!map.containsKey("startpNo"))
//                    predicates.add(builder.lessThanOrEqualTo(root.get("pNo"), Date.valueOf(row.getValue())));
//
//            }
//
//            if ("startpPrice".equals(row.getKey())) {
//                if (!map.containsKey("endpPrice"))
//                    predicates.add(builder.greaterThanOrEqualTo(root.get("pPrice"), new BigDecimal(row.getValue())));
//
//            }
//
//            if ("endpPrice".equals(row.getKey())) {
//                if (!map.containsKey("startpPrice"))
//                    predicates.add(builder.lessThanOrEqualTo(root.get("pPrice"), new BigDecimal(row.getValue())));
//
//            }
//
//        }
//
//        criteria.where(builder.and());
//        criteria.orderBy();
//        TypedQuery<Product> query = getSession().createQuery(criteria);
//
//        return query.getResultList();
//    }
//
//
//    @Override
//    @Transactional
//    public List<Product> getAll(int currentPage) {
//        int first = (currentPage - 1) * PAGE_MAX_RESULT;
//        return getSession().createQuery("from ProductVO", Product.class)
//                .setFirstResult(first)
//                .setMaxResults(PAGE_MAX_RESULT)
//                .list();
//    }
//
//    @Override
//    @Transactional
//    public int update(Product entity) {
//        try {
//            getSession().update(entity);
//            // 回傳給 service，1代表刪除成功
//            return 1;
//        } catch (Exception e) {
//            // 回傳給 service，-1代表刪除失敗
//            return -1;
//        }
//    }
//
//    @Override
//    @Transactional
//    public int delete(Integer id) {
//        Product product = getSession().get(Product.class, id);
//        if (product != null) {
//            getSession().delete(product);
//            // 回傳給 service，1代表刪除成功
//            return 1;
//        } else {
//            // 回傳給 service，-1代表刪除失敗
//            return -1;
//        }
//    }
//
//    @Override
//    @Transactional
//    public long getTotal() {
//        return getSession().createQuery("select count(*) from ProductVO", Long.class).uniqueResult();
//    }
//
//}