require: currency/currency.sc
  module = sys.zb-common
require: number/number.sc
  module = sys.zb-common

require: scripts/utils.js
require: scripts/currency.js

init:
    $http.config({
        cacheTimeToLiveInSeconds: 1 * 60 * 30
    });

patterns:
    # добавил тк по "доллар" сущности из коммона не активируются
    $myCurrency = (~доллар:2/~рубль:27) || converter = function(pt){return $Currencies[pt.value].value;}
theme: /
    state: GetCurrencyPriceInRub
        q!: * ([$Number] ($Currency/$myCurrency::Currency)) * :value *
        q!: * {([$Number] ($Currency/$myCurrency::Currency)) * (вчера:previous)} *
        script:
            var abbreviation = $parseTree._Currency.abbreviation;
            $temp.price = getCurrencyPrice(abbreviation);
        if: !$temp.price
            go!: FailedToObtainPrice
        script:
            var amount = parseInt($parseTree._Number) || 1;
            $temp.currency = amount + $parseTree._Currency.symbol;
            $temp.when = $parseTree.value === "previous" ? "Вчера" : "Сегодня";
            var price = $temp.price[$parseTree.value];
            $temp.prettyPrice = getPrettyPriceInRub(amount * $temp.price.value);
            $temp.trendVerb = $temp.price.value > $temp.price.previous ? "растёт" : "теряет"
            
        a: {{$temp.when}} {{$temp.currency}} – это {{$temp.prettyPrice}}
        a: {{capitalizeFirstLetter($parseTree._Currency.name)}} {{$temp.trendVerb}} в цене

        state: FailedToObtainPrice
            a: Не удалось найти цену этой валюты относительно рубля.
            a: Попробуйте, пожалуйста, указать другую

    state: Start
        q!: $regex</start>
        a: Начнём.

    state: Hello
        intent!: /привет
        a: Привет привет

    state: Bye
        intent!: /пока
        a: Пока пока

    state: NoMatch
        event!: noMatch
        a: Я не понял. Вы сказали: {{$request.query}}

    state: Match
        event!: match
        a: {{$context.intent.answer}}