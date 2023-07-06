function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
}
function getPrettyPriceInRub(price) {
    var roundedRub = Math.floor(price);
    var kopecks = Math.floor((price - roundedRub) * 100);
    var prettyAnswer = roundedRub + " " + $nlp.conform("рубль", roundedRub);
    if (kopecks) {
        prettyAnswer += " " + kopecks + " " + $nlp.conform("копейка", kopecks)
    }
    return prettyAnswer;
}