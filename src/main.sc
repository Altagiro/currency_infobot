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

theme: /
    state: GetCurrencyPriceInRubForToday
        q!: * [$Number] $Currency *
        script:
            var abbreviation = $parseTree._Currency.abbreviation;
            $temp.price = getCurrencyPriceInRubForToday(abbreviation);
        if: !$temp.price
            go!: FailedToObtainPrice
        script:
            $temp.trendIsGrowing = $temp.price.value > $temp.price.previous;
            $temp.amount = parseInt($parseTree._Number) || 1;
        a: {{$temp.amount}} {{$parseTree._Currency.symbol}} – это {{getPrettyPriceInRub($temp.amount * $temp.price.value)}}
        a: {{capitalizeFirstLetter($parseTree._Currency.name)}} {{$temp.trendIsGrowing ? "растёт" : "теряет"}} в цене

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