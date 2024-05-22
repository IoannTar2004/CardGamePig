export function ajax(action, method, data) {
    let xhr = new XMLHttpRequest;

    xhr.open(method, action, true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

    xhr.send('data=' + JSON.stringify(data));
    return xhr
}