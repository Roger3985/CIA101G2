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

	//單筆查詢
	@Override
	public Rental findByNo(Integer rentalNo) {
//  添加邏輯判斷是否有查詢到資料.
		return repository.findByRentalNo(rentalNo); }

	//單筆查詢
	@Override
	public Rental findByRentalName(String rentalName) {
		return repository.findByRentalName(rentalName); }

	//金額由大到小
	@Override
	public List<Rental> findAllSortDESC() {
		List<Rental> sortListDESC = repository.findAll(Sort.by("rentalPrice").descending());
		return sortListDESC;
	}

	//金額由小到大
	@Override
	public List<Rental> findAllSort() {
		List<Rental> sortList = repository.findAll(Sort.by("rentalPrice"));
		return sortList;
	}

	//處理查詢(依租借品的尺寸)
	@Override
	public List<Rental> getRentalSize(Integer rentalSize) {
		return repository.findQueryByRentalSize(rentalSize);
	}

	//處理查詢(依租借品的顏色)
	@Override
	public List<Rental> getRentalColor(String rentalColor) {
		return repository.findQueryByRentalColor(rentalColor);
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

	//修改 (PK有值，save方法修改數據)
	@Override
	public Rental updateRental(Rental rental) {
		return repository.save(rental);
	}

	//單筆查詢
	@Override
	public Rental getOneRental(Integer rentalNo) {
		return repository.findByRentalNo(rentalNo);
	}


	@Override
	public List<Rental> getOneRental(Map<String, Object> getOneRentalMap) {
		return null;
	}

	//新增 (PK為null，save方法插入數據)
	@Override
	public Rental addRental(Rental rental) {
		return repository.save(rental);
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
		if (paramsMap == null || paramsMap.isEmpty()) {
			return repository.findAll(); // 如果没有任何条件，返回所有
		}

		//JPQL查詢語句
		StringBuilder jpql = new StringBuilder("SELECT re FROM Rental re WHERE 1=1");

		Map<String, Object> params = new HashMap<>();

		for (Map.Entry<String, String[]> entry : paramsMap.entrySet()) {
			String paramName = entry.getKey();
			String[] paramValues = entry.getValue();

			if (paramValues != null && paramValues.length > 0) {
				if ("rentalNo".equals(paramName)) {
					Integer rentalNo = Integer.parseInt(paramValues[0]);
					jpql.append(" AND re.rentalNo = :rentalNo");
					params.put("rentalNo", rentalNo);
				} else if ("rentalName".equals(paramName)) {
					String rentalName = paramValues[0];
					jpql.append(" AND re.rentalName LIKE :rentalName");
					params.put("rentalName", "%" + rentalName + "%");
				} else if ("rentalPrice".equals(paramName)) {
					BigDecimal rentalPrice = new BigDecimal(paramValues[0]);
					jpql.append(" AND re.rentalPrice = :rentalPrice");
					params.put("rentalPrice", rentalPrice);
				} else if ("rentalSize".equals(paramName)) {
					Integer rentalSize = Integer.parseInt(paramValues[0]);
					jpql.append(" AND re.rentalSize = :rentalSize");
					params.put("rentalSize", rentalSize);
				} else if ("rentalColor".equals(paramName)) {
					String rentalColor = paramValues[0];
					jpql.append(" AND re.rentalColor LIKE :rentalColor");
					params.put("rentalColor", "%" + rentalColor + "%");
				} else if ("rentalInfo".equals(paramName)) {
					String rentalInfo = paramValues[0];
					jpql.append(" AND re.rentalInfo LIKE :rentalInfo");
					params.put("rentalInfo", "%" + rentalInfo + "%");
				} else if ("rentalStat".equals(paramName)) {
					Byte rentalStat = Byte.parseByte(paramValues[0]);
					jpql.append(" AND re.rentalStat = :rentalStat");
					params.put("rentalStat", rentalStat);
				}
			}
		}
		TypedQuery<Rental> query = entityManager.createQuery(jpql.toString(), Rental.class);
		// 设置参数
		for (Map.Entry<String, Object> rentalEntry : params.entrySet()) {
			query.setParameter(rentalEntry.getKey(), rentalEntry.getValue());
		}
		// 执行查询
		return query.getResultList();
	}


}