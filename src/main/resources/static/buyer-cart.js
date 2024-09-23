
document.addEventListener('DOMContentLoaded', function() {

    const cartAmountElements = document.querySelectorAll('[id^="cartAmount"]');

    cartAmountElements.forEach(function(cartAmountElement) {
        let cartAmountInt = parseInt(cartAmountElement.textContent);

        let productId = cartAmountElement.id.replace('cartAmount', '');

        let removeButton = document.getElementById('removeButton' + productId);

        if (cartAmountInt <= 1) {
        removeButton.disabled = true;
        }
    });
});

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

function addToCartButton(productId) {
    fetch(`/api/cart/add/button?productId=${productId}`, {
        method: 'POST',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('One Item Added', data);
            let cartAmountEdit = document.getElementById('cartAmount' + productId);
            let cartAmountInt = parseInt(cartAmountEdit.textContent);
            cartAmountInt += 1;
            cartAmountEdit.textContent = cartAmountInt;

            let disableRemoveButton = document.getElementById('removeButton' + productId);
            if (cartAmountInt <= 1) {
                disableRemoveButton.disabled = true;
            } else {
                disableRemoveButton.disabled = false;
            }
        })
        .catch((error) => {
            console.error('Error:', error);
            showNotification('Failed to add item to cart. Make sure there\'s enough' +
                ' in stock.', 'danger');
        });
}

function removeFromCartButton(productId) {
    fetch(`/api/cart/remove/button?productId=${productId}`, {
        method: 'DELETE',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('One Item Removed', data);
            let cartAmountEdit = document.getElementById('cartAmount' + productId);
            let cartAmountInt = parseInt(cartAmountEdit.textContent);
            cartAmountInt -= 1;
            cartAmountEdit.textContent = cartAmountInt;

            let disableRemoveButton = document.getElementById('removeButton' + productId);
            if (cartAmountInt <= 1) {
                disableRemoveButton.disabled = true;
            } else {
                disableRemoveButton.disabled = false;
            }
        })
        .catch((error) => {
            console.error('Error:', error);
            showNotification('Failed to remove item from cart.', 'danger');
        });
}

function removeFromCart(productId) {
    fetch(`/api/cart/remove?productId=${productId}`, {
        method: 'DELETE',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Item removed', data);
            // Reload the page to reflect the updated cart
            location.reload();
        })
        .catch((error) => {
            console.error('Error:', error);
            showNotification('Failed to remove item from cart.', 'danger');
        });
}

function clearCart() {
    fetch(`/api/cart/clear`, {
        method: 'DELETE',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Cart cleared', data);
            // Reload the page to reflect the updated cart
            location.reload();
        })
        .catch((error) => {
            console.error('Error:', error);
            showNotification('Failed to clear cart.', 'danger');
        });
}
