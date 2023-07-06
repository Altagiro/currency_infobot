function getCurrencyInfo() {
    return $http.query("https://www.cbr-xml-daily.ru/daily_json.js", {
        method: "GET",
        headers: {"Content-Type": "application/json"}, 
        dataType: "json"
    });
}