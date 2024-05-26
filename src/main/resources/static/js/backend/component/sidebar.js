document.addEventListener('DOMContentLoaded', function() {
    // 查找所有具有 .dropright 類的元素
    document.querySelectorAll('.dropright').forEach(function(dropright) {
        dropright.addEventListener('mouseenter', function() {
            this.querySelector('.dropdown-menu').style.display = 'block';
            setTimeout(() => {
                this.querySelector('.dropdown-menu').style.opacity = '1';
            }, 0);
        });

        dropright.addEventListener('mouseleave', function() {
            this.querySelector('.dropdown-menu').style.opacity = '0';
            setTimeout(() => {
                this.querySelector('.dropdown-menu').style.display = 'none';
            }, 300);
        });
    });
});
