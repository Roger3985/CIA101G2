// src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"
// $("#loginForm").submit(function (event) {
//     // 防止表單預設提交
//     event.preventDefault();
//
//     // 獲取帳號和密碼輸入框的值
//     var identifierValue = $('#exampleInputEmail1').val(); // 帳號輸入框的值
//     var passwordValue = $('#exampleInputPassword1').val(); // 密碼輸入框的值
//
//     // 建立 FormData 物件
//     const formData = new FormData();
//     formData.append('identifier', identifierValue);
//     formData.append('password', passwordValue);
//     formData.append('autoLoginMember', $('#exampleCheck1').is(':checked') ? 1 : 0); // 如果選中，設置值為1，否則為0
//
//     // 發送 POST 請求到後端控制器
//     $.ajax({
//         type: "POST",
//         url: "/frontend/member/loginPageByPopup",
//         contentType: 'application/json',
//         data: JSON.stringify({
//             identifier:　identifierValue,
//             password: passwordValue,
//             autoLoginMember: $('#exampleCheck1').is(':checked') ? 1 : 0
//         }), // 直接使用 FormData 物件
//         // contentType: 'multipart/form-data', // 不設置 contentType，讓 jQuery 自動識別
//         dataType: 'json',
//         processData: false, // 不對資料進行處理
//         success: function (response) {
//             // 登錄成功處理
//             $('#errorMessage').text(''); // 清除錯誤消息
//             $('#topbarlogin').modal('hide'); // 隱藏登錄彈窗
//         },
//         error: function (xhr, status, error) {
//             // 登錄失敗處理
//             var errorMessage = JSON.parse(xhr.responseText);
//             $('#errorMessage').text(errorMessage.message); // 顯示錯誤消息
//         }
//     });
// })

// 引入 Axios 函式庫
// <script src="https://cdnjs.cloudflare.com/ajax/libs/axios/1.6.8/axios.js"></script>
//
// // 當表單提交時處理
// $("#loginForm").submit(function (event) {
//     // 阻止表單的預設提交行為
//     event.preventDefault();
//
//     // 獲取帳號和密碼輸入框的值
//     var identifierValue = $('#exampleInputEmail1').val(); // 帳號輸入框的值
//     var passwordValue = $('#exampleInputPassword1').val(); // 密碼輸入框的值
//
//     // 建立 FormData 物件
//     const formData = new FormData();
//     formData.append('identifier', identifierValue);
//     formData.append('password', passwordValue);
//     formData.append('autoLoginMember', $('#exampleCheck1').is(':checked') ? 1 : 0); // 如果選中，設置值為1，否則為0
//
//     // 使用 Axios 發送 POST 請求到後端控制器
//     axios.post('/frontend/member/loginPageByPopup', formData)
//         .then(function (response) {
//             // 登錄成功處理
//             $('#errorMessage').text(''); // 清除錯誤消息
//             $('#topbarlogin').modal('hide'); // 隱藏登錄彈窗
//         })
//         .catch(function (error) {
//             // 登錄失敗處理
//             if (error.response) {
//                 $('#errorMessage').text(error.response.data); // 顯示錯誤消息在彈窗中
//             }
//         });
// });
