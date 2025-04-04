export function ajax(action, method, data) {
    let xhr = new XMLHttpRequest;

    xhr.open(method, action, true);
    xhr.setRequestHeader('Content-Type', 'application/json');

    let a = JSON.stringify(data)
    xhr.send(a)
    return xhr
}