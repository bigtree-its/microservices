$(document).ready(function() {
    $.ajax({
        url: "http://localhost:8082/orders/v1/baskets?basketId=1234"
    }).then(function(data, status, jqxhr) {
       $('.greeting-id').append(data.id);
       $('.greeting-content').append(data.content);
       console.log(jqxhr);
    });
});