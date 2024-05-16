const usernamePage = document.querySelector('#username-page');
const chatPage = document.querySelector('#chat-page');
const usernameForm = document.querySelector('#usernameForm');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-messages');

// 設定websocket連線
const HostPoint = `/chat/host`;
const localhost = window.location.host;
const path = window.location.pathname;
const webCtx = path.substring(0, path.indexOf("/", 1));
const endPointURL = "ws://" + localhost + HostPoint;
console.log("HostPoint=" + HostPoint);
console.log("localhost=" + localhost);
console.log("path=" + path);
console.log("webCtx=" + endPointURL);
console.log("endPointURL=" + endPointURL);

const msgBody = document.querySelector("#chat-area");
let webSocket;

function connect() {
    webSocket = new WebSocket(endPointURL);
    webSocket.onopen = function (event) {
        console.log("backend connect Suceess!");
    }

    webSocket.onmessage = function (event) {
        console.log("我收到後端的資料了" + event);
        var jsonObj = JSON.parse(event.data);
        var message = jsonObj.message;
        console.log("我收到後端的資料了" + message);
        const messageContainer = document.createElement('div');
        messageContainer.innerHTML = message;
        chatArea.appendChild(messageContainer);
        // chatArea.value = chatArea.value + message;
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
            message: messageContent
        }
        webSocket.send(JSON.stringify(jsonobj))
    }
})
// function buildMessage(data){
//     const messageContainer = document.createElement('div');
//     messageContainer.classList.add('message');
//     let jsonObj =data;
//     let showMsg = jsonObj.message;
//     let time = jsonObj
// }


