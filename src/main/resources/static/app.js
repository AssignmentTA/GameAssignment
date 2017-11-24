var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#results").html("");
  
}

function connect() {
    var socket = new SockJS('/game-web-socket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/game', function (game) {
            showResult(JSON.parse(game.body));
        });
    });
    enableGame();
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    disableGame();
    console.log("Disconnected");
    
}

function start() {
    stompClient.send("/app/play", {}, $("#number").val());
}

function autoStart() {
    stompClient.send("/app/autoPlay", {});
}

function enableGame() {
	 $("#autoStart").prop("disabled", false);
	 $("#start").prop("disabled", false);
}

function disableGame() {
	 $("#autoStart").prop("disabled", true);
	 $("#start").prop("disabled", true);
}


function showResult(game) {

    var moves = game.moves;
    for(var i = 0; i < moves.length; i++) {
        var move = moves[i];
        $("#results").append("<tr><td><b>" +"Player: </b>"+move.player.name+ "       <b>Move Output: </b>"+ move.output +"         <b>Added Number: </b>"+ move.additive + "</td></tr>");

    }
     $("#results").append("<tr><td><b> Winner is " +game.winner.name+ " <b></td></tr>");

}


$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#start" ).click(function() { start(); });
    $( "#autoStart" ).click(function() { autoStart(); });
});

function allowNumberOnly(){
if( !(event.keyCode == 8                                // backspace
        || event.keyCode == 46                              // delete
        || (event.keyCode >= 35 && event.keyCode <= 40)     // arrow keys/home/end
        || (event.keyCode >= 48 && event.keyCode <= 57)     // numbers on keyboard
        || (event.keyCode >= 96 && event.keyCode <= 105))   // number on keypad
        ) {
            event.preventDefault();     // Prevent character input
    }
}
