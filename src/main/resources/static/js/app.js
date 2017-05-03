$('#add-ingredient').on('click', function () {
    var ingredientIndex = $("#ingredient-row").length();

    var ingredientId = $('<input type="hidden" value="' + ingredientIndex + 1 + '" id="ingredients' + ingredientIndex + '.id" ' +
        'name="ingredients[' + ingredientIndex + '].id"/>'
    );
    var ingredientName = $('<input type="text" value="TestIngredient ' + ingredientIndex + 1 +
        '" id="ingredients[' + ingredientIndex + '].name" name="ingredients[' + ingredientIndex + '].name"/>'
    );
    var ingredientMeasurement = $('<input type="text" value="TestMeasurement ' + ingredientIndex + 1 + '" id="ingredients' +
        ingredientIndex + '.measurement" name="ingredients[' + ingredientIndex + '].measurement"/>'
    );

    var ingredientQuantity = $('<input type="text" value="' + ingredientIndex + 1 + '" id="ingredients' + ingredientIndex +
        '.quantity" name="ingredients[' + ingredientIndex + '].quantity"/>'

    );
    var html = $('<div class="ingredient-row">'+ '<div class="prefix-20 grid-30">' + ingredientId + '<p>' + ingredientName + '</p>'
        + '</div>' + '<div class="grid-30">' + '<p>' + ingredientMeasurement + '</p>' + '</div>' + '<div class="grid-10 suffix-10">'
        + '<p>' + ingredientQuantity + '</p>' + '</div>' + '</div>'
    );

    $(".input").before(html);
});


