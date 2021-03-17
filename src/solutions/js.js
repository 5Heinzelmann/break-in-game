function calculateFactorial(inputNumber) {
    return (inputNumber !== 1) ? inputNumber * calculateFactorial(inputNumber - 1) : 1;
}
