var stompClient = null;

function connect() {
    var socket = new SockJS('/admin');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('已連接: ' + frame);
        subscribeToTopics();
    });
}

function subscribeToTopics() {
    // 確保只訂閱一次
    if (!stompClient.subscribed) {
        stompClient.subscribed = true;
        stompClient.subscribe('/topic/parttime', function (message) {
            console.log("1");
            showMessage(message.body);
        });
        stompClient.subscribe('/topic/fulltime', function (message) {
            console.log("2");
            showMessage(message.body);
        });
        stompClient.subscribe('/topic/manager', function (message) {
            console.log("3");
            showMessage(message.body);
        });
        stompClient.subscribe('/topic/boss', function (message) {
            console.log("4");
            showMessage(message.body);
        });
    }
}

function showMessage(message) {
    var alertsDiv = document.getElementById('alerts');
    var alertItem = document.createElement('a');
    alertItem.className = "dropdown-item d-flex align-items-center";
    alertItem.href = "#";
    alertItem.innerHTML = `

             <a class="dropdown-item d-flex align-items-center" href="#">
                <div class="mr-3">
                   <div class="icon-circle bg-warning">
                     <i class="fas fa-exclamation-triangle text-white"></i>
                   </div>
                </div>
                <div>
                    <div class="small text-gray-500">December 2, 2019</div>
                        Spending Alert: We've noticed unusually high spending for your account.
                </div>
             </a>
            <div class="mr-3">
                <div class="icon-circle bg-primary">
                    <i class="fas fa-file-alt text-white"></i>
                </div>
            </div>
            <div>
                <div class="small text-gray-500">剛剛</div>
                <span class="font-weight-bold">${message}</span>
            </div>
        `;
    alertsDiv.insertBefore(alertItem, alertsDiv.childNodes[2]);
}

connect();

// function sendMessage() {
//     var message = "來自客戶端的訊息";
//     stompClient.send("/company/sendMessage", {}, message);
// }
//
// function showMessage(message) {
//     var messagesDiv = document.getElementById('messages');
//     var messageElement = document.createElement('p');
//     messageElement.textContent = message;
//     messagesDiv.appendChild(messageElement);
// }
//
// connect();