document.addEventListener("DOMContentLoaded", function() {
    // 页面加载完毕后，设置麵包屑
    setBreadcrumbFromSession();

    // 为所有包含 data-breadcrumb 属性的链接添加点击事件监听
    document.querySelectorAll('[data-breadcrumb]').forEach(item => {
        item.addEventListener('click', function(e) {
            // 防止链接默认动作
            e.preventDefault();
            // 保存麵包屑到 sessionStorage
            sessionStorage.setItem('breadcrumb', this.getAttribute('data-breadcrumb'));
            // 导航到链接
            window.location.href = this.href;
        });
    });
});

function setBreadcrumbFromSession() {
    const path = sessionStorage.getItem('breadcrumb');
    if (path) {
        updateBreadcrumb(path);
    }
}

function updateBreadcrumb(path) {
    const breadcrumb = document.querySelector('.breadcrumb');
    breadcrumb.innerHTML = ''; // 清空现有麵包屑
    const paths = path.split(' > ');
    paths.forEach((p, index) => {
        const li = document.createElement('li');
        li.className = 'breadcrumb-item';
        if (index === paths.length - 1) {
            li.classList.add('active'); // 当前页面
            li.setAttribute('aria-current', 'page');
        }
        li.textContent = p;
        breadcrumb.appendChild(li);
    });
}
