$('#add-ingredient').on('click', function () {
    var ingredientIndex = $(".ingredient-row").length;

    var ingredientId = '<input type="hidden" id="ingredients' + ingredientIndex + '.id" ' +
        'name="ingredients[' + ingredientIndex + '].id"/>'
    ;
    var ingredientName = '<input type="text" id="ingredients[' + ingredientIndex + '].name" name="ingredients[' + ingredientIndex + '].name"/>'
    ;
    var ingredientMeasurement = '<input type="text" id="ingredients' +
        ingredientIndex + '.measurement" name="ingredients[' + ingredientIndex + '].measurement"/>'
    ;

    var ingredientQuantity = '<input type="text" id="ingredients' + ingredientIndex +
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

   var instructionId = '<input type="hidden" id="instructions' + instructionIndex + '.id" ' +
       'name="instructions[' + instructionIndex + '].id"/>'
   ;

   var instructionDesc = '<input type="text" id="instructions' + instructionIndex +
       '.description" name="instructions[' + instructionIndex + '].description"/>'
   ;

   var html = '<div class="step-row"><div class="prefix-20 grid-80">' + instructionId + '<p>'
       + instructionDesc + '</p></div></div>'
   ;

   $("#add-instruction").before(html);
});

$("#signupButton").click(function() {
    var password1 = $("#firstPassword").val();
    var password2 = $("#secondPassword").val();
    if(password1 == password2) {
        document.getElementById("signupForm").submit();
    } else {
        alert("Passwords must match. Please try again.");
    }
});