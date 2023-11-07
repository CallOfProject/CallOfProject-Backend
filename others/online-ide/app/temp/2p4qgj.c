#include <stdio.h>
#include <math.h>
int dc(int number)
{
    int counter = 0;
    
    while(number != 0)
    {
        number /= 10;
        counter++;
    }
    return counter;
}
int main()
{
    printf("digit count is: %d", dc(321231));
    return 0;
}