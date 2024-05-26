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

window.onload = function() {
    var usernameInput = document.getElementById('username');
    if (usernameInput.value) {
        // e.preventDefault();
        userName = userNameInput.value.trim();
        if (userName == "") {
            alert("請至頁面登入")
        } else {
            console.log(userName);
            HostPoint = `/chat/${userName}`;
            localhost = window.location.host;
            path = window.location.pathname;
            endPointURL = "ws://" + localhost + HostPoint;
            connect();
        }
    }
};


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
        // 請求在線會員清單
        let jsonobj = {
            type: "stateA", sender: `${userName}`, receiver: "host",
        }
        webSocket.send(JSON.stringify(jsonobj));
        setTimeout(3000);
        //  請求history
        let jsonobj_history = {
            type: "history", sender: `${userName}`, receiver: "host"
        }
        webSocket.send(JSON.stringify(jsonobj_history));
    }

    webSocket.onmessage = function (event) {
        console.log("我收到後端的資料了" + event);
        console.log(event);
        var jsonObj = JSON.parse(event.data);
        console.log(jsonObj);
        var pkType = jsonObj.type;
        console.log(pkType);
        var message = jsonObj.message;
        console.log(message);
        var msgTime = jsonObj.timestamp;
        if (pkType == "history") {
            var historyMsgs = JSON.parse(message);
            for (let i = 0; i < historyMsgs.length; i++) {
                var historyData = JSON.parse(historyMsgs[i]);
                var showMsg = historyData.message;
                var showMsgTime = historyData.timestamp;
                const messageContainer = document.createElement('div');
                messageContainer.classList.add('message');
                const messageTime = document.createElement('div');
                if (historyData.sender === userName) {
                    messageContainer.classList.add("sender");
                    messageTime.classList.add("senderTime");
                } else {
                    messageContainer.classList.add("receiver");
                    messageTime.classList.add("receiverTime");
                }
                const message_block = document.createElement('p');
                message_block.innerHTML = showMsg;
                messageContainer.appendChild(message_block);
                messageTime.innerHTML = showMsgTime;
                chatArea.appendChild(messageContainer);
                chatArea.appendChild(messageTime);
            }
            chatArea.scrollTop = chatArea.scrollHeight;
        }
        if (pkType == "chatMsgB") {
            console.log("我收到後端的資料了" + message);
            const messageContainer = document.createElement('div');
            messageContainer.classList.add('message');
            const messageTime = document.createElement('div');
            if (message.sender === userName) {
                messageContainer.classList.add("sender")
                messageTime.classList.add("senderTime");
            } else {
                messageContainer.classList.add("receiver");
                messageTime.classList.add("receiverTime");
            }
            const message_block = document.createElement('p');
            message_block.innerHTML = message;
            messageContainer.appendChild(message_block);
            messageTime.innerHTML = msgTime;
            chatArea.appendChild(messageContainer);
            chatArea.appendChild(messageTime);
            chatArea.scrollTop = chatArea.scrollHeight;
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
    let now = new Date();
    let nowMin = now.getMinutes()
    let nowMinStr;
    if (nowMin <= "9") {
        nowMinStr = "0" + nowMin;
    } else {
        nowMinStr = nowMin;
    }
    let nowStr = now.getFullYear() + "-" + (now.getMonth() + 1) + "-" + now.getDate() + " " + now.getHours() + ":" + nowMinStr;
    console.log(messageContent);
    console.log(nowStr);
    if (messageContent == "") {
        alert("請輸入訊息");
    } else {
        const messageContainer = document.createElement('div');
        messageContainer.classList.add('message')
        messageContainer.classList.add("sender")
        const message_block = document.createElement('p');
        message_block.innerHTML = messageContent;
        messageContainer.appendChild(message_block);
        const messageTime = document.createElement('div');
        messageTime.classList.add("senderTime");
        messageTime.innerHTML = nowStr;
        // messageContainer.appendChild(messageTime);
        chatArea.appendChild(messageContainer)
        chatArea.appendChild(messageTime);
        messageInput.value = '';
        var jsonobj = {
            type: "chatMsgB",
            sender: `${userName}`,
            receiver: "host",
            message: messageContent,
            timestamp: nowStr
        }
        webSocket.send(JSON.stringify(jsonobj));
    }
    chatArea.scrollTop = chatArea.scrollHeight;
});




