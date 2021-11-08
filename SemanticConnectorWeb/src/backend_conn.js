const BACKEND_URL = process.env.REACT_APP_BACKEND_URL;

export async function fetchJson(path, options) {
    const url = `${BACKEND_URL}${path}`;

    const response = await fetch(url, options);
    if (!response.ok) {
        throw new Error(`Response not OK: ${response.status}`);
    }
    return await response.json();
}

export async function sendJson(method, path, payload = {}) {
    const url = `${BACKEND_URL}${path}`;
    console.log(JSON.stringify(payload));
    const response = await fetch(url,
        {
            method: method,
            body: JSON.stringify(payload),
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json"
            }
        });
    if (!response.ok) {
        throw new Error(`Response not OK: ${response.status}`);
    }

    return await response;
}

export async function sendParam(method, path, param) {
    const url = `${BACKEND_URL}${path}?${param}`;

    const response = await fetch(url,
        {
            method: method,
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json"
            }
        });
    if (!response.ok) {
        throw new Error(`Response not OK: ${response.status}`);
    }
    return await response.json();
}