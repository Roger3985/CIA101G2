'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;
var fileDataUrl = "";

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    username = document.querySelector('#name').value.trim();

    if(username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )

    connectingElement.classList.add('hidden');
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if((messageContent || fileDataUrl) && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageContent, // 只傳送文字訊息
            type: 'CHAT' // 確保始終傳送文字訊息
        };
        if (fileDataUrl) {
            chatMessage.type = 'IMAGE'; // 如果有圖片，將訊息類型設置為圖片
            chatMessage.content = fileDataUrl; // 並將 Data URL 設置為內容
        }

        // 如果同時有訊息內容和選擇了文件，將訊息類型設定為同時包含文字和圖片，並將訊息內容設定為訊息內容和文件的 Data URL
        if (messageContent && fileDataUrl) {
            console.log("Message content:", messageContent);
            console.log("File data URL:", fileDataUrl);
            chatMessage.type = 'IMAGE_AND_CHAT';
            chatMessage.content = JSON.stringify({
                text: messageContent,
                image: fileDataUrl
            });
        }
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = ''; // 清除訊息輸入框中的內容
        fileDataUrl = ''; // 清除 fileDataUrl 變數

        // 清空預覽圖片
        var previewImg = document.getElementById('preview');
        previewImg.src = 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChQG/A8ABKzSURBVGiRlZZ9UFTVVYvWra1QXaqPvWtWmUqaLUpUqalpClSopSqqZQWqo+lRdVXUwmmg0Fqg9xaVZqapWq6q1qE2rXZamqKqkqqi5SWlWbq1qmpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/KlWqU2r3Yqpa1aqWq7q5Zaiq2pUqlWqj1a1atqWqSpVqyqjVZTqv1qZVq/Kl';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    if (message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';

        var textElement = document.createElement('p');
        var messageText = document.createTextNode(message.content);
        textElement.appendChild(messageText);
        messageElement.appendChild(textElement);

    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';

        var textElement = document.createElement('p');
        var messageText = document.createTextNode(message.content);
        textElement.appendChild(messageText);
        messageElement.appendChild(textElement);

    } else if (message.type === 'CHAT') {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);

        var textElement = document.createElement('p');
        var messageText = document.createTextNode(message.content);
        textElement.appendChild(messageText);
        messageElement.appendChild(textElement);

    } else if (message.type === 'IMAGE') {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);

        // 顯示圖片而不顯示文字
        var imageElement = document.createElement('img');
        imageElement.src = message.content;
        imageElement.classList.add('small-image');
        messageElement.appendChild(imageElement);

    } else if (message.type === 'IMAGE_AND_CHAT') {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);
        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);

        // 解析文字和圖片資訊
        var contentObject = JSON.parse(message.content);

        // 從解析後的對象中獲取文字和圖片資訊
        var text = contentObject.text;
        var image = contentObject.image;

        // 顯示圖片
        var imageElement = document.createElement('img');
        imageElement.src = image;
        imageElement.classList.add('small-image');
        messageElement.appendChild(imageElement);

        // 顯示文字
        var textElement = document.createElement('p');
        var messageText = document.createTextNode(text);
        textElement.appendChild(messageText);
        messageElement.appendChild(textElement);
    }


    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}


usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)