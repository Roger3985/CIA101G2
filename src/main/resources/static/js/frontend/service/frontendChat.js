const usernamePage = document.querySelector('#username-page');
const chatPage = document.querySelector('#chat-page');
const usernameForm = document.querySelector('#usernameForm');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-messages');

// 設定websocket連線
const HostPoint = `/chat/{userName}`;
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
        console.log("frontend connect Suceess!");
    }

    webSocket.onmessage = function (event){
        let message = event.data;
        console.log(data);
    }


}


