var stompClient = null;
var alerts = []; // 初始化alerts陣列

// 使用WebSocket連接後端伺服器
function connect() {
    var socket = new SockJS('/admin');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('已連接: ' + frame);
        subscribeToTopic();
    });
}

// 根據用戶職位訂閱相關佇列
function subscribeToTopic() {
    fetch('/backend/getUserTitle')
        .then(response => response.text())
        .then(userTitle => {
            var topic = '';
            switch (userTitle) {
                case '1':
                    topic = '/topic/employee';
                    break;
                case '2':
                    topic = '/topic/fullTime';
                    break;
                case '3':
                    topic = '/topic/manager';
                    break;
                case '4':
                    topic = '/topic/boss';
                    break;
                default:
                    console.error('未知的職位');
                    return;
            }
            stompClient.subscribe(topic, function (message) {
                console.log('收到消息: ' + message.body);
                // 從WebSocket接收的消息是JavaScript對象
                showMessage(JSON.parse(message.body), true); // 新消息，newMessage 設置為 true
            });
        });
}

// 頁面加載時獲取歷史消息並顯示
document.addEventListener('DOMContentLoaded', function () {
    fetch('/backend/getMessages')
        .then(response => response.json())
        .then(messages => {
            let unreadCount = 0;
            messages.forEach(msg => {
                if (!msg.readStat) {
                    unreadCount++;
                }
                showMessage(msg, !msg.readStat, msg.messageId);
            });
            document.getElementById('alert-counter').textContent = unreadCount;
        });
});

// 使用 blur 事件來檢測通知中心的關閉
document.getElementById('alertsDropdown').addEventListener('blur', function () {
    markAllAsRead();
    fetch('/backend/markAllAsRead', { method: 'POST' })
        .then(response => response.json())
        .then(data => {
            if (data.status === "success") {
                console.log('所有消息已標記為已讀');
            } else {
                console.error('標記消息為已讀失敗');
            }
        });
}, true); // 使用 true 表示在捕獲階段觸發事件

window.addEventListener('beforeunload', function () {
    console.log("方法執行");

    if (alerts.length > 0) {
        console.log('發送alerts:', JSON.stringify(alerts));
        fetch('/backend/saveMessages', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(alerts)
        }).then(response => {
            console.log('收到回應:', response);
            return response.json();
        }).then(data => {
            if (data.status === "success") {
                console.log('訊息已保存');
            } else {
                console.error('保存訊息失敗');
            }
        }).catch(error => {
            console.error('錯誤:', error);
        });
    }
});

function showMessage(message, newMessage, messageId) {
    var alertsDiv = document.getElementById('alerts');
    var alertCounter = document.getElementById('alert-counter');

    console.log("收到的消息對象：", message); // 檢查消息對象的結構

    // 如果message已經是對象，就不需要JSON.parse，直接使用
    var parsedMessage = typeof message === "string" ? JSON.parse(message) : message;

    // 提取數據，假設結構已經扁平化
    var messageId = parsedMessage.messageId || '';
    var admNo = parsedMessage.admNo || ''; // 確保字段存在，否則給個默認值
    var admName = parsedMessage.admName || '';
    var titleNo = parsedMessage.titleNo || '';
    var messageText = parsedMessage.message || '';
    var time = parsedMessage.messageTime || '';

    // 添加新通知到 alerts 陣列中
    alerts.push({
        messageId: messageId,
        admNo: admNo,
        admName: admName,
        titleNo: titleNo,
        message: messageText,
        messageTime: time,
        readStat: !newMessage
    });

    // 根據 messageTime 排序 alerts 陣列
    alerts.sort(function (a, b) {
        return new Date(b.messageTime) - new Date(a.messageTime);
    });

    // 清空現有的通知顯示
    alertsDiv.innerHTML = '';

    // 只顯示最近的 3 條通知
    var displayAlerts = alerts.slice(0, 3); // 顯示陣列的前 3 個元素
    displayAlerts.forEach(function(alert) {
        var alertItem = document.createElement('a');
        alertItem.className = "dropdown-item d-flex align-items-center";
        alertItem.href = "#";
        alertItem.innerHTML = `
            <div class="mr-3">
                <div class="icon-circle bg-primary">
                    <img class="img-profile" src="/backend/administrator/DBGifReader?admNo=${alert.admNo}" alt="Profile Picture">
                </div>
            </div>
            <div>
                <div class="small text-gray-500">${alert.messageTime}</div>
                <span class="${alert.readStat ? 'text-gray-500' : 'font-weight-bold'}">${alert.admName} : ${alert.message}</span>
            </div>
        `;
        alertsDiv.appendChild(alertItem);
    });

    if (newMessage) {
        var count = parseInt(alertCounter.textContent) || 0;
        alertCounter.textContent = count + 1;
    }
}

// 標記所有消息為已讀
function markAllAsRead() {
    var alertsDiv = document.getElementById('alerts');
    alertsDiv.querySelectorAll('.font-weight-bold').forEach(function (element) {
        element.classList.remove('font-weight-bold');
        element.classList.add('text-gray-500');
    });

    document.getElementById('alert-counter').textContent = '0';

    alerts = alerts.map(alert => {
        alert.readStat = true;
        return alert;
    });
}

connect();
