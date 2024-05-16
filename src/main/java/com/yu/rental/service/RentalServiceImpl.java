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
	 * paramsMap中儲存了請求參數的鍵值，鍵是參數的名稱，值是參數的值數組
	 *先檢查參數映射是否為空，如果為空，則傳回所有租賃資訊。
	 *在迭代參數映射時，根據參數的不同條件建立對應的查詢條件，並將參數值綁定到查詢中。
	 *
	 * 根據參數的不同條件動態地拼接查詢條件，並使用參數綁定來防止 SQL 注入攻擊。
	 * @return 執行該查詢並傳回結果清單。
	 */
	//複合查詢 (使用"Map<String, String[]> paramsMap" 處理多個參數值)
	@Override
	public List<Rental> searchRentals(Map<String, String[]> paramsMap) {

		// 判斷是否有輸入條件，如果沒有給條件，就返回所有租借品資料
		if (paramsMap == null || paramsMap.isEmpty()) {
			return repository.findAll();
		}

		//JPQL查詢語句
		StringBuilder jpql = new StringBuilder("SELECT re FROM Rental re WHERE 1=1");

		Map<String, Object> params = new HashMap<>(); //建立Map<> 應用於從HTTP請求中取得參數


		for (Map.Entry<String, String[]> entry : paramsMap.entrySet()) {
			String paramName = entry.getKey();  //設立儲存key的變數
			String[] paramValues = entry.getValue();  //設立儲存Value的變數

			if (paramValues != null && paramValues.length > 0) {  //若參數不為空&長度大於0，才執行後續的處理
				if ("rentalNo".equals(paramName)) {   //判斷租借品類別編號。如果參數對應
					Integer rentalNo = Integer.parseInt(paramValues[0]); //參數轉換為 Integer 類型
					jpql.append(" AND re.rentalNo = :rentalNo"); //將對應的查詢語句加到 jpql 字串中
					params.put("rentalNo", rentalNo); //參數值添加到params Map中

//				} else if ("rentalCatNo".equals(paramName)) {  //判斷租借品類別編號
//					Integer rentalCatNo = Integer.parseInt(paramValues[0]);
//					jpql.append(" AND re.rentalCatNo = :rentalCatNo");
//					params.put("rentalCatNo", rentalCatNo);

				} else if ("rentalName".equals(paramName)) {
					String rentalName = paramValues[0];
					jpql.append(" AND re.rentalName LIKE :rentalName");  //使用模糊查詢Like
					params.put("rentalName", "%" + rentalName + "%");

				} else if ("rentalSize".equals(paramName)) {  //判斷租借品大小
					Integer rentalSize = Integer.parseInt(paramValues[0]);
					jpql.append(" AND re.rentalSize = :rentalSize");
					params.put("rentalSize", rentalSize);

				} else if ("rentalColor".equals(paramName)) {  //判斷租借品顏色
					String rentalColor = paramValues[0];
					jpql.append(" AND re.rentalColor LIKE :rentalColor");
					params.put("rentalColor", "%" + rentalColor + "%");  //使用模糊查詢Like

				} else if ("rentalStat".equals(paramName)) {  //判斷租借品狀態
					Byte rentalStat = Byte.parseByte(paramValues[0]);
					jpql.append(" AND re.rentalStat = :rentalStat");
					params.put("rentalStat", rentalStat);
				}
			}
		}

		//建立TypedQuery物件，使用jpql.toString() 將先前構建的JPQL字串轉換為具體的JPQL語句
		//指定了查詢的返回類型為 Rental.class (Rental 類型的物件)
		TypedQuery<Rental> query = entityManager.createQuery(jpql.toString(), Rental.class);

		//遍歷先前建構的params Map中的key & Value，使用query.setParameter()將參數值設置到查詢中
		for (Map.Entry<String, Object> rentalEntry : params.entrySet()) {
			query.setParameter(rentalEntry.getKey(), rentalEntry.getValue());
		}

		//使用query.getResultList()來執行查詢，返回查詢結果列表
		return query.getResultList();
	}




}