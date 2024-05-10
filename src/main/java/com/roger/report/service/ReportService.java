package com.roger.report.service;

import com.roger.member.entity.Member;
import com.roger.report.entity.Report;

import java.util.List;

public interface ReportService {

    /**
     * 添加新的回覆檢舉。
     * 此方法接受一個 `Report` 物件，並將其添加到系統中。
     *
     * @param report 要添加的 `Report` 物件。
     */
    public void addReport(Report report);

    /**
     * 獲取指定檢舉文章編號的回覆檢舉。
     * 此方法使用 `reportRepository` 查找給定的 `reportNo`，
     * 並返回一個 `Optional<Report>`。
     *
     * @param reportNo 要查找的檢舉文章編號。
     * @return `Optional<Report>` 包含找到的 `Report` 物件，或 `Optional.empty()` 表示找不到。
     */
    public Report getOneReport(Integer reportNo);

    /**
     * 查找所有回覆檢舉（Report）物件。
     *
     * @return 回覆檢舉物件的列表。如果找不到任何匹配的回覆檢舉物件，則返回空列表。
     */
    public List<Report> findAll();

    /**
     * 根據會員編號查找與該會員回覆檢舉相關的報告。
     * 該方法接受會員編號作為參數，並返回與該會員編號相關的 `Report` 物件。
     * 如果找不到相關的報告，則可能返回 `null`。
     *
     * @param  memNo 會員編號，用於查找與該會員相關的報告。
     * @return 與該會員編號相關的 `Report` 物件；如果找不到，則返回 `null`。
     */
    public Report findByMemberMemNo(Integer memNo);

    public List<Report> findByReportType(Byte reportType);

    /**
     * 根據 reportNo 查找 Report 物件。
     *
     * @param reportNo 檢舉文章編號。
     * @return 對應的 Report 物件，如果不存在則返回 null。
     */
    public Report findReportByReportNo(Integer reportNo);

    /**
     * 獲取所有回覆檢舉列表。
     * 此方法返回系統中所有回覆檢舉的列表。
     *
     * @return 包含所有回覆檢舉的列表。如果沒有回覆檢舉，則返回一個空的回覆檢舉列表。
     */
    public List<Report> getAll();

    /**
     * 根據指定的會員獲取該會員提交的所有回覆檢舉。
     * 此方法接受一個 `Member` 物件，並返回該會員提交的所有回覆檢舉的列表。
     *
     * @param member 指定的會員物件，用於查找該會員提交的回覆檢舉。
     * @return 該會員提交的所有回覆檢舉的列表。如果該會員沒有提交任何回覆檢舉，則返回一個空列表。
     */
    public List<Report> getAllByMem(Member member);

    /**
     * 更新給定的 `Report` 物件。
     * 該方法接受要更新的 `Report` 物件，並返回更新後的 `Report` 物件。
     *
     * @param newData 要更新的 `Report` 物件，包含新的屬性值。
     * @return 更新後的 `Report` 物件。
     */
    public Report edit(Report newData);

    /**
     * 處理與指定檢舉編號相關的已處理檢舉。
     * 該方法根據給定的會員編號查找已處理的檢舉，並進行相關的處理操作。
     *
     * @param reportNo 要處理的檢舉編號。
     *                 該檢舉編號表示與該檢舉編號相關的已處理檢舉。
     */
    public void noTypeReport(Integer reportNo);

    /**
     * 根據會員編號和回覆檢舉類型更新回覆檢舉狀態。
     *
     * @param memNo       要更新的會員編號。
     * @param reportType  新的檢舉類型（0 為未處理，1 為已處理）。
     */
    public void updateReportByReportType(Integer memNo, Byte reportType);

}
