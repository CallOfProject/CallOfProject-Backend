#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>
#include <string.h>
#include <stdbool.h>

#define BLACK "\033[30m"
#define BOLD_RED "\033[1m\033[31m"
#define BOLD_YELLOW "\033[1m\033[33m"


volatile bool it_1 = false;
volatile bool it_2 = false;
volatile bool it_3 = false;

char *msg;

char LETTER;

void printColor()
{
    if (it_1 && !it_2 && !it_3)
        printf(BLACK);

    if (it_2 && !it_1 && !it_3)
        printf(BOLD_YELLOW);

    if (it_3 && !it_1 && !it_2)
        printf(BOLD_RED);

    if (it_1 && it_2 && !it_3)
        printf(rand() % 60 % 2 == 0 ? BLACK : BOLD_YELLOW);

    if (it_1 && !it_2 && it_3)
        printf(rand() % 60 % 2 == 0 ? BOLD_RED : BLACK);

    if (it_1 && it_2 && it_3)
    {
        int rnd = rand() % 76;

        if (rnd % 2 == 0)
            printf(BOLD_YELLOW);

        else if (rnd % 3 == 0)
            printf(BLACK);

        else printf(BOLD_RED);
    }

}
void printShape(int len)
{
    printColor();
    int a, b, size = len;
    for (a = size / 2; a <= size; a += 2)
    {
        for(b = 1; b < size - a; b += 2)
            printf(" ");
        for (b = 1; b <= a; b++)
        {
            printColor();
            printf("%c", LETTER);
        }
        for (b = 1; b <= size - a; b++)
            printf(" ");

        for (b = 1; b <= a - 1; b++)
        {
            printColor();
            printf("%c", LETTER);
        }
        printf("\n");
    }

    for (a = size; a >= 0; a--)
    {
        for (b = a; b < size; ++b)
            printf(" ");
        for (b = 1; b <= ((a * 2) - 1); b++)
        {
            printColor();
            printf("%c", LETTER);
        }

        printf("\n");
    }
}
void clearScreen()
{
    system("clear");
}

void waitXSecond(int sec)
{
    sleep(sec);
}
/*
void randomCreate()
{
    clearScreen();

    //it 1
    LETTER = msg[0];
    it_1 = true;
    printShape();

    waitXSecond(1);
    clearScreen();

    //it 2
    LETTER = msg[1];
    it_2 = true;
    printShape();

    waitXSecond(1);
    clearScreen();

    //it 3
    LETTER = msg[2];
    it_3 = true;
    printShape();

    waitXSecond(1);
    clearScreen();

    //it4
    LETTER = msg[3];
    it_2 = false;
    printShape();

    waitXSecond(1);
    clearScreen();


    //it 5
    LETTER = msg[4];
    it_1 = false;
    it_2 = false;
    printShape();

}
*/
void printRedName(int heartLength)
{
    it_1 = false;
    it_2 = false;
    it_3 = true;
    unsigned int size = strlen(msg);
    for (int i = 0; i < size; ++i)
    {
        LETTER = msg[i];
        printShape(heartLength);
        waitXSecond(1);
        if (i != size - 1)
            clearScreen();
    }
    printf("\t\t\t [%s] \n", msg);
   // printf("\t\t\t  C Lang.\n");
}
void printWrongArguments()
{
    fprintf(stderr, "Your arguments are wrong!!!\n");
    exit(EXIT_FAILURE);
}

int main(int argc, char **argv)
{
    if (argc != 4)
    {
        fprintf(stderr, "Wrong Number Of Arguments!\n");
        exit(EXIT_FAILURE);
    }


    srand(time(NULL));

    char *str = argv[1];

    char *name = argv[2];

    char *heartSize = argv[3];
    int h_s = atoi(heartSize);

    unsigned int size = strlen(name);

    msg = calloc(size, sizeof(char));
    char *s = "N U R I";
    memcpy(&msg, &name, sizeof(name));

    if (strcmp(str, "red") == 0)
       printRedName(h_s);
    return 0;
}

