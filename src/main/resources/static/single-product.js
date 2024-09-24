function showNotification(message, type = 'success') {
    const notification = document.createElement('div');
    notification.className = `alert alert-${type} notification`;
    notification.textContent = message;

    let messageAppend = document.getElementById('cartNotif');
    messageAppend.appendChild(notification)

    setTimeout(() => {
        notification.style.opacity = '0';
        setTimeout(() => {
            messageAppend.removeChild(notification);
        }, 500);
    }, 3000);
}

function addToCart(productId, amountProd) {
    fetch(`/api/cart/add?productId=${productId}&amount=${amountProd}`, {
        method: 'POST',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Item added', data);
            showNotification('Item successfully added to cart!')
        })
        .catch((error) => {
            console.error('Error:', error);
            showNotification('Failed to add item to cart.', 'danger');
        });
}