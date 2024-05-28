package com.yu.rentalcategory.service;

import com.yu.rental.dao.RentalRepository;
import com.yu.rental.entity.Rental;
import com.yu.rentalcategory.dao.RentalCategoryRepository;
import com.yu.rentalcategory.entity.RentalCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RentalCategoryServiceImpl implements RentalCategoryService {

    @Autowired //自動裝配
    private RentalCategoryRepository repository;
    @Autowired
    private RentalRepository rentalRepository;


    @PersistenceContext
    private EntityManager entityManager;

    //單筆查詢
    @Override
    public RentalCategory findByCatNo(Integer rentalCatNo) {
        return repository.findByRentalCatNo(rentalCatNo);	}

    @Override
    public Optional<RentalCategory> findRentalCategory_RentalCatNo(Integer rentalCatNo) {
        return repository.findRentalCategory_RentalCatNo(rentalCatNo);
    }

    //單筆查詢
    @Override
    public RentalCategory getRentalCatName(String rentalCatName) {
        return repository.findByRentalCatName(rentalCatName);	}

    //單筆查詢
    @Override
    public List<RentalCategory> getRentalDesPrice(BigDecimal rentalDesPrice) {
        return repository.findByRentalDesPrice(rentalDesPrice);	}

    //單筆查詢
    @Override
    public RentalCategory getOneRentalCat(Integer rentalCatNo) {
        return repository.findByRentalCatNo(rentalCatNo);
    }

    //全部查詢(RentalCategory)
    @Override
    public List<RentalCategory> findAll() {
        return repository.findAll();
    }

    //----------------------------------------------------------------------------------------------------------------------
    //主要為後端使用：增查改
    @Override
    public RentalCategory addRentalCat(RentalCategory rentalCategory) {
        return repository.save(rentalCategory);
    }

    @Override
    public RentalCategory updateRentalCat(RentalCategory rentalCategory) {
        return repository.save(rentalCategory);
    }

    //複合查詢
    @Override
    public List<RentalCategory> searchRentalCats(Map<String, String[]> paramsMap) {
        //複合查詢 (使用"Map<String, String[]> paramsMap" 處理多個參數值)
        if (paramsMap == null || paramsMap.isEmpty()) {
            return repository.findAll(); // 如果没有任何条件，返回所有
        }

        //JPQL查詢語句
        StringBuilder jpql = new StringBuilder("SELECT cat FROM RentalCategory cat WHERE 1=1");

        Map<String, Object> params = new HashMap<>();

        for (Map.Entry<String, String[]> entry : paramsMap.entrySet()) {
            String paramName = entry.getKey();
            String[] paramValues = entry.getValue();

            if (paramValues != null && paramValues.length > 0) {
                if ("rentalCatNo".equals(paramName)) {
                    Integer rentalCatNo = Integer.parseInt(paramValues[0]);
                    jpql.append(" AND cat.rentalCatNo = :rentalCatNo");
                    params.put("rentalCatNo", rentalCatNo);
                } else if ("rentalCatName".equals(paramName)) {
                    String rentalCatName = paramValues[0];
                    jpql.append(" AND cat.rentalCatName LIKE :rentalCatName");
                    params.put("rentalCatName", "%" + rentalCatName + "%");
                } else if ("rentalStockQty".equals(paramName)) {
                    Integer rentalStockQty = Integer.parseInt(paramValues[0]);
                    jpql.append(" AND cat.rentalStockQty = :rentalStockQty");
                    params.put("rentalStockQty", rentalStockQty);
                } else if ("rentalRentedQty".equals(paramName)) {
                    Integer rentalRentedQty = Integer.parseInt(paramValues[0]);
                    jpql.append(" AND cat.rentalRentedQty = :rentalRentedQty");
                    params.put("rentalRentedQty", rentalRentedQty);
                } else if ("rentalDesPrice".equals(paramName)) {
                    BigDecimal rentalDesPrice = new BigDecimal(paramValues[0]);
                    jpql.append(" AND cat.rentalDesPrice = :rentalDesPrice");
                    params.put("rentalDesPrice", rentalDesPrice);
                }
            }
        }
        TypedQuery<RentalCategory> query = entityManager.createQuery(jpql.toString(), RentalCategory.class);
        // 设置参数
        for (Map.Entry<String, Object> rentalCatEntry : params.entrySet()) {
            query.setParameter(rentalCatEntry.getKey(), rentalCatEntry.getValue());
        }
        // 执行查询
        return query.getResultList();
    }


}

