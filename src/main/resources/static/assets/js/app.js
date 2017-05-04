$('#add-ingredient').on('click', function () {
    var ingredientIndex = $(".ingredient-row").length;

    var ingredientValue = ingredientIndex++;

    var ingredientId = '<input type="hidden" value="' + ingredientValue + '" id="ingredients' + ingredientIndex + '.id" ' +
        'name="ingredients[' + ingredientIndex + '].id"/>'
    ;
    var ingredientName = '<input type="text" value="TestIngredient ' + ingredientIndex +
        '" id="ingredients[' + ingredientIndex + '].name" name="ingredients[' + ingredientIndex + '].name"/>'
    ;
    var ingredientMeasurement = '<input type="text" value="TestMeasurement ' + ingredientIndex + '" id="ingredients' +
        ingredientIndex + '.measurement" name="ingredients[' + ingredientIndex + '].measurement"/>'
    ;

    var ingredientQuantity = '<input type="text" value="' + ingredientIndex + '" id="ingredients' + ingredientIndex +
        '.quantity" name="ingredients[' + ingredientIndex + '].quantity"/>'

    ;
    var html = '<div class="ingredient-row"><div class="prefix-20 grid-30">' + ingredientId
        + '<p>' + ingredientName + '</p></div><div class="grid-30"><p>' + ingredientMeasurement
        + '</p></div><div class="grid-10 suffix-10"><p>' + ingredientQuantity + '</p></div></div>'
    ;

    $("#add-ingredient").before(html);
});

$('#add-instruction').on('click', function () {
   var instructionIndex = $(".step-row").length;

   var instructionValue = instructionIndex++;

   var instructionId = '<input type="hidden" value="' + instructionValue + '" id="instructions' + instructionIndex + '.id" ' +
       'name="instructions[' + instructionIndex + '].id">'
   ;

   var instructionDesc = '<input type="text" id="instructions' + instructionIndex + '.description" name="instructions[' +
       '].description" value="TestDescription ' + instructionIndex + '">'
   ;

   var html = '<div class="step-row"><div class="prefix-20 grid-80">' + instructionId + '<p>'
       + instructionDesc + '</p></div></div>'
   ;

   $("#add-instruction").before(html);
});
