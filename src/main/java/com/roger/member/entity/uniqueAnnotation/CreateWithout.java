package com.roger.member.entity.uniqueAnnotation;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

/**
 * 創建一個新的驗證組合，排除 UniqueMemberAccount.class, UniqueMemberMobile.class, UniqueMemberMail.class
 */
import javax.validation.GroupSequence;
import javax.validation.groups.Default;

/**
 * 創建一個新的驗證組合，排除 UniqueMemberAccount.class, UniqueMemberMobile.class, UniqueMemberMail.class。
 */
public interface CreateWithout extends Default{
    // 此介面排除 UniqueMemberAccount.class, UniqueMemberMobile.class, UniqueMemberMail.class。
}



