package com.chihyun.coupon.model;

import com.chihyun.coupon.dao.CouponRepository;
import com.chihyun.coupon.entity.Coupon;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service("couponService")
public class CouponService {

    @Autowired
    CouponRepository repository;

    @Autowired
    private SessionFactory sessionFactory;

    public void addCoupon(Coupon coupon) {
//        coupon.setCoupExpDate(Timestamp.valueOf(String.valueOf(coupon.getCoupExpDate())));
//        coupon.setCoupRelDate(Timestamp.valueOf(String.valueOf(coupon.getCoupRelDate())));
        repository.save(coupon);
    }

    public void updateCoupon(Coupon coupon) {
        repository.save(coupon);
    }

    public void deleteCoupon(Integer coupNo){
        if(repository.existsById(coupNo)){
            repository.deleteById(coupNo);
        }
    }

    public Coupon getOneCoupon(Integer coupNo) {
        Optional<Coupon> optional = repository.findById(coupNo);
        return optional.orElse(null);
    }

    public List<Coupon> getRel(){
       List<Coupon> list = repository.findAll();
       for (Coupon coupon : list){
           Byte stat = coupon.getCoupRelStat();
           if(stat.equals(0)){
               list.add(coupon);
           }
       }
       return list;
    }


    public List<Coupon> getAll() {
        return repository.findAll();
    }

    public List<Coupon> getAll(Map<String, String[]> map) {
        return CompositeQuery_Coupon.getAllC(map, sessionFactory.openSession());
    }
}
