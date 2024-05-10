document.addEventListener('DOMContentLoaded', function() {

	// 取得今天的日期 
	var today = new Date().toISOString().split('T')[0];

	// 將預約的日期設為最小日期
	var apoWantDateElements = document.querySelectorAll(".apoWantDate");

	// 使用迴圈設置每個日期輸入框的最小日期
	apoWantDateElements.forEach(function(element) {
		element.min = today;
	});
});