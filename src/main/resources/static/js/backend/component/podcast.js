var stompClient = null;

function connect() {
    var socket = new SockJS('/admin');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('已連接: ' + frame);
        subscribeToTopic();
    });
}

function subscribeToTopic() {
    // 使用 AJAX 獲取使用者職位資訊
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
                showMessage(message.body);
            });
        });
}

function sendMessage() {
    var message = "來自客戶端的訊息";
    stompClient.send("/company/sendMessage", {}, message);
}

function showMessage(message) {
    var alertsDiv = document.getElementById('alerts');
    var alertCounter = document.getElementById('alert-counter');
    var alertItem = document.createElement('a');
    alertItem.className = "dropdown-item d-flex align-items-center";
    alertItem.href = "#";
    alertItem.innerHTML = `
        <a class="dropdown-item d-flex align-items-center" href="#">
            <div class="mr-3">
                <div class="icon-circle bg-primary">
                    <i class="fas fa-file-alt text-white"></i>
                </div>
            </div>
            <div>
                <div class="small text-gray-500">Just now</div>
                <span class="font-weight-bold">${message}</span>
            </div>
        </a>
    `;
    alertsDiv.insertBefore(alertItem, alertsDiv.firstChild);
    // Update alert counter
    var count = parseInt(alertCounter.textContent);
}

connect();
