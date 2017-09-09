var mq_vhost    = "/",
    mq_url      = 'chat',
    mq_exchange = '/sample/message';
    mq_queue    = '/queue/';
var client;
function on_connect() {
    client.subscribe(mq_queue + $("#queue").val(), on_message);
    enterRoom();
    $("#btn-send").on("click", function(e) {
        sendMessage(); 
    });
    $("#room").on("change", function(e) {
        enterRoom();
    });
}

function on_connect_error() {
    console.log('Connection failed');
}

function on_message(msg) {
    var obj = JSON.parse(msg.body);
    $(".chat").append('<tr><td><small>' + obj.time + '</small>&nbsp;<strong class="primary-font">' + obj.from + '</strong><p>' + obj.message + '</p></td></tr>');
}

function sendMessage() {
    var msg = new Object();
    msg.message = $("#message-input").val();
    msg.to = $("#room").val();
    client.send(mq_exchange, {"content-type":"text/plain; charset=utf-8"}, JSON.stringify(msg));
}

function enterRoom() {
    $.ajax({
        url: "api/enterroom",
        type:'POST',
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        data: JSON.stringify({room : $("#room").val()}),
        timeout:10000,
        beforeSend: function(xhr) {
            xhr.setRequestHeader($("meta[name='_csrf_header']").attr("content"), $("meta[name='_csrf']").attr("content"));
        },
        success: function(data) {
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

$(function() {
    Stomp.WebSocketClass = SockJS;
    client = Stomp.client(mq_url);
    // Create a client
    client.connect(
        "guest",
        "guest",
        on_connect,
        on_connect_error,
        mq_vhost
    );
});