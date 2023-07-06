function getCurrencyInfo() {
    return $http.query("https://www.cbr-xml-daily.ru/daily_json.js", {
        method: "GET",
        headers: {"Content-Type": "application/json"}, 
        dataType: "json"
    });
}
function getCurrencyPriceInRubForToday(abbreviation) {
    var currencyInfoResponse = getCurrencyInfo();
    if (!currencyInfoResponse.isOk || !currencyInfoResponse.data.Valute[abbreviation]) {
        return null;
    }
    var selectedCurrency = currencyInfoResponse.data.Valute[abbreviation];
    
    return {
        value: selectedCurrency.Value,
        previous: selectedCurrency.Previous
    }
}