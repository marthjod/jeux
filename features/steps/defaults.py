host = "localhost:8080"
rest_api_url = "http://%s/JeuxWeb/rest" % host
rest_api_admin_url = rest_api_url + "/admin"

default_request_params = {
    "admin_cookie": "admin-auth=<COOKIE>",
    "http_auth": "Basic <BASE64>"
}

default_headers = {
    'Host': host,
    'Cookie': default_request_params["admin_cookie"],
    'Authorization': default_request_params["http_auth"],
    'Content-Type': 'application/json; charset=UTF-8',
    'Accept': "*/*",
    'Accept-Encoding': "gzip, deflate",
    'Connection': "keep-alive",
    'Pragma': "no-cache",
    'Cache-Control': "no-cache",
    'User-Agent': "behave/Jeux"
}