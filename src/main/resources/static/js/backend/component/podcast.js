// var stompClient = null;
//
// function connect() {
//     var socket = new SockJS('/admin');
//     stompClient = Stomp.over(socket);
//     stompClient.connect({}, function (frame) {
//         console.log('已連接: ' + frame);
//         subscribeToTopics();
//     });
// }
//
// function subscribeToTopics() {
//     // 確保只訂閱一次
//     if (!stompClient.subscribed) {
//         stompClient.subscribed = true;
//         stompClient.subscribe('/topic/parttime', function (message) {
//             console.log("1");
//             showMessage(message.body);
//         });
//         stompClient.subscribe('/topic/fulltime', function (message) {
//             console.log("2");
//             showMessage(message.body);
//         });
//         stompClient.subscribe('/topic/manager', function (message) {
//             console.log("3");
//             showMessage(message.body);
//         });
//         stompClient.subscribe('/topic/boss', function (message) {
//             console.log("4");
//             showMessage(message.body);
//         });
//     }
// }
//
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