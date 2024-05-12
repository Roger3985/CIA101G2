package com.roger.member.repository;

import com.roger.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

    /**
     * 根據會員的電子郵件查詢會員。
     * 此方法使用 JpaRepository 方法 'findByMemMail' 會以屬性 'memMail' 為設置條件搜尋，根據會員的電子郵件查詢會員。
     *
     * @param memMail 會員的電子郵件。
     * @return 如果找到相應的會員，則返回該會員。
     */
    @Transactional
    public Member findByMemMail(String memMail);

    /**
     * 根據會員的帳號查詢會員。
     * 此方法使用 JpaRepository 方法 'findByMemAcc' 會以屬性 'memAcc' 為設置條件搜尋，根據會員的帳號查詢會員。
     *
     * @param memAcc 會員的帳號。
     * @return 如果找到相應的會員，則返回該會員。
     */
    @Transactional
    public Member findByMemAcc(String memAcc);

    /**
     * 根據會員的電話查詢會員。
     * 此方法使用 JpaRepository 方法 'findByMemMob' 會以屬性 'memMob' 為設置條件搜尋，根據會員的電話查詢會員。
     *
     * @param memMob 會員的電話。
     * @return 如果找到相應的會員，則返回該會員。
     */
    @Transactional
    public Member findByMemMob(String memMob);

    /**
     * 判斷指定的電子郵件是否存在於會員資料庫中。
     * 此方法使用 JpaRepository 的 'existsByMemMail'
     * 方法檢查會員資料庫中是否存在具有指定電子郵件的會員。
     *
     * @param memMail 要檢查的會員電子郵件。
     * @return 如果存在具有指定電子郵件的會員，則返回 true；否則返回 false。
     */
    @Transactional
    public boolean existsByMemMail(String memMail);

    /**
     * 判斷指定的會員帳戶是否存在於會員資料庫中。
     * 此方法使用 JpaRepository 的 'existsByMemAcc' 方法
     * 檢查會員資料庫中是否存在具有指定會員帳戶的會員。
     *
     * @param memAcc 要檢查的會員帳戶。
     * @return 如果存在具有指定會員帳戶的會員，則返回 true；否則返回 false。
     */
    @Transactional
    public boolean existsByMemAcc(String memAcc);

    /**
     * 判斷指定的會員手機號碼是否存在於會員資料庫中。
     * 此方法使用 JpaRepository 的 'existsByMemMob' 方法
     * 檢查會員資料庫中是否存在具有指定手機號碼的會員。
     *
     * @param memMob 要檢查的會員手機號碼。
     * @return 如果存在具有指定手機號碼的會員，則返回 `true`；否則返回 `false`。
     */
    public boolean existsByMemMob(String memMob);

//    @Transactional
//    @Modifying
//    @Query("UPDATE Member member SET member.memPic = :memPic WHERE member.memNo = :memNo")
//    public void updateMemPictureById(@Param("memNo") Integer memNo, @Param("memPic") byte[] memPic);

    /**
     * 更新會員圖片的方法。
     *
     * @param memNo 會員編號。
     * @param memPic 會員圖片的字節陣列。
     */
    @Transactional
    @Modifying
    @Query("UPDATE Member SET memPic = :memPic WHERE memNo = :memNo")
    public void updateMemPicById(@Param("memNo") Integer memNo, @Param("memPic") byte[] memPic);

    /**
     * 更新會員帳號狀態的方法。
     * 此方法接受會員編號和新的帳號狀態，並將會員的帳號狀態更新為提供的狀態。
     *
     * @param memNo 會員編號。
     * @param memStat 新的帳號狀態。
     */
    @Transactional
    @Modifying
    @Query("UPDATE Member SET memStat = :memStat WHERE memNo = :memNo")
    public void updateMemStatById(@Param("memNo") Integer memNo, @Param("memStat") Byte memStat);

    // 複雜查詢
    /**
     * 根據會員編號（memNo）查詢會員。
     * 此方法使用 Spring Data JPA 查找與給定會員編號相匹配的會員。
     * 如果找到匹配的會員，則返回一個 Optional 包含該會員實例；如果未找到匹配的會員，則返回空的 Optional。
     *
     * @param memNo 要查詢的會員編號。
     * @return 一個 Optional，可能包含與給定會員編號相匹配的會員。
     */
    Optional<Member> findMemberByMemNo(@Param("memNo") Integer memNo);


}
