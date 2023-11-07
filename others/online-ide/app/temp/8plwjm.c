#include <stdio.h>
#include<math.h>

int getDigitCount(int number)
{
    int counter = 0;
    
    while (number != 0)
    {
        
        number %= 10;
        
        counter++;
    }
    
    return counter;
}
int main()
{
    printf("%d", getDigitCount(321));
    return 0;
}