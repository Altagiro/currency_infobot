function getCurrencyInfo() {
    return $http.query("https://www.cbr-xml-daily.ru/daily_json.js", {
        method: "GET",
        headers: {"Content-Type": "application/json"}, 
        dataType: "json"
    });
}
function getCurrencyPrice(abbreviation) {
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
function getSortedPriceLeaderBoard() {
    var currencyInfoResponse = getCurrencyInfo();
    if (!currencyInfoResponse.isOk) {
        return null;
    }
    var result = Object.keys(currencyInfoResponse.data.Valute).map(function(abbreviation){
        var current = currencyInfoResponse.data.Valute[abbreviation];
        var difference = (current.Value - current.Previous) / current.Previous;
        return {
            name: current.Name,
            price: current.Value,
            difference: difference.toPrecision(2)
        }
    }).sort(function(currencyA, currencyB) {
        return (currencyA.difference === currencyB.difference) ? 0 : currencyA.difference > currencyB.difference;
    });
    return result;
}