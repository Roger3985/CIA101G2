package com.roger.notice.entity.uniqueAnnotation;

import javax.validation.groups.Default;

public interface Create extends Default {
    // 會先抓預設的NotNull等再抓自創類別，要的再放進去Create類別
}
