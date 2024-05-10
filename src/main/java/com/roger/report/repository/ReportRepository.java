package com.roger.report.repository;

import com.roger.member.entity.Member;
import com.roger.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

    @Query("SELECT r FROM Report r WHERE r.member.memNo = :memNo")
    public List<Report> findByMemberMemNo(@Param("memNo") Integer memNo);

    @Query("SELECT r FROM Report r WHERE r.reportNo = :reportNo")
    Report findReportByReportNo(@Param("reportNo") Integer reportNo);

    /**
     * 查找與指定的相關的回覆檢舉列表。
     * 此方法根據給定的 `Member` 物件查找與該會員相關聯的所有 `Report` 物件。
     *
     * @param member 要查找其相關回覆檢舉的 `Member` 物件。
     * @return 與指定會員相關聯的 `Report` 列表。如果沒有找到任何回覆檢舉，則返回空列表。
     */
    public List<Report> findByMember(Member member);

    /**
     * 查找給定報告類型的所有報告。
     *
     * @param reportType 報告類型。
     * @return 與指定報告類型匹配的報告列表。
     */
    public List<Report> findByReportType(Byte reportType);

    /**
     * 更新指定檢舉回覆的檢舉編號。
     * 該方法根據提供的檢舉編號 `reportNo` 更新與該回覆檢舉狀態的檢舉編號為指定的 `reportType`。
     * 此方法使用 JPQL 查詢來更新回覆檢舉中的狀態 `reportType`，並根據 `reportNo` 進行篩選。
     * 該方法被注解為 `@Transactional`，表示在交易內執行。
     * 同時，使用 `@Modifying` 注解表示該方法執行資料修改操作。
     *
     * @param reportNo    要更新的檢舉編號。
     *                    用於篩選要更新的檢舉編號。
     * @param reportType  要更新的新的檢舉狀態。
     *                    該類型將被應用於與該回覆檢舉相關的檢舉狀態。
     */
    @Transactional
    @Modifying
    @Query("UPDATE Report r SET r.reportType = :reportType WHERE r.reportType = :reportNo")
    public void updateReportByReportType(@Param("reportNo") Integer reportNo, @Param("reportType") Byte reportType);


}

