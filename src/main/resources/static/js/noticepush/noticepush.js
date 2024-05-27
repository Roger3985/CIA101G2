// 建立一個函數來顯示 toast
function showToast(message, duration) {
    // 創建 toast 元素
    const toast = `
            <div class="toast" role="alert" aria-live="assertive" aria-atomic="true" data-bs-autohide="true" data-bs-delay="${duration}">
                <div class="toast-body">
                    ${message}
                </div>
            </div>
        `;

    // 將 toast 元素添加到 container 中
    $('#toastContainer').append(toast);

    // 初始化 toast
    const toastEl = new bootstrap.Toast($('.toast').last());

    // 顯示 toast
    toastEl.show();
}

// 在頁面加載完畢後，依序顯示推送通知
$(document).ready(function() {
    // 定義消息陣列
    const messages = [
        "歡迎你來到fallElove官網",
        "我們男女式禮服，歡迎挑選",
        "也歡迎加入我們的會員，這樣就可以進行一些優惠跟福利喔!"
    ];

    // 定義消息顯示間隔時間（毫秒）
    const interval = 5000; // 5秒

    // 遍歷消息陣列，依次顯示每條消息
    messages.forEach(function(message, index) {
        // 使用 setTimeout 來設置延遲顯示
        setTimeout(function() {
            // 呼叫 showToast 函數顯示推送通知
            showToast(message, 3000); // 每條消息顯示3秒後自動消失
        }, index * interval);
    });
});