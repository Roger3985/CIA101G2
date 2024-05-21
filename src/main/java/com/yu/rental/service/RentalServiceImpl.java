package com.yu.rental.service;

import com.yu.rental.dao.RentalRepository;
import com.yu.rental.entity.Rental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RentalServiceImpl implements RentalService {

	@Autowired //自動裝配
	private RentalRepository repository;

	/**
	 * PersistenceContext注解用于注入一个EntityManager对象，
	 * 使得我们可以在RentalService类中使用这个entityManager对象执行持久化操作，例如保存、更新、删除实体对象，以及执行JPQL查询等。
	 */
	@PersistenceContext
	private EntityManager entityManager;

	//單筆查詢(rentalNo)
	@Override
	public Rental findByNo(Integer rentalNo) {
		return repository.findByRentalNo(rentalNo); }

	//單類查詢(rentalCatNo)
	@Override
	public List<Rental> findByRentalCategoryRentalCatNo(Integer rentalCatNo) {
		return repository.findByRentalCategoryRentalCatNo(rentalCatNo);
	}

	//單筆查詢(String rentalName)
	@Override
	public Rental findByRentalName(String rentalName) {
		return repository.findByRentalName(rentalName); }

	//處理查詢(依租借品的尺寸)
	@Override
	public List<Rental> getRentalSize(Integer rentalSize) {
		return repository.findQueryByRentalSize(rentalSize);
	}

	//處理查詢(依租借品的顏色)
	@Override
	public Rental getRentalColor(String rentalColor) {
		return repository.findQueryByRentalColor(rentalColor);
	}

	//處理查詢(依租借品的狀態)
	@Override
	public List<Rental> findByStat(Byte rentalStat) {
		return repository.findQueryByRentalStat(rentalStat);
	}

	//關鍵字查詢(依租借品的名稱 "模糊查詢")
	@Override
	public List<Rental> getRentalName(String rentalName) {
		return repository.findQueryByRentalName(rentalName);
	}

	//全部查詢(Rental)
	@Override
	public List<Rental> findAll() {
		return repository.findAll();
	}


	//金額由大到小 (取得租借清單，以價格的降冪後返回)
	@Override
	public List<Rental> findAllSortDESC() {
		return repository.findAll(Sort.by("rentalPrice").descending());
	}

	//金額由小到大 (取得租借清單，以價格的升冪後返回)
	@Override
	public List<Rental> findAllSort() {
		return repository.findAll(Sort.by("rentalPrice").ascending());
	}


//----------------------------------------------------------------------------------------------------------------------
	//主要為後端使用：增查改

	//新增 (PK為null，save方法插入數據)
	@Override
	public Rental addRental(Rental rental) {
		return repository.save(rental);
	}

	//修改 (PK有值，save方法修改數據)
	@Override
	public Rental updateRental(Rental rental) {
		return repository.save(rental);
	}

	//單筆查詢(List集合)
	@Override
	public List<Rental> getOneRental(Map<String, Object> getOneRentalMap) {
		return null;
	}



	/**
	 * 根據給定的查詢條件搜尋租賃資訊並傳回結果清單。
	 * Map中儲存了請求參數的鍵值，鍵是參數的名稱，值是參數的值數組
	 *先檢查參數映射是否為空，如果為空，則傳回所有租賃資訊。
	 *在迭代參數映射時，根據參數的不同條件建立對應的查詢條件，並將參數值綁定到查詢中。
	 *
	 * 根據參數的不同條件動態地拼接查詢條件，並使用參數綁定來防止 SQL 注入攻擊。
	 * @return 執行該查詢並傳回結果清單。
	 */
	//複合查詢 (處理多個參數值)
	@Override
	public List<Rental> searchRentals(Map<String, Object> map) {

		if (map.isEmpty()) {
			return repository.findAll();
		}

		Integer rentalNo = null;
		Integer rentalCatNo = null;
		String rentalName = null;
		BigDecimal rentalPrice = null;
		Integer rentalSize = null;
		String rentalColor = null;
		String rentalInfo = null;
		Byte rentalStat = null;

		if (map.containsKey("rentalNo")) {
			rentalNo = (Integer) map.get("rentalNo");
		} else if (map.containsKey("rentalCatNo")) {
			rentalCatNo = (Integer) map.get("rentalCatNo");
		} else if (map.containsKey("rentalName")) {
			rentalName = (String) map.get("rentalName");
		} else if (map.containsKey("rentalPrice")) {
			rentalPrice = (BigDecimal) map.get("rentalPrice");
		} else if (map.containsKey("rentalSize")) {
			rentalSize = (Integer) map.get("rentalSize");
		} else if (map.containsKey("rentalColor")) {
			rentalColor = (String) map.get("rentalColor");
		} else if (map.containsKey("rentalInfo")) {
			rentalInfo = (String) map.get("rentalInfo");
		} else if (map.containsKey("rentalStat")) {
			rentalStat = (Byte) map.get("rentalStat");
		}

		return repository.searchRentals(rentalNo, rentalCatNo, rentalName, rentalPrice, rentalSize, rentalColor, rentalInfo, rentalStat);
	}




}