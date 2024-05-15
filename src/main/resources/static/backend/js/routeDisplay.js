// 1. 當頁面加載時可以在側邊欄將當前所在頁面的標題active,如有折疊元素則展開
// 2. 可以顯示當前頁面的麵包屑
document.addEventListener("DOMContentLoaded", function () {
    // 獲取當前頁面的路徑
    var currentPagePath = window.location.pathname;
    // 設置導覽的活動狀態和折疊元素的展開狀態
    var sidebarLinks = document.querySelectorAll('#accordionSidebar a.nav-link');
    sidebarLinks.forEach(function (link) {
        var linkPath = link.getAttribute('href');
        if (linkPath && currentPagePath.startsWith(linkPath)) { // 檢查當前頁面路徑是否以鏈接路徑開頭
            link.classList.add('active'); // 添加活動狀態類
            // 檢查是否存在父級折疊元素，如果有則展開
            var parentCollapse = link.closest('.collapse');
            if (parentCollapse) {
                parentCollapse.classList.add('show');
            }
        }
    });

    // 更新面包屑内容
    var breadcrumb = document.querySelector('.breadcrumb');
    if (breadcrumb) {
        var olElement = breadcrumb.querySelector('ol');
        if (olElement) {
            // 获取最後一個麵包屑
            var breadcrumbItems = olElement.querySelectorAll('.breadcrumb-item');
            var lastBreadcrumbItem = breadcrumbItems[breadcrumbItems.length - 1];
            if (lastBreadcrumbItem) {
                // 獲取最後一個麵包屑的文本內容
                var lastBreadcrumbText = lastBreadcrumbItem.textContent.trim();
                // 根據最後一個麵包屑的內容設置當前頁面的標題
                var pageTitle = (lastBreadcrumbText === '首頁') ? 'Home' : lastBreadcrumbText;
                document.title = pageTitle;
                // 創建新的麵包屑並設置當前頁面的標題
                var newBreadcrumbItem = document.createElement('li');
                newBreadcrumbItem.classList.add('breadcrumb-item', 'active');
                newBreadcrumbItem.textContent = pageTitle;
                olElement.appendChild(newBreadcrumbItem);
            }
        }
    }

});