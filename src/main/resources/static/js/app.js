/**
 * Foodie Application - Client-Side Interactive Logic
 */

document.addEventListener('DOMContentLoaded', function () {
    // 1. Credit Card Number Formatting (Groups of 4 digits)
    const cardNumberInput = document.getElementById('cardNumber');
    if (cardNumberInput) {
        cardNumberInput.addEventListener('input', function (e) {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length > 16) value = value.substring(0, 16);
            e.target.value = value;
        });
    }

    // 2. Card Expiry Formatting (MM/YY)
    const cardExpiryInput = document.getElementById('cardExpiry');
    if (cardExpiryInput) {
        cardExpiryInput.addEventListener('input', function (e) {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length > 4) value = value.substring(0, 4);
            if (value.length >= 2) {
                e.target.value = value.substring(0, 2) + '/' + value.substring(2);
            } else {
                e.target.value = value;
            }
        });
    }

    // 3. Card CVV Input Formatting (3 digits)
    const cardCvvInput = document.getElementById('cardCvv');
    if (cardCvvInput) {
        cardCvvInput.addEventListener('input', function (e) {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length > 3) value = value.substring(0, 3);
            e.target.value = value;
        });
    }

    // 4. Auto-dismiss Alert Banners after 5 seconds
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });
});
