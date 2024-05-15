document.addEventListener("DOMContentLoaded", function() {
    // 檢查當前頁面是否有設置麵包屑
    const currentBreadcrumb = document.querySelector('[data-breadcrumb]');
    if (!currentBreadcrumb) {
        // 如果沒有，清空 sessionStorage 中的麵包屑
        sessionStorage.removeItem('breadcrumb');
        sessionStorage.removeItem('breadcrumbUrls');
    } else {
        // 頁面載入後，設置麵包屑
        setBreadcrumbFromSession();
    }

    // 為所有包含 data-breadcrumb 屬性的鏈接添加點擊事件監聽
    document.querySelectorAll('[data-breadcrumb]').forEach(item => {
        item.addEventListener('click', function(e) {
            e.preventDefault();
            // 保存麵包屑到 sessionStorage
            sessionStorage.setItem('breadcrumb', this.getAttribute('data-breadcrumb'));
            sessionStorage.setItem('breadcrumbUrls', this.getAttribute('data-breadcrumb-url'));
            // 導航到鏈接
            window.location.href = this.href;
        });
    });
});

function setBreadcrumbFromSession() {
    const path = sessionStorage.getItem('breadcrumb');
    const urls = sessionStorage.getItem('breadcrumbUrls');
    if (path && urls) {
        updateBreadcrumb(path, urls);
    }
}

function updateBreadcrumb(path, urls) {
    const breadcrumb = document.querySelector('.breadcrumb');
    breadcrumb.innerHTML = '';
    const paths = path.split(' - ');
    const urlArray = urls.split(' - ');

    paths.forEach((p, index) => {
        const li = document.createElement('li');
        li.className = 'breadcrumb-item';

        if (index === paths.length - 1) {
            li.classList.add('active');
            li.setAttribute('aria-current', 'page');
            li.textContent = p;
        } else {
            const a = document.createElement('a');
            a.href = urlArray[index];
            a.textContent = p;
            li.appendChild(a);
        }

        breadcrumb.appendChild(li);

        // 添加分隔符
        if (index < paths.length - 1) {
            const separator = document.createElement('span');
            separator.textContent = ' - ';
            breadcrumb.appendChild(separator);
        }
    });
}
