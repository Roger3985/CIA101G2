package com.roger.columnarticle.service;

import com.roger.columnarticle.entity.ColumnArticle;

import java.util.List;

public interface ColumnArticleService {

    /**
     * 查找所有專欄文章。
     *
     * @return 包含所有專欄文章的 List<ColumnArticle> 列表。
     */
    public List<ColumnArticle> findAll();

    /**
     * 獲取指定文章編號的文章專欄。
     * 此方法使用 `reportRepository` 查找給定的 `artNo`，
     * 並返回一個 `Optional<Report>`。
     *
     * @param artNo 要查找的文章編號。
     * @return `Optional<ColumnArticle>` 包含找到的 `ColumnArticle` 物件，或 `Optional.empty()` 表示找不到。
     */
    public ColumnArticle getOneColumnArticle(Integer artNo);

    /**
     * 更新專欄文章的資料。
     * 該方法接受一個新的專欄文章物件 'newData'，並接受該物件的資料更新現有的專欄文章。
     * 此方法返回更新的的專欄文章物件。
     *
     * @param newData 新的專欄文章物件，包含要更新的物件。
     * @return 更新後的專欄文章物件
     */
    public ColumnArticle edit(ColumnArticle newData);

    /**
     * 更新現有的專欄文章。
     * 該方法根據傳入的 ColumnArticle 物件更新現有的專欄文章。在更新過程中，該方法將更改現有專欄文章的屬性以匹配傳入的 notice。
     *
     * @param columnArticle 要更新的 ColumnArticle 物件，包含要更新的專欄文章的相關信息。
     * @return 更新後的 ColumnArticle 物件，反映了所做的更改。
     */
    public ColumnArticle updateColumnArticle(ColumnArticle columnArticle);

    /**
     * 根據文章編號查找專欄文章。
     * 該方法接受一個文章編號作為參數，並返回與該文章編號相關的專欄文章物件。
     *
     * @param artNo 文章編號。
     * @return 專欄文章物件，如果找不到則返回 'null'。
     */
    public ColumnArticle findColumnArticleByArtNo(Integer artNo);


    /**
     * 將文章添加到會員的收藏中。
     *
     * 此方法允許會員收藏特定文章。如果會員已經收藏過該文章，
     * 根據實現細節，此方法將處理該情況（例如，忽略，拋出異常等）。
     *
     * @param memNo 要收藏文章的會員編號（ID）
     * @param artNo 會員要收藏的文章編號（ID）
     * @return 如果文章成功添加到會員的收藏中則返回 true，
     *         否則返回 false（例如，如果該文章已經被收藏過）
     */
    public boolean columnArticleCollection(Integer memNo, Integer artNo);

    /**
     * 獲取指定文章的回應數量。
     *
     * @param artNo 專欄文章的ID
     * @return 回應數量
     */
    public int getResponseCount(Integer artNo);



}
