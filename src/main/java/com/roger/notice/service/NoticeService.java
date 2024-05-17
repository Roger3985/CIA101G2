package com.roger.notice.service;

import com.roger.member.entity.Member;
import com.roger.notice.entity.Notice;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public interface NoticeService {

    /**
     * 將新的通知添加到資料庫中。
     *
     * @param notice 要添加的 Notice 實例，包含通知的相關信息。
     * @return 新添加的 Notice 實例，帶有更新後的 ID 和其他資料庫生成的屬性。
     */
    public Notice addNotice(Notice notice);


    /**
     * 更新現有的通知。
     * 該方法根據傳入的 Notice 物件更新現有的通知。在更新過程中，該方法將更改現有通知的屬性以匹配傳入的 notice。
     *
     * @param notice 要更新的 Notice 物件，包含要更新的通知的相關信息。
     * @return 更新後的 Notice 物件，反映了所做的更改。
     */
    public Notice updateNotice(Notice notice);

    /**
     * 根據給定的 Member 查找對應的 Notice。
     * 該方法根據傳入的 Member 物件查找對應的 Notice 實例。返回的 Notice 實例可能是與
     * 該 Member 相關的最新通知或其他特定條件下的通知。
     *
     * @param member 傳入的 Member 物件，用於查找相關的 Notice。
     * @return Notice 與指定 Member 相關的 Notice 實例。
     *         如果沒有找到符合條件的記錄，則返回 null。
     */
    Notice getOneByMember(Member member);

    /**
     * 根據給定的 motNo 查找對應的 Notice 實例。
     * 該方法根據傳入的 motNo 查找對應的 Notice 實例。
     *
     * @param motNo 傳入的 motNo，用於查找對應的 Notice 實例。
     * @return Notice 與指定 motNo 相關的 Notice 實例。
     *         如果沒有找到符合條件的記錄，則返回 null。
     */
    Notice getOneNotice(Integer motNo);

    /**
     * 從資料庫中查找所有 Notice 實例。
     * 這個方法使用 `noticeRepository` 來執行查詢，並返回所有 NoticeVO 實例的列表。
     *
     * @return List<NoticeVO> 包含所有 NoticeVO 實例的列表。
     *         如果沒有記錄，則返回空列表。
     */
    public List<Notice> getAll();

    /**
     * 根據會員編號查詢所有通知。
     *
     * @param memNo 會員編號
     * @return 該會員所有通知的列表
     */
    public List<Notice> findNoticesByMemberMemNo(Integer memNo);

    /**
     * 根據提供的條件查詢通知。
     *
     * @param map 包含查詢條件的映射。
     * @return 符合條件的通知列表。
     */
    public List<Notice> getByAttributes(Map<String, Object> map);
}
