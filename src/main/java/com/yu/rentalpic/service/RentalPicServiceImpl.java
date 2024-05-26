
package com.yu.rentalpic.service;

import com.roger.member.entity.Member;
import com.yu.rentalpic.dao.RentalPicRepository;
import com.yu.rentalpic.entity.RentalPic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RentalPicServiceImpl implements RentalPicService {

    @Autowired //自動裝配
    private RentalPicRepository repository;

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
    public List<RentalPic> searchRentalPics(Map<String, Object> map) {
        if (map.isEmpty()) {
            return repository.findAll();
        }

        Integer rentalPicNo = null;
        Integer rentalNo = null;
        byte[] rentalFile = null;

        if (map.containsKey("rentalPicNo")) {
            rentalPicNo = (Integer) map.get("rentalPicNo");
        } else if (map.containsKey("rentalNo")) {
            rentalNo = (Integer) map.get("rentalNo");
        } else if (map.containsKey("rentalFile")) {
            rentalFile = (byte[]) map.get("rentalFile");
        }

        return repository.searchRentalPics(rentalPicNo, rentalNo, rentalFile);
    }

    //修改照片
    @Override
    public void updatePicture(RentalPic rentalPic, byte[] rentalFile) {
        repository.updateRentalFileById(rentalPic.getRentalPicNo(), rentalFile);
    }
}
