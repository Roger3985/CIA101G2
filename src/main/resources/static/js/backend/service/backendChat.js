const usernamePage = document.querySelector('#username-page');
const chatPage = document.querySelector('#chat-page');
const usernameForm = document.querySelector('#usernameForm');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-messages');
const memListArea = document.querySelector('#users-list-container');

document.getElementById("messageForm").addEventListener("submit", function (e) {
    e.preventDefault();
})


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

let webSocket;
let memName;

function connect() {
    webSocket = new WebSocket(endPointURL);
    webSocket.onopen = function (event) {
        console.log("backend connect Suceess!");

    }

    webSocket.onmessage = function (event) {
        console.log("我收到後端的資料了" + event);
        console.log(event);
        var jsonObj = JSON.parse(event.data);
        var pkType = jsonObj.type;
        console.log(pkType);


        if (pkType === "stateA") {
            var userlist = jsonObj.userList; //會取到含有host的清單
            console.log("userlist=> " + userlist);
            if (userlist.length != 0) {
                var memlist = userlist.filter(e => !e.includes("host"));
                console.log(memlist);
            }
            const connectedUsersList = document.getElementById('connectedUsers');
            connectedUsersList.innerHTML = '';
            for (let i = 0; i < memlist.length; i++) {
                console.log(memlist[i]);
                const memListContainer = document.createElement('li');
                memListContainer.innerHTML = memlist[i];
                connectedUsersList.appendChild(memListContainer);
                memListContainer.addEventListener("click", function (e) {
                    alert("hi" + memlist[i]);
                    memName = memlist[i];
                    let userReplying_el = document.querySelector(".username-replying");
                    userReplying_el.innerHTML = memName;
                    chatArea.innerHTML = '';
                });
            }
        } else {
            let message = jsonObj.message;
            const messageContainer = document.createElement('div');
            messageContainer.innerHTML = message;
            chatArea.appendChild(messageContainer);
        }
    }
}

messageInput.addEventListener("keyup", function (e) {
    // console.log(e.which);
    if (e.which == 13) {
        el_msg_btn.click();
    }
});

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
            type: "chatMsgB",
            sender: "host",
            receiver: memName,
            message: messageContent,
            // timestamp: Date()
        }
        webSocket.send(JSON.stringify(jsonobj))
    }
});


