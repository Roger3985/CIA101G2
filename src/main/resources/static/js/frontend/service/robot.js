var element = $('.floating-chat');

    setTimeout(function () {
    element.addClass('enter');
}, 1000);

element.click(openElement);

function openElement() {
    var messages = element.find('.messages');
    var textInput = element.find('.text-box');
    element.find('>i').hide();
    element.addClass('expand');
    element.find('.chat').addClass('enter');
    element.find('.contact').addClass('expand');
    var strLength = textInput.val().length * 2;
    textInput.keydown(onMetaAndEnter).prop("disabled", false).focus();
    element.off('click', openElement);
    element.find('.header button').click(closeElement);
    element.find('#sendMessage').click(sendNewMessage);
    messages.scrollTop(messages.prop("scrollHeight"));
}

function closeElement() {
    element.find('.chat').removeClass('enter').hide();
    element.find('>i').show();
    element.removeClass('expand');
    element.find('.contact').removeClass('expand');
    element.find('.header button').off('click', closeElement);
    element.find('#sendMessage').off('click', sendNewMessage);
    element.find('.text-box').off('keydown', onMetaAndEnter).prop("disabled", true).blur();
    setTimeout(function () {
        element.find('.chat').removeClass('enter').show()
        element.click(openElement);
    }, 500);
}

// function sendNewMessage() {
//     var userInput = $('.text-box');
//     var newMessage = userInput.html().replace(/\<div\>|\<br.*?\>/ig, '\n').replace(/\<\/div\>/g, '').trim()
//         .replace(/\n/g, '<br>');
//
//     if (!newMessage) return;
//
//     var messagesContainer = $('.messages');
//
//     messagesContainer.append([
//         '<li class="other">',
//         newMessage,
//         '</li>'
//     ].join(''));
//
//     // clean out old message
//     userInput.html('');
//     // focus on input
//     userInput.focus();
//
//     messagesContainer.finish().animate({
//         scrollTop: messagesContainer.prop("scrollHeight")
//     }, 250);
// }


// var messageForm_el = document.getElementById("messageForm");
// messageForm_el.addEventListener("submit", function (e) {
//     e.preventDefault();
// });
var chatBlock = document.querySelector("#chat-messages");
var messageInput_el = document.getElementById("messages_input");

async function sendNewMessage() {

    var content = messageInput_el.innerText.trim();
    if (content === '') {
        alert("請輸入訊息");
        return;
    } else {
        const messageContainer = document.createElement('li');
        const message = document.createElement('span');
        message.innerHTML = content;
        messageContainer.classList.add('other');
        messageContainer.appendChild(message);
        chatBlock.appendChild(messageContainer);
        messageInput_el.innerText = '';
    }
    try {
        const response = await fetch(`/robot/reply?keywordName=${encodeURIComponent(content)}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'text/plain'
            },
        });
        if (!response.ok) {
            throw new Error('Http error! status: ${response.status}');
        }
        const data = await response.text();
        const bosMsgObj = JSON.parse(data);
        console.log(bosMsgObj);
        console.log(bosMsgObj.length)
        if (bosMsgObj.length > 0) {
            for (let i = 0; i < bosMsgObj.length; i++) {
                const botMsgContent = bosMsgObj[i].responseContent;
                const messageContainer = document.createElement('li');
                const botMsg = document.createElement('span');
                botMsg.innerHTML = botMsgContent;
                messageContainer.classList.add('self');
                messageContainer.appendChild(botMsg);
                chatBlock.appendChild(messageContainer);
            }
        }else{
            const messageContainer = document.createElement('li');
            const botMsg = document.createElement('span');
            // const chaturl = document.createElement('a th:href="@{/frontend/service/frontendServiceChat}"');
            botMsg.innerHTML = "請聯繫客服人員即時為您處理";
            messageContainer.classList.add('self');
            // botMsg.appendChild(chaturl);
            messageContainer.appendChild(botMsg);
            chatBlock.appendChild(messageContainer);
        }


    } catch (error) {
        console.error('Error:', error);
        // const errorMessage = document.createElement('div');
        // errorMessage.textContent = 'An error occurred. Please try again.';
        // document.getElementById('messages').appendChild(errorMessage);
    }
}

// messageInput_el.addEventListener("keyup", function (e) {
//     // console.log(e.which);
//     if (e.which == 13) {
//         msg_btn_el.click();
//     }
// });
//
// var msg_btn_el = document.getElementById("sendMessage");
// msg_btn_el.addEventListener("click", function () {
//     // alert("aaaa");
//     sendMessage();
// });
function onMetaAndEnter(event) {
    if (event.keyCode == 13) {
        sendNewMessage();
    }
}