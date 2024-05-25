window.alert = function(name) {
    document.getElementById('alertMsgModalBody').innerText = name;
    $('#alertMsgModal').modal('show');
};

$(document).ready(function() {
    // 檢查 URL 中是否有錯誤訊息參數
    const urlParams = new URLSearchParams(window.location.search);
    const errorMsg = urlParams.get('error');
    const successMsg = urlParams.get('success');

    if (errorMsg) {
        alert(decodeURIComponent(errorMsg));
        // 清除URL中的error參數
        urlParams.delete('error');
        window.history.replaceState(null, null, '?' + urlParams.toString() + window.location.hash);
    }

    if (successMsg) {
        alert(decodeURIComponent(successMsg));
        // 清除URL中的success參數
        urlParams.delete('success');
        window.history.replaceState(null, null, '?' + urlParams.toString() + window.location.hash);
    }

    // 當模態框顯示時，按下 Enter 鍵觸發「確定」按鈕的點擊事件
    $('#alertMsgModal').on('shown.bs.modal', function() {
        $(document).on('keydown', function(event) {
            if (event.key === 'Enter') {
                $('#confirmButton').click();
            }
        });
    });

    // 當模態框隱藏時，移除 Enter 鍵的事件監聽
    $('#alertMsgModal').on('hidden.bs.modal', function() {
        $(document).off('keydown');
    });

    // 確定按鈕的點擊事件
    $('#confirmButton').on('click', function() {
        $('#alertMsgModal').modal('hide');
    });
});
