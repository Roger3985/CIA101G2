package com.yu.rentalpic.service;

import com.roger.member.entity.Member;
import com.yu.rentalpic.dao.RentalPicRepository;
import com.yu.rentalpic.entity.RentalPic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RentalPicServiceImpl implements RentalPicService {

    @Autowired //自動裝配
    private RentalPicRepository repository;
    @Autowired
    @Qualifier("rentalPic")
    private RedisTemplate<String, String> rentalPicRedisTemplate;


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


//----------------------------------------------------------------------------------------------------------------------
//主要為Redis使用

    //新增、修改資料
    public void saveRentalPicToRedis(String rentalNo, String rentalPicNo, byte[] rentalFile) {
        String base64EncodedImage = Base64.getEncoder().encodeToString(rentalFile);
        rentalPicRedisTemplate.opsForValue().set("rentalPic:" + rentalNo + ":" + rentalPicNo, base64EncodedImage);
    }

    //取得資料
    public String getRentalPicFromRedis(String rentalNo, String rentalPicNo) {
        return rentalPicRedisTemplate.opsForValue().get("rentalPic:" + rentalNo + ":" + rentalPicNo);
    }

    //取得所有資料
    public Map<String, String> getAllRentalPicsFromRedis(String rentalNo, List<String> rentalPicNos) {
        Map<String, String> rentalPics = new HashMap<>();
        for (String rentalPicNo : rentalPicNos) {
            String key = "rentalPic:" + rentalNo + ":" + rentalPicNo;
            String base64EncodedImage = rentalPicRedisTemplate.opsForValue().get(key);
            if (base64EncodedImage != null) {
                rentalPics.put(rentalPicNo, base64EncodedImage);
            }
        }
        return rentalPics;
    }
}
