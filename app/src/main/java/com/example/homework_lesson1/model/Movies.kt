package com.example.homework_lesson1.model

import com.example.homework_lesson1.R

object Movies {

    val movies_info = mapOf(
        "Savage" to "Уэсли Гибсон - зануда и нытик, целыми днями торчащий в офисе. Босс его унижает, подружка ему изменяет. Его жизнь катится в никуда! Обнаружив, что отец, бросивший его в раннем детстве, зверски убит, Уэс оказывается втянут в тайное общество киллеров под названием «Братство». За его обучение берется опытная наставница Фокс, и этот когда-то прикованный к офисному креслу хлюпик развивает молниеносную реакцию и сверхчеловеческую скорость. Однако уже на первом задании Уэсли понимает: есть вещи посложнее, чем вершить судьбы других людей. Это - набраться храбрости и стать хозяином собственной жизни. ",
        "Avatar 2" to "После принятия образа аватара солдат Джейк Салли становится предводителем народа нави и берет на себя миссию по защите новых друзей от корыстных бизнесменов с Земли. Теперь ему есть за кого бороться — с Джейком его прекрасная возлюбленная Нейтири. Когда на Пандору возвращаются до зубов вооруженные земляне, Джейк готов дать им отпор.",
        "Force Works" to "1942 год, оккупированная Европа. Оказавшись в концлагере, бельгиец еврейского происхождения Жиль Кремье выдает себя за перса — для него это единственная возможность остаться в живых. Эта ложь действительно спасает ему жизнь, но Жиль еще не представляет, какой ценой. Довольные таким редким уловом немецкие солдаты приводят Жиля к концлагерному повару Клаусу Коху, который мечтает, как только закончится война, уехать в Иран и открыть там ресторан. Кох ищет настоящего перса, который научит его говорить на персидском языке. Так начинается история Жиля Кремье и Клауса Коха — еврея и немца, узника и тюремщика, учителя и ученика.",
        "Eternal Warrior" to "Франция, конец XIV века. Отношения между рыцарем Жаном де Карружем и его соратником и соседом Жаком Ле Гри постепенно портятся, и дело доходит до неразрешимого конфликта. Оскорблённый Карруж обращается за помощью к королю Франции Карлу VI, и тот постановляет — противники должны решить спор в смертельной дуэли.",
        "Siege" to "Когда в Бруклине взлетает на воздух автобус и на улицах Нью-Йорка разворачивается кровавый террор, Хаб — Хаббарт, глава Объединенного Антитеррористического Подразделения ФБР получает задание задержать террористов и предъявить им обвинение. Элиза Крафт — тайный оперативный агент ЦРУ. Она обладает ценными источниками информации в среде американцев арабского происхождения и тесно связана с ними. Хаб и Элиза работают вместе, объединившись для совместной борьбы против террористической группы. Но этого оказывается недостаточно. Требования общественности обеспечить безопасность граждан вынуждают президента США объявить чрезвычайное положение и обратиться за помощью к армии..."
    )

    val movies_posters = sequenceOf(
        "Savage" to R.drawable.savage,
        "Avatar 2" to R.drawable.avatar_2,
        "Force Works" to R.drawable.force_works,
        "Eternal Warrior" to R.drawable.eternal_warrior,
        "Siege" to R.drawable.siege
    )
}