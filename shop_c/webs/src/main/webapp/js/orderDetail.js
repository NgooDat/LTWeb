/**
 * 
 */
function openCancelModal() {
    const modal = document.getElementById("cancelModal");
    modal.style.display = "flex";
}

function closeCancelModal() {
    const modal = document.getElementById("cancelModal");
    modal.style.display = "none";
}

function confirmCancel() {
    const selectedReason = document.querySelector('input[name="reason"]:checked');
    if (!selectedReason) {
        alert("Vui lòng chọn lý do hủy!");
        return;
    }

    const reasonId = selectedReason.value;

    if (confirm("Bạn có chắc chắn muốn hủy đơn này?")) {
        // Lấy idOrder từ URL
        const urlParams = window.location.pathname.split('/');
        const idOrder = urlParams[urlParams.length - 1].split('.')[0];  // Lấy phần cuối của URL và bỏ ".htm"

        // Redirect to cancel order URL
        window.location.href = `order/cancel/${idOrder}.htm?IdReason=${reasonId}`;
    }
}
