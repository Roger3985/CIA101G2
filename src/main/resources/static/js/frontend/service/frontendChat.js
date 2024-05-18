const usernamePage = document.querySelector('#username-page');
const chatPage = document.querySelector('#chat-page');
const usernameForm = document.querySelector('#usernameForm');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-messages');

// 取得使用者名稱
const userNameInput = document.getElementById("username");
let userName;
let HostPoint;
let localhost;
let path;
let webCtx;
let endPointURL;

usernameForm.addEventListener("submit", function (e) {
    e.preventDefault();
    userName = userNameInput.value.trim();
    if (userName == "") {
        alert("請輸入姓名")
    } else {
        console.log(userName);
        HostPoint = `/chat/${userName}`;
        localhost = window.location.host;
        path = window.location.pathname;
        endPointURL = "ws://" + localhost + HostPoint;
        connect();
        // usernamePage.classList.add('hidden');
        // chatPage.classList.remove('hidden');
    }
});

// 設定websocket連線
// HostPoint = `/chat/${userName}`;
// localhost = window.location.host;
// path = window.location.pathname;
// webCtx = path.substring(0, path.indexOf("/", 1));
// endPointURL = "ws://" + localhost + HostPoint;
console.log("HostPoint=" + HostPoint);
console.log("localhost=" + localhost);
console.log("path=" + path);
console.log("webCtx=" + endPointURL);

document.getElementById("messageForm").addEventListener("submit", function (e) {
    e.preventDefault();
})

let webSocket;

function connect() {
    webSocket = new WebSocket(endPointURL);
    webSocket.onopen = function (event) {
        console.log("我在connect: HostPoint=" + HostPoint);
        console.log("我在connect: endPointURL=" + endPointURL);
        console.log("frontend connect Suceess!");
        connection = true;
        let jsonobj = {
            type: "stateA", sender: `${userName}`, receiver: "host",
        }
        webSocket.send(JSON.stringify(jsonobj));
    }

    webSocket.onmessage = function (event) {
        console.log("我收到後端的資料了" + event);
        console.log(event);
        var jsonObj = JSON.parse(event.data);
        var message = jsonObj.message;
        if (message !== undefined) {
            console.log("我收到後端的資料了" + message);
            const messageContainer = document.createElement('div');
            messageContainer.innerHTML = message;
            chatArea.appendChild(messageContainer);
            // chatArea.value = chatArea.value + message;
        }

    }
}


messageInput.addEventListener("keyup", function (e) {
    if (e.which == 13) {
        el_msg_btn.click();
    }
})

var el_msg_btn = document.getElementById("msg_btn");
el_msg_btn.addEventListener("click", function () {
    const messageContent = messageInput.value.trim();
    console.log(messageContent);
    if (messageContent == "") {
        alert("請輸入訊息");
    } else {
        const messageContainer = document.createElement('div');
        messageContainer.innerHTML = messageContent;
        chatArea.appendChild(messageContainer);
        messageInput.value = '';
        var jsonobj = {
            type: "chatMsgB", sender: `${userName}`, receiver: "host", message: messageContent,
            // timestamp: Date()
        }
        webSocket.send(JSON.stringify(jsonobj));
    }
})




