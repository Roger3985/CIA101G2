// $(document).ready(function() {
//     $(document).ajaxError(function(event, xhr) {
//         if (xhr.status === 401) {
//             var errors;
//             try {
//                 errors = JSON.parse(xhr.responseText); // 解析 JSON 响应
//             } catch (e) {
//                 errors = { error: "未知错误" };
//             }
//
//             var modalBody = $('#modalBody');
//             modalBody.html(''); // 清空之前的内容
//
//             if (errors && errors.error) {
//                 modalBody.append('<p>' + errors.error + '</p>');
//                 var redirectUrl = errors.redirect;
//
//                 $('#errorModal').modal('show');
//
//                 $('#confirmButton').off('click').on('click', function() {
//                     window.location.href = redirectUrl;
//                 });
//             } else {
//                 modalBody.append('<p>未知错误</p>');
//                 $('#errorModal').modal('show');
//             }
//         }
//     });
// });
