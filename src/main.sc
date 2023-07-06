require: currency/currency.sc
  module = sys.zb-common

theme: /
    state: GetCurrencyPriceInRubForToday
        q!: * $Currency *
        script:
            var abbreviation = $parseTree._Currency.abbreviation;
            #$temp.priceToday = getCurrencyPriceInRubForToday(abbreviation);
        a: {{capitalizeFirstLetter($parseTree._Currency.name)}} стоит сегодня Х руб.
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