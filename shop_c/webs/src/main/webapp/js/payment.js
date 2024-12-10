/**
 * 
 */
document.addEventListener("DOMContentLoaded", function() {
	const form = document.querySelector("form");
	const nameInput = document.getElementById("name");
	const phoneInput = document.getElementById("phone");
	const addressInput = document.getElementById("address");
	const paymentSelect = document.getElementById("payment");

	form.addEventListener("submit", function(event) {
		let isValid = true;
		let errorMessage = "";

		// Regular expression to detect HTML tags
		const htmlRegex = /<[^>]*>/g;

		// Validate Tên
		if (nameInput.value.trim() === "" || htmlRegex.test(nameInput.value.trim())) {
			isValid = false;
			errorMessage += "Tên không được để trống!!!\n";
		}

		// Validate Số điện thoại
		const phoneRegex = /^[0-9]{10}$/;
		if (!phoneRegex.test(phoneInput.value.trim())) {
			isValid = false;
			errorMessage += "Số điện thoại không hợp lệ!!!\n";
		}

		// Validate Địa chỉ
		if (addressInput.value.trim() === "" || htmlRegex.test(addressInput.value.trim())) {
			isValid = false;
			errorMessage += "Địa chỉ không được để trống!!!\n";
		}

		// Validate Phương thức thanh toán
		if (paymentSelect.value === "") {
			isValid = false;
			errorMessage += "Vui lòng chọn phương thức thanh toán.\n";
		}

		// Nếu có lỗi, hiển thị và ngăn form submit
		if (!isValid) {
			alert(errorMessage);
			event.preventDefault();
		}
	});

	// Tự động thay đổi action và method của form khi chọn phương thức thanh toán
	paymentSelect.addEventListener("change", function() {
		const selectedValue = paymentSelect.value;

		if (selectedValue === "1") {
			form.action = "payment/result.htm";
			form.method = "post";
		} else if (selectedValue === "2") {
			form.action = "payment/vnpay.htm";
			form.method = "get";
		}
	});
});