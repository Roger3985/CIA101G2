package com.roger.report.service.impl;

import com.roger.member.entity.Member;
import com.roger.report.entity.Report;
import com.roger.report.repository.ReportRepository;
import com.roger.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    ReportRepository reportRepository;

    /**
     * 添加新的回覆檢舉。
     */
    @Override
    public void addReport(Report report) {
        report.setReportTime(Timestamp.valueOf(LocalDateTime.now()));
        reportRepository.save(report);
    }

    /**
     * 獲取指定檢舉文章編號的回覆檢舉。
     */
    @Override
    public Report getOneReport(Integer reportNo) {

        // 使用 reportRepository 查找給定的 reportNo
        Optional<Report> optional = reportRepository.findById(reportNo);

        // 返回查找結果
        return optional.orElse(null);
    }

    /**
     * 查找所有回覆檢舉（Report）物件。
     */
    @Override
    public List<Report> findAll() {
        return reportRepository.findAll();
    }


    /**
     * 根據會員編號查找與該會員相關的回覆檢舉報告。
    */
    @Override
    public Report findByMemberMemNo(Integer memNo) {
        List<Report> report = reportRepository.findByMemberMemNo(memNo);
        if (report != null && !report.isEmpty()) {
            // 返回列表中的第一個回覆檢舉
            return report.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Report> findByReportType(Byte reportType) {
        return reportRepository.findByReportType(reportType);
    }

    /**
     * 根據 reportNo 查找 Report 物件。
     */
    @Override
    public Report findReportByReportNo(Integer reportNo) {
        return reportRepository.findReportByReportNo(reportNo);
    }

    /**
     * 獲取所有回覆檢舉列表。
     */
    @Override
    public List<Report> getAll() {
        return reportRepository.findAll();
    }

    /**
     * 根據指定的會員獲取該會員提交的所有回覆檢舉。
     */
    @Override
    public List<Report> getAllByMem(Member member) {
        return reportRepository.findByMember(member);
    }

    /**
     * 更新給定的 `Report` 物件。
     */
    @Override
    public Report edit(Report newData) {
        return reportRepository.save(newData);
    }

    /**
     * 處理與指定檢舉編號相關的已處理檢舉。
     */
    @Override
    public void noTypeReport(Integer reportNo) {
        Report data = reportRepository.findReportByReportNo(reportNo);
        Byte reportType = Byte.valueOf("0");
        reportRepository.updateReportByReportType(reportNo, reportType);
    }

    /**
     * 根據會員編號和回覆檢舉類型更新回覆檢舉狀態。
     */
    @Override
    @Transactional
    public void updateReportByReportType(Integer memNo, Byte reportType) {
        try {
            // 使用 JPQL 查詢並更新回覆檢舉的類型
            reportRepository.updateReportByReportType(memNo, reportType);
        } catch (Exception e) {
            // 處理可能發生的異常，例如:資料庫錯誤
            e.printStackTrace();
            // 以下添加自定義的異常處理
        }
    }
}
