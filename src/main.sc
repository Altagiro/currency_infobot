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
            var price = $temp.price[$parseTree.value];
            $temp.prettyPrice = getPrettyPriceInRub(amount * $temp.price.value);
            $temp.trendVerb = $temp.price.value > $temp.price.previous ? "больше" : "меньше"
        if: $parseTree.value !== "previous"
            a: Сегодня {{capitalizeFirstLetter($parseTree._Currency.name)}} стоит {{$temp.trendVerb}}, чем вчера. За {{$temp.currency}} придется отдать {{$temp.prettyPrice}}
        else:
            a: Вчера {{$temp.currency}} стоили {{$temp.prettyPrice}}. Сегодня уже {{$temp.trendVerb}}

        if: !$session.toldAboutGainersAnsLosers
            script:
                $session.toldAboutGainersAnsLosers = true;
                var leaderBoard = getSortedPriceLeaderBoard();
                $temp.loser = leaderBoard.shift();
                $temp.leader = leaderBoard.pop();
                
            a: Лидер роста – {{$temp.leader.name}} ({{getPrettyPriceInRub($temp.leader.price)}} {{$temp.leader.difference > 0 ? "+" : "-"}}{{$temp.leader.difference * 100}}%)
            a: В отстающих – {{$temp.loser.name}} ({{getPrettyPriceInRub($temp.loser.price)}} {{$temp.loser.difference > 0 ? "+" : "-"}}{{$temp.loser.difference * 100}}%)

        state: FailedToObtainPrice
            a: Не удалось найти цену этой валюты относительно рубля.
            a: Попробуйте, пожалуйста, указать другую

    state: Start
        q!: $regex</start>
        script:
            $jsapi.startSession();
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