package com.chihyun.mycoupon.model;

import com.chihyun.coupon.entity.Coupon;
import com.chihyun.mycoupon.entity.MyCoupon;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CompositeQuery_MyCoupon {

    public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder, Root<MyCoupon> root, String columnName, String value) {

        Predicate predicate = null;

        if ("coupNo".equals(columnName) || "coupRelStat".equals(columnName) || "memNo".equals(columnName)) {
            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
        } else if ("coupName".equals(columnName)) {
            predicate = builder.like(root.get(columnName), "%" + value + "%");
        } else if ("coupDisc".equals(columnName)) {
            predicate = builder.equal(root.get(columnName), new BigDecimal(value));
        } else if ("coupExpDate".equals(columnName)) {
            predicate = builder.equal(root.get(columnName), java.sql.Date.valueOf(value));
        }
        return predicate;
    }

    public static List<MyCoupon> getAllC(Map<String, String[]> map, Session session) {
        Transaction tx = session.beginTransaction();
        List<MyCoupon> list = null;
        try {

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<MyCoupon> criteriaQuery = builder.createQuery(MyCoupon.class);
            Root<MyCoupon> root = criteriaQuery.from(MyCoupon.class);
            List<Predicate> predicateList = new ArrayList<Predicate>();

            Set<String> keys = map.keySet();
            int count = 0;
            for (String key : keys) {
                String value = map.get(key)[0];
                if (value != null && value.trim().length() != 0 && !"action".equals(key)) {
                    count++;
                    predicateList.add(get_aPredicate_For_AnyDB(builder, root, key, value.trim()));
                    System.out.println("MyCoupon有送出複合查詢的資料欄位數:" + count);
                    System.out.println(value);
                }
            }
            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            criteriaQuery.orderBy(builder.asc(root.get("coupNo")));
            Query query = session.createQuery(criteriaQuery);
            list = query.getResultList();

            tx.commit();
        } catch (RuntimeException ex) {
            if (tx != null)
                tx.rollback();
            throw ex;
        } finally {
            session.close();
        }
        return list;
    }
}
