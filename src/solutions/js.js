function calculateFactorial(number) {
    return (number !== 1) ? number * calculateFactorial(number - 1) : 1;
}
