import SockJS from "sockjs-client";
import Stomp from "stompjs"

const socket = SockJS("/pig")
const stompClient = Stomp.over(socket)
stompClient.debug = null

export function webSocketConnect(playerId, callback) {
    if (!stompClient.connected)
        stompClient.connect({id: playerId}, callback)
}

export function send(object, destination) {
    stompClient.send(`/app/${destination}`, {}, JSON.stringify(object))
}

export function subscribe(point, callback) {
    stompClient.subscribe(point, (message) => {callback(message.body)})
}

export function disconnect() {
    stompClient.disconnect({}, function(frame) {
        console.log('Disconnected: ' + frame);
    });
}