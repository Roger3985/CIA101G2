var stompClient = null;
var alerts = [];

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
                showMessage(message.body, true); // 新消息，isNew 設置為 true
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
                let message = msg.message;
                if (!message.isRead) {
                    unreadCount++;
                }
                showMessage(message, !message.isRead, msg.id); // 传递消息ID
            });
            document.getElementById('alert-counter').textContent = unreadCount;
        });
});

// 監聽通知圖標點擊事件，標記所有消息為已讀
document.getElementById('alertsDropdown').addEventListener('click', function() {
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
});

// window.addEventListener('beforeunload', function () {
//     console.log("方法執行");
//
//     // 測試訊息
//     const testAlert = [{
//         admNo: 0,
//         admName: '測試名稱',
//         titleNo: 1,
//         message: '測試訊息',
//         messageTime: new Date().toISOString(),
//         isRead: false
//     }];
//
//     console.log("發送測試訊息:", JSON.stringify(testAlert));
//
//     const data = new Blob([JSON.stringify(testAlert)], { type: 'application/json' });
//     navigator.sendBeacon('/backend/saveMessages', data);
//
//     if (alerts.length > 0) {
//         console.log('發送alerts:', JSON.stringify(alerts));
//         const alertData = new Blob([JSON.stringify(alerts)], { type: 'application/json' });
//         navigator.sendBeacon('/backend/saveMessages', alertData);
//     }
// });

window.addEventListener('beforeunload', function () {
    console.log("方法執行");

    // 測試訊息
    const testAlert = {
        admNo: 0,
        admName: '測試名稱',
        titleNo: 1,
        message: '測試訊息',
        messageTime: new Date().toISOString(),
        isRead: false
    };

    console.log("發送測試訊息:", JSON.stringify(testAlert));

    fetch('/backend/saveMessages', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(testAlert)
    }).then(response => {
        console.log('收到回應:', response);
        return response.json();
    }).then(data => {
        console.log('回應數據:', data);
        if (data.status === "success") {
            console.log('訊息已保存');
        } else {
            console.error('保存訊息失敗');
        }
    }).catch(error => {
        console.error('錯誤:', error);
    });

    if (alerts.length > 0) {
        console.log('Sending alerts:', alerts);
        fetch('/backend/saveMessages', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(alerts)
        }).then(response => {
            console.log('Response received:', response);
            return response.json();
        }).then(data => {
            if (data.status === "success") {
                console.log('訊息已保存');
            } else {
                console.error('保存訊息失敗');
            }
        }).catch(error => {
            console.error('Error:', error);
        });
    }
});

function showMessage(message, isNew, id) {
    var alertsDiv = document.getElementById('alerts');
    var alertCounter = document.getElementById('alert-counter');

    var parsedMessage = JSON.parse(message);
    var admNo = parsedMessage.admNo;
    var messageText = parsedMessage.message;
    var time = parsedMessage.messageTime;

    var alertItem = document.createElement('a');
    alertItem.className = "dropdown-item d-flex align-items-center";
    alertItem.href = "#";
    alertItem.innerHTML = `
        <div class="mr-3">
            <div class="icon-circle bg-primary">
                <img class="img-profile" src="/backend/administrator/DBGifReader?admNo=${admNo}" alt="Profile Picture">
            </div>
        </div>
        <div>
            <div class="small text-gray-500">${time}</div>
            <span class="${isNew ? 'font-weight-bold' : 'text-gray-500'}">${messageText}</span>
        </div>
    `;
    alertsDiv.insertBefore(alertItem, alertsDiv.firstChild);

    if (isNew) {
        var count = parseInt(alertCounter.textContent) || 0;
        alertCounter.textContent = count + 1;
        alerts.push({id: id, admNo, messageText, time, isNew});
    } else {
        alerts.push({id: id, admNo, messageText, time, isNew: false});
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
        alert.isNew = false;
        return alert;
    });
}

connect();
