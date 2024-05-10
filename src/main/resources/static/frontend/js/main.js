(function ($) {
    "use strict";

    // Dropdown on mouse hover
    $(document).ready(function () {
        function toggleNavbarMethod() {
            if ($(window).width() > 992) {
                $('.navbar .dropdown').on('mouseover', function () {
                    $('.dropdown-toggle', this).trigger('click');
                }).on('mouseout', function () {
                    $('.dropdown-toggle', this).trigger('click').blur();
                });
            } else {
                $('.navbar .dropdown').off('mouseover').off('mouseout');
            }
        }
        toggleNavbarMethod();
        $(window).resize(toggleNavbarMethod);
    });


    // Back to top button
    $(window).scroll(function () {
        if ($(this).scrollTop() > 100) {
            $('.back-to-top').fadeIn('slow');
        } else {
            $('.back-to-top').fadeOut('slow');
        }
    });
    $('.back-to-top').click(function () {
        $('html, body').animate({scrollTop: 0}, 1500, 'easeInOutExpo');
        return false;
    });


    // Vendor carousel
    $('.vendor-carousel').owlCarousel({
        loop: true,
        margin: 29,
        nav: false,
        autoplay: true,
        smartSpeed: 1000,
        responsive: {
            0:{
                items:2
            },
            576:{
                items:3
            },
            768:{
                items:4
            },
            992:{
                items:5
            },
            1200:{
                items:6
            }
        }
    });


    // Related carousel
    $('.related-carousel').owlCarousel({
        loop: true,
        margin: 29,
        nav: false,
        autoplay: true,
        smartSpeed: 1000,
        responsive: {
            0:{
                items:1
            },
            576:{
                items:2
            },
            768:{
                items:3
            },
            992:{
                items:4
            }
        }
    });


    // Product Quantity
    $('.quantity button').on('click', function () {
        var button = $(this);
        var inputField = button.parent().parent().find('input');
        var oldValue = parseInt(inputField.val());
        var productRow = button.closest('.productitm');

        if (button.hasClass('btn-plus')) {
            var newVal = oldValue
        } else {
            if (oldValue > 0) {
                var newVal = oldValue ;
            } else {
                newVal = 0;
            }
        }

        inputField.val(newVal);

        if (newVal === 0) {
            // 弹出确认对话框
            var confirmed = confirm("确定要移除该商品吗？");
            if (confirmed) {
                // 当用户确认移除时，移除商品行
                productRow.remove();
            } else {
                // 如果用户取消移除，恢复数量为1
                inputField.val(1);
            }
        }
    });

    var removeButtons = document.querySelectorAll('.btn.btn-sm.btn-primary.btn-remove');

// 为每个按钮添加点击事件监听器
//     removeButtons.forEach(function(button) {
//         button.addEventListener('click', function() {
//             // 弹出确认对话框
//             var confirmed = confirm("确定要移除该商品吗？");
//             // 如果用户点击了确认按钮
//             if (confirmed) {
//                 // 找到要移除的商品所在的父元素并移除
//                 var productRow = button.closest('.productitm');
//                 productRow.remove();
//
//                 // 使用Thymeleaf动态生成表单
//                 var formHtml = '<form action="/cart/deletecartsuccess" method="post">' +
//                     '<input type="hidden" name="productNo" value="' + cart.compositeKey.productNo + '">' +
//                     '<input type="hidden" name="memNo" value="' + cart.compositeKey.memNo + '">' +
//                     '</form>';
//                 var form = $(formHtml);
//                 $(document.body).append(form);
//                 form.submit();
//             }
//         });
//     });


})(jQuery);
