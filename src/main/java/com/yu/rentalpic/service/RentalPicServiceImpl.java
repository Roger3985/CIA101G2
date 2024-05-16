package com.yu.rentalpic.service;

import com.roger.member.entity.Member;
import com.yu.rentalpic.dao.RentalPicRepository;
import com.yu.rentalpic.entity.RentalPic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Service
public class RentalPicServiceImpl implements RentalPicService {

    @Autowired //自動裝配
    private RentalPicRepository repository;

    /**
     * PersistenceContext注解用于注入一个EntityManager对象，
     * 使得我们可以在RentalService类中使用这个entityManager对象执行持久化操作，例如保存、更新、删除实体对象，以及执行JPQL查询等。
     */
    @PersistenceContext
    private EntityManager entityManager;

    //全部查詢(RentalPic)
    @Override
    public List<RentalPic> findAll() {
        return repository.findAll();
    }

    //單筆查詢(rentalPicNo)
    @Override
    public RentalPic findByRentalPicNo(Integer rentalPicNo) {
        return repository.findByRentalPicNo(rentalPicNo);
    }

    //單筆查詢(rentalNo)
    @Override
    public RentalPic findByRentalRentalNo(Integer rentalNo) {
        return repository.findByRentalRentalNo(rentalNo);
    }

//----------------------------------------------------------------------------------------------------------------------
//主要為後端使用：增查改

    //新增 (PK為null，save方法插入數據)
    @Override
    public RentalPic addRentalPic(RentalPic rentalPic) {
        return repository.save(rentalPic);
    }

    //修改 (PK有值，save方法修改數據)
    @Override
    public RentalPic updateRentalPic(RentalPic rentalPic) {
        return repository.save(rentalPic);
    }

    //複合查詢
    @Override
    public List<RentalPic> getByCompositeQuery(Map<String, String[]> map) {
        return null;
    }

    //變更會員大頭貼
    @Override
    public void changePicture(RentalPic rentalPic, byte[] rentalFile) {
        repository.updateRentalFileById(rentalPic.getRentalPicNo(), rentalFile);
    }
}
