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

// 設一個空物件用於追蹤未讀消息數量，
// 資料結構:{
// "memName1" : 3,
// "memName2" : 4,
// "memName3" : 0 }
let unreadMessage = {};


function connect() {
    webSocket = new WebSocket(endPointURL);
    webSocket.onopen = function (event) {
        console.log("backend connect Suceess!");

    }

    webSocket.onmessage = function (event) {
        console.log("收到後端的資料了" + event);
        console.log(event);
        var jsonObj = JSON.parse(event.data);
        console.log(jsonObj);
        var receiver = jsonObj.receiver;
        console.log(receiver);
        var message = jsonObj.message;
        var pkType = jsonObj.type;
        console.log(pkType);
        var msgTime = jsonObj.timestamp;
        console.log(msgTime);


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
                    // // 清除未讀訊息的數量
                    // unreadMessage[memName] = 0;
                    // updateUnreadMessagesDisplay(memListContainer, memName);
                    // 如果點到的會員有歷史訊息，則在這邊顯示
                    requestHisoryMsg(memName);
                });
            }
        } else if (pkType == "history") {
            var historyMsgs = JSON.parse(message);
            for (let i = 0; i < historyMsgs.length; i++) {
                var historyData = JSON.parse(historyMsgs[i]);
                var showMsg = historyData.message;
                var showMsgTime = historyData.timestamp;
                const messageContainer = document.createElement('div');
                const messageTime = document.createElement('div');
                if (historyData.sender === "host") {
                    messageContainer.classList.add("sender");
                    messageTime.classList.add("senderTime");
                } else {
                    messageContainer.classList.add("receiver");
                    messageTime.classList.add("receiverTime");
                }
                messageContainer.innerHTML = showMsg;
                messageTime.innerHTML = showMsgTime;
                chatArea.appendChild(messageContainer);
                chatArea.appendChild(messageTime);
            }
            chatArea.scrollTop = chatArea.scrollHeight;
        } else if (pkType === "chatMsgB") {
            // 過濾所有來自會員的訊息，僅接收當前聊天室的會員傳送訊息進來
            if (jsonObj.receiver === memName || jsonObj.sender === memName) {
                const messageContainer = document.createElement('div');
                const messageTime = document.createElement('div');
                messageContainer.classList.add("receiver");
                messageTime.classList.add("receiverTime");
                messageContainer.innerHTML = message;
                messageTime.innerHTML = msgTime;
                chatArea.appendChild(messageContainer);
                chatArea.appendChild(messageTime);
            // } else {
                // if (!unreadMessage[jsonObj.sender]) {
                //     unreadMessage[jsonObj.sender] = 0;
                // } else {
                    // let unreadMsgCount = unreadMessage[jsonObj.sender]++;
                    // const userItem = Array.from(document.querySelectorAll('#connectedUsers li')).find(li => li.innerHTML === jsonObj.receiver);
                    // if (userItem) {
                    //     updateUnreadMessagesDisplay(userItem, jsonObj.sender);
                    // }
                    // const showUnreadMsgCount = document.createElement('span');
                    // showUnreadMsgCount.innerHTML = unreadMsgCount;
                    // memListContainer.appendChild(showUnreadMsgCount);
                // }
            }
        }
    }
}

// function updateUnreadMessagesDisplay(element, memName) {
//     if (unreadMessages[memName] > 0) {
//         // element.innerHTML = `${username} (${unreadMessages[username]})`;
//         const showUnreadMsgCount = document.createElement('span');
//         showUnreadMsgCount.innerHTML = unreadMsgCount;
//         memListContainer.appendChild(showUnreadMsgCount);
//     } else {
//         element.innerHTML = memName;
//     }
// }


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
    let now = new Date();
    let nowMin = now.getMinutes()
    let nowMinStr;
    if (nowMin <= "9") {
        nowMinStr = "0" + nowMin;
    } else {
        nowMinStr = nowMin;
    }
    let nowStr = now.getFullYear() + "-" + (now.getMonth() + 1) + "-" + now.getDate() + " " + now.getHours() + ":" + nowMinStr;
    if (messageContent == "") {
        alert("請輸入訊息");
    } else {
        const messageContainer = document.createElement('div');
        messageContainer.innerHTML = messageContent;
        messageContainer.classList.add("sender")
        const messageTime = document.createElement('div');
        messageTime.classList.add("senderTime");
        messageTime.innerHTML = nowStr;
        chatArea.appendChild(messageContainer);
        chatArea.appendChild(messageTime);
        messageInput.value = '';
        var jsonobj = {
            type: "chatMsgB",
            sender: "host",
            receiver: memName,
            message: messageContent,
            timestamp: nowStr
        }
        webSocket.send(JSON.stringify(jsonobj))
    }
    chatArea.scrollTop = chatArea.scrollHeight;
});

function requestHisoryMsg() {
    let jsonobj_history = {
        type: "history", sender: "host", receiver: memName,
    }
    webSocket.send(JSON.stringify(jsonobj_history));
}


